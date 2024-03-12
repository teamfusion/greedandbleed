package com.github.teamfusion.greedandbleed.common.registry;

import com.github.teamfusion.greedandbleed.GreedAndBleed;
import com.github.teamfusion.greedandbleed.common.entity.piglin.GBPygmy;
import com.github.teamfusion.greedandbleed.common.entity.piglin.Hoglet;
import com.github.teamfusion.greedandbleed.platform.CoreRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class MemoryRegistry {
    public static final CoreRegistry<MemoryModuleType<?>> MEMORY_MODULE_TYPES = CoreRegistry.of(BuiltInRegistries.MEMORY_MODULE_TYPE, GreedAndBleed.MOD_ID);

    public static final Supplier<MemoryModuleType<Hoglet>> NEAREST_HOGLET = MEMORY_MODULE_TYPES.create("nearest_hoglet", () -> new MemoryModuleType<>(Optional.empty()));
    public static final Supplier<MemoryModuleType<Hoglet>> NEAREST_TAMED_HOGLET = MEMORY_MODULE_TYPES.create("nearest_tamed_hoglet", () -> new MemoryModuleType<>(Optional.empty()));
    public static final Supplier<MemoryModuleType<List<GBPygmy>>> NEARBY_ADULT_PYGMYS = MEMORY_MODULE_TYPES.create("nearby_adult_pygmys", () -> new MemoryModuleType<>(Optional.empty()));
    public static final Supplier<MemoryModuleType<List<GBPygmy>>> NEAREST_VISIBLE_ADULT_PYGMYS = MEMORY_MODULE_TYPES.create("nearest_visible_adult_pygmys", () -> new MemoryModuleType<>(Optional.empty()));


}
