package com.github.teamfusion.greedandbleed.mixin;

import com.github.teamfusion.greedandbleed.common.entity.piglin.pygmy.Pygmy;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.entity.monster.hoglin.HoglinAi;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HoglinAi.class)
public class HoglinAiMixin {

    @Inject(method = "wasHurtBy", at = @At("HEAD"), cancellable = true)
    private static void wasHurtBy(Hoglin hoglin, LivingEntity livingEntity, CallbackInfo ci) {
        if (livingEntity instanceof Pygmy) {
            ci.cancel();
        }
    }
}
