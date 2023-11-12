package com.github.teamfusion.greedandbleed.mixin;

import com.github.teamfusion.greedandbleed.common.entity.ICrawlSpawn;
import com.github.teamfusion.greedandbleed.common.entity.TraceAndSetOwner;
import com.github.teamfusion.greedandbleed.common.entity.goal.DoNothingGoal;
import com.github.teamfusion.greedandbleed.common.entity.goal.TracedOwnerHurtByTargetGoal;
import com.github.teamfusion.greedandbleed.common.entity.goal.TracedOwnerHurtTargetGoal;
import com.github.teamfusion.greedandbleed.common.registry.PotionRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;
import java.util.UUID;

@Mixin(ZombifiedPiglin.class)
public abstract class ZombifiedPiglinMixin extends Monster implements TraceAndSetOwner, ICrawlSpawn {
    private static final EntityDataAccessor<Optional<UUID>> DATA_UUID = SynchedEntityData.defineId(ZombifiedPiglin.class, EntityDataSerializers.OPTIONAL_UUID);

    protected int timeWithImmunity;
    @Nullable
    private LivingEntity owner;

    private float spawnScale;
    private float spawnScaleO;

    protected ZombifiedPiglinMixin(EntityType<? extends Monster> entityType, Level level) {
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

    public float getSpawnScaleAnimationScale(float f) {
        return Mth.lerp(f, this.spawnScaleO, this.spawnScale) / 1.0f;
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

    @Inject(method = "addBehaviourGoals", at = @At("TAIL"))
    protected void addBehaviourGoals(CallbackInfo callbackInfo) {
        this.goalSelector.addGoal(0, new DoNothingGoal(this));
        //remove uncorrect ai. because zombified piglin called mob. so I prevent
        this.targetSelector.getAvailableGoals().removeIf(wrappedGoal -> {
            return wrappedGoal.getGoal() instanceof HurtByTargetGoal;
        });
        this.targetSelector.addGoal(0, new TracedOwnerHurtByTargetGoal<>(this));
        this.targetSelector.addGoal(1, new TracedOwnerHurtTargetGoal<>(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, new Class[0]) {
            @Override
            public HurtByTargetGoal setAlertOthers(Class<?>... classs) {
                if (getOwner() != null) {
                    return this;
                }
                return super.setAlertOthers(classs);
            }
        }.setAlertOthers(new Class[0]));
    }

    @Inject(method = "customServerAiStep", at = @At("HEAD"))
    protected void customServerAiStep(CallbackInfo callbackInfo) {
        if (!this.level().isClientSide()) {

            if (this.hasEffect(PotionRegistry.IMMUNITY.get()) && this.getEffect(PotionRegistry.IMMUNITY.get()).getAmplifier() > 0) {
                if (hasCorrectConvert()) {
                    if (++timeWithImmunity > 300) {
                        finishImmunity((ServerLevel) this.level());
                    }
                } else if (timeWithImmunity > 0) {
                    --this.timeWithImmunity;
                }
            }
        }
    }

    private boolean hasCorrectConvert() {
        int j = 0;
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        for (int l = (int) this.getY() - 4; l < (int) this.getY() + 4; ++l) {
            BlockState blockState = this.level().getBlockState(mutableBlockPos.set(this.getX(), l, this.getZ()));
            if (blockState.is(Blocks.SOUL_FIRE)) {
                this.level().removeBlock(mutableBlockPos.set(this.getX(), l, this.getZ()), false);
                return true;
            }
        }
        return false;
    }

    protected void finishImmunity(ServerLevel serverLevel) {
        Piglin pig = this.convertTo(EntityType.PIGLIN, true);
        if (pig != null) {
            pig.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 200, 0));
            pig.setImmuneToZombification(true);
            pig.setPersistenceRequired();
            pig.playSound(SoundEvents.ZOMBIE_VILLAGER_CONVERTED);
        }
    }

    @Override
    public boolean hurt(DamageSource damageSource, float f) {
        if (getPose() == Pose.EMERGING) {
            return false;
        }
        return super.hurt(damageSource, f);
    }

    @Inject(method = "maybeAlertOthers", at = @At("HEAD"), cancellable = true)
    private void maybeAlertOthers(CallbackInfo callbackInfo) {
        if (this.getOwner() != null) {
            callbackInfo.cancel();
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
