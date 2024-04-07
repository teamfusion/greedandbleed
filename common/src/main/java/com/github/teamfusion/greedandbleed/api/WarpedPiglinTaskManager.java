package com.github.teamfusion.greedandbleed.api;

import com.github.teamfusion.greedandbleed.common.entity.piglin.SkeletalPiglin;
import com.github.teamfusion.greedandbleed.common.entity.piglin.WarpedPiglin;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.schedule.Activity;

import java.util.List;
import java.util.Optional;

/***
 * An extensible class for initializing and handling a Brain for a LivingEntity.
 * This was based on the class PiglinTasks.
 * @author Thelnfamous1
 * @param <T> A class that extends LivingEntity
 */
public class WarpedPiglinTaskManager<T extends WarpedPiglin> extends PiglinTaskManager<T> {
    /**
     * Constructs a new TaskManager instance given a LivingEntity and a Brain.
     * Note that this should only be instantiated inside LivingEntity#makeBrain,
     * ideally using IHasTaskManager#createTaskManager,
     * and dynamicBrain should be the Brain returned from LivingEntity#brainProvider().makeBrain(dynamic)
     * where dynamic is the Dynamic instance passed into LivingEntity#makeBrain.
     *
     * @param mob          The LivingEntity to associate with this TaskManager
     * @param dynamicBrain The Brain to associate with this TaskManager
     */
    public WarpedPiglinTaskManager(T mob, Brain<T> dynamicBrain) {
        super(mob, dynamicBrain);
    }

    @Override
    public void initMemories() {
    }

    @Override
    protected List<BehaviorControl<? super T>> getCoreTasks() {
        return List.of(new LookAtTargetSink(45, 90), new MoveToTargetSink(), InteractWithDoor.create(), StopBeingAngryIfTargetDead.create());
    }

    @Override
    protected List<BehaviorControl<? super T>> getIdleTasks() {
        return ImmutableList.of(StartAttacking.create(WarpedPiglinTaskManager::findNearestValidAttackTarget), createIdleLookBehaviors(), createIdleMovementBehaviors());
    }

    @Override
    protected List<BehaviorControl<? super T>> getFightTasks() {
        return ImmutableList.of(StopAttackingIfTargetInvalid.create(livingEntity -> !isNearestValidAttackTarget(livingEntity)), SetWalkTargetFromAttackTargetIfTargetOutOfReach.create(1.0f), MeleeAttack.create(20));
    }

    protected List<Pair<? extends BehaviorControl<? super T>, Integer>> getIdleMovementBehaviors() {
        return ImmutableList.of(Pair.of(RandomStroll.fly(0.75F), 2), Pair.of(new DoNothing(30, 60), 1));
    }

    @Override
    protected List<Pair<? extends BehaviorControl<? super T>, Integer>> getIdleLookBehaviors() {
        return ImmutableList.of(Pair.of(SetEntityLookTarget.create((livingEntity) -> {
            return livingEntity instanceof AbstractPiglin;
        }, 8), 1), Pair.of(SetEntityLookTarget.create(EntityType.PLAYER, 8), 1), Pair.of(SetEntityLookTarget.create(8.0F), 1), Pair.of(new DoNothing(30, 60), 1));
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        return null;
    }

    @Override
    public void updateActivity() {
        this.getBrain().setActiveActivityToFirstValid(ImmutableList.of(Activity.FIGHT, Activity.IDLE));
        this.mob.setAggressive(this.getBrain().hasMemoryValue(MemoryModuleType.ATTACK_TARGET));
    }

    @Override
    public void wasHurtBy(LivingEntity entity) {
        if (entity instanceof AbstractPiglin) {
            return;
        }
        if (entity instanceof SkeletalPiglin) {
            return;
        }
        maybeRetaliate(this.mob, entity);
    }

    @Override
    public Optional<? extends LivingEntity> findNearestValidAttackTarget() {
        return findNearestValidAttackTarget(this.mob);
    }

    private static Optional<? extends LivingEntity> findNearestValidAttackTarget(AbstractPiglin abstractPiglin) {
        Optional<LivingEntity> optional = BehaviorUtils.getLivingEntityFromUUIDMemory(abstractPiglin, MemoryModuleType.ANGRY_AT);
        if (optional.isPresent() && Sensor.isEntityAttackableIgnoringLineOfSight(abstractPiglin, optional.get())) {
            return optional;
        }
        Optional<? extends LivingEntity> optional2 = getTargetIfWithinRange(abstractPiglin, MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER);
        if (optional2.isPresent()) {
            return optional2;
        }
        return abstractPiglin.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_NEMESIS);
    }

    private static Optional<? extends LivingEntity> getTargetIfWithinRange(AbstractPiglin abstractPiglin, MemoryModuleType<? extends LivingEntity> memoryModuleType) {
        return abstractPiglin.getBrain().getMemory(memoryModuleType).filter(livingEntity -> livingEntity.closerThan(abstractPiglin, 12.0));
    }

    @Override
    protected void initActivities() {
        super.initActivities();
    }

    @Override
    protected void setCoreAndDefault() {
        this.getBrain().setCoreActivities(ImmutableSet.of(Activity.CORE));
        this.getBrain().setDefaultActivity(Activity.IDLE);
        this.getBrain().useDefaultActivity();
    }

    @Override
    public SoundEvent getSoundForActivity(Activity activity) {
        if (activity == Activity.FIGHT) {
            return SoundEvents.PIGLIN_ANGRY;
        }

        return SoundEvents.PIGLIN_AMBIENT;
    }
}