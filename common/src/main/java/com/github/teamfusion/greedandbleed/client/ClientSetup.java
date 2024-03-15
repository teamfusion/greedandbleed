package com.github.teamfusion.greedandbleed.client;

import com.github.teamfusion.greedandbleed.GreedAndBleed;
import com.github.teamfusion.greedandbleed.client.models.*;
import com.github.teamfusion.greedandbleed.client.network.GreedAndBleedClientNetwork;
import com.github.teamfusion.greedandbleed.client.renderer.*;
import com.github.teamfusion.greedandbleed.common.registry.BlockRegistry;
import com.github.teamfusion.greedandbleed.common.registry.EntityTypeRegistry;
import com.github.teamfusion.greedandbleed.platform.client.RenderRegistry;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.resources.ResourceLocation;

import java.util.Objects;
import java.util.function.Supplier;

public class ClientSetup {
    private static final Supplier<LayerDefinition> OUTER_ARMOR_DEFINITION = () -> LayerDefinition.create(HumanoidModel.createMesh(new CubeDeformation(1.0F), 0.0F), 64, 32);
    private static final Supplier<LayerDefinition> INNER_ARMOR_DEFINITION = () -> LayerDefinition.create(HumanoidModel.createMesh(new CubeDeformation(0.5F), 0.0F), 64, 32);

    public static final ModelLayerLocation HOGLIN = new ModelLayerLocation(new ResourceLocation(GreedAndBleed.MOD_ID, "gb_hoglin"), "gb_hoglin");
    public static final ModelLayerLocation HOGLIN_ARMOR = new ModelLayerLocation(new ResourceLocation(GreedAndBleed.MOD_ID, "hoglin_armor"), "hoglin_armor");


    public static void onBootstrap() {
        GreedAndBleedClientNetwork.registerReceivers();
        // HOGLET RENDERER
        RenderRegistry.entityModel(EntityTypeRegistry.HOGLET, HogletRenderer::new);
        RenderRegistry.entityModel(EntityTypeRegistry.ZOGLET, ZogletRenderer::new);
        RenderRegistry.entityModel(EntityTypeRegistry.SKOGLET, SkogletRenderer::new);
        RenderRegistry.entityModel(EntityTypeRegistry.SHAMAN_PIGLIN, ShamanPiglinRenderer::new);
        RenderRegistry.entityModel(EntityTypeRegistry.PYGMY, PygmyRenderer::new);
        RenderRegistry.entityModel(EntityTypeRegistry.HOGGART, HoggartRenderer::new);
        RenderRegistry.entityModel(EntityTypeRegistry.THROWN_DAMAGEABLE, ThrownItemRenderer::new);
        RenderRegistry.layerDefinition(HogletRenderer.MAIN, AbstractHogletModel::createBodyLayer);
        RenderRegistry.layerDefinition(HogletRenderer.MONSTER, MonsterHogletModel::createBodyLayer);

        // SKELETAL PIGLIN RENDERER
        RenderRegistry.entityModel(EntityTypeRegistry.SKELETAL_PIGLIN, SkeletalPiglinRenderer::new);
        RenderRegistry.layerDefinition(SkeletalPiglinRenderer.MAIN, SkeletalPiglinModel::createBodyLayer);
        RenderRegistry.layerDefinition(SkeletalPiglinRenderer.OUTER_ARMOR, OUTER_ARMOR_DEFINITION);
        RenderRegistry.layerDefinition(SkeletalPiglinRenderer.INNER_ARMOR, INNER_ARMOR_DEFINITION);
        RenderRegistry.layerDefinition(HOGLIN, () -> GBHoglinModelComplete.createBodyLayer(0.0F));
        RenderRegistry.layerDefinition(HOGLIN_ARMOR, () -> GBHoglinModelComplete.createBodyLayer(0.05F));
        RenderRegistry.layerDefinition(ShamanPiglinRenderer.MAIN, ShamanPiglinModel::createBodyLayer);
        RenderRegistry.layerDefinition(PygmyRenderer.MAIN, PygmyModel::createBodyLayer);
        RenderRegistry.layerDefinition(HoggartRenderer.MAIN, HoggartModel::createBodyLayer);
    }

    public static void onInitialized() {
        RenderRegistry.block(RenderType.cutout(), Objects.requireNonNull(BlockRegistry.HOGDEW_FUNGUS.get()), Objects.requireNonNull(BlockRegistry.HOGDEW_LUMPS.get()));
        RenderRegistry.block(RenderType.cutout(), Objects.requireNonNull(BlockRegistry.HOGDEW_DOOR.get()), Objects.requireNonNull(BlockRegistry.HOGDEW_DOOR.get()));
        RenderRegistry.block(RenderType.cutout(), Objects.requireNonNull(BlockRegistry.HOGDEW_TRAPDOOR.get()), Objects.requireNonNull(BlockRegistry.HOGDEW_TRAPDOOR.get()));
    }
}