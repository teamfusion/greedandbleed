package com.github.teamfusion.greedandbleed.common.entity.brain;

import com.github.teamfusion.greedandbleed.api.IPatbleMob;
import com.github.teamfusion.greedandbleed.common.entity.piglin.Hoglet;
import com.github.teamfusion.greedandbleed.common.entity.piglin.pigmy.Pigmy;
import com.github.teamfusion.greedandbleed.common.registry.BlockRegistry;
import com.github.teamfusion.greedandbleed.common.registry.MemoryRegistry;
import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Unit;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.item.ItemStack;

public class PetSomeMob<E extends Mob, T extends LivingEntity> extends Behavior<E> {
    private int talkTick;
    private final int talkMaxTick;
    private final float speed;
    private boolean pat;

    public PetSomeMob(int talkMaxTick, float speed) {
        super(ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.WALK_TARGET, MemoryStatus.REGISTERED, MemoryRegistry.PET_TARGET.get(), MemoryStatus.VALUE_PRESENT), 1200);

        this.talkMaxTick = talkMaxTick;
        this.speed = speed;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel serverLevel, E mob) {
        return mob.getBrain().hasMemoryValue(MemoryRegistry.PET_TARGET.get());
    }

    @Override
    protected boolean canStillUse(ServerLevel serverLevel, E mob, long l) {
        return mob.getBrain().hasMemoryValue(MemoryRegistry.PET_TARGET.get()) && this.checkExtraStartConditions(serverLevel, mob) && this.talkTick < this.talkMaxTick;
    }

    @Override
    protected void start(ServerLevel serverLevel, E livingEntity, long l) {
        super.start(serverLevel, livingEntity, l);
        this.talkTick = 0;
        this.pat = false;
    }

    private void patAnimation(ServerLevel serverLevel, E livingEntity) {
        LivingEntity target = PetSomeMob.getPetTarget(livingEntity);
        if (target instanceof IPatbleMob petableMob) {
            petableMob.responsePet(target);
        }
        if (target instanceof Hoglet hoglet) {
            if (hoglet.getMainHandItem().is(BlockRegistry.HOGDEW_FUNGUS.get().asItem())) {
                hoglet.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
            }
            serverLevel.broadcastEntityEvent(livingEntity, (byte) Pigmy.PET_HOGLET_ANIMATION_ID);
        } else if (target instanceof Hoglin) {
            serverLevel.broadcastEntityEvent(livingEntity, (byte) Pigmy.PET_HOGLIN_ANIMATION_ID);
        }
        this.pat = true;
    }

    @Override
    protected void tick(ServerLevel serverLevel, E mob, long l) {
        LivingEntity target = PetSomeMob.getPetTarget(mob);

        if (!this.pat) {
            if (target != null && mob.position().distanceTo(target.position()) < 1 + target.getBbWidth()) {
                this.patAnimation(serverLevel, mob);
            } else {
                this.move(mob, target);
            }
        } else {
            mob.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker(target, true));

            this.talkTick++;
        }

    }

    @Override
    protected void stop(ServerLevel serverLevel, E mob, long l) {
        mob.getBrain().setMemoryWithExpiry(MemoryRegistry.PET_COOLDOWN.get(), Unit.INSTANCE, 600 + mob.getRandom().nextInt(600));
        mob.getBrain().eraseMemory(MemoryRegistry.PET_TARGET.get());
        mob.getBrain().eraseMemory(MemoryModuleType.LOOK_TARGET);
        mob.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
    }

    private void move(Mob mob, LivingEntity livingEntity) {
        mob.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker(livingEntity, true));
        mob.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(new EntityTracker(livingEntity, true), speed, 0));
    }

    private static LivingEntity getPetTarget(LivingEntity livingEntity) {
        return livingEntity.getBrain().getMemory(MemoryRegistry.PET_TARGET.get()).get();
    }

}

