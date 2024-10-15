package com.github.teamfusion.greedandbleed.common.registry;

import com.github.teamfusion.greedandbleed.GreedAndBleed;
import com.github.teamfusion.greedandbleed.common.block.PygmyStationBlockEntity;
import com.github.teamfusion.greedandbleed.platform.CoreRegistry;
import com.mojang.datafixers.types.Type;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public class BlockEntityRegistry {
    public static final CoreRegistry<BlockEntityType<?>> BLOCK_ENTITIES = CoreRegistry.of(BuiltInRegistries.BLOCK_ENTITY_TYPE, GreedAndBleed.MOD_ID);

    public static final Supplier<BlockEntityType<PygmyStationBlockEntity>> PYGMY_STATION = register("pygmy_station", BlockEntityType.Builder.of(PygmyStationBlockEntity::new, BlockRegistry.PYGMY_STATION.get()));

    private static <T extends BlockEntity> Supplier<BlockEntityType<T>> register(String string, BlockEntityType.Builder<T> builder) {
        Type<?> type = Util.fetchChoiceType(References.BLOCK_ENTITY, string);
        return BLOCK_ENTITIES.create(string, () -> builder.build(type));
    }
}
