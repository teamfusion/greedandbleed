package com.github.teamfusion.greedandbleed.core;

import com.github.teamfusion.greedandbleed.GreedAndBleed;
import net.minecraft.resources.ResourceLocation;

public final class NetworkHandler {
    public static void bootstrap() {
    
    }
    
    public static final ResourceLocation
            SELECT_SYNC_PACKET = new ResourceLocation(GreedAndBleed.MOD_ID, "select_sync_packet");
    public static final ResourceLocation
            RECRUIT_PACKET = new ResourceLocation(GreedAndBleed.MOD_ID, "recruit_packet");
    public static final ResourceLocation
            PATROL_RANGE_PACKET = new ResourceLocation(GreedAndBleed.MOD_ID, "patrol_range_packet");
    static void registerReceivers() {
        //NetworkManager.registerReceiver(Side.S2C, SCREEN_OPEN_PACKET, GreedAndBleedNetwork::onOpenScreen);
    }


}