package com.github.teamfusion.greedandbleed.client.models;

import com.github.teamfusion.greedandbleed.client.animation.HogletAnimation;
import com.github.teamfusion.greedandbleed.common.entity.piglin.Hoglet;
import net.minecraft.client.model.geom.ModelPart;

public class HogletModel<T extends Hoglet> extends AbstractHogletModel<T> {
    private final ModelPart root;
    public HogletModel(ModelPart root) {
        super(root);
        this.root = root;
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        if (!this.riding && entity.isInSittingPose()) {
            this.applyStatic(HogletAnimation.SIT);
        }
        this.animate(entity.diggingAnimationState, HogletAnimation.DIGGING, ageInTicks);
        this.animate(entity.angryAnimationState, HogletAnimation.ANGRY, ageInTicks);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }
}