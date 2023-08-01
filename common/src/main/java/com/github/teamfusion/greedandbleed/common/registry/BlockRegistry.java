package com.github.teamfusion.greedandbleed.common.registry;

import com.github.teamfusion.greedandbleed.GreedAndBleed;
import com.github.teamfusion.greedandbleed.platform.CoreRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

public class BlockRegistry {
    public static final CoreRegistry<Block> BLOCKS = CoreRegistry.of(BuiltInRegistries.BLOCK, GreedAndBleed.MOD_ID);

    public static final Supplier<Block> HOGDEW_PLANKS = createWithItem("hogdew_planks", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_PINK).instrument(NoteBlockInstrument.BASS).strength(2.0f, 3.0f).sound(SoundType.NETHER_WOOD)));
    public static final Supplier<Block> HOGDEW_PLANKS_STAIRS = createWithItem("hogdew_stairs", () -> new StairBlock(HOGDEW_PLANKS.get().defaultBlockState(), BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_PINK).instrument(NoteBlockInstrument.BASS).strength(2.0f, 3.0f).sound(SoundType.NETHER_WOOD)));
    public static final Supplier<Block> HOGDEW_PLANKS_SLAB = createWithItem("hogdew_slab", () -> new SlabBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_PINK).instrument(NoteBlockInstrument.BASS).strength(2.0f, 3.0f).sound(SoundType.NETHER_WOOD)));
    public static final Supplier<Block> HOGDEW_WART_BLOCK = createWithItem("hogdew_wart_block", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_PINK).instrument(NoteBlockInstrument.BASS).strength(1.0f).sound(SoundType.NETHER_WART)));
    public static final Supplier<Block> HOGDEW_STEM = createWithItem("hogdew_stem", () -> netherStem(MapColor.COLOR_PINK));
    public static final Supplier<Block> STRIPPED_HOGDEW_STEM = createWithItem("stripped_hogdew_stem", () -> netherStem(MapColor.COLOR_PINK));
    public static final Supplier<Block> HOGDEW_NYLIUM = createWithItem("hogdew_nylium", () -> new NyliumBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_PINK).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(0.4f).sound(SoundType.NYLIUM).randomTicks()));
    public static final Supplier<Block> HOGDEW_FUNGUS = createWithItem("hogdew_fungus", () -> new NetherSproutsBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_PINK).replaceable().noCollission().instabreak().sound(SoundType.FUNGUS).offsetType(BlockBehaviour.OffsetType.XZ).pushReaction(PushReaction.DESTROY)));

    public static final Supplier<Block> HOGDEW_LUMPS = createWithItem("hogdew_lumps", () -> new GlowLichenBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_PINK).replaceable().noCollission().strength(0.2f).sound(SoundType.FUNGUS).lightLevel(GlowLichenBlock.emission(10)).pushReaction(PushReaction.DESTROY)));

    private static Block netherStem(MapColor mapColor) {
        return new RotatedPillarBlock(BlockBehaviour.Properties.of().mapColor(blockState -> mapColor).instrument(NoteBlockInstrument.BASS).strength(2.0f).sound(SoundType.STEM));
    }

    public static <T extends Block> Supplier<T> create(String key, Supplier<T> entry) {
        return BLOCKS.create(key, entry);
    }

    private static <T extends Block> Supplier<T> createBase(String name, Supplier<? extends T> block, Function<Supplier<T>, Supplier<? extends Item>> item) {
        Supplier<T> register = (Supplier<T>) create(name, block);
        Supplier<? extends Item> itemSupplier = item.apply(register);
        ItemRegistry.ITEMS.create(name, itemSupplier);
        return register;
    }

    private static <B extends Block> Supplier<B> createWithItem(String name, Supplier<? extends Block> block) {
        return (Supplier<B>) createBase(name, block, (object) -> BlockRegistry.registerBlockItem(object));
    }

    private static <T extends Block> Supplier<BlockItem> registerBlockItem(final Supplier<T> block) {
        return () -> {
            return new BlockItem(Objects.requireNonNull(block.get()), new Item.Properties());
        };
    }
}