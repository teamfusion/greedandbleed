package com.github.teamfusion.greedandbleed.mixin;

import com.github.teamfusion.greedandbleed.common.CommonSetup;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    private final LivingEntity $this = (LivingEntity)(Object)this;

    @Inject(method = "tick", at = @At("HEAD"))
    private void gb$tick(CallbackInfo ci) {
        CommonSetup.onHoglinUpdate($this);
    }

    @Inject(method = "hurt", at = @At("HEAD"))
    private void gb$hurt(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if ($this instanceof Player) return;
        CommonSetup.onHoglinAttack($this, source);
    }
}