package com.github.teamfusion.greedandbleed.platform.common.worldgen.fabric;

import com.github.teamfusion.greedandbleed.GreedAndBleed;
import com.github.teamfusion.greedandbleed.common.registry.BiomeRegistry;
import com.github.teamfusion.greedandbleed.platform.common.worldgen.BiomeContext;
import com.github.teamfusion.greedandbleed.platform.common.worldgen.BiomeManager;
import com.github.teamfusion.greedandbleed.platform.common.worldgen.BiomeWriter;
import net.fabricmc.fabric.api.biome.v1.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class BiomeManagerImpl {
    public static void setup() {
        BiomeModifications.create(new ResourceLocation(GreedAndBleed.MOD_ID, "biome_modifier")).add(ModificationPhase.ADDITIONS, context -> true, (selector, modifier) -> {
            BiomeManager.INSTANCE.register(new FabricBiomeWriter(selector, modifier));
        });
        NetherBiomes.addNetherBiome(BiomeRegistry.HOGDEW_HOLLOW, Climate.parameters(0.475F, 0.6f, 0.0f, 0.0f, 0.0f, 0.0f, 0.125F));
    }

    public static <T extends Mob> void registrySpawnPlacement(EntityType<T> entityType, SpawnPlacements.Type placementType, Heightmap.Types heightTypes, SpawnPlacements.SpawnPredicate<T> spawnPredicate) {
        SpawnPlacements.register(entityType, placementType, heightTypes, spawnPredicate);
    }

    static class FabricBiomeWriter extends BiomeWriter {
        private final BiomeSelectionContext selector;
        private final BiomeModificationContext modifier;

        FabricBiomeWriter(BiomeSelectionContext selector, BiomeModificationContext modifier) {
            this.selector = selector;
            this.modifier = modifier;
        }

        @Override
        public ResourceLocation name() {
            return this.selector.getBiomeKey().location();
        }

        @Override
        public ResourceKey<Biome> key() {
            return this.selector.getBiomeKey();
        }

        @Override
        public BiomeContext context() {
            return biome -> FabricBiomeWriter.this.key() == biome;
        }

        @Override
        public void addFeature(GenerationStep.Decoration step, ResourceKey<PlacedFeature> feature) {
            this.modifier.getGenerationSettings().addFeature(step, feature);
        }

        @Override
        public void addSpawn(MobCategory category, EntityType<?> type, int weight, int minGroup, int maxGroup) {
            this.modifier.getSpawnSettings().addSpawn(category, new MobSpawnSettings.SpawnerData(type, weight, minGroup, maxGroup));
        }
    }
}