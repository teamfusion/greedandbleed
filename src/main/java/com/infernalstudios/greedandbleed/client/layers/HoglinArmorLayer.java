package com.infernalstudios.greedandbleed.client.layers;

import com.infernalstudios.greedandbleed.client.models.GBHoglinModelComplete;
import com.infernalstudios.greedandbleed.common.entity.IHasMountArmor;
import com.infernalstudios.greedandbleed.common.item.DyeableHoglinArmorItem;
import com.infernalstudios.greedandbleed.common.item.HoglinArmorItem;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.monster.HoglinEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class HoglinArmorLayer extends LayerRenderer<HoglinEntity, GBHoglinModelComplete<HoglinEntity>> {
   private final GBHoglinModelComplete<HoglinEntity> model = new GBHoglinModelComplete<>(0.1F);

   public HoglinArmorLayer(IEntityRenderer<HoglinEntity, GBHoglinModelComplete<HoglinEntity>> entityRenderer) {
      super(entityRenderer);
   }

   @Override
   public void render(MatrixStack matrices, IRenderTypeBuffer renderTypeBuffer, int light, HoglinEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
      ItemStack itemstack = entity instanceof IHasMountArmor ? ((IHasMountArmor)entity).getArmor() : ItemStack.EMPTY;
      if (itemstack.getItem() instanceof HoglinArmorItem) {
         HoglinArmorItem hoglinArmorItem = (HoglinArmorItem)itemstack.getItem();
         this.getParentModel().copyPropertiesTo(this.model);
         this.model.prepareMobModel(entity, limbAngle, limbDistance, tickDelta);
         this.model.setupAnim(entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);
         float r;
         float g;
         float b;
         if (hoglinArmorItem instanceof DyeableHoglinArmorItem) {
            int i = ((DyeableHoglinArmorItem)hoglinArmorItem).getColor(itemstack);
            r = (float)(i >> 16 & 255) / 255.0F;
            g = (float)(i >> 8 & 255) / 255.0F;
            b = (float)(i & 255) / 255.0F;
         } else {
            r = 1.0F;
            g = 1.0F;
            b = 1.0F;
         }

         IVertexBuilder ivertexbuilder = renderTypeBuffer.getBuffer(RenderType.entityCutoutNoCull(hoglinArmorItem.getTexture()));
         this.model.renderToBuffer(matrices, ivertexbuilder, light, OverlayTexture.NO_OVERLAY, r, g, b, 1.0F);
      }
   }
}
