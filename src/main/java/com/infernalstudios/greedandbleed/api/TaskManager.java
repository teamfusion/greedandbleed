package com.infernalstudios.greedandbleed.api;

import com.google.common.collect.ImmutableList;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.BrainUtil;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.schedule.Activity;
import net.minecraft.entity.ai.brain.task.*;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.piglin.AbstractPiglinEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.vector.Vector3d;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/***
 * An extensible class for initializing and handling a Brain for a LivingEntity.
 * This was based on the class PiglinTasks.
 * @author Thelnfamous1
 * @param <T> A class that extends LivingEntity
 */
public abstract class TaskManager<T extends LivingEntity & IHasTaskManager> implements ITaskManager<T> {
    protected final T mob;
    protected final Brain<T> dynamicBrain;

    /**
     * Constructs a new TaskMaster instance given a LivingEntity and a Brain.
     * Note that this should only be instantiated inside LivingEntity#makeBrain,
     * ideally using IHasTaskMaster#createTaskMaster,
     * and dynamicBrain should be the Brain returned from LivingEntity#brainProvider().makeBrain(dynamic)
     * where dynamic is the Dynamic instance passed into LivingEntity#makeBrain.
     * @param mob The LivingEntity to associate with this TaskMaster
     * @param dynamicBrain The Brain to associate with this TaskMaster
     */
    public TaskManager(T mob, Brain<T> dynamicBrain) {
        this.mob = mob;
        this.dynamicBrain = dynamicBrain;
        this.initActivities();
        this.setCoreAndDefault();
        this.dynamicBrain.useDefaultActivity();
    }

    /**
     * Accessor for the Brain associated with this TaskMaster
     * @return dynamicBrain
     */
    @Override
    public Brain<T> getBrain() {
        return this.dynamicBrain;
    }

    /**
     * Retrieves an Optional SoundEvent for the LivingEntity associated with this TaskMaster instance
     * @return An Optional SoundEvent for mob
     */
    @Override
    public Optional<SoundEvent> getSoundForCurrentActivity() {
        return this.mob.getBrain()
                .getActiveNonCoreActivity()
                .map(this::getSoundForActivity);
    }

    /**
     * Initializes an Activity using a List of Tasks and an integer
     * @param taskList A List of Tasks to associate with the Activity
     * @param activity The Activity to initialize
     * @param priorityStart The value to start generating priority values from in Brain#createPriorityPairs
     */
    protected void initActivity(List<? extends Task<? super T>> taskList, Activity activity, int priorityStart) {
        ImmutableList<Task<? super T>> immutableTaskList =
                ImmutableList.copyOf(taskList);
        this.dynamicBrain.addActivity(
                activity, priorityStart,
                immutableTaskList);
    }
    /**
     * Initializes an Activity using a List of Tasks, an integer and a MemoryModuleType
     * When this particular Activity is stopped, the supplied MemoryModuleType will have its data removed
     * @param taskList A List of Tasks to associate with the Activity
     * @param activity The Activity to initialize
     * @param priorityStart The value to start generating priority values from in Brain#createPriorityPairs
     * @param memoryModuleType The MemoryModuleType to remove data from when the Activity is stopped
     */
    protected void initActivityAndRemoveMemoryWhenStopped(List<? extends Task<? super T>> taskList, Activity activity, int priorityStart, MemoryModuleType memoryModuleType) {
        ImmutableList<Task<? super T>> immutableTaskList =
                ImmutableList.copyOf(taskList);
        this.dynamicBrain.addActivityAndRemoveMemoryWhenStopped(
                activity, priorityStart,
                immutableTaskList,
                memoryModuleType);
    }

    /**
     * Initializes the CORE Activity for the dynamicBrain
     * @param priorityStart The value to start generating priority values from in Brain#createPriorityPairs
     */
    protected void initCoreActivity(int priorityStart) {
        List<Task<? super T>> coreTasks = getCoreTasks();
        this.initActivity(coreTasks, Activity.CORE, priorityStart);
    }
    /**
     * Initializes the IDLE Activity for the dynamicBrain
     * @param priorityStart The value to start generating priority values from in Brain#createPriorityPairs
     */
    protected void initIdleActivity(int priorityStart) {
        List<Task<? super T>> idleTasks = getIdleTasks();
        this.initActivity(idleTasks, Activity.IDLE, priorityStart);
    }
    /**
     * Initializes the WORK Activity for the dynamicBrain
     * @param priorityStart The value to start generating priority values from in Brain#createPriorityPairs
     */
    //TODO: make initActivityWithConditions
    protected void initWorkTasks(int priorityStart) {
        List<Task<? super T>> workTasks = getWorkTasks();
        this.initActivity(workTasks, Activity.WORK, priorityStart);
    }
    /**
     * Initializes the PLAY Activity for the dynamicBrain
     * @param priorityStart The value to start generating priority values from in Brain#createPriorityPairs
     */
    protected void initPlayActivity(int priorityStart) {
        List<Task<? super T>> playTasks = getPlayTasks();
        this.initActivity(playTasks, Activity.PLAY, priorityStart);
    }
    /**
     * Initializes the REST Activity for the dynamicBrain
     * @param priorityStart The value to start generating priority values from in Brain#createPriorityPairs
     */
    protected void initRestActivity(int priorityStart) {
        List<Task<? super T>> restTasks = getRestTasks();
        this.initActivity(restTasks, Activity.REST, priorityStart);
    }
    /**
     * Initializes the MEET Activity for the dynamicBrain
     * @param priorityStart The value to start generating priority values from in Brain#createPriorityPairs
     */
    //TODO: make initActivityWithConditions
    protected void initMeetActivity(int priorityStart) {
        List<Task<? super T>> meetTasks = getMeetTasks();
        this.initActivity(meetTasks, Activity.MEET, priorityStart);
    }
    /**
     * Initializes the PANIC Activity for the dynamicBrain
     * @param priorityStart The value to start generating priority values from in Brain#createPriorityPairs
     */
    protected void initPanicActivity(int priorityStart) {
        List<Task<? super T>> panicTasks = getPanicTasks();
        this.initActivity(panicTasks, Activity.PANIC, priorityStart);
    }
    /**
     * Initializes the RAID Activity for the dynamicBrain
     * @param priorityStart The value to start generating priority values from in Brain#createPriorityPairs
     */
    protected void initRaidActivity(int priorityStart) {
        List<Task<? super T>> raidTasks = getRaidTasks();
        this.initActivity(raidTasks, Activity.RAID, priorityStart);
    }
    /**
     * Initializes the PRE-RAID Activity for the dynamicBrain
     * @param priorityStart The value to start generating priority values from in Brain#createPriorityPairs
     */
    protected void initPreRaidActivity(int priorityStart) {
        List<Task<? super T>> preRaidTasks = getPreRaidTasks();
        this.initActivity(preRaidTasks, Activity.PRE_RAID, priorityStart);
    }
    /**
     * Initializes the HIDE Activity for the dynamicBrain
     * @param priorityStart The value to start generating priority values from in Brain#createPriorityPairs
     */
    protected void initHideActivity(int priorityStart) {
        List<Task<? super T>> hideTasks = getHideTasks();
        this.initActivity(hideTasks, Activity.HIDE, priorityStart);
    }
    /**
     * Initializes the FIGHT Activity for the dynamicBrain
     * @param priorityStart The value to start generating priority values from in Brain#createPriorityPairs
     */
    protected void initFightActivity(int priorityStart) {
        List<Task<? super T>> fightTasks = getFightTasks();
        this.initActivityAndRemoveMemoryWhenStopped(fightTasks, Activity.FIGHT, priorityStart, MemoryModuleType.ATTACK_TARGET);
    }
    /**
     * Initializes the CELEBRATE Activity for the dynamicBrain
     * @param priorityStart The value to start generating priority values from in Brain#createPriorityPairs
     */
    protected void initCelebrateActivity(int priorityStart) {
        List<Task<? super T>> celebrateTasks = getCelebrateTasks();
        this.initActivityAndRemoveMemoryWhenStopped(celebrateTasks, Activity.CELEBRATE, priorityStart, MemoryModuleType.CELEBRATE_LOCATION);
    }
    /**
     * Initializes the ADMIRE_ITEM Activity for the dynamicBrain
     * @param priorityStart The value to start generating priority values from in Brain#createPriorityPairs
     */
    protected void initAdmireItemActivity(int priorityStart) {
        List<Task<? super T>> admireItemTasks = getAdmireItemTasks();
        this.initActivityAndRemoveMemoryWhenStopped(admireItemTasks, Activity.ADMIRE_ITEM, priorityStart, MemoryModuleType.ADMIRING_ITEM);
    }
    /**
     * Initializes the AVOID Activity for the dynamicBrain
     * @param priorityStart The value to start generating priority values from in Brain#createPriorityPairs
     */
    protected void initAvoidActivity(int priorityStart) {
        List<Task<? super T>> avoidTasks = getAvoidTasks();
        this.initActivityAndRemoveMemoryWhenStopped(avoidTasks, Activity.AVOID, priorityStart, MemoryModuleType.AVOID_TARGET);
    }
    /**
     * Initializes the RIDE Activity for the dynamicBrain
     * @param priorityStart The value to start generating priority values from in Brain#createPriorityPairs
     */
    protected void initRideActivity(int priorityStart) {
        List<Task<? super T>> getRideTasks = getRideTasks();
        this.initActivityAndRemoveMemoryWhenStopped(getRideTasks, Activity.RIDE, priorityStart, MemoryModuleType.RIDE_TARGET);
    }

    protected boolean isNearestValidAttackTarget(LivingEntity potentialAttackTarget) {
        return findNearestValidAttackTarget().filter(
                (livingEntity) -> livingEntity == potentialAttackTarget)
                .isPresent();
    }

    // LIST METHODS

    protected List<Task<? super T>> getCoreTasks(){
        return Collections.emptyList();
    }

    protected List<Task<? super T>> getIdleTasks(){
        return Collections.emptyList();
    }

    protected List<Task<? super T>> getWorkTasks(){
        return Collections.emptyList();
    }

    protected List<Task<? super T>> getPlayTasks(){
        return Collections.emptyList();
    }

    protected List<Task<? super T>> getRestTasks(){
        return Collections.emptyList();
    }

    protected List<Task<? super T>> getMeetTasks(){
        return Collections.emptyList();
    }

    protected List<Task<? super T>> getPanicTasks(){
        return Collections.emptyList();
    }

    protected List<Task<? super T>> getRaidTasks(){
        return Collections.emptyList();
    }

    protected List<Task<? super T>> getPreRaidTasks(){
        return Collections.emptyList();
    }

    protected List<Task<? super T>> getHideTasks(){
        return Collections.emptyList();
    }

    protected List<Task<? super T>> getFightTasks(){
        return Collections.emptyList();
    }

    protected List<Task<? super T>> getCelebrateTasks(){
        return Collections.emptyList();
    }

    protected List<Task<? super T>> getAdmireItemTasks(){
        return Collections.emptyList();
    }

    protected List<Task<? super T>> getAvoidTasks(){
        return Collections.emptyList();
    }

    protected List<Task<? super T>> getRideTasks(){
        return Collections.emptyList();
    }

    // ABSTRACT METHODS

    protected abstract void initActivities();

    protected abstract void setCoreAndDefault();

    public abstract SoundEvent getSoundForActivity(Activity activity);

    // STATIC HELPER METHODS

    public static Optional<? extends LivingEntity> findNearestValidAttackTargetFor(IHasTaskManager hasTaskMaster){
        return hasTaskMaster.getTaskManager().findNearestValidAttackTarget();
    }

    public static boolean hasCrossbow(LivingEntity livingEntity) {
        return livingEntity.isHolding(item -> item instanceof CrossbowItem);
    }

    public static boolean isNotHoldingItemInOffhand(LivingEntity livingEntity) {
        return livingEntity.getOffhandItem().isEmpty();
    }

    public static boolean isHoldingItemInOffHand(LivingEntity livingEntity) {
        return !livingEntity.getOffhandItem().isEmpty();
    }

    public static Optional<LivingEntity> getAvoidTarget(LivingEntity livingEntity) {
        return livingEntity.getBrain().hasMemoryValue(MemoryModuleType.AVOID_TARGET) ? livingEntity.getBrain().getMemory(MemoryModuleType.AVOID_TARGET) : Optional.empty();
    }

    public static boolean isAttackAllowed(LivingEntity livingEntity) {
        return EntityPredicates.ATTACK_ALLOWED.test(livingEntity);
    }

    public static boolean seesPlayerHoldingWantedItem(LivingEntity livingEntity) {
        return livingEntity.getBrain().hasMemoryValue(MemoryModuleType.NEAREST_PLAYER_HOLDING_WANTED_ITEM);
    }

    public static boolean doesntSeeAnyPlayerHoldingWantedItem(LivingEntity livingEntity) {
        return !seesPlayerHoldingWantedItem(livingEntity);
    }

    public static boolean wasHurtRecently(LivingEntity livingEntity) {
        return livingEntity.getBrain().hasMemoryValue(MemoryModuleType.HURT_BY);
    }

    public static List<AbstractPiglinEntity> getVisibleAdultPiglins(LivingEntity livingEntity) {
        return livingEntity.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLINS).orElse(ImmutableList.of());
    }

    public static List<AbstractPiglinEntity> getAdultPiglins(LivingEntity livingEntity) {
        return livingEntity.getBrain().getMemory(MemoryModuleType.NEARBY_ADULT_PIGLINS).orElse(ImmutableList.of());
    }

    public static Optional<PlayerEntity> getNearestVisibleTargetablePlayer(LivingEntity livingEntity) {
        return livingEntity.getBrain().hasMemoryValue(MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER) ? livingEntity.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER) : Optional.empty();
    }

    public static boolean isAdmiringItem(LivingEntity livingEntity) {
        return livingEntity.getBrain().hasMemoryValue(MemoryModuleType.ADMIRING_ITEM);
    }

    public static void admireItem(LivingEntity livingEntity, long memoryTime) {
        livingEntity.getBrain().setMemoryWithExpiry(MemoryModuleType.ADMIRING_ITEM, true, memoryTime);
    }

    public static void stopWalking(MobEntity mob) {
        mob.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
        mob.getNavigation().stop();
    }

    public static ItemStack removeOneItemFromItemEntity(ItemEntity itemEntity) {
        ItemStack itemstack = itemEntity.getItem();
        ItemStack singleton = itemstack.split(1);
        if (itemstack.isEmpty()) {
            itemEntity.remove();
        } else {
            itemEntity.setItem(itemstack);
        }

        return singleton;
    }

    public static void eat(LivingEntity livingEntity, long memoryTime) {
        livingEntity.getBrain().setMemoryWithExpiry(MemoryModuleType.ATE_RECENTLY, true, memoryTime);
    }

    public static boolean isAdmiringDisabled(LivingEntity livingEntity) {
        return livingEntity.getBrain().hasMemoryValue(MemoryModuleType.ADMIRING_DISABLED);
    }

    public static boolean hasEatenRecently(LivingEntity livingEntity) {
        return livingEntity.getBrain().hasMemoryValue(MemoryModuleType.ATE_RECENTLY);
    }

    public static Optional<LivingEntity> getAngerTarget(LivingEntity livingEntity) {
        return BrainUtil.getLivingEntityFromUUIDMemory(livingEntity, MemoryModuleType.ANGRY_AT);
    }

    public static void setHuntedRecently(LivingEntity livingEntity, long memoryTime) {
        livingEntity.getBrain().setMemoryWithExpiry(MemoryModuleType.HUNTED_RECENTLY, true, memoryTime);
    }

    public static boolean canFinishAdmiringOffhandItem(LivingEntity livingEntity) {
        return !livingEntity.getOffhandItem().isEmpty() && !livingEntity.getOffhandItem().isShield(livingEntity);

    }

    public static <T extends LivingEntity & IHasTaskManager> void holdInOffhand(T entity, ItemStack stack) {
        if (isHoldingItemInOffHand(entity)) {
            entity.spawnAtLocation(entity.getItemInHand(Hand.OFF_HAND));
        }

        entity.holdInOffHand(stack);
    }

    public static <T extends CreatureEntity & IHasInventory> void putInInventory(T entity, ItemStack stack) {
        ItemStack itemstack = entity.addToInventory(stack);
        throwItemsTowardRandomPos(entity, Collections.singletonList(itemstack));
    }

    public static Vector3d getRandomNearbyPos(CreatureEntity creature) {
        Vector3d vector3d = RandomPositionGenerator.getLandPos(creature, 4, 2);
        return vector3d == null ? creature.position() : vector3d;
    }

    public static void throwItemsTowardRandomPos(CreatureEntity creature, List<ItemStack> throwingItems) {
        throwItemsTowardPos(creature, throwingItems, getRandomNearbyPos(creature));
    }

    public static void throwItemsTowardPlayer(CreatureEntity creature, PlayerEntity player, List<ItemStack> throwingItems) {
        throwItemsTowardPos(creature, throwingItems, player.position());
    }

    public static void throwItemsTowardPos(CreatureEntity creature, List<ItemStack> throwingItems, Vector3d targetVector) {
        if (!throwingItems.isEmpty()) {
            creature.swing(Hand.OFF_HAND);

            for(ItemStack itemstack : throwingItems) {
                BrainUtil.throwItem(creature, itemstack, targetVector.add(0.0D, 1.0D, 0.0D));
            }
        }
    }

    public static void throwItems(CreatureEntity creature, List<ItemStack> throwingItems) {
        Optional<PlayerEntity> optional = creature.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_PLAYER);
        if (optional.isPresent()) {
            throwItemsTowardPlayer(creature, optional.get(), throwingItems);
        } else {
            throwItemsTowardRandomPos(creature, throwingItems);
        }
    }

    public static boolean isNearRepellent(LivingEntity livingEntity) {
        return livingEntity.getBrain().hasMemoryValue(MemoryModuleType.NEAREST_REPELLENT);
    }
}
