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
}
