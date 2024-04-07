package com.github.teamfusion.greedandbleed.mixin.client;

import com.github.teamfusion.greedandbleed.client.sounds.WarpedPiglinSoundInstance;
import com.github.teamfusion.greedandbleed.common.entity.piglin.WarpedPiglin;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin {

    @Shadow
    @Final
    private Minecraft minecraft;

    @Inject(method = "postAddEntitySoundInstance", at = @At("TAIL"))
    private void postAddEntitySoundInstance(Entity entity, CallbackInfo ci) {
        if (entity instanceof WarpedPiglin) {
            WarpedPiglinSoundInstance beeSoundInstance = new WarpedPiglinSoundInstance((WarpedPiglin) entity, SoundEvents.BAT_LOOP, SoundSource.HOSTILE);
            this.minecraft.getSoundManager().queueTickingSound(beeSoundInstance);
        }
    }

}
