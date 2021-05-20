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

   public void render(MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int p_225628_3_, HoglinEntity hoglin, float p_225628_5_, float p_225628_6_, float p_225628_7_, float p_225628_8_, float p_225628_9_, float p_225628_10_) {
      ItemStack itemstack = hoglin instanceof IHasMountArmor ? ((IHasMountArmor)hoglin).getArmor() : ItemStack.EMPTY;
      if (itemstack.getItem() instanceof HoglinArmorItem) {
         HoglinArmorItem hoglinArmorItem = (HoglinArmorItem)itemstack.getItem();
         this.getParentModel().copyPropertiesTo(this.model);
         this.model.prepareMobModel(hoglin, p_225628_5_, p_225628_6_, p_225628_7_);
         this.model.setupAnim(hoglin, p_225628_5_, p_225628_6_, p_225628_8_, p_225628_9_, p_225628_10_);
         float f;
         float f1;
         float f2;
         if (hoglinArmorItem instanceof DyeableHoglinArmorItem) {
            int i = ((DyeableHoglinArmorItem)hoglinArmorItem).getColor(itemstack);
            f = (float)(i >> 16 & 255) / 255.0F;
            f1 = (float)(i >> 8 & 255) / 255.0F;
            f2 = (float)(i & 255) / 255.0F;
         } else {
            f = 1.0F;
            f1 = 1.0F;
            f2 = 1.0F;
         }

         IVertexBuilder ivertexbuilder = renderTypeBuffer.getBuffer(RenderType.entityCutoutNoCull(hoglinArmorItem.getTexture()));
         this.model.renderToBuffer(matrixStack, ivertexbuilder, p_225628_3_, OverlayTexture.NO_OVERLAY, f, f1, f2, 1.0F);
      }
   }
}