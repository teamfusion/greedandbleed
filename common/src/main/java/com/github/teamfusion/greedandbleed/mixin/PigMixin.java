package com.github.teamfusion.greedandbleed.mixin;

import com.github.teamfusion.greedandbleed.common.registry.PotionRegistry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Pig.class)
public abstract class PigMixin extends Animal {
    protected PigMixin(EntityType<? extends Pig> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "thunderHit", at = @At("HEAD"), cancellable = true)
    public void thunderHit(CallbackInfo callbackInfoReturnable) {
        if (this.hasEffect(PotionRegistry.IMMUNITY.get())) {
            callbackInfoReturnable.cancel();
        }
    }
}
