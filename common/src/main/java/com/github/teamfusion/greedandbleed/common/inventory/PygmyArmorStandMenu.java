package com.github.teamfusion.greedandbleed.common.inventory;

import com.github.teamfusion.greedandbleed.common.registry.MenuTypeRegistry;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class PygmyArmorStandMenu extends AbstractContainerMenu {
    private final Container station;

    public PygmyArmorStandMenu(int i, Inventory inventory) {
        this(i, inventory, new SimpleContainer(4));
    }

    public PygmyArmorStandMenu(int i, Inventory inventory, Container container) {
        super(MenuTypeRegistry.PYGMY_ARMOR_STAND.get(), i);
        checkContainerSize(container, 4);
        this.station = container;
        container.startOpen(inventory.player);

        int j;
        int k;
        for (j = 0; j < 4; ++j) {
            this.addSlot(new Slot(container, j, 98 + j * 18, 37));
        }


        for (j = 0; j < 3; ++j) {
            for (k = 0; k < 9; ++k) {
                this.addSlot(new Slot(inventory, k + j * 9 + 9, 8 + k * 18, 84 + j * 18));
            }
        }

        for (j = 0; j < 9; ++j) {
            this.addSlot(new Slot(inventory, j, 8 + j * 18, 142));
        }

    }

    public boolean stillValid(Player player) {
        return this.station.stillValid(player);
    }

    public ItemStack quickMoveStack(Player player, int i) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = (Slot) this.slots.get(i);
        if (slot != null && slot.hasItem()) {
            ItemStack itemStack2 = slot.getItem();
            itemStack = itemStack2.copy();
            if (i < 4) {
                if (!this.moveItemStackTo(itemStack2, 4, 41 - 1, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemStack2, 0, 4, false)) {
                return ItemStack.EMPTY;
            }

            if (itemStack2.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemStack2.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemStack2);
        }

        return itemStack;
    }

    public void removed(Player player) {
        super.removed(player);
        this.station.stopOpen(player);
    }
}
