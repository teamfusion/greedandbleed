package com.github.teamfusion.greedandbleed.mixin.client;

import com.github.teamfusion.greedandbleed.common.CommonSetup;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.damagesource.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LocalPlayer.class)
public class LocalPlayerMixin {
    @Inject(method = "hurt", at = @At("HEAD"))
    private void $hurt(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        CommonSetup.onHoglinAttack((LocalPlayer)(Object)this, source);
    }
}