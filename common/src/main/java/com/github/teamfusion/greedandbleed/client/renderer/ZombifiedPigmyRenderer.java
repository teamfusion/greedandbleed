package com.github.teamfusion.greedandbleed.client.renderer;

import com.github.teamfusion.greedandbleed.GreedAndBleed;
import com.github.teamfusion.greedandbleed.client.models.ZombifiedPigmyModel;
import com.github.teamfusion.greedandbleed.common.entity.piglin.pigmy.ZombifiedPigmies;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;

public class ZombifiedPigmyRenderer<T extends ZombifiedPigmies> extends MobRenderer<T, ZombifiedPigmyModel<T>> {
    public ZombifiedPigmyRenderer(EntityRendererProvider.Context context) {
        super(context, new ZombifiedPigmyModel<>(context.bakeLayer(PigmyRenderer.MAIN)), 0.3F);
        this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return new ResourceLocation(GreedAndBleed.MOD_ID, "textures/entity/piglin/pigmy/zombified_pigmy.png");
    }
}