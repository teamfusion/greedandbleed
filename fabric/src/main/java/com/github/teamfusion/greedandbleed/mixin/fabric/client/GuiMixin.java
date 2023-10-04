package com.github.teamfusion.greedandbleed.mixin.fabric.client;

import com.github.teamfusion.greedandbleed.client.RenderHelper;
import com.github.teamfusion.greedandbleed.common.entity.ToleratingMount;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class GuiMixin {

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;jumpableVehicle()Lnet/minecraft/world/entity/PlayerRideableJumping;", shift = At.Shift.AFTER))
    public void render(GuiGraphics guiGraphics, float f, CallbackInfo ci) {
        if (Minecraft.getInstance().player.getControlledVehicle() instanceof ToleratingMount toleratingMount) {
            RenderHelper.renderToleranceMeter(toleratingMount, guiGraphics);
        }
    }
}
