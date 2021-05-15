package com.infernalstudios.greedandbleed.common.registry;

import com.infernalstudios.greedandbleed.GreedAndBleed;
import com.infernalstudios.greedandbleed.common.entity.piglin.PigmyEntity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class EntityTypeRegistry {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, GreedAndBleed.MODID);

    public static final RegistryObject<EntityType<PigmyEntity>> PIGMY =
            ENTITY_TYPES.register("pigmy", () ->
                    EntityType.Builder.<PigmyEntity>of(PigmyEntity::new, EntityClassification.MONSTER)
                            // pigmy is 14x24 pixels, piglin is 16x31(?)
                            .sized(0.6F, 1.5F)
                            .clientTrackingRange(8)
                            .build(new ResourceLocation(GreedAndBleed.MODID, "pigmy").toString()));
}
