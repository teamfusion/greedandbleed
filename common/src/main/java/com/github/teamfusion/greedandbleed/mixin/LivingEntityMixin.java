package com.github.teamfusion.greedandbleed.mixin;

import com.github.teamfusion.greedandbleed.api.IWarpLink;
import com.github.teamfusion.greedandbleed.common.CommonSetup;
import com.github.teamfusion.greedandbleed.common.entity.piglin.WarpedPiglin;
import com.github.teamfusion.greedandbleed.common.registry.GBDamageSource;
import com.github.teamfusion.greedandbleed.common.registry.PotionRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;
import java.util.UUID;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements IWarpLink {
    public LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Shadow
    public abstract boolean hasEffect(MobEffect mobEffect);

    @Shadow
    public abstract @Nullable MobEffectInstance getEffect(MobEffect mobEffect);

    @Shadow
    public abstract boolean addEffect(MobEffectInstance mobEffectInstance);

    private final LivingEntity $this = (LivingEntity) (Object) this;

    @Unique
    @Nullable
    private Entity greedandbleed$warplinkOwner;
    @Unique
    private Optional<UUID> greedandbleed$warplinkOwnerUUID = Optional.empty();

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    public void addAdditionalSaveData(CompoundTag compoundTag, CallbackInfo ci) {
        if (this.getWarpLinkOwnerUUID().isPresent()) {
            compoundTag.putUUID("WarplingOwner", this.getWarpLinkOwnerUUID().get());
        }
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    public void readAdditionalSaveData(CompoundTag compoundTag, CallbackInfo ci) {
        if (compoundTag.hasUUID("WarplingOwner")) {
            this.setWarpLinkOwnerUUID(Optional.of(compoundTag.getUUID("WarplingOwner")));
        }
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void gb$tick(CallbackInfo ci) {
        CommonSetup.onHoglinUpdate($this);

        if (!this.level().isClientSide) {
            MobEffectInstance mobEffectInstance = this.getEffect(PotionRegistry.WARP_LINK.get());
            if (getWarpLinkOwner() != null) {
                int distance = Mth.ceil(this.blockPosition().distManhattan(getWarpLinkOwner().blockPosition()) / 8F);
                if (distance != mobEffectInstance.getAmplifier()) {
                    addEffect(new MobEffectInstance(mobEffectInstance.getEffect(), mobEffectInstance.getDuration(), distance, mobEffectInstance.isAmbient(), mobEffectInstance.isVisible()));
                }
            }
        }
    }

    @Inject(method = "hurt", at = @At("HEAD"))
    private void gb$hurt(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if ($this instanceof Player) return;
        CommonSetup.onHoglinAttack($this, source);
        //for warplink damage handle
        if (source.getEntity() instanceof IWarpLink warpLink) {
            if (warpLink.getWarpLinkOwner() == $this) {
                source.getEntity().hurt(source.getEntity().damageSources().source(GBDamageSource.WARPED_LINK, $this), amount * 0.5F);
            }
        }
    }

    @Inject(method = "canBeAffected", at = @At("HEAD"), cancellable = true)
    public void canBeAffected(MobEffectInstance mobEffectInstance, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (hasEffect(PotionRegistry.IMMUNITY.get()) && getEffect(PotionRegistry.IMMUNITY.get()).getAmplifier() > mobEffectInstance.getAmplifier()) {
            if (mobEffectInstance.getEffect() != MobEffects.WITHER || mobEffectInstance.getEffect() != MobEffects.DIG_SLOWDOWN || mobEffectInstance.getEffect() != MobEffects.LEVITATION) {
                callbackInfoReturnable.setReturnValue(false);
            }
        }
    }

    @Inject(method = "updateFallFlying", at = @At("HEAD"), cancellable = true)
    private void updateFallFlying(CallbackInfo callbackInfo) {
        LivingEntity livingEntity = (LivingEntity) ((Object) this);
        if (livingEntity instanceof WarpedPiglin) {
            callbackInfo.cancel();
        }
    }

    @Inject(method = "onEffectAdded", at = @At("HEAD"))
    protected void onEffectAdded(MobEffectInstance mobEffectInstance, Entity entity, CallbackInfo ci) {
        if (entity != null && mobEffectInstance.getEffect() == PotionRegistry.WARP_LINK.get()) {
            this.setWarpLinkOwner(entity);
        }
    }

    @Inject(method = "onEffectRemoved", at = @At("HEAD"))
    protected void onEffectRemove(MobEffectInstance mobEffectInstance, CallbackInfo ci) {
        if (mobEffectInstance.getEffect() == PotionRegistry.WARP_LINK.get()) {
            this.setWarpLinkOwner(null);
        }
    }

    @Override
    public void setWarpLinkOwner(@Nullable Entity arg) {
        this.greedandbleed$warplinkOwner = arg;
        this.setWarpLinkOwnerUUID(arg == null ? Optional.empty() : Optional.of(arg.getUUID()));
    }

    @Override
    public void setWarpLinkOwnerUUID(Optional<UUID> uuid) {
        this.greedandbleed$warplinkOwnerUUID = uuid;
    }

    @Override
    public @Nullable Entity getWarpLinkOwner() {
        if (this.greedandbleed$warplinkOwner == null && this.getWarpLinkOwnerUUID().isPresent() && this.level() instanceof ServerLevel) {
            Entity entity = ((ServerLevel) this.level()).getEntity(this.getWarpLinkOwnerUUID().get());
            if (entity instanceof LivingEntity) {
                this.greedandbleed$warplinkOwner = (LivingEntity) entity;
            }
        }

        return this.greedandbleed$warplinkOwner;
    }

    @Override
    public Optional<UUID> getWarpLinkOwnerUUID() {
        return this.greedandbleed$warplinkOwnerUUID;
    }
}