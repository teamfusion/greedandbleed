package com.github.teamfusion.greedandbleed.common.entity;

import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.animal.Animal;

public interface CanOpenMountInventory {
    void openMountInventory(Animal mount, SimpleContainer inventory);
}