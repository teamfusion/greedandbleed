package com.github.teamfusion.greedandbleed.common.entity.goal;

import com.github.teamfusion.greedandbleed.common.entity.piglin.Hoglet;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.EnumSet;

public class AngryForStealerGoal extends Goal {
    protected final Hoglet hoglet;
    protected final double speed;
    protected boolean revengeComplete;

    public AngryForStealerGoal(Hoglet hoglet, double speed) {
        this.setFlags(EnumSet.of(Flag.LOOK, Flag.MOVE));
        this.hoglet = hoglet;
        this.speed = speed;
    }

    @Override
    public boolean canUse() {
        return !this.hoglet.isTame() && this.hoglet.getStealTarget() != null && this.hoglet.getStealTarget().isAlive();
    }

    @Override
    public boolean canContinueToUse() {
        return !this.revengeComplete && super.canContinueToUse();
    }

    @Override
    public void start() {
        super.start();
        this.revengeComplete = false;
    }

    @Override
    public void stop() {
        super.stop();
        this.hoglet.setStealTarget(null);
    }

    private ItemStack findFood(Player player) {
        for (int i = 0; i < player.getInventory().getContainerSize(); ++i) {
            ItemStack itemstack = player.getInventory().getItem(i);
            if (!itemstack.isEmpty() && itemstack.getItem().getFoodProperties() != null) {
                return itemstack.split(1);
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public void tick() {
        super.tick();
        LivingEntity stealer = this.hoglet.getStealTarget();
        if (stealer != null) {
            this.hoglet.getNavigation().moveTo(stealer, this.speed);
            if (this.hoglet.distanceToSqr(stealer) < 6) {
                if (stealer instanceof Player player) {
                    ItemStack stack = findFood(player);
                    if (!stack.isEmpty()) {
                        this.hoglet.eat(this.hoglet.level(), stack);
                    } else {
                        player.hurt(this.hoglet.damageSources().mobAttack(this.hoglet), 0.5F);
                    }
                } else {
                    stealer.hurt(this.hoglet.damageSources().mobAttack(this.hoglet), 0.5F);
                }
                this.revengeComplete = true;
            }
        }
    }
}