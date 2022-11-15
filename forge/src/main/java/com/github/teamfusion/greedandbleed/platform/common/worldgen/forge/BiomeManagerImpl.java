package com.github.teamfusion.greedandbleed.platform.common.worldgen.forge;

import com.github.teamfusion.greedandbleed.GreedAndBleed;
import com.github.teamfusion.greedandbleed.platform.common.worldgen.BiomeContext;
import com.github.teamfusion.greedandbleed.platform.common.worldgen.BiomeManager;
import com.github.teamfusion.greedandbleed.platform.common.worldgen.BiomeWriter;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = GreedAndBleed.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class BiomeManagerImpl {
    public static void setup() {}

    @SubscribeEvent
    public static void event(BiomeLoadingEvent event) {
        BiomeManager.INSTANCE.register(new ForgeBiomeWriter(event));
    }

    static class ForgeBiomeWriter extends BiomeWriter {
        private final BiomeLoadingEvent event;

        ForgeBiomeWriter(BiomeLoadingEvent event) {
            this.event = event;
        }

        @Override
        public ResourceLocation name() {
            return this.event.getName();
        }

        @Override
        public ResourceKey<Biome> key() {
            return ResourceKey.create(Registry.BIOME_REGISTRY, ForgeBiomeWriter.this.name());
        }

        @Override
        public BiomeContext context() {
            return biome -> ForgeBiomeWriter.this.key() == biome;
        }

        @Override
        public void addFeature(GenerationStep.Decoration step, Holder<PlacedFeature> feature) {
            this.event.getGeneration().addFeature(step, feature);
        }

        @Override
        public void addSpawn(MobCategory category, EntityType<?> type, int weight, int minGroup, int maxGroup) {
            this.event.getSpawns().addSpawn(category, new MobSpawnSettings.SpawnerData(type, weight, minGroup, maxGroup));
        }
    }
}