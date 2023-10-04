package com.github.teamfusion.greedandbleed.client;

import com.github.teamfusion.greedandbleed.GreedAndBleed;
import com.github.teamfusion.greedandbleed.common.entity.ToleratingMount;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class RenderHelper {
    public static final ResourceLocation TOLERANCE_METER_LOCATION = new ResourceLocation(GreedAndBleed.MOD_ID, "textures/gui/tolerance_meter.png");
    private static Gui ingameGui;
    private static Minecraft minecraft;

    public static void renderToleranceMeter(ToleratingMount toleratingMount, GuiGraphics guiGraphics) {
        boolean minecraftChanged = minecraft != Minecraft.getInstance();
        if (minecraftChanged) {
            GreedAndBleed.LOGGER.info("RenderHelper.minecraft was updated to the current Minecraft instance!");
        }
        minecraft = minecraftChanged ? Minecraft.getInstance() : minecraft;
        minecraft.getProfiler().push("toleranceBar");

        float toleranceProgress = toleratingMount.getToleranceProgress();
        int barLength = 182;
        int barHeight = 5;
        int scaledToleranceProgress = (int) (toleranceProgress * 183.0F);

        int screenWidth = minecraft.getWindow().getGuiScaledWidth();
        int barXPos = screenWidth / 2 - 91;
        int screenHeight = minecraft.getWindow().getGuiScaledHeight();
        int barYPos = screenHeight - 32 + 3;

        int emptyBarTexOffsX = 0;
        int emptyBarTexOffsY = 84;
        int fullBarTexOffsX = 0;
        int fullTexOffsY = 89;

        Gui ingameGui = getIngameGui(minecraftChanged);
        if (ingameGui != null) {
            guiGraphics.blit(TOLERANCE_METER_LOCATION, barXPos, barYPos, emptyBarTexOffsX, emptyBarTexOffsY, barLength, barHeight);
            if (scaledToleranceProgress > 0) {
                guiGraphics.blit(TOLERANCE_METER_LOCATION, barXPos, barYPos, fullBarTexOffsX, fullTexOffsY, scaledToleranceProgress, barHeight);
            }
        } else {
            GreedAndBleed.LOGGER.error("Unable to render tolerance meter!");
        }

        minecraft.getProfiler().pop();
    }

    @Nullable
    private static Gui getIngameGui(boolean minecraftChanged) {
        if (ingameGui == null || minecraftChanged) {
            Gui gui = Minecraft.getInstance().gui;
            ingameGui = gui;
        }
        return ingameGui;
    }
}