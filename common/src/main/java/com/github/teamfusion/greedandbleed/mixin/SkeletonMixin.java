package com.github.teamfusion.greedandbleed.mixin;

import com.github.teamfusion.greedandbleed.common.entity.ICrawlSpawn;
import com.github.teamfusion.greedandbleed.common.entity.TraceAndSetOwner;
import com.github.teamfusion.greedandbleed.common.entity.goal.DoNothingGoal;
import com.github.teamfusion.greedandbleed.common.entity.goal.TracedOwnerHurtByTargetGoal;
import com.github.teamfusion.greedandbleed.common.entity.goal.TracedOwnerHurtTargetGoal;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;
import java.util.UUID;

@Mixin(AbstractSkeleton.class)
public abstract class SkeletonMixin extends Monster implements TraceAndSetOwner, ICrawlSpawn {
    private static final EntityDataAccessor<Optional<UUID>> DATA_UUID = SynchedEntityData.defineId(AbstractSkeleton.class, EntityDataSerializers.OPTIONAL_UUID);

    @Nullable
    private LivingEntity owner;

    private float spawnScale;
    private float spawnScaleO;

    protected SkeletonMixin(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_UUID, Optional.empty());
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> data) {
        super.onSyncedDataUpdated(data);
        if (DATA_POSE.equals(data)) {
            switch (this.getPose()) {
                case EMERGING: {
                    this.spawnScale = 1.0F;
                    break;
                }
            }
        }
    }

    @Inject(method = "registerGoals", at = @At("TAIL"), cancellable = true)
    protected void registerGoals(CallbackInfo callbackInfo) {
        this.goalSelector.addGoal(0, new DoNothingGoal(this));
        this.targetSelector.addGoal(0, new TracedOwnerHurtByTargetGoal<>(this));
        this.targetSelector.addGoal(1, new TracedOwnerHurtTargetGoal<>(this));
    }

    @Override
    public boolean hurt(DamageSource damageSource, float f) {
        if (getPose() == Pose.EMERGING) {
            return false;
        }
        return super.hurt(damageSource, f);
    }

    @Override
    public void tick() {
        super.tick();
        switch (this.getPose()) {
            case EMERGING: {
                this.clientDiggingParticles();

                break;
            }
            default: {
                this.clientSummonedParticles();
            }
        }
        this.spawnScaleO = this.spawnScale;
        this.spawnScale = this.getPose() == Pose.EMERGING ? Mth.clamp(this.spawnScale - 0.01f, 0.0f, 1.0f) : 1.0F;

        if (this.spawnScale <= 0.0F && this.getPose() == Pose.EMERGING) {
            this.setPose(Pose.STANDING);
        }
    }

    private void clientSummonedParticles() {
        RandomSource randomSource = this.getRandom();
        if (this.getOwnerUUID().isPresent()) {
            if (this.level().isClientSide) {
                for (int i = 0; i < 1; ++i) {
                    double d = this.getX() + (double) Mth.randomBetween(randomSource, -this.getBbWidth() / 2, this.getBbWidth() / 2);
                    double e = this.getY() + (double) Mth.randomBetween(randomSource, 0f, this.getBbHeight());
                    double f = this.getZ() + (double) Mth.randomBetween(randomSource, -this.getBbWidth() / 2, this.getBbWidth() / 2);
                    this.level().addParticle(ParticleTypes.SOUL_FIRE_FLAME, d, e, f, 0.0, 0.0, 0.0);
                }
            }
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

    @Override
    public void setSpawnScale(float scale) {
        this.spawnScale = scale;
    }

    @Override
    public float getSpawnScale() {
        return this.spawnScale;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        if (this.getOwnerUUID().isPresent()) {
            tag.putUUID("Owner", this.getOwnerUUID().get());
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);

        if (tag.hasUUID("Owner")) {
            this.setOwnerUUID(Optional.of(tag.getUUID("Owner")));
        }
    }

    public void setOwnerUUID(Optional<UUID> uuid) {
        this.entityData.set(DATA_UUID, uuid);
    }

    public Optional<UUID> getOwnerUUID() {
        return this.entityData.get(DATA_UUID);
    }

    public void setOwner(@Nullable LivingEntity arg) {
        this.owner = arg;
        this.setOwnerUUID(arg == null ? Optional.empty() : Optional.of(arg.getUUID()));
    }

    @Nullable
    public LivingEntity getOwner() {
        if (this.owner == null && this.getOwnerUUID().isPresent() && this.level() instanceof ServerLevel) {
            Entity entity = ((ServerLevel) this.level()).getEntity(this.getOwnerUUID().get());
            if (entity instanceof LivingEntity) {
                this.owner = (LivingEntity) entity;
            }
        }

        return this.owner;
    }


    @Override
    public boolean canAttack(LivingEntity livingEntity) {
        if (livingEntity == this.getOwner()) {
            return false;
        }
        if (livingEntity instanceof TraceAndSetOwner traceAndSetOwner && traceAndSetOwner.getOwner() == this.getOwner()) {
            return false;
        }
        return super.canAttack(livingEntity);
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return this.getOwner() == null && super.shouldDespawnInPeaceful();
    }
}
