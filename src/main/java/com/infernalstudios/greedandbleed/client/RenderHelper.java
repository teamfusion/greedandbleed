package com.infernalstudios.greedandbleed.client;

import com.infernalstudios.greedandbleed.GreedAndBleed;
import com.infernalstudios.greedandbleed.common.entity.IToleratingMount;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IngameGui;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.lang.reflect.Field;

public class RenderHelper {
    public static final ResourceLocation TOLERANCE_METER_LOCATION = new ResourceLocation(GreedAndBleed.MOD_ID, "textures/gui/tolerance_meter.png");
    public static final String GUI_FIELD_NAME = "field_71456_v";
    private static IngameGui ingameGui;
    private static Minecraft minecraft;

    public static void renderToleranceMeter(IToleratingMount toleratingMount, MatrixStack mStack)
    {
        boolean minecraftChanged = minecraft != Minecraft.getInstance();
        if(minecraftChanged){
            GreedAndBleed.LOGGER.info("RenderHelper.minecraft was updated to the current Minecraft instance!");
        }
        minecraft = minecraftChanged ? Minecraft.getInstance() : minecraft;
        minecraft.getTextureManager().bind(TOLERANCE_METER_LOCATION);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.disableBlend();

        minecraft.getProfiler().push("toleranceBar");
        minecraft.getTextureManager().bind(TOLERANCE_METER_LOCATION);

        float toleranceProgress = toleratingMount.getToleranceProgress();
        int barLength = 182;
        int barHeight = 5;
        int scaledToleranceProgress = (int)(toleranceProgress * 183.0F);

        int screenWidth = minecraft.getWindow().getGuiScaledWidth();
        int barXPos = screenWidth / 2 - 91;
        int screenHeight = minecraft.getWindow().getGuiScaledHeight();
        int barYPos = screenHeight - 32 + 3;

        int emptyBarTexOffsX = 0;
        int emptyBarTexOffsY = 84;
        int fullBarTexOffsX = 0;
        int fullTexOffsY = 89;

        IngameGui ingameGui = getIngameGui(minecraftChanged);
        if(ingameGui != null){
            ingameGui.blit(mStack, barXPos, barYPos, emptyBarTexOffsX, emptyBarTexOffsY, barLength, barHeight);
            if (scaledToleranceProgress > 0) {
                ingameGui.blit(mStack, barXPos, barYPos, fullBarTexOffsX, fullTexOffsY, scaledToleranceProgress, barHeight);
            }
        } else{
            GreedAndBleed.LOGGER.error("Unable to render tolerance meter!");
        }

        minecraft.getProfiler().pop();

        RenderSystem.enableBlend();
        minecraft.getProfiler().pop();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    @Nullable
    private static IngameGui getIngameGui(boolean minecraftChanged){
        if(ingameGui == null || minecraftChanged){
            Field gui = ObfuscationReflectionHelper.findField(Minecraft.class, GUI_FIELD_NAME);
            try {
                ingameGui = (IngameGui) gui.get(Minecraft.getInstance());
                return ingameGui;
            } catch (IllegalAccessException e) {
                GreedAndBleed.LOGGER.error("Couldn't get the value of the Minecraft.gui field! Used field name {}", GUI_FIELD_NAME);
                return null;
            }
        }
        return ingameGui;
    }
}
