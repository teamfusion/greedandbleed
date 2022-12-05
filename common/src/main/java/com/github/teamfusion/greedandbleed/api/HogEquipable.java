package com.github.teamfusion.greedandbleed.api;

import net.minecraft.sounds.SoundSource;
import org.jetbrains.annotations.Nullable;

public interface HogEquipable {
    boolean isHogSaddleable();

    void equipHogSaddle(@Nullable SoundSource soundSource);

    boolean isHogSaddled();
}