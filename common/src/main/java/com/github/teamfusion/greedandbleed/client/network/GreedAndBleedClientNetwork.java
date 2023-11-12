package com.github.teamfusion.greedandbleed.client.network;

import com.github.teamfusion.greedandbleed.GreedAndBleed;
import com.github.teamfusion.greedandbleed.client.screen.HoglinInventoryScreen;
import com.github.teamfusion.greedandbleed.common.inventory.HoglinInventoryMenu;
import com.github.teamfusion.greedandbleed.common.network.GreedAndBleedNetwork;
import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.NetworkManager.Side;
import dev.architectury.networking.transformers.PacketSink;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.hoglin.Hoglin;

import java.util.function.Supplier;

public class GreedAndBleedClientNetwork implements GreedAndBleedNetwork {

    public static void registerReceivers() {
        NetworkManager.registerReceiver(Side.S2C, SCREEN_OPEN_PACKET, GreedAndBleedClientNetwork::onOpenScreen);
        NetworkManager.registerReceiver(Side.S2C, HURT_PACKET, GreedAndBleedClientNetwork::onHurt);
    }

    public static PacketSink ofTrackingEntity(final Supplier<Entity> entitySupplier) {
        return packet -> {
            final Entity entity = entitySupplier.get();
            ((ServerChunkCache) entity.getCommandSenderWorld().getChunkSource()).broadcastAndSend(entity, packet);
        };
    }

    static void onHurt(FriendlyByteBuf buf, NetworkManager.PacketContext context) {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer clientPlayer = Minecraft.getInstance().player;
        int id = buf.readInt();
        float damage = buf.readFloat();
        String damageType = buf.readUtf();
        if (minecraft.level != null) {
            Entity parent = minecraft.level.getEntity(id);
            Registry<DamageType> registry = minecraft.level.registryAccess().registry(Registries.DAMAGE_TYPE).get();
            DamageType dmg = registry.get(new ResourceLocation(damageType));
            if (dmg != null) {
                Holder<DamageType> holder = registry.getHolder(registry.getId(dmg)).orElseGet(null);
                if (holder != null) {
                    DamageSource source = new DamageSource(registry.getHolder(registry.getId(dmg)).get());
                    if (parent != null) {
                        parent.hurt(source, damage);
                    }
                }
            }
        }
    }

    static void onOpenScreen(FriendlyByteBuf buf, NetworkManager.PacketContext context) {
        LocalPlayer clientPlayer = Minecraft.getInstance().player;
        int id = buf.readInt();
        int container = buf.readInt();
        int containerId2 = buf.readInt();
        Minecraft.getInstance().execute(() -> {

            Entity entity = null;
            if (Minecraft.getInstance().level != null) {
                entity = Minecraft.getInstance().level.getEntity(id);
            }
            if (entity instanceof Hoglin hoglin) {
                GreedAndBleed.LOGGER.debug("Client is opening mount inventory for {}", entity);
                SimpleContainer inventory = new SimpleContainer(container);
                HoglinInventoryMenu hoglinInventoryContainer = new HoglinInventoryMenu(containerId2, inventory, clientPlayer.getInventory(), hoglin);
                clientPlayer.containerMenu = hoglinInventoryContainer;
                HoglinInventoryScreen hoglinInventoryScreen = new HoglinInventoryScreen(hoglinInventoryContainer, clientPlayer.getInventory(), hoglin);
                Minecraft.getInstance().setScreen(hoglinInventoryScreen);
            }
        });
    }
}