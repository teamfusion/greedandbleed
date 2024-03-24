package com.github.teamfusion.greedandbleed.common.entity.brain;

import com.github.teamfusion.greedandbleed.common.registry.ItemRegistry;
import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;

public class SlingshotAttack<E extends Mob, T extends LivingEntity> extends Behavior<E> {
    private static final int TIMEOUT = 1200;
    private int attackDelay;
    private SlingshotState slingshotState = SlingshotState.UNCHARGED;

    public SlingshotAttack() {
        super(ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT), 1200);
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel serverLevel, E mob) {
        LivingEntity livingEntity = SlingshotAttack.getAttackTarget(mob);
        return mob.isHolding(ItemRegistry.SLINGSHOT.get()) && BehaviorUtils.canSee(mob, livingEntity) && BehaviorUtils.isWithinAttackRange(mob, livingEntity, 0);
    }

    @Override
    protected boolean canStillUse(ServerLevel serverLevel, E mob, long l) {
        return mob.getBrain().hasMemoryValue(MemoryModuleType.ATTACK_TARGET) && this.checkExtraStartConditions(serverLevel, mob);
    }

    @Override
    protected void tick(ServerLevel serverLevel, E mob, long l) {
        LivingEntity livingEntity = SlingshotAttack.getAttackTarget(mob);
        this.lookAtTarget(mob, livingEntity);
        this.slingShotAttack(mob, livingEntity);
    }

    @Override
    protected void stop(ServerLevel serverLevel, E mob, long l) {
        if (mob.isUsingItem()) {
            mob.stopUsingItem();
        }
    }

    private void slingShotAttack(E mob, LivingEntity livingEntity) {
        if (this.slingshotState == SlingshotAttack.SlingshotState.UNCHARGED) {
            mob.startUsingItem(ProjectileUtil.getWeaponHoldingHand(mob, ItemRegistry.SLINGSHOT.get()));
            this.slingshotState = SlingshotState.CHARGED;
            this.attackDelay = 30 + mob.getRandom().nextInt(5);

        } else if (this.slingshotState == SlingshotAttack.SlingshotState.CHARGED) {
            --this.attackDelay;
            if (this.attackDelay == 0) {
                this.slingshotState = SlingshotAttack.SlingshotState.READY_TO_ATTACK;
            }
        } else if (this.slingshotState == SlingshotAttack.SlingshotState.READY_TO_ATTACK) {
            mob.stopUsingItem();
            ((RangedAttackMob) mob).performRangedAttack(livingEntity, 1.0f);
            ItemStack itemStack2 = mob.getItemInHand(ProjectileUtil.getWeaponHoldingHand(mob, ItemRegistry.SLINGSHOT.get()));
            CrossbowItem.setCharged(itemStack2, false);
            this.slingshotState = SlingshotAttack.SlingshotState.UNCHARGED;
        }
    }

    private void lookAtTarget(Mob mob, LivingEntity livingEntity) {
        mob.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker(livingEntity, true));
    }

    private static LivingEntity getAttackTarget(LivingEntity livingEntity) {
        return livingEntity.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).get();
    }

    enum SlingshotState {
        UNCHARGED,
        CHARGING,
        CHARGED,
        READY_TO_ATTACK

    }
}

