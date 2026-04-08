package dev.csilman.modpackutils.worldgen.placement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;

import java.util.Optional;

public class FixedStructurePlacement extends StructurePlacement {

    public static final MapCodec<FixedStructurePlacement> CODEC =
            RecordCodecBuilder.mapCodec(instance -> instance.group(
                    Codec.INT.fieldOf("chunk_x").forGetter(p -> p.chunkX),
                    Codec.INT.fieldOf("chunk_z").forGetter(p -> p.chunkZ)
            ).apply(instance, FixedStructurePlacement::new));

    private final int chunkX;
    private final int chunkZ;

    protected FixedStructurePlacement(int chunkX, int chunkZ) {
        super(
                Vec3i.ZERO,
                FrequencyReductionMethod.DEFAULT,
                1.0f,
                0,
                Optional.empty()
        );

        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
    }

    @Override
    protected boolean isPlacementChunk(ChunkGeneratorStructureState chunkGeneratorStructureState, int x, int y) {
        return (x == this.chunkX) && (y == this.chunkZ);
    }

    @Override
    public StructurePlacementType<?> type() {
        return ModStructurePlacements.FIXED_PLACEMENT.get();
    }
}
