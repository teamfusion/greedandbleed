package com.github.teamfusion.greedandbleed.client.network;

import com.github.teamfusion.greedandbleed.GreedAndBleed;
import com.github.teamfusion.greedandbleed.client.screen.HoglinInventoryScreen;
import com.github.teamfusion.greedandbleed.common.inventory.HoglinInventoryMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.hoglin.Hoglin;

public class ClientNetworkHandler {
    public static void handleHoglinScreenOpen(FriendlyByteBuf buf, Minecraft minecraft) {
        LocalPlayer clientPlayer = minecraft.player;
        Entity entity = null;
        if (minecraft.level != null) {
            entity = minecraft.level.getEntity(buf.readInt());
        }
        if (entity instanceof Hoglin hoglin) {
            GreedAndBleed.LOGGER.debug("Client is opening mount inventory for {}", entity);
            SimpleContainer inventory = new SimpleContainer(buf.readInt());
            HoglinInventoryMenu hoglinInventoryContainer = new HoglinInventoryMenu(buf.readInt(), inventory, clientPlayer.getInventory(), hoglin);
            clientPlayer.containerMenu = hoglinInventoryContainer;
            HoglinInventoryScreen hoglinInventoryScreen = new HoglinInventoryScreen(hoglinInventoryContainer, clientPlayer.getInventory(), hoglin);
            minecraft.setScreen(hoglinInventoryScreen);
        }
    }
}