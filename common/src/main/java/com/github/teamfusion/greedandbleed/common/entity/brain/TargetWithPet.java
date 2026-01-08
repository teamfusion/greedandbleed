package com.github.teamfusion.greedandbleed.common.entity.brain;

import com.github.teamfusion.greedandbleed.common.registry.MemoryRegistry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.behavior.OneShot;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;

import java.util.Optional;
import java.util.function.Predicate;

public class TargetWithPet {
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
        return BehaviorBuilder.create((instance) -> instance.group(instance.absent(MemoryRegistry.PET_TARGET.get()), instance.present(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES), instance.absent(MemoryRegistry.PET_COOLDOWN.get())).apply(instance, (memoryAccessor, memoryAccessor2, memoryAccessor3) -> (serverLevel, livingEntity, l) -> {
            Optional<LivingEntity> optional = ((NearestVisibleLivingEntities) instance.get(memoryAccessor2)).findClosest(predicate.and((livingEntity2) -> livingEntity2.distanceToSqr(livingEntity) <= (double) g && !livingEntity.hasPassenger(livingEntity2)));
            if (optional.isEmpty()) {
                return false;
            } else {
                memoryAccessor.set(optional.get());
                return true;
            }
        }));
    }
}
