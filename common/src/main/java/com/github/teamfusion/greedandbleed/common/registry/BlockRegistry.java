package com.github.teamfusion.greedandbleed.common.registry;

import com.github.teamfusion.greedandbleed.GreedAndBleed;
import com.github.teamfusion.greedandbleed.platform.CoreRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

public class BlockRegistry {
    public static final CoreRegistry<Block> BLOCKS = CoreRegistry.of(BuiltInRegistries.BLOCK, GreedAndBleed.MOD_ID);

    public static final Supplier<Block> HOGDEW_PLANKS = create("hogdew_planks", () -> new Block(BlockBehaviour.Properties.of()));

    public static <T extends Block> Supplier<T> create(String key, Supplier<T> entry) {
        return BLOCKS.create(key, entry);
    }

    private static <T extends Block> Supplier<T> createBase(String name, Supplier<? extends T> block, Function<Supplier<T>, Supplier<? extends Item>> item) {
        Supplier<T> register = (Supplier<T>) create(name, block);
        Supplier<? extends Item> itemSupplier = item.apply(register);
        ItemRegistry.ITEMS.create(name, itemSupplier);
        return register;
    }

    private static <B extends Block> Supplier<B> register(String name, Supplier<? extends Block> block) {
        return (Supplier<B>) createBase(name, block, (object) -> BlockRegistry.registerBlockItem(object));
    }

    private static <T extends Block> Supplier<BlockItem> registerBlockItem(final Supplier<T> block) {
        return () -> {
            return new BlockItem(Objects.requireNonNull(block.get()), new Item.Properties());
        };
    }
}