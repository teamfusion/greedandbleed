package com.github.teamfusion.greedandbleed.data;

import com.github.teamfusion.greedandbleed.data.client.ModelGenerator;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class DataGeneration implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator gen) {
        gen.addProvider(ModelGenerator::new);

    }
}