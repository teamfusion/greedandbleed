package com.github.teamfusion.greedandbleed.mixin.client;

import com.github.teamfusion.greedandbleed.common.entity.ICrawlSpawn;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(net.minecraft.client.renderer.entity.LivingEntityRenderer.class)
public abstract class LivingEntityRenderer<T extends LivingEntity, M extends EntityModel<T>> {

    @Inject(method = "setupRotations", at = @At("TAIL"))
    protected void setupRotations(T livingEntity, PoseStack poseStack, float f, float g, float h, CallbackInfo ci) {
        if (livingEntity instanceof ICrawlSpawn crawlSpawn) {
            if (livingEntity.getPose() == Pose.EMERGING) {
                poseStack.translate(0, -crawlSpawn.getSpawnScaleAnimationScale(h) * 4.0F, 0);
            }
        }
    }
}