package com.github.teamfusion.greedandbleed.platform.client.fabric;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public class RenderRegistryImpl {
    public static void block(RenderType type, Block... blocks) {
        BlockRenderLayerMap.INSTANCE.putBlocks(type, blocks);
    }

    public static <T extends Entity> void entityModel(Supplier<? extends EntityType<? extends T>> type, EntityRendererProvider<T> provider) {
        EntityRendererRegistry.register(type.get(), provider);
    }

    public static void layerDefinition(ModelLayerLocation location, Supplier<LayerDefinition> definition) {
        EntityModelLayerRegistry.registerModelLayer(location, definition::get);
    }
}