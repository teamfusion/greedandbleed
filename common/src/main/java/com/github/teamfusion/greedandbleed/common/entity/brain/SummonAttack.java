package com.github.teamfusion.greedandbleed.common.entity.brain;

import com.github.teamfusion.greedandbleed.common.entity.piglin.ShamanPiglin;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.behavior.OneShot;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;

public class SummonAttack {


    public static <T> OneShot<ShamanPiglin> create() {
        return BehaviorBuilder.create(instance -> instance.group(instance.present(MemoryModuleType.ATTACK_TARGET), instance.registered(MemoryModuleType.LOOK_TARGET)).apply(instance, (memoryAccessor, memoryAccessor2) -> (serverLevel, pathfinderMob, l) -> {

            if (pathfinderMob.getWave() > 5 || pathfinderMob.summonCooldown > 0) {
                return false;
            }
            LivingEntity living = instance.get(memoryAccessor);
            memoryAccessor2.set(new EntityTracker(living, true));
            pathfinderMob.summon(living);
            return true;
        }));
    }
}