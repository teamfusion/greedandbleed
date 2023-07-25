package com.github.teamfusion.greedandbleed.client.renderer;

import com.github.teamfusion.greedandbleed.GreedAndBleed;
import com.github.teamfusion.greedandbleed.client.models.SkeletalPiglinModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;

public class SkeletalPiglinRenderer extends HumanoidMobRenderer<Mob, SkeletalPiglinModel<Mob>> {
    public static final ModelLayerLocation MAIN = new ModelLayerLocation(new ResourceLocation(GreedAndBleed.MOD_ID, "skeletal_piglin"), "main");
    public static final ModelLayerLocation INNER_ARMOR = new ModelLayerLocation(new ResourceLocation(GreedAndBleed.MOD_ID, "skeletal_piglin"), "inner_armor");
    public static final ModelLayerLocation OUTER_ARMOR = new ModelLayerLocation(new ResourceLocation(GreedAndBleed.MOD_ID, "skeletal_piglin"), "outer_armor");

    public SkeletalPiglinRenderer(EntityRendererProvider.Context context) {
        super(context, new SkeletalPiglinModel<>(context.bakeLayer(MAIN)), 0.7F, 1.0019531F, 1.0F, 1.0019531F);
        this.addLayer(new HumanoidArmorLayer<>(this, new HumanoidModel<>(context.bakeLayer(INNER_ARMOR)), new HumanoidModel<>(context.bakeLayer(OUTER_ARMOR)), context.getModelManager()));
    }

    @Override
    public ResourceLocation getTextureLocation(Mob mob) {
        return new ResourceLocation(GreedAndBleed.MOD_ID, "textures/entity/piglin/skeletal_piglin.png");
    }
}