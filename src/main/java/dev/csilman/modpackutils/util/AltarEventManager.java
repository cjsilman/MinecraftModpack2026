package dev.csilman.modpackutils.util;

import dev.csilman.modpackutils.ModpackUtilsMod;
import dev.csilman.modpackutils.data.AltarSavedData;
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

    private static void tickAwakening(ServerLevel overworld, AltarSavedData data) {
        int currentTick = data.getTicksInPhase();
        BlockPos altarMidpoint = data.getAltarMidpoint();

        if (currentTick == 40) {
            spawnRing(overworld, altarMidpoint, 5, ParticleTypes.REVERSE_PORTAL, 5.0);
        }

        if (currentTick == 80) {
            spawnRing(overworld, altarMidpoint, 10, ParticleTypes.DRAGON_BREATH,5.0);
            spawnRing(overworld, altarMidpoint, 5, ParticleTypes.REVERSE_PORTAL,5.0);
            spawnRingForAllServerPlayers(overworld, 3, ParticleTypes.END_ROD);
        }

        if (currentTick == 160) {
            broadcastTitle(overworld,
                    Component.translatable("info.modpack_utils.altar_event.awakening_start_title"),
                    Component.translatable("info.modpack_utils.altar_event.awakening_start_subtitle")
                    );
            broadcastSound(overworld, SoundEvents.WARDEN_AGITATED, 0.75f);
        }


        if (currentTick >= TICKS_TO_SIEGE) {
            data.setPhase(AltarEventPhase.SIEGE);
            ModpackUtilsMod.LOGGER.info("[ModpackUtils] Altar advancing to SIEGE phase.");
        }


    }

    private static void tickSiege(ServerLevel overworld, AltarSavedData data) {
        int currentTick = data.getTicksInPhase();
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
            int playerX = player.getBlockX();
            int playerY = player.getBlockY();
            int playerZ = player.getBlockZ();
            BlockPos playerPos = new BlockPos(playerX, playerY, playerZ);
            player.level().playSound(null, playerPos, soundEvent, SoundSource.AMBIENT, 1.0f, pitch);
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
            int playerX = player.getBlockX();
            int playerY = player.getBlockY();
            int playerZ = player.getBlockZ();
            BlockPos playerPos = new BlockPos(playerX, playerY, playerZ);
            spawnRing(level, playerPos, radius, particle, 0.5);
        }
    }

}
