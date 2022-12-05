package com.github.teamfusion.greedandbleed.platform.common.fabric;

import com.github.teamfusion.greedandbleed.platform.common.MobRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;

import java.util.function.Supplier;

@SuppressWarnings("ConstantConditions")
public class MobRegistryImpl {
    public static void attributes(Supplier<? extends EntityType<? extends LivingEntity>> type, Supplier<AttributeSupplier.Builder> builder) {
        FabricDefaultAttributeRegistry.register(type.get(), builder.get());
    }

    public static Item spawnEgg(Supplier<? extends EntityType<? extends Mob>> entity, int backgroundColor, int highlightColor, Item.Properties properties) {
        Item entry = new SpawnEggItem(entity.get(), backgroundColor, highlightColor, properties);
        MobRegistry.EGGS.put(entity.get(), entry);
        return entry;
    }
}