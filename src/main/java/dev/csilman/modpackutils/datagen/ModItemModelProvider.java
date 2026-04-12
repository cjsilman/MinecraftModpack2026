package dev.csilman.modpackutils.datagen;

import dev.csilman.modpackutils.ModpackUtilsMod;
import dev.csilman.modpackutils.block.ModBlocks;
import dev.csilman.modpackutils.item.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;

public class ModItemModelProvider extends ItemModelProvider {

    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, ModpackUtilsMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(ModItems.GOD_THREAD.get());
        basicItem(ModItems.FRAGMENTED_MEMORY.get());
        horizontalBlockItem(ModBlocks.ABYSS_BEACON_PEDESTAL_BLOCK);
        horizontalBlockItem(ModBlocks.CURSED_PEDESTAL_BLOCK);
        horizontalBlockItem(ModBlocks.DESERT_PEDESTAL_BLOCK);
        horizontalBlockItem(ModBlocks.IGNIS_BEACON_PEDESTAL_BLOCK);
        horizontalBlockItem(ModBlocks.MECH_BEACON_PEDESTAL_BLOCK);
        horizontalBlockItem(ModBlocks.MONSTROUS_BEACON_PEDESTAL_BLOCK);
        horizontalBlockItem(ModBlocks.STORM_BEACON_PEDESTAL_BLOCK);
        horizontalBlockItem(ModBlocks.VOID_BEACON_PEDESTAL_BLOCK);
    }

    private ItemModelBuilder horizontalBlockItem(DeferredBlock<Block> block) {
        return getBuilder(block.getId().getPath()).parent(new ModelFile.UncheckedModelFile(ResourceLocation.fromNamespaceAndPath(ModpackUtilsMod.MOD_ID,
                "block/" + block.getId().getPath())));
    }
}
