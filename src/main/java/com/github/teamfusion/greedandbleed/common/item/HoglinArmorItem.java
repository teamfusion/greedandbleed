package com.github.teamfusion.greedandbleed.common.item;

import com.github.teamfusion.greedandbleed.GreedAndBleed;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class HoglinArmorItem extends Item {
   private final int protection;
   private final ResourceLocation texture;

   public HoglinArmorItem(int protectionIn, String armorNameIn, Item.Properties properties) {
      this(protectionIn, new ResourceLocation(GreedAndBleed.MOD_ID, "textures/entity/hoglin/armor/hoglin_armor_" + armorNameIn + ".png"), properties);
   }

   public HoglinArmorItem(int protectionIn, ResourceLocation texture, Item.Properties properties) {
      super(properties);
      this.protection = protectionIn;
      this.texture = texture;
   }

   @OnlyIn(Dist.CLIENT)
   public ResourceLocation getTexture() {
      return texture;
   }

   public int getProtection() {
      return this.protection;
   }
}
