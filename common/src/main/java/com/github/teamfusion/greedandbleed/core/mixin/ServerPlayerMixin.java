package com.github.teamfusion.greedandbleed.core.mixin;

import com.github.teamfusion.greedandbleed.GreedAndBleed;
import com.github.teamfusion.greedandbleed.client.network.ClientboundScreenOpenPacket;
import com.github.teamfusion.greedandbleed.common.entity.CanOpenMountInventory;
import com.github.teamfusion.greedandbleed.common.inventory.HoglinInventoryMenu;
import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
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
    public abstract ServerLevel serverLevel();

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
            this.containerMenu = new HoglinInventoryMenu(this.containerCounter, inventory, this.getInventory(), hoglin);
            this.initMenu(this.containerMenu);
            GreedAndBleed.NETWORKING.sendToPlayersTrackingEntity(new ClientboundScreenOpenPacket(hoglin.getId(), inventory.getContainerSize(), this.containerCounter), serverPlayer);
        }
    }

    @Shadow
    private void initMenu(AbstractContainerMenu containerMenu) {
    }

    @Shadow
    private void nextContainerCounter() {
    }
}