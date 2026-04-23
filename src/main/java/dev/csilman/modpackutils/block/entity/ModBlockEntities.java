package dev.csilman.modpackutils.block.entity;

import dev.csilman.modpackutils.ModpackUtilsMod;
import dev.csilman.modpackutils.block.ModBlocks;
import dev.csilman.modpackutils.block.entity.custom.BossBeaconEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, ModpackUtilsMod.MOD_ID);

    public static final Supplier<BlockEntityType<BossBeaconEntity>> BOSS_BEACON_BE = BLOCK_ENTITIES.register("boss_beacon_be",
            () -> BlockEntityType.Builder.of(
                    BossBeaconEntity::new,
                    ModBlocks.ABYSS_BEACON_BLOCK.get(),
                    ModBlocks.CURSED_BEACON_BLOCK.get(),
                    ModBlocks.DESERT_BEACON_BLOCK.get(),
                    ModBlocks.IGNIS_BEACON_BLOCK.get(),
                    ModBlocks.MECH_BEACON_BLOCK.get(),
                    ModBlocks.MONSTROUS_BEACON_BLOCK.get(),
                    ModBlocks.STORM_BEACON_BLOCK.get(),
                    ModBlocks.VOID_BEACON_BLOCK.get(),
                    ModBlocks.CHESED_BEACON_BLOCK.get(),
                    ModBlocks.MALKUTH_BEACON_BLOCK.get()
            ).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
