package com.github.teamfusion.greedandbleed.common.entity.brain;

import com.github.teamfusion.greedandbleed.common.entity.piglin.WarpedPiglin;
import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.phys.Vec3;

public class JumpTheSky<E extends WarpedPiglin> extends Behavior<E> {
    private int cooldownTime;

    public int tick;
    private static final UniformInt TIME_BETWEEN_JUMP = UniformInt.of(100, 160);

    public JumpTheSky() {
        super(ImmutableMap.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT), 1200);
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel serverLevel, E mob) {
        if (--this.cooldownTime <= 0) {
            if (mob.onGround() && mob.isAggressive()) {
                this.cooldownTime = TIME_BETWEEN_JUMP.sample(mob.getRandom());
                return true;
            }
        }
        return false;
    }

    @Override
    protected boolean canStillUse(ServerLevel serverLevel, E mob, long l) {
        return this.tick < 6;
    }

    @Override
    protected void tick(ServerLevel serverLevel, E mob, long l) {
        if (this.tick++ == 3) {
            mob.startFallFlying();
        }
    }


    @Override
    protected void start(ServerLevel serverLevel, E livingEntity, long l) {
        super.start(serverLevel, livingEntity, l);
        this.tick = 0;
        Vec3 vec3 = livingEntity.getDeltaMovement();
        livingEntity.setDeltaMovement(vec3.x, vec3.y + 1F, vec3.z);
    }

    @Override
    protected void stop(ServerLevel serverLevel, E mob, long l) {
        if (mob.isUsingItem()) {
            mob.stopUsingItem();
        }
    }
}

