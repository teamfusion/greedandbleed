package com.github.teamfusion.greedandbleed.data;

import com.github.teamfusion.greedandbleed.common.registry.BiomeRegistry;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;

import java.util.concurrent.CompletableFuture;

public class BiomeGenerator extends FabricDynamicRegistryProvider {
    public BiomeGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }
    
    @Override
    protected void configure(HolderLookup.Provider registries, Entries entries) {
        this.add(registries, entries, BiomeRegistry.HOGDEW_HOLLOW);
    }
    
    private void add(HolderLookup.Provider registries, Entries entries, ResourceKey<Biome> key) {
        HolderLookup.RegistryLookup<Biome> registry = registries.lookupOrThrow(Registries.BIOME);
        entries.add(key, registry.getOrThrow(key).value());
    }
    
    @Override
    public String getName() {
        return "worldgen/biome";
    }
}