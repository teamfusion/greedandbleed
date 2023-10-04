package com.github.teamfusion.greedandbleed.mixin;

import net.minecraft.world.level.block.state.properties.WoodType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(WoodType.class)
public interface WoodTypeInvoker {
    @Invoker("register")
    public static WoodType register(WoodType woodType) {
        throw new AssertionError();
    }
}
