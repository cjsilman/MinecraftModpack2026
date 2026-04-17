package dev.csilman.modpackutils.worldgen.chunkgenerator;

import com.mojang.serialization.MapCodec;
import dev.csilman.modpackutils.ModpackUtilsMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModChunkGenerators {
    public static final DeferredRegister<MapCodec<? extends ChunkGenerator>> REGISTER =
            DeferredRegister.create(Registries.CHUNK_GENERATOR, ModpackUtilsMod.MOD_ID);

    public static final DeferredHolder<MapCodec<? extends ChunkGenerator>,
            MapCodec<BossArenaChunkGenerator>> BOSS_ARENA =
            REGISTER.register("boss_arena", () -> BossArenaChunkGenerator.CODEC);

    public static final DeferredHolder<MapCodec<? extends ChunkGenerator>,
            MapCodec<FlatBossArenaChunkGenerator>> BOSS_ARENA_FLAT =
            REGISTER.register("boss_arena_flat", () -> FlatBossArenaChunkGenerator.CODEC);

    public static void register(IEventBus bus) {
        REGISTER.register(bus);
    }
}
