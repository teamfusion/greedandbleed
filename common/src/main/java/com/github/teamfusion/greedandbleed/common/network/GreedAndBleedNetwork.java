package com.github.teamfusion.greedandbleed.common.network;

import com.github.teamfusion.greedandbleed.GreedAndBleed;
import net.minecraft.resources.ResourceLocation;

public interface GreedAndBleedNetwork {
    ResourceLocation
            SCREEN_OPEN_PACKET = new ResourceLocation(GreedAndBleed.MOD_ID, "screen_open_packet");
    ResourceLocation
            HURT_PACKET = new ResourceLocation(GreedAndBleed.MOD_ID, "screen_open_packet");
    static void registerReceivers() {
        //NetworkManager.registerReceiver(Side.S2C, SCREEN_OPEN_PACKET, GreedAndBleedNetwork::onOpenScreen);
    }


}