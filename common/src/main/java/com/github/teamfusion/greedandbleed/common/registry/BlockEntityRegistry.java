package com.github.teamfusion.greedandbleed.common.registry;

import com.github.teamfusion.greedandbleed.GreedAndBleed;
import com.github.teamfusion.greedandbleed.common.block.PygmyStationBlockEntity;
import com.github.teamfusion.greedandbleed.platform.CoreRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public class BlockEntityRegistry {
    public static final CoreRegistry<BlockEntityType<?>> BLOCK_ENTITIES = CoreRegistry.of(BuiltInRegistries.BLOCK_ENTITY_TYPE, GreedAndBleed.MOD_ID);

    public static final Supplier<BlockEntityType<PygmyStationBlockEntity>> PYGMY_STATION = BLOCK_ENTITIES.create(
        "pygmy_station",
        () -> BlockEntityType.Builder.of(
            PygmyStationBlockEntity::new,
            BlockRegistry.PYGMY_STATION.get()
        ).build(null)
    );
}
