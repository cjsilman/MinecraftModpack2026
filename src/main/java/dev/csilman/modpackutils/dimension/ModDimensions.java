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
    public static final ResourceKey<Level> DESERT_WORLD = ResourceKey.create(
            Registries.DIMENSION,
            ResourceLocation.fromNamespaceAndPath(ModpackUtilsMod.MOD_ID, "desert_world")
    );
    public static final ResourceKey<Level> STORM_WORLD = ResourceKey.create(
            Registries.DIMENSION,
            ResourceLocation.fromNamespaceAndPath(ModpackUtilsMod.MOD_ID, "storm_world")
    );
    public static final ResourceKey<Level> TEST_WORLD = ResourceKey.create(
            Registries.DIMENSION,
            ResourceLocation.fromNamespaceAndPath(ModpackUtilsMod.MOD_ID, "test_world")
    );
}
