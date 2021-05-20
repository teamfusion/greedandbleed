package com.infernalstudios.greedandbleed.common.entity;


import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public interface IToleratingMount {

    boolean canAcceptSaddle();

    default boolean isSaddleStack(ItemStack stack){
        return this.isSaddleItem(stack.getItem());
    }

    default boolean isSaddleItem(Item item){
        return item == this.getDefaultSaddleItem();
    }

    Item getDefaultSaddleItem();

    void setTolerance(int valueIn);

    int getTolerance();

    int getMaxTolerance();

    default float getToleranceProgress(){
        return (float)this.getTolerance() / (float)this.getMaxTolerance();
    }

    default boolean isTolerating() {
        return this.getTolerance() > 0;
    }

    default void addTolerance(int valueIn){
        this.setTolerance(this.getTolerance() + valueIn);
    }

    boolean canPerformMountAction();

    void handleStartMountAction();

    void handleStopMountAction();
}
