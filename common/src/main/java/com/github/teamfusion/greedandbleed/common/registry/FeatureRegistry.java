package com.github.teamfusion.greedandbleed.common.registry;

import com.github.teamfusion.greedandbleed.GreedAndBleed;
import com.github.teamfusion.greedandbleed.common.feature.BlobHollowFeature;
import com.github.teamfusion.greedandbleed.platform.CoreRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.BlockPileConfiguration;

import java.util.function.Supplier;

public class FeatureRegistry {
    public static final CoreRegistry<Feature<?>> FEATURES = CoreRegistry.of(BuiltInRegistries.FEATURE, GreedAndBleed.MOD_ID);
    public static final Supplier<Feature<BlockPileConfiguration>> BLOB_HOLLOW = FEATURES.create("blob_hollow", () -> new BlobHollowFeature(BlockPileConfiguration.CODEC));

}
