package com.github.teamfusion.greedandbleed.common.entity.piglin;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.level.Level;

public abstract class GBPygmy extends GBPiglin {
    public GBPygmy(EntityType<? extends AbstractPiglin> entityType, Level level) {
        super(entityType, level);
    }
}
