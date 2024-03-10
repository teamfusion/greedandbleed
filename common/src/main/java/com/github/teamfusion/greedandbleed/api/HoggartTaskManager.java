package com.github.teamfusion.greedandbleed.api;

import com.github.teamfusion.greedandbleed.common.entity.piglin.GBPygmy;
import com.github.teamfusion.greedandbleed.common.entity.piglin.Hoggart;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.level.GameRules;

import java.util.List;
import java.util.Optional;

/***
 * An extensible class for initializing and handling a Brain for a LivingEntity.
 * This was based on the class PiglinTasks.
 * @author Thelnfamous1
 * @param <T> A class that extends LivingEntity
 */
public class HoggartTaskManager<T extends Hoggart> extends PiglinTaskManager<T> {
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
    public HoggartTaskManager(T mob, Brain<T> dynamicBrain) {
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
        return ImmutableList.of(StartAttacking.create(HoggartTaskManager::findNearestValidAttackTarget), createIdleLookBehaviors(), createIdleMovementBehaviors());
    }

    @Override
    protected List<BehaviorControl<? super T>> getFightTasks() {
        return ImmutableList.of(StopAttackingIfTargetInvalid.create(livingEntity -> !isNearestValidAttackTarget(livingEntity)), SetWalkTargetFromAttackTargetIfTargetOutOfReach.create(1.0f), MeleeAttack.create(20));
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
        if (entity instanceof GBPygmy) {
            return;
        }
        maybeRetaliate(this.mob, entity);
    }

    public static void maybeRetaliate(AbstractPiglin piglin, LivingEntity targetIn) {
        if (!piglin.getBrain().isActive(Activity.AVOID)) {
            if (isAttackAllowed(piglin, targetIn)) {
                if (!BehaviorUtils.isOtherTargetMuchFurtherAwayThanCurrentAttackTarget(piglin, targetIn, 4.0D)) {
                    if (targetIn instanceof Player && piglin.level().getGameRules().getBoolean(GameRules.RULE_UNIVERSAL_ANGER)) {
                        setAngerTargetToNearestTargetablePlayerIfFound(piglin, targetIn);
                        broadcastUniversalAnger(piglin);
                    } else {
                        setAngerTarget(piglin, targetIn);
                        broadcastAngerTarget(piglin, targetIn);
                    }

                }
            }
        }
    }

    public static void broadcastAngerTarget(AbstractPiglin piglin, LivingEntity targetIn) {
        getAdultPiglins(piglin).forEach((adultPiglin) -> {
            if (!(targetIn instanceof Hoglin hoglin)
                    || adultPiglin instanceof GBPygmy gbPiglin && gbPiglin.canHunt()
                    && hoglin.canBeHunted()) {
                setAngerTargetIfCloserThanCurrent(adultPiglin, targetIn);
            }
        });
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
        return abstractPiglin.getBrain().getMemory(memoryModuleType).filter(livingEntity -> livingEntity.closerThan(abstractPiglin, 18.0));
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