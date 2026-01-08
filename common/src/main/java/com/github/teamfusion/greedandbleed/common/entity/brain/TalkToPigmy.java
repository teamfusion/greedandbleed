package com.github.teamfusion.greedandbleed.common.entity.brain;

import com.github.teamfusion.greedandbleed.api.ITalkableMob;
import com.github.teamfusion.greedandbleed.common.entity.piglin.pigmy.Hoggart;
import com.github.teamfusion.greedandbleed.common.entity.piglin.pigmy.Pigmy;
import com.github.teamfusion.greedandbleed.common.registry.MemoryRegistry;
import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;

public class TalkToPigmy<E extends Mob, T extends LivingEntity> extends Behavior<E> {
    private static final int TIMEOUT = 1200;
    private int talkTick;
    private final int talkMaxTick;
    private final float speed;
    private boolean talked;

    public TalkToPigmy(int talkMaxTick, float speed) {
        super(ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.WALK_TARGET, MemoryStatus.REGISTERED, MemoryRegistry.TALK_TARGET.get(), MemoryStatus.VALUE_PRESENT), 1200);
        this.talkMaxTick = talkMaxTick;
        this.speed = speed;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel serverLevel, E mob) {
        return mob.getBrain().hasMemoryValue(MemoryRegistry.TALK_TARGET.get());
    }

    @Override
    protected boolean canStillUse(ServerLevel serverLevel, E mob, long l) {
        return mob.getBrain().hasMemoryValue(MemoryRegistry.TALK_TARGET.get()) && this.checkExtraStartConditions(serverLevel, mob) && this.talkTick < this.talkMaxTick;
    }

    @Override
    protected void start(ServerLevel serverLevel, E livingEntity, long l) {
        super.start(serverLevel, livingEntity, l);
        this.talkTick = 0;
        this.talked = false;
        this.talkAnimation(serverLevel, livingEntity);
    }

    private void talkAnimation(ServerLevel serverLevel, E livingEntity) {
        LivingEntity target = TalkToPigmy.getTalkTarget(livingEntity);

        if (target instanceof ITalkableMob talkableMob) {
            talkableMob.responseTalk(target);
        }

        if (target instanceof Pigmy) {
            serverLevel.broadcastEntityEvent(livingEntity, (byte) Pigmy.TALK_PIGMY_ANIMATION_ID);
        } else if (target instanceof Hoggart && !(livingEntity instanceof Hoggart)) {
            serverLevel.broadcastEntityEvent(livingEntity, (byte) Pigmy.TALK_HOGGART_ANIMATION_ID);
        }
        this.talked = true;
    }

    @Override
    protected void tick(ServerLevel serverLevel, E mob, long l) {
        LivingEntity target = TalkToPigmy.getTalkTarget(mob);
        this.move(mob, target);
        if (!this.talked) {
            if (target != null && mob.position().distanceTo(target.position()) < 5) {
                this.talkAnimation(serverLevel, mob);
            }
        } else {
            this.talkTick++;
        }
    }

    @Override
    protected void stop(ServerLevel serverLevel, E mob, long l) {
        mob.getBrain().setMemoryWithExpiry(MemoryRegistry.TALK_COOLDOWN.get(), Unit.INSTANCE, 400 + mob.getRandom().nextInt(200));
        mob.getBrain().eraseMemory(MemoryRegistry.TALK_TARGET.get());
        mob.getBrain().eraseMemory(MemoryModuleType.LOOK_TARGET);
        mob.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
    }

    private void move(Mob mob, LivingEntity livingEntity) {
        mob.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker(livingEntity, true));
        mob.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(new EntityTracker(livingEntity, true), speed, 2));
    }

    private static LivingEntity getTalkTarget(LivingEntity livingEntity) {
        return livingEntity.getBrain().getMemory(MemoryRegistry.TALK_TARGET.get()).get();
    }
}

