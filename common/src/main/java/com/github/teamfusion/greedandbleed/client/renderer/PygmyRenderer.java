package com.github.teamfusion.greedandbleed.client.renderer;

import com.github.teamfusion.greedandbleed.GreedAndBleed;
import com.github.teamfusion.greedandbleed.client.models.PygmyModel;
import com.github.teamfusion.greedandbleed.common.entity.piglin.Pygmy;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class PygmyRenderer<T extends Pygmy> extends MobRenderer<T, PygmyModel<T>> {
    public static final ModelLayerLocation MAIN = new ModelLayerLocation(new ResourceLocation(GreedAndBleed.MOD_ID, "pygmy"), "main");

    public PygmyRenderer(EntityRendererProvider.Context context) {
        super(context, new PygmyModel<>(context.bakeLayer(MAIN)), 0.3F);
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return new ResourceLocation(GreedAndBleed.MOD_ID, "textures/entity/piglin/pygmy/pygmy.png");
    }

    @Override
    protected boolean isShaking(T livingEntity) {
        return super.isShaking(livingEntity) || livingEntity.isConverting();
    }
}