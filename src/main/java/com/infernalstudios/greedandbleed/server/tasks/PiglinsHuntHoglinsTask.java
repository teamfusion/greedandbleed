package com.infernalstudios.greedandbleed.server.tasks;

import com.google.common.collect.ImmutableMap;
import com.infernalstudios.greedandbleed.api.PiglinTaskManager;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.monster.HoglinEntity;
import net.minecraft.entity.monster.piglin.AbstractPiglinEntity;
import net.minecraft.world.server.ServerWorld;

public class PiglinsHuntHoglinsTask<E extends AbstractPiglinEntity> extends Task<E> {
   public PiglinsHuntHoglinsTask() {
      super(ImmutableMap.of(MemoryModuleType.NEAREST_VISIBLE_HUNTABLE_HOGLIN, MemoryModuleStatus.VALUE_PRESENT, MemoryModuleType.ANGRY_AT, MemoryModuleStatus.VALUE_ABSENT, MemoryModuleType.HUNTED_RECENTLY, MemoryModuleStatus.VALUE_ABSENT, MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLINS, MemoryModuleStatus.REGISTERED));
   }

   protected boolean checkExtraStartConditions(ServerWorld serverWorld, E entity) {
      return !entity.isBaby() && !PiglinTaskManager.hasAnyoneNearbyHuntedRecently(entity);
   }

   protected void start(ServerWorld serverWorld, E entity, long gameTime) {
      HoglinEntity hoglinentity = entity.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_HUNTABLE_HOGLIN).get();
      PiglinTaskManager.setAngerTarget(entity, hoglinentity);
      PiglinTaskManager.dontKillAnyMoreHoglinsForAWhile(entity);
      PiglinTaskManager.broadcastAngerTarget(entity, hoglinentity);
      PiglinTaskManager.broadcastDontKillAnyMoreHoglinsForAWhile(entity);
   }
}