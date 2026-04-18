package dev.csilman.modpackutils.block.custom;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.csilman.modpackutils.component.MemoryDestination;
import dev.csilman.modpackutils.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class BeaconPedestalBlock extends HorizontalDirectionalBlock {

    public static final MapCodec<BeaconPedestalBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            propertiesCodec(),
            MemoryDestination.CODEC.fieldOf("destination").forGetter(b -> b.destination)
    ).apply(instance, BeaconPedestalBlock::new));

    private final MemoryDestination destination;

    public BeaconPedestalBlock(Properties properties, MemoryDestination destination) {
        super(properties);
        this.destination = destination;
    }

    public MemoryDestination getDestination() {
        return destination;
    }

    public InteractionResult tryTeleport(Player player, ItemStack stack, UseOnContext context) {
        if (destination == null) return InteractionResult.FAIL;

        if (stack.getCount() < destination.requiredAmount()) {
            player.displayClientMessage(
                    Component.translatable("info.modpack_utils.fragmented_memory.not_enough"),
                    true
            );
            return InteractionResult.FAIL;
        }

        MinecraftServer server = player.getServer();

        ServerLevel targetLevel = server.getLevel(destination.dimension());

        if (targetLevel == null) return InteractionResult.FAIL;

        if (!player.isCreative()) {
            stack.shrink(destination.requiredAmount());
        }

        context.getLevel().playSound(null, context.getPlayer().blockPosition(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0f, 1.0f);

        ((ServerPlayer) player).teleportTo(
                targetLevel,
                destination.x(), destination.y(), destination.z(),
                Set.of(),
                player.getYRot(),
                player.getXRot()
        );

        player.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 200));

        return InteractionResult.SUCCESS;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        ItemStack item = player.getMainHandItem();

        if (!item.is(ModItems.FRAGMENTED_MEMORY)){
            player.displayClientMessage(
                    Component.translatable("info.modpack_utils.beacon_pedestal.empty_interact"),
                    true
            );
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
}
