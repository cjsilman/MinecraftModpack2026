package dev.csilman.modpackutils.util.altar;

import dev.csilman.modpackutils.ModpackUtilsMod;
import dev.csilman.modpackutils.data.AltarSavedData;
import dev.csilman.modpackutils.util.altar.siege.SiegeWave;
import dev.csilman.modpackutils.util.altar.siege.SiegeWaveDefinitions;
import dev.csilman.modpackutils.util.altar.siege.SiegeWaveSpawner;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetSubtitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitlesAnimationPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;

import java.util.List;

public class AltarEventManager {

    private static final int TICKS_TO_SIEGE = 200;
    private static final int SIEGE_ASSIST_TIME = 400;

    public static void tick(ServerLevel overworld) {
        AltarSavedData data = AltarSavedData.get(overworld);

        if (data.isDormant() || data.isDefeated()) return;

        data.incrementTicks();

        switch(data.getPhase()) {
            case AWAKENING -> tickAwakening(overworld, data);
            case SIEGE -> tickSiege(overworld, data);
            default -> {}
        }

    }

    private static void tickAwakening(ServerLevel level, AltarSavedData data) {
        int currentTick = data.getTicksInPhase();
        BlockPos altarMidpoint = data.getAltarMidpoint();

        if (currentTick == 40) {
            spawnRing(level, altarMidpoint, 5, ParticleTypes.REVERSE_PORTAL, 5.0);
        }

        if (currentTick == 80) {
            spawnRing(level, altarMidpoint, 10, ParticleTypes.DRAGON_BREATH,5.0);
            spawnRing(level, altarMidpoint, 5, ParticleTypes.REVERSE_PORTAL,5.0);
            spawnRingForAllServerPlayers(level, 3, ParticleTypes.END_ROD);
        }

        if (currentTick == 160) {
            broadcastTitle(level,
                    Component.translatable("info.modpack_utils.altar_event.awakening_start_title"),
                    Component.translatable("info.modpack_utils.altar_event.awakening_start_subtitle")
                    );
            broadcastSound(level, SoundEvents.WARDEN_AGITATED, 0.75f);
        }


        if (currentTick >= TICKS_TO_SIEGE) {
            data.setPhase(AltarEventPhase.SIEGE);
            ModpackUtilsMod.LOGGER.info("[ModpackUtils] Altar advancing to SIEGE phase.");
        }


    }

    private static void tickSiege(ServerLevel level, AltarSavedData data) {
        int currentTick = data.getTicksInPhase();
        int currentWave = data.getSiegeWave();

        if (!data.isWaveSpawned()) {
            SiegeWave wave = SiegeWaveDefinitions.getWave(currentWave);
            SiegeWaveSpawner.spawnWave(level, data.getAltarMidpoint(), wave);
            data.setWaveSpawned(true);

            ModpackUtilsMod.LOGGER.info(
                    "[ModpackUtils] Spawning siege wave {}/{}",
                    currentWave+1,
                    SiegeWaveDefinitions.totalWaves()
            );
            return;
        }

        if (currentTick % 20 != 0) return; //Every 1 second



        if (!SiegeWaveSpawner.isWaveCleared(level, data.getAltarMidpoint())) {
            // See if player needs assistance...
            if (currentTick > SIEGE_ASSIST_TIME) {
                SiegeWaveSpawner.highlightAllMobs(level, data.getAltarMidpoint());
            }

            return;
        }

        int nextWave = currentWave + 1;

        if (nextWave < SiegeWaveDefinitions.totalWaves()){
            broadcastTitle(level,
                    Component.literal("§a§lWave Cleared!"),
                    Component.literal("§7Prepare yourselves..."));
            broadcastSound(level, SoundEvents.WARDEN_ANGRY, 0.75f);

            level.getServer().execute(() -> {
                data.setSiegeWave(nextWave);
                data.setWaveSpawned(false);
                data.setPhase(AltarEventPhase.SIEGE);
            });
        } else {
            // All waves cleared
            broadcastTitle(level,
                    Component.literal(""),
                    Component.literal("It is free..."));
            broadcastSound(level, SoundEvents.ENDER_DRAGON_GROWL, 0.75f);

            data.setPhase(AltarEventPhase.BOSS);

            ModpackUtilsMod.LOGGER.info("[ModpackUtils] All waves cleared. Advancing to BOSS phase.");
        }

    }

    public static void broadcastTitle(ServerLevel overworld, Component title, Component subtitle) {
        List<ServerPlayer> players = overworld.getServer().getPlayerList().getPlayers();

        for (ServerPlayer player : players) {
            player.connection.send(
                    new ClientboundSetTitleTextPacket(title)
            );

            player.connection.send(
                    new ClientboundSetSubtitleTextPacket(subtitle)
            );

            player.connection.send(
                    new ClientboundSetTitlesAnimationPacket(
                            10, 70, 20
                    )
            );

        }
    }

    public static void broadcastSound(ServerLevel level, SoundEvent soundEvent, float pitch) {
        List<ServerPlayer> players = level.getServer().getPlayerList().getPlayers();
        for (ServerPlayer player : players) {
            player.level().playSound(null, player.blockPosition(), soundEvent, SoundSource.AMBIENT, 1.0f, pitch);
        }
    }

    public static void spawnRing(ServerLevel level, BlockPos pos, double radius, SimpleParticleType particle, double yOffset) {
        int particlesPerRing = 320;
        for (int i = 0; i < particlesPerRing; i++) {
            double angle = (2 * Math.PI / particlesPerRing) * i;
            double x = pos.getX() + 0.5 + radius * Math.cos(angle);
            double y = pos.getY() + yOffset;
            double z = pos.getZ() + 0.5 + radius * Math.sin(angle);
            level.sendParticles(particle, x, y, z, 3, 0, 0, 0, 0.02);
        }
    }

    public static void spawnRingForAllServerPlayers(ServerLevel level, double radius, SimpleParticleType particle) {
        List<ServerPlayer> players = level.getServer().getPlayerList().getPlayers();
        for (ServerPlayer player : players) {
            spawnRing(level, player.blockPosition(), radius, particle, 0.5);
        }
    }

}
