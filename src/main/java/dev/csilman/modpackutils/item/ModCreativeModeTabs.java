package dev.csilman.modpackutils.item;

import dev.csilman.modpackutils.ModpackUtilsMod;
import dev.csilman.modpackutils.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MOD_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ModpackUtilsMod.MOD_ID);

    public static final Supplier<CreativeModeTab> OreSaplingsTab =
            CREATIVE_MOD_TABS.register("modpack_utils_tab", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.modpack_utils.modpack_utils_tab"))
                    .icon(() -> new ItemStack(ModBlocks.BLACK_OPAL_BLOCK))
                    .displayItems(((itemDisplayParameters, output) -> {
                        output.accept(ModBlocks.BLACK_OPAL_BLOCK);
                        output.accept(ModBlocks.BOSS_BEACON_BLOCK);
                    })).build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MOD_TABS.register(eventBus);
    }
}
