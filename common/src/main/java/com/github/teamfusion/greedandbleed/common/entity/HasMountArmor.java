package com.github.teamfusion.greedandbleed.common.entity;

import net.minecraft.world.item.ItemStack;

public interface HasMountArmor {
    ItemStack getArmor();

    boolean canWearArmor();

    boolean isArmor(ItemStack stack);
}