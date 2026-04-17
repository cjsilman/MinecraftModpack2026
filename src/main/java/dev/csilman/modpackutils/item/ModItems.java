package dev.csilman.modpackutils.item;

import dev.csilman.modpackutils.ModpackUtilsMod;
import dev.csilman.modpackutils.item.custom.FragmentedMemoryItem;
import dev.csilman.modpackutils.item.custom.GodThreadItem;
import dev.csilman.modpackutils.item.custom.PrimordialHeartItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ModpackUtilsMod.MOD_ID);

    public static final DeferredItem<Item> GOD_THREAD = ITEMS.register("god_thread",
            () -> new GodThreadItem(new Item.Properties().fireResistant()));

    public static final DeferredItem<Item> FRAGMENTED_MEMORY = ITEMS.register("fragmented_memory",
            () -> new FragmentedMemoryItem(new Item.Properties()));

    public static final DeferredItem<Item> ABYSS_HEART = ITEMS.register("abyss_heart",
            () -> new PrimordialHeartItem(new Item.Properties().fireResistant()));

    public static final DeferredItem<Item> CURSED_HEART = ITEMS.register("cursed_heart",
            () -> new PrimordialHeartItem(new Item.Properties().fireResistant()));

    public static final DeferredItem<Item> DESERT_HEART = ITEMS.register("desert_heart",
            () -> new PrimordialHeartItem(new Item.Properties().fireResistant()));

    public static final DeferredItem<Item> IGNIS_HEART = ITEMS.register("ignis_heart",
            () -> new PrimordialHeartItem(new Item.Properties().fireResistant()));

    public static final DeferredItem<Item> MECH_HEART = ITEMS.register("mech_heart",
            () -> new PrimordialHeartItem(new Item.Properties().fireResistant()));

    public static final DeferredItem<Item> MONSTROUS_HEART = ITEMS.register("monstrous_heart",
            () -> new PrimordialHeartItem(new Item.Properties().fireResistant()));

    public static final DeferredItem<Item> STORM_HEART = ITEMS.register("storm_heart",
            () -> new PrimordialHeartItem(new Item.Properties().fireResistant()));

    public static final DeferredItem<Item> VOID_HEART = ITEMS.register("void_heart",
            () -> new PrimordialHeartItem(new Item.Properties().fireResistant()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
