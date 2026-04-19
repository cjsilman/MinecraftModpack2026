package dev.csilman.modpackutils.worldgen.chunkgenerator;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.csilman.modpackutils.util.FilteredStructureSetLookup;
import dev.csilman.modpackutils.worldgen.placement.FixedStructurePlacement;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
import net.minecraft.world.level.levelgen.FlatLevelSource;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorSettings;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FlatBossArenaChunkGenerator extends FlatLevelSource {
    public static final int CHUNK_RADIUS = 10;

    public static final MapCodec<FlatBossArenaChunkGenerator> CODEC =
            RecordCodecBuilder.mapCodec(instance -> instance.group(
                    FlatLevelGeneratorSettings.CODEC.fieldOf("settings")
                            .forGetter(g -> g.settings),
                    Codec.INT.optionalFieldOf("chunk_radius", 10)
                            .forGetter(g -> g.chunkRadius),
                    ResourceLocation.CODEC.listOf()
                            .optionalFieldOf("allowed_structure_sets", List.of())
                            .forGetter(g -> List.copyOf(g.allowedStructureSets))
            ).apply(instance, FlatBossArenaChunkGenerator::new));

    private final FlatLevelGeneratorSettings settings;
    private final int chunkRadius;
    private final Set<ResourceLocation> allowedStructureSets;

    private boolean isInsideBoundary(ChunkPos pos) {
        return Math.abs(pos.x) <= chunkRadius && Math.abs(pos.z) <= chunkRadius;
    }

    public FlatBossArenaChunkGenerator(
            FlatLevelGeneratorSettings flatWorldSettings,
            int chunkRadius,
            List<ResourceLocation> allowedStructureSets) {
        super(flatWorldSettings);
        this.settings = flatWorldSettings;
        this.chunkRadius = chunkRadius;
        this.allowedStructureSets = Set.copyOf(allowedStructureSets);
    }

    @Override
    public ChunkGeneratorStructureState createState(HolderLookup<StructureSet> structureSetLookup, RandomState randomState, long seed) {
        if (allowedStructureSets.isEmpty()) {
            return super.createState(structureSetLookup, randomState, seed);
        }

        Stream<Holder.Reference<StructureSet>> filtered = structureSetLookup.listElements()
                .filter(holder -> allowedStructureSets.contains(holder.key().location()));

        return ChunkGeneratorStructureState.createForNormal(randomState, seed, getBiomeSource(),
                new FilteredStructureSetLookup(structureSetLookup, filtered.collect(Collectors.toSet())));
    }

    @Override
    public CompletableFuture<ChunkAccess> fillFromNoise(Blender blender, RandomState randomState, StructureManager structureManager, ChunkAccess chunk) {
        if (!isInsideBoundary(chunk.getPos())) {
            return CompletableFuture.completedFuture(chunk);
        }
        return super.fillFromNoise(blender, randomState, structureManager, chunk);
    }

    @Override
    protected MapCodec<? extends ChunkGenerator> codec() {
        return CODEC;
    }

    @Override
    public @Nullable Pair<BlockPos, Holder<Structure>> findNearestMapStructure(ServerLevel level, HolderSet<Structure> structure, BlockPos pos, int searchRadius, boolean skipKnownStructures) {
        ChunkGeneratorStructureState state = level.getChunkSource().getGeneratorState();

        for (Holder<Structure> structureHolder : structure) {
            List<StructurePlacement> placements = state.getPlacementsForStructure(structureHolder);

            for (StructurePlacement placement : placements) {
                if (placement instanceof FixedStructurePlacement fixed) {
                    if (!fixed.isStructureChunk(state, fixed.getChunkX(), fixed.getChunkZ())) {
                        continue;
                    }

                    BlockPos structurePos = new BlockPos(
                            fixed.getChunkX()*16 +8,
                            pos.getY(),
                            fixed.getChunkX()*16 +8
                    );

                    return Pair.of(structurePos, structureHolder);

                }
            }
        }

        return super.findNearestMapStructure(level, structure, pos, searchRadius, skipKnownStructures);
    }

}
