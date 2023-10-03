package com.github.teamfusion.greedandbleed.common.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.BlockPileConfiguration;

public class BlobHollowFeature extends Feature<BlockPileConfiguration> {
    public BlobHollowFeature(Codec<BlockPileConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<BlockPileConfiguration> featurePlaceContext) {
        BlockState blockState;
        BlockPos blockPos = featurePlaceContext.origin();
        WorldGenLevel worldGenLevel = featurePlaceContext.level();
        RandomSource randomSource = featurePlaceContext.random();
        BlockPileConfiguration blockStateConfiguration = featurePlaceContext.config();
        while (blockPos.getY() > worldGenLevel.getMinBuildHeight() + 3 && (worldGenLevel.isEmptyBlock(blockPos.below()))) {
            blockPos = blockPos.below();
        }
        if (blockPos.getY() <= worldGenLevel.getMinBuildHeight() + 3) {
            return false;
        }
        int size = randomSource.nextInt(2) + 2;
        float f = (float) (size + size + size) * 0.333f + 0.5f;
        float f2 = size - 1;
        for (BlockPos blockPos2 : BlockPos.betweenClosed(blockPos.offset(-size, -size, -size), blockPos.offset(size, size, size))) {
            if (!(blockPos2.distSqr(blockPos) <= (double) (f * f))) continue;
            if ((blockPos2.distSqr(blockPos) <= f2 * f2) && !worldGenLevel.getBlockState(blockPos2).is(BlockTags.FEATURES_CANNOT_REPLACE)) {
                worldGenLevel.setBlock(blockPos2, Blocks.AIR.defaultBlockState(), 3);
            } else {
                worldGenLevel.setBlock(blockPos2, blockStateConfiguration.stateProvider.getState(randomSource, blockPos2), 3);
            }
        }

        return true;
    }
}

