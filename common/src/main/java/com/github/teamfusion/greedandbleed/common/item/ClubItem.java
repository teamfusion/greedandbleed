package com.github.teamfusion.greedandbleed.common.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Vanishable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class ClubItem extends Item implements Vanishable {
    private final Tier tier;
    private final float attackDamage;
    private final Multimap<Attribute, AttributeModifier> defaultModifiers;


    public ClubItem(Tier tier, Item.Properties properties) {
        super(properties);
        this.tier = tier;
        this.attackDamage = (float) 4.0F + tier.getAttackDamageBonus();
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", (double) this.attackDamage, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", (double) -2.8F, AttributeModifier.Operation.ADDITION));
        this.defaultModifiers = builder.build();
    }

    public Tier getTier() {
        return this.tier;
    }

    @Override
    public int getEnchantmentValue() {
        return this.tier.getEnchantmentValue();
    }

    @Override
    public boolean isValidRepairItem(ItemStack itemStack, ItemStack itemStack2) {
        return this.tier.getRepairIngredient().test(itemStack2) || super.isValidRepairItem(itemStack, itemStack2);
    }

    public float getDamage() {
        return this.attackDamage;
    }

    @Override
    public boolean canAttackBlock(BlockState arg, Level arg2, BlockPos arg3, Player arg4) {
        return !arg4.isCreative();
    }

    @Override
    public boolean hurtEnemy(ItemStack arg2, LivingEntity arg22, LivingEntity arg3) {
        arg2.hurtAndBreak(1, arg3, arg -> arg.broadcastBreakEvent(EquipmentSlot.MAINHAND));
        return true;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot arg) {
        return arg == EquipmentSlot.MAINHAND ? this.defaultModifiers : super.getDefaultAttributeModifiers(arg);
    }
}

