package com.github.teamfusion.greedandbleed.client.renderer;

import com.github.teamfusion.greedandbleed.GreedAndBleed;
import com.github.teamfusion.greedandbleed.client.models.WarpedSpitModel;
import com.github.teamfusion.greedandbleed.common.entity.projectile.WarpedSpit;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class WarpedSpitRenderer<T extends WarpedSpit> extends EntityRenderer<T> {
    private static final ResourceLocation LLAMA_SPIT_LOCATION = new ResourceLocation(GreedAndBleed.MOD_ID, "textures/entity/warped_spit.png");
    private final WarpedSpitModel<T> model;
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(GreedAndBleed.MOD_ID, "warped_spit"), "main");

    public WarpedSpitRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new WarpedSpitModel<>(context.bakeLayer(LAYER_LOCATION));
    }

    public void render(T llamaSpit, float f, float g, PoseStack poseStack, MultiBufferSource multiBufferSource, int i) {
        poseStack.pushPose();
        poseStack.translate(0.0F, -0.15F, 0.0F);
        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(g, llamaSpit.yRotO, llamaSpit.getYRot()) - 180F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(g, llamaSpit.xRotO, llamaSpit.getXRot())));
        poseStack.translate(0.0F, -1.15F, 0.0F);
        this.model.setupAnim(llamaSpit, g, 0.0F, -0.1F, 0.0F, 0.0F);
        VertexConsumer vertexConsumer = multiBufferSource.getBuffer(this.model.renderType(LLAMA_SPIT_LOCATION));
        this.model.renderToBuffer(poseStack, vertexConsumer, i, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.popPose();
        super.render(llamaSpit, f, g, poseStack, multiBufferSource, i);
    }

    public ResourceLocation getTextureLocation(T llamaSpit) {
        return LLAMA_SPIT_LOCATION;
    }
}
