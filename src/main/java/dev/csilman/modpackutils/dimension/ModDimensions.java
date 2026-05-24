package dev.csilman.modpackutils.dimension;

import dev.csilman.modpackutils.ModpackUtilsMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

public class ModDimensions {
    public static final ResourceKey<Level> ABYSS_WORLD = ResourceKey.create(
            Registries.DIMENSION,
            ResourceLocation.fromNamespaceAndPath(ModpackUtilsMod.MOD_ID, "abyss_world")
    );
    public static final ResourceKey<Level> CURSED_WORLD = ResourceKey.create(
            Registries.DIMENSION,
            ResourceLocation.fromNamespaceAndPath(ModpackUtilsMod.MOD_ID, "cursed_world")
    );
    public static final ResourceKey<Level> IGNIS_WORLD = ResourceKey.create(
            Registries.DIMENSION,
            ResourceLocation.fromNamespaceAndPath(ModpackUtilsMod.MOD_ID, "ignis_world")
    );
    public static final ResourceKey<Level> MECH_WORLD = ResourceKey.create(
            Registries.DIMENSION,
            ResourceLocation.fromNamespaceAndPath(ModpackUtilsMod.MOD_ID, "mech_world")
    );
    public static final ResourceKey<Level> MONSTROUS_WORLD = ResourceKey.create(
            Registries.DIMENSION,
            ResourceLocation.fromNamespaceAndPath(ModpackUtilsMod.MOD_ID, "monstrous_world")
    );
    public static final ResourceKey<Level> DESERT_WORLD = ResourceKey.create(
            Registries.DIMENSION,
            ResourceLocation.fromNamespaceAndPath(ModpackUtilsMod.MOD_ID, "desert_world")
    );
    public static final ResourceKey<Level> STORM_WORLD = ResourceKey.create(
            Registries.DIMENSION,
            ResourceLocation.fromNamespaceAndPath(ModpackUtilsMod.MOD_ID, "storm_world")
    );
    public static final ResourceKey<Level> JUDGEMENT_WORLD = ResourceKey.create(
            Registries.DIMENSION,
            ResourceLocation.fromNamespaceAndPath(ModpackUtilsMod.MOD_ID, "judgement_world")
    );
    public static final ResourceKey<Level> VOID_WORLD = ResourceKey.create(
            Registries.DIMENSION,
            ResourceLocation.fromNamespaceAndPath(ModpackUtilsMod.MOD_ID, "void_world")
    );
    public static final ResourceKey<Level> CHESED_WORLD = ResourceKey.create(
            Registries.DIMENSION,
            ResourceLocation.fromNamespaceAndPath(ModpackUtilsMod.MOD_ID, "chesed_world")
    );
    public static final ResourceKey<Level> MALKUTH_WORLD = ResourceKey.create(
            Registries.DIMENSION,
            ResourceLocation.fromNamespaceAndPath(ModpackUtilsMod.MOD_ID, "malkuth_world")
    );
}
