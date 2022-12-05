package com.github.teamfusion.greedandbleed.platform.common;

import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.item.Item;

import java.util.Map;
import java.util.function.Supplier;

public class MobRegistry {
    public static final Map<EntityType<? extends Mob>, Item> EGGS = Maps.newIdentityHashMap();

    @ExpectPlatform
    public static void attributes(Supplier<? extends EntityType<? extends LivingEntity>> type, Supplier<AttributeSupplier.Builder> builder) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static Item spawnEgg(Supplier<? extends EntityType<? extends Mob>> entity, int backgroundColor, int highlightColor, Item.Properties properties) {
        throw new AssertionError();
    }

    public static Iterable<Item> eggs() {
        return Iterables.unmodifiableIterable(EGGS.values());
    }
}