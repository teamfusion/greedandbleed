package com.github.teamfusion.greedandbleed.common.entity.brain;

import com.github.teamfusion.greedandbleed.common.entity.ICrawlSpawn;
import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.schedule.Activity;

public class ModEmerging<E extends PathfinderMob> extends Behavior<E> {
    public ModEmerging(int i) {
        super(ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED), i);
    }

    @Override
    protected boolean canStillUse(ServerLevel serverLevel, E warden, long l) {
        return warden instanceof ICrawlSpawn crawlSpawn && crawlSpawn.getSpawnScale() <= 0.0F;
    }

    @Override
    protected void start(ServerLevel serverLevel, E warden, long l) {
    }

    @Override
    protected void stop(ServerLevel serverLevel, E warden, long l) {
        if (((Entity) warden).hasPose(Pose.EMERGING)) {
            warden.setPose(Pose.STANDING);
            warden.getBrain().setActiveActivityIfPossible(Activity.EMERGE);
        }
    }
}

