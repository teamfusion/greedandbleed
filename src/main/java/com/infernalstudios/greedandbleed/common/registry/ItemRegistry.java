package com.infernalstudios.greedandbleed.common.registry;

import com.infernalstudios.greedandbleed.GreedAndBleed;
import com.infernalstudios.greedandbleed.common.item.GBOnAStickItem;
import com.infernalstudios.greedandbleed.common.item.HoglinArmorItem;
import com.infernalstudios.greedandbleed.common.item.HoglinSaddleItem;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.SaddleItem;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemRegistry {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, GreedAndBleed.MODID);

    public static final RegistryObject<Item> CRIMSON_FUNGUS_ON_A_STICK = ITEMS.register(
            "crimson_fungus_on_a_stick", () ->
                    new GBOnAStickItem((
                            new Item.Properties())
                            .durability(100)
                            .tab(ItemGroup.TAB_TRANSPORTATION),
                            EntityType.HOGLIN,
                            1)
    );
    public static final RegistryObject<Item> GOLDEN_HOGLIN_ARMOR = ITEMS.register(
            "golden_hoglin_armor", () ->
                    new HoglinArmorItem(7, "gold",
                            (new Item.Properties())
                            .stacksTo(1)
                            .tab(ItemGroup.TAB_MISC))
    );
    public static final RegistryObject<Item> NETHERITE_HOGLIN_ARMOR = ITEMS.register(
            "netherite_hoglin_armor", () ->
                    new HoglinArmorItem(11, "netherite",
                            (new Item.Properties())
                            .stacksTo(1)
                            .tab(ItemGroup.TAB_MISC))
    );

    public static final RegistryObject<Item> HOGLIN_SADDLE = ITEMS.register(
            "hoglin_saddle", () ->
                    new HoglinSaddleItem(
                            (new Item.Properties())
                                    .stacksTo(1)
                                    .tab(ItemGroup.TAB_TRANSPORTATION))
    );
}
