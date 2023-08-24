package com.github.teamfusion.greedandbleed.mixin;

import com.github.teamfusion.greedandbleed.common.CommonSetup;
import com.github.teamfusion.greedandbleed.common.registry.PotionRegistry;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Shadow
    public abstract boolean hasEffect(MobEffect mobEffect);

    @Shadow
    public abstract @Nullable MobEffectInstance getEffect(MobEffect mobEffect);

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

    @Inject(method = "canBeAffected", at = @At("HEAD"), cancellable = true)
    public void canBeAffected(MobEffectInstance mobEffectInstance, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (hasEffect(PotionRegistry.IMMUNITY.get()) && getEffect(PotionRegistry.IMMUNITY.get()).getAmplifier() > mobEffectInstance.getAmplifier()) {
            if (mobEffectInstance.getEffect().getCategory() == MobEffectCategory.HARMFUL) {
                if (mobEffectInstance.getEffect() != MobEffects.WITHER || mobEffectInstance.getEffect() != MobEffects.DIG_SLOWDOWN || mobEffectInstance.getEffect() != MobEffects.LEVITATION) {
                    callbackInfoReturnable.setReturnValue(false);
                }
            }
        }
    }
}