package com.github.teamfusion.greedandbleed.common.registry;

import com.github.teamfusion.greedandbleed.GreedAndBleed;
import com.github.teamfusion.greedandbleed.common.entity.piglin.Hoglet;
import com.github.teamfusion.greedandbleed.common.entity.piglin.Skelet;
import com.github.teamfusion.greedandbleed.common.entity.piglin.SkeletalPiglin;
import com.github.teamfusion.greedandbleed.common.entity.piglin.Zoglet;
import com.github.teamfusion.greedandbleed.platform.CoreRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

import java.util.function.Supplier;

public class EntityTypeRegistry {
    public static final CoreRegistry<EntityType<?>> ENTITIES = CoreRegistry.of(BuiltInRegistries.ENTITY_TYPE, GreedAndBleed.MOD_ID);

    public static final Supplier<EntityType<SkeletalPiglin>> SKELETAL_PIGLIN = create("skeletal_piglin", EntityType.Builder.of(SkeletalPiglin::new, MobCategory.MONSTER).sized(0.6F, 1.95F));
    public static final Supplier<EntityType<Hoglet>> HOGLET = create("hoglet", EntityType.Builder.of(Hoglet::new, MobCategory.MONSTER).sized(0.6F, 0.55F));
    public static final Supplier<EntityType<Zoglet>> ZOGLET = create("zoglet", EntityType.Builder.of(Zoglet::new, MobCategory.MONSTER).sized(0.6F, 0.55F));
    public static final Supplier<EntityType<Skelet>> SKELET = create("skelet", EntityType.Builder.of(Skelet::new, MobCategory.MONSTER).sized(0.6F, 0.55F));

    private static <T extends Entity> Supplier<EntityType<T>> create(String key, EntityType.Builder<T> builder) {
        return ENTITIES.create(key, () -> builder.build(key));
    }
}