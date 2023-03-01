package com.github.teamfusion.greedandbleed.mixin.client;

import com.github.teamfusion.greedandbleed.GreedAndBleed;
import com.github.teamfusion.greedandbleed.common.entity.HasMountInventory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MultiPlayerGameMode.class)
public class MultiPlayerGameModeMixin {
    @Shadow @Final private Minecraft minecraft;

    @Inject(method = "isServerControlledInventory", at = @At("RETURN"), cancellable = true)
    private void gb$isMountInventory(CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue() && this.minecraft.player != null) {
            GreedAndBleed.LOGGER.debug("Player {} is riding a mount with inventory!", this.minecraft.player);
            cir.setReturnValue(this.minecraft.player.isPassenger() && this.minecraft.player.getVehicle() instanceof HasMountInventory);
        }
    }
}