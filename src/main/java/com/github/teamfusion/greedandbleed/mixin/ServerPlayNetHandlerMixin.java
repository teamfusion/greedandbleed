package com.github.teamfusion.greedandbleed.mixin;

import com.github.teamfusion.greedandbleed.GreedAndBleed;
import com.github.teamfusion.greedandbleed.common.entity.IHasMountInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.ServerPlayNetHandler;
import net.minecraft.network.play.client.CEntityActionPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetHandler.class)
public class ServerPlayNetHandlerMixin {

    @Shadow
    public ServerPlayerEntity player;

    @Inject(at = @At(value = "INVOKE",
			target = "Lnet/minecraft/network/play/client/CEntityActionPacket;getAction()Lnet/minecraft/network/play/client/CEntityActionPacket$Action;"),
			method = "handlePlayerCommand")
    private void handleOpenMountInventory(CEntityActionPacket actionPacket, CallbackInfo ci){
        if (actionPacket.getAction() == CEntityActionPacket.Action.OPEN_INVENTORY
                && this.player.getVehicle() instanceof IHasMountInventory) {
            GreedAndBleed.LOGGER.debug("Server player {} is opening the mount inventory for {}", this.player, this.player.getVehicle());
            ((IHasMountInventory)this.player.getVehicle()).openInventory(this.player);
        }
    }
}
