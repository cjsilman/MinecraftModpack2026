package dev.csilman.modpackutils.datagen;

import dev.csilman.modpackutils.block.ModBlocks;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;

import java.util.Set;

public class ModBlockLootTableProvider extends BlockLootSubProvider {
    protected ModBlockLootTableProvider(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
        dropSelf(ModBlocks.BLACK_OPAL_BLOCK.get());
        dropSelf(ModBlocks.SACRED_STONE.get());
        dropSelf(ModBlocks.ABYSS_BEACON_BLOCK.get());
        dropSelf(ModBlocks.CURSED_BEACON_BLOCK.get());
        dropSelf(ModBlocks.DESERT_BEACON_BLOCK.get());
        dropSelf(ModBlocks.IGNIS_BEACON_BLOCK.get());
        dropSelf(ModBlocks.MECH_BEACON_BLOCK.get());
        dropSelf(ModBlocks.MONSTROUS_BEACON_BLOCK.get());
        dropSelf(ModBlocks.STORM_BEACON_BLOCK.get());
        dropSelf(ModBlocks.VOID_BEACON_BLOCK.get());
        dropSelf(ModBlocks.ABYSS_BEACON_PEDESTAL_BLOCK.get());
        dropSelf(ModBlocks.CURSED_PEDESTAL_BLOCK.get());
        dropSelf(ModBlocks.DESERT_PEDESTAL_BLOCK.get());
        dropSelf(ModBlocks.IGNIS_BEACON_PEDESTAL_BLOCK.get());
        dropSelf(ModBlocks.MECH_BEACON_PEDESTAL_BLOCK.get());
        dropSelf(ModBlocks.MONSTROUS_BEACON_PEDESTAL_BLOCK.get());
        dropSelf(ModBlocks.STORM_BEACON_PEDESTAL_BLOCK.get());
        dropSelf(ModBlocks.VOID_BEACON_PEDESTAL_BLOCK.get());
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(Holder::value)::iterator;
    }
}
