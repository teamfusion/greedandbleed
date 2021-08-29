package com.github.teamfusion.greedandbleed.common.registry;

import com.github.teamfusion.greedandbleed.GreedAndBleed;
import com.github.teamfusion.greedandbleed.common.entity.piglin.SkeletalPiglinEntity;
import com.github.teamfusion.greedandbleed.common.entity.piglin.PigmyEntity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class EntityTypeRegistry {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, GreedAndBleed.MOD_ID);

    public static final EntityType<PigmyEntity> PIGMY_TYPE = EntityType.Builder.of(PigmyEntity::new, EntityClassification.MONSTER)
            // pigmy is 14x24 pixels, piglin is 16x31(?)
            .sized(0.6F, 1.5F)
            .clientTrackingRange(8)
            .build(new ResourceLocation(GreedAndBleed.MOD_ID, "pigmy").toString());
    public static final RegistryObject<EntityType<PigmyEntity>> PIGMY = ENTITY_TYPES.register("pigmy", () -> PIGMY_TYPE);

    public static final EntityType<SkeletalPiglinEntity> SKELETAL_PIGLIN_TYPE = EntityType.Builder.of(SkeletalPiglinEntity::new, EntityClassification.MONSTER)
            .sized(0.6f, 1.95f) // Hitbox Size
            .build(new ResourceLocation(GreedAndBleed.MOD_ID, "skeletal_piglin").toString());
    public static final RegistryObject<EntityType<SkeletalPiglinEntity>> SKELETAL_PIGLIN = ENTITY_TYPES.register("skeletal_piglin",
            () -> SKELETAL_PIGLIN_TYPE);
}
