package com.github.teamfusion.greedandbleed.common.integrations;

import com.blackgear.platform.common.integration.MobIntegration.Event;
import com.github.teamfusion.greedandbleed.common.entity.piglin.Hoglet;
import com.github.teamfusion.greedandbleed.common.entity.piglin.ShamanPiglin;
import com.github.teamfusion.greedandbleed.common.entity.piglin.SkeletalPiglin;
import com.github.teamfusion.greedandbleed.common.entity.piglin.WarpedPiglin;
import com.github.teamfusion.greedandbleed.common.entity.piglin.pigmy.Hoggart;
import com.github.teamfusion.greedandbleed.common.entity.piglin.pigmy.Pigmy;
import com.github.teamfusion.greedandbleed.common.entity.piglin.pigmy.Shrygmy;
import com.github.teamfusion.greedandbleed.common.entity.piglin.pigmy.ZombifiedPigmies;
import com.github.teamfusion.greedandbleed.common.registry.EntityTypeRegistry;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.levelgen.Heightmap;

public class MobIntegrations {
    public static void bootstrap(Event event) {
        registerPlacements(event);
        registerAttributes(event);
    }
    
    private static void registerAttributes(Event event) {
        event.registerAttributes(EntityTypeRegistry.SKELETAL_PIGLIN, SkeletalPiglin::setCustomAttributes);
        event.registerAttributes(EntityTypeRegistry.HOGLET, Hoglet::setCustomAttributes);
        event.registerAttributes(EntityTypeRegistry.SKELETON_HOGLET, Hoglet::setCustomAttributes);
        event.registerAttributes(EntityTypeRegistry.ZOMBIFIED_HOGLET, Hoglet::setCustomAttributes);
        event.registerAttributes(EntityTypeRegistry.SHAMAN_PIGLIN, ShamanPiglin::setCustomAttributes);
        event.registerAttributes(EntityTypeRegistry.PIGMY, Pigmy::setCustomAttributes);
        event.registerAttributes(EntityTypeRegistry.ZOMBIFIED_PIGMIES, ZombifiedPigmies::setCustomAttributes);
        event.registerAttributes(EntityTypeRegistry.SHRYGMY, Shrygmy::setCustomAttributes);
        event.registerAttributes(EntityTypeRegistry.HOGGART, Hoggart::setCustomAttributes);
        event.registerAttributes(EntityTypeRegistry.WARPED_PIGLIN, WarpedPiglin::setCustomAttributes);
    }
    
    private static void registerPlacements(Event event) {
        event.registerPlacement(EntityTypeRegistry.HOGLET, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Hoglet::checkHogletSpawnRules);
        event.registerPlacement(EntityTypeRegistry.ZOMBIFIED_HOGLET, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
        event.registerPlacement(EntityTypeRegistry.SKELETON_HOGLET, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
        event.registerPlacement(EntityTypeRegistry.SKELETAL_PIGLIN, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
        event.registerPlacement(EntityTypeRegistry.SHAMAN_PIGLIN, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
        event.registerPlacement(EntityTypeRegistry.HOGGART, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
        event.registerPlacement(EntityTypeRegistry.PIGMY, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
        event.registerPlacement(EntityTypeRegistry.ZOMBIFIED_PIGMIES, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
        event.registerPlacement(EntityTypeRegistry.SHRYGMY, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
        event.registerPlacement(EntityTypeRegistry.WARPED_PIGLIN, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
        
    }
}
