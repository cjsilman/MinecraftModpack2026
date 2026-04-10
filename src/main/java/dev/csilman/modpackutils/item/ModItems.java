package dev.csilman.modpackutils.item;

import dev.csilman.modpackutils.ModpackUtilsMod;
import dev.csilman.modpackutils.item.custom.GodThreadItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ModpackUtilsMod.MOD_ID);

    public static final DeferredItem<Item> GOD_THREAD = ITEMS.register("god_thread",
            () -> new GodThreadItem(new Item.Properties().fireResistant()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
