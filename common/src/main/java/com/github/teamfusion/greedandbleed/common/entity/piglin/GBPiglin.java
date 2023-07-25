package com.github.teamfusion.greedandbleed.common.entity.piglin;

import com.github.teamfusion.greedandbleed.api.HasTaskManager;
import com.github.teamfusion.greedandbleed.api.ITaskManager;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public abstract class GBPiglin extends AbstractPiglin implements HasTaskManager {
    protected static final EntityDataAccessor<Boolean> DATA_BABY_ID = SynchedEntityData.defineId(GBPiglin.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Boolean> DATA_IS_CHARGING_CROSSBOW = SynchedEntityData.defineId(GBPiglin.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Boolean> DATA_IS_DANCING = SynchedEntityData.defineId(GBPiglin.class, EntityDataSerializers.BOOLEAN);
    private static final UUID SPEED_MODIFIER_BABY_UUID = UUID.fromString("766bfa64-11f3-11ea-8d71-362b9e155667");
    public static final AttributeModifier SPEED_MODIFIER_BABY = new AttributeModifier(SPEED_MODIFIER_BABY_UUID, "Baby speed boost", 0.2F, AttributeModifier.Operation.MULTIPLY_BASE);
    protected boolean cannotHunt = false;
    protected ITaskManager<?> taskManager;

    public GBPiglin(EntityType<? extends AbstractPiglin> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public int getExperienceReward() {
        return this.xpReward;
    }

    @Override
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
        if (this.level.isClientSide) {
            return false;
        } else {
            if (isHurt && source.getEntity() instanceof LivingEntity living) {
                this.taskManager.wasHurtBy(living);
            }

            return isHurt;
        }
    }

    @Nullable @Override
    protected SoundEvent getAmbientSound() {
        return this.level.isClientSide ? null : this.taskManager.getSoundForCurrentActivity().orElse(null);
    }

    @Override
    public boolean isBaby() {
        return this.getEntityData().get(DATA_BABY_ID);
    }

    @Override
    public void setBaby(boolean baby) {
        this.getEntityData().set(DATA_BABY_ID, baby);
        if (!this.level.isClientSide) {
            AttributeInstance movementSpeed = this.getAttribute(Attributes.MOVEMENT_SPEED);
            if (movementSpeed != null) {
                movementSpeed.removeModifier(SPEED_MODIFIER_BABY);
                if (baby) {
                    movementSpeed.addTransientModifier(SPEED_MODIFIER_BABY);
                }
            }
        }
    }

    public boolean isDancing() {
        return this.entityData.get(DATA_IS_DANCING);
    }

    public void setDancing(boolean isDancing) {
        this.entityData.set(DATA_IS_DANCING, isDancing);
    }

    protected boolean isChargingCrossbow() {
        return this.entityData.get(DATA_IS_CHARGING_CROSSBOW);
    }

    public void setChargingCrossbow(boolean chargingCrossbow) {
        this.entityData.set(DATA_IS_CHARGING_CROSSBOW, chargingCrossbow);
    }

    // HAS TASK MASTER

    @Override
    public ITaskManager<?> getTaskManager() {
        return this.taskManager;
    }

    @Override
    public void playSound(SoundEvent soundEvent) {
        this.playSound(soundEvent, this.getSoundVolume(), this.getVoicePitch());
    }
}