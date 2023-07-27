package com.github.teamfusion.greedandbleed.client.screen;

import com.github.teamfusion.greedandbleed.GreedAndBleed;
import com.github.teamfusion.greedandbleed.api.HogEquipable;
import com.github.teamfusion.greedandbleed.common.entity.HasMountArmor;
import com.github.teamfusion.greedandbleed.common.entity.HasMountInventory;
import com.github.teamfusion.greedandbleed.common.inventory.HoglinInventoryMenu;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.entity.player.Inventory;

public class HoglinInventoryScreen extends AbstractContainerScreen<HoglinInventoryMenu> {
    private static final ResourceLocation HOGLIN_INVENTORY_LOCATION = new ResourceLocation(GreedAndBleed.MOD_ID, "textures/gui/container/hoglin.png");
    private final Hoglin hoglin;
    private float xMouse;
    private float yMouse;

    public HoglinInventoryScreen(HoglinInventoryMenu hoglinInventoryMenu, Inventory inventory, Hoglin hoglin) {
        super(hoglinInventoryMenu, inventory, hoglin.getDisplayName());
        this.hoglin = hoglin;
    }

    protected void renderBg(GuiGraphics poseStack, float f, int i, int j) {
        RenderSystem.setShaderTexture(0, HOGLIN_INVENTORY_LOCATION);
        int k = (this.width - this.imageWidth) / 2;
        int l = (this.height - this.imageHeight) / 2;
        poseStack.blit(HOGLIN_INVENTORY_LOCATION, k, l, 0, 0, this.imageWidth, this.imageHeight);
        HasMountInventory chestedHoglin = (HasMountInventory) this.hoglin;
        if (chestedHoglin.hasChest()) {
            poseStack.blit(HOGLIN_INVENTORY_LOCATION, k + 79, l + 17, 0, this.imageHeight, chestedHoglin.getInventoryColumns() * 18, 54);
        }


        if (((HogEquipable) this.hoglin).isHogSaddleable()) {
            poseStack.blit(HOGLIN_INVENTORY_LOCATION, k + 7, l + 35 - 18, 18, this.imageHeight + 54, 18, 18);
        }

        if (((HasMountArmor) this.hoglin).canWearArmor()) {
            poseStack.blit(HOGLIN_INVENTORY_LOCATION, k + 7, l + 35, 0, this.imageHeight + 54, 18, 18);
        }

        InventoryScreen.renderEntityInInventoryFollowsMouse(poseStack, k + 51, l + 60, 17, (float) (k + 51) - this.xMouse, (float) (l + 75 - 50) - this.yMouse, this.hoglin);
    }

    public void render(GuiGraphics poseStack, int i, int j, float f) {
        this.renderBackground(poseStack);
        this.xMouse = (float) i;
        this.yMouse = (float) j;
        super.render(poseStack, i, j, f);
        this.renderTooltip(poseStack, i, j);
    }
}
