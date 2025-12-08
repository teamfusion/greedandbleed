package com.github.teamfusion.greedandbleed.client.models;

import com.github.teamfusion.greedandbleed.common.entity.piglin.pigmy.ZombifiedPigmies;
import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.geom.ModelPart;

public class ZombifiedPigmyModel<T extends ZombifiedPigmies> extends AbstractPigmyModel<T> implements ArmedModel {
    public ZombifiedPigmyModel(ModelPart root) {
        super(root);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        AnimationUtils.animateZombieArms(this.left_arm, this.right_arm, entity.isAggressive(), this.attackTime, ageInTicks);

    }
}