package com.infernalstudios.greedandbleed.taskmanager;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.infernalstudios.greedandbleed.api.PiglinTaskManager;
import com.infernalstudios.greedandbleed.api.TaskManager;
import com.infernalstudios.greedandbleed.entity.PiglinPygmyEntity;
import com.infernalstudios.greedandbleed.tasks.*;
import com.infernalstudios.greedandbleed.api.PiglinReflectionHelper;
import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.BrainUtil;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.schedule.Activity;
import net.minecraft.entity.ai.brain.task.*;
import net.minecraft.entity.monster.HoglinEntity;
import net.minecraft.entity.monster.piglin.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/***
 * An extensible class for initializing and handling a Brain for an PiglinPygmyEntity.
 * This was based on the class PiglinTasks.
 * @author Thelnfamous1
 * @param <T> A class that extends PiglinPygmyEntity
 */
public class PiglinPygmyTaskManager<T extends PiglinPygmyEntity> extends PiglinTaskManager<T> {

    public PiglinPygmyTaskManager(T pygmy, Brain<T> dynamicBrain) {
        super(pygmy, dynamicBrain);
    }

    @Override
    public void initActivities() {
        super.initActivities();
        this.initAdmireItemActivity(10);
        this.initCelebrateActivity(10);
        this.initAvoidActivity(10);
        this.initRideActivity(10);
    }

    @Override
    public Optional<? extends LivingEntity> findNearestValidAttackTarget() {
        if (isNearZombified(this.mob)) {
            return Optional.empty();
        } else {
            Optional<LivingEntity> angryAt = BrainUtil.getLivingEntityFromUUIDMemory(this.mob, MemoryModuleType.ANGRY_AT);
            if (angryAt.isPresent() && isAttackAllowed(angryAt.get())) {
                return angryAt;
            } else {
                if (this.dynamicBrain.hasMemoryValue(MemoryModuleType.UNIVERSAL_ANGER)) {
                    Optional<PlayerEntity> nearestPlayer = this.dynamicBrain.getMemory(MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER);
                    if (nearestPlayer.isPresent()) {
                        return nearestPlayer;
                    }
                }

                Optional<MobEntity> nearestNemesis = this.dynamicBrain.getMemory(MemoryModuleType.NEAREST_VISIBLE_NEMESIS);
                if (nearestNemesis.isPresent()) {
                    return nearestNemesis;
                } else {
                    Optional<PlayerEntity> nearestPlayerNotWearingGold = this.dynamicBrain.getMemory(MemoryModuleType.NEAREST_TARGETABLE_PLAYER_NOT_WEARING_GOLD);
                    return nearestPlayerNotWearingGold.isPresent() && isAttackAllowed(nearestPlayerNotWearingGold.get()) ? nearestPlayerNotWearingGold : Optional.empty();
                }
            }
        }
    }

    @Override
    public void setCoreAndDefault() {
        this.dynamicBrain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        this.dynamicBrain.setDefaultActivity(Activity.IDLE);
    }

    @Override
    public void initMemories() {
        int i = TIME_BETWEEN_HUNTS.randomValue(this.mob.level.random);
        this.dynamicBrain.setMemoryWithExpiry(MemoryModuleType.HUNTED_RECENTLY, true, (long)i);
    }

    @Override
    public ActionResultType mobInteract(PlayerEntity player, Hand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (canPiglinAdmire(this.mob, itemstack)) {
            ItemStack singleton = itemstack.split(1);
            holdInOffhand(this.mob, singleton);
            admireItem(this.mob, 120L);
            stopWalking(this.mob);
            return ActionResultType.CONSUME;
        } else {
            return ActionResultType.PASS;
        }
    }

    @Override
    public void updateActivity() {
        Activity activity = this.dynamicBrain.getActiveNonCoreActivity().orElse((Activity)null);
        this.dynamicBrain.setActiveActivityToFirstValid(ImmutableList.of(Activity.ADMIRE_ITEM, Activity.FIGHT, Activity.AVOID, Activity.CELEBRATE, Activity.RIDE, Activity.IDLE));
        Activity activity1 = this.dynamicBrain.getActiveNonCoreActivity().orElse((Activity)null);
        if (activity != activity1) {
            this.getSoundForCurrentActivity().ifPresent(this.mob::playSound);
        }

        this.mob.setAggressive(this.dynamicBrain.hasMemoryValue(MemoryModuleType.ATTACK_TARGET));
        if (!this.dynamicBrain.hasMemoryValue(MemoryModuleType.RIDE_TARGET) && isPygmyBabyRidingBaby(this.mob)) {
            this.mob.stopRiding();
        }

        if (!this.dynamicBrain.hasMemoryValue(MemoryModuleType.CELEBRATE_LOCATION)) {
            this.dynamicBrain.eraseMemory(MemoryModuleType.DANCING);
        }

        this.mob.setDancing(this.dynamicBrain.hasMemoryValue(MemoryModuleType.DANCING));
    }

    @Override
    public SoundEvent getSoundForActivity(Activity activity) {
        if (activity == Activity.FIGHT) {
            return SoundEvents.PIGLIN_ANGRY;
        } else if (this.mob.isConverting()) {
            return SoundEvents.PIGLIN_RETREAT;
        } else if (activity == Activity.AVOID && isNearAvoidTarget(this.mob)) {
            return SoundEvents.PIGLIN_RETREAT;
        } else if (activity == Activity.ADMIRE_ITEM) {
            return SoundEvents.PIGLIN_ADMIRING_ITEM;
        } else if (activity == Activity.CELEBRATE) {
            return SoundEvents.PIGLIN_CELEBRATE;
        } else if (seesPlayerHoldingWantedItem(this.mob)) {
            return SoundEvents.PIGLIN_JEALOUS;
        } else {
            return isNearRepellent(this.mob) ? SoundEvents.PIGLIN_RETREAT : SoundEvents.PIGLIN_AMBIENT;
        }
    }

    @Override
    public void wasHurtBy(LivingEntity entity) {
        if (!(entity instanceof PiglinPygmyEntity)) {
            if (isHoldingItemInOffHand(this.mob)) {
                stopHoldingOffHandItem(this.mob, false);
            }

            Brain<PiglinPygmyEntity> brain = this.mob.getBrain();
            brain.eraseMemory(MemoryModuleType.CELEBRATE_LOCATION);
            brain.eraseMemory(MemoryModuleType.DANCING);
            brain.eraseMemory(MemoryModuleType.ADMIRING_ITEM);
            if (entity instanceof PlayerEntity) {
                brain.setMemoryWithExpiry(MemoryModuleType.ADMIRING_DISABLED, true, 400L);
            }

            getAvoidTarget(this.mob).ifPresent((p_234462_2_) -> {
                if (p_234462_2_.getType() != entity.getType()) {
                    brain.eraseMemory(MemoryModuleType.AVOID_TARGET);
                }

            });
            if (this.mob.isBaby()) {
                brain.setMemoryWithExpiry(MemoryModuleType.AVOID_TARGET, entity, 100L);
                if (isAttackAllowed(entity)) {
                    broadcastAngerTarget(this.mob, entity);
                }

            } else if (entity instanceof HoglinEntity && hoglinsOutnumberPiglins(this.mob)) {
                setAvoidTargetAndDontHuntForAWhile(this.mob, entity);
                broadcastRetreatToPygmies(this.mob, entity);
            } else {
                maybeRetaliate(this.mob, entity);
            }
        }
    }

    @Override
    protected List<Pair<Task<? super T>, Integer>> getIdleLookBehaviors(){
        float maxDist = 8.0F;
        return Arrays.asList(
                Pair.of(new LookAtEntityTask(EntityType.PLAYER, maxDist), 1),
                Pair.of(new LookAtEntityTask(EntityType.PIGLIN, maxDist), 1),
                Pair.of(new LookAtEntityTask(maxDist), 1),
                Pair.of(new DummyTask(30, 60), 1)
        );
    }

    @Override
    protected List<Pair<Task<? super T>, Integer>> getIdleMovementBehaviors(){
        float speedModifer = 8.0F;
        return Arrays.asList(
                Pair.of(new WalkRandomlyTask(speedModifer), 2),
                Pair.of(InteractWithEntityTask.of(EntityType.PIGLIN, 8, MemoryModuleType.INTERACTION_TARGET, speedModifer, 2), 2),
                Pair.of(new SupplementedTask<>(
                        TaskManager::doesntSeeAnyPlayerHoldingWantedItem,
                        new WalkTowardsLookTargetTask(speedModifer, 3)), 2),
                Pair.of(new DummyTask(30, 60), 1)
        );
    }

    @Override
    protected List<Task<? super T>> getCoreTasks() {
        List<Task<? super T>> coreTasks = super.getCoreTasks();
        coreTasks.add(
                new LookTask(45, 90));
        coreTasks.add(
                new WalkToTargetTask());
        coreTasks.add(
                new InteractWithDoorTask());
        coreTasks.add(babyAvoidNemesis());
        coreTasks.add(avoidZombified());
        coreTasks.add(new FinishAdmiringItemTask<>(PiglinTaskManager::performBarter, TaskManager::canFinishAdmiringOffhandItem));
        coreTasks.add(new AdmiringItemTask<>(120, PiglinTaskManager::isPiglinLoved));
        coreTasks.add(new EndAttackTask(300, PiglinTaskManager::wantsToDance));
        coreTasks.add(
                new GetAngryTask<>());
        return coreTasks;
    }

    @Override
    protected List<Task<? super T>> getIdleTasks() {
        List<Task<? super T>> idleTasks = super.getIdleTasks();
        idleTasks.add(new LookAtEntityTask(PiglinTaskManager::isPlayerHoldingLovedItem, 14.0F));
        idleTasks.add(
                new ForgetAttackTargetTask<T>(
                        AbstractPiglinEntity::isAdult,
                        TaskManager::findNearestValidAttackTargetFor
                ));
        idleTasks.add(
                new SupplementedTask<>(PiglinReflectionHelper::reflectCanHunt, new PiglinsHuntHoglinsTask<>())
        );
        idleTasks.add(
                avoidRepellent()
        );
        idleTasks.add(
                babySometimesRideBabyHoglin()
        );
        idleTasks.add(
                createIdleLookBehaviors()
        );
        idleTasks.add(
                createIdleMovementBehaviors()
        );
        idleTasks.add(
                new FindInteractionAndLookTargetTask(EntityType.PLAYER, 4)
        );
        return idleTasks;
    }

    @Override
    protected List<Task<? super T>> getFightTasks() {
        List<Task<? super T>> fightTasks = super.getFightTasks();
        fightTasks.add(
                new FindNewAttackTargetTask<>((potentialAttackTarget) -> !isNearestValidAttackTarget(potentialAttackTarget))
        );
        fightTasks.add(
                new SupplementedTask<>(TaskManager::hasCrossbow, new AttackStrafingTask<>(5, 0.75F))
        );
        fightTasks.add(
                new MoveToTargetTask(1.0F)
        );
        fightTasks.add(
                new AttackTargetTask(20)
        );
        fightTasks.add(new ShootTargetTask<>());
        fightTasks.add(new FinishHuntingTask<>(EntityType.HOGLIN, PiglinTaskManager::dontKillAnyMoreHoglinsForAWhile));
        fightTasks.add(new PredicateTask<>(PiglinTaskManager::isNearZombified, MemoryModuleType.ATTACK_TARGET));
        return fightTasks;
    }

    @Override
    protected List<Task<? super T>> getAdmireItemTasks() {
        List<Task<? super T>> admireItemTasks = super.getAdmireItemTasks();
        admireItemTasks.add(
                new PickupWantedItemTask<>(PiglinTaskManager::isNotHoldingLovedItemInOffHand, 1.0F, true, 9)
        );
        admireItemTasks.add(
                new ForgetAdmiringItemTask<>(9)
        );
        admireItemTasks.add(
                new IgnoreAdmiringItemTask<>(200, 200, TaskManager::isNotHoldingItemInOffhand)
        );
        return admireItemTasks;
    }

    @Override
    protected List<Task<? super T>> getCelebrateTasks() {
        List<Task<? super T>> celebrateTasks = super.getCelebrateTasks();
        celebrateTasks.add(
                avoidRepellent()
        );
        celebrateTasks.add(
                new ForgetAttackTargetTask<PiglinPygmyEntity>(AbstractPiglinEntity::isAdult, TaskManager::findNearestValidAttackTargetFor)
                );
        celebrateTasks.add(
                new ForgetAttackTargetTask<PiglinPygmyEntity>(AbstractPiglinEntity::isAdult, TaskManager::findNearestValidAttackTargetFor)
                );
        celebrateTasks.add(
                new SupplementedTask<PiglinPygmyEntity>((p_234457_0_) -> !p_234457_0_.isDancing(), new HuntCelebrationTask<>(2, 1.0F))
                );
        celebrateTasks.add(
                new SupplementedTask<PiglinPygmyEntity>(PiglinPygmyEntity::isDancing, new HuntCelebrationTask<>(4, 0.6F))
                );
        celebrateTasks.add(
                new FirstShuffledTask<>(
                        ImmutableList.of(
                                Pair.of(new LookAtEntityTask(EntityType.PIGLIN, 8.0F), 1),
                                Pair.of(new WalkRandomlyTask(0.6F, 2, 1), 1),
                                Pair.of(new DummyTask(10, 20), 1)))
        );
        return celebrateTasks;
    }

    @Override
    protected List<Task<? super T>> getAvoidTasks() {
        List<Task<? super T>> avoidTasks = super.getAvoidTasks();
        avoidTasks.add(
                RunAwayTask.entity(MemoryModuleType.AVOID_TARGET, 1.0F, 12, true)
        );
        avoidTasks.add(
                createIdleLookBehaviors()
                );
        avoidTasks.add(
                createIdleMovementBehaviors()
                );
        avoidTasks.add(
                new PredicateTask<PiglinPygmyEntity>(PiglinTaskManager::wantsToStopFleeing, MemoryModuleType.AVOID_TARGET)
        );
        return avoidTasks;
    }

    @Override
    protected List<Task<? super T>> getRideTasks() {
        List<Task<? super T>> rideTasks = super.getRideTasks();
        rideTasks.add(
                new RideEntityTask<>(0.8F)
                );
        rideTasks.add(
                new LookAtEntityTask(PiglinTaskManager::isPlayerHoldingLovedItem, 8.0F)
                );
        rideTasks.add(
                new SupplementedTask<>(Entity::isPassenger, createIdleLookBehaviors())
                );
        rideTasks.add(
                new StopRidingEntityTask<>(8, PiglinTaskManager::wantsToStopRiding)
        );
        return rideTasks;
    }

    // STATIC HELPER METHODS

    public static void broadcastRetreatToPygmies(AbstractPiglinEntity piglin, LivingEntity targetIn) {
        getVisibleAdultPiglins(piglin)
                .stream()
                .filter(
                        (visibleAdultPiglin) -> visibleAdultPiglin instanceof PiglinPygmyEntity)
                .forEach(
                        (pygmy) -> retreatFromNearestTarget(pygmy, targetIn));
    }

    public static boolean isPygmyBabyRidingBaby(AbstractPiglinEntity piglin) {
        if (!piglin.isBaby()) {
            return false;
        } else {
            Entity vehicle = piglin.getVehicle();
            return vehicle instanceof PiglinPygmyEntity && ((PiglinPygmyEntity)vehicle).isBaby() || vehicle instanceof HoglinEntity && ((HoglinEntity)vehicle).isBaby();
        }
    }

}
