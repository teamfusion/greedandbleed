package com.github.teamfusion.greedandbleed.mixin;

import com.github.teamfusion.greedandbleed.common.CommonSetup;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public class PlayerMixin {
    @Inject(method = "hurt", at = @At("HEAD"))
    private void $hurt(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        CommonSetup.onHoglinAttack((Player) (Object) this, source);
    }

}