package com.github.teamfusion.greedandbleed.data.client;

import com.github.teamfusion.greedandbleed.common.registry.BlockRegistry;
import com.github.teamfusion.greedandbleed.common.registry.ItemRegistry;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

public class ModelGenerator extends FabricModelProvider {
    public ModelGenerator(FabricDataOutput dataGenerator) {
        super(dataGenerator);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators gen) {
        gen.woodProvider(BlockRegistry.HOGDEW_STEM.get()).logUVLocked(BlockRegistry.HOGDEW_STEM.get()).wood(BlockRegistry.HOGDEW_HYPHAE.get());
        gen.woodProvider(BlockRegistry.STRIPPED_HOGDEW_STEM.get()).logUVLocked(BlockRegistry.STRIPPED_HOGDEW_STEM.get()).wood(BlockRegistry.STRIPPED_HOGDEW_HYPHAE.get());

        gen.createTrivialCube(BlockRegistry.HOGDEW_CLUSTER.get());
        gen.createCrossBlock(BlockRegistry.HOGDEW_FUNGUS.get(), BlockModelGenerators.TintState.NOT_TINTED);

        gen.createTrivialCube(BlockRegistry.HOGDEW_WART_BLOCK.get());
        gen.createDoor(BlockRegistry.HOGDEW_DOOR.get());
        gen.createMultiface(BlockRegistry.HOGDEW_LUMPS.get());
        gen.createOrientableTrapdoor(BlockRegistry.HOGDEW_TRAPDOOR.get());
        gen.family(BlockRegistry.HOGDEW_PLANKS.get()).stairs(BlockRegistry.HOGDEW_PLANKS_STAIRS.get()).slab(BlockRegistry.HOGDEW_PLANKS_SLAB.get()).fenceGate(BlockRegistry.HOGDEW_FENCE_GATE.get()).fence(BlockRegistry.HOGDEW_FENCE.get()).button(BlockRegistry.HOGDEW_BUTTON.get()).pressurePlate(BlockRegistry.HOGDEW_PRESSURE_PLATE.get());
        gen.createNyliumBlock(BlockRegistry.HOGDEW_NYLIUM.get());

        createNyliumPath(gen, BlockRegistry.HOGDEW_NYLIUM_PATH.get());
        createNyliumPath(gen, BlockRegistry.CRIMSON_NYLIUM_PATH.get());
        createNyliumPath(gen, BlockRegistry.WARPED_NYLIUM_PATH.get());
        
        createSpawnEgg(gen, ItemRegistry.SKELETAL_PIGLIN_SPAWN_EGG.get());
        createSpawnEgg(gen, ItemRegistry.HOGLET_SPAWN_EGG.get());
        createSpawnEgg(gen, ItemRegistry.ZOMBIFIED_HOGLET_SPAWN_EGG.get());
        createSpawnEgg(gen, ItemRegistry.SKELETON_HOGLET_SPAWN_EGG.get());
        createSpawnEgg(gen, ItemRegistry.SHAMAN_PIGLIN_SPAWN_EGG.get());
        createSpawnEgg(gen, ItemRegistry.PIGMY_SPAWN_EGG.get());
        createSpawnEgg(gen, ItemRegistry.SHRYGMY_SPAWN_EGG.get());
        createSpawnEgg(gen, ItemRegistry.HOGGART_SPAWN_EGG.get());
        createSpawnEgg(gen, ItemRegistry.ZOMBIFIED_PIGMY_SPAWN_EGG.get());
        createSpawnEgg(gen, ItemRegistry.WARPED_PIGLIN_SPAWN_EGG.get());
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
        gen.generateFlatItem(BlockRegistry.HOGDEW_FUNGUS.get().asItem(), ModelTemplates.FLAT_ITEM);
    }

    private void createNyliumPath(BlockModelGenerators generators, Block block) {
        ResourceLocation resourceLocation = GBTexturedModel.NYLIUM_PATH.create(block, generators.modelOutput);
        generators.blockStateOutput.accept(generators.createRotatedVariant(block, resourceLocation));
    }
    
    public void createSpawnEgg(BlockModelGenerators gen, ItemLike item) {
        gen.delegateItemModel(item.asItem(), ModelLocationUtils.decorateItemModelLocation("template_spawn_egg"));
    }
}