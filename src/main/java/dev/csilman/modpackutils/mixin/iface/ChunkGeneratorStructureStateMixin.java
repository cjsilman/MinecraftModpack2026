package dev.csilman.modpackutils.mixin.iface;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public interface ChunkGeneratorStructureStateMixin {
    ResourceKey<Level> getDimensionKey();
    void setDimensionKey(ResourceKey<Level> key);
}
