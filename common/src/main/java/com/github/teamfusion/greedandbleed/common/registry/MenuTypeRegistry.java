package com.github.teamfusion.greedandbleed.common.registry;

import com.github.teamfusion.greedandbleed.GreedAndBleed;
import com.github.teamfusion.greedandbleed.common.inventory.PygmyArmorStandMenu;
import com.github.teamfusion.greedandbleed.common.inventory.PygmyStationMenu;
import com.github.teamfusion.greedandbleed.platform.CoreRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;

import java.util.function.Supplier;

public class MenuTypeRegistry {
    public static final CoreRegistry<MenuType<?>> MENU_TYPE = CoreRegistry.of(BuiltInRegistries.MENU, GreedAndBleed.MOD_ID);

    public static final Supplier<MenuType<PygmyStationMenu>> PYGMY_STATION = MENU_TYPE.create("pygmy_station", () -> new MenuType<>(PygmyStationMenu::new, FeatureFlags.VANILLA_SET));
    public static final Supplier<MenuType<PygmyArmorStandMenu>> PYGMY_ARMOR_STAND = MENU_TYPE.create("pygmy_armor_stand", () -> new MenuType<>(PygmyArmorStandMenu::new, FeatureFlags.VANILLA_SET));
}
