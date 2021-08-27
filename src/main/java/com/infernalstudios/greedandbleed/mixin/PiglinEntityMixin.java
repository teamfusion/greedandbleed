package com.infernalstudios.greedandbleed.mixin;

import com.infernalstudios.greedandbleed.common.entity.piglin.SkeletalPiglinEntity;
import com.infernalstudios.greedandbleed.common.registry.EntityTypeRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.piglin.AbstractPiglinEntity;
import net.minecraft.entity.monster.piglin.PiglinEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PiglinEntity.class)
public abstract class PiglinEntityMixin extends AbstractPiglinEntity {
	public PiglinEntityMixin(EntityType<? extends AbstractPiglinEntity> p_i241915_1_, World p_i241915_2_) {
		super(p_i241915_1_, p_i241915_2_);
	}

	@Override
	public void die(DamageSource p_70645_1_) {

		if (p_70645_1_ == DamageSource.IN_FIRE) {
			BlockState state = this.level.getBlockState(this.blockPosition());

			if (state.getBlock() == Blocks.SOUL_FIRE) {
				SkeletalPiglinEntity zombifiedpiglinentity = this.convertTo(EntityTypeRegistry.SKELETAL_PIGLIN.get(), true);
				if (zombifiedpiglinentity != null) {
					zombifiedpiglinentity.addEffect(new EffectInstance(Effects.CONFUSION, 200, 0));
					net.minecraftforge.event.ForgeEventFactory.onLivingConvert(this, zombifiedpiglinentity);
				}
			}
		}

		super.die(p_70645_1_);
	}
}
