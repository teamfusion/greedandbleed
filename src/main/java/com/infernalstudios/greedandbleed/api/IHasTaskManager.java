package com.infernalstudios.greedandbleed.api;


import com.mojang.serialization.Dynamic;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;

/**
 * An interface for implementing an ITaskMaster into a LivingEntity
 * @author Thelnfamous1
 */
public interface IHasTaskManager {
    /**
     * Accessor for the ITaskMaster instance associated with this IHasTaskMaster instance
     * @return A ITaskMaster instance variable
     */
    ITaskManager<?> getTaskManager();

    /**
     * Create a new ITaskMaster instance
     * Note that this should only be called inside LivingEntity#makeBrain,
     * and should use the Brain instance returned from LivingEntity#brainProvider().makeBrain(dynamic)
     * where dynamic is the Dynamic instance passed into LivingEntity#makeBrain.
     * @param dynamic The Dynamic instance used by LivingEntity#brainProvider().makeBrain(dynamic)
     * @return A new ITaskMaster instance
     */
    ITaskManager<?> createTaskManager(Dynamic<?> dynamic);

    /**
     * Plays a SoundEvent from this IHasTaskMaster instance
     * This should be used in an implementation of ITaskMaster#updateActivity
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
     * Handler for setting an ItemStack to the off hand
     * Useful for mobs that have specific behaviors for held items in the off hand
     * Inspired by PiglinEntity#holdInOffHand
     * @param stack The ItemStack to be held by the off hand
     */
    void holdInOffHand(ItemStack stack);
}
