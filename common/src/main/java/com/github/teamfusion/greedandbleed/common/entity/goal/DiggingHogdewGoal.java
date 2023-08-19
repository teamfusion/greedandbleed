package com.github.teamfusion.greedandbleed.common.entity.goal;

import com.github.teamfusion.greedandbleed.common.registry.BlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;

public class DiggingHogdewGoal extends MoveToBlockGoal {
    protected boolean digging;
    protected boolean diggingStop;
    protected int diggingTick;
    protected final int actionPoint;
    protected final int animationLengh;

    public DiggingHogdewGoal(PathfinderMob mob, double speed, int searchRange, int actionPoint, int animationLengh) {
        super(mob, speed, searchRange);
        this.actionPoint = actionPoint;
        this.animationLengh = animationLengh;
    }

    @Override
    public boolean canUse() {
        return !this.mob.getMainHandItem().is(BlockRegistry.HOGDEW_FUNGUS.get().asItem()) && super.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        if (this.diggingStop) {
            return false;
        }
        if (this.digging && this.isValidTarget(this.mob.level(), this.blockPos)) {
            return true;
        }
        return super.canContinueToUse();
    }

    @Override
    public void start() {
        super.start();
        this.diggingTick = 0;
        this.digging = false;
        this.diggingStop = false;
    }

    @Override
    public void stop() {
        super.stop();
        this.mob.setPose(Pose.STANDING);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.digging) {
            if (!this.isValidTarget(this.mob.level(), blockPos)) {
                this.diggingStop = true;
            }

            if (this.diggingTick == this.actionPoint) {
                this.mob.level().levelEvent(2001, this.blockPos, Block.getId(this.mob.level().getBlockState(this.blockPos)));
                this.mob.level().removeBlock(blockPos, false);
                this.mob.playSound(SoundEvents.FUNGUS_BREAK);
                this.mob.setItemInHand(InteractionHand.MAIN_HAND, BlockRegistry.HOGDEW_FUNGUS.get().asItem().getDefaultInstance());
                this.mob.setPose(Pose.STANDING);
            }
            if (this.diggingTick == this.animationLengh) {
                this.diggingStop = true;
            }
            ++this.diggingTick;
        } else {
            if (this.isReachedTarget()) {
                if (this.mob.getPose() != Pose.DIGGING) {
                    this.mob.setPose(Pose.DIGGING);
                    this.digging = true;
                }
            }
        }
    }

    @Override
    protected boolean isValidTarget(LevelReader levelReader, BlockPos blockPos) {
        return levelReader.getBlockState(blockPos).is(BlockRegistry.HOGDEW_FUNGUS.get());
    }

    public double acceptedDistance() {
        return 2.0;
    }
}