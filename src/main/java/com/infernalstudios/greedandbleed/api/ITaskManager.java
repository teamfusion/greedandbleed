package com.infernalstudios.greedandbleed.api;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;

import java.util.Optional;

public interface ITaskManager<T extends LivingEntity & IHasTaskManager> {

    /**
     * Accessor for the Brain associated with this ITaskManager
     * @return dynamicBrain
     */
    Brain<T> getBrain();

    /**
     * Retrieves an Optional SoundEvent for the LivingEntity associated with this ITaskManager instance
     * Call this in LivingEntity#playAmbientSound and ITaskManager#updateActivity
     * Inspired by PiglinTasks#getSoundForCurrentActivity
     * @return An Optional SoundEvent for this TaskManager's mob
     */
    Optional<SoundEvent> getSoundForCurrentActivity();

    /**
     * Initializes specific MemoryModuleTypes for this ITaskManager's dynamicBrain
     * Inspired by PiglinTasks#initMemories
     * Call this in MobEntity#finalizeSpawn
     */
    void initMemories();

    /**
     * Handles when a PlayerEntity interacts with this ITaskManager's mob
     * Call this in MobEntity#mobInteract
     * Inspired by PiglinTasks#mobInteract
     *
     * @param player The PlayerEntity interacting with mob
     * @param hand   The Hand the player has used to interact
     * @return The ActionResultType outcome of this interaction
     */
    ActionResultType mobInteract(PlayerEntity player, Hand hand);

    /**
     * Handles updating the Activity of this ITaskManager's dynamicBrain
     * Inspired by PiglinTasks#updateActivity
     * Call this in MobEntity#updateServerAiStep
     */
    void updateActivity();

    /**
     * Call this in LivingEntity#hurt
     * Inspired by PiglinTasks#wasHurtBy
     * @param entity The LivingEntity that hurt  this TaskManager's mob
     */
    void wasHurtBy(LivingEntity entity);

    /**
     * Retrieves an Optional of the nearest valid attack target for this ITaskManager's mob
     * Call this in a static helper method supplied to FindNewAttackTargetTask
     * @return An Optional of a LivingEntity that is the nearest valid attack target for this ITaskManager's mob
     */
    Optional<? extends LivingEntity> findNearestValidAttackTarget();
}
