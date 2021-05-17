package com.infernalstudios.greedandbleed.common.entity;


public interface IToleratingMount {

    //@OnlyIn(Dist.CLIENT)
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
