package com.github.teamfusion.greedandbleed.client.network;

import com.github.teamfusion.greedandbleed.GreedAndBleed;
import com.github.teamfusion.greedandbleed.client.screen.HoglinInventoryScreen;
import com.github.teamfusion.greedandbleed.common.inventory.HoglinInventoryMenu;
import com.github.teamfusion.greedandbleed.common.network.GreedAndBleedNetwork;
import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.NetworkManager.Side;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.hoglin.Hoglin;

public class GreedAndBleedClientNetwork implements GreedAndBleedNetwork {

    public static void registerReceivers() {
        NetworkManager.registerReceiver(Side.S2C, SCREEN_OPEN_PACKET, GreedAndBleedClientNetwork::onOpenScreen);
    }


    static void onOpenScreen(FriendlyByteBuf buf, NetworkManager.PacketContext context) {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer clientPlayer = Minecraft.getInstance().player;
        int id = buf.readInt();
        int containerId = buf.readInt();
        int containerId2 = buf.readInt();
        minecraft.execute(() -> {
            Entity entity = null;
            if (Minecraft.getInstance().level != null) {
                entity = Minecraft.getInstance().level.getEntity(id);
            }
            if (entity instanceof Hoglin hoglin) {
                GreedAndBleed.LOGGER.debug("Client is opening mount inventory for {}", entity);
                SimpleContainer inventory = new SimpleContainer(containerId);
                HoglinInventoryMenu hoglinInventoryContainer = new HoglinInventoryMenu(containerId2, inventory, clientPlayer.getInventory(), hoglin);
                clientPlayer.containerMenu = hoglinInventoryContainer;
                HoglinInventoryScreen hoglinInventoryScreen = new HoglinInventoryScreen(hoglinInventoryContainer, clientPlayer.getInventory(), hoglin);
                Minecraft.getInstance().setScreen(hoglinInventoryScreen);
            }
        });
    }
}