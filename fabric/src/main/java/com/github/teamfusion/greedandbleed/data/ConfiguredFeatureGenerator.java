package com.github.teamfusion.greedandbleed.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

import java.util.concurrent.CompletableFuture;

public class ConfiguredFeatureGenerator extends FabricDynamicRegistryProvider {
    public ConfiguredFeatureGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }
    
    @Override
    protected void configure(HolderLookup.Provider registries, Entries entries) {
        this.add(registries, entries, ModConfiguredFeatures.HOGDEW_BUBBLE);
        this.add(registries, entries, ModConfiguredFeatures.HOGDEW_FUNGUS);
        this.add(registries, entries, ModConfiguredFeatures.HOGDEW_LUMPS);
        this.add(registries, entries, ModConfiguredFeatures.HOGDEW_HOLLOW_VEGITATION);
        this.add(registries, entries, ModConfiguredFeatures.HOGDEW_PATCH);
    }
    
    private void add(HolderLookup.Provider registries, Entries entries, ResourceKey<ConfiguredFeature<?, ?>> key) {
        HolderLookup.RegistryLookup<ConfiguredFeature<?, ?>> registry = registries.lookupOrThrow(Registries.CONFIGURED_FEATURE);
        entries.add(key, registry.getOrThrow(key).value());
    }
    
    @Override
    public String getName() {
        return "worldgen/configured_feature";
    }
}