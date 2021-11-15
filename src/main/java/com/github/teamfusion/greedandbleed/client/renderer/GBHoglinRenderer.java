package com.github.teamfusion.greedandbleed.client.renderer;

import com.github.teamfusion.greedandbleed.GreedAndBleed;
import com.github.teamfusion.greedandbleed.api.IHogEquipable;
import com.github.teamfusion.greedandbleed.client.layers.HoglinArmorLayer;
import com.github.teamfusion.greedandbleed.client.models.GBHoglinModelComplete;
import com.github.teamfusion.greedandbleed.common.entity.IHasMountInventory;
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
                   "textures/entity/hoglin/hoglin.png");
   private static final ResourceLocation GB_HOGLIN_SADDLE_TEXTURE =
           new ResourceLocation(GreedAndBleed.MOD_ID,
                   "textures/entity/hoglin/hoglin_with_saddle.png");

   public GBHoglinRenderer(EntityRendererManager entityRendererManager) {
      super(entityRendererManager, new GBHoglinModelComplete<>(0.0F), 0.7F);
      this.addLayer(new HoglinArmorLayer(this));
   }

   @Override
   public ResourceLocation getTextureLocation(HoglinEntity hoglinEntity) {
      boolean hasChestOrSaddle = hoglinEntity instanceof IHasMountInventory && ((IHasMountInventory) hoglinEntity).hasChest()
              || hoglinEntity instanceof IHogEquipable && ((IHogEquipable) hoglinEntity).isHogSaddled();

      return hasChestOrSaddle ? GB_HOGLIN_SADDLE_TEXTURE : GB_HOGLIN_TEXTURE;
   }

   @Override
   protected boolean isShaking(HoglinEntity hoglinEntity) {
      return hoglinEntity.isConverting();
   }
}
