package dev.csilman.modpackutils.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public record MemoryDestination(
        ResourceKey<Level> dimension,
        double x,
        double y,
        double z,
        int requiredAmount,
        boolean requireFragmentedMemory
) {
    public static final Codec<MemoryDestination> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceKey.codec(Registries.DIMENSION).fieldOf("dimension").forGetter(MemoryDestination::dimension),
            Codec.DOUBLE.fieldOf("x").forGetter(MemoryDestination::x),
            Codec.DOUBLE.fieldOf("y").forGetter(MemoryDestination::y),
            Codec.DOUBLE.fieldOf("z").forGetter(MemoryDestination::z),
            Codec.intRange(1, Integer.MAX_VALUE).fieldOf("required_amount").forGetter(MemoryDestination::requiredAmount),
            Codec.BOOL.fieldOf("require_fragmented_memory").forGetter(MemoryDestination::requireFragmentedMemory)
    ).apply(instance, MemoryDestination::new));
}
