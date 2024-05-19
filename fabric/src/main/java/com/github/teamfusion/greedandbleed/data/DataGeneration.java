package com.github.teamfusion.greedandbleed.data;

import com.github.teamfusion.greedandbleed.data.client.ModelGenerator;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.registries.VanillaRegistries;

public class DataGeneration implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator gen) {
        final FabricDataGenerator.Pack pack = gen.createPack();
        pack.addProvider(ModelGenerator::new);
        pack.addProvider(RecipeGenerator::new);
        BlockTagGenerator blockTagGenerator = pack.addProvider(BlockTagGenerator::new);

        pack.addProvider((output, registriesFuture) -> new ItemTagGenerator(output, registriesFuture, blockTagGenerator));

    }

    @Override
    public void buildRegistry(RegistrySetBuilder registryBuilder) {
        DataGeneratorEntrypoint.super.buildRegistry(registryBuilder);
        registryBuilder.add(Registries.CONFIGURED_FEATURE, ModConfiguredFeatures::bootstrap);
        registryBuilder.add(Registries.PLACED_FEATURE, ModPlacedFeatures::bootstrap);
        registryBuilder.add(Registries.BIOME, ModBiomes::bootstrap);
        registryBuilder.buildPatch(RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY), VanillaRegistries.createLookup());
    }
}