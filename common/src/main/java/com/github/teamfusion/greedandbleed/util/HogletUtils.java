package com.github.teamfusion.greedandbleed.util;

import com.github.teamfusion.greedandbleed.common.entity.piglin.Hoglet;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;

public class HogletUtils {
    public static void angerAndSteal(Hoglet hoglet, LivingEntity therf) {
        if (hoglet.getStealTarget() == null && !hoglet.isTame()) {
            hoglet.playSound(SoundEvents.HOGLIN_ANGRY, 1.0F, hoglet.getVoicePitch());
            hoglet.setStealTarget(therf);
        }
    }
}
