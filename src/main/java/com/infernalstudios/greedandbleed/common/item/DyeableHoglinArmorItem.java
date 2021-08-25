package com.infernalstudios.greedandbleed.common.item;

import net.minecraft.item.IDyeableArmorItem;
import net.minecraft.item.Item;

@SuppressWarnings("unused")
public class DyeableHoglinArmorItem extends HoglinArmorItem implements IDyeableArmorItem {
   public DyeableHoglinArmorItem(int protectionIn, String armorName, Item.Properties properties) {
      super(protectionIn, armorName, properties);
   }
   public DyeableHoglinArmorItem(int protectionIn, net.minecraft.util.ResourceLocation texture, Item.Properties properties) {
      super(protectionIn, texture, properties);
   }
}
