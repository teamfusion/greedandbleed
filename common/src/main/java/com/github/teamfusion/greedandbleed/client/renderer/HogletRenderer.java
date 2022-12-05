package com.github.teamfusion.greedandbleed.client.renderer;

import com.github.teamfusion.greedandbleed.GreedAndBleed;
import com.github.teamfusion.greedandbleed.client.models.HogletModel;
import com.github.teamfusion.greedandbleed.common.entity.piglin.Hoglet;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class HogletRenderer extends MobRenderer<Hoglet, HogletModel<Hoglet>> {
    public static final ModelLayerLocation MAIN = new ModelLayerLocation(new ResourceLocation(GreedAndBleed.MOD_ID, "hoglet"), "main");

    public HogletRenderer(EntityRendererProvider.Context context) {
        super(context, new HogletModel<>(context.bakeLayer(MAIN)), 0.35F);
    }

    @Override
    public ResourceLocation getTextureLocation(Hoglet entity) {
        return new ResourceLocation(GreedAndBleed.MOD_ID, "textures/entity/hoglet/hoglet.png");
    }
}