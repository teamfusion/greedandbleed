package com.github.teamfusion.greedandbleed.common;

import com.github.teamfusion.greedandbleed.common.entity.piglin.Hoglet;
import com.github.teamfusion.greedandbleed.common.entity.piglin.SkeletalPiglin;
import com.github.teamfusion.greedandbleed.common.registry.EntityTypeRegistry;
import com.github.teamfusion.greedandbleed.platform.common.MobRegistry;
import com.github.teamfusion.greedandbleed.platform.common.worldgen.BiomeManager;

public class CommonSetup {
    public static void onBootstrap() {
        MobRegistry.attributes(EntityTypeRegistry.SKELETAL_PIGLIN, SkeletalPiglin::setCustomAttributes);
        MobRegistry.attributes(EntityTypeRegistry.HOGLET, Hoglet::setCustomAttributes);
    }

    public static void onInitialized() {
        BiomeManager.setup();
    }
}