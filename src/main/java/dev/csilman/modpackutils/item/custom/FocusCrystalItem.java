package dev.csilman.modpackutils.item.custom;

import dev.csilman.modpackutils.item.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

import java.util.List;

public class FocusCrystalItem extends Item {

    public FocusCrystalItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("tooltip.modpack_utils.focus_crystal.tooltip.1").withStyle(ChatFormatting.LIGHT_PURPLE));

        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        if (level.isClientSide()) return InteractionResult.SUCCESS;

        if (level.dimension() == Level.END)
        {
            Player player = context.getPlayer();
            ItemStack stack = context.getItemInHand();
            BlockPos pos = context.getClickedPos();

            if (stack.is(ModItems.FOCUS_CRYSTAL) &&
                    (level.getBlockState(pos).getBlock().equals(Blocks.BEDROCK))) {

                if (!player.isCreative()) {
                    stack.shrink(1);
                }

                ItemStack result = new ItemStack(ModItems.TUNED_CRYSTAL.get());

                if (!player.getInventory().add(result)) {
                    player.drop(result, false);
                }

                level.playSound(null, pos,
                        net.minecraft.sounds.SoundEvents.AMETHYST_BLOCK_CHIME,
                        net.minecraft.sounds.SoundSource.PLAYERS,
                        1.0f, 1.2f);

                player.displayClientMessage(
                        Component.translatable("tooltip.modpack_utils.focus_crystal.tuned"),
                        true
                );
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }
}
