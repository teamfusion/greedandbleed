package com.github.teamfusion.greedandbleed.client.models;

import com.github.teamfusion.greedandbleed.common.entity.piglin.pygmy.Pygmy;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.geom.ModelPart;

public class PygmyModel<T extends Pygmy> extends AbstractPygmyModel<T> implements ArmedModel {
    public PygmyModel(ModelPart root) {
        super(root);
    }
    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
    }
}