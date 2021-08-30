package com.github.teamfusion.greedandbleed.common.entity.piglin;

import com.github.teamfusion.greedandbleed.api.IHasInventory;
import com.github.teamfusion.greedandbleed.common.entity.piglin.PigmyTaskManager;
import com.github.teamfusion.greedandbleed.api.TaskManager;
import com.github.teamfusion.greedandbleed.common.registry.ItemRegistry;
import com.github.teamfusion.greedandbleed.server.registry.MemoryModuleTypeRegistry;
import com.github.teamfusion.greedandbleed.server.registry.SensorTypeRegistry;
import com.google.common.collect.Lists;
import com.mojang.serialization.Dynamic;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.ai.brain.sensor.SensorType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.HoglinEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.piglin.AbstractPiglinEntity;
import net.minecraft.entity.monster.piglin.PiglinAction;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.*;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

@SuppressWarnings({ "NullableProblems", "unused" })
public class PigmyEntity extends GBPiglinEntity implements ICrossbowUser, IHasInventory {
    protected Inventory inventory = new Inventory(8);

    public PigmyEntity(EntityType<? extends AbstractPiglinEntity> entityType, World world) {
        super(entityType, world);
    }

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return MonsterEntity.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 16.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.35F)
                .add(Attributes.ATTACK_DAMAGE, 5.0D);
    }

    public static boolean checkPygmySpawnRules(EntityType<PigmyEntity> pygmy, IWorld blockState, SpawnReason spawnReason, BlockPos blockPos, Random random) {
        return !blockState.getBlockState(blockPos.below()).is(Blocks.NETHER_WART_BLOCK);
    }

    @Override
    public ILivingEntityData finalizeSpawn(IServerWorld serverWorld, DifficultyInstance difficultyInstance, SpawnReason spawnReason, @Nullable ILivingEntityData entityData, @Nullable CompoundNBT compoundNBT) {
        if (spawnReason != SpawnReason.STRUCTURE) {
            if (serverWorld.getRandom().nextFloat() < 0.2F) {
                this.setBaby(true);
            } else if (this.isAdult()) {
                boolean isHoglinJockey = this.createHoglinJockey(serverWorld, difficultyInstance);
                this.setItemSlot(EquipmentSlotType.MAINHAND,
                        isHoglinJockey ?
                                new ItemStack(ItemRegistry.CRIMSON_FUNGUS_ON_A_STICK.get()) :
                                this.createSpawnWeapon());
            }
        }

        this.taskManager.initMemories();
        this.populateDefaultEquipmentSlots(difficultyInstance);
        this.populateDefaultEquipmentEnchantments(difficultyInstance);

        return super.finalizeSpawn(serverWorld, difficultyInstance, spawnReason, entityData, compoundNBT);
    }

    private boolean createHoglinJockey(IServerWorld serverWorld, DifficultyInstance difficultyInstance) {
        if ((double) serverWorld.getRandom().nextFloat() < 0.05D) {
            Predicate<HoglinEntity> isRideableHoglin =
                    (hoglin) -> hoglin.isAlive()
                            && hoglin.isAdult()
                            && !hoglin.isVehicle()
                            && !hoglin.isPassenger();
            List<HoglinEntity> nearbyAdultHoglins = serverWorld.getEntitiesOfClass(HoglinEntity.class, this.getBoundingBox().inflate(5.0D, 3.0D, 5.0D),
                    isRideableHoglin);
            for(HoglinEntity hoglin : nearbyAdultHoglins){
                //hoglin.setChickenJockey(true);
                if(hoglin instanceof IEquipable
                        && ((IEquipable)hoglin).isSaddled()){
                    this.startRiding(hoglin);
                    return true;
                }
            }
        } else if ((double) serverWorld.getRandom().nextFloat() < 0.05D) {
            HoglinEntity hoglin = EntityType.HOGLIN.create(this.level);
            if (hoglin != null) {
                hoglin.moveTo(this.getX(), this.getY(), this.getZ(), this.yRot, 0.0F);
                hoglin.finalizeSpawn(serverWorld, difficultyInstance, SpawnReason.JOCKEY, null, null);
                hoglin.setBaby(false);
                //hoglin.setChickenJockey(true);
                if (hoglin instanceof IEquipable && ((IEquipable) hoglin).isSaddleable()) {
                    ((IEquipable) hoglin).equipSaddle(null);
                    this.startRiding(hoglin);
                    serverWorld.addFreshEntity(hoglin);
                    return true;
                }
            }
        }
        return false;
    }

    private ItemStack createSpawnWeapon() {
        return (double)this.random.nextFloat() < 0.5D ? new ItemStack(Items.CROSSBOW) : new ItemStack(Items.GOLDEN_SWORD);
    }

    @Override
    protected void populateDefaultEquipmentSlots(DifficultyInstance difficultyInstance) {
        if (this.isAdult()) {
            this.maybeWearArmor(EquipmentSlotType.HEAD, new ItemStack(Items.GOLDEN_HELMET));
            this.maybeWearArmor(EquipmentSlotType.CHEST, new ItemStack(Items.GOLDEN_CHESTPLATE));
            this.maybeWearArmor(EquipmentSlotType.LEGS, new ItemStack(Items.GOLDEN_LEGGINGS));
            this.maybeWearArmor(EquipmentSlotType.FEET, new ItemStack(Items.GOLDEN_BOOTS));
        }

    }

    private void maybeWearArmor(EquipmentSlotType slotType, ItemStack stack) {
        if (this.level.random.nextFloat() < 0.1F) {
            this.setItemSlot(slotType, stack);
        }

    }

    @Override
    public ActionResultType mobInteract(PlayerEntity player, Hand hand) {
        ActionResultType actionresulttype = super.mobInteract(player, hand);
        if (actionresulttype.consumesAction()) {
            return actionresulttype;
        } else if (!this.level.isClientSide) {
            return this.taskManager.mobInteract(player, hand);
        } else {
			boolean canAdmire = PigmyTaskManager.canPiglinAdmire(this, player.getItemInHand(hand)) && this.getArmPose() != PiglinAction.ADMIRING_ITEM;
            return canAdmire ? ActionResultType.SUCCESS : ActionResultType.PASS;
        }
    }

    @Override
    public PiglinAction getArmPose() {
		if (this.isDancing()) {
			return PiglinAction.DANCING;
		} else if (PigmyTaskManager.isLovedItem(this.getOffhandItem().getItem())) {
			return PiglinAction.ADMIRING_ITEM;
		} else if (this.isAggressive() && this.isHoldingMeleeWeapon()) {
			return PiglinAction.ATTACKING_WITH_MELEE_WEAPON;
		} else if (this.isChargingCrossbow()) {
			return PiglinAction.CROSSBOW_CHARGE;
		} else {
			return this.isAggressive() && this.isHolding(item -> item instanceof CrossbowItem) ? PiglinAction.CROSSBOW_HOLD : PiglinAction.DEFAULT;
		}
    }

    @Override
    public boolean wantsToPickUp(ItemStack stack) {
		return this.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)
				&& this.canPickUpLoot()
				&& PigmyTaskManager.wantsToPickup(this, stack);
    }

    @Override
    protected void pickUpItem(ItemEntity itemEntity) {
		this.onItemPickup(itemEntity);
		PigmyTaskManager.pickUpItem(this, itemEntity);
    }

    @Override
    protected boolean canReplaceCurrentItem(ItemStack replacementItem, ItemStack currentItem) {
        if (EnchantmentHelper.hasBindingCurse(currentItem)) {
            return false;
        } else {
			boolean desirableReplacement = PigmyTaskManager.isLovedItem(replacementItem.getItem()) || replacementItem.getItem() instanceof CrossbowItem;
			boolean desirableCurrent = PigmyTaskManager.isLovedItem(currentItem.getItem()) || currentItem.getItem() instanceof CrossbowItem;
			if (desirableReplacement && !desirableCurrent) {
				return true;
			} else if (!desirableReplacement && desirableCurrent) {
				return false;
			} else {
				boolean notReplacingCrossbow = !(replacementItem.getItem() instanceof CrossbowItem) && currentItem.getItem() instanceof CrossbowItem;
				return (!this.isAdult() || !notReplacingCrossbow)
						&& super.canReplaceCurrentItem(replacementItem, currentItem);
			}
		}
    }

    @Override
    protected void finishConversion(ServerWorld serverWorld) {
		PigmyTaskManager.cancelAdmiring(this);
		this.removeAllItemsFromInventory(this::spawnAtLocation);
        super.finishConversion(serverWorld);
    }

    @Override
    protected void dropCustomDeathLoot(DamageSource damageSource, int p_213333_2_, boolean p_213333_3_) {
        super.dropCustomDeathLoot(damageSource, p_213333_2_, p_213333_3_);
        this.removeAllItemsFromInventory(this::spawnAtLocation);
    }

    // BRAIN
    @Override
    protected void customServerAiStep() {
        this.level.getProfiler().push("piglinPygmyBrain");
        this.getBrain().tick((ServerWorld)this.level, this);
        this.level.getProfiler().pop();
        this.taskManager.updateActivity();
        super.customServerAiStep();
    }

    @Override
    protected Brain.BrainCodec<PigmyEntity> brainProvider() {
        return Brain.provider(PYGMY_MEMORY_TYPES, PYGMY_SENSOR_TYPES);
    }

    @Override
    protected Brain<?> makeBrain(Dynamic<?> dynamic) {
        this.taskManager = this.createTaskManager(dynamic);
        return this.taskManager.getBrain();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Brain<PigmyEntity> getBrain() {
        return (Brain<PigmyEntity>) super.getBrain();
    }

    // SAVE DATA

    @Override
    public void addAdditionalSaveData(CompoundNBT compoundNBT) {
        super.addAdditionalSaveData(compoundNBT);
        if (this.isBaby()) {
            compoundNBT.putBoolean("IsBaby", true);
        }

        if (this.cannotHunt) {
            compoundNBT.putBoolean("CannotHunt", true);
        }
        this.saveInventoryToNBT(compoundNBT);

    }

    @Override
    public void readAdditionalSaveData(CompoundNBT compoundNBT) {
        super.readAdditionalSaveData(compoundNBT);
        this.setBaby(compoundNBT.getBoolean("IsBaby"));
        this.setCannotHunt(compoundNBT.getBoolean("CannotHunt"));
        this.loadInventoryFromNBT(compoundNBT);
    }

    // ENTITY DATA

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_BABY_ID, false);
        this.entityData.define(DATA_IS_CHARGING_CROSSBOW, false);
        this.entityData.define(DATA_IS_DANCING, false);
    }

    @Override
    public void onSyncedDataUpdated(DataParameter<?> dataParameter) {
        super.onSyncedDataUpdated(dataParameter);
        if (DATA_BABY_ID.equals(dataParameter)) {
            this.refreshDimensions();
        }

    }

    // SOUNDS

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.PIGLIN_HURT;
    }


    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.PIGLIN_DEATH;
    }


    @Override
    protected void playStepSound(BlockPos blockPos, BlockState blockState) {
        this.playSound(SoundEvents.PIGLIN_STEP, 0.15F, 1.0F);
    }

    @Override
    protected void playConvertedSound() {
        this.playSound(SoundEvents.PIGLIN_CONVERTED_TO_ZOMBIFIED);
    }

    // CROSSBOW USER

    @Override
    public void shootCrossbowProjectile(LivingEntity p_230284_1_, ItemStack p_230284_2_, ProjectileEntity p_230284_3_, float p_230284_4_) {
        this.shootCrossbowProjectile(this, p_230284_1_, p_230284_3_, p_230284_4_, 1.6F);
    }

    @Override
    public void onCrossbowAttackPerformed() {
        this.noActionTime = 0;
    }

    @Override
    public void performRangedAttack(LivingEntity p_82196_1_, float p_82196_2_) {
        this.performCrossbowAttack(this, 1.6F);
    }

    // HAS TASK MASTER

    @Override
    public TaskManager<PigmyEntity> createTaskManager(Dynamic<?> dynamic) {
        return new PigmyTaskManager<>(this, this.brainProvider().makeBrain(dynamic));
    }

    @Override
    public void holdInMainHand(ItemStack stack) {
        this.setItemSlotAndDropWhenKilled(EquipmentSlotType.MAINHAND, stack);
    }

    @Override
    public void holdInOffHand(ItemStack stack) {
		if (PigmyTaskManager.isBarteringItem(stack)) {
			this.setItemSlot(EquipmentSlotType.OFFHAND, stack);
			this.setGuaranteedDrop(EquipmentSlotType.OFFHAND);
		} else {
			this.setItemSlotAndDropWhenKilled(EquipmentSlotType.OFFHAND, stack);
		}

    }

    // HAS INVENTORY

    @Override
    public Inventory getInventory(){
        return this.inventory;
    }

    // SENSOR TYPES AND MEMORY MODULE TYPES

    public static final List<SensorType<? extends Sensor<? super PigmyEntity>>> PYGMY_SENSOR_TYPES =
            Lists.newArrayList(
                    SensorType.NEAREST_LIVING_ENTITIES,
                    SensorType.NEAREST_PLAYERS,
                    SensorType.NEAREST_ITEMS,
                    SensorType.HURT_BY,
                    SensorTypeRegistry.PIGMY_SPECIFIC_SENSOR.get()
            );

    public static final List<MemoryModuleType<?>> PYGMY_MEMORY_TYPES =
            Lists.newArrayList(
                    MemoryModuleType.LOOK_TARGET,
                    MemoryModuleType.DOORS_TO_CLOSE,
                    MemoryModuleType.LIVING_ENTITIES,
                    MemoryModuleType.VISIBLE_LIVING_ENTITIES,
                    MemoryModuleType.NEAREST_VISIBLE_PLAYER,
                    MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER,
                    MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLINS,
                    MemoryModuleType.NEARBY_ADULT_PIGLINS,
                    MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM,
                    MemoryModuleType.HURT_BY,
                    MemoryModuleType.HURT_BY_ENTITY,
                    MemoryModuleType.WALK_TARGET,
                    MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE,
                    MemoryModuleType.ATTACK_TARGET,
                    MemoryModuleType.ATTACK_COOLING_DOWN,
                    MemoryModuleType.INTERACTION_TARGET,
                    MemoryModuleType.PATH,
                    MemoryModuleType.ANGRY_AT,
                    MemoryModuleType.UNIVERSAL_ANGER,
                    MemoryModuleType.AVOID_TARGET,
                    MemoryModuleType.ADMIRING_ITEM,
                    MemoryModuleType.TIME_TRYING_TO_REACH_ADMIRE_ITEM,
                    MemoryModuleType.ADMIRING_DISABLED,
                    MemoryModuleType.DISABLE_WALK_TO_ADMIRE_ITEM,
                    MemoryModuleType.CELEBRATE_LOCATION,
                    MemoryModuleType.DANCING,
                    MemoryModuleType.HUNTED_RECENTLY,
                    MemoryModuleType.NEAREST_VISIBLE_BABY_HOGLIN,
                    MemoryModuleType.NEAREST_VISIBLE_NEMESIS,
                    MemoryModuleType.NEAREST_VISIBLE_ZOMBIFIED,
                    MemoryModuleType.RIDE_TARGET,
                    MemoryModuleType.VISIBLE_ADULT_PIGLIN_COUNT,
                    MemoryModuleType.VISIBLE_ADULT_HOGLIN_COUNT,
                    MemoryModuleTypeRegistry.NEAREST_VISIBLE_ADULT_HOGLIN.get(),
                    MemoryModuleType.NEAREST_TARGETABLE_PLAYER_NOT_WEARING_GOLD,
                    MemoryModuleType.NEAREST_PLAYER_HOLDING_WANTED_ITEM,
                    MemoryModuleType.ATE_RECENTLY,
                    MemoryModuleType.NEAREST_REPELLENT
            );
}
