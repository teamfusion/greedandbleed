package com.github.teamfusion.greedandbleed.mixin;

import com.github.teamfusion.greedandbleed.common.entity.TraceAndSetOwner;
import com.github.teamfusion.greedandbleed.common.entity.goal.TracedOwnerHurtByTargetGoal;
import com.github.teamfusion.greedandbleed.common.entity.goal.TracedOwnerHurtTargetGoal;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(AbstractSkeleton.class)
public abstract class SkeletonMixin extends Monster implements TraceAndSetOwner {
    @Nullable
    private LivingEntity owner;
    @Nullable
    private UUID ownerUUID;

    protected SkeletonMixin(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "registerGoals", at = @At("TAIL"), cancellable = true)
    protected void registerGoals(CallbackInfo callbackInfo) {
        this.targetSelector.addGoal(0, new TracedOwnerHurtByTargetGoal<>(this));
        this.targetSelector.addGoal(1, new TracedOwnerHurtTargetGoal<>(this));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        if (this.ownerUUID != null) {
            tag.putUUID("Owner", this.ownerUUID);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);

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
