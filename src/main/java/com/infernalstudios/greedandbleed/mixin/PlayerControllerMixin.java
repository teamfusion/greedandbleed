package com.infernalstudios.greedandbleed.mixin;

import com.infernalstudios.greedandbleed.GreedAndBleed;
import com.infernalstudios.greedandbleed.common.entity.IHasMountInventory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerController;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
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

    @Inject(at = @At("RETURN"), method = "isServerControlledInventory", cancellable = true)
    private void isMountInventory(CallbackInfoReturnable<Boolean> cir){
        GreedAndBleed.LOGGER.info("Player {} is riding a mount with inventory? {}",
                this.minecraft.player,
                this.minecraft.player.getVehicle() instanceof IHasMountInventory);
        if(!cir.getReturnValue()){
            GreedAndBleed.LOGGER.info("Player {} is riding a mount with inventory!", this.minecraft.player);
            cir.setReturnValue(this.minecraft.player.isPassenger()
                    && this.minecraft.player.getVehicle() instanceof IHasMountInventory
            );
        }
    }
}
