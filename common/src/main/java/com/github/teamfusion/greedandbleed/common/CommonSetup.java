package com.github.teamfusion.greedandbleed.common;

import com.github.teamfusion.greedandbleed.common.entity.ToleratingMount;
import com.github.teamfusion.greedandbleed.common.entity.piglin.Hoglet;
import com.github.teamfusion.greedandbleed.common.entity.piglin.SkeletalPiglin;
import com.github.teamfusion.greedandbleed.common.registry.EntityTypeRegistry;
import com.github.teamfusion.greedandbleed.platform.common.MobRegistry;
import com.github.teamfusion.greedandbleed.platform.common.worldgen.BiomeManager;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

public class CommonSetup {
    public static void onBootstrap() {
        MobRegistry.attributes(EntityTypeRegistry.SKELETAL_PIGLIN, SkeletalPiglin::setCustomAttributes);
        MobRegistry.attributes(EntityTypeRegistry.HOGLET, Hoglet::setCustomAttributes);
    }

    public static void onInitialized() {
        BiomeManager.setup();
    }

    public static void onHoglinAttack(LivingEntity entity, DamageSource source) {
        if (source.getDirectEntity() instanceof ToleratingMount mount && !entity.level.isClientSide) {
            if (mount.getTolerance() > 0) {
                mount.addTolerance(-1);
            } else if (mount.getTolerance() < 0) {
                mount.setTolerance(0);
            }
        }
    }

    public static void onHoglinUpdate(LivingEntity entity) {
        if (entity instanceof ToleratingMount mount && !entity.level.isClientSide) {
            if (mount.getTolerance() > 0) {
                mount.addTolerance(-1);
            } else if (mount.getTolerance() < 0) {
                mount.setTolerance(0);
            }
        }
    }
}