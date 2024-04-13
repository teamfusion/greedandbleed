package com.github.teamfusion.greedandbleed.common.entity.piglin;

import com.github.teamfusion.greedandbleed.api.ITaskManager;
import com.github.teamfusion.greedandbleed.api.WarpedPiglinTaskManager;
import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Dynamic;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.piglin.PiglinArmPose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class WarpedPiglin extends GBPiglin implements Shearable {
    protected static final ImmutableList<SensorType<? extends Sensor<? super WarpedPiglin>>> SENSOR_TYPES = ImmutableList.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_PLAYERS, SensorType.NEAREST_ITEMS, SensorType.HURT_BY, SensorType.PIGLIN_BRUTE_SPECIFIC_SENSOR);
    protected static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(MemoryModuleType.LOOK_TARGET, MemoryModuleType.DOORS_TO_CLOSE, MemoryModuleType.NEAREST_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLINS, MemoryModuleType.NEARBY_ADULT_PIGLINS, MemoryModuleType.HURT_BY, MemoryModuleType.HURT_BY_ENTITY, MemoryModuleType.WALK_TARGET, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.ATTACK_TARGET, MemoryModuleType.ATTACK_COOLING_DOWN, MemoryModuleType.INTERACTION_TARGET, MemoryModuleType.PATH, MemoryModuleType.ANGRY_AT, MemoryModuleType.NEAREST_VISIBLE_NEMESIS, MemoryModuleType.HOME);
    public WarpedPiglin(EntityType<? extends WarpedPiglin> entityType, Level level) {
        super(entityType, level);
        this.setCanPickUpLoot(false);
    }

    @Override
    protected BodyRotationControl createBodyControl() {
        return new WarpedBodyRotationControl(this);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag p_250330_) {
        super.addAdditionalSaveData(p_250330_);
        p_250330_.putBoolean("IsFallFlying", this.getPose() == Pose.FALL_FLYING);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag p_250781_) {
        super.readAdditionalSaveData(p_250781_);
        if (p_250781_.getBoolean("IsFallFlying")) {
            this.setPose(Pose.FALL_FLYING);
        }
    }

    public void startFallFlying() {
        this.setSharedFlag(7, true);
    }

    public void stopFallFlying() {
        this.setSharedFlag(7, true);
        this.setSharedFlag(7, false);
    }


    @Override
    public void tick() {
        super.tick();
        this.updatePlayerPose();
    }

    protected void updatePlayerPose() {
        Pose pose;
        if (this.isFallFlying()) {
            pose = Pose.FALL_FLYING;
        } else {
            pose = Pose.STANDING;
        }

        Pose pose1 = pose;

        this.setPose(pose1);
    }

    public EntityDimensions getDimensions(Pose p_36166_) {
        if (p_36166_ == Pose.FALL_FLYING) {
            return EntityDimensions.scalable(0.8F, 0.8F);
        }
        return super.getDimensions(p_36166_);
    }

    @Override
    public void travel(Vec3 vec3) {
        double d = 0.08;
        if (this.isControlledByLocalInstance() && this.isFallFlying()) {
            float f;
            this.checkSlowFallDistance();
            Vec3 vec35 = this.getDeltaMovement();
            Vec3 vec36 = this.getLookAngle();
            f = this.getXRot() * 0.017453292F;
            double i = Math.sqrt(vec36.x * vec36.x + vec36.z * vec36.z);
            double j = vec35.horizontalDistance();
            double k = vec36.length();
            double l = Math.cos((double) f);
            l = l * l * Math.min(1.0, k / 0.4);
            vec35 = this.getDeltaMovement().add(0.0, d * (-1.0 + l * 0.75), 0.0);
            double m;
            if (vec35.y < 0.0 && i > 0.0) {
                m = vec35.y * -0.1 * l;
                vec35 = vec35.add(vec36.x * m / i, m, vec36.z * m / i);
            }

            if (f < 0.0F && i > 0.0) {
                m = j * (double) (-Mth.sin(f)) * 0.04;
                vec35 = vec35.add(-vec36.x * m / i, m * 3.2, -vec36.z * m / i);
            }

            if (i > 0.0) {
                vec35 = vec35.add((vec36.x / i * j - vec35.x) * 0.1, 0.0, (vec36.z / i * j - vec35.z) * 0.1);
            }

            this.setDeltaMovement(vec35.multiply(0.95, 0.8, 0.95));
            this.move(MoverType.SELF, this.getDeltaMovement());
            if (this.horizontalCollision && !this.level().isClientSide) {
                m = this.getDeltaMovement().horizontalDistance();
                double n = j - m;
                float o = (float) (n * 10.0 - 3.0);
                if (o > 0.0F) {
                    this.playSound(this.getFallDamageSound((int) o), 1.0F, 1.0F);
                    this.hurt(this.damageSources().flyIntoWall(), o);
                }
            }

            if (this.onGround() && !this.level().isClientSide) {
                this.setSharedFlag(7, false);
            }
        } else {
            super.travel(vec3);
        }
    }

    private SoundEvent getFallDamageSound(int i) {
        return i > 4 ? this.getFallSounds().big() : this.getFallSounds().small();
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if (this.isFallFlying()) {
            if (this.isInWaterOrBubble() || this.isInPowderSnow) {
                this.stopFallFlying();
            }
        }


        if (!this.isFallFlying() && !this.isInWater() && this.fallDistance > 3.0F) {
            this.startFallFlying();
        }
    }


    protected int calculateFallDamage(float p_149389_, float p_149390_) {

        return super.calculateFallDamage(p_149389_, p_149390_);
    }


    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
    }


    @Override
    protected Brain.Provider<WarpedPiglin> brainProvider() {
        return Brain.provider(MEMORY_TYPES, SENSOR_TYPES);
    }

    protected Brain<?> makeBrain(Dynamic<?> dynamic) {
        this.taskManager = this.createTaskManager(dynamic);
        return this.taskManager.getBrain();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Brain<WarpedPiglin> getBrain() {
        return (Brain<WarpedPiglin>) super.getBrain();
    }

    // ATTRIBUTES
    public static AttributeSupplier.Builder setCustomAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 16.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.FLYING_SPEED, 0.1F);
    }

    @Override
    public MobType getMobType() {
        return MobType.UNDEAD;
    }

    @Override
    protected void customServerAiStep() {
        this.level().getProfiler().push("shamanBrain");
        this.getBrain().tick((ServerLevel) this.level(), this);
        this.level().getProfiler().pop();
        this.taskManager.updateActivity();
        super.customServerAiStep();
    }

    // EXPERIENCE POINTS
    @Override
    public int getExperienceReward() {
        return 2 + this.level().random.nextInt(2);
    }


    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.PIGLIN_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.PIGLIN_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.ZOMBIE_STEP, 0.15F, 1.0F);
    }

    @Override
    protected float getStandingEyeHeight(Pose pose, EntityDimensions dimensions) {
        if (pose == Pose.FALL_FLYING) {
            return dimensions.height * 0.5F;
        }
        return dimensions.height * 0.85F;
    }

    @Override
    public double getPassengersRidingOffset() {
        return (double) this.getBbHeight() * 0.92D;
    }
    @Override
    public PiglinArmPose getArmPose() {
        return null;
    }

    @Override
    protected void playConvertedSound() {
        this.playSound(SoundEvents.PIGLIN_CONVERTED_TO_ZOMBIFIED, 1.0f, this.getVoicePitch());

    }

    @Override
    public ITaskManager<?> createTaskManager(Dynamic<?> dynamic) {
        return new WarpedPiglinTaskManager<>(this, this.brainProvider().makeBrain(dynamic));
    }

    @Override
    public void holdInMainHand(ItemStack stack) {

    }

    @Override
    public void holdInOffHand(ItemStack stack) {

    }

    @Override
    public boolean removeWhenFarAway(double distance) {
        return false;
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return false;
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor serverLevelAccessor, DifficultyInstance difficultyInstance, MobSpawnType mobSpawnType, @Nullable SpawnGroupData spawnGroupData, @Nullable CompoundTag compoundTag) {
        if (mobSpawnType == MobSpawnType.STRUCTURE) {
            this.taskManager.initMemories();
        }
        return super.finalizeSpawn(serverLevelAccessor, difficultyInstance, mobSpawnType, spawnGroupData, compoundTag);
    }

    @Override
    public InteractionResult mobInteract(Player player2, InteractionHand interactionHand) {
        ItemStack itemStack = player2.getItemInHand(interactionHand);
        if (itemStack.is(Items.SHEARS) && this.readyForShearing()) {
            this.shear(SoundSource.PLAYERS);
            this.gameEvent(GameEvent.SHEAR, player2);
            if (!this.level().isClientSide) {
                itemStack.hurtAndBreak(1, player2, player -> player.broadcastBreakEvent(interactionHand));
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }
        return super.mobInteract(player2, interactionHand);
    }

    @Override
    public void shear(SoundSource soundSource) {
        this.convertTo(EntityType.PIGLIN, false);
        this.level().playSound(null, this, SoundEvents.ZOMBIE_VILLAGER_CONVERTED, soundSource, 1.0f, 1.0f);
        for (int i = 0; i < 3; ++i) {
            this.level().addFreshEntity(new ItemEntity(this.level(), this.getX(), this.getY(1.0), this.getZ(), new ItemStack(Items.WARPED_FUNGUS)));
        }
    }

    @Override
    public boolean readyForShearing() {
        return true;
    }

    class WarpedBodyRotationControl extends BodyRotationControl {
        public WarpedBodyRotationControl(Mob p_33216_) {
            super(p_33216_);
        }

        public void clientTick() {
            if (WarpedPiglin.this.isFallFlying()) {
                super.clientTick();
                WarpedPiglin.this.setYRot(WarpedPiglin.this.getYHeadRot());
                WarpedPiglin.this.setYBodyRot(WarpedPiglin.this.getYHeadRot());
            } else {
                super.clientTick();
            }
        }
    }
}