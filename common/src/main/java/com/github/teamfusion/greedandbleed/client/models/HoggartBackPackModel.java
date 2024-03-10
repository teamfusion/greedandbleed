package com.github.teamfusion.greedandbleed.client.models;

import com.github.teamfusion.greedandbleed.common.entity.piglin.Hoggart;
import net.minecraft.client.model.geom.ModelPart;

public class HoggartBackPackModel<T extends Hoggart> extends HoggartModel<T> {
    public HoggartBackPackModel(ModelPart root) {
        super(root);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        this.piggy_backpack.visible = true;
        this.body.skipDraw = true;
        this.left_arm.visible = false;
        this.right_arm.visible = false;
        this.left_leg.visible = false;
        this.right_leg.visible = false;
        this.head.visible = false;
    }
}
