package com.github.teamfusion.greedandbleed.common.item.slingshot;

import com.github.teamfusion.greedandbleed.common.entity.projectile.ThrownDamageableEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class SlingshotBehavior {
    public Projectile getProjectile(Level level, BlockPos pos, LivingEntity shooter, ItemStack stack, float power) {
        return new ThrownDamageableEntity(level, shooter);
    }

    public void addProjectileEffects(Level level, LivingEntity shooter, Projectile slingshotProjectile, ItemStack stack) {
        if (slingshotProjectile instanceof ThrownDamageableEntity thrownDamageableEntity) {
            thrownDamageableEntity.setBaseDamage(3.0F);
            if (stack.getItem() == Items.EGG || stack.getItem() == Items.SNOWBALL) {
                //set egg and snowball damage
                thrownDamageableEntity.setBaseDamage(1F);
            }
            if (stack.is(ItemTags.VILLAGER_PLANTABLE_SEEDS)) {
                thrownDamageableEntity.setBaseDamage(0F);
            }
            if (stack.getItem() == Items.GOLD_NUGGET || stack.getItem() == Items.IRON_NUGGET) {
                thrownDamageableEntity.setBaseDamage(2F);
            }
            if (stack.getItem() == Items.PUFFERFISH || stack.getItem() == Items.SPIDER_EYE) {
                thrownDamageableEntity.setBaseDamage(2F);
            }
            if (stack.getItem() == Items.RAW_GOLD || stack.getItem() == Items.RAW_IRON || stack.getItem() == Items.RAW_COPPER) {
                thrownDamageableEntity.setBaseDamage(2.5F);
            }
            if (stack.getItem() == Items.BRICK || stack.getItem() == Items.NETHER_BRICK || stack.getItem() == Items.IRON_INGOT || stack.getItem() == Items.COPPER_INGOT) {
                thrownDamageableEntity.setBaseDamage(3F);
            }
            if (stack.getItem() == Items.NETHERITE_INGOT || stack.getItem() == Items.GOLD_INGOT) {
                thrownDamageableEntity.setBaseDamage(4F);
            }
            if (stack.getItem() == Items.DIAMOND || stack.getItem() == Items.EMERALD) {
                thrownDamageableEntity.setBaseDamage(3F);
            }
        }
        if (slingshotProjectile instanceof ThrowableItemProjectile projectile) {
            projectile.setItem(stack);
        }
    }

    public void shootBehavior(Level level, BlockPos pos, LivingEntity shooter, ItemStack stack, float power) {
        Projectile slingshotProjectile = this.getProjectile(level, pos, shooter, stack, power);

        slingshotProjectile.setOwner(shooter);
        slingshotProjectile.shootFromRotation(shooter, shooter.getXRot(), shooter.getYRot(), this.getXRot(), power * this.getMaxPower(), 1.0F);
        this.addProjectileEffects(level, shooter, slingshotProjectile, stack);
        level.addFreshEntity(slingshotProjectile);
    }

    public float getMaxPower() {
        return 2.5F;
    }

    public float getXRot() {
        return 0F;
    }
}
