package com.github.teamfusion.greedandbleed.fabric;

import com.github.teamfusion.greedandbleed.GreedAndBleed;
import com.github.teamfusion.greedandbleed.common.registry.BlockRegistry;
import com.github.teamfusion.greedandbleed.common.registry.PoiRegistry;
import com.google.common.collect.ImmutableSet;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.state.BlockState;

public class GreedAndBleedFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        GreedAndBleed.bootstrap();
        registerPoiBlockStates();
    }

    private static void registerPoiBlockStates() {
        PoiTypes.registerBlockStates(
            BuiltInRegistries.POINT_OF_INTEREST_TYPE.getHolderOrThrow(PoiRegistry.PYGMY_STATION),
            ImmutableSet.<BlockState>builder().addAll(BlockRegistry.PYGMY_STATION.get().getStateDefinition().getPossibleStates()).build()
        );
    }

    private static <T extends AbstractContainerMenu> MenuType<T> register(ResourceLocation string, MenuType menuSupplier) {
        return Registry.register(BuiltInRegistries.MENU, string, menuSupplier);
    }
}