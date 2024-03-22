package com.github.teamfusion.greedandbleed.fabric;

import com.github.teamfusion.greedandbleed.GreedAndBleed;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;

public class GreedAndBleedFabricClient implements ClientModInitializer {


    @Override
    public void onInitializeClient() {
        ModelLoadingRegistry.INSTANCE.registerModelProvider((manager, out) -> {
            out.accept(new ModelResourceLocation(
                    new ResourceLocation(GreedAndBleed.MOD_ID, "slingshot_back_shooting"),
                    "inventory"));
        });
    }


}