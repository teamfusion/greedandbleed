package com.github.teamfusion.greedandbleed.api;

import net.minecraft.util.SoundCategory;

import javax.annotation.Nullable;

public interface IHogEquipable {
	boolean isHogSaddleable();

	void equipHogSaddle(@Nullable SoundCategory soundCategory);

	boolean isHogSaddled();
}