package com.github.teamfusion.greedandbleed.mixin.client;

import com.github.teamfusion.greedandbleed.common.entity.ICrawlSpawn;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(MobRenderer.class)
public abstract class MobRendererMixin<T extends Mob, M extends EntityModel<T>>
        extends LivingEntityRenderer<T, M> {
    public MobRendererMixin(EntityRendererProvider.Context context, M entityModel, float f) {
        super(context, entityModel, f);
    }

    @Override
    protected void setupRotations(T livingEntity, PoseStack poseStack, float f, float g, float h) {
        super.setupRotations(livingEntity, poseStack, f, g, h);

        if (livingEntity instanceof ICrawlSpawn crawlSpawn) {
            if (livingEntity.getPose() == Pose.EMERGING) {
                poseStack.translate(0, -crawlSpawn.getSpawnScaleAnimationScale(h) * 4.0F, 0);
            }
        }
    }
}