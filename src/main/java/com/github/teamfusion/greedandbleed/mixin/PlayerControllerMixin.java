package com.github.teamfusion.greedandbleed.mixin;

import com.github.teamfusion.greedandbleed.GreedAndBleed;
import com.github.teamfusion.greedandbleed.common.entity.IHasMountInventory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerController;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerController.class)
public class PlayerControllerMixin {

    @Final
    @Shadow
    private Minecraft minecraft;

    @Inject(at = @At("RETURN"), method = "isServerControlledInventory", cancellable = true, remap = false)
    private void isMountInventory(CallbackInfoReturnable<Boolean> cir){
        if(!cir.getReturnValue() && this.minecraft.player != null) {
            GreedAndBleed.LOGGER.debug("Player {} is riding a mount with inventory!", this.minecraft.player);
            cir.setReturnValue(this.minecraft.player.isPassenger()
                    && this.minecraft.player.getVehicle() instanceof IHasMountInventory
            );
        }
    }
}
