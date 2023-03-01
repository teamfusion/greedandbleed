package com.github.teamfusion.greedandbleed.common.item;

import com.github.teamfusion.greedandbleed.common.entity.ToleratingMount;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ItemSteerable;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FoodOnAStickItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

// Pig is used as a placeholder parameter for OnAStickItem
public class GBOnAStickItem extends FoodOnAStickItem<Pig> {
    private final EntityType<?> canInteractWith;
    private final int consumeItemDamage;

    public GBOnAStickItem(Properties properties, EntityType<?> canInteractWith, int consumeItemDamage) {
        super(properties, EntityType.PIG, consumeItemDamage);
        this.canInteractWith = canInteractWith;
        this.consumeItemDamage= consumeItemDamage;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide) {
            Entity vehicle = player.getVehicle();
            boolean canInteractWith = player.isPassenger() && vehicle != null && vehicle.getType() == this.canInteractWith;
            if (canInteractWith) {
                if (vehicle instanceof ToleratingMount mount) {
                    mount.addTolerance(this.consumeItemDamage * 20);
                    return useOnAStickItem(player, hand, stack);
                }

                if (vehicle instanceof ItemSteerable steerable) {
                    if (steerable.boost()) {
                        return useOnAStickItem(player, hand, stack);
                    }
                }
            }

            player.awardStat(Stats.ITEM_USED.get(this));
        }

        return InteractionResultHolder.pass(stack);
    }

    private InteractionResultHolder<ItemStack> useOnAStickItem(Player player, InteractionHand hand, ItemStack stack) {
        stack.hurtAndBreak(this.consumeItemDamage, player, holder -> holder.broadcastBreakEvent(hand));
        if (stack.isEmpty()) {
            ItemStack emptyRod = new ItemStack(Items.FISHING_ROD);
            emptyRod.setTag(stack.getTag());
            return InteractionResultHolder.success(emptyRod);
        }

        return InteractionResultHolder.success(stack);
    }
}