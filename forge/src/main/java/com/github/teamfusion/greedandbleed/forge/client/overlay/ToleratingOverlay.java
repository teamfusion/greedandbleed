package com.github.teamfusion.greedandbleed.forge.client.overlay;

import com.github.teamfusion.greedandbleed.client.RenderHelper;
import com.github.teamfusion.greedandbleed.common.entity.ToleratingMount;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class ToleratingOverlay implements IGuiOverlay {
    @Override
    public void render(ForgeGui forgeGui, GuiGraphics arg, float f, int i, int j) {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;
        if (player != null && player.getVehicle() instanceof ToleratingMount) {
            ToleratingMount toleratingMount = (ToleratingMount) player.getVehicle();
            RenderHelper.renderToleranceMeter(toleratingMount, arg);
        }
    }
}
