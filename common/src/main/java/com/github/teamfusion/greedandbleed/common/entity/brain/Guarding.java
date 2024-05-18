package com.github.teamfusion.greedandbleed.common.entity.brain;

import com.github.teamfusion.greedandbleed.common.entity.piglin.pygmy.Shrygmy;
import com.github.teamfusion.greedandbleed.util.PiglinUtils;
import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.item.ShieldItem;

public class Guarding<E extends Mob, T extends LivingEntity> extends Behavior<E> {
    public Guarding() {
        super(ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT));
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel serverLevel, E mob) {
        LivingEntity livingEntity = Guarding.getAttackTarget(mob);
        return mob.isHolding(predicate -> {
            return predicate.getItem() instanceof ShieldItem;
        }) && BehaviorUtils.canSee(mob, livingEntity);
    }

    @Override
    protected boolean canStillUse(ServerLevel serverLevel, E mob, long l) {
        return mob.getBrain().hasMemoryValue(MemoryModuleType.ATTACK_TARGET) && this.checkExtraStartConditions(serverLevel, mob);
    }

    @Override
    protected void start(ServerLevel serverLevel, E livingEntity, long l) {
        super.start(serverLevel, livingEntity, l);
        this.guard(livingEntity);
    }

    @Override
    protected void stop(ServerLevel serverLevel, E mob, long l) {
        if (mob.isUsingItem()) {
            mob.stopUsingItem();
        }
    }

    private void guard(E mob) {
        if (!(mob instanceof Shrygmy shrygmy) || shrygmy.shieldCooldown <= 0) {
            mob.startUsingItem(PiglinUtils.getWeaponHoldingHand(mob, predicate -> {
                return predicate.getItem() instanceof ShieldItem;
            }));
        }
    }

    protected boolean timedOut(long l) {
        return false;
    }

    private static LivingEntity getAttackTarget(LivingEntity livingEntity) {
        return livingEntity.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).get();
    }
}

