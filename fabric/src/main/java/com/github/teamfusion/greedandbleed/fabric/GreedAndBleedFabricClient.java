package com.github.teamfusion.greedandbleed.fabric;

import com.github.teamfusion.greedandbleed.client.screen.HoglinInventoryScreen;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screens.MenuScreens;

public class GreedAndBleedFabricClient implements ClientModInitializer {


    @Override
    public void onInitializeClient() {
        MenuScreens.register(GreedAndBleedFabric.HOGLIN_MENU_HANDLER, HoglinInventoryScreen::new);
    }


}