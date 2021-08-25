package com.infernalstudios.greedandbleed.client.renderer;

import com.infernalstudios.greedandbleed.GreedAndBleed;
import com.infernalstudios.greedandbleed.client.models.SkeletalPiglinModel;
import com.infernalstudios.greedandbleed.common.entity.piglin.SkeletalPiglinEntity;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.util.ResourceLocation;

@SuppressWarnings("NullableProblems")
public class SkeletalPiglinRenderer extends BipedRenderer<SkeletalPiglinEntity, SkeletalPiglinModel<SkeletalPiglinEntity>> {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(GreedAndBleed.MOD_ID, "textures/entity/piglin/skeletal_piglin.png");

    public SkeletalPiglinRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new SkeletalPiglinModel<>(), 0.7f);
        this.addLayer(new BipedArmorLayer<>(this, new BipedModel<>(0.5F), new BipedModel<>(1.02F)));
    }

    @Override
    public ResourceLocation getTextureLocation(SkeletalPiglinEntity entity) {
        return TEXTURE;
    }
}
