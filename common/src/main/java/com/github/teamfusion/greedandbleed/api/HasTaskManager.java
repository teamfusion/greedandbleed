package com.github.teamfusion.greedandbleed.api;

import com.mojang.serialization.Dynamic;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ItemStack;

/**
 * An interface for implementing an ITaskManager into a LivingEntity
 * @author Thelnfamous1
 */
public interface HasTaskManager {
    /**
     * Accessor for the ITaskManager instance associated with this IHasTaskManager instance
     * @return A ITaskManager instance variable
     */
    ITaskManager<?> getTaskManager();

    /**
     * Create a new ITaskManager instance
     * Note that this should only be called inside LivingEntity#makeBrain,
     * and should use the Brain instance returned from LivingEntity#brainProvider().makeBrain(dynamic)
     * where dynamic is the Dynamic instance passed into LivingEntity#makeBrain.
     * @param dynamic The Dynamic instance used by LivingEntity#brainProvider().makeBrain(dynamic)
     * @return A new ITaskManager instance
     */
    ITaskManager<?> createTaskManager(Dynamic<?> dynamic);

    /**
     * Plays a SoundEvent from this IHasTaskManager instance
     * This should be used in an implementation of ITaskManager#updateActivity
     * Inspired by PiglinEntity#playSound
     * @param soundEvent The SoundEvent to play
     */
    void playSound(SoundEvent soundEvent);

    /**
     * Handler for setting an ItemStack to the main hand
     * Useful for mobs that have specific behaviors for held items in the main hand
     * Inspired by PiglinEntity#holdInMainHand
     * @param stack The ItemStack to be held by the main hand
     */
    void holdInMainHand(ItemStack stack);

    /**
     * Handler for setting an ItemStack to the offhand
     * Useful for mobs that have specific behaviors for held items in the offhand
     * Inspired by PiglinEntity#holdInOffHand
     * @param stack The ItemStack to be held by the offhand
     */
    void holdInOffHand(ItemStack stack);
}