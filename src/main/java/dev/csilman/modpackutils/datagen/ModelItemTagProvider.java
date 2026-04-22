package dev.csilman.modpackutils.datagen;

import dev.csilman.modpackutils.ModpackUtilsMod;
import dev.csilman.modpackutils.block.ModBlocks;
import dev.csilman.modpackutils.item.ModItems;
import dev.csilman.modpackutils.util.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModelItemTagProvider extends ItemTagsProvider {
    public ModelItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, ModpackUtilsMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(ModTags.Items.TRANSFORMABLE_ITEMS)
                .add(ModItems.ABYSS_HEART.get())
                .add(ModItems.CURSED_HEART.get())
                .add(ModItems.DESERT_HEART.get())
                .add(ModItems.IGNIS_HEART.get())
                .add(ModItems.MECH_HEART.get())
                .add(ModItems.MONSTROUS_HEART.get())
                .add(ModItems.STORM_HEART.get())
                .add(ModItems.VOID_HEART.get())
                .add(ModItems.CHESED_HEART.get())
                .add(ModItems.MALKUTH_HEART.get());

        this.tag(ModTags.Items.NO_BEACON_PEDESTAL_INTERACT)
                .add(ModBlocks.ABYSS_BEACON_BLOCK.get().asItem())
                .add(ModBlocks.CURSED_BEACON_BLOCK.get().asItem())
                .add(ModBlocks.DESERT_BEACON_BLOCK.get().asItem())
                .add(ModBlocks.IGNIS_BEACON_BLOCK.get().asItem())
                .add(ModBlocks.MECH_BEACON_BLOCK.get().asItem())
                .add(ModBlocks.MONSTROUS_BEACON_BLOCK.get().asItem())
                .add(ModBlocks.STORM_BEACON_BLOCK.get().asItem())
                .add(ModBlocks.VOID_BEACON_BLOCK.get().asItem());
    }
}
