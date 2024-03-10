package com.github.teamfusion.greedandbleed.client.renderer.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;

/*
 * from Bagus Lib
 * @credit bagu_chan
 */
public class SecondModelLayer<T extends Mob, M extends EntityModel<T>>
        extends RenderLayer<T, M> {
    private final ResourceLocation location;
    private final M layerModel;

    public SecondModelLayer(RenderLayerParent<T, M> renderLayerParent, ResourceLocation location, M model) {
        super(renderLayerParent);
        this.location = location;
        this.layerModel = model;
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, T mob, float f, float g, float h, float j, float k, float l) {
        SecondModelLayer.coloredCutoutModelCopyLayerRender(this.getParentModel(), this.layerModel, location, poseStack, multiBufferSource, i, mob, f, g, j, k, l, h, 1.0f, 1.0f, 1.0f);
    }
}

