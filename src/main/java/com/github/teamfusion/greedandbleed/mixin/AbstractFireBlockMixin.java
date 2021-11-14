package com.github.teamfusion.greedandbleed.mixin;

import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractFireBlock.class)
public abstract class AbstractFireBlockMixin extends Block {

	@Shadow
	@Final
	private float fireDamage;

	public AbstractFireBlockMixin(Properties p_i48440_1_) {
		super(p_i48440_1_);
	}

	@Inject(at = @At("HEAD"),
			method = "entityInside", remap = false, cancellable = true)
	public void entityInside(BlockState p_196262_1_, World p_196262_2_, BlockPos p_196262_3_, Entity p_196262_4_, CallbackInfo callbackInfo) {
		if (!p_196262_4_.fireImmune() && (this != Blocks.SOUL_FIRE || !(p_196262_4_ instanceof MobEntity) || ((MobEntity) p_196262_4_).getMobType() != CreatureAttribute.UNDEAD)) {
			p_196262_4_.setRemainingFireTicks(p_196262_4_.getRemainingFireTicks() + 1);
			if (p_196262_4_.getRemainingFireTicks() == 0) {
				p_196262_4_.setSecondsOnFire(8);
			}

			p_196262_4_.hurt(DamageSource.IN_FIRE, this.fireDamage);
		}
		callbackInfo.cancel();
	}
}
