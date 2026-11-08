package com.github.teamfusion.greedandbleed.common;

import com.blackgear.platform.common.data.LootModifier;
import com.blackgear.platform.common.integration.BlockIntegration;
import com.blackgear.platform.common.integration.MobIntegration;
import com.blackgear.platform.common.worldgen.modifier.BiomeManager;
import com.blackgear.platform.core.ParallelDispatch;
import com.github.teamfusion.greedandbleed.common.integrations.BlockIntegrations;
import com.github.teamfusion.greedandbleed.common.integrations.LootIntegrations;
import com.github.teamfusion.greedandbleed.common.integrations.MobIntegrations;
import com.github.teamfusion.greedandbleed.common.integrations.WorldIntegrations;
import com.github.teamfusion.greedandbleed.common.item.slingshot.SlingshotItem;
import com.github.teamfusion.greedandbleed.common.network.GreedAndBleedNetwork;
import com.github.teamfusion.greedandbleed.common.network.GreedAndBleedServerNetwork;

public class CommonSetup {
    public static void onBootstrap() {
        MobIntegration.registerIntegrations(MobIntegrations::bootstrap);
    }
    
    public static void onInitialized(ParallelDispatch dispatch) {
        dispatch.enqueueWork(() -> {
            BiomeManager.add(WorldIntegrations::bootstrap);
            BlockIntegration.registerIntegrations(BlockIntegrations::bootstrap);
            LootModifier.modify(LootIntegrations.INSTANCE);
            
            SlingshotItem.bootstrap();
        });
        
        GreedAndBleedNetwork.registerReceivers();
        GreedAndBleedServerNetwork.registerReceivers();
    }
}