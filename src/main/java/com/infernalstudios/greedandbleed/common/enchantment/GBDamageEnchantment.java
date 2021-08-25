package com.infernalstudios.greedandbleed.common.enchantment;

import com.infernalstudios.greedandbleed.common.entity.GBCreatureAttribute;
import net.minecraft.enchantment.DamageEnchantment;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;

@SuppressWarnings({ "NullableProblems", "unused" })
public class GBDamageEnchantment extends DamageEnchantment {
   private static final String[] NAMES = new String[]{"all", "undead", "arthropods", "piglins"};
   private static final int[] GB_MIN_COST = new int[]{1, 5, 5, 5};
   private static final int[] GB_LEVEL_COST = new int[]{11, 8, 8, 8};
   private static final int[] GB_LEVEL_COST_SPAN = new int[]{20, 20, 20, 20};

   public GBDamageEnchantment(Enchantment.Rarity rarity, int damageType, EquipmentSlotType... slotTypes) {
      super(rarity, damageType, slotTypes);
   }
   @Override
   public int getMinCost(int level) {
      if(this.type == 3){
         return GB_MIN_COST[this.type] + (level - 1) * GB_LEVEL_COST[this.type];
      } else{
         return super.getMinCost(level);
      }
   }

   @Override
   public int getMaxCost(int level) {
      if(this.type == 3){
         return this.getMinCost(level) + GB_LEVEL_COST_SPAN[this.type];
      } else{
         return super.getMaxCost(level);
      }
   }

   @Override
   public float getDamageBonus(int level, CreatureAttribute creatureAttribute) {
      return this.type == 3 && creatureAttribute == GBCreatureAttribute.PIGLIN ? (float)level * 2.5F : super.getDamageBonus(level, creatureAttribute);
   }

   @Override
   public boolean checkCompatibility(Enchantment enchantment) {
      return !(enchantment instanceof GBDamageEnchantment) && super.checkCompatibility(enchantment);
   }

   /*
   Can only apply to axes via the enchanting table
    */
   @Override
   public boolean canApplyAtEnchantingTable(ItemStack stack) {
      return stack.getItem() instanceof AxeItem
              || (!(stack.getItem() instanceof SwordItem) && super.canApplyAtEnchantingTable(stack));
   }

   /*
   Can apply to either axes or swords via anvils
    */
   @Override
   public boolean canEnchant(ItemStack stack) {
      return stack.getItem() instanceof AxeItem
              || stack.getItem() instanceof SwordItem
              || super.canEnchant(stack);
   }
}
