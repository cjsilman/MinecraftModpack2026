package dev.csilman.modpackutils.datagen;


import dev.csilman.modpackutils.ModpackUtilsMod;
import dev.csilman.modpackutils.block.ModBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;

public class ModBlockStateProvider extends BlockStateProvider {

    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, ModpackUtilsMod.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        blockWithItem(ModBlocks.BLACK_OPAL_BLOCK);
        blockWithItem(ModBlocks.SACRED_STONE);
        blockWithItem(ModBlocks.ABYSS_BEACON_BLOCK);
        blockWithItem(ModBlocks.CURSED_BEACON_BLOCK);
        blockWithItem(ModBlocks.DESERT_BEACON_BLOCK);
        blockWithItem(ModBlocks.IGNIS_BEACON_BLOCK);
        blockWithItem(ModBlocks.MECH_BEACON_BLOCK);
        blockWithItem(ModBlocks.MONSTROUS_BEACON_BLOCK);
        blockWithItem(ModBlocks.STORM_BEACON_BLOCK);
        blockWithItem(ModBlocks.VOID_BEACON_BLOCK);

        horizontalBlock(ModBlocks.ABYSS_BEACON_PEDESTAL_BLOCK.get(), mcLoc("block/lodestone_side"), modLoc("block/abyss_beacon_pedestal_block"), mcLoc("block/lodestone_top"));
        horizontalBlock(ModBlocks.CURSED_PEDESTAL_BLOCK.get(), mcLoc("block/lodestone_side"), modLoc("block/cursed_beacon_pedestal_block"), mcLoc("block/lodestone_top"));
        horizontalBlock(ModBlocks.DESERT_PEDESTAL_BLOCK.get(), mcLoc("block/lodestone_side"), modLoc("block/desert_beacon_pedestal_block"), mcLoc("block/lodestone_top"));
        horizontalBlock(ModBlocks.IGNIS_BEACON_PEDESTAL_BLOCK.get(), mcLoc("block/lodestone_side"), modLoc("block/ignis_beacon_pedestal_block"), mcLoc("block/lodestone_top"));
        horizontalBlock(ModBlocks.MECH_BEACON_PEDESTAL_BLOCK.get(), mcLoc("block/lodestone_side"), modLoc("block/mech_beacon_pedestal_block"), mcLoc("block/lodestone_top"));
        horizontalBlock(ModBlocks.MONSTROUS_BEACON_PEDESTAL_BLOCK.get(), mcLoc("block/lodestone_side"), modLoc("block/monstrous_beacon_pedestal_block"), mcLoc("block/lodestone_top"));
        horizontalBlock(ModBlocks.STORM_BEACON_PEDESTAL_BLOCK.get(), mcLoc("block/lodestone_side"), modLoc("block/storm_beacon_pedestal_block"), mcLoc("block/lodestone_top"));
        horizontalBlock(ModBlocks.VOID_BEACON_PEDESTAL_BLOCK.get(), mcLoc("block/lodestone_side"), modLoc("block/void_beacon_pedestal_block"), mcLoc("block/lodestone_top"));
        horizontalBlock(ModBlocks.HOME_BEACON_PEDESTAL_BLOCK.get(), mcLoc("block/lodestone_side"), modLoc("block/home_beacon_pedestal_block"), mcLoc("block/lodestone_top"));
    }

    private void blockWithItem(DeferredBlock<Block> deferredBlock) {
        simpleBlockWithItem(deferredBlock.get(), cubeAll(deferredBlock.get()));
    }

}
