package com.infernalstudios.greedandbleed.common.registry;

import com.infernalstudios.greedandbleed.GreedAndBleed;
import com.infernalstudios.greedandbleed.common.item.GBOnAStickItem;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
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
}
