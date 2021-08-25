package com.infernalstudios.greedandbleed.server.registry;

import com.infernalstudios.greedandbleed.GreedAndBleed;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.monster.HoglinEntity;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Optional;

public class MemoryModuleTypeRegistry {

    public static final DeferredRegister<MemoryModuleType<?>> MEMORY_MODULE_TYPES = DeferredRegister.create(ForgeRegistries.MEMORY_MODULE_TYPES, GreedAndBleed.MOD_ID);

    public static final RegistryObject<MemoryModuleType<HoglinEntity>> NEAREST_VISIBLE_ADULT_HOGLIN = MEMORY_MODULE_TYPES.register(
            "nearest_visible_adult_hoglin", () ->
                    new MemoryModuleType<>(Optional.empty())
    );
}
