package com.github.teamfusion.greedandbleed.client.network;

import com.github.teamfusion.greedandbleed.common.network.GreedAndBleedNetwork;
import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.NetworkManager.Side;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;

public class GreedAndBleedClientNetwork implements GreedAndBleedNetwork {

    public static void registerReceivers() {
        NetworkManager.registerReceiver(Side.S2C, SCREEN_OPEN_PACKET, GreedAndBleedClientNetwork::onOpenScreen);
    }


    static void onOpenScreen(FriendlyByteBuf buf, NetworkManager.PacketContext context) {
        Minecraft client = Minecraft.getInstance();
        client.execute(() -> {
            ClientNetworkHandler.handleHoglinScreenOpen(buf, client);
        });
    }
}