package com.github.teamfusion.greedandbleed.common.integrations;

import com.blackgear.platform.common.data.LootModifier;
import com.github.teamfusion.greedandbleed.common.registry.EnchantmentRegistry;
import com.github.teamfusion.greedandbleed.common.registry.ItemRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.EnchantRandomlyFunction;

public class LootIntegrations implements LootModifier.LootTableModifier {
    public static final LootIntegrations INSTANCE = new LootIntegrations();
    
    @Override
    public void modify(ResourceLocation path, LootModifier.LootTableContext context, boolean builtin) {
        if (path == BuiltInLootTables.NETHER_BRIDGE) {
            context.addPool(LootPool.lootPool().add(
                LootItem.lootTableItem(Items.ENCHANTED_BOOK)
                    .apply(new EnchantRandomlyFunction.Builder().withEnchantment(EnchantmentRegistry.WOE_OF_SWINES.get()))
            ));
        }
        
        if (path == EntityType.PIGLIN.getDefaultLootTable()) {
            context.addPool(LootPool.lootPool().add(
                LootItem.lootTableItem(ItemRegistry.PIGLIN_BELT.get())
            ));
        }
    }
}