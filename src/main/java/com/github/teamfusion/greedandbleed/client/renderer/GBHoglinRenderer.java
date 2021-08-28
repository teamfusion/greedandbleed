package com.github.teamfusion.greedandbleed.client.renderer;

import com.github.teamfusion.greedandbleed.client.layers.HoglinArmorLayer;
import com.github.teamfusion.greedandbleed.client.models.GBHoglinModelComplete;
import com.github.teamfusion.greedandbleed.GreedAndBleed;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.entity.monster.HoglinEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@SuppressWarnings("NullableProblems")
@OnlyIn(Dist.CLIENT)
public class GBHoglinRenderer extends MobRenderer<HoglinEntity, GBHoglinModelComplete<HoglinEntity>> {
   private static final ResourceLocation GB_HOGLIN_TEXTURE =
           new ResourceLocation(GreedAndBleed.MOD_ID,
                   "textures/entity/hoglin/hoglin_with_saddle.png");

   public GBHoglinRenderer(EntityRendererManager entityRendererManager) {
      super(entityRendererManager, new GBHoglinModelComplete<>(0.0F), 0.7F);
      this.addLayer(new HoglinArmorLayer(this));
   }

   @Override
   public ResourceLocation getTextureLocation(HoglinEntity hoglinEntity) {
      return GB_HOGLIN_TEXTURE;
   }

   @Override
   protected boolean isShaking(HoglinEntity hoglinEntity) {
      return hoglinEntity.isConverting();
   }
}
