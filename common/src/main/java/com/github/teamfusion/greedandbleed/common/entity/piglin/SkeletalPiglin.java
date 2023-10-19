package com.github.teamfusion.greedandbleed.common.entity.piglin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.EnumSet;
import java.util.UUID;

public class SkeletalPiglin extends Monster implements NeutralMob {
    private static final EntityDataAccessor<Boolean> DATA_BABY_ID = SynchedEntityData.defineId(SkeletalPiglin.class, EntityDataSerializers.BOOLEAN);
    private static final UUID SPEED_MODIFIER_BABY_UUID = UUID.fromString("766bfa64-11f3-11ea-8d71-362b9e155667");
    private static final AttributeModifier SPEED_MODIFIER_BABY = new AttributeModifier(SPEED_MODIFIER_BABY_UUID, "Baby speed boost", 0.2F, AttributeModifier.Operation.MULTIPLY_BASE);
    public static final UniformInt RANGED_INT = TimeUtil.rangeOfSeconds(20, 39);
    private int angerTime;
    private UUID angerTarget;

    private float spawnScale;
    private float spawnScaleO;

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
        if (DATA_POSE.equals(data)) {
            switch (this.getPose()) {
                case EMERGING: {
                    this.spawnScale = 1.0F;
                    break;
                }
            }
        }

        super.onSyncedDataUpdated(data);
        if (DATA_BABY_ID.equals(data)) {
            this.refreshDimensions();
        }
    }

    // ATTRIBUTES
    public static AttributeSupplier.Builder setCustomAttributes() {
        return Monster.createMobAttributes()
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
        this.goalSelector.addGoal(0, new DoNothingGoal());
        this.goalSelector.addGoal(1, new PanicGoal(this, 0.6D));
        this.goalSelector.addGoal(2, new RestrictSunGoal(this));
        this.goalSelector.addGoal(3, new FleeSunGoal(this, 0.6D));
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Wolf.class, 6.0F, 0.55D, 0.6D));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 0.5D));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
    }

    @Override
    public boolean isInvulnerableTo(DamageSource damageSource) {
        if (this.getPose() == Pose.EMERGING && !damageSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            return true;
        }
        return super.isInvulnerableTo(damageSource);
    }

    @Override
    public void tick() {
        super.tick();
        switch (this.getPose()) {
            case EMERGING: {
                this.clientDiggingParticles();

                break;
            }
        }
        this.spawnScaleO = this.spawnScale;
        this.spawnScale = this.getPose() == Pose.EMERGING ? Mth.clamp(this.spawnScale - 0.01f, 0.0f, 1.0f) : 1.0F;

        if (this.spawnScale <= 0.0F && this.getPose() == Pose.EMERGING) {
            this.setPose(Pose.STANDING);
        }
    }

    public float getSpawnScaleAnimationScale(float f) {
        return Mth.lerp(f, this.spawnScaleO, this.spawnScale) / 1.0f;
    }

    private void clientDiggingParticles() {
        RandomSource randomSource = this.getRandom();
        BlockState blockState = this.getBlockStateOn();
        if (blockState.getRenderShape() != RenderShape.INVISIBLE) {
            if (this.level().isClientSide) {
                for (int i = 0; i < 6; ++i) {
                    double d = this.getX() + (double) Mth.randomBetween(randomSource, -0.2f, 0.2f);
                    double e = this.getY();
                    double f = this.getZ() + (double) Mth.randomBetween(randomSource, -0.2f, 0.2f);
                    this.level().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, blockState), d, e, f, 0.0, 0.0, 0.0);
                }
            }
            this.playSound(blockState.getSoundType().getBreakSound(), 1.0F, 1.0F);

        }

        if (this.level().isClientSide) {
            for (int i = 0; i < 3; ++i) {
                double d = this.getX() + (double) Mth.randomBetween(randomSource, -this.getBbWidth() / 2, this.getBbWidth() / 2);
                double e = this.getY() + (double) Mth.randomBetween(randomSource, 0f, this.getBbHeight());
                double f = this.getZ() + (double) Mth.randomBetween(randomSource, -this.getBbWidth() / 2, this.getBbWidth() / 2);
                this.level().addParticle(ParticleTypes.SOUL_FIRE_FLAME, d, e, f, 0.0, 0.0, 0.0);
            }
        }
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    @Override
    public void aiStep() {
        if (this.isAlive()) {
            boolean shouldApplySunburn = this.shouldBurnInDay() && this.isSunBurnTick();
            if (shouldApplySunburn) {
                ItemStack stack = this.getItemBySlot(EquipmentSlot.HEAD);
                if (!stack.isEmpty()) {
                    if (stack.isDamageableItem()) {
                        stack.setDamageValue(stack.getDamageValue() + this.random.nextInt(2));
                        if (stack.getDamageValue() >= stack.getMaxDamage()) {
                            this.broadcastBreakEvent(EquipmentSlot.HEAD);
                            this.setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY);
                        }
                    }

                    shouldApplySunburn = false;
                }

                if (shouldApplySunburn) {
                    this.setSecondsOnFire(8);
                }
            }
        }

        super.aiStep();
    }

    // EXPERIENCE POINTS
    @Override
    public int getExperienceReward() {
        return 2 + this.level().random.nextInt(2);
    }


    @Nullable
    @Override
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

    @Nullable
    @Override
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
        return (double) this.getBbHeight() * 0.92D;
    }

    @Override
    public void setBaby(boolean baby) {
        this.getEntityData().set(DATA_BABY_ID, baby);
        if (!this.level().isClientSide) {
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
    protected void populateDefaultEquipmentSlots(RandomSource randomSource, DifficultyInstance instance) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.GOLDEN_SHOVEL));
        this.maybeWearArmor(EquipmentSlot.HEAD, new ItemStack(Items.GOLDEN_HELMET), randomSource);
        this.maybeWearArmor(EquipmentSlot.CHEST, new ItemStack(Items.GOLDEN_CHESTPLATE), randomSource);
        this.maybeWearArmor(EquipmentSlot.LEGS, new ItemStack(Items.GOLDEN_LEGGINGS), randomSource);
        this.maybeWearArmor(EquipmentSlot.FEET, new ItemStack(Items.GOLDEN_BOOTS), randomSource);
    }

    private void maybeWearArmor(EquipmentSlot equipmentSlot, ItemStack itemStack, RandomSource randomSource) {
        if (randomSource.nextFloat() < 0.1F) {
            this.setItemSlot(equipmentSlot, itemStack);
        }

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

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData groupData, @Nullable CompoundTag tag) {
        float difficultyMultiplier = difficulty.getSpecialMultiplier();
        this.setCanPickUpLoot(level.getRandom().nextFloat() < 0.55F * difficultyMultiplier);

        this.populateDefaultEquipmentSlots(random, difficulty);
        this.populateDefaultEquipmentEnchantments(random, difficulty);

        if (this.getItemBySlot(EquipmentSlot.HEAD).isEmpty()) {
            LocalDate date = LocalDate.now();
            int currentDay = date.get(ChronoField.DAY_OF_MONTH);
            int currentMonth = date.get(ChronoField.MONTH_OF_YEAR);
            if (currentMonth == 10 && currentDay == 31 && level.getRandom().nextFloat() < 0.25F) {
                this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(this.random.nextFloat() < 0.1F ? Blocks.JACK_O_LANTERN : Blocks.CARVED_PUMPKIN));
                this.armorDropChances[EquipmentSlot.HEAD.getIndex()] = 0.0F;
            }
        }

        return super.finalizeSpawn(level, difficulty, spawnType, groupData, tag);
    }

    class DoNothingGoal
            extends Goal {
        public DoNothingGoal() {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP, Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            return SkeletalPiglin.this.getPose() == Pose.EMERGING;
        }
    }
}