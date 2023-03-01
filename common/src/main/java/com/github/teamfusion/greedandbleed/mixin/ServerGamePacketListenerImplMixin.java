package com.github.teamfusion.greedandbleed.mixin;

import com.github.teamfusion.greedandbleed.GreedAndBleed;
import com.github.teamfusion.greedandbleed.common.entity.HasMountInventory;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerGamePacketListenerImpl.class)
public class ServerGamePacketListenerImplMixin {
    @Shadow public ServerPlayer player;

    @Inject(method = "handlePlayerCommand", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/protocol/game/ServerboundPlayerCommandPacket;getAction()Lnet/minecraft/network/protocol/game/ServerboundPlayerCommandPacket$Action;"))
    private void gb$handleOpenMountInventory(ServerboundPlayerCommandPacket packet, CallbackInfo ci) {
        if (packet.getAction() == ServerboundPlayerCommandPacket.Action.OPEN_INVENTORY && this.player.getVehicle() instanceof HasMountInventory mount) {
            GreedAndBleed.LOGGER.debug("Server player {} is opening the mount inventory for {}", this.player, this.player.getVehicle());
            mount.openInventory(this.player);
        }
    }
}