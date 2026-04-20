package dev.csilman.modpackutils.util.altar.siege;

import dev.csilman.modpackutils.ModpackUtilsMod;
import dev.csilman.modpackutils.util.altar.AltarEventManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.AABB;

import java.util.List;
import java.util.Random;

public class SiegeWaveSpawner {
    private static final double SPAWN_RADIUS_MIN = 8.0;
    private static final double SPAWN_RADIUS_MAX = 15.0;

    public static final String SIEGE_MOB_TAG = "altar_siege_mob";

    private static final double SCAN_RADIUS = 128.0;

    private static final Random RANDOM = new Random();

    private int playerScaleFactor = 1; //default to 1


    public static void spawnWave(ServerLevel level, BlockPos pos, SiegeWave wave) {
        AltarEventManager.broadcastTitle(
                level,
                Component.literal(wave.title()),
                Component.literal(wave.subtitle()
                ));

        for (SiegeWave.WaveEntry entry : wave.entries()) {
            for(int i = 0; i < entry.count(); i++) {
                spawnSiegeMob(level, pos, entry);
            }
        }
    }

    private static double findSafeY(ServerLevel level, EntityType<?> entity, double x, int startY, double z) {
        BlockPos.MutableBlockPos check = new BlockPos.MutableBlockPos();
        for (int y = startY + 5; y > startY-10; y--) {
            check.set((int) x, y, (int) z);
            if (level.getBlockState(check).isValidSpawn(level, check, entity) && level.getBlockState(check.above()).isAir()) {
                return y+1;
            }
        }
        return startY;
    }

    private static void spawnSiegeMob(ServerLevel level, BlockPos pos, SiegeWave.WaveEntry entry) {

        ResourceLocation entityId = ResourceLocation.parse(entry.entityRegistryName());
        EntityType<?> entityType = BuiltInRegistries.ENTITY_TYPE.get(entityId);

        if (entityType == null || entityType == EntityType.PIG) {
            ModpackUtilsMod.LOGGER.warn(
                    "[ModpackUtils] Skipping unknown entity type: {}", entry.entityRegistryName()
            );
            return;
        }

        double angle = RANDOM.nextDouble() * 2 * Math.PI;
        double radius = SPAWN_RADIUS_MIN + RANDOM.nextDouble() * (SPAWN_RADIUS_MAX - SPAWN_RADIUS_MIN);

        double x = pos.getX() + radius * Math.cos(angle);
        double z = pos.getZ() + radius * Math.sin(angle);
        double y = findSafeY(level, entityType, x, pos.getY(), z);

        Entity entity = entityType.create(level);
        if(!(entity instanceof Mob mob)) return;

        mob.moveTo(x, y, z, RANDOM.nextFloat() * 360f, 0f);
        mob.addTag(SIEGE_MOB_TAG);

        if (entry.customName() != null) {
            mob.setCustomName(Component.literal(entry.customName()));
            mob.setCustomNameVisible(true);
        }

        applyWaveBuff(mob);

        level.addFreshEntity(mob);
        mob.finalizeSpawn(level, level.getCurrentDifficultyAt(mob.blockPosition()),
                MobSpawnType.EVENT, null);

    }

    private static void applyWaveBuff(Mob mob) {
        if (mob.getAttribute(Attributes.MAX_HEALTH) != null) {
            mob.getAttribute(Attributes.MAX_HEALTH).setBaseValue(mob.getMaxHealth() * 2.0);
            mob.setHealth(mob.getMaxHealth());
        }

        if (mob.getAttribute(Attributes.MOVEMENT_SPEED) != null) {
            mob.getAttribute(Attributes.MOVEMENT_SPEED)
                    .setBaseValue(mob.getAttributeValue(Attributes.MOVEMENT_SPEED) * 1.3);
        }

        mob.addEffect(new MobEffectInstance(
                MobEffects.DAMAGE_BOOST, Integer.MAX_VALUE, 1, false, false));
    }

    public static boolean isWaveCleared(ServerLevel level, BlockPos pos) {
        AABB scanArea = new AABB(pos).inflate(SCAN_RADIUS);

        List<LivingEntity> remaining = level.getEntitiesOfClass(
                LivingEntity.class,
                scanArea,
                entity -> entity.getTags().contains(SIEGE_MOB_TAG) && entity.isAlive()
        );

        return remaining.isEmpty();
    }

    public static void highlightAllMobs(ServerLevel level, BlockPos pos) {
        AABB scanArea = new AABB(pos).inflate(SCAN_RADIUS);

        List<LivingEntity> remaining = level.getEntitiesOfClass(
                LivingEntity.class,
                scanArea,
                entity -> entity.getTags().contains(SIEGE_MOB_TAG) && entity.isAlive()
        );

        for (LivingEntity entity : remaining) {
            entity.addEffect(
                    new MobEffectInstance(
                            MobEffects.GLOWING,
                            Integer.MAX_VALUE,
                            1,
                            false,
                            false
                    )
            );
        }
    }

}
