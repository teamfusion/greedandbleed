package com.github.teamfusion.greedandbleed.common.network;

import com.blackgear.platform.core.network.base.Packet;
import com.blackgear.platform.core.network.base.PacketContext;
import com.blackgear.platform.core.network.base.PacketHandler;
import com.github.teamfusion.greedandbleed.GreedAndBleed;
import com.github.teamfusion.greedandbleed.common.item.slingshot.SlingshotPouchItem;
import com.github.teamfusion.greedandbleed.common.registry.ItemRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public record ServerboundSelectSyncPacket(int id) implements Packet<ServerboundSelectSyncPacket> {
    public static final ResourceLocation ID = new ResourceLocation(GreedAndBleed.MOD_ID, "select_sync");
    public static final Handler HANDLER = new Handler();
    
    @Override
    public ResourceLocation getId() {
        return ID;
    }
    
    @Override
    public PacketHandler<ServerboundSelectSyncPacket> getHandler() {
        return HANDLER;
    }
    
    public static class Handler implements PacketHandler<ServerboundSelectSyncPacket> {
        @Override
        public void encode(ServerboundSelectSyncPacket packet, FriendlyByteBuf buf) {
            buf.writeInt(packet.id);
        }
        
        @Override
        public ServerboundSelectSyncPacket decode(FriendlyByteBuf buf) {
            return new ServerboundSelectSyncPacket(buf.readInt());
        }
        
        @Override
        public PacketContext handle(ServerboundSelectSyncPacket packet) {
            return (player, level) -> {
                ItemStack pouch = player.getMainHandItem().is(ItemRegistry.SLINGSHOT_POUCH.get()) ? player.getMainHandItem() : player.getOffhandItem().is(ItemRegistry.SLINGSHOT_POUCH.get()) ? player.getOffhandItem() : ItemStack.EMPTY;
                
                if (!pouch.isEmpty()) {
                    SlingshotPouchItem.cycle(packet.id, pouch);
                }
            };
        }
    }
}