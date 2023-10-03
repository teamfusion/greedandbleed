package com.github.teamfusion.greedandbleed.common.block;

import com.github.teamfusion.greedandbleed.common.registry.BlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.block.MultifaceSpreader;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

import java.util.function.ToIntFunction;

public class HogdewLumpBlock extends MultifaceBlock
        implements BonemealableBlock {
    private static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    private final MultifaceSpreader spreader = new MultifaceSpreader(this);

    public HogdewLumpBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState((BlockState) this.defaultBlockState());
    }

    public static ToIntFunction<BlockState> emission(int i) {
        return blockState -> MultifaceBlock.hasAnyFace(blockState) ? i : 0;
    }

    @Override
    public boolean canBeReplaced(BlockState blockState, BlockPlaceContext blockPlaceContext) {
        return !blockPlaceContext.getItemInHand().is(BlockRegistry.HOGDEW_LUMPS.get().asItem()) || super.canBeReplaced(blockState, blockPlaceContext);
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader levelReader, BlockPos blockPos, BlockState blockState, boolean bl) {
        return Direction.stream().anyMatch(direction -> this.spreader.canSpreadInAnyDirection(blockState, levelReader, blockPos, direction.getOpposite()));
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource randomSource, BlockPos blockPos, BlockState blockState) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel serverLevel, RandomSource randomSource, BlockPos blockPos, BlockState blockState) {
        this.spreader.spreadFromRandomFaceTowardRandomDirection(blockState, serverLevel, blockPos, randomSource);
    }

    @Override
    public boolean propagatesSkylightDown(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return blockState.getFluidState().isEmpty();
    }

    @Override
    public MultifaceSpreader getSpreader() {
        return this.spreader;
    }
}

