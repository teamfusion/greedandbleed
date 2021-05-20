package com.infernalstudios.greedandbleed.client.network;

import com.infernalstudios.greedandbleed.GreedAndBleed;
import com.infernalstudios.greedandbleed.client.screen.HoglinInventoryScreen;
import com.infernalstudios.greedandbleed.common.inventory.HoglinInventoryContainer;
import com.infernalstudios.greedandbleed.server.network.packet.SOpenHoglinWindowPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.inventory.Inventory;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientNetworkHandler {

        public static void handleHoglinScreenOpen(SOpenHoglinWindowPacket packet, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                Minecraft minecraft = Minecraft.getInstance();
                ClientPlayerEntity clientPlayer = minecraft.player;
                Entity entity = null;
                if (clientPlayer != null) {
                    entity = clientPlayer.level.getEntity(packet.getEntityId());
                }
                if (entity instanceof AnimalEntity) {
                    GreedAndBleed.LOGGER.debug("Client is opening mount inventory for {}", entity);
                    AnimalEntity hoglin = (AnimalEntity)entity;
                    Inventory inventory = new Inventory(packet.getSize());
                    HoglinInventoryContainer hoglinInventoryContainer = new HoglinInventoryContainer(packet.getContainerId(), clientPlayer.inventory, inventory, hoglin);
                    clientPlayer.containerMenu = hoglinInventoryContainer;
                    HoglinInventoryScreen hoglinInventoryScreen = new HoglinInventoryScreen(hoglinInventoryContainer, clientPlayer.inventory, hoglin);
                    minecraft.setScreen(hoglinInventoryScreen);
                }
            }
            );
        }
    }