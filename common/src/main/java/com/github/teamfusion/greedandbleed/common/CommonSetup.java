package com.github.teamfusion.greedandbleed.common;

import com.blackgear.platform.common.data.LootModifier;
import com.blackgear.platform.common.integration.BlockIntegration;
import com.blackgear.platform.common.integration.MobIntegration;
import com.blackgear.platform.common.worldgen.modifier.BiomeManager;
import com.blackgear.platform.core.ParallelDispatch;
import com.blackgear.platform.core.network.base.NetworkDirection;
import com.github.teamfusion.greedandbleed.GreedAndBleed;
import com.github.teamfusion.greedandbleed.common.entity.ToleratingMount;
import com.github.teamfusion.greedandbleed.common.integrations.BlockIntegrations;
import com.github.teamfusion.greedandbleed.common.integrations.LootIntegrations;
import com.github.teamfusion.greedandbleed.common.integrations.MobIntegrations;
import com.github.teamfusion.greedandbleed.common.integrations.WorldIntegrations;
import com.github.teamfusion.greedandbleed.common.item.slingshot.SlingshotItem;
import com.github.teamfusion.greedandbleed.common.network.ServerboundPatrolRangePacket;
import com.github.teamfusion.greedandbleed.common.network.ServerboundRecruitPacket;
import com.github.teamfusion.greedandbleed.common.network.ServerboundSelectSyncPacket;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

public class CommonSetup {
    public static void onBootstrap() {
        MobIntegration.registerIntegrations(MobIntegrations::bootstrap);
    }
    
    public static void onInitialized(ParallelDispatch dispatch) {
        dispatch.enqueueWork(() -> {
            BiomeManager.add(WorldIntegrations::bootstrap);
            BlockIntegration.registerIntegrations(BlockIntegrations::bootstrap);
            LootModifier.modify(LootIntegrations.INSTANCE);
            
            SlingshotItem.bootstrap();
        });
        
        GreedAndBleed.NETWORKING.registerPacket(NetworkDirection.SERVERBOUND, ServerboundPatrolRangePacket.ID, ServerboundPatrolRangePacket.HANDLER, ServerboundPatrolRangePacket.class);
        GreedAndBleed.NETWORKING.registerPacket(NetworkDirection.SERVERBOUND, ServerboundRecruitPacket.ID, ServerboundRecruitPacket.HANDLER, ServerboundRecruitPacket.class);
        GreedAndBleed.NETWORKING.registerPacket(NetworkDirection.SERVERBOUND, ServerboundSelectSyncPacket.ID, ServerboundSelectSyncPacket.HANDLER, ServerboundSelectSyncPacket.class);
    }
    
    public static void onHoglinAttack(LivingEntity entity, DamageSource source) {
        if (source.getDirectEntity() instanceof ToleratingMount mount && !entity.level().isClientSide) {
            if (mount.getTolerance() > 0) {
                mount.addTolerance(-1);
            } else if (mount.getTolerance() < 0) {
                mount.setTolerance(0);
            }
        }
    }
    
    public static void onHoglinUpdate(LivingEntity entity) {
        if (entity instanceof ToleratingMount mount && !entity.level().isClientSide) {
            if (mount.getTolerance() > 0) {
                mount.addTolerance(-1);
            } else if (mount.getTolerance() < 0) {
                mount.setTolerance(0);
            }
        }
    }
}