package com.github.teamfusion.greedandbleed.api;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;

/**
 * An interface for mobs that have inventories
 * @author Thelnfamous1
 */
public interface HasInventory {
    /**
     * The id of a CompoundTag as an int, returned by its implementation of Tag#getId as a byte
     * Used in calls of CompoundTag#getList
     */
    int COMPOUND_NBT_ID = 10;

    /**
     * Accessor for the Inventory instance associated with this HasInventory instance
     * @return An Inventory instance variable
     */
    SimpleContainer getInventory();

    /**
     * Adds an ItemStack to the Inventory instance associated with this HasInventory instance
     * @param stack The ItemStack to add to the Inventory
     * @return The remainder of the ItemStack after adding as much of it as possible to the Inventory
     */
    default ItemStack addToInventory(ItemStack stack) {
        return this.getInventory().addItem(stack);
    }

    /**
     * Checks if an ItemStack can be added to the Inventory instance associated with this HasInventory instance
     * @param stack The ItemStack to check if it can be added to the Inventory
     * @return Whether the ItemStack can be added to the Inventory
     * Note that it will return true if the ItemStack can be partially added to the Inventory
     */
    default boolean canAddToInventory(ItemStack stack) {
        return this.getInventory().canAddItem(stack);
    }

    /**
     * Removes all items from the Inventory instance associated with this HasInventory instance
     * and applies a Consumer to each of them
     * Call this in LivingEntity#dropCustomDeathLoot and after Mob#convertTo
     * where consumer should be Entity#spawnAtLocation to spawn each stack as an ItemEntity
     * @param consumer The Consumer to apply to each ItemStack removed from the Inventory
     */
    default void removeAllItemsFromInventory(Consumer<? super ItemStack> consumer) {
        this.getInventory().removeAllItems().forEach(consumer);
    }

    /**
     * Call in LivingEntity#addAdditionalSaveData
     * @param compound The CompoundTag to write the inventory to
     */
    default void saveInventoryToTag(CompoundTag compound) {
        compound.put("Inventory", this.getInventory().createTag());
    }

    /**
     * Call in LivingEntity#readAdditionalSaveData
     * @param compound The CompoundTag to load the inventory from
     */
    default void loadInventoryFromTag(CompoundTag compound) {
        this.getInventory().fromTag(compound.getList("Inventory", COMPOUND_NBT_ID));
    }
}