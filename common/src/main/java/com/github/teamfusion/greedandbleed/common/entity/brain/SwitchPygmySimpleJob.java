package com.github.teamfusion.greedandbleed.common.entity.brain;

import com.github.teamfusion.greedandbleed.common.entity.piglin.pigmy.GBPigmy;
import com.github.teamfusion.greedandbleed.common.registry.MemoryRegistry;
import com.google.common.collect.ImmutableMap;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.schedule.Activity;

public class SwitchPygmySimpleJob<E extends GBPigmy, T extends LivingEntity> extends Behavior<E> {
    public SwitchPygmySimpleJob() {
        super(ImmutableMap.of(MemoryRegistry.WORK_TIME.get(), MemoryStatus.REGISTERED, MemoryModuleType.JOB_SITE, MemoryStatus.REGISTERED, MemoryModuleType.LIKED_PLAYER, MemoryStatus.REGISTERED), 1200);
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel serverLevel, E mob) {
        boolean flag = mob.getBrain().hasMemoryValue(MemoryRegistry.WORK_TIME.get());
        return !flag && mob.getBrain().isActive(Activity.WORK) || flag && mob.getBrain().isActive(Activity.IDLE);
    }

    @Override
    protected void start(ServerLevel serverLevel, E livingEntity, long l) {
        super.start(serverLevel, livingEntity, l);
        Brain<?> brain = livingEntity.getBrain();
        boolean flag = brain.hasMemoryValue(MemoryRegistry.WORK_TIME.get());
        if (flag) {
            brain.setActiveActivityIfPossible(Activity.WORK);
            if (!brain.hasMemoryValue(MemoryModuleType.JOB_SITE)) {
                brain.eraseMemory(MemoryModuleType.LIKED_PLAYER);
                brain.eraseMemory(MemoryRegistry.WORK_TIME.get());
            }
        } else {
            if (brain.hasMemoryValue(MemoryModuleType.JOB_SITE)) {
                serverLevel.getPoiManager()
                        .release(brain.getMemory(MemoryModuleType.JOB_SITE).get().pos());
                DebugPackets.sendPoiTicketCountPacket(serverLevel, brain.getMemory(MemoryModuleType.JOB_SITE).get().pos());

                brain.eraseMemory(MemoryModuleType.JOB_SITE);
            }
            if (brain.hasMemoryValue(MemoryModuleType.LIKED_PLAYER)) {
                brain.eraseMemory(MemoryModuleType.LIKED_PLAYER);
            }
            if (livingEntity.getMode() != GBPigmy.Mode.FOLLOW) {
                livingEntity.setMode(GBPigmy.Mode.FOLLOW);
            }
            brain.setActiveActivityIfPossible(Activity.IDLE);
        }
    }
}

