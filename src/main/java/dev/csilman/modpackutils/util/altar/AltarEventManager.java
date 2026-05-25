package dev.csilman.modpackutils.util.altar;

import dev.csilman.modpackutils.ModpackUtilsMod;
import dev.csilman.modpackutils.block.ModBlocks;
import dev.csilman.modpackutils.data.AltarSavedData;
import dev.csilman.modpackutils.util.altar.siege.SiegePhase;
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
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;

import java.util.List;

public class AltarEventManager {

    private static final int TICKS_TO_SIEGE = 400;
    private static final int SIEGE_ASSIST_TIME = 400;
    private static final int TIME_TO_START_NEXT_WAVE = 100;

    public static void tick(ServerLevel overworld) {
        AltarSavedData data = AltarSavedData.get(overworld);

        if (data.isDormant() || data.isDefeated()) return;

        data.incrementPhaseTicks();

        switch(data.getAltarPhase()) {
            case AWAKENING -> tickAwakening(overworld, data);
            case SIEGE -> tickSiege(overworld, data);
            case BOSS -> tickBoss(overworld, data);
            default -> {}
        }

    }

    private static void tickAwakening(ServerLevel level, AltarSavedData data) {
        int currentTick = data.getTicksInPhase();

        if (currentTick == 40) {
            spawnRing(level, data.getAltarMidpoint(), 5, ParticleTypes.REVERSE_PORTAL, 5.0);
        }

        if (currentTick == 80) {
            spawnRing(level, data.getAltarMidpoint(), 10, ParticleTypes.DRAGON_BREATH,5.0);
            spawnRing(level, data.getAltarMidpoint(), 5, ParticleTypes.REVERSE_PORTAL,5.0);
            spawnRingForAllServerPlayers(level, 3, ParticleTypes.END_ROD);
            AltarWeatherManager.startSiegeWeather(level);
        }

        if (currentTick == 160) {
            broadcastTitle(level,
                    Component.translatable("info.modpack_utils.altar_event.awakening_start_title"),
                    Component.translatable("info.modpack_utils.altar_event.awakening_start_subtitle")
                    );
            broadcastSound(level, SoundEvents.WARDEN_AGITATED, 0.75f);
        }


        if (currentTick >= TICKS_TO_SIEGE) {
            data.setAltarPhase(AltarEventPhase.SIEGE);
            ModpackUtilsMod.LOGGER.info("[ModpackUtils] Altar advancing to SIEGE phase.");
            data.setTicksInPhase(0);
        }


    }

    private static void tickSiege(ServerLevel level, AltarSavedData data) {
        int currentTick = data.getTicksInPhase();
        int currentWave = data.getSiegeWave();
        int currentWaveTick = data.getTicksInWave();
        int dataDelayWaveStartCounter = data.getDelayWaveStartCounter();
        SiegePhase currentSiegePhase = data.getSiegePhase();

        if (currentSiegePhase == SiegePhase.STARTING_WAVE) {
            data.setDelayWaveStartCounter(dataDelayWaveStartCounter+1);
        } else {
            data.setDelayWaveStartCounter(0);
        }

        if (currentSiegePhase == SiegePhase.IN_WAVE) {
            data.setTicksInWave(data.getTicksInWave()+1);
        } else {
            data.setTicksInWave(0);
        }

        //------------------
        // Every 1 Second
        //------------------
        if (currentTick % 20 == 0) {
            if (currentSiegePhase == SiegePhase.NONE) {
                // First time entering siege event
                ModpackUtilsMod.LOGGER.info("[ModpackUtils] Siege Phase NONE -> STARTING_WAVE");
                data.setSiegePhase(SiegePhase.STARTING_WAVE);
                return;
            }

            if (currentSiegePhase == SiegePhase.STARTING_WAVE) {
                if (dataDelayWaveStartCounter > TIME_TO_START_NEXT_WAVE)
                {
                    startSiegeWave(level, data, currentWave);
                    ModpackUtilsMod.LOGGER.info("[ModpackUtils] Siege Phase STARTING_WAVE -> IN_WAVE");
                    data.setSiegePhase(SiegePhase.IN_WAVE);
                    return;
                }
            }

            if (currentSiegePhase == SiegePhase.IN_WAVE) {
                if (SiegeWaveSpawner.isWaveCleared(level, data.getAltarMidpoint())) {
                    // No mobs currently exist
                    ModpackUtilsMod.LOGGER.info("[ModpackUtils] Siege Phase IN_WAVE -> ENDING_WAVE");
                    data.setSiegePhase(SiegePhase.ENDING_WAVE);
                } else {
                    // If wave is not cleared, check if player(s) need assistance...
                    if (currentWaveTick > SIEGE_ASSIST_TIME && !data.isMobsHighlighted()) {
                        ModpackUtilsMod.LOGGER.info("[ModpackUtils] Highlighting all mobs");
                        SiegeWaveSpawner.highlightAllMobs(level, data.getAltarMidpoint( ));
                        data.setMobsHighlighted(true);
                    }
                }
            }

            if (currentSiegePhase == SiegePhase.ENDING_WAVE) {
                int nextWave = currentWave + 1;

                if (nextWave < SiegeWaveDefinitions.totalWaves()){
                    // More waves to go
                    ModpackUtilsMod.LOGGER.info("[ModpackUtils] Siege Phase ENDING_WAVE -> STARTING_WAVE");
                    data.setSiegePhase(SiegePhase.STARTING_WAVE);

                    broadcastSound(level, SoundEvents.WARDEN_ANGRY, 0.75f);

                    level.getServer().execute(() -> {
                        data.setSiegeWave(nextWave);
                        data.setWaveSpawned(false);
                        data.setMobsHighlighted(false);
                        data.setAltarPhase(AltarEventPhase.SIEGE);
                    });
                } else {
                    // All waves cleared
                    ModpackUtilsMod.LOGGER.info("[ModpackUtils] Siege Phase ENDING_WAVE -> COMPLETED_SIEGE");
                    data.setMobsHighlighted(false);
                    data.setSiegePhase(SiegePhase.COMPLETED_SIEGE);
                }
            }

            if (currentSiegePhase == SiegePhase.COMPLETED_SIEGE) {
                broadcastTitle(level,
                        Component.literal(""),
                        Component.literal("Complete."));

                data.setAltarPhase(AltarEventPhase.BOSS);
                data.setTicksInPhase(0);
                AltarWeatherManager.resetWeather(level);

                ModpackUtilsMod.LOGGER.info("[ModpackUtils] Siege completed. Advancing to BOSS phase.");
            }


        }

        //------------------
        // Every 2 Seconds
        //------------------
        if (currentTick % 40 == 0) {
            int stage = data.getSiegeParticleStage();

            if (stage+1 > 4) {
                data.setSiegeParticleStage(0);
            } else {
                data.setSiegeParticleStage(stage+1);
            }

            spawnRingStage(stage, level, data);
        }


        //------------------
        // Every 10 Seconds
        //------------------
        if (currentTick % 200 == 0) {
            summonLightning(level, data.getAltarMidpoint(), -25, 25);
        }

        if (currentTick % 205 == 0) {
            summonLightning(level, data.getAltarMidpoint(), -25, 25);
        }

    }

    private static void tickBoss(ServerLevel level, AltarSavedData data) {
        int currentTick = data.getTicksInPhase();

        if (currentTick == 100) {
            broadcastTitle(level,
                    Component.literal(""),
                    Component.literal("Proceed..."));

            BlockPos altarMidpoint = data.getAltarMidpoint();
            level.setBlock(altarMidpoint, ModBlocks.JUDGEMENT_BEACON_PEDESTAL_BLOCK.get().defaultBlockState(), 3);

            broadcastSound(level, SoundEvents.END_PORTAL_SPAWN, 0.5f);

            data.setAltarPhase(AltarEventPhase.DEFEATED);
            data.setTicksInPhase(0);
        }
    }

    private static void startSiegeWave(ServerLevel level, AltarSavedData data, int waveNumber) {
        if (!data.isWaveSpawned()) {
            SiegeWave wave = SiegeWaveDefinitions.getWave(waveNumber);
            SiegeWaveSpawner.spawnWave(level, data.getAltarMidpoint(), wave);
            data.setWaveSpawned(true);
            data.setTicksInWave(0);

            ModpackUtilsMod.LOGGER.info(
                    "[ModpackUtils] Spawning siege wave {}/{}",
                    waveNumber+1,
                    SiegeWaveDefinitions.totalWaves()
            );
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

    public static void spawnRingStage(int stage, ServerLevel level, AltarSavedData data) {
        switch(stage) {
            case 0:
                spawnRing(level, data.getAltarMidpoint(), 4, ParticleTypes.DRAGON_BREATH,10.0);
                break;
            case 1:
                spawnRing(level, data.getAltarMidpoint(), 8, ParticleTypes.DRAGON_BREATH,10.0);
                break;
            case 2:
                spawnRing(level, data.getAltarMidpoint(), 12, ParticleTypes.DRAGON_BREATH,10.0);
                break;
            case 3:
                spawnRing(level, data.getAltarMidpoint(), 16, ParticleTypes.DRAGON_BREATH,10.0);
                break;
            case 4:
                spawnRing(level, data.getAltarMidpoint(), 20, ParticleTypes.DRAGON_BREATH,10.0);
                break;
            default:
                break;
        }
    }

    public static void spawnRingForAllServerPlayers(ServerLevel level, double radius, SimpleParticleType particle) {
        List<ServerPlayer> players = level.getServer().getPlayerList().getPlayers();
        for (ServerPlayer player : players) {
            spawnRing(level, player.blockPosition(), radius, particle, 0.5);
        }
    }

    public static void summonLightning(ServerLevel level, BlockPos pos, int rangeMin, int rangeMax) {
        int x_adjust = level.random.nextIntBetweenInclusive(rangeMin, rangeMax);
        int z_adjust = level.random.nextIntBetweenInclusive(rangeMin, rangeMax);
        int x_negative = (level.random.nextIntBetweenInclusive(0, 1) == 0) ? 1 : -1;
        int z_negative = (level.random.nextIntBetweenInclusive(0, 1) == 0) ? 1 : -1;

        int pos_x = x_negative*(pos.getX() + x_adjust);
        int pos_z = z_negative*(pos.getZ() + z_adjust);

        LightningBolt lightning = new LightningBolt(EntityType.LIGHTNING_BOLT, level);
        lightning.setPos(pos_x, pos.getY(), pos_z);
        lightning.setVisualOnly(true);
        level.addFreshEntity(lightning);

        ModpackUtilsMod.LOGGER.info("[ModpackUtils] Summon lightning x: {}, y: {}, z: {}", pos_x, pos.getY(), pos_z);

    }

    public static int getNumberOfPlayersOnServer(ServerLevel level) {
        List<ServerPlayer> players = level.getServer().getPlayerList().getPlayers();
        return players.size();
    }

}
