package com.github.teamfusion.greedandbleed.common.registry;

import com.github.teamfusion.greedandbleed.common.entity.piglin.HogletEntity;
import com.github.teamfusion.greedandbleed.common.entity.piglin.PigmyEntity;
import com.github.teamfusion.greedandbleed.common.entity.piglin.SkeletalPiglinEntity;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.world.gen.Heightmap;

public class SpawnPlacementRegistry {

    public static void register() {
        EntitySpawnPlacementRegistry.register(EntityTypeRegistry.PIGMY.get(),
                EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, PigmyEntity::checkPygmySpawnRules);

        EntitySpawnPlacementRegistry.register(EntityTypeRegistry.SKELETAL_PIGLIN.get(),
                EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, SkeletalPiglinEntity::checkSkeletalPiglinSpawnRules);
        EntitySpawnPlacementRegistry.register(EntityTypeRegistry.HOGLET.get(),
                EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, HogletEntity::checkHogletSpawnRules);
    }
}
