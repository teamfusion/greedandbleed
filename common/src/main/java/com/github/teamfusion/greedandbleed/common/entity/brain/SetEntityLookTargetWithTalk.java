package com.github.teamfusion.greedandbleed.common.entity.brain;

import com.github.teamfusion.greedandbleed.common.entity.piglin.Hoglet;
import com.github.teamfusion.greedandbleed.common.entity.piglin.pigmy.Hoggart;
import com.github.teamfusion.greedandbleed.common.entity.piglin.pigmy.Pigmy;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.behavior.OneShot;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
import net.minecraft.world.entity.monster.hoglin.Hoglin;

import java.util.Optional;
import java.util.function.Predicate;

public class SetEntityLookTargetWithTalk {
    public static BehaviorControl<LivingEntity> create(MobCategory mobCategory, float f) {
        return create(((livingEntity) -> mobCategory.equals(livingEntity.getType().getCategory())), f);
    }

    public static OneShot<LivingEntity> create(EntityType<?> entityType, float f) {
        return create(((livingEntity) -> entityType.equals(livingEntity.getType())), f);
    }

    public static OneShot<LivingEntity> create(float f) {
        return create(((livingEntity) -> true), f);
    }

    public static OneShot<LivingEntity> create(Predicate<LivingEntity> predicate, float f) {
        float g = f * f;
        return BehaviorBuilder.create((instance) -> instance.group(instance.absent(MemoryModuleType.LOOK_TARGET), instance.present(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES)).apply(instance, (memoryAccessor, memoryAccessor2) -> (serverLevel, livingEntity, l) -> {
            Optional<LivingEntity> optional = ((NearestVisibleLivingEntities) instance.get(memoryAccessor2)).findClosest(predicate.and((livingEntity2) -> livingEntity2.distanceToSqr(livingEntity) <= (double) g && !livingEntity.hasPassenger(livingEntity2)));
            if (optional.isEmpty()) {
                return false;
            } else {
                if (optional.get() instanceof Pigmy) {
                    serverLevel.broadcastEntityEvent(livingEntity, (byte) Pigmy.TALK_PIGMY_ANIMATION_ID);
                } else if (optional.get() instanceof Hoggart && !(livingEntity instanceof Hoggart)) {
                    serverLevel.broadcastEntityEvent(livingEntity, (byte) Pigmy.TALK_HOGGART_ANIMATION_ID);
                } else if (optional.get() instanceof Hoglet) {
                    serverLevel.broadcastEntityEvent(livingEntity, (byte) Pigmy.PET_HOGLET_ANIMATION_ID);
                } else if (optional.get() instanceof Hoglin) {
                    serverLevel.broadcastEntityEvent(livingEntity, (byte) Pigmy.PET_HOGLIN_ANIMATION_ID);
                }
                memoryAccessor.set(new EntityTracker((Entity) optional.get(), true));
                return true;
            }
        }));
    }
}
