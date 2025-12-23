package com.github.teamfusion.greedandbleed.data.worldgen.structure;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.StructureSet;

import java.util.concurrent.CompletableFuture;

public class StructureSetGenerator extends FabricDynamicRegistryProvider {
    public StructureSetGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(HolderLookup.Provider registries, Entries entries) {
        this.add(registries, entries, ModStructureSets.SHAMAN_BASE);
        this.add(registries, entries, ModStructureSets.PIGMY_ENCAMPMENT);
    }

    private void add(HolderLookup.Provider registries, Entries entries, ResourceKey<StructureSet> key) {
        HolderLookup.RegistryLookup<StructureSet> registry = registries.lookupOrThrow(Registries.STRUCTURE_SET);
        entries.add(key, registry.getOrThrow(key).value());
    }

    @Override
    public String getName() {
        return "worldgen/structure_set";
    }
}