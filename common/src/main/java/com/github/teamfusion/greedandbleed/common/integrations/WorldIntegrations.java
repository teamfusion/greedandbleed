package com.github.teamfusion.greedandbleed.common.integrations;

import com.blackgear.platform.common.worldgen.modifier.BiomeContext;
import com.blackgear.platform.common.worldgen.modifier.BiomeWriter;
import com.github.teamfusion.greedandbleed.common.registry.EntityTypeRegistry;
import net.minecraft.world.level.biome.Biomes;

public class WorldIntegrations {
    public static void bootstrap(BiomeWriter writer, BiomeContext context) {
        if (context.is(Biomes.CRIMSON_FOREST)) {
            writer.addSpawn(EntityTypeRegistry.HOGLET.get(), 8, 2, 4);
        }

        if (context.is(Biomes.WARPED_FOREST)) {
            writer.addSpawn(EntityTypeRegistry.WARPED_PIGLIN.get(), 1, 4, 4);
            writer.setSpawnCost(EntityTypeRegistry.WARPED_PIGLIN.get(), 1.0, 0.01);
        }

        if (context.is(Biomes.SOUL_SAND_VALLEY)) {
            writer.addSpawn(EntityTypeRegistry.SKELETAL_PIGLIN.get(), 10, 4, 5);
            writer.setSpawnCost(EntityTypeRegistry.SKELETAL_PIGLIN.get(), 0.7, 0.15);
        }
    }
}