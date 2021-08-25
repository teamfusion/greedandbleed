package com.infernalstudios.greedandbleed.server.sensor;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.infernalstudios.greedandbleed.api.PiglinTaskManager;
import com.infernalstudios.greedandbleed.server.registry.MemoryModuleTypeRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CampfireBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.monster.HoglinEntity;
import net.minecraft.entity.monster.WitherSkeletonEntity;
import net.minecraft.entity.monster.piglin.AbstractPiglinEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@SuppressWarnings("NullableProblems")
public class PigmySpecificSensor extends Sensor<LivingEntity> {
   @Override
   public Set<MemoryModuleType<?>> requires() {
      return ImmutableSet.of(
              MemoryModuleType.VISIBLE_LIVING_ENTITIES,
              MemoryModuleType.LIVING_ENTITIES,
              MemoryModuleType.NEAREST_VISIBLE_NEMESIS,
              MemoryModuleType.NEAREST_TARGETABLE_PLAYER_NOT_WEARING_GOLD,
              MemoryModuleType.NEAREST_PLAYER_HOLDING_WANTED_ITEM,
              MemoryModuleTypeRegistry.NEAREST_VISIBLE_ADULT_HOGLIN.get(),
              MemoryModuleType.NEAREST_VISIBLE_BABY_HOGLIN,
              MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLINS,
              MemoryModuleType.NEARBY_ADULT_PIGLINS,
              MemoryModuleType.VISIBLE_ADULT_PIGLIN_COUNT,
              MemoryModuleType.VISIBLE_ADULT_HOGLIN_COUNT,
              MemoryModuleType.NEAREST_REPELLENT);
   }

   @Override
   protected void doTick(ServerWorld world, LivingEntity sensorMob) {
      Brain<?> brain = sensorMob.getBrain();
      brain.setMemory(MemoryModuleType.NEAREST_REPELLENT, findNearestRepellent(world, sensorMob));
      Optional<MobEntity> nearVisibleNemesis = Optional.empty();
      Optional<HoglinEntity> nearVisibleAdultHoglin = Optional.empty();
      Optional<HoglinEntity> nearVisibleBabyHoglin = Optional.empty();
      Optional<LivingEntity> nearVisibleZombified = Optional.empty();
      Optional<PlayerEntity> nearTargetPlayerNoGold = Optional.empty();
      Optional<PlayerEntity> nearPlayerHoldingWanted = Optional.empty();
      int adultHoglinCounter = 0;
      List<AbstractPiglinEntity> nearVisibleAdultPiglins = Lists.newArrayList();
      List<AbstractPiglinEntity> nearAdultPiglins = Lists.newArrayList();

      for(LivingEntity visibleLiving : brain.getMemory(MemoryModuleType.VISIBLE_LIVING_ENTITIES).orElse(ImmutableList.of())) {
         if (visibleLiving instanceof HoglinEntity) {
            HoglinEntity hoglinentity = (HoglinEntity)visibleLiving;
            if (hoglinentity.isBaby() && !nearVisibleBabyHoglin.isPresent()) {
               nearVisibleBabyHoglin = Optional.of(hoglinentity);
            } else if (hoglinentity.isAdult()) {
               ++adultHoglinCounter;
               if (!nearVisibleAdultHoglin.isPresent()) {
                  nearVisibleAdultHoglin = Optional.of(hoglinentity);
               }
            }
         } else if (visibleLiving instanceof AbstractPiglinEntity) {
            AbstractPiglinEntity piglinentity = (AbstractPiglinEntity)visibleLiving;
            if (piglinentity.isAdult()) {
               nearVisibleAdultPiglins.add(piglinentity);
            }
         } else if (visibleLiving instanceof PlayerEntity) {
            PlayerEntity playerentity = (PlayerEntity)visibleLiving;
            if (!nearTargetPlayerNoGold.isPresent() && EntityPredicates.ATTACK_ALLOWED.test(visibleLiving)
                    //&& !PiglinTaskManager.isWearingGold(playerentity)
            ) {
               nearTargetPlayerNoGold = Optional.of(playerentity);
            }

            if (!nearPlayerHoldingWanted.isPresent() && !playerentity.isSpectator() && PiglinTaskManager.isPlayerHoldingLovedItem(playerentity)) {
               nearPlayerHoldingWanted = Optional.of(playerentity);
            }
         } else if (nearVisibleNemesis.isPresent() || !(visibleLiving instanceof WitherSkeletonEntity) && !(visibleLiving instanceof WitherEntity)) {
            if (!nearVisibleZombified.isPresent() && PiglinTaskManager.isZombified(visibleLiving)) {
               nearVisibleZombified = Optional.of(visibleLiving);
            }
         } else {
            nearVisibleNemesis = Optional.of((MobEntity)visibleLiving);
         }
      }

      for(LivingEntity nearbyLiving : brain.getMemory(MemoryModuleType.LIVING_ENTITIES).orElse(ImmutableList.of())) {
         if (nearbyLiving instanceof AbstractPiglinEntity && ((AbstractPiglinEntity)nearbyLiving).isAdult()) {
            nearAdultPiglins.add((AbstractPiglinEntity)nearbyLiving);
         }
      }

      brain.setMemory(MemoryModuleType.NEAREST_VISIBLE_NEMESIS, nearVisibleNemesis);
      brain.setMemory(MemoryModuleTypeRegistry.NEAREST_VISIBLE_ADULT_HOGLIN.get(), nearVisibleAdultHoglin);
      brain.setMemory(MemoryModuleType.NEAREST_VISIBLE_BABY_HOGLIN, nearVisibleBabyHoglin);
      brain.setMemory(MemoryModuleType.NEAREST_VISIBLE_ZOMBIFIED, nearVisibleZombified);
      brain.setMemory(MemoryModuleType.NEAREST_TARGETABLE_PLAYER_NOT_WEARING_GOLD, nearTargetPlayerNoGold);
      brain.setMemory(MemoryModuleType.NEAREST_PLAYER_HOLDING_WANTED_ITEM, nearPlayerHoldingWanted);
      brain.setMemory(MemoryModuleType.NEARBY_ADULT_PIGLINS, nearAdultPiglins);
      brain.setMemory(MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLINS, nearVisibleAdultPiglins);
      brain.setMemory(MemoryModuleType.VISIBLE_ADULT_PIGLIN_COUNT, nearVisibleAdultPiglins.size());
      brain.setMemory(MemoryModuleType.VISIBLE_ADULT_HOGLIN_COUNT, adultHoglinCounter);
   }

   private static Optional<BlockPos> findNearestRepellent(ServerWorld world, LivingEntity sensorMob) {
      return BlockPos.findClosestMatch(sensorMob.blockPosition(), 8, 4,
              (pos) -> isValidRepellent(world, pos));
   }

   private static boolean isValidRepellent(ServerWorld world, BlockPos blockPos) {
      BlockState blockstate = world.getBlockState(blockPos);
      boolean flag = blockstate.is(BlockTags.PIGLIN_REPELLENTS);
      return flag && blockstate.is(Blocks.SOUL_CAMPFIRE) ? CampfireBlock.isLitCampfire(blockstate) : flag;
   }
}
