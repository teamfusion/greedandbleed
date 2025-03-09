package com.github.teamfusion.greedandbleed.mixin;

import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(AbstractPiglin.class)
public interface AbstractPiglinInvoker {
    @Invoker("isImmuneToZombification")
    boolean isImmuneToZombification();
}
