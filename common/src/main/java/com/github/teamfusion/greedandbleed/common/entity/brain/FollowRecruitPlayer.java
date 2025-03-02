package com.github.teamfusion.greedandbleed.common.entity.brain;

import com.github.teamfusion.greedandbleed.common.entity.piglin.pygmy.GBPygmy;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.behavior.OneShot;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.level.Level;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

public class FollowRecruitPlayer {
    public FollowRecruitPlayer() {
    }

    public static OneShot<GBPygmy> create(float f) {
        return create((livingEntity) -> f);
    }

    public static OneShot<GBPygmy> create(Function<GBPygmy, Float> function) {
        return BehaviorBuilder.create((instance) -> instance.group(instance.present(MemoryModuleType.LIKED_PLAYER), instance.registered(MemoryModuleType.LOOK_TARGET), instance.registered(MemoryModuleType.WALK_TARGET)).apply(instance, (memoryAccessor, memoryAccessor2, memoryAccessor3) -> (serverLevel, pygmy, l) -> {

            Optional<ServerPlayer> likedPlayer = getLikedPlayer(pygmy);

            if (likedPlayer.isEmpty() || pygmy.isWaiting()) {
                return false;
            }
            if (pygmy.closerThan(likedPlayer.get(), (double) (32)) && !pygmy.closerThan(likedPlayer.get(), (double) 5)) {
                WalkTarget walkTarget = new WalkTarget(new EntityTracker(likedPlayer.get(), false), (Float) function.apply(pygmy), 5 - 1);
                memoryAccessor2.set(new EntityTracker(likedPlayer.get(), true));
                memoryAccessor3.set(walkTarget);
                return true;
            } else {
                return false;
            }
        }));
    }

    public static Optional<ServerPlayer> getLikedPlayer(LivingEntity arg) {
        Level level = arg.level();
        if (!level.isClientSide() && level instanceof ServerLevel serverLevel) {
            Optional<UUID> optional = arg.getBrain().getMemory(MemoryModuleType.LIKED_PLAYER);
            if (optional.isPresent()) {
                Entity entity = serverLevel.getEntity((UUID) optional.get());
                if (entity instanceof ServerPlayer) {
                    ServerPlayer serverPlayer = (ServerPlayer) entity;
                    if ((serverPlayer.gameMode.isSurvival() || serverPlayer.gameMode.isCreative()) && serverPlayer.closerThan(arg, (double) 64.0F)) {
                        return Optional.of(serverPlayer);
                    }
                }

                return Optional.empty();
            }
        }

        return Optional.empty();
    }
}
