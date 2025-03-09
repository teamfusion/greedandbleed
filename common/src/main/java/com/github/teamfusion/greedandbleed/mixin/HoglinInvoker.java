package com.github.teamfusion.greedandbleed.mixin;

import net.minecraft.world.entity.monster.hoglin.Hoglin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Hoglin.class)
public interface HoglinInvoker {
    @Invoker("isImmuneToZombification")
    boolean isImmuneToZombification();
}
