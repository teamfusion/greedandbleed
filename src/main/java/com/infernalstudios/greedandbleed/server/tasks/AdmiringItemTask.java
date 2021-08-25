package com.infernalstudios.greedandbleed.server.tasks;

import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.world.server.ServerWorld;

import java.util.Optional;
import java.util.function.Predicate;

@SuppressWarnings("NullableProblems")
public class AdmiringItemTask<E extends LivingEntity> extends Task<E> {
   private final int admireDuration;
   private final Predicate<ItemEntity> itemPredicate;

   public AdmiringItemTask(int admireDurationIn, Predicate<ItemEntity> itemPredicateIn) {
      super(ImmutableMap.of(MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM, MemoryModuleStatus.VALUE_PRESENT, MemoryModuleType.ADMIRING_ITEM, MemoryModuleStatus.VALUE_ABSENT, MemoryModuleType.ADMIRING_DISABLED, MemoryModuleStatus.VALUE_ABSENT, MemoryModuleType.DISABLE_WALK_TO_ADMIRE_ITEM, MemoryModuleStatus.VALUE_ABSENT));
      this.admireDuration = admireDurationIn;
      this.itemPredicate = itemPredicateIn;
   }

   @Override
   protected boolean checkExtraStartConditions(ServerWorld serverWorld, E entity) {
      Optional<ItemEntity> itemOpt = entity.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM);
      return itemOpt.filter(itemPredicate).isPresent();

   }

   @Override
   protected void start(ServerWorld serverWorld, E entity, long gameTime) {
      entity.getBrain().setMemoryWithExpiry(MemoryModuleType.ADMIRING_ITEM, true, (long)this.admireDuration);
   }
}
