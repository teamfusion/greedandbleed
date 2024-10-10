package com.github.teamfusion.greedandbleed.data;

import com.github.teamfusion.greedandbleed.GreedAndBleed;
import com.github.teamfusion.greedandbleed.common.registry.ItemRegistry;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.EnchantWithLevelsFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemDamageFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.function.BiConsumer;

public class ChestLootTableGenerator extends SimpleFabricLootTableProvider {
    public static final ResourceLocation SHAMAN_BASE = new ResourceLocation(GreedAndBleed.MOD_ID, "chests/shaman_base");

    public ChestLootTableGenerator(FabricDataOutput dataGenerator) {
        super(dataGenerator, LootContextParamSets.CHEST);
    }

    @Override
    public void generate(BiConsumer<ResourceLocation, LootTable.Builder> biConsumer) {
        biConsumer.accept(SHAMAN_BASE, LootTable.lootTable()
                .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(3.0F, 4.0F))
                        .add(LootItem.lootTableItem(Items.GOLD_INGOT).setWeight(6)).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 4.0F)))
                        .add(LootItem.lootTableItem(Items.GOLD_NUGGET).setWeight(10)).apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 8.0F)))
                        .add(LootItem.lootTableItem(ItemRegistry.HOGLIN_SADDLE.get()).setWeight(4))
                )

                .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(2.0F, 3.0F))
                        .add(LootItem.lootTableItem(ItemRegistry.GOLDEN_SHIELD.get()).setWeight(10))
                        .add(LootItem.lootTableItem(Items.GOLDEN_HELMET).setWeight(5).apply(EnchantWithLevelsFunction.enchantWithLevels(UniformGenerator.between(5, 15))))
                        .add(LootItem.lootTableItem(Items.GOLDEN_CHESTPLATE).setWeight(5).apply(EnchantWithLevelsFunction.enchantWithLevels(UniformGenerator.between(5, 15))))
                        .add(LootItem.lootTableItem(Items.GOLDEN_LEGGINGS).setWeight(5).apply(EnchantWithLevelsFunction.enchantWithLevels(UniformGenerator.between(5, 15))))
                        .add(LootItem.lootTableItem(Items.GOLDEN_BOOTS).setWeight(5).apply(EnchantWithLevelsFunction.enchantWithLevels(UniformGenerator.between(5, 15))))
                )

                .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(4.0F, 6.0F))
                        .add(LootItem.lootTableItem(Items.BONE).setWeight(15)).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 3.0F)))
                        .add(LootItem.lootTableItem(Items.ROTTEN_FLESH).setWeight(15)).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 3.0F)))
                        .add(LootItem.lootTableItem(Items.CRIMSON_FUNGUS).setWeight(10)).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 3.0F)))
                        .add(LootItem.lootTableItem(Items.STRING).setWeight(10)).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 3.0F)))
                        .add(LootItem.lootTableItem(Items.NETHER_WART).setWeight(5)).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 3.0F)))
                        .add(LootItem.lootTableItem(ItemRegistry.CRIMSON_FUNGUS_ON_A_STICK.get()).setWeight(3)).apply(SetItemDamageFunction.setDamage(UniformGenerator.between(0.1F, 0.5F)))
                )

        );

    }
}