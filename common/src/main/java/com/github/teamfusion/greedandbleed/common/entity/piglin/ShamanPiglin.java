package com.github.teamfusion.greedandbleed.common.entity.piglin;

import com.github.teamfusion.greedandbleed.api.ITaskManager;
import com.github.teamfusion.greedandbleed.api.ShamanPiglinTaskManager;
import com.github.teamfusion.greedandbleed.client.network.GreedAndBleedClientNetwork;
import com.github.teamfusion.greedandbleed.common.entity.HasMountArmor;
import com.github.teamfusion.greedandbleed.common.entity.SummonData;
import com.github.teamfusion.greedandbleed.common.entity.SummonHandler;
import com.github.teamfusion.greedandbleed.common.entity.TraceAndSetOwner;
import com.github.teamfusion.greedandbleed.common.registry.EntityTypeRegistry;
import com.github.teamfusion.greedandbleed.common.registry.ItemRegistry;
import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Dynamic;
import io.netty.buffer.Unpooled;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Zoglin;
import net.minecraft.world.entity.monster.piglin.PiglinArmPose;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

import static dev.architectury.networking.NetworkManager.collectPackets;
import static dev.architectury.networking.NetworkManager.serverToClient;

public class ShamanPiglin extends GBPiglin implements NeutralMob {
    protected static final ImmutableList<SensorType<? extends Sensor<? super ShamanPiglin>>> SENSOR_TYPES = ImmutableList.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_PLAYERS, SensorType.NEAREST_ITEMS, SensorType.HURT_BY, SensorType.PIGLIN_BRUTE_SPECIFIC_SENSOR);
    protected static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(MemoryModuleType.LOOK_TARGET, MemoryModuleType.DOORS_TO_CLOSE, MemoryModuleType.NEAREST_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLINS, MemoryModuleType.NEARBY_ADULT_PIGLINS, MemoryModuleType.HURT_BY, MemoryModuleType.HURT_BY_ENTITY, MemoryModuleType.WALK_TARGET, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.ATTACK_TARGET, MemoryModuleType.ATTACK_COOLING_DOWN, MemoryModuleType.INTERACTION_TARGET, MemoryModuleType.PATH, MemoryModuleType.ANGRY_AT, MemoryModuleType.NEAREST_VISIBLE_NEMESIS, MemoryModuleType.HOME);
    private static final EntityDataAccessor<Integer> DATA_WAVE = SynchedEntityData.defineId(ShamanPiglin.class, EntityDataSerializers.INT);

    public static final UniformInt RANGED_INT = TimeUtil.rangeOfSeconds(20, 39);
    private int angerTime;
    private UUID angerTarget;
    public int summonCooldown;
    public int groupsSpawned;
    protected static final EntityDataAccessor<Boolean> DATA_SOUL_GUARD = SynchedEntityData.defineId(ShamanPiglin.class, EntityDataSerializers.BOOLEAN);

    public final SummonHandler summonHandler = new SummonHandler();

    public ShamanPiglin(EntityType<? extends ShamanPiglin> entityType, Level level) {
        super(entityType, level);
        this.setCanPickUpLoot(false);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_SOUL_GUARD, false);
        this.entityData.define(DATA_WAVE, 1);
    }

    public boolean isSoulGuard() {
        return this.getEntityData().get(DATA_SOUL_GUARD);
    }

    public void setSoulGuard(boolean soul) {
        this.getEntityData().set(DATA_SOUL_GUARD, soul);
    }

    public int getWave() {
        return this.getEntityData().get(DATA_WAVE);
    }

    public void setWave(int wave) {
        this.getEntityData().set(DATA_WAVE, wave);
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
    public Brain<ShamanPiglin> getBrain() {
        return (Brain<ShamanPiglin>) super.getBrain();
    }

    // ATTRIBUTES
    public static AttributeSupplier.Builder setCustomAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 10.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D);
    }

    @Override
    public MobType getMobType() {
        return MobType.UNDEAD;
    }


    @Override
    public boolean isBaby() {
        return false;
    }

    @Override
    public void setBaby(boolean baby) {
    }

    @Override
    protected void customServerAiStep() {
        this.level().getProfiler().push("shamanBrain");
        this.getBrain().tick((ServerLevel) this.level(), this);
        this.level().getProfiler().pop();
        this.taskManager.updateActivity();
        this.taskManager.initMemories();
        super.customServerAiStep();
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.summonCooldown > 0) {
            --this.summonCooldown;
        }
        this.summonHandler.tick(this.level(), this);
        if (!this.level().isClientSide()) {
            this.setSoulGuard(!this.summonHandler.getList().isEmpty());
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) {
            return false;
        }
        if (!source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
        for (SummonData data : this.summonHandler.getList()) {
            LivingEntity living = data.getOwner(this.level());
            if (living != null) {
                if (!this.level().isClientSide()) {
                    FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
                    buf.writeInt(this.getId());
                    buf.writeFloat(amount);
                    buf.writeUtf(source.typeHolder().unwrapKey().get().location().toString());
                    living.hurt(source, amount);
                    collectPackets(GreedAndBleedClientNetwork.ofTrackingEntity(() -> living), serverToClient(), GreedAndBleedClientNetwork.HURT_PACKET, buf);
                    this.playSound(SoundEvents.FIRE_EXTINGUISH);
                }
                return false;
            }
        }


        if (this.getWave() <= 5) {
            this.playSound(SoundEvents.FIRE_EXTINGUISH);
            return false;
        }
        }
        return super.hurt(source, amount);
    }

    // EXPERIENCE POINTS
    @Override
    public int getExperienceReward() {
        return 2 + this.level().random.nextInt(2);
    }


    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.PIGLIN_BRUTE_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.PIGLIN_BRUTE_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.ZOMBIE_STEP, 0.15F, 1.0F);
    }

    @Override
    protected float getStandingEyeHeight(Pose pose, EntityDimensions dimensions) {
        return this.isBaby() ? 0.93F : 1.74F;
    }

    @Override
    public double getPassengersRidingOffset() {
        return (double) this.getBbHeight() * 0.92D;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("SummonCooldown", this.summonCooldown);
        tag.putInt("Wave", this.getWave());
        summonHandler.addAdditionalSaveData(tag);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.summonCooldown = tag.getInt("SummonCooldown");
        this.setWave(tag.getInt("Wave"));
        summonHandler.readAdditionalSaveData(tag);
    }

    @Override
    public PiglinArmPose getArmPose() {
        return null;
    }

    @Override
    protected void playConvertedSound() {
        this.playSound(SoundEvents.PIGLIN_BRUTE_CONVERTED_TO_ZOMBIFIED, 1.0f, this.getVoicePitch());

    }

    @Override
    public ITaskManager<?> createTaskManager(Dynamic<?> dynamic) {
        return new ShamanPiglinTaskManager(this, this.brainProvider().makeBrain(dynamic));
    }

    @Override
    public void holdInMainHand(ItemStack stack) {

    }

    @Override
    public void holdInOffHand(ItemStack stack) {

    }

    @Override
    public int getRemainingPersistentAngerTime() {
        return this.angerTime;
    }

    @Override
    public void setRemainingPersistentAngerTime(int time) {
        this.angerTime = time;
    }

    @Nullable
    @Override
    public UUID getPersistentAngerTarget() {
        return this.angerTarget;
    }

    @Override
    public void setPersistentAngerTarget(@Nullable UUID target) {
        this.angerTarget = target;
    }

    @Override
    public void startPersistentAngerTimer() {
        this.setRemainingPersistentAngerTime(RANGED_INT.sample(this.random));
    }

    public void summon(LivingEntity living) {
        if (summonHandler.getList().isEmpty()) {
            ServerLevel serverLevel = (ServerLevel) this.level();
            int count = 0;
            BlockPos blockPos = this.blockPosition().offset(-3 + this.random.nextInt(6), this.random.nextInt(3) - this.random.nextInt(3), -3 + this.random.nextInt(6));


            if (serverLevel.isEmptyBlock(blockPos) && serverLevel.isEmptyBlock(blockPos.above()) && !serverLevel.isEmptyBlock(blockPos.below())) {
                for (SummonType summonType : SummonType.VALUES) {
                    for (int i = 0; i < summonType.spawnsPerWaveBeforeBonus[getWave()]; i++) {
                        Mob piglin = summonType.entityType.create(this.level());


                        piglin.moveTo(blockPos, 0.0f, 0.0f);
                        piglin.setPose(Pose.EMERGING);
                        summonHandler.addSummonData(piglin);
                        if (piglin instanceof TraceAndSetOwner traceableEntity) {
                            traceableEntity.setOwner(this);
                        }
                        if (!(piglin instanceof Zoglin)) {
                            if (this.getWave() == 3) {
                                this.maybyWearArmorWithSummon(piglin, EquipmentSlot.HEAD, new ItemStack(Items.GOLDEN_HELMET), this.getWave() * 0.1F);
                                this.maybyWearArmorWithSummon(piglin, EquipmentSlot.CHEST, new ItemStack(Items.GOLDEN_CHESTPLATE), this.getWave() * 0.1F);
                                this.maybyWearArmorWithSummon(piglin, EquipmentSlot.LEGS, new ItemStack(Items.GOLDEN_LEGGINGS), this.getWave() * 0.1F);
                                this.maybyWearArmorWithSummon(piglin, EquipmentSlot.FEET, new ItemStack(Items.GOLDEN_BOOTS), this.getWave() * 0.1F);
                            } else if (this.getWave() == 5) {
                                this.wearArmorWithSummon(piglin, EquipmentSlot.HEAD, EnchantmentHelper.enchantItem(random, new ItemStack(Items.GOLDEN_HELMET), getWave() * 5, false));
                                this.wearArmorWithSummon(piglin, EquipmentSlot.CHEST, EnchantmentHelper.enchantItem(random, new ItemStack(Items.GOLDEN_CHESTPLATE), getWave() * 5, false));
                                this.wearArmorWithSummon(piglin, EquipmentSlot.LEGS, EnchantmentHelper.enchantItem(random, new ItemStack(Items.GOLDEN_LEGGINGS), getWave() * 5, false));
                                this.wearArmorWithSummon(piglin, EquipmentSlot.FEET, EnchantmentHelper.enchantItem(random, new ItemStack(Items.GOLDEN_BOOTS), getWave() * 5, false));
                            } else if (this.getWave() >= 4) {
                                this.maybyWearArmorWithSummon(piglin, EquipmentSlot.HEAD, EnchantmentHelper.enchantItem(random, new ItemStack(Items.GOLDEN_HELMET), getWave() * 5, false), this.getWave() * 0.1F);
                                this.maybyWearArmorWithSummon(piglin, EquipmentSlot.CHEST, EnchantmentHelper.enchantItem(random, new ItemStack(Items.GOLDEN_CHESTPLATE), getWave() * 5, false), this.getWave() * 0.1F);
                                this.maybyWearArmorWithSummon(piglin, EquipmentSlot.LEGS, EnchantmentHelper.enchantItem(random, new ItemStack(Items.GOLDEN_LEGGINGS), getWave() * 5, false), this.getWave() * 0.1F);
                                this.maybyWearArmorWithSummon(piglin, EquipmentSlot.FEET, EnchantmentHelper.enchantItem(random, new ItemStack(Items.GOLDEN_BOOTS), getWave() * 5, false), this.getWave() * 0.1F);
                            }
                        } else if (piglin instanceof HasMountArmor hasMountArmor) {
                            if (this.getWave() == 5) {
                                hasMountArmor.setArmor(new ItemStack(ItemRegistry.GOLDEN_HOGLIN_ARMOR.get()));
                            }
                        }
                        if (piglin instanceof SkeletalPiglin skeletalPiglin) {
                            if (this.random.nextBoolean()) {
                                piglin.setItemSlot(EquipmentSlot.MAINHAND, EnchantmentHelper.enchantItem(random, new ItemStack(Items.GOLDEN_SWORD), getWave() * 5, false));
                            } else {
                                piglin.setItemSlot(EquipmentSlot.MAINHAND, EnchantmentHelper.enchantItem(random, new ItemStack(Items.CROSSBOW), getWave() * 5, false));
                            }
                        }
                        piglin.setPose(Pose.EMERGING);

                        if (piglin instanceof PathfinderMob pathfinderMob) {
                            pathfinderMob.getBrain().setActiveActivityIfPossible(Activity.EMERGE);
                        }

                        piglin.finalizeSpawn(serverLevel, this.level().getCurrentDifficultyAt(blockPos), MobSpawnType.MOB_SUMMONED, null, null);

                        piglin.setTarget(living);
                        serverLevel.addFreshEntityWithPassengers(piglin);

                        count += 1;
                        this.summonCooldown = 200;

                    }
                }
                this.setWave(this.getWave() + 1);
            }

    }

    }

    protected void wearArmorWithSummon(Mob mob, EquipmentSlot equipmentSlot, ItemStack itemStack) {
        mob.setItemSlot(equipmentSlot, itemStack);
    }

    protected void maybyWearArmorWithSummon(Mob mob, EquipmentSlot equipmentSlot, ItemStack itemStack, float chance) {
        if (this.random.nextFloat() < chance) {
            mob.setItemSlot(equipmentSlot, itemStack);
        }
    }

    enum SummonType {
        SKELETON_PIGLIN(EntityTypeRegistry.SKELETAL_PIGLIN.get(), new int[]{0, 2, 2, 2, 2, 3}),
        ZOMBIFIED_PIGLIN(EntityType.ZOMBIFIED_PIGLIN, new int[]{0, 1, 0, 0, 2, 3}),
        ZOGLIN(EntityType.ZOGLIN, new int[]{0, 0, 0, 0, 1, 2});

        static final SummonType[] VALUES;
        final EntityType<? extends Mob> entityType;
        final int[] spawnsPerWaveBeforeBonus;

        SummonType(EntityType<? extends Mob> entityType, int[] is) {
            this.entityType = entityType;
            this.spawnsPerWaveBeforeBonus = is;
        }

        static {
            VALUES = SummonType.values();
        }
    }
}