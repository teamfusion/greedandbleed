package com.github.teamfusion.greedandbleed.common.registry;

import com.github.teamfusion.greedandbleed.GreedAndBleed;
import com.github.teamfusion.greedandbleed.common.entity.piglin.*;
import com.github.teamfusion.greedandbleed.common.entity.piglin.pygmy.Hoggart;
import com.github.teamfusion.greedandbleed.common.entity.piglin.pygmy.Pygmy;
import com.github.teamfusion.greedandbleed.common.entity.piglin.pygmy.Shrygmy;
import com.github.teamfusion.greedandbleed.common.entity.projectile.ThrownDamageableEntity;
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
    public static final Supplier<EntityType<Zoglet>> ZOGLET = create("zoglet", EntityType.Builder.of(Zoglet::new, MobCategory.MONSTER).fireImmune().sized(0.6F, 0.55F));
    public static final Supplier<EntityType<Skoglet>> SKOGLET = create("skoglet", EntityType.Builder.of(Skoglet::new, MobCategory.MONSTER).sized(0.6F, 0.55F));
    public static final Supplier<EntityType<ShamanPiglin>> SHAMAN_PIGLIN = create("shaman_piglin", EntityType.Builder.of(ShamanPiglin::new, MobCategory.MONSTER).sized(0.6F, 1.95F));
    public static final Supplier<EntityType<Pygmy>> PYGMY = create("pygmy", EntityType.Builder.of(Pygmy::new, MobCategory.MONSTER).sized(0.6F, 1.55F));
    public static final Supplier<EntityType<Shrygmy>> SHRYGMY = create("shrygmy", EntityType.Builder.of(Shrygmy::new, MobCategory.MONSTER).sized(0.6F, 1.55F));
    public static final Supplier<EntityType<Hoggart>> HOGGART = create("hoggart", EntityType.Builder.of(Hoggart::new, MobCategory.MONSTER).sized(0.6F, 1.95F));
    public static final Supplier<EntityType<WarpedPiglin>> WARPED_PIGLIN = create("warped_piglin", EntityType.Builder.of(WarpedPiglin::new, MobCategory.MONSTER).sized(0.6F, 1.95F));
    public static final Supplier<EntityType<ThrownDamageableEntity>> THROWN_DAMAGEABLE = create("thrown_damageable_projectile", EntityType.Builder.<ThrownDamageableEntity>of(ThrownDamageableEntity::new, MobCategory.MISC).sized(0.3F, 0.3F));

    private static <T extends Entity> Supplier<EntityType<T>> create(String key, EntityType.Builder<T> builder) {
        return ENTITIES.create(key, () -> builder.build(key));
    }
}