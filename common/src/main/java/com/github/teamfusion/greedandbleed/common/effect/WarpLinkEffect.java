package com.github.teamfusion.greedandbleed.common.effect;

import com.github.teamfusion.greedandbleed.common.registry.GBDamageSource;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class WarpLinkEffect extends MobEffect {
    public WarpLinkEffect(MobEffectCategory mobEffectCategory, int i) {
        super(mobEffectCategory, i);
    }

    @Override
    public void applyEffectTick(LivingEntity livingEntity, int i) {
        livingEntity.hurt(livingEntity.damageSources().source(GBDamageSource.WARPED_INFECT), Mth.clamp(i - 2, 1, 4));
    }


    @Override
    public boolean isDurationEffectTick(int i, int j) {
        int k;
        k = 100 >> j;
        if (k > 0) {
            return i % k == 0;
        } else {
            return true;
        }
    }
}
