package com.github.teamfusion.greedandbleed.mixin.client;

import com.github.teamfusion.greedandbleed.common.CommonSetup;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.world.damagesource.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RemotePlayer.class)
public class RemotePlayerMixin {
    @Inject(method = "hurt", at = @At("HEAD"))
    private void $hurt(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        CommonSetup.onHoglinAttack((RemotePlayer)(Object)this, source);
    }
}