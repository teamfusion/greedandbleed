package com.github.teamfusion.greedandbleed.mixin;

import com.github.teamfusion.greedandbleed.GreedAndBleed;
import com.github.teamfusion.greedandbleed.api.IHogEquipable;
import com.github.teamfusion.greedandbleed.common.entity.ICanOpenMountInventory;
import com.github.teamfusion.greedandbleed.common.entity.IHasMountArmor;
import com.github.teamfusion.greedandbleed.common.entity.IHasMountInventory;
import com.github.teamfusion.greedandbleed.common.entity.IToleratingMount;
import com.github.teamfusion.greedandbleed.common.item.HoglinArmorItem;
import com.github.teamfusion.greedandbleed.common.registry.ItemRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.BoostHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IRideable;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.monster.HoglinEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IInventoryChangedListener;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.UUID;

@SuppressWarnings("NullableProblems")
@Mixin(HoglinEntity.class)
public abstract class HoglinEntityMixin extends AnimalEntity implements IRideable, IHogEquipable, IToleratingMount, IInventoryChangedListener, IHasMountArmor, IHasMountInventory {
    private static final DataParameter<Boolean> DATA_SADDLE_ID = EntityDataManager.defineId(HoglinEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> DATA_BOOST_TIME = EntityDataManager.defineId(HoglinEntity.class, DataSerializers.INT);
    private final BoostHelper steering = new BoostHelper(this.entityData, DATA_BOOST_TIME, DATA_SADDLE_ID);

    private static final DataParameter<Integer> DATA_TOLERANCE = EntityDataManager.defineId(HoglinEntity.class, DataSerializers.INT);

    private static final UUID ARMOR_MODIFIER_UUID = UUID.fromString("556E1665-8B10-40C8-8F9D-CF9B1667F295");
    protected Inventory inventory;

    private static final DataParameter<Boolean> DATA_ID_CHEST = EntityDataManager.defineId(HoglinEntity.class, DataSerializers.BOOLEAN);

    private HoglinEntityMixin(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    // MIXINS

    @Inject(at = @At("RETURN"), method = "finishConversion")
    private void dropInventoryWhenZombified(ServerWorld serverWorld, CallbackInfo ci){
        this.dropEquipment();
    }

    @Inject(at = @At("TAIL"), method = "<init>")
    private void setUpInventory(EntityType<? extends HoglinEntity> p_i231569_1_, World p_i231569_2_, CallbackInfo ci){
        this.createInventory();
    }

    @Inject(at = @At("RETURN"),
            method = "mobInteract", cancellable = true)
    private void handleMountInteraction(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResultType> cir){
        ItemStack heldItem = player.getItemInHand(hand);

        // If this hoglin is an adult
        if (!this.isBaby()) {
            // Handle opening inventory with shift-key down during interaction
            if (isPacified(this) && player.isSecondaryUseActive()) {
                this.openInventory(player);
                cir.setReturnValue(ActionResultType.sidedSuccess(this.level.isClientSide));
                return;
            }

            // Handle interacting with the hoglin as it is being ridden
            if (this.isVehicle()) {
                cir.setReturnValue(super.mobInteract(player, hand));
                return;
            }
        }

        // If the held item is not empty
        if (!heldItem.isEmpty()) {

            // Return early if this is food for the hoglin, the original method already handled it
            if (this.isFood(heldItem)) {
                //return this.fedFood(player, heldItem);
                return;
            }

            // If the hoglin is pacified, it will accept a variety of interactions
            if(isPacified(this)){

                // Invoke interactLivingEntity on the ItemStack
                ActionResultType actionresulttype = heldItem.interactLivingEntity(player, this, hand);

                // If interactLivingEntity was successful, return
                if (actionresulttype.consumesAction()) {
                    cir.setReturnValue(actionresulttype);
                    return;
                }

                // Handle equipping a chest and creating an inventory for the hoglin
                if (!this.hasChest() && this.isChestStack(heldItem)) {
                    this.setChest(true);
                    this.playChestEquipsSound();
                    if (!player.abilities.instabuild) {
                        heldItem.shrink(1);
                    }

                    this.createInventory();
                    cir.setReturnValue(ActionResultType.sidedSuccess(this.level.isClientSide));
                    return;
                }

                // Handle opening the hoglin's inventory with a saddle or armor
                boolean canGiveSaddle = !this.isBaby() && !this.isHogSaddled() && this.isSaddleStack(heldItem);
                boolean canOpenInventoryWithItem = this.isArmor(heldItem) || canGiveSaddle;
                if (canOpenInventoryWithItem) {
                    this.openInventory(player);
                    cir.setReturnValue(ActionResultType.sidedSuccess(this.level.isClientSide));
                    return;
                }
            } else {
                // Make the hoglin angry and reject the interaction
                this.playSound(SoundEvents.HOGLIN_ANGRY, this.getSoundVolume(), this.getVoicePitch());
                cir.setReturnValue(ActionResultType.FAIL);
                return;
            }
        }

        // If this hoglin is a baby
        if (this.isBaby()) {
            // Handle interacting with a baby hoglin
            cir.setReturnValue(super.mobInteract(player, hand));
        } else if(isPacified(this)){
            // Handle mounting the hoglin
            player.startRiding(this);
            cir.setReturnValue(ActionResultType.sidedSuccess(this.level.isClientSide));
        }
    }

    @Inject(at = @At("RETURN"),
            method = "defineSynchedData")
    private void defineSteeringData(CallbackInfo callbackInfo){
        this.entityData.define(DATA_SADDLE_ID, false);
        this.entityData.define(DATA_BOOST_TIME, 0);
        this.entityData.define(DATA_TOLERANCE, 0);
        this.entityData.define(DATA_ID_CHEST, false);
    }

    @Inject(at = @At("RETURN"),
            method = "addAdditionalSaveData")
    private void addSteeringData(CompoundNBT compoundNBT, CallbackInfo callbackInfo) {
        this.steering.addAdditionalSaveData(compoundNBT);
        compoundNBT.putInt("Tolerance", this.getTolerance());

        if (!this.inventory.getItem(0).isEmpty()) {
            compoundNBT.put("SaddleItem", this.inventory.getItem(0).save(new CompoundNBT()));
        }
        if (!this.inventory.getItem(1).isEmpty()) {
            compoundNBT.put("ArmorItem", this.inventory.getItem(1).save(new CompoundNBT()));
        }
        compoundNBT.putBoolean("ChestedHoglin", this.hasChest());
        if (this.hasChest()) {
            ListNBT listnbt = new ListNBT();

            for(int i = 2; i < this.inventory.getContainerSize(); ++i) {
                ItemStack itemstack = this.inventory.getItem(i);
                if (!itemstack.isEmpty()) {
                    CompoundNBT compoundnbt = new CompoundNBT();
                    compoundnbt.putByte("Slot", (byte)i);
                    itemstack.save(compoundnbt);
                    listnbt.add(compoundnbt);
                }
            }

            compoundNBT.put("Items", listnbt);
        }
    }

    @Inject(at = @At("RETURN"),
            method = "readAdditionalSaveData")
    private void readSteeringData(CompoundNBT compoundNBT, CallbackInfo callbackInfo) {
        this.steering.readAdditionalSaveData(compoundNBT);
        this.setTolerance(compoundNBT.getInt("Tolerance"));

        if (compoundNBT.contains("SaddleItem", 10)) {
            ItemStack itemstack = ItemStack.of(compoundNBT.getCompound("SaddleItem"));
            if (this.isSaddleStack(itemstack)) {
                this.inventory.setItem(0, itemstack);
            }
        }
        if (compoundNBT.contains("ArmorItem", 10)) {
            ItemStack itemstack = ItemStack.of(compoundNBT.getCompound("ArmorItem"));
            if (!itemstack.isEmpty() && this.isArmor(itemstack)) {
                this.inventory.setItem(1, itemstack);
            }
        }
        this.setChest(compoundNBT.getBoolean("ChestedHoglin"));
        if (this.hasChest()) {
            ListNBT listnbt = compoundNBT.getList("Items", 10);
            this.createInventory();

            for(int i = 0; i < listnbt.size(); ++i) {
                CompoundNBT compoundnbt = listnbt.getCompound(i);
                int j = compoundnbt.getByte("Slot") & 255;
                if (j >= 2 && j < this.inventory.getContainerSize()) {
                    this.inventory.setItem(j, ItemStack.of(compoundnbt));
                }
            }
        }
        this.updateContainerEquipment();
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

    private void setArmorEquipment(ItemStack stack) {
        this.setArmor(stack);
        if (!this.level.isClientSide) {
            ModifiableAttributeInstance armor = this.getAttribute(Attributes.ARMOR);
            if (armor != null) {
                armor.removeModifier(ARMOR_MODIFIER_UUID);
                if (this.isArmor(stack)) {
                    int i = ((HoglinArmorItem) stack.getItem()).getProtection();
                    if (i != 0) {
                        armor.addTransientModifier(new AttributeModifier(ARMOR_MODIFIER_UUID, "Horse armor bonus", i, AttributeModifier.Operation.ADDITION));
                    }
                }
            }
        }
    }

    private void setArmor(ItemStack p_213805_1_) {
        this.setItemSlot(EquipmentSlotType.CHEST, p_213805_1_);
        this.setDropChance(EquipmentSlotType.CHEST, 0.0F);
    }


    private void playChestEquipsSound() {
        this.playSound(SoundEvents.DONKEY_CHEST, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
    }

    // OVERRIDDEN METHODS

    @Override
    public void onSyncedDataUpdated(DataParameter<?> dataParameter) {
        if (DATA_BOOST_TIME.equals(dataParameter) && this.level.isClientSide) {
            this.steering.onSynced();
        }
        super.onSyncedDataUpdated(dataParameter);
    }

    @Override
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
        if (this.inventory != null) {
            for(int i = 0; i < this.inventory.getContainerSize(); ++i) {
                ItemStack itemstack = this.inventory.getItem(i);
                if (!itemstack.isEmpty() && !EnchantmentHelper.hasVanishingCurse(itemstack)) {
                    this.spawnAtLocation(itemstack);
                }
            }
        }
        if (this.hasChest()) {
            if (!this.level.isClientSide) {
                this.spawnAtLocation(this.getDefaultChestItem());
            }

            this.setChest(false);
        }
    }

    @Override
    public boolean setSlot(int slotId, ItemStack stackIn) {
        if (slotId == 499) {
            if (this.hasChest() && stackIn.isEmpty()) {
                this.setChest(false);
                this.createInventory();
                return true;
            }

            if (!this.hasChest() && this.isChestStack(stackIn)) {
                this.setChest(true);
                this.createInventory();
                return true;
            }
        }
        int i = slotId - 400;
        if (i >= 0 && i < 2 && i < this.inventory.getContainerSize()) {
            if (i == 0 && !this.isSaddleStack(stackIn)) {
                return false;
            } else if (i != 1 || this.canWearArmor() && this.isArmor(stackIn)) {
                this.inventory.setItem(i, stackIn);
                this.updateContainerEquipment();
                return true;
            } else {
                return false;
            }
        } else {
            int j = slotId - 500 + 2;
            if (j >= 2 && j < this.inventory.getContainerSize()) {
                this.inventory.setItem(j, stackIn);
                return true;
            } else {
                return false;
            }
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
    public boolean isHogSaddleable() {
        return this.isAlive() && !this.isBaby();
    }

    @Override
    public void equipHogSaddle(@Nullable SoundCategory soundCategory) {
        this.setSaddled(true);
        if (soundCategory != null) {
            this.level.playSound(null, this, SoundEvents.HORSE_SADDLE, soundCategory, 0.5F, 1.0F);
        }
    }

    @Override
    public boolean isHogSaddled() {
        return this.steering.hasSaddle();
    }

    private void setSaddled(boolean saddled){
        this.steering.setSaddle(saddled);
        this.inventory.setItem(0, saddled ? new ItemStack(this.getDefaultSaddleItem()) : ItemStack.EMPTY);
    }

    // ITOLERATINGMOUNT METHODS

    @Override
    public boolean canAcceptSaddle() {
        return this.isHogSaddleable() && isPacified(this);
    }

    @Override
    public Item getDefaultSaddleItem(){
        return ItemRegistry.HOGLIN_SADDLE.get();
    }

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
        if(valueIn > this.getMaxTolerance()){
            this.entityData.set(DATA_TOLERANCE, this.getMaxTolerance());
            GreedAndBleed.LOGGER.debug("Tried to set tolerance for {} as {} when max is {}", this.toString(), valueIn, this.getMaxTolerance());
        }
        else if(valueIn < 0){
            this.entityData.set(DATA_TOLERANCE, 0);
            GreedAndBleed.LOGGER.debug("Tried to set tolerance for {} as {} which is below 0", this.toString(), valueIn);
        }
        else{
            this.entityData.set(DATA_TOLERANCE, valueIn);
        }
    }

    @Override
    public boolean canPerformMountAction() {
        return this.isHogSaddled() & this.isTolerating();
    }

    @Override
    public void handleStartMountAction() {

    }

    @Override
    public void handleStopMountAction() {

    }

    // INVENTORY

    private int getInventorySize() {
        return this.hasChest() ? 17 : 2;
    }

    private void createInventory() {
        Inventory inventory = this.inventory;
        this.inventory = new Inventory(this.getInventorySize());
        if (inventory != null) {
            inventory.removeListener(this);
            int i = Math.min(inventory.getContainerSize(), this.inventory.getContainerSize());

            for(int j = 0; j < i; ++j) {
                ItemStack itemstack = inventory.getItem(j);
                if (!itemstack.isEmpty()) {
                    this.inventory.setItem(j, itemstack.copy());
                }
            }
        }

        this.inventory.addListener(this);
        this.updateContainerEquipment();
        this.itemHandler = net.minecraftforge.common.util.LazyOptional.of(() -> new net.minecraftforge.items.wrapper.InvWrapper(this.inventory));
    }

    private void updateContainerEquipment() {
        if (!this.level.isClientSide) {
            this.steering.setSaddle(!this.inventory.getItem(0).isEmpty());
            this.setArmorEquipment(this.inventory.getItem(1));
            this.setDropChance(EquipmentSlotType.CHEST, 0.0F);
        }
    }

    // IHASMOUNTINVENTORY

    @Override
    public Item getDefaultChestItem() {
        return Blocks.CHEST.asItem();
    }

    @Override
    public void openInventory(PlayerEntity player) {
        if (!this.level.isClientSide
                && (!this.isVehicle() || this.hasPassenger(player))
                && player instanceof ICanOpenMountInventory) {
            GreedAndBleed.LOGGER.debug("Opening mount inventory for {}", this);
            ((ICanOpenMountInventory)player).openMountInventory(this, this.inventory);
        }
    }

    @Override
    public boolean hasChest() {
        return this.entityData.get(DATA_ID_CHEST);
    }

    @Override
    public void setChest(boolean p_110207_1_) {
        this.entityData.set(DATA_ID_CHEST, p_110207_1_);
    }

    @Override
    public int getInventoryColumns() {
        return 5;
    }

    // IINVENTORYCHANGEDLISTENER METHODS

    @Override
    public void containerChanged(IInventory inventory) {
        boolean wasSaddled = this.isHogSaddled();
        this.updateContainerEquipment();
        if (this.tickCount > 20 && !wasSaddled && this.isHogSaddled()) {
            this.playSound(SoundEvents.HORSE_SADDLE, 0.5F, 1.0F);
        }
    }

    // IHASARMOR METHODS
    @Override
    public ItemStack getArmor() {
        return this.getItemBySlot(EquipmentSlotType.CHEST);
    }

    @Override
    public boolean canWearArmor() {
        return true;
    }

    @Override
    public boolean isArmor(ItemStack stack) {
        return stack.getItem() instanceof HoglinArmorItem;
    }

    // FORGE STUFF
    private net.minecraftforge.common.util.LazyOptional<?> itemHandler = null;

    @Override
    public <T> net.minecraftforge.common.util.LazyOptional<T> getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, @Nullable net.minecraft.util.Direction facing) {
        if (this.isAlive() && capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && itemHandler != null)
            return itemHandler.cast();
        return super.getCapability(capability, facing);
    }

    @Override
    protected void invalidateCaps() {
        super.invalidateCaps();
        if (itemHandler != null) {
            net.minecraftforge.common.util.LazyOptional<?> oldHandler = itemHandler;
            itemHandler = null;
            oldHandler.invalidate();
        }
    }
}
