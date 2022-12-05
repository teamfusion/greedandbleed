package com.github.teamfusion.greedandbleed.client;

import com.github.teamfusion.greedandbleed.client.models.HogletModel;
import com.github.teamfusion.greedandbleed.client.models.SkeletalPiglinModel;
import com.github.teamfusion.greedandbleed.client.renderer.HogletRenderer;
import com.github.teamfusion.greedandbleed.client.renderer.SkeletalPiglinRenderer;
import com.github.teamfusion.greedandbleed.common.registry.EntityTypeRegistry;
import com.github.teamfusion.greedandbleed.platform.client.RenderRegistry;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;

import java.util.function.Supplier;

public class ClientSetup {
    private static final Supplier<LayerDefinition> OUTER_ARMOR_DEFINITION = () -> LayerDefinition.create(HumanoidModel.createMesh(new CubeDeformation(1.0F), 0.0F), 64, 32);
    private static final Supplier<LayerDefinition> INNER_ARMOR_DEFINITION = () -> LayerDefinition.create(HumanoidModel.createMesh(new CubeDeformation(0.5F), 0.0F), 64, 32);

    public static void onBootstrap() {
        // HOGLET RENDERER
        RenderRegistry.entityModel(EntityTypeRegistry.HOGLET, HogletRenderer::new);
        RenderRegistry.layerDefinition(HogletRenderer.MAIN, HogletModel::createBodyLayer);

        // SKELETAL PIGLIN RENDERER
        RenderRegistry.entityModel(EntityTypeRegistry.SKELETAL_PIGLIN, SkeletalPiglinRenderer::new);
        RenderRegistry.layerDefinition(SkeletalPiglinRenderer.MAIN, SkeletalPiglinModel::createBodyLayer);
        RenderRegistry.layerDefinition(SkeletalPiglinRenderer.OUTER_ARMOR, OUTER_ARMOR_DEFINITION);
        RenderRegistry.layerDefinition(SkeletalPiglinRenderer.INNER_ARMOR, INNER_ARMOR_DEFINITION);
    }

    public static void onInitialized() {

    }
}