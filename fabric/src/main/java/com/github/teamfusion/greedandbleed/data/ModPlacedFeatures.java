package com.github.teamfusion.greedandbleed.data;

import com.github.teamfusion.greedandbleed.GreedAndBleed;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;

public class ModPlacedFeatures {
    public static final ResourceKey<PlacedFeature> HOGDEW_BUBBLE = registerKey("hogdew_bubble");
    public static final ResourceKey<PlacedFeature> HOGDEW_LUMPS = registerKey("hogdew_lumps");
    public static final ResourceKey<PlacedFeature> HOGDEW_PATCH = registerKey("hogdew_patch");
    public static final ResourceKey<PlacedFeature> HOGDEW_FUNGUS = registerKey("hogdew_fungus");

    public static ResourceKey<PlacedFeature> registerKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation(GreedAndBleed.MOD_ID, name));
    }


    public static void bootstrap(BootstapContext<PlacedFeature> context) {

        HolderGetter<ConfiguredFeature<?, ?>> configuredFeature = context.lookup(Registries.CONFIGURED_FEATURE);

        PlacementUtils.register(context, HOGDEW_BUBBLE, configuredFeature.getOrThrow(ModConfiguredFeatures.HOGDEW_BUBBLE), CountOnEveryLayerPlacement.of(3), BiomeFilter.biome());
        PlacementUtils.register(context, HOGDEW_PATCH, configuredFeature.getOrThrow(ModConfiguredFeatures.HOGDEW_PATCH), CountPlacement.of(125), InSquarePlacement.spread(), PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT, EnvironmentScanPlacement.scanningFor(Direction.DOWN, BlockPredicate.solid(), BlockPredicate.ONLY_IN_AIR_PREDICATE, 12), RandomOffsetPlacement.vertical(ConstantInt.of(1)), BiomeFilter.biome());
        PlacementUtils.register(context, HOGDEW_LUMPS, configuredFeature.getOrThrow(ModConfiguredFeatures.HOGDEW_LUMPS), CountPlacement.of(UniformInt.of(100, 160)), InSquarePlacement.spread(), PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT, BiomeFilter.biome());
        PlacementUtils.register(context, HOGDEW_FUNGUS, configuredFeature.getOrThrow(ModConfiguredFeatures.HOGDEW_FUNGUS), CountOnEveryLayerPlacement.of(8), BiomeFilter.biome());
    }
}
