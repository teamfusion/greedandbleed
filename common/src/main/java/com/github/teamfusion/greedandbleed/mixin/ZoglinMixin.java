package com.github.teamfusion.greedandbleed.mixin;

import com.github.teamfusion.greedandbleed.common.entity.HasMountArmor;
import com.github.teamfusion.greedandbleed.common.entity.IConvertToNormal;
import com.github.teamfusion.greedandbleed.common.entity.ICrawlSpawn;
import com.github.teamfusion.greedandbleed.common.entity.TraceAndSetOwner;
import com.github.teamfusion.greedandbleed.common.item.HoglinArmorItem;
import com.github.teamfusion.greedandbleed.common.registry.PotionRegistry;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
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
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Zoglin;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;
import java.util.UUID;

@Mixin(Zoglin.class)
public abstract class ZoglinMixin extends Monster implements TraceAndSetOwner, ICrawlSpawn, HasMountArmor, IConvertToNormal {
    private static final UUID ARMOR_MODIFIER_UUID = UUID.fromString("556E1665-8B10-40C8-8F9D-CF9B1667F295");
    private static final EntityDataAccessor<Optional<UUID>> DATA_UUID = SynchedEntityData.defineId(Zoglin.class, EntityDataSerializers.OPTIONAL_UUID);

    @Nullable
    private LivingEntity owner;
    @Unique
    protected int timeWithImmunity;
    @Unique
    protected boolean canConvertToNormal;

    @Unique
    private float spawnScale;
    @Unique
    private float spawnScaleO;

    protected ZoglinMixin(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "defineSynchedData", at = @At("RETURN"))
    private void gb$defineSteeringData(CallbackInfo ci) {
        this.entityData.define(DATA_UUID, Optional.empty());
    }

    @Inject(method = "onSyncedDataUpdated", at = @At("TAIL"))
    public void onSyncedDataUpdated(EntityDataAccessor<?> entityDataAccessor, CallbackInfo ci) {
        if (DATA_POSE.equals(entityDataAccessor)) {
            switch (this.getPose()) {
                case EMERGING: {
                    this.spawnScale = 1.0F;
                    break;
                }
            }
        }
    }

    @Inject(method = "customServerAiStep", at = @At("HEAD"))
    protected void customServerAiStep(CallbackInfo callbackInfo) {
        if (!this.level().isClientSide()) {

            if (this.hasEffect(PotionRegistry.IMMUNITY.get()) && this.getEffect(PotionRegistry.IMMUNITY.get()).getAmplifier() > 0) {
                if (gb$hasCorrectConvert()) {
                    if (++timeWithImmunity > 300) {
                        finishImmunity((ServerLevel) this.level());
                    }
                    if (this.level() instanceof ServerLevel serverLevel) {
                        serverLevel.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, this.getRandomX(this.getBbWidth() / 2), this.getRandomY(), this.getRandomZ(this.getBbWidth() / 2), 2, 1.0F, 0D, 0D, 0D);
                    }
                } else if (timeWithImmunity > 0) {
                    --this.timeWithImmunity;
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

    public boolean gb$hasCorrectConvert() {
        return this.canConvertToNormal;
    }

    public void gb$setCanConvertToNormal(boolean canConvertToNormal) {
        this.canConvertToNormal = canConvertToNormal;
    }

    protected void finishImmunity(ServerLevel serverLevel) {
        Hoglin pig = this.convertTo(EntityType.HOGLIN, true);
        if (pig != null) {
            pig.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 200, 0));
            pig.setImmuneToZombification(true);
            pig.setPersistenceRequired();
            pig.playSound(SoundEvents.ZOMBIE_VILLAGER_CONVERTED);
        }
    }

    @Inject(method = "isTargetable", at = @At("HEAD"), cancellable = true)
    private void isTargetable(LivingEntity livingEntity, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {

        if (this.getOwner() != null) {
            if (this.getOwner().getLastHurtMob() == livingEntity) {
                callbackInfoReturnable.setReturnValue(true);
            }
        }
        if (this.getOwner() == livingEntity) {
            callbackInfoReturnable.setReturnValue(false);
        }
        if (livingEntity instanceof TraceAndSetOwner traceAndSetOwner) {
            if (traceAndSetOwner.getOwner() == this.getOwner()) {
                callbackInfoReturnable.setReturnValue(false);
            }
        }
        if (livingEntity.getPose() == Pose.EMERGING) {
            callbackInfoReturnable.setReturnValue(false);
        }
    }

    @Inject(method = "setAttackTarget", at = @At("HEAD"), cancellable = true)
    private void setAttackTarget(LivingEntity livingEntity, CallbackInfo callbackInfo) {
        if (this.getOwner() == livingEntity) {
            callbackInfo.cancel();
        }
        if (livingEntity instanceof TraceAndSetOwner traceAndSetOwner) {
            if (traceAndSetOwner.getOwner() == this.getOwner()) {
                callbackInfo.cancel();
            }
        }
        if (livingEntity.getPose() == Pose.EMERGING) {
            callbackInfo.cancel();
        }
    }


    @Override
    public ItemStack getArmor() {
        return this.getItemBySlot(EquipmentSlot.CHEST);
    }

    public void setArmor(ItemStack stack) {
        this.setItemSlot(EquipmentSlot.CHEST, stack);
        if (!this.level().isClientSide) {
            AttributeInstance armor = this.getAttribute(Attributes.ARMOR);
            if (armor != null) {
                armor.removeModifier(ARMOR_MODIFIER_UUID);
                if (this.isArmor(stack)) {
                    int i = ((HoglinArmorItem) stack.getItem()).getProtection();
                    if (i != 0) {
                        armor.addTransientModifier(new AttributeModifier(ARMOR_MODIFIER_UUID, "Horse armor bonus", i, AttributeModifier.Operation.ADDITION));
                    }
                }
            }
        }
    }

    @Override
    public boolean canWearArmor() {
        return true;
    }

    @Override
    public boolean isArmor(ItemStack stack) {
        return stack.getItem() instanceof HoglinArmorItem;
    }

    @Override
    public SlotAccess getSlot(int slot) {
        return slot == 102 ? new SlotAccess() {
            @Override
            public ItemStack get() {
                return ZoglinMixin.this.getArmor();
            }

            @Override
            public boolean set(ItemStack stack) {
                ZoglinMixin.this.setArmor(stack);
                return true;
            }
        } : super.getSlot(slot);
    }


    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        if (this.getOwnerUUID().isPresent()) {
            tag.putUUID("Owner", this.getOwnerUUID().get());
        }
        tag.putBoolean("CanConvertToNormal", this.canConvertToNormal);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);

        if (tag.hasUUID("Owner")) {
            this.setOwnerUUID(Optional.of(tag.getUUID("Owner")));
        }
        this.gb$setCanConvertToNormal(tag.getBoolean("CanConvertToNormal"));
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

    @Inject(method = "hurt", at = @At("HEAD"), cancellable = true)
    private void hurt(DamageSource damageSource, float f, CallbackInfoReturnable<Boolean> cir) {
        if (getPose() == Pose.EMERGING) {
            cir.setReturnValue(true);
        }
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return this.getOwner() == null && super.shouldDespawnInPeaceful();
    }
}
