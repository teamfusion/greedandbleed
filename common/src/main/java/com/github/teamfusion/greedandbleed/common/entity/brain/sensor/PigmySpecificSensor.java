package com.github.teamfusion.greedandbleed.common.entity.brain.sensor;

import com.github.teamfusion.greedandbleed.common.entity.piglin.Hoglet;
import com.github.teamfusion.greedandbleed.common.entity.piglin.pigmy.GBPigmy;
import com.github.teamfusion.greedandbleed.common.entity.piglin.pigmy.Hoggart;
import com.github.teamfusion.greedandbleed.common.entity.piglin.pigmy.Pigmy;
import com.github.teamfusion.greedandbleed.common.registry.MemoryRegistry;
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

public class PigmySpecificSensor extends Sensor<LivingEntity> {
    @Override
    public Set<MemoryModuleType<?>> requires() {
        return ImmutableSet.of(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryModuleType.NEAREST_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_NEMESIS, MemoryRegistry.NEAREST_VISIBLE_ADULT_PYGMYS.get(), MemoryRegistry.NEARBY_ADULT_PYGMYS.get(), MemoryRegistry.NEAREST_HOGLET.get(), MemoryRegistry.NEAREST_TAMED_HOGLET.get());
    }

    @Override
    protected void doTick(ServerLevel serverLevel, LivingEntity livingEntity2) {
        Brain<?> brain = livingEntity2.getBrain();
        Optional<Hoglet> optional = Optional.empty();

        Optional<Hoglet> optional3 = Optional.empty();
        Optional<Mob> optional2 = Optional.empty();
        int i = 0;
        ArrayList<GBPigmy> list = Lists.newArrayList();
        ArrayList<GBPigmy> list2 = Lists.newArrayList();
        NearestVisibleLivingEntities nearestVisibleLivingEntities = brain.getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES).orElse(NearestVisibleLivingEntities.empty());
        for (LivingEntity livingEntity22 : nearestVisibleLivingEntities.findAll(livingEntity -> true)) {


            if ((livingEntity22 instanceof WitherSkeleton || livingEntity22 instanceof WitherBoss || livingEntity22 instanceof AbstractPiglin && !livingEntity22.isBaby())) {
                optional2 = Optional.of((Mob) livingEntity22);
                continue;
            }
        }
        List<LivingEntity> list3 = brain.getMemory(MemoryModuleType.NEAREST_LIVING_ENTITIES).orElse(ImmutableList.of());
        for (LivingEntity livingEntity3 : list3) {
            if (livingEntity3 instanceof Hoglet hoglet) {

                optional = Optional.of(hoglet);
                if (hoglet.isTame()) {
                    optional3 = Optional.of(hoglet);
                }
            }

            if (livingEntity2 instanceof Pigmy pigmy && livingEntity3 instanceof Hoggart hoggart && pigmy.getControlledVehicle() == null && !hoggart.hasControllingPassenger() && pigmy.isAggressive() && hoggart.isAggressive()) {
                brain.setMemory(MemoryModuleType.RIDE_TARGET, hoggart);
            }

            GBPigmy abstractPiglin;
            if (!(livingEntity3 instanceof GBPigmy) || !(abstractPiglin = (GBPigmy) livingEntity3).isAdult()) continue;
            list2.add(abstractPiglin);
        }
        brain.setMemory(MemoryRegistry.NEAREST_HOGLET.get(), optional);
        brain.setMemory(MemoryRegistry.NEAREST_TAMED_HOGLET.get(), optional3);
        brain.setMemory(MemoryModuleType.NEAREST_VISIBLE_NEMESIS, optional2);
        brain.setMemory(MemoryRegistry.NEARBY_ADULT_PYGMYS.get(), list2);
        brain.setMemory(MemoryRegistry.NEAREST_VISIBLE_ADULT_PYGMYS.get(), list);
    }
}

