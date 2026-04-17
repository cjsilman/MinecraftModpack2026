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

                        output.accept(ModBlocks.SACRED_STONE);
                        output.accept(ModItems.GOD_THREAD);
                        output.accept(ModItems.FRAGMENTED_MEMORY);
                        output.accept(ModItems.ABYSS_HEART);
                        output.accept(ModItems.CURSED_HEART);
                        output.accept(ModItems.DESERT_HEART);
                        output.accept(ModItems.IGNIS_HEART);
                        output.accept(ModItems.MECH_HEART);
                        output.accept(ModItems.MONSTROUS_HEART);
                        output.accept(ModItems.STORM_HEART);
                        output.accept(ModItems.VOID_HEART);

                        output.accept(ModBlocks.ABYSS_BEACON_BLOCK);
                        output.accept(ModBlocks.CURSED_BEACON_BLOCK);
                        output.accept(ModBlocks.DESERT_BEACON_BLOCK);
                        output.accept(ModBlocks.IGNIS_BEACON_BLOCK);
                        output.accept(ModBlocks.MECH_BEACON_BLOCK);
                        output.accept(ModBlocks.MONSTROUS_BEACON_BLOCK);
                        output.accept(ModBlocks.STORM_BEACON_BLOCK);
                        output.accept(ModBlocks.VOID_BEACON_BLOCK);

                        output.accept(ModBlocks.ABYSS_BEACON_PEDESTAL_BLOCK);
                        output.accept(ModBlocks.CURSED_PEDESTAL_BLOCK);
                        output.accept(ModBlocks.DESERT_PEDESTAL_BLOCK);
                        output.accept(ModBlocks.IGNIS_BEACON_PEDESTAL_BLOCK);
                        output.accept(ModBlocks.MECH_BEACON_PEDESTAL_BLOCK);
                        output.accept(ModBlocks.MONSTROUS_BEACON_PEDESTAL_BLOCK);
                        output.accept(ModBlocks.STORM_BEACON_PEDESTAL_BLOCK);
                        output.accept(ModBlocks.VOID_BEACON_PEDESTAL_BLOCK);
                    })).build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MOD_TABS.register(eventBus);
    }
}
