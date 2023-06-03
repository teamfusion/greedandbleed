package com.github.teamfusion.greedandbleed.common.inventory;

import com.github.teamfusion.greedandbleed.api.HogEquipable;
import com.github.teamfusion.greedandbleed.common.entity.HasMountArmor;
import com.github.teamfusion.greedandbleed.common.entity.HasMountInventory;
import com.github.teamfusion.greedandbleed.common.entity.ToleratingMount;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class HoglinInventoryMenu extends AbstractContainerMenu {
    private final Container hoglinContainer;
    private final Hoglin hoglin;

    public HoglinInventoryMenu(int containerId, SimpleContainer inventory, Inventory playerInventory, Hoglin hoglin) {
        super(null, containerId);
        this.hoglinContainer = inventory;
        if (!(hoglin instanceof HogEquipable)
                || !(hoglin instanceof HasMountArmor)
                || !(hoglin instanceof ToleratingMount)) {
            throw new IllegalArgumentException("This entity type " + hoglin.getType() + " is not valid for HoglinInventoryContainer!");
        }
        this.hoglin = hoglin;
        inventory.startOpen(playerInventory.player);
        this.addSlot(new Slot(inventory, 0, 8, 18) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return ((ToleratingMount) hoglin).isSaddleStack(stack)
                        && !this.hasItem()
                        && ((HogEquipable) hoglin).isHogSaddleable();
            }

            @Override
            public boolean isActive() {
                return ((HogEquipable) hoglin).isHogSaddleable();
            }
        });
        this.addSlot(new Slot(inventory, 1, 8, 36) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return ((HasMountArmor) hoglin).isArmor(stack);
            }

            @Override
            public boolean isActive() {
                return ((HasMountArmor) hoglin).canWearArmor();
            }

            @Override
            public int getMaxStackSize() {
                return 1;
            }
        });
        if (hoglin instanceof HasMountInventory && ((HasMountInventory) hoglin).hasChest()) {
            for (int k = 0; k < 3; ++k) {
                for (int l = 0; l < ((HasMountInventory) hoglin).getInventoryColumns(); ++l) {
                    this.addSlot(new Slot(inventory, 2 + l + k * ((HasMountInventory) hoglin).getInventoryColumns(), 80 + l * 18, 18 + k * 18));
                }
            }
        }

        for (int i1 = 0; i1 < 3; ++i1) {
            for (int k1 = 0; k1 < 9; ++k1) {
                this.addSlot(new Slot(playerInventory, k1 + i1 * 9 + 9, 8 + k1 * 18, 102 + i1 * 18 - 18));
            }
        }

        for (int j1 = 0; j1 < 9; ++j1) {
            this.addSlot(new Slot(playerInventory, j1, 8 + j1 * 18, 142));
        }

    }

    @Override
    public boolean stillValid(Player player) {
        return this.hoglinContainer.stillValid(player) && this.hoglin.isAlive() && this.hoglin.distanceTo(player) < 8.0F;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotIndex) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(slotIndex);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            int i = this.hoglinContainer.getContainerSize();
            if (slotIndex < i) {
                if (!this.moveItemStackTo(itemstack1, i, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.getSlot(1).mayPlace(itemstack1) && !this.getSlot(1).hasItem()) {
                if (!this.moveItemStackTo(itemstack1, 1, 2, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.getSlot(0).mayPlace(itemstack1)) {
                if (!this.moveItemStackTo(itemstack1, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (i <= 2 || !this.moveItemStackTo(itemstack1, 2, i, false)) {
                int j = i + 27;
                int k = j + 9;
                if (slotIndex >= j && slotIndex < k) {
                    if (!this.moveItemStackTo(itemstack1, i, j, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (slotIndex < j) {
                    if (!this.moveItemStackTo(itemstack1, j, k, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!this.moveItemStackTo(itemstack1, j, j, false)) {
                    return ItemStack.EMPTY;
                }

                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return itemstack;
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.hoglinContainer.stopOpen(player);
    }
}