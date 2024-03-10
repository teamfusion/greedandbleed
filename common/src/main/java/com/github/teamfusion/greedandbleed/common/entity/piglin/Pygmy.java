package com.github.teamfusion.greedandbleed.common.entity.piglin;

import com.github.teamfusion.greedandbleed.api.ITaskManager;
import com.github.teamfusion.greedandbleed.api.PygmyTaskManager;
import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Dynamic;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.piglin.PiglinArmPose;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.UUID;

public class Pygmy extends GBPiglin {
    protected static final ImmutableList<SensorType<? extends Sensor<? super Pygmy>>> SENSOR_TYPES = ImmutableList.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_PLAYERS, SensorType.NEAREST_ITEMS, SensorType.HURT_BY, SensorType.PIGLIN_BRUTE_SPECIFIC_SENSOR);
    protected static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(MemoryModuleType.LOOK_TARGET, MemoryModuleType.DOORS_TO_CLOSE, MemoryModuleType.NEAREST_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLINS, MemoryModuleType.NEARBY_ADULT_PIGLINS, MemoryModuleType.HURT_BY, MemoryModuleType.HURT_BY_ENTITY, MemoryModuleType.WALK_TARGET, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.ATTACK_TARGET, MemoryModuleType.ATTACK_COOLING_DOWN, MemoryModuleType.INTERACTION_TARGET, MemoryModuleType.PATH, MemoryModuleType.ANGRY_AT, MemoryModuleType.NEAREST_VISIBLE_NEMESIS, MemoryModuleType.HOME);

    private static final EntityDataAccessor<Boolean> DATA_BABY_ID = SynchedEntityData.defineId(Pygmy.class, EntityDataSerializers.BOOLEAN);
    private static final UUID SPEED_MODIFIER_BABY_UUID = UUID.fromString("766bfa64-11f3-11ea-8d71-362b9e155667");
    private static final AttributeModifier SPEED_MODIFIER_BABY = new AttributeModifier(SPEED_MODIFIER_BABY_UUID, "Baby speed boost", 0.2F, AttributeModifier.Operation.MULTIPLY_BASE);

    public Pygmy(EntityType<? extends Pygmy> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_BABY_ID, false);
    }

    public static AttributeSupplier.Builder setCustomAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 14.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D);
    }

    @Override
    public void setBaby(boolean baby) {
        this.getEntityData().set(DATA_BABY_ID, baby);
        if (!this.level().isClientSide) {
            AttributeInstance instance = this.getAttribute(Attributes.MOVEMENT_SPEED);
            if (instance != null) {
                instance.removeModifier(SPEED_MODIFIER_BABY);
                if (baby) {
                    instance.addTransientModifier(SPEED_MODIFIER_BABY);
                }
            }
        }
    }

    @Override
    public boolean isBaby() {
        return this.getEntityData().get(DATA_BABY_ID);
    }

    @Override
    protected void customServerAiStep() {
        this.level().getProfiler().push("pygmyBrain");
        this.getBrain().tick((ServerLevel) this.level(), this);
        this.level().getProfiler().pop();
        this.taskManager.updateActivity();
        this.taskManager.initMemories();
        super.customServerAiStep();
    }

    @Override
    protected Brain.Provider<?> brainProvider() {
        return Brain.provider(MEMORY_TYPES, SENSOR_TYPES);
    }

    protected Brain<?> makeBrain(Dynamic<?> dynamic) {
        this.taskManager = this.createTaskManager(dynamic);
        return this.taskManager.getBrain();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Brain<Pygmy> getBrain() {
        return (Brain<Pygmy>) super.getBrain();
    }


    @Override
    public ITaskManager<?> createTaskManager(Dynamic<?> dynamic) {
        return new PygmyTaskManager(this, this.brainProvider().makeBrain(dynamic));
    }

    @Override
    public void holdInMainHand(ItemStack stack) {

    }

    @Override
    public void holdInOffHand(ItemStack stack) {

    }

    @Override
    protected float getStandingEyeHeight(Pose pose, EntityDimensions entityDimensions) {
        return entityDimensions.height * 0.85F;
    }

    @Override
    public PiglinArmPose getArmPose() {
        return PiglinArmPose.DEFAULT;
    }

    @Override
    protected void playConvertedSound() {

    }
}
