package com.github.teamfusion.greedandbleed.common.registry;

import com.github.teamfusion.greedandbleed.GreedAndBleed;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public class GBEntityTypeTags {
    public static final TagKey<EntityType<?>> WOE_OF_SWINES_TARGET = create("woe_of_swines_target");

    private static TagKey<EntityType<?>> create(String string) {
        return TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(GreedAndBleed.MOD_ID, string));
    }
}
