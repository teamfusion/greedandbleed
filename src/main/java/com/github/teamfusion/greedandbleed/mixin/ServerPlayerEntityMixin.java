package com.github.teamfusion.greedandbleed.mixin;

import com.github.teamfusion.greedandbleed.server.network.packet.SOpenHoglinWindowPacket;
import com.github.teamfusion.greedandbleed.GreedAndBleed;
import com.github.teamfusion.greedandbleed.common.entity.ICanOpenMountInventory;
import com.github.teamfusion.greedandbleed.common.inventory.HoglinInventoryContainer;
import com.github.teamfusion.greedandbleed.common.network.NetworkHandler;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.IContainerListener;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.PacketDistributor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity  implements IContainerListener, ICanOpenMountInventory {

    @Shadow
    public int containerCounter;

    public ServerPlayerEntityMixin(World world, BlockPos blockPos, float yRotIn, GameProfile gameProfile) {
        super(world, blockPos, yRotIn, gameProfile);
    }

    @Override
    public void openMountInventory(AnimalEntity mount, IInventory inventory) {
        if (this.containerMenu != this.inventoryMenu) {
            this.closeContainer();
        }

        this.nextContainerCounter();
        SOpenHoglinWindowPacket sOpenHoglinWindowPacket = new SOpenHoglinWindowPacket(this.containerCounter, inventory.getContainerSize(), mount.getId());
        GreedAndBleed.LOGGER.debug("Sending open mount inventory for {} to client!", mount);
        NetworkHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> mount), sOpenHoglinWindowPacket);
        //this.connection.send(new SOpenHorseWindowPacket(this.containerCounter, inventory.getContainerSize(), mount.getId()));
        this.containerMenu = new HoglinInventoryContainer(this.containerCounter, this.inventory, inventory, mount);
        this.containerMenu.addSlotListener(this);
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.entity.player.PlayerContainerEvent.Open(this, this.containerMenu));
    }

    @Shadow
    public abstract void nextContainerCounter();
}
