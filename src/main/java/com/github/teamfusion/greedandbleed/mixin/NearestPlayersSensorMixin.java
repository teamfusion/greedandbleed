package com.github.teamfusion.greedandbleed.mixin;

import com.github.teamfusion.greedandbleed.common.entity.IToleratingMount;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.sensor.NearestPlayersSensor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.server.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

@Mixin(NearestPlayersSensor.class)
public class NearestPlayersSensorMixin {


    // MIXINS

    @Redirect(at = @At(value = "INVOKE",
            target = "Ljava/util/stream/Stream;findFirst()Ljava/util/Optional;"),
            method = "doTick", remap = false)
    private Optional<PlayerEntity> accountForTolerance(Stream<PlayerEntity> stream, ServerWorld serverWorld, LivingEntity sensorMob){
        return stream
                .filter(notToleratedPassenger(sensorMob))
                .findFirst();
    }

    // HELPERS

    private Predicate<PlayerEntity> notToleratedPassenger(LivingEntity sensorMob) {
        return player -> !sensorMob.getPassengers().contains(player)
                || !(sensorMob instanceof IToleratingMount
                    && ((IToleratingMount)sensorMob).isTolerating());
    }
}
