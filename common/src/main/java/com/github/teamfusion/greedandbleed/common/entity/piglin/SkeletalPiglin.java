package com.github.teamfusion.greedandbleed.common.entity.piglin;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.FleeSunGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RestrictSunGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.RandomSource;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.Random;
import java.util.UUID;

public class SkeletalPiglin extends Monster implements NeutralMob {
    private static final EntityDataAccessor<Boolean> DATA_BABY_ID = SynchedEntityData.defineId(SkeletalPiglin.class, EntityDataSerializers.BOOLEAN);
    private static final UUID SPEED_MODIFIER_BABY_UUID = UUID.fromString("766bfa64-11f3-11ea-8d71-362b9e155667");
    private static final AttributeModifier SPEED_MODIFIER_BABY = new AttributeModifier(SPEED_MODIFIER_BABY_UUID, "Baby speed boost", 0.2F, AttributeModifier.Operation.MULTIPLY_BASE);
    public static final UniformInt RANGED_INT = TimeUtil.rangeOfSeconds(20, 39);
    private int angerTime;
    private UUID angerTarget;
    protected final Random rand = new Random();

    public SkeletalPiglin(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_BABY_ID, false);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> data) {
        super.onSyncedDataUpdated(data);
        if (DATA_BABY_ID.equals(data)) {
            this.refreshDimensions();
        }
    }

    // ATTRIBUTES
    public static AttributeSupplier.Builder setCustomAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.5D);
    }

    public static boolean checkSkeletalPiglinSpawnRules(EntityType<SkeletalPiglin> piglin, LevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        return level.getDifficulty() != Difficulty.PEACEFUL && level.getBlockState(pos.below()).getBlock() != Blocks.NETHER_WART_BLOCK;
    }

    @Override
    public MobType getMobType() {
        return MobType.UNDEAD;
    }

    protected boolean shouldBurnInDay() {
        return true;
    }

    // BEHAVIOR
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new PanicGoal(this, 0.6D));
        this.goalSelector.addGoal(2, new RestrictSunGoal(this));
        this.goalSelector.addGoal(3, new FleeSunGoal(this, 0.6D));
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Wolf.class, 6.0F, 0.55D, 0.6D));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 0.5D));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    @Override
    public void aiStep() {
        if (this.isAlive()) {
            boolean flag = this.shouldBurnInDay() && this.isSunBurnTick();
            if (flag) {
                ItemStack stack = this.getItemBySlot(EquipmentSlot.HEAD);
                if (!stack.isEmpty()) {
                    if (stack.isDamageableItem()) {
                        stack.setDamageValue(stack.getDamageValue() + this.rand.nextInt(2));
                        if (stack.getDamageValue() >= stack.getMaxDamage()) {
                            this.broadcastBreakEvent(EquipmentSlot.HEAD);
                            this.setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY);
                        }
                    }

                    flag = false;
                }

                if (flag) {
                    this.setSecondsOnFire(8);
                }
            }
        }

        super.aiStep();
    }

    // EXPERIENCE POINTS
    @Override
    protected int getExperienceReward(Player player) {
        return 2 + this.level.random.nextInt(2);
    }

    @Nullable @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.SKELETON_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.SKELETON_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.SKELETON_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.SKELETON_STEP, 0.15F, 1.0F);
    }

    @Override
    public int getRemainingPersistentAngerTime() {
        return this.angerTime;
    }

    @Override
    public void setRemainingPersistentAngerTime(int time) {
        this.angerTime = time;
    }

    @Nullable @Override
    public UUID getPersistentAngerTarget() {
        return this.angerTarget;
    }

    @Override
    public void setPersistentAngerTarget(@Nullable UUID target) {
        this.angerTarget = target;
    }

    @Override
    public void startPersistentAngerTimer() {
        this.setRemainingPersistentAngerTime(RANGED_INT.sample(this.random));
    }

    @Override
    protected float getStandingEyeHeight(Pose pose, EntityDimensions dimensions) {
        return this.isBaby() ? 0.93F : 1.74F;
    }

    @Override
    public double getPassengersRidingOffset() {
        return (double)this.getBbHeight() * 0.92D;
    }

    @Override
    public void setBaby(boolean baby) {
        this.getEntityData().set(DATA_BABY_ID, baby);
        if (!this.level.isClientSide) {
            AttributeInstance instance = this.getAttribute(Attributes.MOVEMENT_SPEED);
            if (instance != null) {
                instance.removeModifier(SPEED_MODIFIER_BABY);
                if (baby) {
                    instance.addTransientModifier(SPEED_MODIFIER_BABY);
                }
            }
        }
    }

    @Override
    public boolean isBaby() {
        return this.getEntityData().get(DATA_BABY_ID);
    }

    @Override
    protected void populateDefaultEquipmentSlots(DifficultyInstance instance) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.GOLDEN_SHOVEL));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("IsBaby", this.isBaby());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setBaby(tag.getBoolean("IsBaby"));
    }

    @Nullable @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData groupData, @Nullable CompoundTag tag) {
        float multiplier = difficulty.getSpecialMultiplier();
        this.setCanPickUpLoot(this.rand.nextFloat() < 0.55F * multiplier);

        this.populateDefaultEquipmentSlots(difficulty);
        this.populateDefaultEquipmentEnchantments(difficulty);

        if (this.getItemBySlot(EquipmentSlot.HEAD).isEmpty()) {
            LocalDate date = LocalDate.now();
            int day = date.get(ChronoField.DAY_OF_MONTH);
            int month = date.get(ChronoField.MONTH_OF_YEAR);
            if (month == 10 && day == 31 && this.rand.nextFloat() < 0.25F) {
                this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(this.random.nextFloat() < 0.1F ? Blocks.JACK_O_LANTERN : Blocks.CARVED_PUMPKIN));
                this.armorDropChances[EquipmentSlot.HEAD.getIndex()] = 0.0F;
            }
        }

        return super.finalizeSpawn(level, difficulty, spawnType, groupData, tag);
    }
}