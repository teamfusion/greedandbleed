package com.github.teamfusion.greedandbleed.common.registry;

import com.github.teamfusion.greedandbleed.GreedAndBleed;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public class GBBiomeTags {
    public static final TagKey<Biome> HAS_SHAMAN_BASE = create("has_structure/shaman_base");
    public static final TagKey<Biome> HAS_PYGMY_ENCAMPMENTS = create("has_structure/pygmy_encampments");

    private static TagKey<Biome> create(String string) {
        return TagKey.create(Registries.BIOME, new ResourceLocation(GreedAndBleed.MOD_ID, string));
    }
}
