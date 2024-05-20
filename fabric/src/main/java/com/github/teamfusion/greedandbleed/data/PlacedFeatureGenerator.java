package com.github.teamfusion.greedandbleed.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.concurrent.CompletableFuture;

public class PlacedFeatureGenerator extends FabricDynamicRegistryProvider {
    public PlacedFeatureGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }
    
    @Override
    protected void configure(HolderLookup.Provider registries, Entries entries) {
        this.add(registries, entries, ModPlacedFeatures.HOGDEW_BUBBLE);
        this.add(registries, entries, ModPlacedFeatures.HOGDEW_FUNGUS);
        this.add(registries, entries, ModPlacedFeatures.HOGDEW_LUMPS);
        this.add(registries, entries, ModPlacedFeatures.HOGDEW_PATCH);
        this.add(registries, entries, ModPlacedFeatures.HOGDEW_HOLLOW_VEGITATION);
    }
    
    private void add(HolderLookup.Provider registries, Entries entries, ResourceKey<PlacedFeature> key) {
        HolderLookup.RegistryLookup<PlacedFeature> registry = registries.lookupOrThrow(Registries.PLACED_FEATURE);
        entries.add(key, registry.getOrThrow(key).value());
    }
    
    @Override
    public String getName() {
        return "worldgen/placed_feature";
    }
}