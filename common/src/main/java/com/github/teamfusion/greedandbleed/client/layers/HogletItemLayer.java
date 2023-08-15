package com.github.teamfusion.greedandbleed.client.layers;

import com.github.teamfusion.greedandbleed.client.models.HogletModel;
import com.github.teamfusion.greedandbleed.common.entity.piglin.Hoglet;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class HogletItemLayer extends RenderLayer<Hoglet, HogletModel<Hoglet>> {
    private final ItemInHandRenderer itemInHandRenderer;

    public HogletItemLayer(RenderLayerParent<Hoglet, HogletModel<Hoglet>> renderLayerParent, ItemInHandRenderer itemInHandRenderer) {
        super(renderLayerParent);
        this.itemInHandRenderer = itemInHandRenderer;
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, Hoglet hoglet, float f, float g, float h, float j, float k, float l) {
        poseStack.pushPose();
        float scale = hoglet.isBaby() ? 0.75F : 1.0F;
        this.getParentModel().body.translateAndRotate(poseStack);
        this.getParentModel().head.translateAndRotate(poseStack);
        poseStack.translate(0, 0, -0.75 * scale);
        poseStack.mulPose(Axis.YP.rotationDegrees(k));
        poseStack.mulPose(Axis.XP.rotationDegrees(-l));
        poseStack.mulPose(Axis.XP.rotationDegrees(90.0f));
        ItemStack itemStack = hoglet.getItemBySlot(EquipmentSlot.MAINHAND);
        this.itemInHandRenderer.renderItem(hoglet, itemStack, ItemDisplayContext.GROUND, false, poseStack, multiBufferSource, i);
        poseStack.popPose();
    }
}

