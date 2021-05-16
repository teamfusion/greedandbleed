package com.infernalstudios.greedandbleed.common.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IRideable;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.OnAStickItem;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

// PigEntity is used as a placeholder parameter for OnAStickItem
public class GBOnAStickItem extends OnAStickItem<PigEntity> {
   private final EntityType<?> canInteractWith;
   private final int consumeItemDamage;

   public GBOnAStickItem(Item.Properties properties, EntityType<?> canInteractWith, int consumeDamageIn) {
      super(properties, EntityType.PIG, consumeDamageIn);
      this.canInteractWith = canInteractWith;
      this.consumeItemDamage = consumeDamageIn;
   }

   @Override
   public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
      ItemStack itemstack = player.getItemInHand(hand);
      if (world.isClientSide) {
         return ActionResult.pass(itemstack);
      } else {
         Entity entity = player.getVehicle();
         if (player.isPassenger()
                 && entity instanceof IRideable
                 && entity.getType() == this.canInteractWith) {
            IRideable irideable = (IRideable)entity;
            if (irideable.boost()) {
               itemstack.hurtAndBreak(this.consumeItemDamage, player, (holder) -> {
                  holder.broadcastBreakEvent(hand);
               });
               if (itemstack.isEmpty()) {
                  ItemStack nothingOnAStickStack = new ItemStack(Items.FISHING_ROD);
                  nothingOnAStickStack.setTag(itemstack.getTag());
                  return ActionResult.success(nothingOnAStickStack);
               }

               return ActionResult.success(itemstack);
            }
         }

         player.awardStat(Stats.ITEM_USED.get(this));
         return ActionResult.pass(itemstack);
      }
   }
}