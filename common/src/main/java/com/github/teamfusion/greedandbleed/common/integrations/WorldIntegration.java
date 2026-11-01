package com.github.teamfusion.greedandbleed.common.integrations;

import com.blackgear.platform.common.worldgen.modifier.BiomeContext;
import com.blackgear.platform.common.worldgen.modifier.BiomeWriter;
import com.github.teamfusion.greedandbleed.common.registry.EntityTypeRegistry;
import dev.architectury.registry.level.biome.BiomeModifications;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.MobSpawnSettings;

public class WorldIntegration {
    public static void bootstrap(BiomeWriter writer, BiomeContext context) {
        BiomeModifications.addProperties((biomeContext, mutable) -> {
            if (Biomes.CRIMSON_FOREST.location() == biomeContext.getKey().get()) {
                mutable.getSpawnProperties().addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityTypeRegistry.HOGLET.get(), 8, 2, 4));
            }
            
            if (Biomes.WARPED_FOREST.location() == biomeContext.getKey().get()) {
                mutable.getSpawnProperties().addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityTypeRegistry.WARPED_PIGLIN.get(), 1, 4, 4));
                mutable.getSpawnProperties().setSpawnCost(EntityTypeRegistry.WARPED_PIGLIN.get(), 1.0, 0.01);
            }
            
            if (Biomes.SOUL_SAND_VALLEY.location() == biomeContext.getKey().get()) {
                mutable.getSpawnProperties().addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityTypeRegistry.SKELETAL_PIGLIN.get(), 10, 4, 5));
                mutable.getSpawnProperties().setSpawnCost(EntityTypeRegistry.SKELETAL_PIGLIN.get(), 0.7, 0.15);
            }
        });
        
//        if (context.is(Biomes.CRIMSON_FOREST)) {
//            writer.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityTypeRegistry.HOGLET.get(), 8, 2, 4));
//        }
//
//        if (context.is(Biomes.WARPED_FOREST)) {
//            writer.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityTypeRegistry.WARPED_PIGLIN.get(), 1, 4, 4));
////            mutable.getSpawnProperties().setSpawnCost(EntityTypeRegistry.WARPED_PIGLIN.get(), 1.0, 0.01);
//        }
//
//        if (context.is(Biomes.SOUL_SAND_VALLEY)) {
//            writer.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityTypeRegistry.SKELETAL_PIGLIN.get(), 10, 4, 5));
////            mutable.getSpawnProperties().setSpawnCost(EntityTypeRegistry.SKELETAL_PIGLIN.get(), 0.7, 0.15);
//        }
    }
}