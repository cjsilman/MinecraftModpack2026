package dev.csilman.modpackutils.worldgen.chunkgenerator;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.csilman.modpackutils.util.FilteredStructureSetLookup;
import dev.csilman.modpackutils.worldgen.placement.FixedStructurePlacement;
import net.minecraft.core.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.*;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BossArenaChunkGenerator extends NoiseBasedChunkGenerator {

    public static final int CHUNK_RADIUS = 10;

    public static final MapCodec<BossArenaChunkGenerator> CODEC =
            RecordCodecBuilder.mapCodec(instance -> instance.group(
                    BiomeSource.CODEC.fieldOf("biome_source")
                            .forGetter(ChunkGenerator::getBiomeSource),
                    NoiseGeneratorSettings.CODEC.fieldOf("settings")
                            .forGetter(g -> g.noiseSettings),
                    Codec.INT.optionalFieldOf("chunk_radius", 10)
                            .forGetter(g -> g.chunkRadius),
                    ResourceLocation.CODEC.listOf()
                            .optionalFieldOf("allowed_structure_sets", List.of())
                            .forGetter(g -> List.copyOf(g.allowedStructureSets))
            ).apply(instance, BossArenaChunkGenerator::new));

    private final Holder<NoiseGeneratorSettings> noiseSettings;
    private final int chunkRadius;
    private final NoiseBasedChunkGenerator noiseChunkGenerator;
    private final Set<ResourceLocation> allowedStructureSets;

    public BossArenaChunkGenerator(BiomeSource biomeSource, Holder<NoiseGeneratorSettings> noiseSettings, int chunkRadius, List<ResourceLocation> allowedStructureSets) {
        super(biomeSource, noiseSettings);
        this.noiseSettings = noiseSettings;
        this.chunkRadius = chunkRadius;
        this.noiseChunkGenerator = new NoiseBasedChunkGenerator(biomeSource, noiseSettings);
        this.allowedStructureSets = Set.copyOf(allowedStructureSets);
    }

    private boolean isInsideBoundary(ChunkPos pos) {
        return Math.abs(pos.x) <= chunkRadius && Math.abs(pos.z) <= chunkRadius;
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
    protected MapCodec<? extends ChunkGenerator> codec() {
        return CODEC;
    }

    @Override
    public void applyCarvers(WorldGenRegion worldGenRegion, long seed, RandomState randomState, BiomeManager biomeManager, StructureManager structureManager, ChunkAccess chunkAccess, GenerationStep.Carving carving) {
        if (!isInsideBoundary(chunkAccess.getPos())) {
            // Do nothing
        } else {
            noiseChunkGenerator.applyCarvers(worldGenRegion, seed, randomState, biomeManager, structureManager, chunkAccess, carving);
        }
    }

    @Override
    public void buildSurface(WorldGenRegion worldGenRegion, StructureManager structureManager, RandomState randomState, ChunkAccess chunkAccess) {
        if (!isInsideBoundary(chunkAccess.getPos())) {
            // Do nothing
        } else {
            noiseChunkGenerator.buildSurface(worldGenRegion, structureManager, randomState, chunkAccess);
        }
    }

    @Override
    public void applyBiomeDecoration(WorldGenLevel level, ChunkAccess chunk, StructureManager structureManager) {
        if (!isInsideBoundary(chunk.getPos())) {
            // Do nothing
        } else {
            noiseChunkGenerator.applyBiomeDecoration(level, chunk, structureManager);
        }
    }

    @Override
    public void spawnOriginalMobs(WorldGenRegion worldGenRegion) {
        if (!isInsideBoundary(worldGenRegion.getCenter())) {
            // Do nothing
        } else {
            noiseChunkGenerator.spawnOriginalMobs(worldGenRegion);
        }
    }

    @Override
    public int getGenDepth() {
        return (this.noiseSettings.value()).noiseSettings().height();
    }

    @Override
    public int getSeaLevel() {
        return  (this.noiseSettings.value()).seaLevel();
    }

    @Override
    public int getMinY() {
        return (this.noiseSettings.value()).noiseSettings().minY();
    }

    @Override
    public CompletableFuture<ChunkAccess> fillFromNoise(Blender blender, RandomState randomState, StructureManager structureManager, ChunkAccess chunkAccess) {
        if (!isInsideBoundary(chunkAccess.getPos())) {
            return CompletableFuture.completedFuture(chunkAccess);
        }
        return noiseChunkGenerator.fillFromNoise(blender, randomState, structureManager, chunkAccess);
    }

    @Override
    public int getBaseHeight(int x, int z, Heightmap.Types types, LevelHeightAccessor levelHeightAccessor, RandomState randomState) {
        ChunkPos pos = new ChunkPos(SectionPos.blockToSectionCoord(x), SectionPos.blockToSectionCoord(z));

        if (!isInsideBoundary(pos)) {
            return levelHeightAccessor.getMinBuildHeight();
        } else {
            return noiseChunkGenerator.getBaseHeight(x, z, types, levelHeightAccessor, randomState);
        }
    }

    @Override
    public NoiseColumn getBaseColumn(int x, int z, LevelHeightAccessor levelHeightAccessor, RandomState randomState) {
        ChunkPos pos = new ChunkPos(SectionPos.blockToSectionCoord(x), SectionPos.blockToSectionCoord(z));

        if (!isInsideBoundary(pos)) {
            return new NoiseColumn(levelHeightAccessor.getMinBuildHeight(), new BlockState[0]);
        }
        return noiseChunkGenerator.getBaseColumn(x, z, levelHeightAccessor, randomState);
    }

    @Override
    public void addDebugScreenInfo(List<String> info, RandomState randomState, BlockPos blockPos) {
        noiseChunkGenerator.addDebugScreenInfo(info, randomState, blockPos);
    }

    @Override
    public CompletableFuture<ChunkAccess> createBiomes(RandomState randomState, Blender blender, StructureManager structureManager, ChunkAccess chunk) {
        return noiseChunkGenerator.createBiomes(randomState, blender, structureManager, chunk);
    }

    @Override
    public BiomeSource getBiomeSource() {
        return this.noiseChunkGenerator.getBiomeSource();
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
