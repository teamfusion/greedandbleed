package com.github.teamfusion.greedandbleed.common.entity.piglin.pygmy;

import com.github.teamfusion.greedandbleed.api.ITaskManager;
import com.github.teamfusion.greedandbleed.api.ShrygmyTaskManager;
import com.github.teamfusion.greedandbleed.common.registry.ItemRegistry;
import com.github.teamfusion.greedandbleed.common.registry.MemoryRegistry;
import com.github.teamfusion.greedandbleed.common.registry.PotionRegistry;
import com.github.teamfusion.greedandbleed.common.registry.SensorRegistry;
import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Dynamic;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;

public class Shrygmy extends GBPygmy {
    protected static final ImmutableList<SensorType<? extends Sensor<? super Shrygmy>>> SENSOR_TYPES = ImmutableList.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_PLAYERS, SensorType.NEAREST_ITEMS, SensorType.HURT_BY, SensorRegistry.PYGMY_SPECIFIC_SENSOR.get());
    protected static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(MemoryModuleType.LOOK_TARGET, MemoryModuleType.DOORS_TO_CLOSE, MemoryModuleType.NEAREST_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLINS, MemoryModuleType.NEARBY_ADULT_PIGLINS, MemoryModuleType.HURT_BY, MemoryModuleType.HURT_BY_ENTITY, MemoryModuleType.WALK_TARGET, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.ATTACK_TARGET, MemoryModuleType.ATTACK_COOLING_DOWN, MemoryModuleType.INTERACTION_TARGET, MemoryModuleType.PATH, MemoryModuleType.ANGRY_AT, MemoryModuleType.NEAREST_VISIBLE_NEMESIS
            , MemoryRegistry.NEAREST_HOGLET.get(), MemoryRegistry.NEAREST_TAMED_HOGLET.get()
            , MemoryModuleType.LIKED_PLAYER, MemoryRegistry.WORK_TIME.get(), MemoryModuleType.JOB_SITE);


    public int shieldCooldown = 0;
    public Shrygmy(EntityType<? extends Shrygmy> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
    }

    public static AttributeSupplier.Builder setCustomAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 12.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D).add(Attributes.ATTACK_DAMAGE, 2.5F);
    }

    @Override
    public void startUsingItem(InteractionHand interactionHand) {
        super.startUsingItem(interactionHand);
    }

    @Override
    protected void blockUsingShield(LivingEntity livingEntity) {
        super.blockUsingShield(livingEntity);
        if (livingEntity.canDisableShield()) {
            this.swing(InteractionHand.OFF_HAND);
            livingEntity.addEffect(new MobEffectInstance(PotionRegistry.STUN.get(), 100, 0, false, false, true));
            this.level().broadcastEntityEvent(this, (byte) 30);
            this.shieldCooldown = 200;
        } else {
            this.swing(InteractionHand.OFF_HAND);
            livingEntity.addEffect(new MobEffectInstance(PotionRegistry.STUN.get(), 200, 0, false, false, true));
            this.addEffect(new MobEffectInstance(PotionRegistry.STUN.get(), 200, 0, false, false, true));
            this.stopUsingItem();
            this.level().broadcastEntityEvent(this, (byte) 30);
            this.shieldCooldown = 200;
        }
    }

    @Override
    protected void hurtCurrentlyUsedShield(float f) {
        if (!(this.useItem.getItem() instanceof ShieldItem)) {
            return;
        }
        if (f >= 3.0f) {
            int i = 1 + Mth.floor(f);
            InteractionHand interactionHand = this.getUsedItemHand();
            this.useItem.hurtAndBreak(i, this, player -> player.broadcastBreakEvent(interactionHand));
            if (this.useItem.isEmpty()) {
                if (interactionHand == InteractionHand.MAIN_HAND) {
                    this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
                } else {
                    this.setItemSlot(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
                }
                this.useItem = ItemStack.EMPTY;
                this.playSound(SoundEvents.SHIELD_BREAK, 0.8f, 0.8f + this.level().random.nextFloat() * 0.4f);
            }
        }
    }

    @Override
    protected void customServerAiStep() {
        this.level().getProfiler().push("pygmyBrain");
        this.getBrain().tick((ServerLevel) this.level(), this);
        this.level().getProfiler().pop();
        this.taskManager.updateActivity();
        super.customServerAiStep();
    }

    @Override
    public void tick() {
        super.tick();
        if (this.shieldCooldown > 0) {
            --this.shieldCooldown;
        }
    }

    @Override
    protected Brain.Provider<Shrygmy> brainProvider() {
        return Brain.provider(MEMORY_TYPES, SENSOR_TYPES);
    }

    protected Brain<?> makeBrain(Dynamic<?> dynamic) {
        this.taskManager = this.createTaskManager(dynamic);
        return this.taskManager.getBrain();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Brain<Shrygmy> getBrain() {
        return (Brain<Shrygmy>) super.getBrain();
    }


    @Override
    public ITaskManager<?> createTaskManager(Dynamic<?> dynamic) {
        return new ShrygmyTaskManager<>(this, this.brainProvider().makeBrain(dynamic));
    }

    @Override
    public void holdInMainHand(ItemStack stack) {
        this.setItemInHand(InteractionHand.MAIN_HAND, stack);
    }

    @Override
    public void holdInOffHand(ItemStack stack) {
        this.setItemInHand(InteractionHand.OFF_HAND, stack);
    }

    @Override
    protected float getStandingEyeHeight(Pose pose, EntityDimensions entityDimensions) {
        return entityDimensions.height * 0.85F;
    }

    @Override
    protected void playConvertedSound() {
        this.playSound(SoundEvents.PIGLIN_CONVERTED_TO_ZOMBIFIED, 1.0f, this.getVoicePitch());
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor serverLevelAccessor, DifficultyInstance difficultyInstance, MobSpawnType mobSpawnType, @Nullable SpawnGroupData spawnGroupData, @Nullable CompoundTag compoundTag) {
        this.populateDefaultEquipmentSlots(serverLevelAccessor.getRandom(), difficultyInstance);
        return super.finalizeSpawn(serverLevelAccessor, difficultyInstance, mobSpawnType, spawnGroupData, compoundTag);
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource randomSource, DifficultyInstance difficultyInstance) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ItemRegistry.CLUB.get()));
        this.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(ItemRegistry.GOLDEN_SHIELD.get()));
    }

    @Override
    public float getVoicePitch() {
        return super.getVoicePitch() + 0.25F;
    }
}
