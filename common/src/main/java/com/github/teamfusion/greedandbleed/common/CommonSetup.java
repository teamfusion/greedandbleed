package com.github.teamfusion.greedandbleed.common;

import com.github.teamfusion.greedandbleed.common.entity.ToleratingMount;
import com.github.teamfusion.greedandbleed.common.entity.piglin.Hoglet;
import com.github.teamfusion.greedandbleed.common.entity.piglin.SkeletalPiglin;
import com.github.teamfusion.greedandbleed.common.network.GreedAndBleedNetwork;
import com.github.teamfusion.greedandbleed.common.registry.EntityTypeRegistry;
import com.github.teamfusion.greedandbleed.platform.common.MobRegistry;
import com.github.teamfusion.greedandbleed.platform.common.worldgen.BiomeManager;
import dev.architectury.registry.level.biome.BiomeModifications;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.Heightmap;

public class CommonSetup {
    public static void onBootstrap() {
        MobRegistry.attributes(EntityTypeRegistry.SKELETAL_PIGLIN, SkeletalPiglin::setCustomAttributes);
        MobRegistry.attributes(EntityTypeRegistry.HOGLET, Hoglet::setCustomAttributes);
    }

    public static void onInitialized() {
        BiomeManager.registrySpawnPlacement(EntityTypeRegistry.HOGLET.get(), SpawnPlacements.Type.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Hoglet::checkHogletSpawnRules);
        BiomeManager.registrySpawnPlacement(EntityTypeRegistry.SKELETAL_PIGLIN.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);

        BiomeModifications.addProperties((biomeContext, mutable) -> {
            if (Biomes.CRIMSON_FOREST.location() == biomeContext.getKey().get()) {
                mutable.getSpawnProperties().addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityTypeRegistry.HOGLET.get(), 8, 4, 4));
            }

            if (Biomes.SOUL_SAND_VALLEY.location() == biomeContext.getKey().get()) {
                mutable.getSpawnProperties().addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityTypeRegistry.SKELETAL_PIGLIN.get(), 10, 4, 5));
                mutable.getSpawnProperties().setSpawnCost(EntityTypeRegistry.SKELETAL_PIGLIN.get(), 0.7, 0.15);
            }
        });

        BiomeManager.setup();
        GreedAndBleedNetwork.registerReceivers();
    }

    public static void onHoglinAttack(LivingEntity entity, DamageSource source) {
        if (source.getDirectEntity() instanceof ToleratingMount mount && !entity.level().isClientSide) {
            if (mount.getTolerance() > 0) {
                mount.addTolerance(-1);
            } else if (mount.getTolerance() < 0) {
                mount.setTolerance(0);
            }
        }
    }

    public static void onHoglinUpdate(LivingEntity entity) {
        if (entity instanceof ToleratingMount mount && !entity.level().isClientSide) {
            if (mount.getTolerance() > 0) {
                mount.addTolerance(-1);
            } else if (mount.getTolerance() < 0) {
                mount.setTolerance(0);
            }
        }
    }
}