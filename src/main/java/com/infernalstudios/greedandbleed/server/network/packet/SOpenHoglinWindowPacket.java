package com.infernalstudios.greedandbleed.server.network.packet;

import com.infernalstudios.greedandbleed.client.network.ClientNetworkHandler;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SOpenHoglinWindowPacket {
    private final int containerId;
    private final int size;
    private final int entityId;

    public SOpenHoglinWindowPacket(int containerIdIn, int sizeIdIn, int entityIdIn) {
        this.containerId = containerIdIn;
        this.size = sizeIdIn;
        this.entityId = entityIdIn;
    }

    public static SOpenHoglinWindowPacket read(PacketBuffer buf) {
        int containerId = buf.readUnsignedByte();
        int size = buf.readVarInt();
        int entityId = buf.readInt();
        return new SOpenHoglinWindowPacket(containerId, size, entityId);
    }

    public static void write(SOpenHoglinWindowPacket packet, PacketBuffer buf) {
        buf.writeByte(packet.containerId);
        buf.writeVarInt(packet.size);
        buf.writeInt(packet.entityId);
    }

    public static void handle(SOpenHoglinWindowPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ClientNetworkHandler.handleHoglinScreenOpen(packet, ctx);
    }

    @OnlyIn(Dist.CLIENT)
    public int getContainerId() {
        return this.containerId;
    }

    @OnlyIn(Dist.CLIENT)
    public int getSize() {
        return this.size;
    }

    @OnlyIn(Dist.CLIENT)
    public int getEntityId() {
        return this.entityId;
    }
}
