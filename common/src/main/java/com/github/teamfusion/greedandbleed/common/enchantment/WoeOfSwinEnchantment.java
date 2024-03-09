package com.github.teamfusion.greedandbleed.common.enchantment;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.DamageEnchantment;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class WoeOfSwinEnchantment extends Enchantment {
    public WoeOfSwinEnchantment(Rarity rarity, EnchantmentCategory enchantmentCategory, EquipmentSlot[] equipmentSlots) {
        super(rarity, enchantmentCategory, equipmentSlots);
    }

    public int getMinCost(int i) {
        return 5 + (i - 1) * 1;
    }

    public int getMaxCost(int i) {
        return this.getMinCost(i) + 30;
    }

    public int getMaxLevel() {
        return 5;
    }

    @Override
    protected boolean checkCompatibility(Enchantment enchantment) {
        return super.checkCompatibility(enchantment) && !(enchantment instanceof DamageEnchantment);
    }

    public boolean canEnchant(ItemStack itemStack) {
        return itemStack.getItem() instanceof AxeItem;
    }

    public void doPostAttack(LivingEntity livingEntity, Entity entity, int i) {
        if (entity instanceof LivingEntity livingEntity2) {
            if (i > 0 && livingEntity2 instanceof AbstractPiglin) {
                int j = 20 + livingEntity.getRandom().nextInt(10 * i);
                livingEntity2.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, j, 0));
            }
        }

    }

    @Override
    public boolean isTreasureOnly() {
        return true;
    }

    @Override
    public boolean isDiscoverable() {
        return false;
    }
}
