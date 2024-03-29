package com.github.teamfusion.greedandbleed.common.entity;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public interface ToleratingMount {
    boolean canAcceptSaddle();

    default boolean isSaddleStack(ItemStack stack) {
        return this.isSaddleItem(stack.getItem());
    }

    default boolean isSaddleItem(Item item) {
        return item == this.getDefaultSaddleItem();
    }

    Item getDefaultSaddleItem();

    void setTolerance(int value);

    int getTolerance();

    int getMaxTolerance();

    default float getToleranceProgress() {
        return (float)this.getTolerance() / (float)this.getMaxTolerance();
    }

    default boolean isTolerating() {
        return this.getTolerance() > 0;
    }

    default void addTolerance(int value) {
        this.setTolerance(this.getTolerance() + value);
    }

    boolean canPerformMountAction();

    void handleStartMountAction();

    void handleStopMountAction();
}