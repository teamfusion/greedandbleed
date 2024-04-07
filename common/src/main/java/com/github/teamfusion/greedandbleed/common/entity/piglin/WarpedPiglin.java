package com.github.teamfusion.greedandbleed.common.entity.piglin;

import com.github.teamfusion.greedandbleed.api.ITaskManager;
import com.github.teamfusion.greedandbleed.api.WarpedPiglinTaskManager;
import com.github.teamfusion.greedandbleed.common.entity.movecontrol.NoticeDangerFlyingMoveControl;
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
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
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


    private float groundScale;
    private float oGroundScale;
    public WarpedPiglin(EntityType<? extends WarpedPiglin> entityType, Level level) {
        super(entityType, level);
        this.setCanPickUpLoot(false);
        this.moveControl = new NoticeDangerFlyingMoveControl(this, 15, false);
    }

    @Override
    protected PathNavigation createNavigation(Level arg) {
        FlyingPathNavigation flyingpathnavigation = new FlyingPathNavigation(this, arg);
        flyingpathnavigation.setCanOpenDoors(false);
        flyingpathnavigation.setCanFloat(true);
        flyingpathnavigation.setCanPassDoors(true);
        return flyingpathnavigation;
    }

    @Override
    public void travel(Vec3 arg) {
        if (this.isControlledByLocalInstance() && this.isNoGravity()) {
            if (this.isInWater()) {
                this.moveRelative(0.02f, arg);
                this.move(MoverType.SELF, this.getDeltaMovement());
                this.setDeltaMovement(this.getDeltaMovement().scale(0.8f));
            } else if (this.isInLava()) {
                this.moveRelative(0.02f, arg);
                this.move(MoverType.SELF, this.getDeltaMovement());
                this.setDeltaMovement(this.getDeltaMovement().scale(0.5));
            } else {
                this.moveRelative(this.getSpeed(), arg);
                this.move(MoverType.SELF, this.getDeltaMovement());
                this.setDeltaMovement(this.getDeltaMovement().scale(0.91f));
            }
            this.calculateEntityAnimation(false);
        } else {
            super.travel(arg);
        }

    }

    @Override
    public void aiStep() {
        super.aiStep();
        this.calculateFlapping();
    }

    private void calculateFlapping() {
        Vec3 vec3 = this.getDeltaMovement();
        if (!this.level().isClientSide()) {
            if (!this.isNoGravity() && this.getNavigation().isInProgress() && this.groundScale < 1F && vec3.y < 0.0) {
                this.setDeltaMovement(vec3.multiply(1.0, 0.6 + (0.4 * this.groundScale), 1.0));

            }
            if (this.groundScale < 1F && vec3.y < 0.0) {
                this.fallDistance = 0F;
            }
        }
        this.oGroundScale = this.groundScale;
        if (!this.isNoGravity() && this.groundScale < 1F) {
            this.groundScale += 0.1F;
        } else if (this.groundScale > 0F && this.isNoGravity()) {
            this.groundScale -= 0.1F;
        }
    }

    public float getGroundScale(float particalTick) {
        return Mth.lerp(particalTick, this.groundScale, this.oGroundScale);
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
        return this.isBaby() ? 0.93F : 1.74F;
    }

    @Override
    public double getPassengersRidingOffset() {
        return (double) this.getBbHeight() * 0.92D;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
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
}