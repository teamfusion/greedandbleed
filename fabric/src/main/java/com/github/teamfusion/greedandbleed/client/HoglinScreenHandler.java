package com.github.teamfusion.greedandbleed.client;

import com.github.teamfusion.greedandbleed.common.inventory.HoglinInventoryMenu;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.entity.player.Inventory;

public class HoglinScreenHandler implements ScreenHandlerRegistry.ExtendedClientHandlerFactory<HoglinInventoryMenu> {


    @Override
    public HoglinInventoryMenu create(int syncId, Inventory inventory, FriendlyByteBuf buf) {
        Entity entity = inventory.player.level().getEntity(buf.readInt());
        return new HoglinInventoryMenu(syncId, new SimpleContainer(buf.readInt()), inventory, (Hoglin) entity);
    }
}
