package com.github.teamfusion.greedandbleed.common.entity.brain;

import com.github.teamfusion.greedandbleed.common.entity.piglin.pigmy.GBPigmy;
import com.github.teamfusion.greedandbleed.common.entity.piglin.pigmy.Pigmy;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.NetherWartBlock;
import net.minecraft.world.level.block.SoulSandBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FarmingWart extends Behavior<GBPigmy> {
    private static final int HARVEST_DURATION = 200;
    public static final float SPEED_MODIFIER = 0.5F;
    @Nullable
    private BlockPos aboveFarmlandPos;
    private long nextOkStartTime;
    private int timeWorkedSoFar;
    private final List<BlockPos> validFarmlandAroundPigmy = Lists.newArrayList();

    public FarmingWart() {
        super(ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT));
    }

    protected boolean checkExtraStartConditions(ServerLevel serverLevel, GBPigmy villager) {
        if (!serverLevel.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
            return false;
        } else {
            BlockPos.MutableBlockPos mutableBlockPos = villager.blockPosition().mutable();
            this.validFarmlandAroundPigmy.clear();

            for (int i = -1; i <= 1; ++i) {
                for (int j = -1; j <= 1; ++j) {
                    for (int k = -1; k <= 1; ++k) {
                        mutableBlockPos.set(villager.getX() + (double) i, villager.getY() + (double) j, villager.getZ() + (double) k);
                        if (this.validPos(mutableBlockPos, serverLevel)) {
                            this.validFarmlandAroundPigmy.add(new BlockPos(mutableBlockPos));
                        }
                    }
                }
            }

            this.aboveFarmlandPos = this.getValidFarmland(serverLevel);
            return this.aboveFarmlandPos != null;
        }
    }

    @Nullable
    private BlockPos getValidFarmland(ServerLevel serverLevel) {
        return this.validFarmlandAroundPigmy.isEmpty() ? null : (BlockPos) this.validFarmlandAroundPigmy.get(serverLevel.getRandom().nextInt(this.validFarmlandAroundPigmy.size()));
    }

    private boolean validPos(BlockPos blockPos, ServerLevel serverLevel) {
        BlockState blockState = serverLevel.getBlockState(blockPos);
        Block block = blockState.getBlock();
        Block block2 = serverLevel.getBlockState(blockPos.below()).getBlock();
        return block instanceof NetherWartBlock && blockState.getValue(NetherWartBlock.AGE) == 3 || blockState.isAir() && block2 instanceof SoulSandBlock;
    }

    protected void start(ServerLevel serverLevel, GBPigmy villager, long l) {
        if (l > this.nextOkStartTime && this.aboveFarmlandPos != null) {
            villager.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new BlockPosTracker(this.aboveFarmlandPos));
            villager.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(new BlockPosTracker(this.aboveFarmlandPos), 0.6F, 1));
        }

    }

    protected void stop(ServerLevel serverLevel, GBPigmy villager, long l) {
        villager.getBrain().eraseMemory(MemoryModuleType.LOOK_TARGET);
        villager.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
        this.timeWorkedSoFar = 0;
        this.nextOkStartTime = l + 40L;
    }

    protected void tick(ServerLevel serverLevel, GBPigmy pigmy, long l) {
        if (this.aboveFarmlandPos == null || this.aboveFarmlandPos.closerToCenterThan(pigmy.position(), (double) 1.0F)) {
            if (this.aboveFarmlandPos != null && l > this.nextOkStartTime) {
                BlockState blockState = serverLevel.getBlockState(this.aboveFarmlandPos);
                Block block = blockState.getBlock();
                Block block2 = serverLevel.getBlockState(this.aboveFarmlandPos.below()).getBlock();
                if (block instanceof NetherWartBlock && blockState.getValue(NetherWartBlock.AGE) == 3) {
                    serverLevel.destroyBlock(this.aboveFarmlandPos, false, pigmy);
                    BlockState blockState2 = Blocks.NETHER_WART.defaultBlockState().setValue(NetherWartBlock.AGE, 0);
                    serverLevel.setBlockAndUpdate(this.aboveFarmlandPos, blockState2);
                    serverLevel.gameEvent(GameEvent.BLOCK_PLACE, this.aboveFarmlandPos, GameEvent.Context.of(pigmy, blockState2));
                    serverLevel.broadcastEntityEvent(pigmy, (byte) Pigmy.FARMING_ANIMATION_ID);
                    serverLevel.playSound((Player) null, (double) this.aboveFarmlandPos.getX(), (double) this.aboveFarmlandPos.getY(), (double) this.aboveFarmlandPos.getZ(), SoundEvents.NETHER_WART_PLANTED, SoundSource.BLOCKS, 1.0F, 1.0F);

                }

                if (block instanceof NetherWartBlock && blockState.getValue(NetherWartBlock.AGE) != 3) {
                    this.validFarmlandAroundPigmy.remove(this.aboveFarmlandPos);
                    this.aboveFarmlandPos = this.getValidFarmland(serverLevel);
                    if (this.aboveFarmlandPos != null) {
                        this.nextOkStartTime = l + 20L;
                        pigmy.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(new BlockPosTracker(this.aboveFarmlandPos), 0.5F, 1));
                        pigmy.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new BlockPosTracker(this.aboveFarmlandPos));
                    }
                }
            }

            ++this.timeWorkedSoFar;
        }
    }

    protected boolean canStillUse(ServerLevel serverLevel, GBPigmy villager, long l) {
        return this.timeWorkedSoFar < 200;
    }
}
