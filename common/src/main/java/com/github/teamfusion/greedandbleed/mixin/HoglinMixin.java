package com.github.teamfusion.greedandbleed.mixin;

import com.github.teamfusion.greedandbleed.GreedAndBleed;
import com.github.teamfusion.greedandbleed.api.HogEquipable;
import com.github.teamfusion.greedandbleed.common.entity.CanOpenMountInventory;
import com.github.teamfusion.greedandbleed.common.entity.HasMountArmor;
import com.github.teamfusion.greedandbleed.common.entity.HasMountInventory;
import com.github.teamfusion.greedandbleed.common.entity.ToleratingMount;
import com.github.teamfusion.greedandbleed.common.item.HoglinArmorItem;
import com.github.teamfusion.greedandbleed.common.registry.ItemRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerListener;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ItemBasedSteering;
import net.minecraft.world.entity.ItemSteerable;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;
import java.util.function.Predicate;

@SuppressWarnings("WrongEntityDataParameterClass")
@Mixin(Hoglin.class)
public abstract class HoglinMixin extends Animal implements ItemSteerable, HogEquipable, ToleratingMount, ContainerListener, HasMountArmor, HasMountInventory {

    private static final EntityDataAccessor<Boolean> DATA_HAS_SADDLE = SynchedEntityData.defineId(Hoglin.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> DATA_BOOST_TIME = SynchedEntityData.defineId(Hoglin.class, EntityDataSerializers.INT);
    private final ItemBasedSteering steering = new ItemBasedSteering(this.entityData, DATA_BOOST_TIME, DATA_HAS_SADDLE);
    private static final EntityDataAccessor<Integer> DATA_TOLERANCE = SynchedEntityData.defineId(Hoglin.class, EntityDataSerializers.INT);
    private static final UUID ARMOR_MODIFIER_UUID = UUID.fromString("556E1665-8B10-40C8-8F9D-CF9B1667F295");
    protected SimpleContainer inventory;
    private static final EntityDataAccessor<Boolean> DATA_HAS_CHEST = SynchedEntityData.defineId(Hoglin.class, EntityDataSerializers.BOOLEAN);

    protected HoglinMixin(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    //MIXINS

    @Inject(method = "finishConversion", at = @At("RETURN"))
    private void gb$dropInventoryWhenZombified(ServerLevel level, CallbackInfo ci) {
        this.dropEquipment();
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void gb$setUpInventory(EntityType<? extends Hoglin> entityType, Level level, CallbackInfo ci) {
        this.createInventory();
    }

    @Inject(method = "mobInteract", at = @At("RETURN"), cancellable = true)
    private void gb$handleMountInteraction(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        ItemStack stack = player.getItemInHand(hand);
        if (!this.isBaby()) {
            // Handle opening inventory with shift-key down during interaction
            if (isPacified(this) && player.isSecondaryUseActive()) {
                this.openInventory(player);
                cir.setReturnValue(InteractionResult.sidedSuccess(this.level.isClientSide));
                return;
            }

            // Handle interacting with the hoglind as it is being ridden
            if (this.isVehicle()) {
                cir.setReturnValue(super.mobInteract(player, hand));
                return;
            }
        }

        if (!stack.isEmpty()) {
            // Return early if this is food for the hoglin, the original method already handled it
            if (this.isFood(stack)) {
                //return this.fedFood(player, stack);
                return;
            }

            if (isPacified(this)) {
                InteractionResult result = stack.interactLivingEntity(player, this, hand);
                // Return if the interaction was successful
                if (result.consumesAction()) {
                    cir.setReturnValue(result);
                    return;
                }

                // Handle equipping a chest and creating an inventory for the hoglin
                if (!this.hasChest() && this.isChestStack(stack)) {
                    this.setChest(true);
                    this.playChestEquipsSound();
                    if (!player.getAbilities().instabuild) {
                        stack.shrink(1);
                    }

                    this.createInventory();
                    cir.setReturnValue(InteractionResult.sidedSuccess(this.level.isClientSide));
                    return;
                }

                // Handle opening the hoglin's inventory with a saddle or armor
                boolean canGiveSaddle = !this.isBaby() && !this.isHogSaddled() && this.isSaddleStack(stack);
                boolean canOpenInventoryWithItem = this.isArmor(stack) || canGiveSaddle;
                if (canOpenInventoryWithItem) {
                    this.openInventory(player);
                    cir.setReturnValue(InteractionResult.sidedSuccess(this.level.isClientSide));
                    return;
                }
            } else {
                // Make the hoglin angry and reject the interaction
                this.playSound(SoundEvents.HOGLIN_ANGRY, this.getSoundVolume(), this.getVoicePitch());
                cir.setReturnValue(InteractionResult.FAIL);
                return;
            }
        }

        if (this.isBaby()) {
            cir.setReturnValue(super.mobInteract(player, hand));
        } else if (isPacified(this)) {
            // Handle mounting the hoglin
            player.startRiding(this);
            cir.setReturnValue(InteractionResult.sidedSuccess(this.level.isClientSide));
        }
    }

    @Inject(method = "defineSynchedData", at = @At("RETURN"))
    private void gb$defineSteeringData(CallbackInfo ci) {
        this.entityData.define(DATA_HAS_SADDLE, false);
        this.entityData.define(DATA_BOOST_TIME, 0);
        this.entityData.define(DATA_TOLERANCE, 0);
        this.entityData.define(DATA_HAS_CHEST, false);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("RETURN"))
    private void gb$addSteeringData(CompoundTag tag, CallbackInfo ci) {
        this.steering.addAdditionalSaveData(tag);
        tag.putInt("Tolerance", this.getTolerance());

        if (!this.inventory.getItem(0).isEmpty()) {
            tag.put("SaddleItem", this.inventory.getItem(0).save(new CompoundTag()));
        }

        if (!this.inventory.getItem(1).isEmpty()) {
            tag.put("ArmorItem", this.inventory.getItem(1).save(new CompoundTag()));
        }

        tag.putBoolean("ChestedHoglin", this.hasChest());
        if (this.hasChest()) {
            ListTag items = new ListTag();

            for (int slot = 2; slot < this.inventory.getContainerSize(); slot++) {
                ItemStack stack = this.inventory.getItem(slot);
                if (!stack.isEmpty()) {
                    CompoundTag item = new CompoundTag();
                    item.putByte("Slot", (byte)slot);
                    stack.save(item);
                    items.add(item);
                }
            }

            tag.put("Items", items);
        }
    }

    @Inject(method = "readAdditionalSaveData", at = @At("RETURN"))
    private void gb$readSteeringData(CompoundTag tag, CallbackInfo ci) {
        this.steering.readAdditionalSaveData(tag);
        this.setTolerance(tag.getInt("Tolerance"));

        if (tag.contains("SaddleItem", 10)) {
            ItemStack stack = ItemStack.of(tag.getCompound("SaddleItem"));
            if (this.isSaddleStack(stack)) {
                this.inventory.setItem(0, stack);
            }
        }

        if (tag.contains("ArmorItem", 10)) {
            ItemStack stack = ItemStack.of(tag.getCompound("ArmorItem"));
            if (!stack.isEmpty() && this.isArmor(stack)) {
                this.inventory.setItem(1, stack);
            }
        }

        this.setChest(tag.getBoolean("ChestedHoglin"));
        if (this.hasChest()) {
            ListTag items = tag.getList("Items", 10);
            this.createInventory();

            for (int i = 0; i < items.size(); i++) {
                CompoundTag item = items.getCompound(i);
                int slot = item.getByte("Slot") & 255;
                if (slot >= 2 && slot < this.inventory.getContainerSize()) {
                    this.inventory.setItem(slot, ItemStack.of(item));
                }
            }
        }

        this.updateContainerEquipment();
    }

    @Inject(method = "doHurtTarget", at = @At("HEAD"))
    private void gb$removeRidersIfTargetingThem(Entity target, CallbackInfoReturnable<Boolean> cir) {
        if (this.getPassengers().contains(target)) {
            this.ejectPassengers();
        }
    }

    // HELPERS

    private static boolean isPacified(Animal hoglin) {
        return hoglin.getBrain().hasMemoryValue(MemoryModuleType.PACIFIED);
    }

    private void setArmorEquipment(ItemStack stack) {
        this.setArmor(stack);
        if (!this.level.isClientSide) {
            AttributeInstance armor = this.getAttribute(Attributes.ARMOR);
            if (armor != null) {
                armor.removeModifier(ARMOR_MODIFIER_UUID);
                if (this.isArmor(stack)) {
                    int i = ((HoglinArmorItem)stack.getItem()).getProtection();
                    if (i != 0) {
                        armor.addTransientModifier(new AttributeModifier(ARMOR_MODIFIER_UUID, "Horse armor bonus", i, AttributeModifier.Operation.ADDITION));
                    }
                }
            }
        }
    }

    private void setArmor(ItemStack stack) {
        this.setItemSlot(EquipmentSlot.CHEST, stack);
        this.setDropChance(EquipmentSlot.CHEST, 0.0F);
    }

    private void playChestEquipsSound() {
        this.playSound(SoundEvents.DONKEY_CHEST, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
    }

    // OVERRIDEN METHODS

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> data) {
        if (DATA_BOOST_TIME.equals(data) && this.level.isClientSide) {
            this.steering.onSynced();
        }

        super.onSyncedDataUpdated(data);
    }

    @Nullable @Override
    public Entity getControllingPassenger() {
        return this.getPassengers().isEmpty() ? null : this.getPassengers().get(0);
    }

    @Override
    public boolean canBeControlledByRider() {
        Entity entity = this.getControllingPassenger();
        if (entity instanceof Player player) {
            if (!this.isTolerating()) {
                return false;
            }

            return player.getMainHandItem().is(ItemRegistry.CRIMSON_FUNGUS_ON_A_STICK.get()) || player.getOffhandItem().is(ItemRegistry.CRIMSON_FUNGUS_ON_A_STICK.get());
        } else {
            return false;
        }
    }

    @Override
    protected void dropEquipment() {
        super.dropEquipment();
        if (this.inventory != null) {
            for (int i = 0; i < this.inventory.getContainerSize(); i++) {
                ItemStack stack = this.inventory.getItem(i);
                if (!stack.isEmpty() && !EnchantmentHelper.hasVanishingCurse(stack)) {
                    this.spawnAtLocation(stack);
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

    private SlotAccess createEquipmentSlotAccess(int slot, Predicate<ItemStack> predicate) {
        return new SlotAccess() {
            @Override
            public ItemStack get() {
                return HoglinMixin.this.inventory.getItem(slot);
            }

            @Override
            public boolean set(ItemStack stack) {
                if (!predicate.test(stack)) {
                    return false;
                } else {
                    HoglinMixin.this.inventory.setItem(slot, stack);
                    HoglinMixin.this.updateContainerEquipment();
                    return true;
                }
            }
        };
    }

    private SlotAccess getEquipmentSlot(int i) {
        int j = i - 400;
        if (j >= 0 && j < 2 && j < this.inventory.getContainerSize()) {
            if (j == 0) {
                return this.createEquipmentSlotAccess(j, stack -> stack.isEmpty() || this.isSaddleStack(stack));
            }

            if (j == 1) {
                if (!this.canWearArmor()) {
                    return SlotAccess.NULL;
                }

                return this.createEquipmentSlotAccess(j, itemStack -> itemStack.isEmpty() || this.isArmor(itemStack));
            }
        }

        int k = i - 500 + 2;
        return k >= 2 && k < this.inventory.getContainerSize() ? SlotAccess.forContainer(this.inventory, k) : super.getSlot(i);
    }

    @Override
    public SlotAccess getSlot(int slot) {
        return slot == 499 ? new SlotAccess() {
            @Override
            public ItemStack get() {
                return HoglinMixin.this.hasChest() ? new ItemStack(Items.CHEST) : ItemStack.EMPTY;
            }

            @Override
            public boolean set(ItemStack stack) {
                if (stack.isEmpty()) {
                    if (HoglinMixin.this.hasChest()) {
                        HoglinMixin.this.setChest(false);
                        HoglinMixin.this.createInventory();
                    }

                    return true;
                } else if (HoglinMixin.this.isChestStack(stack)) {
                    if (!HoglinMixin.this.hasChest()) {
                        HoglinMixin.this.setChest(true);
                        HoglinMixin.this.createInventory();
                    }

                    return true;
                } else {
                    return false;
                }
            }
        } : this.getEquipmentSlot(slot);
    }

    // ITEM STEERABLE METHODS

    @Override
    public boolean boost() {
        //return this.steering.boost(this.getRandom());
        return false;
    }

    @Override
    public void travelWithInput(Vec3 movementInput) {
        super.travel(movementInput);
    }

    @Override
    public void travel(Vec3 movementInput) {
        this.travel(this, this.steering, movementInput);
    }

    @Override
    public float getSteeringSpeed() {
        float toleranceProgress = this.getToleranceProgress();
        float minSpeedFactor = 0.4F;
        float midSpeedFactor = minSpeedFactor * 1.75F;
        float maxSpeedFactor = minSpeedFactor * 2.5F;
        float speedFactor = toleranceProgress > 0.66F ? maxSpeedFactor : toleranceProgress > 0.33F ? midSpeedFactor : maxSpeedFactor;
        return (float)this.getAttributeValue(Attributes.MOVEMENT_SPEED) * speedFactor;
    }

    // SADDLEABLE METHODS

    @Override
    public boolean isHogSaddleable() {
        return this.isAlive() && !this.isBaby();
    }

    @Override
    public void equipHogSaddle(@Nullable SoundSource soundSource) {
        this.setSaddled(true);
        if (soundSource != null) {
            this.level.playSound(null, this, SoundEvents.HORSE_SADDLE, soundSource, 0.5F, 1.0F);
        }
    }

    @Override
    public boolean isHogSaddled() {
        return this.steering.hasSaddle();
    }

    private void setSaddled(boolean saddled) {
        this.steering.setSaddle(saddled);
        this.inventory.setItem(0, saddled ? new ItemStack(this.getDefaultSaddleItem()) : ItemStack.EMPTY);
    }

    // TOLERATING MOUNT METHODS

    @Override
    public boolean canAcceptSaddle() {
        return this.isHogSaddleable() && isPacified(this);
    }

    @Override
    public Item getDefaultSaddleItem() {
        return ItemRegistry.HOGLIN_SADDLE.get();
    }

    @Override
    public int getTolerance() {
        return this.entityData.get(DATA_TOLERANCE);
    }

    @Override
    public int getMaxTolerance() {
        return 18 * 20;
    }

    @Override
    public void setTolerance(int value) {
        if (value > this.getMaxTolerance()) {
            this.entityData.set(DATA_TOLERANCE, this.getMaxTolerance());
            GreedAndBleed.LOGGER.debug("Tried to set tolerance for {} as {} when max is {}", this, value, this.getMaxTolerance());
        } else if (value < 0) {
            this.entityData.set(DATA_TOLERANCE, 0);
            GreedAndBleed.LOGGER.debug("Tried to set tolerance for {} as {} which is below 0", this, value);
        } else {
            this.entityData.set(DATA_TOLERANCE, value);
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
        SimpleContainer inventory = this.inventory;
        this.inventory = new SimpleContainer(this.getInventorySize());
        if (inventory != null) {
            inventory.removeListener(this);
            int i = Math.min(inventory.getContainerSize(), this.inventory.getContainerSize());

            for (int slot = 0; slot < i; slot++) {
                ItemStack stack = inventory.getItem(slot);
                if (!stack.isEmpty()) {
                    this.inventory.setItem(slot, stack.copy());
                }
            }
        }

        this.inventory.addListener(this);
        this.updateContainerEquipment();
    }

    private void updateContainerEquipment() {
        if (!this.level.isClientSide) {
            this.steering.setSaddle(!this.inventory.getItem(0).isEmpty());
            this.setArmorEquipment(this.inventory.getItem(1));
            this.setDropChance(EquipmentSlot.CHEST, 0.0F);
        }
    }

    // HAS MOUNT INVENTORY

    @Override
    public Item getDefaultChestItem() {
        return Blocks.CHEST.asItem();
    }

    @Override
    public void openInventory(Player player) {
        if (!this.level.isClientSide && (!this.isVehicle() || this.hasPassenger(player)) && player instanceof CanOpenMountInventory rider) {
            GreedAndBleed.LOGGER.debug("Opening mount inventory for {}", this);
            rider.openMountInventory(this, this.inventory);
        }
    }

    @Override
    public boolean hasChest() {
        return this.entityData.get(DATA_HAS_CHEST);
    }

    @Override
    public void setChest(boolean chest) {
        this.entityData.set(DATA_HAS_CHEST, chest);
    }

    @Override
    public int getInventoryColumns() {
        return 5;
    }

    // INVENTORY CHANGED LISTENER

    @Override
    public void containerChanged(Container container) {
        boolean wasSaddled = this.isHogSaddled();
        this.updateContainerEquipment();
        if (this.tickCount > 20 && !wasSaddled && this.isHogSaddled()) {
            this.playSound(SoundEvents.HORSE_SADDLE, 0.5F, 1.0F);
        }
    }

    // HAS ARMOR

    @Override
    public ItemStack getArmor() {
        return this.getItemBySlot(EquipmentSlot.CHEST);
    }

    @Override
    public boolean canWearArmor() {
        return true;
    }

    @Override
    public boolean isArmor(ItemStack stack) {
        return stack.getItem() instanceof HoglinArmorItem;
    }
}