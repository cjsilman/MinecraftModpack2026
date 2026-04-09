package dev.csilman.modpackutils.block.custom;

import com.mojang.serialization.MapCodec;
import dev.csilman.modpackutils.block.entity.ModBlockEntities;
import dev.csilman.modpackutils.block.entity.custom.BossBeaconEntity;
import net.minecraft.core.BlockPos;
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
import org.jetbrains.annotations.Nullable;

public class BossBeaconBlock extends BaseEntityBlock {
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");
    public static final MapCodec<BossBeaconBlock> CODEC = simpleCodec(BossBeaconBlock::new);
    private final int beamColor;

    public BossBeaconBlock(Properties properties) {
        super(properties);
        beamColor = 0;
        registerDefaultState(this.stateDefinition.any().setValue(ACTIVE, false));
    }

    public BossBeaconBlock(Properties properties, int beamColor) {
        super(properties);
        this.beamColor = beamColor;
        registerDefaultState(this.stateDefinition.any().setValue(ACTIVE, false));
    }

    public int getBeamColor() {
        return beamColor;
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
