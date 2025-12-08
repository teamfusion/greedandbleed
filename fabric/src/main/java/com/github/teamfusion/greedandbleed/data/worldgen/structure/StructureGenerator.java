package com.github.teamfusion.greedandbleed.data.worldgen.structure;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.Structure;

import java.util.concurrent.CompletableFuture;

public class StructureGenerator extends FabricDynamicRegistryProvider {
    public StructureGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(HolderLookup.Provider registries, Entries entries) {
        this.add(registries, entries, ModStructures.SHAMAN_BASE);
    }

    private void add(HolderLookup.Provider registries, Entries entries, ResourceKey<Structure> key) {
        HolderLookup.RegistryLookup<Structure> registry = registries.lookupOrThrow(Registries.STRUCTURE);
        entries.add(key, registry.getOrThrow(key).value());
    }

    @Override
    public String getName() {
        return "worldgen/structure";
    }
}