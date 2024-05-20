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
        
        pack.addProvider(ConfiguredFeatureGenerator::new);
        pack.addProvider(PlacedFeatureGenerator::new);
        pack.addProvider(BiomeGenerator::new);
    }

    @Override
    public void buildRegistry(RegistrySetBuilder builder) {
        builder.add(Registries.CONFIGURED_FEATURE, ModConfiguredFeatures::bootstrap);
        builder.add(Registries.PLACED_FEATURE, ModPlacedFeatures::bootstrap);
        builder.add(Registries.BIOME, ModBiomes::bootstrap);
    }
}