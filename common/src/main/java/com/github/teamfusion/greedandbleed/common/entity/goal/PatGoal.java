package com.github.teamfusion.greedandbleed.common.entity.goal;

import com.github.teamfusion.greedandbleed.api.IPatbleMob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class PatGoal extends Goal {
    public final PathfinderMob pathfinderMob;

    public PatGoal(PathfinderMob pathfinderMob) {
        this.pathfinderMob = pathfinderMob;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        return this.pathfinderMob instanceof IPatbleMob patbleMob && this.pathfinderMob.getTarget() != null && patbleMob.isPat();
    }

    @Override
    public void start() {
        super.start();
        this.pathfinderMob.getNavigation().stop();
    }
}