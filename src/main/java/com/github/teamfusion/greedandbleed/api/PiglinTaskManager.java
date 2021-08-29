package com.github.teamfusion.greedandbleed.api;

import com.github.teamfusion.greedandbleed.mixin.AbstractPiglinEntityInvoker;
import com.github.teamfusion.greedandbleed.mixin.MobEntityInvoker;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.BrainUtil;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.schedule.Activity;
import net.minecraft.entity.ai.brain.task.PiglinIdleActivityTask;
import net.minecraft.entity.ai.brain.task.RunAwayTask;
import net.minecraft.entity.ai.brain.task.RunSometimesTask;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.HoglinEntity;
import net.minecraft.entity.monster.ZoglinEntity;
import net.minecraft.entity.monster.ZombifiedPiglinEntity;
import net.minecraft.entity.monster.piglin.AbstractPiglinEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Hand;
import net.minecraft.util.RangedInteger;
import net.minecraft.util.TickRangeConverter;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.server.ServerWorld;

import java.util.*;

/***
 * An extensible class for initializing and handling a Brain for an AbstractPiglinEntity.
 * This was based on the class PiglinTasks.
 * @author Thelnfamous1
 * @param <T> A class that extends AbstractPiglinEntity
 */
@SuppressWarnings("unused")
public abstract class PiglinTaskManager<T extends AbstractPiglinEntity & IHasTaskManager> extends TaskManager<T> {

    protected static final Set<Item> FOOD_ITEMS = new HashSet<>(Arrays.asList(Items.PORKCHOP, Items.COOKED_PORKCHOP));
    protected static final Set<Item> POCKETED_ITEMS = new HashSet<>(Collections.singletonList(Items.GOLD_NUGGET));
    protected static final Set<Item> BARTERING_ITEMS = new HashSet<>(Collections.singletonList(Items.GOLD_INGOT));
    protected static final RangedInteger TIME_BETWEEN_HUNTS = TickRangeConverter.rangeOfSeconds(30, 120);
    protected static final RangedInteger RIDE_START_INTERVAL = TickRangeConverter.rangeOfSeconds(10, 40);
    protected static final RangedInteger RIDE_DURATION = TickRangeConverter.rangeOfSeconds(10, 30);
    protected static final RangedInteger RETREAT_DURATION = TickRangeConverter.rangeOfSeconds(5, 20);
    protected static final RangedInteger AVOID_ZOMBIFIED_DURATION = TickRangeConverter.rangeOfSeconds(5, 7);
    protected static final RangedInteger BABY_AVOID_NEMESIS_DURATION = TickRangeConverter.rangeOfSeconds(5, 7);

    public PiglinTaskManager(T piglin, Brain<T> dynamicBrain) {
        super(piglin, dynamicBrain);
    }

    @Override
    protected void initActivities() {
        this.initCoreActivity(0);
        this.initIdleActivity(10);
        this.initFightActivity(10);
    }

    // STATIC TASKS

    protected static RunSometimesTask<AbstractPiglinEntity> babySometimesRideBabyHoglin() {
        return new RunSometimesTask<>(new PiglinIdleActivityTask<>(AbstractPiglinEntity::isBaby, MemoryModuleType.NEAREST_VISIBLE_BABY_HOGLIN, MemoryModuleType.RIDE_TARGET, RIDE_DURATION), RIDE_START_INTERVAL);
    }

    protected static RunAwayTask<BlockPos> avoidRepellent() {
        return RunAwayTask.pos(MemoryModuleType.NEAREST_REPELLENT, 1.0F, 8, false);
    }

    protected static PiglinIdleActivityTask<AbstractPiglinEntity, LivingEntity> babyAvoidNemesis() {
        return new PiglinIdleActivityTask<>(AbstractPiglinEntity::isBaby, MemoryModuleType.NEAREST_VISIBLE_NEMESIS, MemoryModuleType.AVOID_TARGET, BABY_AVOID_NEMESIS_DURATION);
    }

    protected static PiglinIdleActivityTask<AbstractPiglinEntity, LivingEntity> avoidZombified() {
        return new PiglinIdleActivityTask<>(PiglinTaskManager::isNearZombified, MemoryModuleType.NEAREST_VISIBLE_ZOMBIFIED, MemoryModuleType.AVOID_TARGET, AVOID_ZOMBIFIED_DURATION);
    }

    // STATIC HELPER METHODS

    public static boolean isNearZombified(AbstractPiglinEntity piglin) {
        Brain<?> brain = piglin.getBrain();
        if (brain.hasMemoryValue(MemoryModuleType.NEAREST_VISIBLE_ZOMBIFIED)) {
            Optional<LivingEntity> entity = brain.getMemory(MemoryModuleType.NEAREST_VISIBLE_ZOMBIFIED);
            if (entity.isPresent()) {
                return piglin.closerThan(entity.get(), 6.0D);
            }
        }

        return false;
    }

    public static boolean wantsToDance(LivingEntity livingEntity, LivingEntity targetIn) {
        if (targetIn instanceof HoglinEntity) {
            return false;
        } else {
            return (new Random(livingEntity.level.getGameTime())).nextFloat() < 0.1F;
        }
    }

    public static boolean piglinsEqualOrOutnumberHoglins(AbstractPiglinEntity piglin) {
        return !hoglinsOutnumberPiglins(piglin);
    }

    public static boolean hoglinsOutnumberPiglins(AbstractPiglinEntity piglin) {
        int i = piglin.getBrain().getMemory(MemoryModuleType.VISIBLE_ADULT_PIGLIN_COUNT).orElse(0) + 1;
        int j = piglin.getBrain().getMemory(MemoryModuleType.VISIBLE_ADULT_HOGLIN_COUNT).orElse(0);
        return j > i;
    }

    public static void setAvoidTargetAndDontHuntForAWhile(AbstractPiglinEntity piglin, LivingEntity targetIn) {
        piglin.getBrain().eraseMemory(MemoryModuleType.ANGRY_AT);
        piglin.getBrain().eraseMemory(MemoryModuleType.ATTACK_TARGET);
        piglin.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
        piglin.getBrain().setMemoryWithExpiry(MemoryModuleType.AVOID_TARGET, targetIn, RETREAT_DURATION.randomValue(piglin.level.random));
        setHuntedRecently(piglin, TIME_BETWEEN_HUNTS.randomValue(piglin.level.random));
    }

    public static void maybeRetaliate(AbstractPiglinEntity piglin, LivingEntity targetIn) {
        if (!piglin.getBrain().isActive(Activity.AVOID)) {
            if (isAttackAllowed(targetIn)) {
                if (!BrainUtil.isOtherTargetMuchFurtherAwayThanCurrentAttackTarget(piglin, targetIn, 4.0D)) {
                    if (targetIn instanceof PlayerEntity && piglin.level.getGameRules().getBoolean(GameRules.RULE_UNIVERSAL_ANGER)) {
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

    public static void broadcastAngerTarget(AbstractPiglinEntity piglin, LivingEntity targetIn) {
        getAdultPiglins(piglin).forEach((adultPiglin) -> {
            if (!(targetIn instanceof HoglinEntity)
                    || ((AbstractPiglinEntityInvoker) adultPiglin).canHunt()
                    && ((HoglinEntity) targetIn).canBeHunted()) {
                setAngerTargetIfCloserThanCurrent(adultPiglin, targetIn);
            }
        });
    }

    public static void broadcastUniversalAnger(AbstractPiglinEntity piglin) {
        getAdultPiglins(piglin).forEach((adultPiglin) -> getNearestVisibleTargetablePlayer(adultPiglin).ifPresent((player) -> setAngerTarget(adultPiglin, player)));
    }

    public static void setAngerTarget(AbstractPiglinEntity piglin, LivingEntity targetIn) {
        if (isAttackAllowed(targetIn)) {
            piglin.getBrain().eraseMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
            piglin.getBrain().setMemoryWithExpiry(MemoryModuleType.ANGRY_AT, targetIn.getUUID(), 600L);
            if (targetIn instanceof HoglinEntity && ((AbstractPiglinEntityInvoker) piglin).canHunt()) {
                setHuntedRecently(piglin, TIME_BETWEEN_HUNTS.randomValue(piglin.level.random));
            }

            if (targetIn instanceof PlayerEntity && piglin.level.getGameRules().getBoolean(GameRules.RULE_UNIVERSAL_ANGER)) {
                piglin.getBrain().setMemoryWithExpiry(MemoryModuleType.UNIVERSAL_ANGER, true, 600L);
            }

        }
    }

    public static void setAngerTargetToNearestTargetablePlayerIfFound(AbstractPiglinEntity piglin, LivingEntity targetIn) {
        Optional<PlayerEntity> nearestPlayer = getNearestVisibleTargetablePlayer(piglin);
        if (nearestPlayer.isPresent()) {
            setAngerTarget(piglin, nearestPlayer.get());
        } else {
            setAngerTarget(piglin, targetIn);
        }

    }

    public static void setAngerTargetIfCloserThanCurrent(AbstractPiglinEntity piglin, LivingEntity targetIn) {
        Optional<LivingEntity> angerTarget = getAngerTarget(piglin);
        LivingEntity nearestTarget = BrainUtil.getNearestTarget(piglin, angerTarget, targetIn);
        if (!angerTarget.isPresent() || angerTarget.get() != nearestTarget) {
            setAngerTarget(piglin, nearestTarget);
        }
    }

    public static void retreatFromNearestTarget(AbstractPiglinEntity piglin, LivingEntity targetIn) {
        Brain<?> brain = piglin.getBrain();
        LivingEntity nearestTarget = BrainUtil.getNearestTarget(piglin, brain.getMemory(MemoryModuleType.AVOID_TARGET), targetIn);
        nearestTarget = BrainUtil.getNearestTarget(piglin, brain.getMemory(MemoryModuleType.ATTACK_TARGET), nearestTarget);
        setAvoidTargetAndDontHuntForAWhile(piglin, nearestTarget);
    }

    public static boolean isNotHoldingLovedItemInOffHand(AbstractPiglinEntity piglin) {
        return piglin.getOffhandItem().isEmpty() || !isLovedItem(piglin.getOffhandItem().getItem());
    }

    public static boolean canPiglinAdmire(AbstractPiglinEntity piglin, ItemStack stack) {
        return !isAdmiringDisabled(piglin) && !isAdmiringItem(piglin) && piglin.isAdult() && isBarteringItem(stack);
    }

    public static void cancelAdmiring(AbstractPiglinEntity piglin) {
        if (isAdmiringItem(piglin) && !piglin.getOffhandItem().isEmpty()) {
            piglin.spawnAtLocation(piglin.getOffhandItem());
            piglin.setItemInHand(Hand.OFF_HAND, ItemStack.EMPTY);
        }
    }

    public static void dontKillAnyMoreHoglinsForAWhile(AbstractPiglinEntity piglin) {
        setHuntedRecently(piglin, TIME_BETWEEN_HUNTS.randomValue(piglin.level.random));
    }

    public static void broadcastDontKillAnyMoreHoglinsForAWhile(AbstractPiglinEntity piglin) {
        getVisibleAdultPiglins(piglin)
                .forEach(PiglinTaskManager::dontKillAnyMoreHoglinsForAWhile);
    }

    public static boolean hasAnyoneNearbyHuntedRecently(AbstractPiglinEntity piglin) {
        return piglin.getBrain().hasMemoryValue(MemoryModuleType.HUNTED_RECENTLY)
                || getVisibleAdultPiglins(piglin)
                .stream()
                .anyMatch(
                        (adultPiglin) -> adultPiglin.getBrain().hasMemoryValue(MemoryModuleType.HUNTED_RECENTLY)
                );
    }

    public static <T extends AbstractPiglinEntity & IHasTaskManager & IHasInventory> void stopHoldingOffHandItem(T piglin, boolean doBarter) {
        ItemStack itemstack = piglin.getItemInHand(Hand.OFF_HAND);
        piglin.setItemInHand(Hand.OFF_HAND, ItemStack.EMPTY);
        if (piglin.isAdult()) {
            boolean isPiglinBarter = isBarteringItem(itemstack);
            if (doBarter && isPiglinBarter) {
                throwItems(piglin, PiglinTaskManager.getBarterResponseItems(piglin));
            } else if (!isPiglinBarter) {
                boolean didEquip = piglin.equipItemIfPossible(itemstack);
                if (!didEquip) {
                    putInInventory(piglin, itemstack);
                }
            }
        } else {
            boolean didEquip = piglin.equipItemIfPossible(itemstack);
            if (!didEquip) {
                ItemStack mainHandItem = piglin.getMainHandItem();
                if (isLovedItem(mainHandItem.getItem())) {
                    putInInventory(piglin, mainHandItem);
                } else {
                    throwItems(piglin, Collections.singletonList(mainHandItem));
                }

                piglin.holdInMainHand(itemstack);
            }
        }
    }

    public static List<ItemStack> getBarterResponseItems(AbstractPiglinEntity piglin) {
        MinecraftServer server = piglin.level.getServer();
        if (server != null) {
            LootTable loottable = server.getLootTables().get(LootTables.PIGLIN_BARTERING);
            return loottable.getRandomItems(new LootContext.Builder((ServerWorld) piglin.level).withParameter(LootParameters.THIS_ENTITY, piglin).withRandom(piglin.level.random).create(LootParameterSets.PIGLIN_BARTER));
        }

        return new ArrayList<>();
    }

    public static <T extends AbstractPiglinEntity & IHasInventory & IHasTaskManager>void pickUpItem(T piglin, ItemEntity itemEntity) {
        stopWalking(piglin);
        ItemStack itemstack;
        if (isPocketedItem(itemEntity.getItem())) {
            piglin.take(itemEntity, itemEntity.getItem().getCount());
            itemstack = itemEntity.getItem();
            itemEntity.remove();
        } else {
            piglin.take(itemEntity, 1);
            itemstack = removeOneItemFromItemEntity(itemEntity);
        }

        Item item = itemstack.getItem();
        if (isLovedItem(item)) {
            piglin.getBrain().eraseMemory(MemoryModuleType.TIME_TRYING_TO_REACH_ADMIRE_ITEM);
            holdInOffhand(piglin, itemstack);
            admireItem(piglin, 120L);
        } else if (isFood(itemstack) && !hasEatenRecently(piglin)) {
            eat(piglin, 200L);
        } else {
            boolean flag = piglin.equipItemIfPossible(itemstack);
            if (!flag) {
                putInInventory(piglin, itemstack);
            }
        }
    }

    public static <T extends AbstractPiglinEntity & IHasInventory & IHasTaskManager> void performBarter(T piglin) {
        PiglinTaskManager.stopHoldingOffHandItem(piglin, true);
    }

    public static boolean wantsToStopFleeing(AbstractPiglinEntity piglinEntity) {
        Brain<?> brain = piglinEntity.getBrain();
        if (!brain.hasMemoryValue(MemoryModuleType.AVOID_TARGET)) {
            return true;
        } else {
            Optional<LivingEntity> avoidTargetOpt = brain.getMemory(MemoryModuleType.AVOID_TARGET);
            if (avoidTargetOpt.isPresent()) {
                LivingEntity avoidTarget = avoidTargetOpt.get();
                if (avoidTarget instanceof HoglinEntity) {
                    return piglinsEqualOrOutnumberHoglins(piglinEntity);
                } else if (isZombified(avoidTarget)) {
                    return !brain.isMemoryValue(MemoryModuleType.NEAREST_VISIBLE_ZOMBIFIED, avoidTarget);
                }
            }
        }

        return false;
    }

    public static boolean wantsToStopRiding(AbstractPiglinEntity piglin, Entity vehicle) {
        if (!(vehicle instanceof MobEntity)) {
            return false;
        } else {
            MobEntity mobVehicle = (MobEntity)vehicle;
            return !mobVehicle.isBaby() || !mobVehicle.isAlive() || wasHurtRecently(piglin) || wasHurtRecently(mobVehicle) || mobVehicle instanceof AbstractPiglinEntity && mobVehicle.getVehicle() == null;
        }
    }

    public static boolean isPiglinLoved(ItemEntity itemEntity){
        return isLovedItem(itemEntity.getItem());
    }

    public static boolean isPlayerHoldingLovedItem(LivingEntity livingEntity) {
        return livingEntity instanceof PlayerEntity && livingEntity.isHolding(PiglinTaskManager::isLovedItem);
    }

    public static boolean isWearingGold(LivingEntity livingEntity) {
        for(ItemStack itemstack : livingEntity.getArmorSlots()) {
            if (piglinsTolerate(itemstack, livingEntity)) {
                return true;
            }
        }

        return false;
    }

    public static boolean isNearAvoidTarget(LivingEntity livingEntity) {
        Brain<?> brain = livingEntity.getBrain();
        Optional<LivingEntity> avoidTarget = brain.getMemory(MemoryModuleType.AVOID_TARGET);
        return avoidTarget.isPresent() && avoidTarget.get().closerThan(livingEntity, 12.0D);
    }

    public static <T extends AbstractPiglinEntity & IHasInventory> boolean wantsToPickup(T piglin, ItemStack stack) {
        Item item = stack.getItem();
        if (item.is(ItemTags.PIGLIN_REPELLENTS)) {
            return false;
        } else if (isAdmiringDisabled(piglin) && piglin.getBrain().hasMemoryValue(MemoryModuleType.ATTACK_TARGET)) {
            return false;
        } else if (stack.isPiglinCurrency()) {
            return isNotHoldingLovedItemInOffHand(piglin);
        } else {
            boolean canAddToInventory = piglin.canAddToInventory(stack);
            if (isPocketedItem(stack)) {
                return canAddToInventory;
            } else if (isFood(stack)) {
                return !hasEatenRecently(piglin) && canAddToInventory;
            } else if (!isLovedItem(stack)) {
                return canReplaceCurrentItem(piglin, stack);
            } else {
                return isNotHoldingLovedItemInOffHand(piglin) && canAddToInventory;
            }
        }
    }

    public static boolean canReplaceCurrentItem(MobEntity mob, ItemStack replacementItem) {
        EquipmentSlotType equipmentslottype = MobEntity.getEquipmentSlotForItem(replacementItem);
        ItemStack currentItem = mob.getItemBySlot(equipmentslottype);
        return ((MobEntityInvoker) mob).canReplaceCurrentItem(replacementItem, currentItem);
    }

    public static boolean isZombified(LivingEntity livingEntity) {
        return livingEntity instanceof ZombifiedPiglinEntity || livingEntity instanceof ZoglinEntity;
    }

    public static boolean isFood(ItemStack stack) {
        return FOOD_ITEMS.contains(stack.getItem());
    }

    public static boolean isPocketedItem(ItemStack stack) {
        return POCKETED_ITEMS.contains(stack.getItem());
    }

    public static boolean isBarteringItem(ItemStack stack) {
        return BARTERING_ITEMS.contains(stack.getItem()) || stack.isPiglinCurrency();
    }

    public static boolean isLovedItem(ItemStack stack) {
        return isLovedItem(stack.getItem());
    }

    public static boolean isLovedItem(Item item) {
        return item.is(ItemTags.PIGLIN_LOVED);
    }

    public static boolean piglinsTolerate(ItemStack stack, LivingEntity livingEntity){
        return stack.getItem().makesPiglinsNeutral(stack, livingEntity);
    }
}
