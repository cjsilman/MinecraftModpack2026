package dev.csilman.modpackutils.item.custom;

import dev.csilman.modpackutils.block.custom.BeaconPedestalBlock;
import dev.csilman.modpackutils.component.MemoryDestination;
import dev.csilman.modpackutils.component.ModDataComponentTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Set;

public class FragmentedMemoryItem extends Item {

    public FragmentedMemoryItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        if (level.isClientSide()) return InteractionResult.SUCCESS;

        BlockPos pos = context.getClickedPos();

        if (!(level.getBlockState(pos).getBlock() instanceof BeaconPedestalBlock beaconPedestalBlock)) {
            return InteractionResult.PASS;
        }

        Player player = context.getPlayer();
        ItemStack stack = context.getItemInHand();

        return beaconPedestalBlock.tryTeleport(player, stack, context);
    }
}
