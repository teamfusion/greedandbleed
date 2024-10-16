package com.github.teamfusion.greedandbleed.client.renderer;

import com.github.teamfusion.greedandbleed.GreedAndBleed;
import com.github.teamfusion.greedandbleed.client.layers.CustomArmorLayer;
import com.github.teamfusion.greedandbleed.client.models.HoggartBackPackModel;
import com.github.teamfusion.greedandbleed.client.models.HoggartModel;
import com.github.teamfusion.greedandbleed.client.renderer.layer.SecondModelLayer;
import com.github.teamfusion.greedandbleed.common.entity.piglin.pygmy.Hoggart;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;

public class HoggartRenderer<T extends Hoggart> extends MobRenderer<T, HoggartModel<T>> {
    public static final ModelLayerLocation MAIN = new ModelLayerLocation(new ResourceLocation(GreedAndBleed.MOD_ID, "hoggart"), "main");
    public static final ResourceLocation TEXTURE_BACKPACK = new ResourceLocation(GreedAndBleed.MOD_ID, "textures/entity/piglin/hoggart/pigmy_backpack.png");
    public static final ResourceLocation TEXTURE = new ResourceLocation(GreedAndBleed.MOD_ID, "textures/entity/piglin/hoggart/crimson_hoggart.png");

    public HoggartRenderer(EntityRendererProvider.Context context) {
        super(context, new HoggartModel<>(context.bakeLayer(MAIN)), 0.3F);
        this.addLayer(new SecondModelLayer<>(this, TEXTURE_BACKPACK, new HoggartBackPackModel<>(context.bakeLayer(MAIN))));
        this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
        this.addLayer(new CustomArmorLayer<>(this, context));
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return TEXTURE;
    }

    @Override
    protected boolean isShaking(T livingEntity) {
        return super.isShaking(livingEntity) || livingEntity.isConverting();
    }
}