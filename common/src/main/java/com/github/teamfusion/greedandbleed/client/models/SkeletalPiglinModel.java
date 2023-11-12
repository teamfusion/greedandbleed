package com.github.teamfusion.greedandbleed.client.models;

import com.github.teamfusion.greedandbleed.common.entity.piglin.SkeletalPiglin;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PiglinModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class SkeletalPiglinModel<T extends SkeletalPiglin> extends PiglinModel<T> {
    public final ModelPart rightEar = this.head.getChild("right_ear");
    private final ModelPart leftEar = this.head.getChild("left_ear");
    private final ModelPart root;

    public SkeletalPiglinModel(ModelPart root) {
        super(root);
        this.root = root;
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = PlayerModel.createMesh(CubeDeformation.NONE, false);
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild(
                "body",
                CubeListBuilder.create().texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F),
                PartPose.ZERO
        );

        PartDefinition head = root.addOrReplaceChild(
                "head",
                CubeListBuilder.create()
                        .texOffs(0, 0).addBox(-5.0F, -8.0F, -4.0F, 10.0F, 8.0F, 8.0F)
                        .texOffs(31, 1).addBox(-2.0F, -4.0F, -5.0F, 4.0F, 4.0F, 1.0F)
                        .texOffs(2, 0).addBox(-3.0F, -2.0F, -5.0F, 1.0F, 2.0F, 1.0F)
                        .texOffs(2, 4).addBox(2.0F, -2.0F, -5.0F, 1.0F, 2.0F, 1.0F),
                PartPose.ZERO
        );
        head.addOrReplaceChild(
                "left_ear",
                CubeListBuilder.create().texOffs(51, 6).addBox(0.0F, 0.0F, -2.0F, 1.0F, 5.0F, 4.0F),
                PartPose.offsetAndRotation(4.5F, -6.0F, 0.0F, 0.0F, 0.0F, (float) (-Math.PI / 6))
        );
        head.addOrReplaceChild(
                "right_ear",
                CubeListBuilder.create().texOffs(39, 6).addBox(-1.0F, 0.0F, -2.0F, 1.0F, 5.0F, 4.0F),
                PartPose.offsetAndRotation(-4.5F, -6.0F, 0.0F, 0.0F, 0.0F, (float) (Math.PI / 6))
        );

        root.addOrReplaceChild(
                "right_arm",
                CubeListBuilder.create().texOffs(40, 16).addBox(-2.0F, -2.0F, -1.0F, 3.0F, 12.0F, 3.0F),
                PartPose.offset(-5.0F, 2.0F, 0.0F)
        );
        root.addOrReplaceChild(
                "left_arm",
                CubeListBuilder.create().texOffs(40, 16).mirror().addBox(-1.0F, -2.0F, -1.0F, 3.0F, 12.0F, 3.0F),
                PartPose.offset(5.0F, 2.0F, 0.0F)
        );
        root.addOrReplaceChild(
                "right_leg",
                CubeListBuilder.create().texOffs(50, 15).addBox(-2.1F, 0.001F, -1.0F, 3.0F, 12.0F, 3.0F),
                PartPose.offset(-1.9F, 12.0F, -0.5F)
        );
        root.addOrReplaceChild(
                "left_leg",
                CubeListBuilder.create().texOffs(50, 15).mirror().addBox(-0.9F, 0.001F, -1.0F, 3.0F, 12.0F, 3.0F),
                PartPose.offset(1.9F, 12.0F, -0.5F)
        );

        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public void prepareMobModel(T mob, float f, float g, float h) {
        this.rightArmPose = HumanoidModel.ArmPose.EMPTY;
        this.leftArmPose = HumanoidModel.ArmPose.EMPTY;
        ItemStack itemStack = ((LivingEntity) mob).getItemInHand(InteractionHand.MAIN_HAND);
        if (itemStack.is(Items.BOW) && ((Mob) mob).isAggressive()) {
            if (((Mob) mob).getMainArm() == HumanoidArm.RIGHT) {
                this.rightArmPose = HumanoidModel.ArmPose.BOW_AND_ARROW;
            } else {
                this.leftArmPose = HumanoidModel.ArmPose.BOW_AND_ARROW;
            }
        }

        if (itemStack.is(Items.CROSSBOW)) {
            if (((Mob) mob).getMainArm() == HumanoidArm.RIGHT) {
                this.rightArmPose = ArmPose.CROSSBOW_HOLD;
            } else {
                this.leftArmPose = ArmPose.CROSSBOW_HOLD;
            }
        }
        if (mob.isChargingCrossbow()) {
            if (((Mob) mob).getMainArm() == HumanoidArm.RIGHT) {
                this.rightArmPose = ArmPose.CROSSBOW_CHARGE;
            } else {
                this.leftArmPose = ArmPose.CROSSBOW_CHARGE;
            }
        }
        super.prepareMobModel(mob, f, g, h);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        float f1 = ageInTicks * 0.1F + limbSwing * 0.5F;
        float f2 = 0.08F + limbSwingAmount * 0.4F;
        this.leftEar.zRot = (-(float)Math.PI / 6F) - Mth.cos(f1 * 1.2F) * f2;
        this.rightEar.zRot = ((float)Math.PI / 6F) + Mth.cos(f1) * f2;

        poseRightArm(entity);
        poseLeftArm(entity);

        this.leftPants.copyFrom(this.leftLeg);
        this.rightPants.copyFrom(this.rightLeg);
        this.leftSleeve.copyFrom(this.leftArm);
        this.rightSleeve.copyFrom(this.rightArm);
        this.jacket.copyFrom(this.body);
        this.hat.copyFrom(this.head);

        this.hat.visible = false;
    }

    private void poseRightArm(T livingEntity) {
        switch (this.rightArmPose) {
            case EMPTY: {
                this.rightArm.yRot = 0.0f;
                break;
            }
            case BLOCK: {
                this.rightArm.xRot = this.rightArm.xRot * 0.5f - 0.9424779f;
                this.rightArm.yRot = -0.5235988f;
                break;
            }
            case ITEM: {
                this.rightArm.xRot = this.rightArm.xRot * 0.5f - 0.31415927f;
                this.rightArm.yRot = 0.0f;
                break;
            }
            case THROW_SPEAR: {
                this.rightArm.xRot = this.rightArm.xRot * 0.5f - (float) Math.PI;
                this.rightArm.yRot = 0.0f;
                break;
            }
            case BOW_AND_ARROW: {
                this.rightArm.yRot = -0.1f + this.head.yRot;
                this.leftArm.yRot = 0.1f + this.head.yRot + 0.4f;
                this.rightArm.xRot = -1.5707964f + this.head.xRot;
                this.leftArm.xRot = -1.5707964f + this.head.xRot;
                break;
            }
            case CROSSBOW_CHARGE: {
                AnimationUtils.animateCrossbowCharge(this.rightArm, this.leftArm, livingEntity, true);
                break;
            }
            case CROSSBOW_HOLD: {
                AnimationUtils.animateCrossbowHold(this.rightArm, this.leftArm, this.head, true);
                break;
            }
            case BRUSH: {
                this.rightArm.xRot = this.rightArm.xRot * 0.5f - 0.62831855f;
                this.rightArm.yRot = 0.0f;
                break;
            }
            case SPYGLASS: {
                this.rightArm.xRot = Mth.clamp(this.head.xRot - 1.9198622f - (((Entity) livingEntity).isCrouching() ? 0.2617994f : 0.0f), -2.4f, 3.3f);
                this.rightArm.yRot = this.head.yRot - 0.2617994f;
                break;
            }
            case TOOT_HORN: {
                this.rightArm.xRot = Mth.clamp(this.head.xRot, -1.2f, 1.2f) - 1.4835298f;
                this.rightArm.yRot = this.head.yRot - 0.5235988f;
            }
        }
    }

    private void poseLeftArm(T livingEntity) {
        switch (this.leftArmPose) {
            case EMPTY: {
                this.leftArm.yRot = 0.0f;
                break;
            }
            case BLOCK: {
                this.leftArm.xRot = this.leftArm.xRot * 0.5f - 0.9424779f;
                this.leftArm.yRot = 0.5235988f;
                break;
            }
            case ITEM: {
                this.leftArm.xRot = this.leftArm.xRot * 0.5f - 0.31415927f;
                this.leftArm.yRot = 0.0f;
                break;
            }
            case THROW_SPEAR: {
                this.leftArm.xRot = this.leftArm.xRot * 0.5f - (float) Math.PI;
                this.leftArm.yRot = 0.0f;
                break;
            }
            case BOW_AND_ARROW: {
                this.rightArm.yRot = -0.1f + this.head.yRot - 0.4f;
                this.leftArm.yRot = 0.1f + this.head.yRot;
                this.rightArm.xRot = -1.5707964f + this.head.xRot;
                this.leftArm.xRot = -1.5707964f + this.head.xRot;
                break;
            }
            case CROSSBOW_CHARGE: {
                AnimationUtils.animateCrossbowCharge(this.rightArm, this.leftArm, livingEntity, false);
                break;
            }
            case CROSSBOW_HOLD: {
                AnimationUtils.animateCrossbowHold(this.rightArm, this.leftArm, this.head, false);
                break;
            }
            case BRUSH: {
                this.leftArm.xRot = this.leftArm.xRot * 0.5f - 0.62831855f;
                this.leftArm.yRot = 0.0f;
                break;
            }
            case SPYGLASS: {
                this.leftArm.xRot = Mth.clamp(this.head.xRot - 1.9198622f - (((Entity) livingEntity).isCrouching() ? 0.2617994f : 0.0f), -2.4f, 3.3f);
                this.leftArm.yRot = this.head.yRot + 0.2617994f;
                break;
            }
            case TOOT_HORN: {
                this.leftArm.xRot = Mth.clamp(this.head.xRot, -1.2f, 1.2f) - 1.4835298f;
                this.leftArm.yRot = this.head.yRot + 0.5235988f;
            }
        }
    }


    @Override
    public void translateToHand(HumanoidArm arm, PoseStack matrices) {
        float offset = arm == HumanoidArm.RIGHT ? 1.0F : -1.0F;
        ModelPart part = this.getArm(arm);
        part.x += offset;
        part.translateAndRotate(matrices);
        part.x -= offset;
    }
}