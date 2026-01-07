package com.github.teamfusion.greedandbleed.client.models;

import com.github.teamfusion.greedandbleed.client.animation.PigmyAnimations;
import com.github.teamfusion.greedandbleed.common.entity.piglin.pigmy.Pigmy;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.geom.ModelPart;

public class PigmyModel<T extends Pigmy> extends AbstractPigmyModel<T> implements ArmedModel {
    public PigmyModel(ModelPart root) {
        super(root);
    }
    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        this.animate(entity.SLINGSHOT_ANIMATION, PigmyAnimations.slingshot, ageInTicks);
    }
}