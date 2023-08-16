package com.github.teamfusion.greedandbleed.platform.common.worldgen.forge;

import com.github.teamfusion.greedandbleed.GreedAndBleed;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = GreedAndBleed.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class BiomeManagerImpl {
    public static void setup() {
    }

    public static <T extends Mob> void registrySpawnPlacement(EntityType<T> entityType, SpawnPlacements.Type placementType, Heightmap.Types heightTypes, SpawnPlacements.SpawnPredicate<T> spawnPredicate) {
        SpawnPlacements.register(entityType, placementType, heightTypes, spawnPredicate);
    }

    //TODO Rewrite to 1.19.4
    /*@SubscribeEvent
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
    }*/
}