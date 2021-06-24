package com.infernalstudios.greedandbleed.common.registry;

import com.infernalstudios.greedandbleed.GreedAndBleed;
import com.infernalstudios.greedandbleed.common.enchantment.GBDamageEnchantment;
import com.infernalstudios.greedandbleed.common.entity.piglin.PigmyEntity;
import com.infernalstudios.greedandbleed.common.entity.piglin.SkeletalPiglinEntity;
import net.minecraft.enchantment.DamageEnchantment;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class EnchantmentRegistry {

    public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, GreedAndBleed.MODID);

    public static final RegistryObject<Enchantment> WOE_OF_SWINES =
            ENCHANTMENTS.register("woe_of_swines",
                    () -> new GBDamageEnchantment(Enchantment.Rarity.UNCOMMON, 3, EquipmentSlotType.MAINHAND)
            );
}
