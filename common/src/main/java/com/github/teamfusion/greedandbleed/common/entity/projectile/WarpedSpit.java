package com.github.teamfusion.greedandbleed.common.entity.projectile;

import com.github.teamfusion.greedandbleed.common.registry.EntityTypeRegistry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public class WarpedSpit extends ThrowableProjectile {
    public WarpedSpit(EntityType<? extends WarpedSpit> entityType, Level level) {
        super(entityType, level);
    }

    public WarpedSpit(Level level, LivingEntity livingEntity) {
        this(EntityTypeRegistry.WARPED_SPIT.get(), level);
        this.setOwner(livingEntity);
        this.setPos(livingEntity.getX() - (double) (livingEntity.getBbWidth()) * 0.5 * (double) Mth.sin(livingEntity.yBodyRot * 0.017453292F), livingEntity.getEyeY() - 0.10000000149011612, livingEntity.getZ() + (double) (livingEntity.getBbWidth()) * 0.5 * (double) Mth.cos(livingEntity.yBodyRot * 0.017453292F));
    }

    protected void onHitEntity(EntityHitResult entityHitResult) {
        super.onHitEntity(entityHitResult);
        Entity var3 = this.getOwner();
        if (var3 instanceof LivingEntity livingEntity) {
            entityHitResult.getEntity().hurt(this.damageSources().mobProjectile(this, livingEntity), 2.0F);
        }

    }

    @Override
    protected float getGravity() {
        return 0.01F;
    }

    protected void onHitBlock(BlockHitResult blockHitResult) {
        super.onHitBlock(blockHitResult);
        if (!this.level().isClientSide) {
            this.discard();
        }

    }

    protected void defineSynchedData() {
    }

    public void recreateFromPacket(ClientboundAddEntityPacket clientboundAddEntityPacket) {
        super.recreateFromPacket(clientboundAddEntityPacket);
        double d = clientboundAddEntityPacket.getXa();
        double e = clientboundAddEntityPacket.getYa();
        double f = clientboundAddEntityPacket.getZa();

        for (int i = 0; i < 7; ++i) {
            double g = 0.4 + 0.1 * (double) i;
            this.level().addParticle(ParticleTypes.SNEEZE, this.getX(), this.getY(), this.getZ(), d * g, e, f * g);
        }

        this.setDeltaMovement(d, e, f);
    }
}
