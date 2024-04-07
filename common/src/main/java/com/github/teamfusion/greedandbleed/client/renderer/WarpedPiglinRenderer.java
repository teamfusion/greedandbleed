package com.github.teamfusion.greedandbleed.client.renderer;

import com.github.teamfusion.greedandbleed.GreedAndBleed;
import com.github.teamfusion.greedandbleed.client.models.WarpedPiglinModel;
import com.github.teamfusion.greedandbleed.common.entity.piglin.WarpedPiglin;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;

public class WarpedPiglinRenderer<T extends WarpedPiglin> extends MobRenderer<T, WarpedPiglinModel<T>> {
    public static final ModelLayerLocation MAIN = new ModelLayerLocation(new ResourceLocation(GreedAndBleed.MOD_ID, "warped_piglin"), "main");

    public WarpedPiglinRenderer(EntityRendererProvider.Context context) {
        super(context, new WarpedPiglinModel<>(context.bakeLayer(MAIN)), 0.3F);
        this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return new ResourceLocation(GreedAndBleed.MOD_ID, "textures/entity/piglin/warped_piglin.png");
    }

    @Override
    protected boolean isShaking(T livingEntity) {
        return super.isShaking(livingEntity) || livingEntity.isConverting();
    }
}