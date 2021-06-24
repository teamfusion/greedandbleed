package com.infernalstudios.greedandbleed.client.models;

import com.infernalstudios.greedandbleed.common.entity.piglin.SkeletalPiglinEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.MathHelper;

public class SkeletalPiglinModel<T extends SkeletalPiglinEntity> extends PlayerModel<T> {

    private final ModelRenderer Body;
    private final ModelRenderer Head;
    private final ModelRenderer LeftEar;
    private final ModelRenderer RightEar;
    private final ModelRenderer RightArm;
    private final ModelRenderer LeftArm;

    public SkeletalPiglinModel() {
        super(0.0f, false);
        texWidth = 64;
        texHeight = 64;

        this.body = new ModelRenderer(this, 16, 16);
        this.body.addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 0.0F, false);
        this.head = new ModelRenderer(this);
        this.head.texOffs(0, 0).addBox(-5.0F, -8.0F, -4.0F, 10.0F, 8.0F, 8.0F, 0.0F, false);
        this.head.texOffs(31, 1).addBox(-2.0F, -4.0F, -5.0F, 4.0F, 4.0F, 1.0F, 0.0F, false);
        this.head.texOffs(2, 0).addBox(-3.0F, -2.0F, -5.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.head.texOffs(2, 4).addBox(2.0F, -2.0F, -5.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.LeftEar = new ModelRenderer(this);
        this.LeftEar.setPos(4.5F, -6.0F, 0.0F);
        this.LeftEar.texOffs(51, 6).addBox(0.0F, 0.0F, -2.0F, 1.0F, 5.0F, 4.0F, 0.0F, false);
        this.LeftEar.texOffs(54, 10).addBox(0.0F, 0.0F, 1.0F, 1.0F, 5.0F, 0.0F, 0.0F, false);
        this.LeftEar.texOffs(54, 10).addBox(0.0F, 0.0F, -1.0F, 1.0F, 5.0F, 0.0F, 0.0F, false);
        this.LeftEar.texOffs(54, 10).addBox(0.0F, 4.0F, -1.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);
        this.head.addChild(this.LeftEar);
        this.RightEar = new ModelRenderer(this);
        this.RightEar.setPos(-4.5F, -6.0F, 0.0F);
        this.RightEar.texOffs(51, 6).addBox(-1.0F, 0.0F, -2.0F, 1.0F, 5.0F, 4.0F, 0.0F, true);
        this.RightEar.texOffs(54, 10).addBox(-1.0F, 0.0F, 1.0F, 1.0F, 5.0F, 0.0F, 0.0F, true);
        this.RightEar.texOffs(54, 10).addBox(-1.0F, 0.0F, -1.0F, 1.0F, 5.0F, 0.0F, 0.0F, true);
        this.RightEar.texOffs(54, 10).addBox(-1.0F, 4.0F, -1.0F, 1.0F, 1.0F, 2.0F, 0.0F, true);
        this.head.addChild(this.RightEar);

        this.leftArm = new ModelRenderer(this, 40, 16);
        this.leftArm.addBox(-1.0F, -2.0F, -1.0F, 3.0F, 12.0F, 3.0F, 0.0f, true);
        this.leftArm.setPos(5.0F, 2.0F, 0.0F);

        this.rightArm = new ModelRenderer(this, 40, 16);
        this.rightArm.addBox(-2.0F, -2.0F, -1.0F, 3.0F, 12.0F, 3.0F, 0.0f, false);
        this.rightArm.setPos(-5.0F, 2.0F, 0.0F);

        this.leftLeg = new ModelRenderer(this, 50, 15);
        this.leftLeg.addBox(-0.9F, 0.001F, -1.0F, 3.0F, 12.0F, 3.0F, 0.0F, true);
        this.leftLeg.setPos(1.9F, 12.0F, -0.5F);

        this.rightLeg = new ModelRenderer(this, 50, 15);
        this.rightLeg.addBox(-2.1F, 0.001F, -1.0F, 3.0F, 12.0F, 3.0F, 0.0F, false);
        this.rightLeg.setPos(-1.9F, 12.0F, -0.5F);

        this.hat = new ModelRenderer(this);

        this.Body = this.body.createShallowCopy();
        this.Head = this.head.createShallowCopy();
        this.LeftArm = this.leftArm.createShallowCopy();
        this.RightArm = this.leftArm.createShallowCopy();
    }

    @Override
    public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.body.copyFrom(this.Body);
        this.head.copyFrom(this.Head);
        this.leftArm.copyFrom(this.LeftArm);
        this.rightArm.copyFrom(this.RightArm);
        super.setupAnim(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        float f1 = ageInTicks * 0.1F + limbSwing * 0.5F;
        float f2 = 0.08F + limbSwingAmount * 0.4F;
        this.LeftEar.zRot = (-(float)Math.PI / 6F) - MathHelper.cos(f1 * 1.2F) * f2;
        this.RightEar.zRot = ((float)Math.PI / 6F) + MathHelper.cos(f1) * f2;

        this.leftPants.copyFrom(this.leftLeg);
        this.rightPants.copyFrom(this.rightLeg);
        this.leftSleeve.copyFrom(this.leftArm);
        this.rightSleeve.copyFrom(this.rightArm);
        this.jacket.copyFrom(this.body);
        this.hat.copyFrom(this.head);
    }

    @Override
    public void translateToHand(HandSide sideIn, MatrixStack matrixStackIn) {
        float f = sideIn == HandSide.RIGHT ? 1.5F : -1.5F;
        ModelRenderer modelrenderer = this.getArm(sideIn);
        modelrenderer.x += f;
        modelrenderer.translateAndRotate(matrixStackIn);
        modelrenderer.x -= f;
    }
}