package com.github.teamfusion.greedandbleed.mixin;

import com.github.teamfusion.greedandbleed.common.entity.TraceAndSetOwner;
import com.github.teamfusion.greedandbleed.common.registry.PotionRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Zoglin;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@Mixin(Zoglin.class)
public abstract class ZoglinMixin extends Monster implements TraceAndSetOwner {
    @Nullable
    private LivingEntity owner;
    @Nullable
    private UUID ownerUUID;
    protected int timeWithImmunity;

    protected ZoglinMixin(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
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
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    public void addAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {
        if (this.ownerUUID != null) {
            tag.putUUID("Owner", this.ownerUUID);
        }
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    public void readAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {
        if (tag.hasUUID("Owner")) {
            this.ownerUUID = tag.getUUID("Owner");
        }
    }

    public void setOwner(@Nullable LivingEntity arg) {
        this.owner = arg;
        this.ownerUUID = arg == null ? null : arg.getUUID();
    }

    @Nullable
    public LivingEntity getOwner() {
        if (this.owner == null && this.ownerUUID != null && this.level() instanceof ServerLevel) {
            Entity entity = ((ServerLevel) this.level()).getEntity(this.ownerUUID);
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
