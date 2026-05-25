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
        basicItem(ModItems.SYNCHRONIZED_THREAD.get());
        basicItem(ModItems.FOCUS_CRYSTAL.get());
        basicItem(ModItems.TUNED_CRYSTAL.get());
        basicItem(ModItems.FRAGMENTED_MEMORY.get());
        basicItem(ModItems.ABYSS_HEART.get());
        basicItem(ModItems.CURSED_HEART.get());
        basicItem(ModItems.DESERT_HEART.get());
        basicItem(ModItems.IGNIS_HEART.get());
        basicItem(ModItems.MECH_HEART.get());
        basicItem(ModItems.MONSTROUS_HEART.get());
        basicItem(ModItems.STORM_HEART.get());
        basicItem(ModItems.VOID_HEART.get());
        basicItem(ModItems.MALKUTH_HEART.get());
        basicItem(ModItems.CHESED_HEART.get());
        horizontalBlockItem(ModBlocks.ABYSS_BEACON_PEDESTAL_BLOCK);
        horizontalBlockItem(ModBlocks.CURSED_BEACON_PEDESTAL_BLOCK);
        horizontalBlockItem(ModBlocks.DESERT_BEACON_PEDESTAL_BLOCK);
        horizontalBlockItem(ModBlocks.IGNIS_BEACON_PEDESTAL_BLOCK);
        horizontalBlockItem(ModBlocks.MECH_BEACON_PEDESTAL_BLOCK);
        horizontalBlockItem(ModBlocks.MONSTROUS_BEACON_PEDESTAL_BLOCK);
        horizontalBlockItem(ModBlocks.STORM_BEACON_PEDESTAL_BLOCK);
        horizontalBlockItem(ModBlocks.VOID_BEACON_PEDESTAL_BLOCK);
        horizontalBlockItem(ModBlocks.CHESED_BEACON_PEDESTAL_BLOCK);
        horizontalBlockItem(ModBlocks.MALKUTH_BEACON_PEDESTAL_BLOCK);
        horizontalBlockItem(ModBlocks.JUDGEMENT_BEACON_PEDESTAL_BLOCK);
        horizontalBlockItem(ModBlocks.HOME_BEACON_PEDESTAL_BLOCK);
    }

    private ItemModelBuilder horizontalBlockItem(DeferredBlock<Block> block) {
        return getBuilder(block.getId().getPath()).parent(new ModelFile.UncheckedModelFile(ResourceLocation.fromNamespaceAndPath(ModpackUtilsMod.MOD_ID,
                "block/" + block.getId().getPath())));
    }
}
