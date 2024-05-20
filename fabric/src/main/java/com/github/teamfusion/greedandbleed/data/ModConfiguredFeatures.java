package com.github.teamfusion.greedandbleed.data;

import com.github.teamfusion.greedandbleed.GreedAndBleed;
import com.github.teamfusion.greedandbleed.common.registry.BlockRegistry;
import com.github.teamfusion.greedandbleed.common.registry.FeatureRegistry;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.HugeFungusConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.*;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.placement.CaveSurface;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;

import java.util.List;

public class ModConfiguredFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> HOGDEW_BUBBLE = registerKey("hogdew_bubble");
    public static final ResourceKey<ConfiguredFeature<?, ?>> HOGDEW_FUNGUS = registerKey("hogdew_fungus");
    public static final ResourceKey<ConfiguredFeature<?, ?>> HOGDEW_LUMPS = registerKey("hogdew_lumps");
    public static final ResourceKey<ConfiguredFeature<?, ?>> HOGDEW_HOLLOW_VEGITATION = registerKey("hogdew_hollow_vegitation");
    public static final ResourceKey<ConfiguredFeature<?, ?>> HOGDEW_PATCH = registerKey("hogdew_patch");
    
    public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(GreedAndBleed.MOD_ID, name));
    }

    public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context) {
        HolderGetter<ConfiguredFeature<?, ?>> holderGetter = context.lookup(Registries.CONFIGURED_FEATURE);
        BlockPredicate blockPredicate = BlockPredicate.matchesBlocks(BlockRegistry.HOGDEW_FUNGUS.get());

        FeatureUtils.register(context, HOGDEW_BUBBLE, FeatureRegistry.BLOB_HOLLOW.get(), new BlockPileConfiguration(BlockStateProvider.simple(BlockRegistry.HOGDEW_CLUSTER.get())));
        FeatureUtils.register(context, HOGDEW_FUNGUS, Feature.HUGE_FUNGUS, new HugeFungusConfiguration(BlockRegistry.HOGDEW_NYLIUM.get().defaultBlockState(), BlockRegistry.HOGDEW_STEM.get().defaultBlockState(), BlockRegistry.HOGDEW_WART_BLOCK.get().defaultBlockState(), Blocks.SHROOMLIGHT.defaultBlockState(), blockPredicate, false));
        FeatureUtils.register(context, HOGDEW_LUMPS, Feature.MULTIFACE_GROWTH, new MultifaceGrowthConfiguration(BlockRegistry.HOGDEW_LUMPS.get(), 20, true, true, true, 1.0f, HolderSet.direct(Block::builtInRegistryHolder, BlockRegistry.HOGDEW_CLUSTER.get(), BlockRegistry.HOGDEW_WART_BLOCK.get())));
        FeatureUtils.register(context, HOGDEW_HOLLOW_VEGITATION, Feature.RANDOM_PATCH, FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(BlockRegistry.HOGDEW_FUNGUS.get())), List.of(BlockRegistry.HOGDEW_NYLIUM.get())));
        FeatureUtils.register(context, HOGDEW_PATCH, Feature.VEGETATION_PATCH, new VegetationPatchConfiguration(BlockTags.BASE_STONE_NETHER, new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder().add(BlockRegistry.HOGDEW_NYLIUM.get().defaultBlockState(), 5).add(BlockRegistry.HOGDEW_CLUSTER.get().defaultBlockState(), 1).build()), PlacementUtils.inlinePlaced(holderGetter.getOrThrow(HOGDEW_HOLLOW_VEGITATION), new PlacementModifier[0]), CaveSurface.FLOOR, ConstantInt.of(4), 0.3F, 5, 0.00F, UniformInt.of(6, 8), 0.7F));
    }

    private static RandomPatchConfiguration grassPatch(BlockStateProvider p_195203_, int p_195204_) {
        return FeatureUtils.simpleRandomPatchConfiguration(p_195204_, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(p_195203_)));
    }
}
