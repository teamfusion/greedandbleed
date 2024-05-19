package com.github.teamfusion.greedandbleed.common.registry;

import com.github.teamfusion.greedandbleed.GreedAndBleed;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class GBBlockTags {
    public static final TagKey<Block> HOGDEW_LOG = create("hogdew_log");

    private static TagKey<Block> create(String string) {
        return TagKey.create(Registries.BLOCK, new ResourceLocation(GreedAndBleed.MOD_ID, string));
    }
}
