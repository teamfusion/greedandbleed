package com.github.teamfusion.greedandbleed.mixin;

import com.github.teamfusion.greedandbleed.common.registry.BiomeRegistry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.HoglinSpecificSensor;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HoglinSpecificSensor.class)
public class HoglinSpecificSensorMixin {

    @Inject(method = ("doTick(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/monster/hoglin/Hoglin;)V"), at = @At("HEAD"))
    private void doTick(ServerLevel serverLevel, Hoglin hoglin, CallbackInfo callbackInfo) {
        Brain<Hoglin> brain = hoglin.getBrain();
        if (hoglin.level().getBiome(hoglin.blockPosition()).is(BiomeRegistry.HOGDEW_HOLLOW)) {
            brain.setMemoryWithExpiry(MemoryModuleType.PACIFIED, true, 600);
        }
    }
}
