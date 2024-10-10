package com.github.teamfusion.greedandbleed.client.models;

import com.github.teamfusion.greedandbleed.common.entity.piglin.pygmy.ZombifiedPygmy;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.geom.ModelPart;

public class ZombifiedPygmyModel<T extends ZombifiedPygmy> extends AbstractPygmyModel<T> implements ArmedModel {
    public ZombifiedPygmyModel(ModelPart root) {
        super(root);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
    }
}