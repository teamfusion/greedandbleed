package com.github.teamfusion.greedandbleed.data;

import com.github.teamfusion.greedandbleed.data.client.ModelGenerator;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class DataGeneration implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator gen) {
        final FabricDataGenerator.Pack pack = gen.createPack();
        pack.addProvider(ModelGenerator::new);
        pack.addProvider(RecipeGenerator::new);
        BlockTagGenerator blockTagGenerator = pack.addProvider(BlockTagGenerator::new);

        pack.addProvider((output, registriesFuture) -> new ItemTagGenerator(output, registriesFuture, blockTagGenerator));
    }
}