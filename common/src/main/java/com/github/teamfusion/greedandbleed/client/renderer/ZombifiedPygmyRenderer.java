package com.github.teamfusion.greedandbleed.client.renderer;

import com.github.teamfusion.greedandbleed.GreedAndBleed;
import com.github.teamfusion.greedandbleed.client.models.ZombifiedPygmyModel;
import com.github.teamfusion.greedandbleed.common.entity.piglin.pygmy.ZombifiedPygmy;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;

public class ZombifiedPygmyRenderer<T extends ZombifiedPygmy> extends MobRenderer<T, ZombifiedPygmyModel<T>> {
    public ZombifiedPygmyRenderer(EntityRendererProvider.Context context) {
        super(context, new ZombifiedPygmyModel<>(context.bakeLayer(PygmyRenderer.MAIN)), 0.3F);
        this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return new ResourceLocation(GreedAndBleed.MOD_ID, "textures/entity/piglin/pygmy/zombified_pigmy.png");
    }
}