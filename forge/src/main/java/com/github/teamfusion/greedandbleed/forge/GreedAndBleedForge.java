package com.github.teamfusion.greedandbleed.forge;

import com.github.teamfusion.greedandbleed.GreedAndBleed;
import com.github.teamfusion.greedandbleed.platform.common.worldgen.BiomeManager;
import net.minecraftforge.fml.common.Mod;

@Mod(GreedAndBleed.MOD_ID)
public class GreedAndBleedForge {
    public GreedAndBleedForge() {
        GreedAndBleed.bootstrap();
        BiomeManager.setup();
    }
}