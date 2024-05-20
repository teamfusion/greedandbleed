package com.github.teamfusion.greedandbleed.data.client;

import com.github.teamfusion.greedandbleed.common.registry.BlockRegistry;
import com.github.teamfusion.greedandbleed.common.registry.ItemRegistry;
import com.github.teamfusion.greedandbleed.platform.common.MobRegistry;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.data.models.model.TexturedModel;

public class ModelGenerator extends FabricModelProvider {
    public ModelGenerator(FabricDataOutput dataGenerator) {
        super(dataGenerator);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators gen) {
        MobRegistry.eggs().forEach(item -> gen.delegateItemModel(item, ModelLocationUtils.decorateItemModelLocation("template_spawn_egg")));
        gen.createAxisAlignedPillarBlock(BlockRegistry.HOGDEW_STEM.get(), TexturedModel.COLUMN);
        gen.createAxisAlignedPillarBlock(BlockRegistry.STRIPPED_HOGDEW_STEM.get(), TexturedModel.COLUMN);
        gen.createTrivialCube(BlockRegistry.HOGDEW_CLUSTER.get());
        gen.createCrossBlock(BlockRegistry.HOGDEW_FUNGUS.get(), BlockModelGenerators.TintState.NOT_TINTED);

        gen.createTrivialCube(BlockRegistry.HOGDEW_WART_BLOCK.get());
        gen.createDoor(BlockRegistry.HOGDEW_DOOR.get());
        gen.createRotatedMirroredVariantBlock(BlockRegistry.HOGDEW_LUMPS.get());
        gen.createTrapdoor(BlockRegistry.HOGDEW_TRAPDOOR.get());
        gen.family(BlockRegistry.HOGDEW_PLANKS.get()).stairs(BlockRegistry.HOGDEW_PLANKS_STAIRS.get()).slab(BlockRegistry.HOGDEW_PLANKS_SLAB.get()).fenceGate(BlockRegistry.HOGDEW_FENCE_GATE.get()).fence(BlockRegistry.HOGDEW_FENCE.get());
    }
    @Override
    public void generateItemModels(ItemModelGenerators gen) {
        gen.generateFlatItem(ItemRegistry.CLUB.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        gen.generateFlatItem(ItemRegistry.STONE_CLUB.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        gen.generateFlatItem(ItemRegistry.AMULET.get(), ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(ItemRegistry.CRIMSON_FUNGUS.get(), ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(ItemRegistry.CRIMSON_FUNGUS_ON_A_STICK.get(), ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(ItemRegistry.GOLDEN_HOGLIN_ARMOR.get(), ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(ItemRegistry.NETHERITE_HOGLIN_ARMOR.get(), ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(ItemRegistry.HOGLIN_SADDLE.get(), ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(ItemRegistry.PEBBLE.get(), ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(ItemRegistry.PIGLIN_BELT.get(), ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(BlockRegistry.HOGDEW_LUMPS.get().asItem(), ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(BlockRegistry.HOGDEW_FUNGUS.get().asItem(), ModelTemplates.FLAT_ITEM);
    }
}