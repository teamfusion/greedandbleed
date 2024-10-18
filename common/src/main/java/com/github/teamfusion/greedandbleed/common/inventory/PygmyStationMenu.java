package com.github.teamfusion.greedandbleed.common.inventory;

import com.github.teamfusion.greedandbleed.common.registry.ItemRegistry;
import com.github.teamfusion.greedandbleed.common.registry.MenuTypeRegistry;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class PygmyStationMenu extends AbstractContainerMenu {
    private final Container station;
    public PygmyStationMenu(int i, Inventory inventory) {
        this(i, inventory, new SimpleContainer(1));
    }

    public PygmyStationMenu(int i, Inventory inventory, Container container) {
        super(MenuTypeRegistry.PYGMY_STATION.get(), i);
        checkContainerSize(container, 1);
        this.station = container;

        container.startOpen(inventory.player);

        int j;
        int k;

        this.addSlot(new Slot(container, 0, 80, 37) {
                    @Override
                    public boolean mayPlace(ItemStack itemStack) {
                        return itemStack.is(ItemRegistry.PIGLIN_BELT.get());
                    }
                });
            /* else {
                this.addSlot(new Slot(container, j, 80 + j * 18, 37));
            }*/



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
            if (i < 1) {
                if (!this.moveItemStackTo(itemStack2, 1, 41 - 4, true)) {
                    return ItemStack.EMPTY;
                }
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
