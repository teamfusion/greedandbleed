package com.github.teamfusion.greedandbleed.api;

import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public interface IWarpLink {

    public void setWarpLinkOwnerUUID(Optional<UUID> uuid);

    public Optional<UUID> getWarpLinkOwnerUUID();

    public void setWarpLinkOwner(@Nullable Entity arg);

    @Nullable
    public Entity getWarpLinkOwner();
}
