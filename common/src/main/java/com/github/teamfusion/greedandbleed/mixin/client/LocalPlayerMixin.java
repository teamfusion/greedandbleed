package com.github.teamfusion.greedandbleed.mixin.client;

import com.github.teamfusion.greedandbleed.common.CommonSetup;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin extends Player {
    @Shadow
    public Input input;

    public LocalPlayerMixin(Level level, BlockPos blockPos, float f, GameProfile gameProfile) {
        super(level, blockPos, f, gameProfile);
    }

    @Inject(method = "hurt", at = @At("HEAD"))
    private void $hurt(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        CommonSetup.onHoglinAttack((LocalPlayer)(Object)this, source);
    }
}