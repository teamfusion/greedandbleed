package com.github.teamfusion.greedandbleed.common.registry;

import com.github.teamfusion.greedandbleed.GreedAndBleed;
import com.github.teamfusion.greedandbleed.common.enchantment.WoeOfSwinEnchantment;
import com.github.teamfusion.greedandbleed.platform.CoreRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

import java.util.function.Supplier;

public class EnchantmentRegistry {
    public static final CoreRegistry<Enchantment> ENCHANTMENT = CoreRegistry.of(BuiltInRegistries.ENCHANTMENT, GreedAndBleed.MOD_ID);
    public static final Supplier<Enchantment> WOE_OF_SWINES = create("woe_of_swines", new WoeOfSwinEnchantment(Enchantment.Rarity.RARE, EnchantmentCategory.WEAPON, new EquipmentSlot[]{EquipmentSlot.MAINHAND}));

    private static Supplier<Enchantment> create(String key, Enchantment enchantment) {
        return ENCHANTMENT.create(key, () -> enchantment);
    }
}
