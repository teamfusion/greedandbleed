package com.github.teamfusion.greedandbleed.fabric;

import com.github.teamfusion.greedandbleed.GreedAndBleed;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public class GreedAndBleedFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        GreedAndBleed.bootstrap();
    }

    private static <T extends AbstractContainerMenu> MenuType<T> register(ResourceLocation string, MenuType menuSupplier) {
        return Registry.register(BuiltInRegistries.MENU, string, menuSupplier);
    }
}