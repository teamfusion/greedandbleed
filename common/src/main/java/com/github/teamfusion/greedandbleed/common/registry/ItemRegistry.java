package com.github.teamfusion.greedandbleed.common.registry;

import com.github.teamfusion.greedandbleed.GreedAndBleed;
import com.github.teamfusion.greedandbleed.common.item.*;
import com.github.teamfusion.greedandbleed.common.item.slingshot.SlingshotItem;
import com.github.teamfusion.greedandbleed.common.item.slingshot.SlingshotPouchItem;
import com.github.teamfusion.greedandbleed.platform.CoreRegistry;
import com.github.teamfusion.greedandbleed.platform.common.MobRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.Tiers;

import java.util.function.Supplier;

public class ItemRegistry {
    public static final CoreRegistry<Item> ITEMS = CoreRegistry.of(BuiltInRegistries.ITEM, GreedAndBleed.MOD_ID);

    public static final Supplier<Item> CRIMSON_FUNGUS_ON_A_STICK = create("crimson_fungus_on_a_stick", () -> new GBOnAStickWithHoglinItem(new Item.Properties().durability(220), 1));
    public static final Supplier<Item> GOLDEN_HOGLIN_ARMOR = create("golden_hoglin_armor", () -> new HoglinArmorItem(7, "gold", new Item.Properties().stacksTo(1)));
    public static final Supplier<Item> NETHERITE_HOGLIN_ARMOR = create("netherite_hoglin_armor", () -> new HoglinArmorItem(11, "netherite", new Item.Properties().stacksTo(1)));
    public static final Supplier<Item> HOGLIN_SADDLE = create("hoglin_saddle", () -> new HoglinSaddleItem(new Item.Properties().stacksTo(1)));
    public static final Supplier<Item> AMULET = create("amulet", () -> new AmuletItem(new Item.Properties().stacksTo(1)));
    public static final Supplier<Item> PIGLIN_BELT = create("piglin_belt", () -> new Item(new Item.Properties()));

    public static final Supplier<Item> SLINGSHOT = create("slingshot", () -> new SlingshotItem(new Item.Properties().durability(320)));

    public static final Supplier<Item> PEBBLE = create("pebble", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> CRIMSON_FUNGUS = create("crimson_fungus", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> SLINGSHOT_POUCH = create("slingshot_pouch", () -> new SlingshotPouchItem(new Item.Properties().stacksTo(1)));
    public static final Supplier<Item> CLUB = create("club", () -> new ClubItem(Tiers.GOLD, new Item.Properties().stacksTo(1).durability(82)));
    public static final Supplier<Item> STONE_CLUB = create("stone_club", () -> new ClubItem(Tiers.STONE, new Item.Properties().stacksTo(1).durability(102)));
    public static final Supplier<Item> GOLDEN_SHIELD = create("golden_shield", () -> new ShieldItem(new Item.Properties().stacksTo(1).durability(84)));


    public static final Supplier<Item> SKELETAL_PIGLIN_SPAWN_EGG = create("skeletal_piglin_spawn_egg", () -> MobRegistry.spawnEgg(EntityTypeRegistry.SKELETAL_PIGLIN, 12698049, 4802889, new Item.Properties()));
    public static final Supplier<Item> HOGLET_SPAWN_EGG = create("hoglet_spawn_egg", () -> MobRegistry.spawnEgg(EntityTypeRegistry.HOGLET, 10051392, 16380836, new Item.Properties()));
    public static final Supplier<Item> ZOGLET_SPAWN_EGG = create("zoglet_spawn_egg", () -> MobRegistry.spawnEgg(EntityTypeRegistry.ZOGLET, 13004373, 0xE6E6E6, new Item.Properties()));
    public static final Supplier<Item> SKELET_SPAWN_EGG = create("skoglet_spawn_egg", () -> MobRegistry.spawnEgg(EntityTypeRegistry.SKOGLET, 0xC1C1C1, 0x494949, new Item.Properties()));
    public static final Supplier<Item> SHAMAN_PIGLIN_SPAWN_EGG = create("shaman_piglin_spawn_egg", () -> MobRegistry.spawnEgg(EntityTypeRegistry.SHAMAN_PIGLIN, 0xF2BA86, 0xC1C1C1, new Item.Properties()));
    public static final Supplier<Item> PIGMY_SPAWN_EGG = create("pygmy_spawn_egg", () -> MobRegistry.spawnEgg(EntityTypeRegistry.PYGMY, 0xAD5532, 0xF2BA86, new Item.Properties()));
    public static final Supplier<Item> SHRYGMY_SPAWN_EGG = create("shrygmy_spawn_egg", () -> MobRegistry.spawnEgg(EntityTypeRegistry.SHRYGMY, 0xAD5532, 0xC53038, new Item.Properties()));
    public static final Supplier<Item> HOGGART_SPAWN_EGG = create("hoggart_spawn_egg", () -> MobRegistry.spawnEgg(EntityTypeRegistry.HOGGART, 0xAC4138, 0xF2BA86, new Item.Properties()));
    public static final Supplier<Item> WARPED_PIGLIN_SPAWN_EGG = create("warped_piglin_spawn_egg", () -> MobRegistry.spawnEgg(EntityTypeRegistry.WARPED_PIGLIN, 10051392, 0x167E86, new Item.Properties()));

    public static <T extends Item> Supplier<T> create(String key, Supplier<T> entry) {
        return ITEMS.create(key, entry);
    }
}