package dev.csilman.modpackutils.block.custom;

import dev.csilman.modpackutils.item.ModItems;
import dev.csilman.modpackutils.util.ModTags;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.util.SpawnUtil;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class SacredStoneBlock extends Block {
    public SacredStoneBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        if (level.isClientSide()) return;

        if (!level.getBlockTicks().hasScheduledTick(pos, this)) {
            if (entity instanceof ItemEntity itemEntity) {
                if (isValidItem(itemEntity.getItem())) {
                    level.scheduleTick(pos, this, 70);

                    // Effects
                    double radius = 16.0;
                    List<ServerPlayer> nearbyPlayers = getNearbyPlayers(level, pos, radius);

                    level.playSound(
                            null,
                            pos,
                            SoundEvents.WITHER_SPAWN,
                            SoundSource.BLOCKS,
                            1.0f,
                            0.5f
                    );

                    for (ServerPlayer player : nearbyPlayers) {
                        player.addEffect(new MobEffectInstance(
                                MobEffects.BLINDNESS,
                                75,
                                0,
                                false,
                                true
                        ));

                        player.addEffect(new MobEffectInstance(
                                MobEffects.DARKNESS,
                                75,
                                0,
                                false,
                                true
                        ));
                    }
                }
            }
        }

        super.stepOn(level, pos, state, entity);
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {

        double radius = 16.0;
        List<ServerPlayer> nearbyPlayers = getNearbyPlayers(level, pos, radius);

        level.getEntitiesOfClass(ItemEntity.class, new AABB(pos).inflate(2))
                .forEach(itemEntity -> {
                    if (isValidItem(itemEntity.getItem())) {
                        itemEntity.setItem(new ItemStack(ModItems.GOD_THREAD.get(), itemEntity.getItem().getCount()));
                    }
                });

        level.sendParticles(
                ParticleTypes.GLOW,
                pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5,
                200,     // count
                0.8, 0.8, 0.8,  // spread
                0.08    // speed
        );

        LightningBolt lightning = new LightningBolt(EntityType.LIGHTNING_BOLT, level);
        lightning.setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
        lightning.setVisualOnly(true);
        level.addFreshEntity(lightning);

        if (level.random.nextFloat() < 0.05f) { // 5% chance
            SpawnUtil.trySpawnMob(EntityType.WARDEN, MobSpawnType.TRIGGERED, level, pos, 20, 5, 6, SpawnUtil.Strategy.ON_TOP_OF_COLLIDER);
            for (ServerPlayer player : nearbyPlayers) {
                player.displayClientMessage(
                        Component.translatable("info.modpack_utils.sacred_stone.spawn_warning")
                                .withStyle(ChatFormatting.DARK_RED),
                        true
                );
            }
        }

        super.tick(state, level, pos, random);
    }

    private List<ServerPlayer> getNearbyPlayers(Level level, BlockPos startPosition, double searchRadius) {
        List<ServerPlayer> nearbyPlayers = level.getEntitiesOfClass(
                ServerPlayer.class,
                new AABB(startPosition).inflate(searchRadius)
        );

        return nearbyPlayers;
    }

    private boolean isValidItem(ItemStack item) {
        return item.is(ModTags.Items.TRANSFORMABLE_ITEMS);
    }
}
