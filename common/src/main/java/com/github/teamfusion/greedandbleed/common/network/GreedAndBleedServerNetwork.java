package com.github.teamfusion.greedandbleed.common.network;

import com.github.teamfusion.greedandbleed.common.item.SlingshotPouchItem;
import com.github.teamfusion.greedandbleed.common.registry.ItemRegistry;
import dev.architectury.networking.NetworkManager;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class GreedAndBleedServerNetwork implements GreedAndBleedNetwork {

    public static void registerReceivers() {
        NetworkManager.registerReceiver(NetworkManager.Side.C2S, SELECT_SYNC_PACKET, GreedAndBleedServerNetwork::onSelectSync);
    }

    private static void onSelectSync(FriendlyByteBuf friendlyByteBuf, NetworkManager.PacketContext packetContext) {
        int id = friendlyByteBuf.readInt();
        Player player = Minecraft.getInstance().player;
        ItemStack pouch = player.getMainHandItem().is(ItemRegistry.SLINGSHOT_POUCH.get()) ? player.getMainHandItem() : player.getOffhandItem().is(ItemRegistry.SLINGSHOT_POUCH.get()) ? player.getOffhandItem() : ItemStack.EMPTY;

        if (!pouch.isEmpty()) {
            SlingshotPouchItem.cycle(id, pouch);
        }
    }
}
