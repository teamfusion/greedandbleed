package com.infernalstudios.greedandbleed.mixin;

import com.infernalstudios.greedandbleed.common.entity.IToleratingMount;
import com.infernalstudios.greedandbleed.common.registry.ItemRegistry;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
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
public abstract class HoglinEntityMixin extends AnimalEntity implements IRideable, IEquipable, IToleratingMount {
    private static final DataParameter<Boolean> DATA_SADDLE_ID = EntityDataManager.defineId(HoglinEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> DATA_BOOST_TIME = EntityDataManager.defineId(HoglinEntity.class, DataSerializers.INT);
    private final BoostHelper steering = new BoostHelper(this.entityData, DATA_BOOST_TIME, DATA_SADDLE_ID);

    private static final DataParameter<Integer> DATA_TOLERANCE = EntityDataManager.defineId(HoglinEntity.class, DataSerializers.INT);

    protected HoglinEntityMixin(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    // MIXINS

    /**
     * Note that for these Mixins, I used the Minecraft Development plugin for IntelliJ
     */

    @Inject(at = @At("RETURN"),
            method = "mobInteract", cancellable = true)
    private void giveSaddle(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResultType> cir){
        boolean food = this.isFood(player.getItemInHand(hand));
        if (!food
                && this.isSaddled()
                && !this.isVehicle()
                && !player.isSecondaryUseActive()
                && isPacified(this)) {
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

    @Inject(at = @At("RETURN"),
            method = "defineSynchedData")
    private void defineSteeringData(CallbackInfo callbackInfo){
        this.entityData.define(DATA_SADDLE_ID, false);
        this.entityData.define(DATA_BOOST_TIME, 0);
        this.entityData.define(DATA_TOLERANCE, 0);
    }

    @Inject(at = @At("RETURN"),
            method = "addAdditionalSaveData")
    private void addSteeringData(CompoundNBT compoundNBT, CallbackInfo callbackInfo) {
        this.steering.addAdditionalSaveData(compoundNBT);
        compoundNBT.putInt("Tolerance", this.getTolerance());
    }

    @Inject(at = @At("RETURN"),
            method = "readAdditionalSaveData")
    private void readSteeringData(CompoundNBT compoundNBT, CallbackInfo callbackInfo) {
        this.steering.readAdditionalSaveData(compoundNBT);
        this.setTolerance(compoundNBT.getInt("Tolerance"));
    }

    @Inject(at = @At("HEAD"),
            method = "doHurtTarget")
    private void removeRidersIfTargetingThem(Entity target, CallbackInfoReturnable<Boolean> cir) {
        if(this.getPassengers().contains(target)){
            this.ejectPassengers();
        }
    }

    // HELPERS

    private static boolean isPacified(AnimalEntity hoglin) {
        return hoglin.getBrain().hasMemoryValue(MemoryModuleType.PACIFIED);
    }

    // OVERRIDDEN METHODS

    @Override
    public void onSyncedDataUpdated(DataParameter<?> dataParameter) {
        if (DATA_BOOST_TIME.equals(dataParameter) && this.level.isClientSide) {
            this.steering.onSynced();
        }
        super.onSyncedDataUpdated(dataParameter);
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
            if(!this.isTolerating()){
                return false;
            }
            PlayerEntity playerentity = (PlayerEntity)entity;
            return playerentity.getMainHandItem().getItem() ==
                    ItemRegistry.CRIMSON_FUNGUS_ON_A_STICK.get()
                    || playerentity.getOffhandItem().getItem()
                    == ItemRegistry.CRIMSON_FUNGUS_ON_A_STICK.get();
        }
    }

    @Override
    protected void dropEquipment() {
        super.dropEquipment();
        if (this.isSaddled()) {
            this.spawnAtLocation(Items.SADDLE);
        }
    }

    // IRIDEABLE METHODS

    @Override
    public boolean boost() {
        //return this.steering.boost(this.getRandom());
        return false;
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
        float toleranceProgress = this.getToleranceProgress();
        float minSpeedFactor = 0.4F;
        float middleSpeedFactor = minSpeedFactor * 1.75F;
        float maxSpeedFactor = minSpeedFactor * 2.5F;
        float speedFactor = toleranceProgress > 0.66F ?
                maxSpeedFactor : toleranceProgress > 0.33F ?
                middleSpeedFactor : minSpeedFactor;
        return (float)this.getAttributeValue(Attributes.MOVEMENT_SPEED) * speedFactor;
    }

    // IEQUIPABLE METHODS

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

    // ITOLERATINGMOUNT METHODS

    @Override
    public int getTolerance() {
        return this.entityData.get(DATA_TOLERANCE);
    }

    @Override
    public int getMaxTolerance(){
        return 18 * 20;
    }

    @Override
    public void setTolerance(int valueIn) {
        this.entityData.set(DATA_TOLERANCE, Math.min(valueIn, this.getMaxTolerance()));
    }

    @Override
    public boolean canPerformMountAction() {
        return this.isSaddled() & this.isTolerating();
    }

    @Override
    public void handleStartMountAction() {

    }

    @Override
    public void handleStopMountAction() {

    }

}
