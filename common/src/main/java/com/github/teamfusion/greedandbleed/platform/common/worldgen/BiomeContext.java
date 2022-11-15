package com.github.teamfusion.greedandbleed.platform.common.worldgen;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;

public interface BiomeContext {
    boolean is(ResourceKey<Biome> biome);
}