package com.infernalstudios.greedandbleed.client.models;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.MobEntity;

public class PigmyArmorModel<T extends MobEntity> extends BipedModel<T> {

    public PigmyArmorModel(float modelSize) {
        this(modelSize, 64, 32);
    }

    public PigmyArmorModel(float modelSize, int texWidthIn, int textHeightIn) {
        super(modelSize);
        this.texWidth = texWidthIn;
        this.texHeight = textHeightIn;

        float yOffset = 0.0F;

        // custom body
        this.body = new ModelRenderer(this, 16, 16);
        this.body.addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, modelSize, modelSize, modelSize + 0.5F);
        this.body.setPos(0.0F, 0.0F + yOffset, 0.0F);

        float armZOffset = 0.0F;

        // custom arms
        this.rightArm = new ModelRenderer(this, 40, 16);
        this.rightArm.addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, modelSize, modelSize, modelSize + 0.5F);
        this.rightArm.setPos(-5.0F, 2.0F + yOffset, 0.0F + armZOffset);

        this.leftArm = new ModelRenderer(this, 40, 16);
        this.leftArm.mirror = true;
        this.leftArm.addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, modelSize, modelSize, modelSize + 0.5F);
        this.leftArm.setPos(5.0F, 2.0F + yOffset, 0.0F + armZOffset);

        float legYOffset = 0.0F;

        // custom legs
        this.rightLeg = new ModelRenderer(this, 0, 16);
        this.rightLeg.addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, modelSize);
        this.rightLeg.setPos(-1.9F, 12.0F + yOffset + legYOffset, 0.0F);

        this.leftLeg = new ModelRenderer(this, 0, 16);
        this.leftLeg.mirror = true;
        this.leftLeg.addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, modelSize);
        this.leftLeg.setPos(1.9F, 12.0F + yOffset + legYOffset, 0.0F);

    }
}
