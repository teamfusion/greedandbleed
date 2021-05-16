package com.infernalstudios.greedandbleed.client.renderer;

import com.infernalstudios.greedandbleed.GreedAndBleed;
import com.infernalstudios.greedandbleed.client.models.GBHoglinModel;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.BoarModel;
import net.minecraft.entity.monster.HoglinEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GBHoglinRenderer extends MobRenderer<HoglinEntity, GBHoglinModel<HoglinEntity>> {
   private static final ResourceLocation GB_HOGLIN_TEXTURE =
           new ResourceLocation(GreedAndBleed.MODID,
                   "textures/entity/hoglin/hoglin_with_saddle.png");

   public GBHoglinRenderer(EntityRendererManager entityRendererManager) {
      super(entityRendererManager, new GBHoglinModel<>(), 0.7F);
   }

   public ResourceLocation getTextureLocation(HoglinEntity hoglinEntity) {
      return GB_HOGLIN_TEXTURE;
   }

   protected boolean isShaking(HoglinEntity hoglinEntity) {
      return hoglinEntity.isConverting();
   }
}