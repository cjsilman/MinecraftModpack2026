package dev.csilman.modpackutils.block.custom;

import com.mojang.serialization.MapCodec;
import dev.csilman.modpackutils.block.ModBlocks;
import dev.csilman.modpackutils.block.entity.ModBlockEntities;
import dev.csilman.modpackutils.block.entity.custom.BossBeaconEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.neoforged.neoforge.registries.DeferredBlock;
import org.jetbrains.annotations.Nullable;

public class BossBeaconBlock extends BaseEntityBlock {
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");
    public static final MapCodec<BossBeaconBlock> CODEC = simpleCodec(BossBeaconBlock::new);
    private final int beamColor;
    private final DeferredBlock<Block> activatingBlock;
    private final Holder<MobEffect> aoeEffect;

    public BossBeaconBlock(Properties properties) {
        super(properties);
        this.beamColor = 0;
        this.activatingBlock = ModBlocks.ABYSS_BEACON_PEDESTAL_BLOCK;
        this.aoeEffect = MobEffects.REGENERATION;
        registerDefaultState(this.stateDefinition.any().setValue(ACTIVE, false));
    }

    public BossBeaconBlock(Properties properties, int beamColor, DeferredBlock<Block> activatingBlock, Holder<MobEffect> aoeMobEffect) {
        super(properties);
        this.beamColor = beamColor;
        this.activatingBlock = activatingBlock;
        this.aoeEffect = aoeMobEffect;
        registerDefaultState(this.stateDefinition.any().setValue(ACTIVE, false));
    }

    public int getBeamColor() {
        return beamColor;
    }

    public DeferredBlock<Block> getActivatingBlock() {
        return activatingBlock;
    }

    public Holder<MobEffect> getAoeEffect() {
        return aoeEffect;
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ACTIVE);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new BossBeaconEntity(blockPos, blockState);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if(level.isClientSide()) return null;
        return createTickerHelper(blockEntityType, ModBlockEntities.BOSS_BEACON_BE.get(),
                BossBeaconEntity::tick);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
        if (!level.isClientSide()) {
            level.getBlockEntity(pos, ModBlockEntities.BOSS_BEACON_BE.get()).ifPresent(BossBeaconEntity::checkPrerequisite);
        }
    }
}
