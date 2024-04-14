package com.github.teamfusion.greedandbleed.common.registry;

import com.github.teamfusion.greedandbleed.GreedAndBleed;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;

public interface GBDamageSource {
    ResourceKey<DamageType> WARPED = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(GreedAndBleed.MOD_ID, "warped"));
    ResourceKey<DamageType> WARPED_INFECT = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(GreedAndBleed.MOD_ID, "warped_infect"));
    ResourceKey<DamageType> WARPED_LINK = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(GreedAndBleed.MOD_ID, "warped_link"));
}