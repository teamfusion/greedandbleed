package com.github.teamfusion.greedandbleed.mixin;

import net.minecraft.world.level.block.state.properties.BlockSetType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BlockSetType.class)
public interface BlockSetTypeInvoker {
    @Invoker("register")
    public static BlockSetType register(BlockSetType woodType) {
        throw new AssertionError();
    }
}
