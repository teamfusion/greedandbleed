package com.github.teamfusion.greedandbleed.mixin.client;

import com.github.teamfusion.greedandbleed.client.RenderHelper;
import com.github.teamfusion.greedandbleed.common.entity.ToleratingMount;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class GuiMixin {

    @Shadow
    private int screenWidth;

    @Shadow
    private int screenHeight;

    @Inject(method = "render", at = @At(value = "HEAD"))
    public void renderHead(GuiGraphics guiGraphics, float f, CallbackInfo ci) {
        RenderHelper.renderWarpLink(guiGraphics, Minecraft.getInstance().player, screenWidth, screenHeight);
    }

    @Inject(method = "render", at = @At(value = "TAIL"))
    public void render(GuiGraphics guiGraphics, float f, CallbackInfo ci) {
        if (Minecraft.getInstance().player.getVehicle() instanceof ToleratingMount toleratingMount) {
            RenderHelper.renderToleranceMeter(toleratingMount, guiGraphics);
        }
        RenderHelper.renderSlingshotPouchSlot(guiGraphics, f);
    }
}
