package com.github.teamfusion.greedandbleed.client.sounds;

import com.github.teamfusion.greedandbleed.common.entity.piglin.WarpedPiglin;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;

public class WarpedPiglinSoundInstance extends AbstractTickableSoundInstance {
    private static final float VOLUME_MIN = 0.0f;
    private static final float VOLUME_MAX = 1.2f;
    private static final float PITCH_MIN = 0.0f;
    protected final WarpedPiglin piglin;
    private boolean hasSwitched;

    public WarpedPiglinSoundInstance(WarpedPiglin piglin, SoundEvent soundEvent, SoundSource soundSource) {
        super(soundEvent, soundSource, SoundInstance.createUnseededRandom());
        this.piglin = piglin;
        this.x = (float) piglin.getX();
        this.y = (float) piglin.getY();
        this.z = (float) piglin.getZ();
        this.looping = true;
        this.delay = 0;
        this.volume = 0.0f;
    }

    @Override
    public void tick() {
        if (this.piglin.isRemoved() || this.hasSwitched) {
            this.stop();
            return;
        }
        this.x = (float) this.piglin.getX();
        this.y = (float) this.piglin.getY();
        this.z = (float) this.piglin.getZ();
        float f = (float) this.piglin.getDeltaMovement().horizontalDistance();
        if (f >= 0.01f) {
            this.pitch = Mth.lerp(Mth.clamp(f, this.getMinPitch(), this.getMaxPitch()), this.getMinPitch(), this.getMaxPitch());
            this.volume = Mth.lerp(Mth.clamp(f, 0.0f, 0.5f), 0.0f, 1.2f);
        } else {
            this.pitch = 0.0f;
            this.volume = 0.0f;
        }
    }

    private float getMinPitch() {
        if (this.piglin.isBaby()) {
            return 1.1f;
        }
        return 0.7f;
    }

    private float getMaxPitch() {
        if (this.piglin.isBaby()) {
            return 1.5f;
        }
        return 1.1f;
    }

    @Override
    public boolean canStartSilent() {
        return true;
    }

    @Override
    public boolean canPlaySound() {
        return !this.piglin.isSilent() && this.piglin.isNoGravity();
    }
}

