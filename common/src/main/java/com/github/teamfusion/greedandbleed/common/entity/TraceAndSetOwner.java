package com.github.teamfusion.greedandbleed.common.entity;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TraceableEntity;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public interface TraceAndSetOwner extends TraceableEntity {
    void setOwner(@Nullable LivingEntity arg);

    void setOwnerUUID(Optional<UUID> uuid);

    Optional<UUID> getOwnerUUID();
}
