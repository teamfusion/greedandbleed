package com.github.teamfusion.greedandbleed.mixin;

import com.github.teamfusion.greedandbleed.common.entity.piglin.SkeletalPiglinEntity;
import com.github.teamfusion.greedandbleed.common.registry.EntityTypeRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.piglin.AbstractPiglinEntity;
import net.minecraft.entity.monster.piglin.PiglinEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("NullableProblems")
@Mixin(PiglinEntity.class)
public abstract class PiglinEntityMixin extends AbstractPiglinEntity {
	private static final DataParameter<Boolean> DATA_ON_SOUL_FIRE = EntityDataManager.defineId(PiglinEntity.class, DataSerializers.BOOLEAN);


	public PiglinEntityMixin(EntityType<? extends AbstractPiglinEntity> p_i241915_1_, World p_i241915_2_) {
		super(p_i241915_1_, p_i241915_2_);
	}

	@Inject(at = @At("TAIL"),
			method = "defineSynchedData", remap = false)
	private void defineSynchedData(CallbackInfo callbackInfo) {
		this.entityData.define(DATA_ON_SOUL_FIRE, false);
	}

	@Inject(at = @At("TAIL"),
			method = "addAdditionalSaveData", remap = false)
	private void addAdditionalData(CompoundNBT compoundNBT, CallbackInfo callbackInfo) {
		compoundNBT.putBoolean("OnSoulFire", this.isOnSoulFire());

	}

	@Inject(at = @At("TAIL"),
			method = "readAdditionalSaveData", remap = false)
	private void readAdditionalData(CompoundNBT compoundNBT, CallbackInfo callbackInfo) {
		this.setOnSoulFire(compoundNBT.getBoolean("OnSoulFire"));
	}

	@Override
	public void die(DamageSource p_70645_1_) {
		if (isOnSoulFire()) {
			SkeletalPiglinEntity skeletalpiglinentity = this.convertTo(EntityTypeRegistry.SKELETAL_PIGLIN.get(), true);
			if (skeletalpiglinentity != null) {
				skeletalpiglinentity.addEffect(new EffectInstance(Effects.CONFUSION, 200, 0));
				skeletalpiglinentity.playSound(SoundEvents.SOUL_ESCAPE, 1.0F, 1.0F);
				net.minecraftforge.event.ForgeEventFactory.onLivingConvert(this, skeletalpiglinentity);
			}
		} else {
			super.die(p_70645_1_);
		}
	}

	@Override
	public void tick() {
		super.tick();
		if (BlockPos.betweenClosedStream(this.getBoundingBox().deflate(0.001D)).anyMatch((p_233572_0_) -> {
			BlockState state = level.getBlockState(p_233572_0_);
			return state.is(Blocks.SOUL_FIRE);
		})) {
			this.setOnSoulFire(true);
		}
	}

	@Override
	public void clearFire() {
		super.clearFire();
		this.setOnSoulFire(false);
	}

	public boolean isOnSoulFire() {
		return this.entityData.get(DATA_ON_SOUL_FIRE);
	}

	public void setOnSoulFire(boolean soulFire) {
		this.entityData.set(DATA_ON_SOUL_FIRE, soulFire);
	}
}
