package com.github.teamfusion.greedandbleed.platform.common.forge;

import com.github.teamfusion.greedandbleed.platform.common.MobRegistry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.function.Supplier;

public class MobRegistryImpl {
    public static void attributes(Supplier<? extends EntityType<? extends LivingEntity>> type, Supplier<AttributeSupplier.Builder> builder) {
        FMLJavaModLoadingContext.get().getModEventBus().<EntityAttributeCreationEvent>addListener(event -> event.put(type.get(), builder.get().build()));
    }

    public static Item spawnEgg(Supplier<? extends EntityType<? extends Mob>> entity, int backgroundColor, int highlightColor, Item.Properties properties) {
        return new ForgeSpawnEggItem(entity, backgroundColor, highlightColor, properties);
    }
}