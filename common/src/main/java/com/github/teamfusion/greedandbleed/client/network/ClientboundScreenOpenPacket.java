package com.github.teamfusion.greedandbleed.client.network;

import com.blackgear.platform.core.network.base.Packet;
import com.blackgear.platform.core.network.base.PacketContext;
import com.blackgear.platform.core.network.base.PacketHandler;
import com.github.teamfusion.greedandbleed.GreedAndBleed;
import com.github.teamfusion.greedandbleed.client.screen.HoglinInventoryScreen;
import com.github.teamfusion.greedandbleed.common.inventory.HoglinInventoryMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.hoglin.Hoglin;

public record ClientboundScreenOpenPacket(int id, int container, int containerId) implements Packet<ClientboundScreenOpenPacket> {
    public static final ResourceLocation ID = new ResourceLocation(GreedAndBleed.MOD_ID, "screen_open");
    public static final Handler HANDLER = new Handler();
    
    @Override
    public ResourceLocation getId() {
        return ID;
    }
    
    @Override
    public PacketHandler<ClientboundScreenOpenPacket> getHandler() {
        return HANDLER;
    }
    
    public static class Handler implements PacketHandler<ClientboundScreenOpenPacket> {
        @Override
        public void encode(ClientboundScreenOpenPacket packet, FriendlyByteBuf buf) {
        
        }
        
        @Override
        public ClientboundScreenOpenPacket decode(FriendlyByteBuf buf) {
            return new ClientboundScreenOpenPacket(buf.readInt(), buf.readInt(), buf.readInt());
        }
        
        @Override
        public PacketContext handle(ClientboundScreenOpenPacket packet) {
            return (player, level) -> Minecraft.getInstance().execute(() -> {
                Entity entity = level.getEntity(packet.id);
                if (entity instanceof Hoglin hoglin) {
                    GreedAndBleed.LOGGER.debug("Client is opening mount inventory for {}", entity);
                    SimpleContainer inventory = new SimpleContainer(packet.container);
                    HoglinInventoryMenu hoglinInventoryContainer = new HoglinInventoryMenu(packet.containerId, inventory, player.getInventory(), hoglin);
                    player.containerMenu = hoglinInventoryContainer;
                    HoglinInventoryScreen hoglinInventoryScreen = new HoglinInventoryScreen(hoglinInventoryContainer, player.getInventory(), hoglin);
                    Minecraft.getInstance().setScreen(hoglinInventoryScreen);
                }
            });
        }
    }
}