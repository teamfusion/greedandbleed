package com.github.teamfusion.greedandbleed.data.worldgen.structure;

import com.github.teamfusion.greedandbleed.data.worldgen.structure.piece.ShamanBasePieces;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

import java.util.concurrent.CompletableFuture;

public class StructureTemplatePoolGenerator extends FabricDynamicRegistryProvider {
    public StructureTemplatePoolGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(HolderLookup.Provider registries, Entries entries) {
        this.add(registries, entries, ShamanBasePieces.SHAMAN_BASE);
    }

    private void add(HolderLookup.Provider registries, Entries entries, ResourceKey<StructureTemplatePool> key) {
        HolderLookup.RegistryLookup<StructureTemplatePool> registry = registries.lookupOrThrow(Registries.TEMPLATE_POOL);
        entries.add(key, registry.getOrThrow(key).value());
    }

    @Override
    public String getName() {
        return "worldgen/template_pool";
    }
}