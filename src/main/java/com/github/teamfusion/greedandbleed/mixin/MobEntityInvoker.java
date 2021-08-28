package com.github.teamfusion.greedandbleed.mixin;

import net.minecraft.entity.MobEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(MobEntity.class)
public interface MobEntityInvoker {
	@Invoker("canReplaceCurrentItem")
	boolean canReplaceCurrentItem(ItemStack newStack, ItemStack oldStack);
}
