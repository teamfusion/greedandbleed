package com.infernalstudios.greedandbleed.common.item;

import com.infernalstudios.greedandbleed.common.entity.IToleratingMount;
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
@SuppressWarnings("NullableProblems")
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
      ItemStack onAStick = player.getItemInHand(hand);
      if (!world.isClientSide) {
         Entity vehicle = player.getVehicle();
         boolean canInteractWith = player.isPassenger()
                 && vehicle != null
                 && vehicle.getType() == this.canInteractWith;
         if (canInteractWith) {
            if(vehicle instanceof IToleratingMount){
               IToleratingMount toleratingMount = (IToleratingMount) vehicle;
               toleratingMount.addTolerance(this.consumeItemDamage * 20);
               return useOnAStickItem(player, hand, onAStick);
            }
            if(vehicle instanceof IRideable){
               IRideable irideable = (IRideable) vehicle;
               if (irideable.boost()) {
                  return useOnAStickItem(player, hand, onAStick);
               }
            }
         }
         player.awardStat(Stats.ITEM_USED.get(this));
      }
      return ActionResult.pass(onAStick);
   }

   private ActionResult<ItemStack> useOnAStickItem(PlayerEntity player, Hand hand, ItemStack onAStick) {
      onAStick.hurtAndBreak(this.consumeItemDamage, player, (holder) -> holder.broadcastBreakEvent(hand));
      if (onAStick.isEmpty()) {
         ItemStack nothingOnAStick = new ItemStack(Items.FISHING_ROD);
         nothingOnAStick.setTag(onAStick.getTag());
         return ActionResult.success(nothingOnAStick);
      }

      return ActionResult.success(onAStick);
   }
}
