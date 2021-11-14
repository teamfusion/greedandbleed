package com.github.teamfusion.greedandbleed.common.entity.projectile;

import com.github.teamfusion.greedandbleed.common.registry.EntityTypeRegistry;
import com.github.teamfusion.greedandbleed.common.registry.ItemRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IRendersAsItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.IPacket;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

public class ThrownDamageableEntity extends ProjectileItemEntity implements IRendersAsItem {
	private double baseDamage = 2.0D;

	public ThrownDamageableEntity(EntityType<? extends ThrownDamageableEntity> entityType, World world) {
		super(entityType, world);
	}

	public ThrownDamageableEntity(World world, LivingEntity living) {
		super(EntityTypeRegistry.THROWN_DAMAGEABLE_TYPE, living, world);
	}

	public ThrownDamageableEntity(World world, double x, double y, double z) {
		super(EntityTypeRegistry.THROWN_DAMAGEABLE_TYPE, x, y, z, world);
	}

	protected Item getDefaultItem() {
		return ItemRegistry.PEBBLE.get();
	}

	@OnlyIn(Dist.CLIENT)
	private IParticleData getParticle() {
		ItemStack itemstack = this.getItemRaw();
		return (IParticleData) (itemstack.isEmpty() ? new ItemParticleData(ParticleTypes.ITEM, new ItemStack(this.getDefaultItem())) : new ItemParticleData(ParticleTypes.ITEM, itemstack));
	}

	@OnlyIn(Dist.CLIENT)
	public void handleEntityEvent(byte data) {
		if (data == 3) {
			IParticleData iparticledata = this.getParticle();

			for (int i = 0; i < 8; ++i) {
				this.level.addParticle(iparticledata, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
			}
		}

	}

	protected void onHitEntity(EntityRayTraceResult result) {
		super.onHitEntity(result);
		Entity entity = result.getEntity();
		float f = (float) this.getDeltaMovement().length();
		int i = MathHelper.ceil(MathHelper.clamp((double) f * this.baseDamage, 0.0D, 2.147483647E9D));

		entity.hurt(DamageSource.thrown(this, this.getOwner()), i);
	}

	protected void onHit(RayTraceResult result) {
		super.onHit(result);
		if (!this.level.isClientSide) {
			this.level.broadcastEntityEvent(this, (byte) 3);
			this.remove();
		}

	}

	public void setBaseDamage(double p_70239_1_) {
		this.baseDamage = p_70239_1_;
	}

	public double getBaseDamage() {
		return this.baseDamage;
	}

	@Override
	public IPacket<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}