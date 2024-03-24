package com.github.teamfusion.greedandbleed.mixin.client;

import com.github.teamfusion.greedandbleed.common.registry.ItemRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Shadow @Nullable public LocalPlayer player;
    @Shadow @Nullable public MultiPlayerGameMode gameMode;
    @Shadow @Final public GameRenderer gameRenderer;

    @Inject(
        method = "startUseItem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;isEmpty()Z",
            shift = At.Shift.AFTER
        ),
        cancellable = true
    )
    private void injected(CallbackInfo ci) {
        for (InteractionHand hand : InteractionHand.values()) {
            if (this.player != null && this.player.getItemInHand(hand).is(ItemRegistry.SLINGSHOT.get())) {
                InteractionResult result = this.gameMode.useItem(this.player, hand);
                if (result.consumesAction()) {
                    if (result.shouldSwing()) {
                        this.player.swing(hand);
                    }

                    this.gameRenderer.itemInHandRenderer.itemUsed(hand);
                    ci.cancel();
                }
            }
        }
    }
}
