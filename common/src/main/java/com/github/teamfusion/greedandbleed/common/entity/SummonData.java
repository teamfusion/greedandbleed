package com.github.teamfusion.greedandbleed.common.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class SummonData {
    @Nullable
    private LivingEntity owner;
    @Nullable
    private UUID ownerUUID;

    public void setOwnerUUID(@Nullable LivingEntity livingEntity) {
        this.ownerUUID = livingEntity.getUUID();
    }

    @Nullable
    public UUID getOwnerUUID() {
        return this.ownerUUID;
    }

    @Nullable
    public LivingEntity getOwner(Level level) {
        Entity entity;
        if (this.owner == null && this.ownerUUID != null && level instanceof ServerLevel && (entity = ((ServerLevel) level).getEntity(this.ownerUUID)) instanceof LivingEntity) {
            this.owner = (LivingEntity) entity;
        }
        return this.owner;
    }

    public CompoundTag save(CompoundTag compoundTag) {
        compoundTag.putUUID("Uuid", this.getOwnerUUID());
        return compoundTag;
    }

    public void load(CompoundTag compoundTag) {
        this.ownerUUID = compoundTag.getUUID("Uuid");
    }
}
