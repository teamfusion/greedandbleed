package com.github.teamfusion.greedandbleed.common.entity.goal;

import com.github.teamfusion.greedandbleed.common.entity.TraceAndSetOwner;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;

import java.util.EnumSet;

public class TracedOwnerHurtTargetGoal<T extends Mob & TraceAndSetOwner> extends TargetGoal {
    private final T tameAnimal;
    private LivingEntity ownerLastHurtBy;
    private int timestamp;

    public TracedOwnerHurtTargetGoal(T tamableAnimal) {
        super(tamableAnimal, false);
        this.tameAnimal = tamableAnimal;
        this.setFlags(EnumSet.of(Flag.TARGET));
    }

    @Override
    public boolean canUse() {
        if (this.tameAnimal.getOwner() == null) {
            return false;
        }
        Entity entity = this.tameAnimal.getOwner();
        if (!(entity instanceof LivingEntity livingEntity)) {
            return false;
        }
        this.ownerLastHurtBy = livingEntity.getLastHurtMob();
        int i = livingEntity.getLastHurtMobTimestamp();
        return i != this.timestamp && this.canAttack(this.ownerLastHurtBy, TargetingConditions.DEFAULT);
    }

    @Override
    public void start() {
        this.mob.setTarget(this.ownerLastHurtBy);
        Entity entity = this.tameAnimal.getOwner();
        if ((entity instanceof LivingEntity livingEntity)) {

            if (livingEntity != null) {
                this.timestamp = livingEntity.getLastHurtMobTimestamp();
            }
        }
        super.start();
    }
}

