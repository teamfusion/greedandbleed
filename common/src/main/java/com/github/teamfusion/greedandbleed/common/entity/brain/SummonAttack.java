package com.github.teamfusion.greedandbleed.common.entity.brain;

import com.github.teamfusion.greedandbleed.common.entity.piglin.ShamanPiglin;
import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

import java.util.Optional;

public class SummonAttack<E extends ShamanPiglin> extends Behavior<E> {
    protected int ticks;

    public SummonAttack() {
        super(ImmutableMap.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT, MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED));
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel p_22538_, E p_22539_) {
        return p_22539_.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).isPresent() && p_22539_.summonCooldown <= 0;
    }

    @Override
    protected boolean canStillUse(ServerLevel p_22545_, E p_22546_, long p_22547_) {
        return p_22546_.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).isPresent() && p_22546_.summonCooldown <= 0;
    }

    @Override
    protected void start(ServerLevel p_22540_, E p_22541_, long p_22542_) {
        super.start(p_22540_, p_22541_, p_22542_);
    }

    @Override
    protected void stop(ServerLevel p_22548_, E p_22549_, long p_22550_) {
        super.stop(p_22548_, p_22549_, p_22550_);
    }

    @Override
    protected void tick(ServerLevel p_22551_, E p_22552_, long p_22553_) {
        super.tick(p_22551_, p_22552_, p_22553_);
        Optional<LivingEntity> optional = p_22552_.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET);
        if (optional.isPresent()) {
            p_22552_.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker(optional.get(), true));

            if (++this.ticks >= 40) {
                p_22552_.summon();
                this.ticks = 0;
            }
        }
    }
}