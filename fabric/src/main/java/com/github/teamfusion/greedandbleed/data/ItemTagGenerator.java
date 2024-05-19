package com.github.teamfusion.greedandbleed.data;

import com.github.teamfusion.greedandbleed.common.registry.BlockRegistry;
import com.github.teamfusion.greedandbleed.common.registry.GBItemTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.ItemTags;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ItemTagGenerator extends FabricTagProvider.ItemTagProvider {


    public ItemTagGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> completableFuture, @Nullable FabricTagProvider.BlockTagProvider blockTagProvider) {
        super(output, completableFuture, blockTagProvider);
    }

    @Override
    protected void addTags(HolderLookup.Provider arg) {
        this.getOrCreateTagBuilder(GBItemTags.HOGDEW_LOG).add(BlockRegistry.HOGDEW_STEM.get().asItem()).add(BlockRegistry.STRIPPED_HOGDEW_STEM.get().asItem());
        this.getOrCreateTagBuilder(ItemTags.PLANKS).add(BlockRegistry.HOGDEW_PLANKS.get().asItem());
        this.getOrCreateTagBuilder(ItemTags.WOODEN_STAIRS).add(BlockRegistry.HOGDEW_PLANKS_STAIRS.get().asItem());
        this.getOrCreateTagBuilder(ItemTags.WOODEN_SLABS).add(BlockRegistry.HOGDEW_PLANKS_SLAB.get().asItem());
        this.getOrCreateTagBuilder(ItemTags.WOODEN_FENCES).add(BlockRegistry.HOGDEW_FENCE.get().asItem());
        this.getOrCreateTagBuilder(ItemTags.FENCE_GATES).add(BlockRegistry.HOGDEW_FENCE_GATE.get().asItem());
        this.getOrCreateTagBuilder(ItemTags.DOORS).add(BlockRegistry.HOGDEW_DOOR.get().asItem());
        this.getOrCreateTagBuilder(ItemTags.LOGS).addTag(GBItemTags.HOGDEW_LOG);
    }
}
