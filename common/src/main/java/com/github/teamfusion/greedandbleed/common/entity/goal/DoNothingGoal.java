package com.github.teamfusion.greedandbleed.common.entity.goal;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class DoNothingGoal extends Goal {
    public final PathfinderMob pathfinderMob;

    public DoNothingGoal(PathfinderMob pathfinderMob) {
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP, Goal.Flag.LOOK));
        this.pathfinderMob = pathfinderMob;
    }

    @Override
    public boolean canUse() {
        return this.pathfinderMob.getPose() == Pose.EMERGING;
    }
}
