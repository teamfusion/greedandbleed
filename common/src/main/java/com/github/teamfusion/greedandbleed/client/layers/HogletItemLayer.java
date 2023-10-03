package com.github.teamfusion.greedandbleed.client.layers;

import com.github.teamfusion.greedandbleed.client.models.AbstractHogletModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class HogletItemLayer<T extends LivingEntity, M extends AbstractHogletModel<T>> extends RenderLayer<T, M> {
    private final ItemInHandRenderer itemInHandRenderer;

    public HogletItemLayer(RenderLayerParent<T, M> renderLayerParent, ItemInHandRenderer itemInHandRenderer) {
        super(renderLayerParent);
        this.itemInHandRenderer = itemInHandRenderer;
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, T entity, float f, float g, float h, float j, float k, float l) {
        poseStack.pushPose();
        float scale = entity.isBaby() ? 0.75F : 1.0F;
        this.getParentModel().body.translateAndRotate(poseStack);
        this.getParentModel().head.translateAndRotate(poseStack);
        poseStack.translate(0, 0, -0.75 * scale);
        poseStack.mulPose(Axis.YP.rotationDegrees(k));
        poseStack.mulPose(Axis.XP.rotationDegrees(-l));
        poseStack.mulPose(Axis.XP.rotationDegrees(90.0f));
        ItemStack itemStack = entity.getItemBySlot(EquipmentSlot.MAINHAND);
        this.itemInHandRenderer.renderItem(entity, itemStack, ItemDisplayContext.GROUND, false, poseStack, multiBufferSource, i);
        poseStack.popPose();
    }
}

