package com.github.teamfusion.greedandbleed.platform.fabric;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class EnvironmentImpl {
    public static CreativeModeTab createTab(ResourceLocation location, ItemStack icon) {
        return FabricItemGroup.builder(location).icon(() -> icon).build();
    }
}