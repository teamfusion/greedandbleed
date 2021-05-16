package com.infernalstudios.greedandbleed.common.entity;


public interface IActionMount {

    //@OnlyIn(Dist.CLIENT)
    void onPlayerInput(int powerIn);

    boolean canPerformMountAction();

    void handleStartMountAction(int powerIn);

    void handleStopMountAction();
}
