package com.github.teamfusion.greedandbleed.common.registry;

import com.github.teamfusion.greedandbleed.GreedAndBleed;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class GBItemTags {
    public static final TagKey<Item> HOGDEW_LOG = create("hogdew_log");

    private static TagKey<Item> create(String string) {
        return TagKey.create(Registries.ITEM, new ResourceLocation(GreedAndBleed.MOD_ID, string));
    }
}
