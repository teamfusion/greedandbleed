package com.github.teamfusion.greedandbleed.common.entity.brain.sensor;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class WarpedPiglinSpecificSensor extends Sensor<LivingEntity> {
    @Override
    public Set<MemoryModuleType<?>> requires() {
        return ImmutableSet.of(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryModuleType.NEAREST_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_NEMESIS, MemoryModuleType.NEARBY_ADULT_PIGLINS, MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLINS);
    }

    @Override
    protected void doTick(ServerLevel serverLevel, LivingEntity livingEntity2) {
        Brain<?> brain = livingEntity2.getBrain();
        Optional<Mob> optional2 = Optional.empty();
        int i = 0;
        ArrayList<AbstractPiglin> list = Lists.newArrayList();
        ArrayList<AbstractPiglin> list2 = Lists.newArrayList();
        NearestVisibleLivingEntities nearestVisibleLivingEntities = brain.getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES).orElse(NearestVisibleLivingEntities.empty());
        for (LivingEntity livingEntity22 : nearestVisibleLivingEntities.findAll(livingEntity -> true)) {

            if ((livingEntity22 instanceof WitherSkeleton || livingEntity22 instanceof WitherBoss)) {
                optional2 = Optional.of((Mob) livingEntity22);
                continue;
            }
        }
        List<LivingEntity> list3 = brain.getMemory(MemoryModuleType.NEAREST_LIVING_ENTITIES).orElse(ImmutableList.of());
        for (LivingEntity livingEntity3 : list3) {
            AbstractPiglin abstractPiglin;
            if (!(livingEntity3 instanceof AbstractPiglin) || !(abstractPiglin = (AbstractPiglin) livingEntity3).isAdult())
                continue;
            list2.add(abstractPiglin);
        }
        brain.setMemory(MemoryModuleType.NEAREST_VISIBLE_NEMESIS, optional2);
        brain.setMemory(MemoryModuleType.NEARBY_ADULT_PIGLINS, list2);
        brain.setMemory(MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLINS, list);
    }
}

