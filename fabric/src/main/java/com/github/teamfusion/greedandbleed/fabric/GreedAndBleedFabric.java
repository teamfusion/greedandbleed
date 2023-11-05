package com.github.teamfusion.greedandbleed.fabric;

import com.github.teamfusion.greedandbleed.GreedAndBleed;
import com.github.teamfusion.greedandbleed.client.HoglinScreenHandler;
import com.github.teamfusion.greedandbleed.common.inventory.HoglinInventoryMenu;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public class GreedAndBleedFabric implements ModInitializer {
    public static final HoglinScreenHandler HOGLIN_HANDLER = new HoglinScreenHandler();

    public static final MenuType<HoglinInventoryMenu> HOGLIN_MENU_HANDLER = new ExtendedScreenHandlerType<>(HOGLIN_HANDLER);
    @Override
    public void onInitialize() {
        GreedAndBleed.bootstrap();
        register(new ResourceLocation(GreedAndBleed.MOD_ID, "hoglin_menu"), HOGLIN_MENU_HANDLER);
    }

    private static <T extends AbstractContainerMenu> MenuType<T> register(ResourceLocation string, MenuType menuSupplier) {
        return Registry.register(BuiltInRegistries.MENU, string, menuSupplier);
    }
}