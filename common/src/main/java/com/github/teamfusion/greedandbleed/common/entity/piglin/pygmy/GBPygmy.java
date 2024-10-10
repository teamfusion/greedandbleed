package com.github.teamfusion.greedandbleed.common.entity.piglin.pygmy;

import com.github.teamfusion.greedandbleed.api.HasTaskManager;
import com.github.teamfusion.greedandbleed.api.ITaskManager;
import com.github.teamfusion.greedandbleed.common.registry.EntityTypeRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.util.GoalUtils;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public abstract class GBPygmy extends Monster implements HasTaskManager {
    protected static final EntityDataAccessor<Boolean> DATA_BABY_ID = SynchedEntityData.defineId(GBPygmy.class, EntityDataSerializers.BOOLEAN);

    protected static final EntityDataAccessor<Boolean> DATA_IMMUNE_TO_ZOMBIFICATION = SynchedEntityData.defineId(GBPygmy.class, EntityDataSerializers.BOOLEAN);
    protected int timeInOverworld;
    private static final UUID SPEED_MODIFIER_BABY_UUID = UUID.fromString("766bfa64-11f3-11ea-8d71-362b9e155667");
    public static final AttributeModifier SPEED_MODIFIER_BABY = new AttributeModifier(SPEED_MODIFIER_BABY_UUID, "Baby speed boost", 0.2F, AttributeModifier.Operation.MULTIPLY_BASE);
    protected boolean cannotHunt = false;
    protected ITaskManager<?> taskManager;

    public GBPygmy(EntityType<? extends GBPygmy> entityType, Level level) {
        super(entityType, level);
        this.setCanPickUpLoot(true);
        this.applyOpenDoorsAbility();
        this.setPathfindingMalus(BlockPathTypes.DANGER_FIRE, 16.0f);
        this.setPathfindingMalus(BlockPathTypes.DAMAGE_FIRE, -1.0f);
    }

    private void applyOpenDoorsAbility() {
        if (GoalUtils.hasGroundPathNavigation(this)) {
            ((GroundPathNavigation) this.getNavigation()).setCanOpenDoors(true);
        }
    }

    public boolean isAdult() {
        return !this.isBaby();
    }

    @Override
    public boolean isBaby() {
        return this.getEntityData().get(DATA_BABY_ID);
    }

    @Override
    public void setBaby(boolean baby) {
        this.getEntityData().set(DATA_BABY_ID, baby);
        if (!this.level().isClientSide) {
            AttributeInstance movementSpeed = this.getAttribute(Attributes.MOVEMENT_SPEED);
            if (movementSpeed != null) {
                movementSpeed.removeModifier(SPEED_MODIFIER_BABY);
                if (baby) {
                    movementSpeed.addTransientModifier(SPEED_MODIFIER_BABY);
                }
            }
        }
    }

    @Override
    public ITaskManager<?> getTaskManager() {
        return this.taskManager;
    }


    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand interactionHand) {
        if (this.taskManager != null) {
            InteractionResult result = this.taskManager.mobInteract(player, interactionHand);
            if (result != null && result.consumesAction()) {
                return result;
            }
        }
        return super.mobInteract(player, interactionHand);
    }

    @Override
    public void playSound(SoundEvent soundEvent) {
        this.playSound(soundEvent, this.getSoundVolume(), this.getVoicePitch());
    }

    public void setImmuneToZombification(boolean bl) {
        this.getEntityData().set(DATA_IMMUNE_TO_ZOMBIFICATION, bl);
    }

    protected boolean isImmuneToZombification() {
        return this.getEntityData().get(DATA_IMMUNE_TO_ZOMBIFICATION);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_BABY_ID, false);
        this.entityData.define(DATA_IMMUNE_TO_ZOMBIFICATION, false);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        if (this.isImmuneToZombification()) {
            compoundTag.putBoolean("IsImmuneToZombification", true);
        }
        compoundTag.putInt("TimeInOverworld", this.timeInOverworld);
    }

    @Override
    public double getMyRidingOffset() {
        return this.isBaby() ? -0.05 : -0.45;
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        this.setImmuneToZombification(compoundTag.getBoolean("IsImmuneToZombification"));
        this.timeInOverworld = compoundTag.getInt("TimeInOverworld");
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        this.timeInOverworld = this.isConverting() ? ++this.timeInOverworld : 0;
        if (this.timeInOverworld > 300) {
            this.playConvertedSound();
            this.finishConversion((ServerLevel) this.level());
        }
    }

    protected void playConvertedSound() {
        this.playSound(SoundEvents.PIGLIN_CONVERTED_TO_ZOMBIFIED, 1.0F, this.getVoicePitch());
    }

    public boolean isConverting() {
        return !this.level().dimensionType().piglinSafe() && !this.isImmuneToZombification() && !this.isNoAi();
    }

    protected void finishConversion(ServerLevel serverLevel) {
        ZombifiedPygmy zombifiedPiglin = this.convertTo(EntityTypeRegistry.ZOMBIFIED_PYGMY.get(), true);
        if (zombifiedPiglin != null) {
            zombifiedPiglin.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 200, 0));
        }
    }

    @Override
    @Nullable
    public LivingEntity getTarget() {
        return this.brain.getMemory(MemoryModuleType.ATTACK_TARGET).orElse(null);
    }


    public boolean canHunt() {
        return !this.cannotHunt;
    }

    public void setCannotHunt(boolean cannotHunt) {
        this.cannotHunt = cannotHunt;
    }

    public boolean canReplaceCurrentItem(ItemStack from, ItemStack to) {
        return super.canReplaceCurrentItem(from, to);
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return false;
    }

    @Override
    public boolean removeWhenFarAway(double distance) {
        return !this.isPersistenceRequired();
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        boolean isHurt = super.hurt(source, amount);
        if (this.level().isClientSide) {
            return false;
        } else {
            if (isHurt && source.getEntity() instanceof LivingEntity living) {
                this.taskManager.wasHurtBy(living);
            }

            return isHurt;
        }
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return this.level().isClientSide ? null : this.taskManager.getSoundForCurrentActivity().orElse(null);
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.PIGLIN_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.PIGLIN_DEATH;
    }
}
