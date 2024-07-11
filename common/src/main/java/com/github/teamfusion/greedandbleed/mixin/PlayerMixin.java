package com.github.teamfusion.greedandbleed.mixin;

import com.github.teamfusion.greedandbleed.common.CommonSetup;
import com.github.teamfusion.greedandbleed.common.enchantment.WoeOfSwinEnchantment;
import com.github.teamfusion.greedandbleed.common.registry.GBEntityTypeTags;
import com.github.teamfusion.greedandbleed.common.registry.PotionRegistry;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {
    @Shadow
    public abstract void resetAttackStrengthTicker();

    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "hurt", at = @At("HEAD"))
    private void $hurt(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        CommonSetup.onHoglinAttack((Player) (Object) this, source);
    }

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    public void tick(CallbackInfo ci) {
        if (hasEffect(PotionRegistry.STUN.get())) {
            resetAttackStrengthTicker();
        }
    }




    @ModifyExpressionValue(
            method = "attack",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;getDamageBonus(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/MobType;)F",
                    ordinal = 0
            )
    )
    private float gb$applySwineDamage(float damageBonus, Entity target) {
        if (target.getType().is(GBEntityTypeTags.WOE_OF_SWINES_TARGET)) {
            damageBonus += applySwineDamageBonus(this.getMainHandItem());
        }

        return damageBonus;
    }

    @Unique
    public float applySwineDamageBonus(ItemStack stack) {
        MutableFloat mutableFloat = new MutableFloat();
        EnchantmentHelper.runIterationOnItem((enchantment, i) -> {
            float defaultBonus = 0;
            if (enchantment instanceof WoeOfSwinEnchantment dmg) {
                mutableFloat.add((float) i * 2.5F);
            }

            mutableFloat.add(defaultBonus);
        }, stack);
        return mutableFloat.floatValue();
    }
}