package com.infernalstudios.greedandbleed.common.registry;

import com.infernalstudios.greedandbleed.common.entity.piglin.PygmyEntity;
import com.infernalstudios.greedandbleed.common.entity.piglin.SkeletalPiglinEntity;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.world.gen.Heightmap;

public class SpawnPlacementRegistry {

    public static void register(){
        EntitySpawnPlacementRegistry.register(EntityTypeRegistry.PYGMY.get(),
                EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, PygmyEntity::checkPygmySpawnRules);

        EntitySpawnPlacementRegistry.register(EntityTypeRegistry.SKELETAL_PIGLIN.get(),
                EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, SkeletalPiglinEntity::checkSkeletalPiglinSpawnRules);
    }
}
