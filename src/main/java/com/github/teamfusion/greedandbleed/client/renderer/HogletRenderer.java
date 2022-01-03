package com.github.teamfusion.greedandbleed.client.renderer;

import com.github.teamfusion.greedandbleed.GreedAndBleed;
import com.github.teamfusion.greedandbleed.client.models.HogletModel;
import com.github.teamfusion.greedandbleed.common.entity.piglin.HogletEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class HogletRenderer extends MobRenderer<HogletEntity, HogletModel<HogletEntity>> {
	protected static final ResourceLocation TEXTURE = new ResourceLocation(GreedAndBleed.MOD_ID, "textures/entity/hoglet/hoglet.png");

	public HogletRenderer(EntityRendererManager renderManagerIn) {
		super(renderManagerIn, new HogletModel<>(), 0.35f);
	}

	@Override
	public ResourceLocation getTextureLocation(HogletEntity entity) {
		return TEXTURE;
	}
}
