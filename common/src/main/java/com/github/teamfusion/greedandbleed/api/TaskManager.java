package com.github.teamfusion.greedandbleed.api;

import com.github.teamfusion.greedandbleed.common.entity.piglin.pygmy.GBPygmy;
import com.github.teamfusion.greedandbleed.common.registry.MemoryRegistry;
import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.behavior.RunOne;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.phys.Vec3;

import java.util.*;

/***
 * An extensible class for initializing and handling a Brain for a LivingEntity.
 * This was based on the class PiglinTasks.
 * @author Thelnfamous1
 * @param <T> A class that extends LivingEntity
 */
public abstract class TaskManager<T extends LivingEntity & HasTaskManager> implements ITaskManager<T> {
    protected final T mob;
    protected final Brain<T> dynamicBrain;

    /**
     * Constructs a new TaskManager instance given a LivingEntity and a Brain.
     * Note that this should only be instantiated inside LivingEntity#makeBrain,
     * ideally using IHasTaskManager#createTaskManager,
     * and dynamicBrain should be the Brain returned from LivingEntity#brainProvider().makeBrain(dynamic)
     * where dynamic is the Dynamic instance passed into LivingEntity#makeBrain.
     * @param mob The LivingEntity to associate with this TaskManager
     * @param dynamicBrain The Brain to associate with this TaskManager
     */
    public TaskManager(T mob, Brain<T> dynamicBrain) {
        this.mob = mob;
        this.dynamicBrain = dynamicBrain;
        this.initActivities();
        this.setCoreAndDefault();
        this.dynamicBrain.useDefaultActivity();
    }

    @Override
    public Brain<T> getBrain() {
        return this.dynamicBrain;
    }

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
    protected void initActivity(List<? extends BehaviorControl<? super T>> taskList, Activity activity, int priorityStart) {
        ImmutableList<BehaviorControl<? super T>> immutableTaskList =
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
    protected void initActivityAndRemoveMemoryWhenStopped(List<? extends BehaviorControl<? super T>> taskList, Activity activity, int priorityStart, MemoryModuleType<?> memoryModuleType) {
        ImmutableList<BehaviorControl<? super T>> immutableTaskList =
                ImmutableList.copyOf(taskList);
        this.dynamicBrain.addActivityAndRemoveMemoryWhenStopped(
                activity, priorityStart,
                immutableTaskList,
                memoryModuleType);
    }

    protected void initActivityWithConditions(List<Pair<Integer, BehaviorControl<? super T>>> taskList, Activity activity, Set<Pair<MemoryModuleType<?>, MemoryStatus>> condition) {
        ImmutableList<Pair<Integer, BehaviorControl<? super T>>> immutableTaskList =
                ImmutableList.copyOf(taskList);

        Set<Pair<MemoryModuleType<?>, MemoryStatus>> conditions =
                Set.copyOf(condition);
        this.dynamicBrain.addActivityWithConditions(
                activity,
                immutableTaskList, conditions);
    }

    /**
     * Initializes the CORE Activity for the dynamicBrain
     * @param priorityStart The value to start generating priority values from in Brain#createPriorityPairs
     */
    protected void initCoreActivity(int priorityStart) {
        List<BehaviorControl<? super T>> coreTasks = getCoreTasks();
        this.initActivity(coreTasks, Activity.CORE, priorityStart);
    }
    /**
     * Initializes the IDLE Activity for the dynamicBrain
     * @param priorityStart The value to start generating priority values from in Brain#createPriorityPairs
     */
    protected void initIdleActivity(int priorityStart) {
        List<BehaviorControl<? super T>> idleTasks = getIdleTasks();
        this.initActivity(idleTasks, Activity.IDLE, priorityStart);
    }
    /**
     * Initializes the WORK Activity for the dynamicBrain
     * @param priorityStart The value to start generating priority values from in Brain#createPriorityPairs
     */
    //TODO: make initActivityWithConditions
    protected void initWorkActivity(int priorityStart) {
        List<BehaviorControl<? super T>> workTasks = getWorkTasks();
        this.initActivity(workTasks, Activity.WORK, priorityStart);
    }
    /**
     * Initializes the PLAY Activity for the dynamicBrain
     * @param priorityStart The value to start generating priority values from in Brain#createPriorityPairs
     */
    protected void initPlayActivity(int priorityStart) {
        List<BehaviorControl<? super T>> playTasks = getPlayTasks();
        this.initActivity(playTasks, Activity.PLAY, priorityStart);
    }
    /**
     * Initializes the REST Activity for the dynamicBrain
     * @param priorityStart The value to start generating priority values from in Brain#createPriorityPairs
     */
    protected void initRestActivity(int priorityStart) {
        List<BehaviorControl<? super T>> restTasks = getRestTasks();
        this.initActivity(restTasks, Activity.REST, priorityStart);
    }
    /**
     * Initializes the MEET Activity for the dynamicBrain
     * @param priorityStart The value to start generating priority values from in Brain#createPriorityPairs
     */
    //TODO: make initActivityWithConditions
    protected void initMeetActivity(int priorityStart) {
        List<BehaviorControl<? super T>> meetTasks = getMeetTasks();
        this.initActivity(meetTasks, Activity.MEET, priorityStart);
    }
    /**
     * Initializes the PANIC Activity for the dynamicBrain
     * @param priorityStart The value to start generating priority values from in Brain#createPriorityPairs
     */
    protected void initPanicActivity(int priorityStart) {
        List<BehaviorControl<? super T>> panicTasks = getPanicTasks();
        this.initActivity(panicTasks, Activity.PANIC, priorityStart);
    }
    /**
     * Initializes the RAID Activity for the dynamicBrain
     * @param priorityStart The value to start generating priority values from in Brain#createPriorityPairs
     */
    protected void initRaidActivity(int priorityStart) {
        List<BehaviorControl<? super T>> raidTasks = getRaidTasks();
        this.initActivity(raidTasks, Activity.RAID, priorityStart);
    }
    /**
     * Initializes the PRE-RAID Activity for the dynamicBrain
     * @param priorityStart The value to start generating priority values from in Brain#createPriorityPairs
     */
    protected void initPreRaidActivity(int priorityStart) {
        List<BehaviorControl<? super T>> preRaidTasks = getPreRaidTasks();
        this.initActivity(preRaidTasks, Activity.PRE_RAID, priorityStart);
    }
    /**
     * Initializes the HIDE Activity for the dynamicBrain
     * @param priorityStart The value to start generating priority values from in Brain#createPriorityPairs
     */
    protected void initHideActivity(int priorityStart) {
        List<BehaviorControl<? super T>> hideTasks = getHideTasks();
        this.initActivity(hideTasks, Activity.HIDE, priorityStart);
    }
    /**
     * Initializes the FIGHT Activity for the dynamicBrain
     * @param priorityStart The value to start generating priority values from in Brain#createPriorityPairs
     */
    protected void initFightActivity(int priorityStart) {
        List<BehaviorControl<? super T>> fightTasks = getFightTasks();
        this.initActivityAndRemoveMemoryWhenStopped(fightTasks, Activity.FIGHT, priorityStart, MemoryModuleType.ATTACK_TARGET);
    }
    /**
     * Initializes the CELEBRATE Activity for the dynamicBrain
     * @param priorityStart The value to start generating priority values from in Brain#createPriorityPairs
     */
    protected void initCelebrateActivity(int priorityStart) {
        List<BehaviorControl<? super T>> celebrateTasks = getCelebrateTasks();
        this.initActivityAndRemoveMemoryWhenStopped(celebrateTasks, Activity.CELEBRATE, priorityStart, MemoryModuleType.CELEBRATE_LOCATION);
    }
    /**
     * Initializes the ADMIRE_ITEM Activity for the dynamicBrain
     * @param priorityStart The value to start generating priority values from in Brain#createPriorityPairs
     */
    protected void initAdmireItemActivity(int priorityStart) {
        List<BehaviorControl<? super T>> admireItemTasks = getAdmireItemTasks();
        this.initActivityAndRemoveMemoryWhenStopped(admireItemTasks, Activity.ADMIRE_ITEM, priorityStart, MemoryModuleType.ADMIRING_ITEM);
    }
    /**
     * Initializes the AVOID Activity for the dynamicBrain
     * @param priorityStart The value to start generating priority values from in Brain#createPriorityPairs
     */
    protected void initAvoidActivity(int priorityStart) {
        List<BehaviorControl<? super T>> avoidTasks = getAvoidTasks();
        this.initActivityAndRemoveMemoryWhenStopped(avoidTasks, Activity.AVOID, priorityStart, MemoryModuleType.AVOID_TARGET);
    }
    /**
     * Initializes the RIDE Activity for the dynamicBrain
     * @param priorityStart The value to start generating priority values from in Brain#createPriorityPairs
     */
    protected void initRideActivity(int priorityStart) {
        List<BehaviorControl<? super T>> getRideTasks = getRideTasks();
        this.initActivityAndRemoveMemoryWhenStopped(getRideTasks, Activity.RIDE, priorityStart, MemoryModuleType.RIDE_TARGET);
    }

    protected boolean isNearestValidAttackTarget(LivingEntity potentialAttackTarget) {
        return findNearestValidAttackTarget().filter(
                        (livingEntity) -> livingEntity == potentialAttackTarget)
                .isPresent();
    }


    protected RunOne<T> createIdleLookBehaviors() {
        return new RunOne<>(
                getIdleLookBehaviors()
        );
    }

    protected RunOne<T> createIdleMovementBehaviors() {
        return new RunOne<>(
                getIdleMovementBehaviors()
        );
    }

    // LIST METHODS

    protected List<BehaviorControl<? super T>> getCoreTasks() {
        return new ArrayList<>();
    }

    protected List<BehaviorControl<? super T>> getIdleTasks() {
        return new ArrayList<>();
    }

    protected List<BehaviorControl<? super T>> getWorkTasks() {
        return new ArrayList<>();
    }

    protected List<BehaviorControl<? super T>> getPlayTasks() {
        return new ArrayList<>();
    }

    protected List<BehaviorControl<? super T>> getRestTasks() {
        return new ArrayList<>();
    }

    protected List<BehaviorControl<? super T>> getMeetTasks() {
        return new ArrayList<>();
    }

    protected List<BehaviorControl<? super T>> getPanicTasks() {
        return new ArrayList<>();
    }

    protected List<BehaviorControl<? super T>> getRaidTasks() {
        return new ArrayList<>();
    }

    protected List<BehaviorControl<? super T>> getPreRaidTasks() {
        return new ArrayList<>();
    }

    protected List<BehaviorControl<? super T>> getHideTasks() {
        return new ArrayList<>();
    }

    protected List<BehaviorControl<? super T>> getFightTasks() {
        return new ArrayList<>();
    }

    protected List<BehaviorControl<? super T>> getCelebrateTasks() {
        return new ArrayList<>();
    }

    protected List<BehaviorControl<? super T>> getAdmireItemTasks() {
        return new ArrayList<>();
    }

    protected List<BehaviorControl<? super T>> getAvoidTasks() {
        return new ArrayList<>();
    }

    protected List<BehaviorControl<? super T>> getRideTasks() {
        return new ArrayList<>();
    }

    protected List<Pair<? extends BehaviorControl<? super T>, Integer>> getIdleLookBehaviors() {
        return new ArrayList<>();
    }

    protected List<Pair<? extends BehaviorControl<? super T>, Integer>> getIdleMovementBehaviors() {
        return new ArrayList<>();
    }


    // ABSTRACT METHODS

    protected abstract void initActivities();

    protected abstract void setCoreAndDefault();

    public abstract SoundEvent getSoundForActivity(Activity activity);

    // STATIC HELPER METHODS

    public static Optional<? extends LivingEntity> findNearestValidAttackTargetFor(HasTaskManager hasTaskManager){
        return hasTaskManager.getTaskManager().findNearestValidAttackTarget();
    }

    public static boolean hasCrossbow(LivingEntity livingEntity) {
        return livingEntity.isHolding(item -> item.getItem() instanceof CrossbowItem);
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

    public static boolean isAttackAllowed(LivingEntity livingEntity, LivingEntity target) {
        return Sensor.isEntityAttackableIgnoringLineOfSight(livingEntity, target);
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

    public static List<AbstractPiglin> getVisibleAdultPiglins(LivingEntity livingEntity) {
        return livingEntity.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLINS).orElse(ImmutableList.of());
    }

    public static List<AbstractPiglin> getAdultPiglins(LivingEntity livingEntity) {
        return livingEntity.getBrain().getMemory(MemoryModuleType.NEARBY_ADULT_PIGLINS).orElse(ImmutableList.of());
    }

    public static List<GBPygmy> getVisibleAdultPygmys(LivingEntity livingEntity) {
        return livingEntity.getBrain().getMemory(MemoryRegistry.NEAREST_VISIBLE_ADULT_PYGMYS.get()).orElse(ImmutableList.of());
    }

    public static List<GBPygmy> getAdultPygmys(LivingEntity livingEntity) {
        return livingEntity.getBrain().getMemory(MemoryRegistry.NEARBY_ADULT_PYGMYS.get()).orElse(ImmutableList.of());
    }


    public static Optional<Player> getNearestVisibleTargetablePlayer(LivingEntity livingEntity) {
        return livingEntity.getBrain().hasMemoryValue(MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER) ? livingEntity.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER) : Optional.empty();
    }

    public static boolean isAdmiringItem(LivingEntity livingEntity) {
        return livingEntity.getBrain().hasMemoryValue(MemoryModuleType.ADMIRING_ITEM);
    }

    public static void admireItem(LivingEntity livingEntity, long memoryTime) {
        livingEntity.getBrain().setMemoryWithExpiry(MemoryModuleType.ADMIRING_ITEM, true, memoryTime);
    }

    public static void stopWalking(Mob mob) {
        mob.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
        mob.getNavigation().stop();
    }

    public static ItemStack removeOneItemFromItemEntity(ItemEntity itemEntity) {
        ItemStack itemstack = itemEntity.getItem();
        ItemStack singleton = itemstack.split(1);
        if (itemstack.isEmpty()) {
            itemEntity.discard();
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
        return BehaviorUtils.getLivingEntityFromUUIDMemory(livingEntity, MemoryModuleType.ANGRY_AT);
    }

    public static void setHuntedRecently(LivingEntity livingEntity, long memoryTime) {
        livingEntity.getBrain().setMemoryWithExpiry(MemoryModuleType.HUNTED_RECENTLY, true, memoryTime);
    }

    public static boolean canFinishAdmiringOffhandItem(LivingEntity livingEntity) {
        return !livingEntity.getOffhandItem().isEmpty() && !(livingEntity.getOffhandItem().getItem() instanceof ShieldItem);

    }

    public static <T extends LivingEntity & HasTaskManager> void holdInOffhand(T entity, ItemStack stack) {
        if (isHoldingItemInOffHand(entity)) {
            entity.spawnAtLocation(entity.getItemInHand(InteractionHand.OFF_HAND));
        }

        entity.holdInOffHand(stack);
    }

    public static <T extends PathfinderMob & HasInventory> void putInInventory(T entity, ItemStack stack) {
        ItemStack itemstack = entity.addToInventory(stack);
        throwItemsTowardRandomPos(entity, Collections.singletonList(itemstack));
    }

    public static Vec3 getRandomNearbyPos(PathfinderMob creature) {
        Vec3 Vec3 = LandRandomPos.getPos(creature, 4, 2);
        return Vec3 == null ? creature.position() : Vec3;
    }

    public static void throwItemsTowardRandomPos(PathfinderMob creature, List<ItemStack> throwingItems) {
        throwItemsTowardPos(creature, throwingItems, getRandomNearbyPos(creature));
    }

    public static void throwItemsTowardPlayer(PathfinderMob creature, Player player, List<ItemStack> throwingItems) {
        throwItemsTowardPos(creature, throwingItems, player.position());
    }

    public static void throwItemsTowardPos(PathfinderMob creature, List<ItemStack> throwingItems, Vec3 targetVector) {
        if (!throwingItems.isEmpty()) {
            creature.swing(InteractionHand.OFF_HAND);

            for(ItemStack itemstack : throwingItems) {
                BehaviorUtils.throwItem(creature, itemstack, targetVector.add(0.0D, 1.0D, 0.0D));
            }
        }
    }

    public static void throwItems(PathfinderMob creature, List<ItemStack> throwingItems) {
        Optional<Player> optional = creature.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_PLAYER);
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