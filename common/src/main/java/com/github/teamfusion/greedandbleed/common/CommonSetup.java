package com.github.teamfusion.greedandbleed.common;

import com.github.teamfusion.greedandbleed.platform.common.worldgen.BiomeManager;

public class CommonSetup {
    public static void onBootstrap() {

    }

    public static void onInitialize() {
        BiomeManager.setup();
    }
}