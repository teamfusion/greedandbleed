package com.github.teamfusion.greedandbleed.data.client;

import com.github.teamfusion.greedandbleed.platform.common.MobRegistry;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.model.ModelLocationUtils;

public class ModelGenerator extends FabricModelProvider {
    public ModelGenerator(FabricDataOutput dataGenerator) {
        super(dataGenerator);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators gen) {
        MobRegistry.eggs().forEach(item -> gen.delegateItemModel(item, ModelLocationUtils.decorateItemModelLocation("template_spawn_egg")));
    }

    @Override
    public void generateItemModels(ItemModelGenerators gen) {

    }
}