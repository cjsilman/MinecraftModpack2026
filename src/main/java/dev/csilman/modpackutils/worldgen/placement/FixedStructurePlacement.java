package dev.csilman.modpackutils.worldgen.placement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.csilman.modpackutils.dimension.ModDimensions;
import dev.csilman.modpackutils.mixin.iface.ChunkGeneratorStructureStateMixin;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;

import java.util.List;
import java.util.Optional;

public class FixedStructurePlacement extends StructurePlacement {

    public static final MapCodec<FixedStructurePlacement> CODEC =
            RecordCodecBuilder.mapCodec(instance -> instance.group(
                    Codec.INT.fieldOf("chunk_x").forGetter(p -> p.chunkX),
                    Codec.INT.fieldOf("chunk_z").forGetter(p -> p.chunkZ),
                    ResourceKey.codec(Registries.DIMENSION)
                            .listOf()
                            .fieldOf("dimensions")
                            .forGetter(p -> p.dimensions)
            ).apply(instance, FixedStructurePlacement::new));

    private final int chunkX;
    private final int chunkZ;
    private final List<ResourceKey<Level>> dimensions;

    protected FixedStructurePlacement(int chunkX, int chunkZ, List<ResourceKey<Level>> dimensions) {
        super(
                Vec3i.ZERO,
                FrequencyReductionMethod.DEFAULT,
                1.0f,
                0,
                Optional.empty()
        );

        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        this.dimensions = dimensions;
    }

    @Override
    protected boolean isPlacementChunk(ChunkGeneratorStructureState chunkGeneratorStructureState, int x, int y) {

        ResourceKey<Level> currentDimension = ((ChunkGeneratorStructureStateMixin) chunkGeneratorStructureState).getDimensionKey();

        if(this.dimensions.contains(currentDimension)) {
            return (x == this.chunkX) && (y == this.chunkZ);
        }
        else {
            return false;
        }
    }

    @Override
    public StructurePlacementType<?> type() {
        return ModStructurePlacements.FIXED_PLACEMENT.get();
    }
}
