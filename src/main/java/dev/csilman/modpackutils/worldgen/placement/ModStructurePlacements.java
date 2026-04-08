package dev.csilman.modpackutils.worldgen.placement;

import dev.csilman.modpackutils.ModpackUtilsMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModStructurePlacements {
    public static final DeferredRegister<StructurePlacementType<?>> REGISTER =
            DeferredRegister.create(Registries.STRUCTURE_PLACEMENT, ModpackUtilsMod.MOD_ID);

    public static final DeferredHolder<StructurePlacementType<?>, StructurePlacementType<FixedStructurePlacement>>
            FIXED_PLACEMENT = REGISTER.register("fixed", () -> () -> FixedStructurePlacement.CODEC);

    public static void register(IEventBus bus) {
        REGISTER.register(bus);
    }
}
