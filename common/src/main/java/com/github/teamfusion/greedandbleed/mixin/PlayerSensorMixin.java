package com.github.teamfusion.greedandbleed.mixin;

import com.github.teamfusion.greedandbleed.common.entity.ToleratingMount;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.sensing.PlayerSensor;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

@Mixin(PlayerSensor.class)
public class PlayerSensorMixin {
    // MIXINS
    @Redirect(method = "doTick", at = @At(value = "INVOKE", target = "Ljava/util/stream/Stream;findFirst()Ljava/util/Optional;"))
    private Optional<Player> gb$accountForTolerance(Stream<Player> instance, ServerLevel level, LivingEntity entity) {
        return instance.filter(this.nonToleratedPassenger(entity)).findFirst();
    }

    // HELPERS
    private Predicate<Player> nonToleratedPassenger(LivingEntity entity) {
        return player -> !entity.getPassengers().contains(player) || !(entity instanceof ToleratingMount mount && mount.isTolerating());
    }
}