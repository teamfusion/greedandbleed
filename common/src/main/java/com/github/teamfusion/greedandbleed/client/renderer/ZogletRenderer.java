package com.github.teamfusion.greedandbleed.client.renderer;

import com.github.teamfusion.greedandbleed.GreedAndBleed;
import com.github.teamfusion.greedandbleed.client.layers.HogletItemLayer;
import com.github.teamfusion.greedandbleed.client.models.AbstractHogletModel;
import com.github.teamfusion.greedandbleed.common.entity.piglin.Zoglet;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class ZogletRenderer<T extends Zoglet> extends MobRenderer<T, AbstractHogletModel<T>> {
    public ZogletRenderer(EntityRendererProvider.Context context) {
        super(context, new AbstractHogletModel<>(context.bakeLayer(HogletRenderer.MONSTER)), 0.35F);
        this.addLayer(new HogletItemLayer<>(this, context.getItemInHandRenderer()));
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return new ResourceLocation(GreedAndBleed.MOD_ID, "textures/entity/hoglet/zoglet.png");
    }
}