package com.github.teamfusion.greedandbleed.client.network;

import com.blackgear.platform.core.network.base.Packet;
import com.blackgear.platform.core.network.base.PacketContext;
import com.blackgear.platform.core.network.base.PacketHandler;
import com.github.teamfusion.greedandbleed.GreedAndBleed;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;

import java.util.Optional;

public record ClientboundHurtPacket(int id, float damage, String damageType) implements Packet<ClientboundHurtPacket> {
    public static final ResourceLocation ID = new ResourceLocation(GreedAndBleed.MOD_ID, "hurt");
    public static final Handler HANDLER = new Handler();
    
    @Override
    public ResourceLocation getId() {
        return ID;
    }
    
    @Override
    public PacketHandler<ClientboundHurtPacket> getHandler() {
        return HANDLER;
    }
    
    public static class Handler implements PacketHandler<ClientboundHurtPacket> {
        @Override
        public void encode(ClientboundHurtPacket packet, FriendlyByteBuf buf) {
            buf.writeInt(packet.id);
            buf.writeFloat(packet.damage);
            buf.writeUtf(packet.damageType);
        }
        
        @Override
        public ClientboundHurtPacket decode(FriendlyByteBuf buf) {
            return new ClientboundHurtPacket(buf.readInt(), buf.readFloat(), buf.readUtf());
        }
        
        @Override
        public PacketContext handle(ClientboundHurtPacket packet) {
            return (player, level) -> {
                Entity parent = level.getEntity(packet.id);
                Optional<Registry<DamageType>> registry = level.registryAccess().registry(Registries.DAMAGE_TYPE);
                registry.ifPresent(reg -> {
                    DamageType damage = reg.get(new ResourceLocation(packet.damageType));
                    if (damage != null) {
                        Holder<DamageType> holder = reg.getHolder(reg.getId(damage)).orElseGet(null);
                        if (holder != null) {
                            DamageSource source = new DamageSource(reg.getHolder(reg.getId(damage)).get());
                            if (parent != null) {
                                parent.hurt(source, packet.damage);
                            }
                        }
                    }
                });
            };
        }
    }
}