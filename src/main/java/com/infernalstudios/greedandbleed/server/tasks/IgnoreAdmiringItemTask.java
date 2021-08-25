package com.infernalstudios.greedandbleed.server.tasks;

import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.world.server.ServerWorld;

import java.util.Optional;
import java.util.function.Predicate;

@SuppressWarnings("NullableProblems")
public class IgnoreAdmiringItemTask<E extends LivingEntity> extends Task<E> {
   private final int maxTimeToReachItem;
   private final int disableTime;
   private final Predicate<E> extraStartPredicate;

   public IgnoreAdmiringItemTask(int maxTimeToReachItemIn, int disableTimeIn, Predicate<E> extraStartPredicateIn) {
      super(ImmutableMap.of(MemoryModuleType.ADMIRING_ITEM, MemoryModuleStatus.VALUE_PRESENT, MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM, MemoryModuleStatus.VALUE_PRESENT, MemoryModuleType.TIME_TRYING_TO_REACH_ADMIRE_ITEM, MemoryModuleStatus.REGISTERED, MemoryModuleType.DISABLE_WALK_TO_ADMIRE_ITEM, MemoryModuleStatus.REGISTERED));
      this.maxTimeToReachItem = maxTimeToReachItemIn;
      this.disableTime = disableTimeIn;
      this.extraStartPredicate = extraStartPredicateIn;
   }

   @Override
   protected boolean checkExtraStartConditions(ServerWorld serverWorld, E entity) {
      return this.extraStartPredicate.test(entity);
   }

   @Override
   protected void start(ServerWorld serverWorld, E entity, long gameTime) {
      Brain<?> brain = entity.getBrain();
      Optional<Integer> optional = brain.getMemory(MemoryModuleType.TIME_TRYING_TO_REACH_ADMIRE_ITEM);
      if (!optional.isPresent()) {
         brain.setMemory(MemoryModuleType.TIME_TRYING_TO_REACH_ADMIRE_ITEM, 0);
      } else {
         int i = optional.get();
         if (i > this.maxTimeToReachItem) {
            brain.eraseMemory(MemoryModuleType.ADMIRING_ITEM);
            brain.eraseMemory(MemoryModuleType.TIME_TRYING_TO_REACH_ADMIRE_ITEM);
            brain.setMemoryWithExpiry(MemoryModuleType.DISABLE_WALK_TO_ADMIRE_ITEM, true, this.disableTime);
         } else {
            brain.setMemory(MemoryModuleType.TIME_TRYING_TO_REACH_ADMIRE_ITEM, i + 1);
         }
      }

   }
}
