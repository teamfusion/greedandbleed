package com.github.teamfusion.greedandbleed.common.entity;

import net.minecraft.world.Container;
import net.minecraft.world.entity.animal.Animal;

public interface CanOpenMountInventory {
    void openMountInventory(Animal mount, Container inventory);
}