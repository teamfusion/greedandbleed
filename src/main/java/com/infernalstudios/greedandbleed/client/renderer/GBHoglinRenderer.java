package com.infernalstudios.greedandbleed.client.renderer;

import com.infernalstudios.greedandbleed.GreedAndBleed;
import com.infernalstudios.greedandbleed.client.layers.HoglinArmorLayer;
import com.infernalstudios.greedandbleed.client.models.GBHoglinModelComplete;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.entity.monster.HoglinEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GBHoglinRenderer extends MobRenderer<HoglinEntity, GBHoglinModelComplete<HoglinEntity>> {
   private static final ResourceLocation GB_HOGLIN_TEXTURE =
           new ResourceLocation(GreedAndBleed.MODID,
                   "textures/entity/hoglin/hoglin_with_saddle.png");

   public GBHoglinRenderer(EntityRendererManager entityRendererManager) {
      super(entityRendererManager, new GBHoglinModelComplete<>(0.0F), 0.7F);
      this.addLayer(new HoglinArmorLayer(this));
   }

   public ResourceLocation getTextureLocation(HoglinEntity hoglinEntity) {
      return GB_HOGLIN_TEXTURE;
   }

   protected boolean isShaking(HoglinEntity hoglinEntity) {
      return hoglinEntity.isConverting();
   }
}