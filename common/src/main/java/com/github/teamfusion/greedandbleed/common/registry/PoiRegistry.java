package com.github.teamfusion.greedandbleed.common.registry;

import com.github.teamfusion.greedandbleed.GreedAndBleed;
import com.github.teamfusion.greedandbleed.platform.CoreRegistry;
import com.google.common.collect.ImmutableSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.block.state.BlockState;

public class PoiRegistry {
    public static final CoreRegistry<PoiType> POI_TYPES = CoreRegistry.of(BuiltInRegistries.POINT_OF_INTEREST_TYPE, GreedAndBleed.MOD_ID);

    public static final ResourceKey<PoiType> PYGMY_STATION = POI_TYPES.resource("pygmy_station",
        () -> new PoiType(
            ImmutableSet.<BlockState>builder().addAll(BlockRegistry.PYGMY_STATION.get().getStateDefinition().getPossibleStates()).build(),
            1,
            1
        )
    );
}