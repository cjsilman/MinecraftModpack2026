package dev.csilman.modpackutils.datagen;

import dev.csilman.modpackutils.ModpackUtilsMod;
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
                .add(ModItems.ABYSS_HEART.get());
    }
}
