package com.github.teamfusion.greedandbleed.forge;

import com.github.teamfusion.greedandbleed.GreedAndBleed;
import net.minecraftforge.fml.common.Mod;

@Mod(GreedAndBleed.MOD_ID)
public class GreedAndBleedForge {
    public GreedAndBleedForge() {
        GreedAndBleed.bootstrap();
    }
}