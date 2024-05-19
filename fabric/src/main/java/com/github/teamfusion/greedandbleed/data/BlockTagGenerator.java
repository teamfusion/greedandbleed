package com.github.teamfusion.greedandbleed.data;

import com.github.teamfusion.greedandbleed.common.registry.BlockRegistry;
import com.github.teamfusion.greedandbleed.common.registry.GBBlockTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.BlockTags;

import java.util.concurrent.CompletableFuture;

public class BlockTagGenerator extends FabricTagProvider.BlockTagProvider {

    public BlockTagGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider arg) {
        this.getOrCreateTagBuilder(GBBlockTags.HOGDEW_LOG).add(BlockRegistry.HOGDEW_STEM.get()).add(BlockRegistry.STRIPPED_HOGDEW_STEM.get());
        this.getOrCreateTagBuilder(BlockTags.MINEABLE_WITH_AXE).addTag(GBBlockTags.HOGDEW_LOG).add(BlockRegistry.HOGDEW_DOOR.get()).add(BlockRegistry.HOGDEW_FENCE.get()).add(BlockRegistry.HOGDEW_FENCE_GATE.get())
                .add(BlockRegistry.HOGDEW_TRAPDOOR.get()).add(BlockRegistry.HOGDEW_DOOR.get()).add(BlockRegistry.HOGDEW_PLANKS_SLAB.get()).add(BlockRegistry.HOGDEW_PLANKS_STAIRS.get());
        this.getOrCreateTagBuilder(BlockTags.MINEABLE_WITH_PICKAXE).add(BlockRegistry.HOGDEW_NYLIUM.get());
        this.getOrCreateTagBuilder(BlockTags.MINEABLE_WITH_HOE).add(BlockRegistry.HOGDEW_FUNGUS.get()).add(BlockRegistry.HOGDEW_CLUSTER.get()).add(BlockRegistry.HOGDEW_WART_BLOCK.get()).add(BlockRegistry.HOGDEW_WART_BLOCK.get());
        this.getOrCreateTagBuilder(BlockTags.NYLIUM).add(BlockRegistry.HOGDEW_NYLIUM.get());
        this.getOrCreateTagBuilder(BlockTags.PLANKS).add(BlockRegistry.HOGDEW_PLANKS.get());
        this.getOrCreateTagBuilder(BlockTags.WOODEN_STAIRS).add(BlockRegistry.HOGDEW_PLANKS_STAIRS.get());
        this.getOrCreateTagBuilder(BlockTags.WOODEN_SLABS).add(BlockRegistry.HOGDEW_PLANKS_SLAB.get());
        this.getOrCreateTagBuilder(BlockTags.WOODEN_FENCES).add(BlockRegistry.HOGDEW_FENCE.get());
        this.getOrCreateTagBuilder(BlockTags.FENCE_GATES).add(BlockRegistry.HOGDEW_FENCE_GATE.get());
        this.getOrCreateTagBuilder(BlockTags.DOORS).add(BlockRegistry.HOGDEW_DOOR.get());
    }
}
