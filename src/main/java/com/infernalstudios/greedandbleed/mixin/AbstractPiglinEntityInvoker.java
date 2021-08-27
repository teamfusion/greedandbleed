package com.infernalstudios.greedandbleed.mixin;

import net.minecraft.entity.monster.piglin.AbstractPiglinEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(AbstractPiglinEntity.class)
public interface AbstractPiglinEntityInvoker {
	@Invoker("canHunt")
	boolean canHunt();
}
