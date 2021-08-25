package com.infernalstudios.greedandbleed.server.tasks;

import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.world.server.ServerWorld;

import java.util.Optional;
import java.util.function.Function;

@SuppressWarnings({ "NullableProblems", "unused" })
public class FinishHuntingTask<E extends LivingEntity> extends Task<E> {
   private final EntityType<?> huntType;
   private final Function<E, Void> finishFunction;

   public FinishHuntingTask(EntityType<?> huntTypeIn, Function<E, Void> finishFunctionIn) {
      super(ImmutableMap.of(MemoryModuleType.ATTACK_TARGET, MemoryModuleStatus.VALUE_PRESENT, MemoryModuleType.HUNTED_RECENTLY, MemoryModuleStatus.REGISTERED));
      this.huntType = huntTypeIn;
      this.finishFunction = finishFunctionIn;
   }

   @Override
   protected void start(ServerWorld serverWorld, E entity, long gameTime) {
      if (this.isAttackTargetDead(entity)) {
         this.finishFunction.apply(entity);
      }
   }

   protected boolean isAttackTargetDead(E entity) {
      Optional<LivingEntity> livingEntityOpt = entity.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET);
      if (livingEntityOpt.isPresent()) {
         LivingEntity livingEntity = livingEntityOpt.get();
         return livingEntity.getType() == this.huntType && livingEntity.isDeadOrDying();
      }

      return false;
   }
}
