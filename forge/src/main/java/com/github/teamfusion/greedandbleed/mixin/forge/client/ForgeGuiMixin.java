package com.github.teamfusion.greedandbleed.mixin.forge.client;

import com.github.teamfusion.greedandbleed.client.RenderHelper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ForgeGui.class)
public class ForgeGuiMixin {

    @Inject(method = "render", at = @At(value = "TAIL"))
    public void render(GuiGraphics guiGraphics, float f, CallbackInfo ci) {
        RenderHelper.renderSlingshotPouchSlot(guiGraphics, f);
    }
}
