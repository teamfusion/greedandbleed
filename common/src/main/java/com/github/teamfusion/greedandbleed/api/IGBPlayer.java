package com.github.teamfusion.greedandbleed.api;

import net.minecraft.core.BlockPos;

public interface IGBPlayer {
    void setBlockEntityPos(BlockPos blockPos);

    BlockPos getBlockEntityPos();
}
