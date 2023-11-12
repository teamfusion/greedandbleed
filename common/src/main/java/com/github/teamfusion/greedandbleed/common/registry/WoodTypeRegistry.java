package com.github.teamfusion.greedandbleed.common.registry;

import com.github.teamfusion.greedandbleed.mixin.WoodTypeInvoker;
import net.minecraft.world.level.block.state.properties.WoodType;

public class WoodTypeRegistry {
    public static final WoodType HOGDEW = WoodTypeInvoker.register(new WoodType("greedandbleed:hogdew", BlockSetTypeRegistry.HOGDEW));

}
