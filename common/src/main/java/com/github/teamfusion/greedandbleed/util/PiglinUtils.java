package com.github.teamfusion.greedandbleed.util;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

public class PiglinUtils {

    public static InteractionHand getWeaponHoldingHand(LivingEntity livingEntity, Predicate<ItemStack> item) {
        return item.test(livingEntity.getMainHandItem()) ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
    }
}
