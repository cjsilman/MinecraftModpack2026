package dev.csilman.modpackutils.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class PrimordialHeartItem extends Item {

    public PrimordialHeartItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("tooltip.modpack_utils.primordial_heart.tooltip.1").withStyle(ChatFormatting.DARK_PURPLE));

        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
}
