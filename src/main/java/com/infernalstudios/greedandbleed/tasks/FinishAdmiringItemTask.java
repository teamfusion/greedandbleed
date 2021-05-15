package com.infernalstudios.greedandbleed.tasks;

import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.world.server.ServerWorld;

import java.util.function.Function;
import java.util.function.Predicate;

public class FinishAdmiringItemTask<E extends LivingEntity> extends Task<E> {
   private final Function<E, Void> finishFunction;
   private final Predicate<E> extraStartPredicate;
   public FinishAdmiringItemTask(Function<E, Void> finishFunctionIn, Predicate<E> extraStartPredicateIn) {
      super(ImmutableMap.of(MemoryModuleType.ADMIRING_ITEM, MemoryModuleStatus.VALUE_ABSENT));
      this.finishFunction = finishFunctionIn;
      this.extraStartPredicate = extraStartPredicateIn;
   }

   protected boolean checkExtraStartConditions(ServerWorld serverWorld, E entity) {
      return this.extraStartPredicate.test(entity);
   }

   protected void start(ServerWorld serverWorld, E entity, long gameTime) {
      this.finishFunction.apply(entity);
   }
}