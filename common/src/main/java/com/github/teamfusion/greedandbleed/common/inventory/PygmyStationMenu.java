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
    private static final int SLOT_COUNT = 9;
    private static final int INV_SLOT_START = 9;
    private static final int INV_SLOT_END = 36;
    private static final int USE_ROW_SLOT_START = 36;
    private static final int USE_ROW_SLOT_END = 45;
    private final Container dispenser;

    public PygmyStationMenu(int i, Inventory inventory) {
        this(i, inventory, new SimpleContainer(5));
    }

    public PygmyStationMenu(int i, Inventory inventory, Container container) {
        super(MenuTypeRegistry.PYGMY_STATION.get(), i);
        checkContainerSize(container, 5);
        this.dispenser = container;
        container.startOpen(inventory.player);

        int j;
        int k;
        for (j = 0; j < 3; ++j) {
            if (j == 0) {

                this.addSlot(new Slot(container, j, 80, 37) {
                    @Override
                    public boolean mayPlace(ItemStack itemStack) {
                        return itemStack.is(ItemRegistry.PIGLIN_BELT.get());
                    }
                });
            } else {
                this.addSlot(new Slot(container, j, 80 + j * 18, 37));
            }
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
        return this.dispenser.stillValid(player);
    }

    public ItemStack quickMoveStack(Player player, int i) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = (Slot) this.slots.get(i);
        if (slot != null && slot.hasItem()) {
            ItemStack itemStack2 = slot.getItem();
            itemStack = itemStack2.copy();
            if (i < 9) {
                if (!this.moveItemStackTo(itemStack2, 5, 41, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemStack2, 0, 5, false)) {
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
        this.dispenser.stopOpen(player);
    }
}
