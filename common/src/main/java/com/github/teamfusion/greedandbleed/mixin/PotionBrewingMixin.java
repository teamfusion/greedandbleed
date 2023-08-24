package com.github.teamfusion.greedandbleed.mixin;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(PotionBrewing.class)
public interface PotionBrewingMixin {

    @Invoker("addMix")
    static void invokeAddMix(Potion input, Item item, Potion output) {
    }

}