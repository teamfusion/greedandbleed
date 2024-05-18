package com.github.teamfusion.greedandbleed.mixin.client;

import com.github.teamfusion.greedandbleed.client.models.GoldenShieldModel;
import com.github.teamfusion.greedandbleed.common.registry.ItemRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockEntityWithoutLevelRenderer.class)
public class BlockEntityWithoutLevelRendererMixin {
    @Shadow
    @Final
    private EntityModelSet entityModelSet;
    @Unique
    private GoldenShieldModel greedandbleed$shieldModel;

    @Inject(
            method = "renderByItem",
            at = @At("HEAD")
    )
    public void render(ItemStack itemStack, ItemDisplayContext itemDisplayContext, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j, CallbackInfo ci) {
        if (itemStack.is(ItemRegistry.GOLDEN_SHIELD.get())) {
            boolean bl = BlockItem.getBlockEntityData(itemStack) != null;
            poseStack.pushPose();
            poseStack.scale(1.0f, -1.0f, -1.0f);
            VertexConsumer vertexConsumer = ItemRenderer.getFoilBufferDirect(multiBufferSource, this.greedandbleed$shieldModel.renderType(GoldenShieldModel.LOCATION), true, itemStack.hasFoil());
            this.greedandbleed$shieldModel.handle().render(poseStack, vertexConsumer, i, j, 1.0f, 1.0f, 1.0f, 1.0f);
            this.greedandbleed$shieldModel.plate().render(poseStack, vertexConsumer, i, j, 1.0f, 1.0f, 1.0f, 1.0f);

            poseStack.popPose();
        }

    }

    @Inject(
            method = "onResourceManagerReload",
            at = @At("HEAD")
    )
    public void onResourceManagerReload(ResourceManager resourceManager, CallbackInfo ci) {
        this.greedandbleed$shieldModel = new GoldenShieldModel(this.entityModelSet.bakeLayer(GoldenShieldModel.LAYER_LOCATION));
    }
}
