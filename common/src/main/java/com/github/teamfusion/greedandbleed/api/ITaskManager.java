package com.github.teamfusion.greedandbleed.api;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.player.Player;

import java.util.Optional;

public interface ITaskManager<T extends LivingEntity & HasTaskManager> {
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
     * Handles when a Player interacts with this ITaskManager's mob
     * Call this in MobEntity#mobInteract
     * Inspired by PiglinTasks#mobInteract
     *
     * @param player The Player interacting with mob
     * @param hand   The Hand the player has used to interact
     * @return The ActionResultType outcome of this interaction
     */
    InteractionResult mobInteract(Player player, InteractionHand hand);

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