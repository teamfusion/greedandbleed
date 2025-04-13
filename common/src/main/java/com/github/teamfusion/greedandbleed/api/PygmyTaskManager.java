package com.github.teamfusion.greedandbleed.api;

import com.github.teamfusion.greedandbleed.common.entity.brain.*;
import com.github.teamfusion.greedandbleed.common.entity.brain.StrollToPoi;
import com.github.teamfusion.greedandbleed.common.entity.piglin.pygmy.GBPygmy;
import com.github.teamfusion.greedandbleed.common.entity.piglin.pygmy.Pygmy;
import com.github.teamfusion.greedandbleed.common.item.slingshot.SlingshotItem;
import com.github.teamfusion.greedandbleed.common.registry.MemoryRegistry;
import com.github.teamfusion.greedandbleed.common.registry.PoiRegistry;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;

import java.util.List;
import java.util.Optional;

/***
 * An extensible class for initializing and handling a Brain for a LivingEntity.
 * This was based on the class PiglinTasks.
 * @author Thelnfamous1
 * @param <T> A class that extends LivingEntity
 */
public class PygmyTaskManager<T extends Pygmy> extends TaskManager<T> {
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
    public PygmyTaskManager(T mob, Brain<T> dynamicBrain) {
        super(mob, dynamicBrain);
    }

    @Override
    public void initMemories() {
    }

    @Override
    protected void initActivities() {
        this.initCoreActivity(0);
        this.initIdleActivity(10);
        this.initFightActivity(10);
        this.initWorkActivity(10);
        this.initRideActivity(5);
    }

    //I need work activity with condition so I'll override it
    @Override
    protected void initWorkActivity(int priorityStart) {
        dynamicBrain.addActivity(Activity.WORK, priorityStart, ImmutableList.of(StartAttacking.create(PygmyTaskManager::findNearestValidAttackTarget), createIdleLookBehaviors(), createWorkMovementBehaviors()));

    }

    protected RunOne<T> createWorkMovementBehaviors() {
        return new RunOne<>(
                getWorkMovementBehaviors()
        );
    }


    @Override
    protected void initRideActivity(int priorityStart) {
        dynamicBrain.addActivityAndRemoveMemoryWhenStopped(Activity.RIDE, priorityStart, ImmutableList.of(Mount.create(1.25F)), MemoryModuleType.RIDE_TARGET);
    }

    @Override
    protected List<Pair<? extends BehaviorControl<? super T>, Integer>> getIdleLookBehaviors() {
        return ImmutableList.of(Pair.of(SetEntityLookTarget.create((livingEntity) -> {
            return livingEntity instanceof AbstractPiglin;
        }, 8), 1), Pair.of(SetEntityLookTarget.create(EntityType.PLAYER, 8), 1), Pair.of(SetEntityLookTarget.create(8.0F), 1), Pair.of(new DoNothing(30, 60), 1));
    }

    @Override
    protected List<Pair<? extends BehaviorControl<? super T>, Integer>> getIdleMovementBehaviors() {
        return ImmutableList.of(Pair.of(RandomStroll.stroll(0.6F), 2), Pair.of(new DoNothing(30, 60), 1));
    }

    protected List<Pair<? extends BehaviorControl<? super T>, Integer>> getWorkMovementBehaviors() {

        return ImmutableList.of(Pair.of(BehaviorBuilder.triggerIf(predicate -> {
            return predicate.getMode() == GBPygmy.Mode.FOLLOW;
        }, FollowRecruitPlayer.create(0.9F)), 2), Pair.of(BehaviorBuilder.triggerIf(predicate -> {
            return predicate.getMode() == GBPygmy.Mode.PATROL;
        }, StrollAroundPoi.create(MemoryModuleType.JOB_SITE, 0.9F, this.mob.getPatrolRange())), 2), Pair.of(BehaviorBuilder.triggerIf(predicate -> {
            return predicate.getMode() == GBPygmy.Mode.PATROL;
        }, StrollToPoi.create(MemoryModuleType.JOB_SITE, 0.9F, this.mob.getPatrolRange(), 32)), 2), Pair.of(new WorkAtPygmyPoi(), 2), Pair.of(BehaviorBuilder.triggerIf(predicate -> {
            return predicate.getMode() != GBPygmy.Mode.WAIT;
        }, RandomStroll.stroll(0.6F)), 5), Pair.of(new DoNothing(30, 60), 1));
    }
    @Override
    protected List<BehaviorControl<? super T>> getCoreTasks() {
        return List.of(new LookAtTargetSink(45, 90), new MoveToTargetSink(), InteractWithDoor.create(), new SwitchPygmySimpleJob<>(), ValidateNearbyPoi.create(holder -> holder.is(PoiRegistry.PYGMY_STATION), MemoryModuleType.JOB_SITE), new CountDownCooldownTicks(MemoryRegistry.WORK_TIME.get()));
    }

    @Override
    protected List<BehaviorControl<? super T>> getIdleTasks() {
        return ImmutableList.of(StartAttacking.create(PygmyTaskManager::findNearestValidAttackTarget), createIdleLookBehaviors(), createIdleMovementBehaviors(), SetLookAndInteract.create(EntityType.PLAYER, 4));
    }

    @Override
    protected List<BehaviorControl<? super T>> getFightTasks() {
        return ImmutableList.of(StopAttackingIfTargetInvalid.create(livingEntity -> !isNearestValidAttackTarget(livingEntity)), BehaviorBuilder.triggerIf(PygmyTaskManager::hasSlingshotWithBackUp, BackUpIfTooClose.create(5, 0.75f)), BehaviorBuilder.triggerIf(PygmyTaskManager::hasNotRiding, SetWalkTargetFromAttackTargetIfTargetFar.create(1.15f)), BehaviorBuilder.triggerIf(PygmyTaskManager::hasNotRiding, MeleeAttack.create(20)), new SlingshotAttack<>());
    }


    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        GBPygmy.Mode mode = this.mob.getMode();
        if (this.getBrain().hasMemoryValue(MemoryModuleType.LIKED_PLAYER) && this.getBrain().getMemory(MemoryModuleType.LIKED_PLAYER).get() == player.getUUID()) {
            this.mob.setMode(GBPygmy.Mode.changeMode(this.mob.getMode()));
            if (mode == GBPygmy.Mode.FOLLOW) {
                player.displayClientMessage(Component.translatable("entity.greedandbleed.pygmy.following"), true);
            } else if (mode == GBPygmy.Mode.PATROL) {
                player.displayClientMessage(Component.translatable("entity.greedandbleed.pygmy.patrol"), true);
            } else {
                player.displayClientMessage(Component.translatable("entity.greedandbleed.pygmy.waiting"), true);
            }


            return InteractionResult.sidedSuccess(player.level().isClientSide);
        }

        return null;
    }


    @Override
    public void updateActivity() {
        this.getBrain().setActiveActivityToFirstValid(ImmutableList.of(Activity.RIDE, Activity.FIGHT, Activity.WORK, Activity.IDLE));
        this.mob.setAggressive(this.getBrain().hasMemoryValue(MemoryModuleType.ATTACK_TARGET));
        if (this.mob.getControlledVehicle() instanceof Mob vehicle) {
            if (this.getBrain().hasMemoryValue(MemoryModuleType.RIDE_TARGET)) {
                this.getBrain().eraseMemory(MemoryModuleType.RIDE_TARGET);
            }
            if (!vehicle.isAggressive()) {
                this.mob.unRide();
            }
        }
    }

    @Override
    public void wasHurtBy(LivingEntity entity) {
        if (entity instanceof GBPygmy) {
            return;
        }
        if (entity instanceof Hoglin) {
            return;
        }
        maybeRetaliate(this.mob, entity);
    }

    public static void maybeRetaliate(GBPygmy piglin, LivingEntity targetIn) {
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

    public static void broadcastAngerTarget(GBPygmy piglin, LivingEntity targetIn) {
        getAdultPygmys(piglin).forEach((adultPiglin) -> {
            if (!(targetIn instanceof Hoglin hoglin)
                    || piglin.canHunt()
                    && hoglin.canBeHunted()) {
                setAngerTargetIfCloserThanCurrent(adultPiglin, targetIn);
            }
        });
    }


    public static void broadcastUniversalAnger(GBPygmy piglin) {
        getAdultPygmys(piglin).forEach((adultPiglin) -> getNearestVisibleTargetablePlayer(adultPiglin).ifPresent((player) -> setAngerTarget(adultPiglin, player)));
    }

    public static void setAngerTarget(GBPygmy piglin, LivingEntity targetIn) {
        if (isAttackAllowed(piglin, targetIn)) {
            piglin.getBrain().eraseMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
            piglin.getBrain().setMemoryWithExpiry(MemoryModuleType.ANGRY_AT, targetIn.getUUID(), 600L);

            if (targetIn instanceof Player && piglin.level().getGameRules().getBoolean(GameRules.RULE_UNIVERSAL_ANGER)) {
                piglin.getBrain().setMemoryWithExpiry(MemoryModuleType.UNIVERSAL_ANGER, true, 600L);
            }

        }
    }

    public static void setAngerTargetToNearestTargetablePlayerIfFound(GBPygmy piglin, LivingEntity targetIn) {
        Optional<Player> nearestPlayer = getNearestVisibleTargetablePlayer(piglin);
        if (nearestPlayer.isPresent()) {
            setAngerTarget(piglin, nearestPlayer.get());
        } else {
            setAngerTarget(piglin, targetIn);
        }

    }

    public static void setAngerTargetIfCloserThanCurrent(GBPygmy piglin, LivingEntity targetIn) {
        Optional<LivingEntity> angerTarget = getAngerTarget(piglin);
        LivingEntity nearestTarget = BehaviorUtils.getNearestTarget(piglin, angerTarget, targetIn);
        if (angerTarget.isEmpty() || angerTarget.get() != nearestTarget) {
            setAngerTarget(piglin, nearestTarget);
        }
    }
    @Override
    public Optional<? extends LivingEntity> findNearestValidAttackTarget() {
        return findNearestValidAttackTarget(this.mob);
    }

    private static Optional<? extends LivingEntity> findNearestValidAttackTarget(GBPygmy abstractPiglin) {
        Optional<LivingEntity> optional = BehaviorUtils.getLivingEntityFromUUIDMemory(abstractPiglin, MemoryModuleType.ANGRY_AT);
        if (optional.isPresent() && Sensor.isEntityAttackableIgnoringLineOfSight(abstractPiglin, optional.get())) {
            return optional;
        }

        Optional<? extends LivingEntity> optional3 = getTamedTargetIfWithinRange(abstractPiglin, MemoryRegistry.NEAREST_TAMED_HOGLET.get());


        Optional<? extends LivingEntity> optional2 = getTargetIfWithinRange(abstractPiglin, MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER);
        if (optional2.isPresent() && optional3.isEmpty()) {
            return optional2;
        }
        return abstractPiglin.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_NEMESIS);
    }

    private static Optional<? extends LivingEntity> getTargetIfWithinRange(GBPygmy abstractPiglin, MemoryModuleType<? extends LivingEntity> memoryModuleType) {
        return abstractPiglin.getBrain().getMemory(memoryModuleType).filter(livingEntity -> livingEntity.closerThan(abstractPiglin, 18.0));
    }

    private static Optional<? extends LivingEntity> getTamedTargetIfWithinRange(GBPygmy abstractPiglin, MemoryModuleType<? extends LivingEntity> memoryModuleType) {
        return abstractPiglin.getBrain().getMemory(memoryModuleType).filter(livingEntity -> livingEntity.closerThan(abstractPiglin, 24.0));
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

    private static boolean hasSlingshotWithBackUp(LivingEntity arg) {
        return arg.isHolding(is -> is.getItem() instanceof SlingshotItem) && hasNotRiding(arg);
    }

    private static boolean hasNotRiding(LivingEntity arg) {
        return !arg.getBrain().hasMemoryValue(MemoryModuleType.RIDE_TARGET);
    }

    private static boolean hasRiding(LivingEntity arg) {
        return !hasNotRiding(arg);
    }

}