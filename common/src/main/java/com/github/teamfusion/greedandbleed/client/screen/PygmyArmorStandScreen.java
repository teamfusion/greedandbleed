package com.github.teamfusion.greedandbleed.client.screen;

import com.github.teamfusion.greedandbleed.GreedAndBleed;
import com.github.teamfusion.greedandbleed.common.inventory.PygmyArmorStandMenu;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class PygmyArmorStandScreen extends AbstractContainerScreen<PygmyArmorStandMenu> {
    private static final ResourceLocation PYGMY_INVENTORY_LOCATION = new ResourceLocation(GreedAndBleed.MOD_ID, "textures/gui/container/pygmy_armor_stand.png");

    public PygmyArmorStandScreen(PygmyArmorStandMenu pygmyStationMenu, Inventory inventory, Component component) {
        super(pygmyStationMenu, inventory, component);
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
