package dev.csilman.modpackutils.mixin.accessor;

import dev.csilman.modpackutils.mixin.iface.ChunkGeneratorStructureStateMixin;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ChunkGeneratorStructureState.class)
public class ChunkGeneratorStructureStateMixinImpl implements ChunkGeneratorStructureStateMixin {
    @Unique
    private ResourceKey<Level> dimensionKey;

    @Override
    public ResourceKey<Level> getDimensionKey() {
        return this.dimensionKey;
    }

    @Override
    public void setDimensionKey(ResourceKey<Level> key) {
        this.dimensionKey = key;
    }
}
