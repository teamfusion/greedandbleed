package com.github.teamfusion.greedandbleed.platform.common.worldgen;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.Arrays;
import java.util.function.BiConsumer;

public abstract class BiomeWriter {
    protected void add(BiConsumer<BiomeWriter, BiomeContext> modifier) {
        modifier.accept(this, this.context());
    }

    @SafeVarargs
    protected final void add(BiConsumer<BiomeWriter, BiomeContext> modifier, ResourceKey<Biome>... biomes) {
        Arrays.stream(biomes).forEach(biome -> {
            if (biome == this.key()) modifier.accept(this, this.context());
        });
    }

    public abstract ResourceLocation name();

    public abstract ResourceKey<Biome> key();

    public abstract BiomeContext context();

    public abstract void addFeature(GenerationStep.Decoration step, ResourceKey<PlacedFeature> feature);

    public abstract void addSpawn(MobCategory category, EntityType<?> type, int weight, int minGroup, int maxGroup);
}