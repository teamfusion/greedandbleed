package com.github.teamfusion.greedandbleed.mixin;

import com.github.teamfusion.greedandbleed.common.registry.PotionRegistry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractPiglin.class)
public abstract class AbstractPiglinMixin extends Monster {
    protected AbstractPiglinMixin(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "isConverting", at = @At("HEAD"), cancellable = true)
    public void isConverting(CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (this.hasEffect(PotionRegistry.IMMUNITY.get())) {
            callbackInfoReturnable.setReturnValue(false);
        }
    }
}
