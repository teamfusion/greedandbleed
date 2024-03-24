package com.github.teamfusion.greedandbleed.mixin.fabric.client;

import com.github.teamfusion.greedandbleed.GreedAndBleed;
import com.github.teamfusion.greedandbleed.common.registry.ItemRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin {
    @Unique private static final ModelResourceLocation SLINGSHOT_MODEL = new ModelResourceLocation(GreedAndBleed.MOD_ID, "slingshot_back", "inventory");

    @Inject(
        method = "render",
        at = @At("HEAD"),
        cancellable = true
    )
    public void render(ItemStack stack, ItemDisplayContext displayContext, boolean leftHanded, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay, BakedModel model, CallbackInfo ci) {
        if (!stack.isEmpty()) {
            boolean isFirstPerson = displayContext == ItemDisplayContext.FIRST_PERSON_RIGHT_HAND || displayContext == ItemDisplayContext.FIRST_PERSON_LEFT_HAND;
            if (isFirstPerson) {
                if (stack.is(ItemRegistry.SLINGSHOT.get())) {
                    poseStack.pushPose();
                    BakedModel slingshotModel = Minecraft.getInstance().getModelManager().getModel(SLINGSHOT_MODEL);
                    model = slingshotModel.getOverrides().resolve(slingshotModel, stack, null, Minecraft.getInstance().player, 0);
                    model.getTransforms().getTransform(displayContext).apply(leftHanded, poseStack);
                    poseStack.translate(-0.5F, -0.5F, -0.5F);

                    VertexConsumer vertices = ItemRenderer.getFoilBufferDirect(buffer, Sheets.translucentItemSheet(), true, stack.hasFoil());

                    this.renderModelLists(model, stack, combinedLight, combinedOverlay, poseStack, vertices);
                    poseStack.popPose();
                    ci.cancel();
                }
            }
        }
    }

    @Shadow
    private void renderModelLists(BakedModel bakedModel, ItemStack itemStack, int i, int j, PoseStack poseStack, VertexConsumer vertexConsumer) {}
}