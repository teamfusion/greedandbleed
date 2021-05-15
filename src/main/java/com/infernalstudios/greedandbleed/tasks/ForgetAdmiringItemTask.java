package com.infernalstudios.greedandbleed.tasks;

import com.google.common.collect.ImmutableMap;
import java.util.Optional;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.world.server.ServerWorld;

public class ForgetAdmiringItemTask<E extends LivingEntity> extends Task<E> {
   private final int maxDistanceToItem;

   public ForgetAdmiringItemTask(int maxDistanceToItemIn) {
      super(ImmutableMap.of(MemoryModuleType.ADMIRING_ITEM, MemoryModuleStatus.VALUE_PRESENT, MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM, MemoryModuleStatus.REGISTERED));
      this.maxDistanceToItem = maxDistanceToItemIn;
   }

   protected boolean checkExtraStartConditions(ServerWorld serverWorld, E entity) {
      if (!entity.getOffhandItem().isEmpty()) {
         return false;
      } else {
         Optional<ItemEntity> optional = entity.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM);
         return optional.map(itemEntity -> !itemEntity.closerThan(entity, (double) this.maxDistanceToItem)).orElse(true);
      }
   }

   protected void start(ServerWorld serverWorld, E entity, long gameTime) {
      entity.getBrain().eraseMemory(MemoryModuleType.ADMIRING_ITEM);
   }
}