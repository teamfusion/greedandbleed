package com.github.teamfusion.greedandbleed.common.entity.brain;

import com.github.teamfusion.greedandbleed.common.entity.projectile.WarpedSpit;
import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

public class WarpedSpitAttack<E extends Mob, T extends LivingEntity> extends Behavior<E> {
    private static final int TIMEOUT = 1200;
    private int attackDelay;

    public WarpedSpitAttack() {
        super(ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT));
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel serverLevel, E mob) {
        LivingEntity livingEntity = WarpedSpitAttack.getAttackTarget(mob);
        return BehaviorUtils.canSee(mob, livingEntity);
    }

    @Override
    protected boolean canStillUse(ServerLevel serverLevel, E mob, long l) {
        return mob.getBrain().hasMemoryValue(MemoryModuleType.ATTACK_TARGET) && this.checkExtraStartConditions(serverLevel, mob);
    }

    @Override
    protected void tick(ServerLevel serverLevel, E mob, long l) {
        LivingEntity livingEntity = WarpedSpitAttack.getAttackTarget(mob);
        this.lookAtTarget(mob, livingEntity);
        this.attack(serverLevel, mob, livingEntity);
    }

    @Override
    protected void start(ServerLevel serverLevel, E livingEntity, long l) {
        super.start(serverLevel, livingEntity, l);
    }

    @Override
    protected void stop(ServerLevel serverLevel, E mob, long l) {
        this.attackDelay = 0;
    }

    private void attack(ServerLevel serverLevel, E mob, LivingEntity livingEntity) {
        if (this.attackDelay >= 40) {
            this.performRangedAttack(serverLevel, mob, livingEntity);
            this.attackDelay = 0;
        } else {
            ++this.attackDelay;
        }
    }

    public void performRangedAttack(ServerLevel serverLevel, E mob, LivingEntity livingEntity) {
        WarpedSpit spit = new WarpedSpit(serverLevel, mob);
        double e = livingEntity.getX() - mob.getX();
        double g = livingEntity.getEyeY() - mob.getEyeY();
        double h = livingEntity.getZ() - mob.getZ();
        spit.shoot(e, g, h, 1.0f, 12.0f - serverLevel.getDifficulty().getId() * 3F);
        mob.playSound(SoundEvents.LLAMA_SPIT, 1.0f, 0.4f / (mob.getRandom().nextFloat() * 0.4f + 0.8f));
        serverLevel.addFreshEntity(spit);
    }

    private void lookAtTarget(Mob mob, LivingEntity livingEntity) {
        mob.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker(livingEntity, true));
    }

    private static LivingEntity getAttackTarget(LivingEntity livingEntity) {
        return livingEntity.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).get();
    }
}

