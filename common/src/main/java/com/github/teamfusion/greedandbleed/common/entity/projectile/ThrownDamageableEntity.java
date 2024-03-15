package com.github.teamfusion.greedandbleed.common.entity.projectile;

import com.github.teamfusion.greedandbleed.common.registry.EntityTypeRegistry;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class ThrownDamageableEntity extends ThrowableItemProjectile {
    private double baseDamage = 2.0D;

    public ThrownDamageableEntity(EntityType<? extends ThrownDamageableEntity> entityType, Level world) {
        super(entityType, world);
    }

    public ThrownDamageableEntity(Level world, LivingEntity living) {
        super(EntityTypeRegistry.THROWN_DAMAGEABLE.get(), living, world);
    }

    public ThrownDamageableEntity(Level world, double x, double y, double z) {
        super(EntityTypeRegistry.THROWN_DAMAGEABLE.get(), x, y, z, world);
    }

    @Override
    public void handleEntityEvent(byte b) {
        if (b == 3) {
            double d = 0.08;
            for (int i = 0; i < 8; ++i) {
                this.level().addParticle(new ItemParticleOption(ParticleTypes.ITEM, this.getItem()), this.getX(), this.getY(), this.getZ(), ((double) this.random.nextFloat() - 0.5) * 0.08, ((double) this.random.nextFloat() - 0.5) * 0.08, ((double) this.random.nextFloat() - 0.5) * 0.08);
            }
        }
    }

    @Override
    protected void onHit(HitResult hitResult) {
        super.onHit(hitResult);
        if (!this.level().isClientSide) {
            this.level().broadcastEntityEvent(this, (byte) 3);
            this.discard();
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        super.onHitEntity(entityHitResult);
        Entity entity = entityHitResult.getEntity();
        float f = (float) this.getDeltaMovement().length();
        int i = Mth.ceil(Mth.clamp((double) f * this.baseDamage, 0.0D, 2.147483647E9D));

        entity.hurt(this.damageSources().thrown(this, this.getOwner()), i);
    }


    public void setBaseDamage(double p_70239_1_) {
        this.baseDamage = p_70239_1_;
    }

    public double getBaseDamage() {
        return this.baseDamage;
    }

    @Override
    protected Item getDefaultItem() {
        return null;
    }
}