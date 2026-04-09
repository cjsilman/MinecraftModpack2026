package dev.csilman.modpackutils.block.entity.custom;

import dev.csilman.modpackutils.block.ModBlocks;
import dev.csilman.modpackutils.block.custom.BossBeaconBlock;
import dev.csilman.modpackutils.block.entity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BossBeaconEntity extends BlockEntity {
    private static final int EFFECT_INTERVAL = 40;
    private static final int EFFECT_RADIUS = 16;

    private boolean active = false;
    private int tickCounter = 0;


    public BossBeaconEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.BOSS_BEACON_BE.get(), pos, blockState);
    }

    public boolean isActive() {
        return active;
    }

    public void checkPrerequisite() {
        if (level == null) {
            return;
        }

        BlockPos belowPos = worldPosition.below();
        BlockState below = level.getBlockState(belowPos);

        boolean shouldBeActive = below.is(ModBlocks.BLACK_OPAL_BLOCK);

        if (shouldBeActive != this.active) {
            if (shouldBeActive) {
                level.playSound(
                        null,
                        worldPosition,
                        SoundEvents.END_PORTAL_SPAWN,
                        SoundSource.BLOCKS,
                        1.0f,
                        1.0f
                );
            }
            else {
                level.playSound(
                        null,
                        worldPosition,
                        SoundEvents.BEACON_DEACTIVATE,
                        SoundSource.BLOCKS,
                        1.0f,
                        1.0f
                );
            }
            this.active = shouldBeActive;
            level.setBlock(worldPosition, getBlockState().setValue(BossBeaconBlock.ACTIVE, active), 3);
            setChanged();
        }


    }

    public static void tick(Level level, BlockPos pos, BlockState state, BossBeaconEntity entity) {
        if (level.isClientSide()) {
            return;
        }

        entity.tickCounter++;
        if (entity.tickCounter >= EFFECT_INTERVAL) {
            entity.tickCounter = 0;
            entity.checkPrerequisite();

            if (entity.active) {
                entity.applyEffects();
            }
        }
    }

    private void applyEffects() {
        if (level == null) {
            return;
        }

        AABB area = new AABB(worldPosition).inflate(EFFECT_RADIUS);
        List<Player> players = level.getEntitiesOfClass(Player.class, area);

        for (Player player : players) {
            player.addEffect(new MobEffectInstance(
                    MobEffects.REGENERATION,
                    EFFECT_INTERVAL + 20,
                    0,
                    true,
                    false,
                    true
            ));
        }
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putBoolean("active", active);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        active = tag.getBoolean("active");
    }
}
