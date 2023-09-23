package com.github.teamfusion.greedandbleed.common.registry;

import com.github.teamfusion.greedandbleed.GreedAndBleed;
import com.github.teamfusion.greedandbleed.platform.CoreRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;

import java.util.function.Supplier;
import java.util.stream.Stream;

public class CreativeTabRegistry {
    public static final CoreRegistry<CreativeModeTab> CREATIVE_TABS = CoreRegistry.of(BuiltInRegistries.CREATIVE_MODE_TAB, GreedAndBleed.MOD_ID);

    public static final Supplier<CreativeModeTab> GLEED_AND_BLEED = create("gleed_and_bleed", () -> dev.architectury.registry.CreativeTabRegistry.create(builder -> {
        builder.title(Component.translatable("itemGroup.greed_and_bleed"))
                .icon(() -> ItemRegistry.GOLDEN_HOGLIN_ARMOR.get().getDefaultInstance())
                .displayItems((parameters, output) -> {
                    output.acceptAll(Stream.of(
                            ItemRegistry.GOLDEN_HOGLIN_ARMOR,
                            ItemRegistry.NETHERITE_HOGLIN_ARMOR,
                            ItemRegistry.HOGLIN_SADDLE,
                            ItemRegistry.CRIMSON_FUNGUS_ON_A_STICK,
                            ItemRegistry.HOGLET_SPAWN_EGG,
                            ItemRegistry.ZOGLET_SPAWN_EGG,
                            ItemRegistry.SKELET_SPAWN_EGG,
                            ItemRegistry.SKELETAL_PIGLIN_SPAWN_EGG
                    ).map(sup -> {
                        return sup.get().getDefaultInstance();
                    }).toList());
                    output.acceptAll(Stream.of(
                            BlockRegistry.HOGDEW_NYLIUM,
                            BlockRegistry.HOGDEW_STEM,
                            BlockRegistry.STRIPPED_HOGDEW_STEM,
                            BlockRegistry.HOGDEW_PLANKS,
                            BlockRegistry.HOGDEW_PLANKS_STAIRS,
                            BlockRegistry.HOGDEW_PLANKS_SLAB,
                            BlockRegistry.HOGDEW_WART_BLOCK,
                            BlockRegistry.HOGDEW_CLUSTER,
                            BlockRegistry.HOGDEW_FUNGUS,
                            BlockRegistry.HOGDEW_LUMPS
                    ).map(sup -> {
                        return sup.get().asItem().getDefaultInstance();
                    }).toList());
                    output.accept(PotionUtils.setPotion(new ItemStack(Items.POTION), PotionRegistry.IMMUNITY_POTION.get()));
                    output.accept(PotionUtils.setPotion(new ItemStack(Items.POTION), PotionRegistry.STRONG_IMMUNITY_POTION.get()));
                    output.accept(PotionUtils.setPotion(new ItemStack(Items.POTION), PotionRegistry.LONG_IMMUNITY_POTION.get()));
                }).build();
    }));

    public static <T extends CreativeModeTab> Supplier<T> create(String key, Supplier<T> entry) {
        return CREATIVE_TABS.create(key, entry);
    }
}