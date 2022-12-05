package com.github.teamfusion.greedandbleed.common.entity;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public interface HasMountInventory {
    default boolean isChestStack(ItemStack stack) {
        return this.isChestItem(stack.getItem());
    }

    default boolean isChestItem(Item item) {
        return item == this.getDefaultChestItem();
    }

    Item getDefaultChestItem();

    void openInventory(Player player);

    boolean hasChest();

    void setChest(boolean chest);

    int getInventoryColumns();
}