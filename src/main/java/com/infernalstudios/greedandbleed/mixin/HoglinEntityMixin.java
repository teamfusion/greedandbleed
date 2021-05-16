package com.infernalstudios.greedandbleed.mixin;

import com.infernalstudios.greedandbleed.common.registry.ItemRegistry;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.monster.HoglinEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(HoglinEntity.class)
public abstract class HoglinEntityMixin extends AnimalEntity implements IRideable, IEquipable {
    private static final DataParameter<Boolean> DATA_SADDLE_ID = EntityDataManager.defineId(HoglinEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> DATA_BOOST_TIME = EntityDataManager.defineId(HoglinEntity.class, DataSerializers.INT);
    private final BoostHelper steering = new BoostHelper(this.entityData, DATA_BOOST_TIME, DATA_SADDLE_ID);

    protected HoglinEntityMixin(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public void onSyncedDataUpdated(DataParameter<?> dataParameter) {
        if (DATA_BOOST_TIME.equals(dataParameter) && this.level.isClientSide) {
            this.steering.onSynced();
        }
        super.onSyncedDataUpdated(dataParameter);
    }

    @Inject(at = @At("RETURN"), method = "Lnet/minecraft/entity/monster/HoglinEntity;mobInteract(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Hand;)Lnet/minecraft/util/ActionResultType;", cancellable = true)
    private void giveSaddle(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResultType> cir){
        boolean food = this.isFood(player.getItemInHand(hand));
        if (!food && this.isSaddled() && !this.isVehicle() && !player.isSecondaryUseActive()) {
            if (!this.level.isClientSide) {
                player.startRiding(this);
            }
            cir.setReturnValue(ActionResultType.sidedSuccess(this.level.isClientSide));
        } else {
            ActionResultType actionresulttype = cir.getReturnValue();
            if (!actionresulttype.consumesAction()) {
                ItemStack itemInHand = player.getItemInHand(hand);
                cir.setReturnValue(
                        itemInHand.getItem() == Items.SADDLE ?
                        itemInHand.interactLivingEntity(player, this, hand) :
                        ActionResultType.PASS);
            }
        }
    }


    @Inject(at = @At("RETURN"), method = "Lnet/minecraft/entity/monster/HoglinEntity;defineSynchedData()V")
    private void defineSteeringData(CallbackInfo callbackInfo){
        this.entityData.define(DATA_SADDLE_ID, false);
        this.entityData.define(DATA_BOOST_TIME, 0);
    }

    @Inject(at = @At("RETURN"), method = "Lnet/minecraft/entity/monster/HoglinEntity;addAdditionalSaveData(Lnet/minecraft/nbt/CompoundNBT;)V")
    private void addSteeringData(CompoundNBT compoundNBT, CallbackInfo callbackInfo) {
        this.steering.addAdditionalSaveData(compoundNBT);
    }

    @Inject(at = @At("RETURN"), method = "Lnet/minecraft/entity/monster/HoglinEntity;readAdditionalSaveData(Lnet/minecraft/nbt/CompoundNBT;)V")
    private void readSteeringData(CompoundNBT compoundNBT, CallbackInfo callbackInfo) {
        this.steering.readAdditionalSaveData(compoundNBT);
    }

    @Override
    public boolean boost() {
        return this.steering.boost(this.getRandom());
    }

    @Override
    public void travelWithInput(Vector3d travelVector) {
        super.travel(travelVector);
    }

    @Override
    public void travel(Vector3d travelVector) {
        this.travel(this, this.steering, travelVector);
    }

    @Override
    public float getSteeringSpeed() {
        return (float)this.getAttributeValue(Attributes.MOVEMENT_SPEED) * (0.225F);
    }

    @Override
    public boolean isSaddleable() {
        return this.isAlive() && !this.isBaby();
    }

    @Override
    public void equipSaddle(@Nullable SoundCategory soundCategory) {
        this.steering.setSaddle(true);
        if (soundCategory != null) {
            this.level.playSound((PlayerEntity)null, this, SoundEvents.HORSE_SADDLE, soundCategory, 0.5F, 1.0F);
        }
    }

    @Override
    public boolean isSaddled() {
        return this.steering.hasSaddle();
    }

    @Nullable
    public Entity getControllingPassenger() {
        return this.getPassengers().isEmpty() ? null : this.getPassengers().get(0);
    }

    @Override
    public boolean canBeControlledByRider() {
        Entity entity = this.getControllingPassenger();
        if (!(entity instanceof PlayerEntity)) {
            return false;
        } else {
            PlayerEntity playerentity = (PlayerEntity)entity;
            return playerentity.getMainHandItem().getItem() ==
                    ItemRegistry.CRIMSON_FUNGUS_ON_A_STICK.get()
                    || playerentity.getOffhandItem().getItem()
                    == ItemRegistry.CRIMSON_FUNGUS_ON_A_STICK.get();
        }
    }

}
