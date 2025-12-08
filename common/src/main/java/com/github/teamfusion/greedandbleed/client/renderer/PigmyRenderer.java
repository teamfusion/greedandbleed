package com.github.teamfusion.greedandbleed.client.renderer;

import com.github.teamfusion.greedandbleed.GreedAndBleed;
import com.github.teamfusion.greedandbleed.client.layers.CustomArmorLayer;
import com.github.teamfusion.greedandbleed.client.models.PigmyModel;
import com.github.teamfusion.greedandbleed.common.entity.piglin.pigmy.Pigmy;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;

public class PigmyRenderer<T extends Pigmy> extends MobRenderer<T, PigmyModel<T>> {
    public static final ModelLayerLocation MAIN = new ModelLayerLocation(new ResourceLocation(GreedAndBleed.MOD_ID, "pigmy"), "main");

    public PigmyRenderer(EntityRendererProvider.Context context) {
        super(context, new PigmyModel<>(context.bakeLayer(MAIN)), 0.3F);
        this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
        this.addLayer(new CustomArmorLayer<>(this, context));
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return new ResourceLocation(GreedAndBleed.MOD_ID, "textures/entity/piglin/pigmy/crimson_pigmy.png");
    }

    @Override
    protected boolean isShaking(T livingEntity) {
        return super.isShaking(livingEntity) || livingEntity.isConverting();
    }
}