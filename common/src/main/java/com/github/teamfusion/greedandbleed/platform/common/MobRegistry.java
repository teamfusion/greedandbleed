package com.github.teamfusion.greedandbleed.platform.common;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;

import java.util.function.Supplier;

public class MobRegistry {
    @ExpectPlatform
    public static void attributes(Supplier<? extends EntityType<? extends LivingEntity>> type, Supplier<AttributeSupplier.Builder> builder) {
        throw new AssertionError();
    }
}