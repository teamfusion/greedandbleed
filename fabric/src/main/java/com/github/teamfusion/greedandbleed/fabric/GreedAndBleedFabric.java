package com.github.teamfusion.greedandbleed.fabric;

import com.github.teamfusion.greedandbleed.GreedAndBleed;
import net.fabricmc.api.ModInitializer;

public class GreedAndBleedFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        GreedAndBleed.bootstrap();
    }
}