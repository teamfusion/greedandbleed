package com.github.teamfusion.greedandbleed.common.item.slingshot;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class BuckshotSlingshotBehavior extends SlingshotBehavior {

    public void shootBehavior(Level level, BlockPos pos, LivingEntity shooter, ItemStack stack, float power) {
        for (int i = 0; i < 3; i++) {
            Projectile slingshotProjectile = this.getProjectile(level, pos, shooter, stack, power);

            slingshotProjectile.setOwner(shooter);
            slingshotProjectile.shootFromRotation(shooter, shooter.getXRot() + (shooter.getRandom().nextFloat() - shooter.getRandom().nextFloat()) * 5F, shooter.getYRot() + (shooter.getRandom().nextFloat() - shooter.getRandom().nextFloat()) * 5F, this.getXRot(), power * this.getMaxPower(), 1.0F);
            this.addProjectileEffects(level, shooter, slingshotProjectile, stack);
            level.addFreshEntity(slingshotProjectile);
        }
    }
}
