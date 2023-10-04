package com.github.teamfusion.greedandbleed.mixin;

import com.github.teamfusion.greedandbleed.client.network.GreedAndBleedClientNetwork;
import com.github.teamfusion.greedandbleed.common.entity.CanOpenMountInventory;
import com.github.teamfusion.greedandbleed.common.inventory.HoglinInventoryMenu;
import com.mojang.authlib.GameProfile;
import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin extends Player implements CanOpenMountInventory {
    @Shadow
    private int containerCounter;

    public ServerPlayerMixin(Level level, BlockPos blockPos, float f, GameProfile gameProfile) {
        super(level, blockPos, f, gameProfile);
    }

    @Override
    public void openMountInventory(Animal mount, SimpleContainer inventory) {
        if (mount instanceof Hoglin hoglin) {
            ServerPlayer serverPlayer = (ServerPlayer) ((Object) this);
            if (this.containerMenu != this.inventoryMenu) {
                this.closeContainer();
            }

            this.nextContainerCounter();
            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
            buf.writeInt(hoglin.getId());
            buf.writeInt(inventory.getContainerSize());
            buf.writeInt(this.containerCounter);
            NetworkManager.sendToPlayer(serverPlayer, GreedAndBleedClientNetwork.SCREEN_OPEN_PACKET, buf);
            this.containerMenu = new HoglinInventoryMenu(this.containerCounter, inventory, this.getInventory(), hoglin);
            this.initMenu(this.containerMenu);

        }
    }

    @Shadow
    private void initMenu(AbstractContainerMenu containerMenu) {
    }

    @Shadow
    private void nextContainerCounter() {
    }
}