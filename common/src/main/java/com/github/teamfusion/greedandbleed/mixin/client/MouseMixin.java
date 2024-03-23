package com.github.teamfusion.greedandbleed.mixin.client;

import com.github.teamfusion.greedandbleed.client.RenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.sounds.SoundEvents;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public class MouseMixin {
    @Shadow
    @Final
    private Minecraft minecraft;
    @Shadow
    private double accumulatedScroll;

    /**
     * Updates the local scrutiny level based on client scrolling.
     */
    @Inject(
            method = "onScroll",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/MouseHandler;accumulatedScroll:D"
                    , ordinal = 7),
            cancellable = true
    )
    private void handleZoomScrolling(CallbackInfo ci) {
        LocalPlayer player = this.minecraft.player;
        if (RenderHelper.onMouseScrolled(minecraft, accumulatedScroll)) {
            player.playSound(SoundEvents.BUNDLE_INSERT, 1.0F, 1.0F);
            ci.cancel();
        }
    }
}