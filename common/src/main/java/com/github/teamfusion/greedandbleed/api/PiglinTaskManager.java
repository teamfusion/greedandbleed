package com.github.teamfusion.greedandbleed.api;

import com.github.teamfusion.greedandbleed.common.entity.piglin.GBPiglin;
import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Zoglin;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.item.*;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

import java.util.*;

public abstract class PiglinTaskManager<T extends AbstractPiglin & HasTaskManager> extends TaskManager<T> {
    protected static final Set<Item> FOOD_ITEMS = new HashSet<>(Arrays.asList(Items.PORKCHOP, Items.COOKED_PORKCHOP));
    protected static final Set<Item> POCKETED_ITEMS = new HashSet<>(Collections.singletonList(Items.GOLD_NUGGET));
    protected static final Set<Item> BARTERING_ITEMS = new HashSet<>(Collections.singletonList(Items.GOLD_INGOT));
    protected static final UniformInt TIME_BETWEEN_HUNTS = TimeUtil.rangeOfSeconds(30, 120);
    protected static final UniformInt RIDE_START_INTERVAL = TimeUtil.rangeOfSeconds(10, 40);
    protected static final UniformInt RIDE_DURATION = TimeUtil.rangeOfSeconds(10, 30);
    protected static final UniformInt RETREAT_DURATION = TimeUtil.rangeOfSeconds(5, 20);
    protected static final UniformInt AVOID_ZOMBIFIED_DURATION = TimeUtil.rangeOfSeconds(5, 7);
    protected static final UniformInt BABY_AVOID_NEMESIS_DURATION = TimeUtil.rangeOfSeconds(5, 7);

    public PiglinTaskManager(T mob, Brain<T> dynamicBrain) {
        super(mob, dynamicBrain);
    }

    @Override
    protected void initActivities() {
        this.initCoreActivity(0);
        this.initIdleActivity(10);
        this.initFightActivity(10);
    }

    // STATIC TASKS

    protected static BehaviorControl<LivingEntity> babySometimesRideBabyHoglin() {
        SetEntityLookTargetSometimes.Ticker ticker = new SetEntityLookTargetSometimes.Ticker(RIDE_START_INTERVAL);
        return CopyMemoryWithExpiry.create((livingEntity) -> {
            return livingEntity.isBaby() && ticker.tickDownAndCheck(livingEntity.level().random);
        }, MemoryModuleType.NEAREST_VISIBLE_BABY_HOGLIN, MemoryModuleType.RIDE_TARGET, RIDE_DURATION);
    }

    protected static BehaviorControl<PathfinderMob> avoidRepellent() {
        return SetWalkTargetAwayFrom.pos(MemoryModuleType.NEAREST_REPELLENT, 1.0F, 8, false);
    }

    protected static BehaviorControl<AbstractPiglin> babyAvoidNemesis() {
        return CopyMemoryWithExpiry.create(AbstractPiglin::isBaby, MemoryModuleType.NEAREST_VISIBLE_NEMESIS, MemoryModuleType.AVOID_TARGET, BABY_AVOID_NEMESIS_DURATION);
    }

    protected static BehaviorControl<AbstractPiglin> avoidZombified() {
        return CopyMemoryWithExpiry.create(PiglinTaskManager::isNearZombified, MemoryModuleType.NEAREST_VISIBLE_ZOMBIFIED, MemoryModuleType.AVOID_TARGET, AVOID_ZOMBIFIED_DURATION);
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

    // STATIC HELPER METHODS

    public static boolean isNearZombified(AbstractPiglin piglin) {
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
        if (targetIn instanceof Hoglin) {
            return false;
        } else {
            return new Random(livingEntity.level().getGameTime()).nextFloat() < 0.1F;
        }
    }

    public static boolean piglinsEqualOrOutnumberHoglins(AbstractPiglin piglin) {
        return !hoglinsOutnumberPiglins(piglin);
    }

    public static boolean hoglinsOutnumberPiglins(AbstractPiglin piglin) {
        int i = piglin.getBrain().getMemory(MemoryModuleType.VISIBLE_ADULT_PIGLIN_COUNT).orElse(0) + 1;
        int j = piglin.getBrain().getMemory(MemoryModuleType.VISIBLE_ADULT_HOGLIN_COUNT).orElse(0);
        return j > i;
    }

    public static void setAvoidTargetAndDontHuntForAWhile(AbstractPiglin piglin, LivingEntity targetIn) {
        piglin.getBrain().eraseMemory(MemoryModuleType.ANGRY_AT);
        piglin.getBrain().eraseMemory(MemoryModuleType.ATTACK_TARGET);
        piglin.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
        piglin.getBrain().setMemoryWithExpiry(MemoryModuleType.AVOID_TARGET, targetIn, RETREAT_DURATION.sample(piglin.level().random));
        setHuntedRecently(piglin, TIME_BETWEEN_HUNTS.sample(piglin.level().random));
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
                    || adultPiglin instanceof GBPiglin gbPiglin && gbPiglin.canHunt()
                    && hoglin.canBeHunted()) {
                setAngerTargetIfCloserThanCurrent(adultPiglin, targetIn);
            }
        });
    }

    public static void broadcastUniversalAnger(AbstractPiglin piglin) {
        getAdultPiglins(piglin).forEach((adultPiglin) -> getNearestVisibleTargetablePlayer(adultPiglin).ifPresent((player) -> setAngerTarget(adultPiglin, player)));
    }

    public static void setAngerTarget(AbstractPiglin piglin, LivingEntity targetIn) {
        if (isAttackAllowed(piglin, targetIn)) {
            piglin.getBrain().eraseMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
            piglin.getBrain().setMemoryWithExpiry(MemoryModuleType.ANGRY_AT, targetIn.getUUID(), 600L);
            if (targetIn instanceof Hoglin && piglin instanceof GBPiglin gbPiglin && gbPiglin.canHunt()) {
                setHuntedRecently(piglin, TIME_BETWEEN_HUNTS.sample(piglin.level().random));
            }

            if (targetIn instanceof Player && piglin.level().getGameRules().getBoolean(GameRules.RULE_UNIVERSAL_ANGER)) {
                piglin.getBrain().setMemoryWithExpiry(MemoryModuleType.UNIVERSAL_ANGER, true, 600L);
            }

        }
    }

    public static void setAngerTargetToNearestTargetablePlayerIfFound(AbstractPiglin piglin, LivingEntity targetIn) {
        Optional<Player> nearestPlayer = getNearestVisibleTargetablePlayer(piglin);
        if (nearestPlayer.isPresent()) {
            setAngerTarget(piglin, nearestPlayer.get());
        } else {
            setAngerTarget(piglin, targetIn);
        }

    }

    public static void setAngerTargetIfCloserThanCurrent(AbstractPiglin piglin, LivingEntity targetIn) {
        Optional<LivingEntity> angerTarget = getAngerTarget(piglin);
        LivingEntity nearestTarget = BehaviorUtils.getNearestTarget(piglin, angerTarget, targetIn);
        if (angerTarget.isEmpty() || angerTarget.get() != nearestTarget) {
            setAngerTarget(piglin, nearestTarget);
        }
    }

    public static void retreatFromNearestTarget(AbstractPiglin piglin, LivingEntity targetIn) {
        Brain<?> brain = piglin.getBrain();
        LivingEntity nearestTarget = BehaviorUtils.getNearestTarget(piglin, brain.getMemory(MemoryModuleType.AVOID_TARGET), targetIn);
        nearestTarget = BehaviorUtils.getNearestTarget(piglin, brain.getMemory(MemoryModuleType.ATTACK_TARGET), nearestTarget);
        setAvoidTargetAndDontHuntForAWhile(piglin, nearestTarget);
    }

    public static boolean isNotHoldingLovedItemInOffHand(AbstractPiglin piglin) {
        return piglin.getOffhandItem().isEmpty() || !isLovedItem(piglin.getOffhandItem().getItem());
    }

    public static boolean canPiglinAdmire(AbstractPiglin piglin, ItemStack stack) {
        return !isAdmiringDisabled(piglin) && !isAdmiringItem(piglin) && piglin.isAdult() && isBarteringItem(stack);
    }

    public static void cancelAdmiring(AbstractPiglin piglin) {
        if (isAdmiringItem(piglin) && !piglin.getOffhandItem().isEmpty()) {
            piglin.spawnAtLocation(piglin.getOffhandItem());
            piglin.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
        }
    }

    public static void dontKillAnyMoreHoglinsForAWhile(AbstractPiglin piglin) {
        setHuntedRecently(piglin, TIME_BETWEEN_HUNTS.sample(piglin.level().random));
    }

    public static void broadcastDontKillAnyMoreHoglinsForAWhile(AbstractPiglin piglin) {
        getVisibleAdultPiglins(piglin)
                .forEach(PiglinTaskManager::dontKillAnyMoreHoglinsForAWhile);
    }

    public static boolean hasAnyoneNearbyHuntedRecently(AbstractPiglin piglin) {
        return piglin.getBrain().hasMemoryValue(MemoryModuleType.HUNTED_RECENTLY)
                || getVisibleAdultPiglins(piglin)
                .stream()
                .anyMatch(
                        (adultPiglin) -> adultPiglin.getBrain().hasMemoryValue(MemoryModuleType.HUNTED_RECENTLY)
                );
    }

    public static <T extends AbstractPiglin & HasTaskManager & HasInventory> void stopHoldingOffHandItem(T piglin, boolean doBarter) {
        ItemStack itemstack = piglin.getItemInHand(InteractionHand.OFF_HAND);
        piglin.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
        if (piglin.isAdult()) {
            boolean isPiglinBarter = isBarteringItem(itemstack);
            if (doBarter && isPiglinBarter) {
                throwItems(piglin, PiglinTaskManager.getBarterResponseItems(piglin));
            } else if (!isPiglinBarter) {
                ItemStack equipItem = piglin.equipItemIfPossible(itemstack);
                if (!equipItem.isEmpty()) {
                    putInInventory(piglin, itemstack);
                }
            }
        } else {
            ItemStack equipItem = piglin.equipItemIfPossible(itemstack);
            if (!equipItem.isEmpty()) {
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

    public static List<ItemStack> getBarterResponseItems(AbstractPiglin piglin) {
        MinecraftServer server = piglin.level().getServer();
        if (server != null) {
            LootTable loottable = server.getLootData().getLootTable(BuiltInLootTables.PIGLIN_BARTERING);
            return loottable.getRandomItems(new LootParams.Builder((ServerLevel) piglin.level()).withParameter(LootContextParams.THIS_ENTITY, piglin).create(LootContextParamSets.PIGLIN_BARTER));
        }

        return new ArrayList<>();
    }

    public static <T extends AbstractPiglin & HasInventory & HasTaskManager> void pickUpItem(T piglin, ItemEntity itemEntity) {
        stopWalking(piglin);
        ItemStack itemstack;
        if (isPocketedItem(itemEntity.getItem())) {
            piglin.take(itemEntity, itemEntity.getItem().getCount());
            itemstack = itemEntity.getItem();
            itemEntity.discard();
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
            ItemStack equipItem = piglin.equipItemIfPossible(itemstack);
            if (!equipItem.isEmpty()) {
                putInInventory(piglin, itemstack);
            }
        }
    }

    public static <T extends AbstractPiglin & HasInventory & HasTaskManager> void performBarter(T piglin) {
        PiglinTaskManager.stopHoldingOffHandItem(piglin, true);
    }

    public static boolean wantsToStopFleeing(AbstractPiglin piglinEntity) {
        Brain<?> brain = piglinEntity.getBrain();
        if (!brain.hasMemoryValue(MemoryModuleType.AVOID_TARGET)) {
            return true;
        } else {
            Optional<LivingEntity> avoidTargetOpt = brain.getMemory(MemoryModuleType.AVOID_TARGET);
            if (avoidTargetOpt.isPresent()) {
                LivingEntity avoidTarget = avoidTargetOpt.get();
                if (avoidTarget instanceof Hoglin) {
                    return piglinsEqualOrOutnumberHoglins(piglinEntity);
                } else if (isZombified(avoidTarget)) {
                    return !brain.isMemoryValue(MemoryModuleType.NEAREST_VISIBLE_ZOMBIFIED, avoidTarget);
                }
            }
        }

        return false;
    }

    public static boolean wantsToStopRiding(AbstractPiglin piglin, Entity vehicle) {
        if (!(vehicle instanceof Mob mob)) {
            return false;
        } else {
            return !mob.isBaby() || !mob.isAlive() || wasHurtRecently(piglin) || wasHurtRecently(mob) || mob instanceof AbstractPiglin && mob.getVehicle() == null;
        }
    }

    public static boolean isPiglinLoved(ItemEntity itemEntity){
        return isLovedItem(itemEntity.getItem());
    }

    public static boolean isPlayerHoldingLovedItem(LivingEntity livingEntity) {
        return livingEntity instanceof Player && livingEntity.isHolding(PiglinTaskManager::isLovedItem);
    }

    public static boolean isWearingGold(LivingEntity livingEntity) {
        for(ItemStack itemstack : livingEntity.getArmorSlots()) {
            if (piglinsTolerate(itemstack)) {
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

    public static <T extends AbstractPiglin & HasInventory> boolean wantsToPickup(T piglin, ItemStack stack) {
        if (stack.is(ItemTags.PIGLIN_REPELLENTS)) {
            return false;
        } else if (isAdmiringDisabled(piglin) && piglin.getBrain().hasMemoryValue(MemoryModuleType.ATTACK_TARGET)) {
            return false;
        } else if (stack.is(Items.GOLD_INGOT)) {
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

    public static boolean canReplaceCurrentItem(Mob mob, ItemStack replacementItem) {
        EquipmentSlot equipmentslot = Mob.getEquipmentSlotForItem(replacementItem);
        ItemStack currentItem = mob.getItemBySlot(equipmentslot);
        return mob instanceof GBPiglin piglin && piglin.canReplaceCurrentItem(replacementItem, currentItem);
    }

    public static boolean isZombified(LivingEntity livingEntity) {
        return livingEntity instanceof ZombifiedPiglin || livingEntity instanceof Zoglin;
    }

    public static boolean isFood(ItemStack stack) {
        return FOOD_ITEMS.contains(stack.getItem());
    }

    public static boolean isPocketedItem(ItemStack stack) {
        return POCKETED_ITEMS.contains(stack.getItem());
    }

    public static boolean isBarteringItem(ItemStack stack) {
        return BARTERING_ITEMS.contains(stack.getItem()) || stack.is(Items.GOLD_INGOT);
    }

    public static boolean isLovedItem(ItemStack stack) {
        return isLovedItem(stack.getItem());
    }

    public static boolean isLovedItem(Item item) {
        return item.getDefaultInstance().is(ItemTags.PIGLIN_LOVED);
    }

    public static boolean piglinsTolerate(ItemStack stack) {
        return stack.getItem() instanceof ArmorItem armor && armor.getMaterial() == ArmorMaterials.GOLD;
    }
}