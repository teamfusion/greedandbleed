package com.github.teamfusion.greedandbleed.common.entity.movecontrol;

import com.github.teamfusion.greedandbleed.common.entity.piglin.WarpedPiglin;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class NoticeDangerFlyingMoveControl extends FlyingMoveControl {
    private final int maxTurn;
    private final boolean hoversInPlace;

    public NoticeDangerFlyingMoveControl(WarpedPiglin warpedPiglin, int maxTurn, boolean hoversInPlace) {
        super(warpedPiglin, maxTurn, hoversInPlace);
        this.maxTurn = maxTurn;
        this.hoversInPlace = hoversInPlace;
    }

    @Override
    public void tick() {
        if (this.operation != MoveControl.Operation.MOVE_TO) {

            if (!this.hoversInPlace) {
                boolean flag = isFallableForMovementBetween(this.mob, this.mob.position(), this.mob.position().add(0, -8F, 0), true);
                if (flag) {
                    this.mob.setNoGravity(false);
                } else if (!this.mob.isNoGravity()) {
                    this.mob.setNoGravity(true);
                }
            }
            this.mob.setYya(0.0f);
            this.mob.setZza(0.0f);
        } else {
            super.tick();
        }
    }

    public static boolean isFallableForMovementBetween(Mob mob, Vec3 vec3, Vec3 vec32, boolean bl) {
        Vec3 vec33 = new Vec3(vec32.x, vec32.y + (double) mob.getBbHeight() * 0.5, vec32.z);
        return mob.level().clip(new ClipContext(vec3, vec33, ClipContext.Block.COLLIDER, bl ? ClipContext.Fluid.ANY : ClipContext.Fluid.NONE, mob)).getType() == HitResult.Type.BLOCK;
    }
}
