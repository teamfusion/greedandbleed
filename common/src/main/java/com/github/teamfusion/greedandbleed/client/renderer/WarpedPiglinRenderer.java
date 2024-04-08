package com.github.teamfusion.greedandbleed.client.renderer;

import com.github.teamfusion.greedandbleed.GreedAndBleed;
import com.github.teamfusion.greedandbleed.client.models.WarpedPiglinModel;
import com.github.teamfusion.greedandbleed.common.entity.piglin.WarpedPiglin;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class WarpedPiglinRenderer<T extends WarpedPiglin> extends MobRenderer<T, WarpedPiglinModel<T>> {
    public static final ModelLayerLocation MAIN = new ModelLayerLocation(new ResourceLocation(GreedAndBleed.MOD_ID, "warped_piglin"), "main");

    public WarpedPiglinRenderer(EntityRendererProvider.Context context) {
        super(context, new WarpedPiglinModel<>(context.bakeLayer(MAIN)), 0.3F);
        this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
    }

    @Override
    protected void setupRotations(T p_117802_, PoseStack p_117803_, float p_117804_, float p_117805_, float p_117806_) {
        if (p_117802_.isFallFlying()) {
            super.setupRotations(p_117802_, p_117803_, p_117804_, p_117805_, p_117806_);
            float f1 = (float) p_117802_.getFallFlyingTicks() + p_117806_;
            float f2 = Mth.clamp(f1 * f1 / 100.0F, 0.0F, 1.0F);
            if (!p_117802_.isAutoSpinAttack()) {
                p_117803_.mulPose(Axis.XP.rotationDegrees(f2 * (-90.0F - p_117802_.getXRot())));
            }

            Vec3 vec3 = p_117802_.getViewVector(p_117806_);
            Vec3 vec31 = p_117802_.getDeltaMovement();
            double d0 = vec31.horizontalDistanceSqr();
            double d1 = vec3.horizontalDistanceSqr();
            if (d0 > 0.0D && d1 > 0.0D) {
                double d2 = (vec31.x * vec3.x + vec31.z * vec3.z) / Math.sqrt(d0 * d1);
                double d3 = vec31.x * vec3.z - vec31.z * vec3.x;
                p_117803_.mulPose(Axis.YP.rotation((float) (Math.signum(d3) * Math.acos(d2))));
            }
        } else {
            super.setupRotations(p_117802_, p_117803_, p_117804_, p_117805_, p_117806_);
        }

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