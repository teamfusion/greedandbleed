package com.github.teamfusion.greedandbleed.common.entity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public interface IHasMountInventory {

    default boolean isChestStack(ItemStack itemStack){
        return this.isChestItem(itemStack.getItem());
    }

    default boolean isChestItem(Item item){
        return item == this.getDefaultChestItem();
    }

    Item getDefaultChestItem();

    void openInventory(PlayerEntity player);

    boolean hasChest();

    void setChest(boolean p_110207_1_);

    int getInventoryColumns();
}
