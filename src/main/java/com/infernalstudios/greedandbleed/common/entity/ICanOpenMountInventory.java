package com.infernalstudios.greedandbleed.common.entity;

import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.inventory.IInventory;

public interface ICanOpenMountInventory {
    void openMountInventory(AnimalEntity mount, IInventory inventory);
}
