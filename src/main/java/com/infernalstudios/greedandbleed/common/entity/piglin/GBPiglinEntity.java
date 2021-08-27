package com.infernalstudios.greedandbleed.common.entity.piglin;

import com.infernalstudios.greedandbleed.api.IHasTaskManager;
import com.infernalstudios.greedandbleed.api.ITaskManager;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.monster.piglin.AbstractPiglinEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

import java.util.UUID;

@SuppressWarnings("NullableProblems")
public abstract class GBPiglinEntity extends AbstractPiglinEntity implements IHasTaskManager {
    protected static final DataParameter<Boolean> DATA_BABY_ID = EntityDataManager.defineId(GBPiglinEntity.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Boolean> DATA_IS_CHARGING_CROSSBOW = EntityDataManager.defineId(GBPiglinEntity.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Boolean> DATA_IS_DANCING = EntityDataManager.defineId(GBPiglinEntity.class, DataSerializers.BOOLEAN);
    private static final UUID SPEED_MODIFIER_BABY_UUID = UUID.fromString("766bfa64-11f3-11ea-8d71-362b9e155667");
    public static final AttributeModifier SPEED_MODIFIER_BABY = new AttributeModifier(SPEED_MODIFIER_BABY_UUID, "Baby speed boost", 0.2F, AttributeModifier.Operation.MULTIPLY_BASE);

    protected boolean cannotHunt = false;
    protected ITaskManager<?> taskManager;

    public GBPiglinEntity(EntityType<? extends AbstractPiglinEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected int getExperienceReward(PlayerEntity playerEntity) {
        return this.xpReward;
    }

    @Override
    public boolean canHunt() {
        return !this.cannotHunt;
    }

    protected void setCannotHunt(boolean cannotHunt) {
        this.cannotHunt = cannotHunt;
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return false;
    }

    @Override
    public boolean removeWhenFarAway(double p_213397_1_) {
        return !this.isPersistenceRequired();
    }

    @Override
    public boolean hurt(DamageSource damageSource, float amount) {
        boolean isHurt = super.hurt(damageSource, amount);
        if (this.level.isClientSide) {
            return false;
        } else {
            if (isHurt && damageSource.getEntity() instanceof LivingEntity) {
                this.taskManager.wasHurtBy((LivingEntity)damageSource.getEntity());
            }

            return isHurt;
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return this.level.isClientSide ?
                null : this.taskManager.getSoundForCurrentActivity().orElse(null);
    }

    @Override
    public boolean isBaby() {
        return this.getEntityData().get(DATA_BABY_ID);
    }

    @Override
    public void setBaby(boolean baby) {
        this.getEntityData().set(DATA_BABY_ID, baby);
        if (!this.level.isClientSide) {
            ModifiableAttributeInstance movementSpeed = this.getAttribute(Attributes.MOVEMENT_SPEED);
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

    public boolean canReplaceCurrentItem(ItemStack stack) {
        EquipmentSlotType equipmentslottype = MobEntity.getEquipmentSlotForItem(stack);
        ItemStack itemstack = this.getItemBySlot(equipmentslottype);
        return this.canReplaceCurrentItem(stack, itemstack);
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
