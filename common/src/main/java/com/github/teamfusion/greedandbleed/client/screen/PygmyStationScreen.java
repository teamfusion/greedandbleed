package com.github.teamfusion.greedandbleed.client.screen;

import com.github.teamfusion.greedandbleed.GreedAndBleed;
import com.github.teamfusion.greedandbleed.api.IGBPlayer;
import com.github.teamfusion.greedandbleed.common.inventory.PygmyStationMenu;
import com.github.teamfusion.greedandbleed.common.network.GreedAndBleedServerNetwork;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class PygmyStationScreen extends AbstractContainerScreen<PygmyStationMenu> {
    private static final ResourceLocation PYGMY_INVENTORY_LOCATION = new ResourceLocation(GreedAndBleed.MOD_ID, "textures/gui/container/pygmy_station.png");

    public PygmyStationScreen(PygmyStationMenu pygmyStationMenu, Inventory inventory, Component component) {
        super(pygmyStationMenu, inventory, component);
    }

    @Override
    protected void init() {
        super.init();
        int k = (this.width - this.imageWidth) / 2;
        int l = (this.height - this.imageHeight) / 2;
        this.addRenderableWidget(Button.builder(Component.translatable("gui.pygmy_station.recruit"), button -> {
            if (Minecraft.getInstance().player instanceof IGBPlayer gbPlayer && gbPlayer.getBlockEntityPos() != null) {
                FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
                buf.writeBlockPos(gbPlayer.getBlockEntityPos());
                NetworkManager.sendToServer(GreedAndBleedServerNetwork.RECRUIT_PACKET, buf);
            }
            this.onClose();
        }).pos(k + 10, l + 50).size(69 - 16, 21).build());
    }

    protected void renderBg(GuiGraphics poseStack, float f, int i, int j) {
        RenderSystem.setShaderTexture(0, PYGMY_INVENTORY_LOCATION);
        int k = (this.width - this.imageWidth) / 2;
        int l = (this.height - this.imageHeight) / 2;
        poseStack.blit(PYGMY_INVENTORY_LOCATION, k, l, 0, 0, this.imageWidth, this.imageHeight);

        //InventoryScreen.renderEntityInInventoryFollowsMouse(poseStack, k + 51, l + 60, 17, (float) (k + 51) - this.xMouse, (float) (l + 75 - 50) - this.yMouse, this.hoglin);
    }

    public void render(GuiGraphics poseStack, int i, int j, float f) {
        this.renderBackground(poseStack);
        super.render(poseStack, i, j, f);
        this.renderTooltip(poseStack, i, j);
    }
}
