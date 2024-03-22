package com.github.teamfusion.greedandbleed.mixin.client;

import com.github.teamfusion.greedandbleed.GreedAndBleed;
import com.github.teamfusion.greedandbleed.common.registry.ItemRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelShaper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.minecraft.client.renderer.entity.ItemRenderer.getFoilBuffer;
import static net.minecraft.client.renderer.entity.ItemRenderer.getFoilBufferDirect;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin {

    private static final ModelResourceLocation SLINGSHOT_MODEL = new ModelResourceLocation(GreedAndBleed.MOD_ID, "slingshot_back_shooting", "inventory");
    @Shadow
    @Final
    private ItemModelShaper itemModelShaper;


    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    public void render(ItemStack itemStack, ItemDisplayContext itemDisplayContext, boolean bl, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j, BakedModel bakedModel, CallbackInfo ci) {
        if (!itemStack.isEmpty()) {
            boolean bl2 = itemDisplayContext == ItemDisplayContext.FIRST_PERSON_RIGHT_HAND || itemDisplayContext == ItemDisplayContext.FIRST_PERSON_LEFT_HAND;
            if (bl2) {
                if (itemStack.is(ItemRegistry.SLINGSHOT.get())) {
                    poseStack.pushPose();
                    bakedModel = Minecraft.getInstance().getModelManager().getModel(SLINGSHOT_MODEL);
                    bakedModel.getTransforms().getTransform(itemDisplayContext).apply(bl, poseStack);
                    poseStack.translate(-0.5F, -0.5F, -0.5F);
                    boolean bl3 = true;

                    RenderType renderType = Sheets.translucentItemSheet();
                    VertexConsumer vertexConsumer;
                    if (bl3) {
                        vertexConsumer = getFoilBufferDirect(multiBufferSource, renderType, true, itemStack.hasFoil());
                    } else {
                        vertexConsumer = getFoilBuffer(multiBufferSource, renderType, true, itemStack.hasFoil());
                    }

                    this.renderModelLists(bakedModel, itemStack, i, j, poseStack, vertexConsumer);
                    poseStack.popPose();
                    ci.cancel();
                }
            }


        }
    }

    @Shadow
    private void renderModelLists(BakedModel bakedModel, ItemStack itemStack, int i, int j, PoseStack poseStack, VertexConsumer vertexConsumer) {

    }
}