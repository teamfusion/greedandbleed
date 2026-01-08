package com.github.teamfusion.greedandbleed.api;

import net.minecraft.world.entity.LivingEntity;

public interface IPatbleMob {
    public void responsePet(LivingEntity target);

    boolean isPat();
}
