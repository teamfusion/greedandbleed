package com.github.teamfusion.greedandbleed.client.models;

import com.github.teamfusion.greedandbleed.client.animation.HoggartAnimations;
import com.github.teamfusion.greedandbleed.client.animation.HumanoidAnimations;
import com.github.teamfusion.greedandbleed.common.entity.piglin.pygmy.Hoggart;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.HumanoidArm;

public class HoggartModel<T extends Hoggart> extends HierarchicalModel<T> implements ArmedModel {
    public final ModelPart Entity;
    public final ModelPart hoggart;

    public final ModelPart body;
    public final ModelPart head;
    public final ModelPart right_arm;
    public final ModelPart left_arm;
    public final ModelPart right_leg;
    public final ModelPart left_leg;
    public final ModelPart piggy_backpack;

    public HoggartModel(ModelPart root) {
        this.Entity = root.getChild("Entities");
        this.hoggart = this.Entity.getChild("Hoggart");

        this.body = this.hoggart.getChild("body");
        this.head = this.body.getChild("head");
        this.right_arm = this.body.getChild("right_arm");
        this.left_arm = this.body.getChild("left_arm");
        this.right_leg = this.hoggart.getChild("right_leg");
        this.left_leg = this.hoggart.getChild("left_leg");
        this.piggy_backpack = this.body.getChild("piggyback_backpack");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition Entities = partdefinition.addOrReplaceChild("Entities", CubeListBuilder.create(), PartPose.offset(-32.0F, 24.0F, 0.5F));

        PartDefinition Hoggart = Entities.addOrReplaceChild("Hoggart", CubeListBuilder.create(), PartPose.offset(32.0F, -2.0F, 0.0F));

        PartDefinition body = Hoggart.addOrReplaceChild("body", CubeListBuilder.create().texOffs(23, 23).addBox(-5.0F, -12.0F, -3.5F, 10.0F, 12.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -11.0F, 0.0F));

        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(30, 0).addBox(-5.0F, -8.0F, -4.0F, 10.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(0.0F, -15.0F, -7.0F, 0.0F, 15.0F, 15.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-5.0F, -4.0F, -7.0F, 10.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -12.0F, 0.0F));

        PartDefinition left_tusk = head.addOrReplaceChild("left_tusk", CubeListBuilder.create().texOffs(57, 27).addBox(0.0F, -10.0F, -1.0F, 2.0F, 11.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, -1.0F, -5.0F));

        PartDefinition right_tusk = head.addOrReplaceChild("right_tusk", CubeListBuilder.create().texOffs(50, 16).addBox(-2.0F, -10.0F, -1.0F, 2.0F, 11.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, -1.0F, -5.0F));

        PartDefinition left_ear = head.addOrReplaceChild("left_ear", CubeListBuilder.create().texOffs(36, 59).addBox(0.0F, -0.5F, -2.0F, 1.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.0F, -5.5F, 0.0F, 0.0F, 0.0F, -0.5236F));

        PartDefinition right_ear = head.addOrReplaceChild("right_ear", CubeListBuilder.create().texOffs(58, 16).addBox(-1.0F, -0.5F, -2.0F, 1.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.0F, -5.5F, 0.0F, 0.0F, 0.0F, 0.5236F));

        PartDefinition left_arm = body.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(55, 54).addBox(0.0F, -1.0F, -2.5F, 5.0F, 12.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(0, 49).addBox(0.0F, -1.0F, -2.5F, 5.0F, 12.0F, 5.0F, new CubeDeformation(0.25F)), PartPose.offset(5.0F, -11.0F, 1.0F));

        PartDefinition right_arm = body.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(40, 42).addBox(-5.0F, -1.0F, -2.5F, 5.0F, 12.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, -11.0F, 1.0F));

        PartDefinition piggyback_backpack = body.addOrReplaceChild("piggyback_backpack", CubeListBuilder.create(), PartPose.offset(4.0F, -8.5F, 6.0F));

        PartDefinition backpack = piggyback_backpack.addOrReplaceChild("backpack", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -4.0F, 0.0F, 12.0F, 12.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, -1.0F, -2.5F));

        PartDefinition chain_detail_left2 = backpack.addOrReplaceChild("chain_detail_left2", CubeListBuilder.create().texOffs(51, 0).mirror().addBox(-0.5F, 0.5F, -4.5F, 1.0F, 12.0F, 9.0F, new CubeDeformation(0.25F)).mirror(false), PartPose.offsetAndRotation(5.501F, -4.5F, 4.5F, 0.0F, 0.0F, -0.0873F));

        PartDefinition chain_detail_right2 = backpack.addOrReplaceChild("chain_detail_right2", CubeListBuilder.create().texOffs(51, 0).addBox(-0.5F, 0.5F, -4.5F, 1.0F, 12.0F, 9.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(-5.501F, -4.5F, 4.5F, 0.0F, 0.0F, 0.0873F));

        PartDefinition right_leg = Hoggart.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(20, 42).addBox(-2.5F, 0.0F, -2.5F, 5.0F, 14.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(2.5F, -12.0F, 1.0F));

        PartDefinition left_leg = Hoggart.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 30).addBox(-2.5F, 0.0F, -2.5F, 5.0F, 14.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.5F, -12.0F, 1.0F));

        return LayerDefinition.create(meshdefinition, 96, 96);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.head.yRot = netHeadYaw * ((float) Math.PI / 180.0F);
        this.head.xRot = headPitch * ((float) Math.PI / 180.0F);

        if (entity.isPassenger()) {
            this.applyStatic(HumanoidAnimations.SIT);
        } else {
            this.animateWalk(HumanoidAnimations.WALK, limbSwing, limbSwingAmount, 2.0F, 2.5F);
        }
        if (entity.isAggressive()) {
            if (entity.isLeftHanded()) {
                this.applyStatic(HumanoidAnimations.ATTACK_LEFT);
            } else {
                this.applyStatic(HumanoidAnimations.ATTACK_RIGHT);
            }
        } else if (entity.walkAnimation.isMoving()) {
            this.animateWalk(HumanoidAnimations.WALK_SWING, limbSwing, limbSwingAmount, 2.0F, 2.5F);
        } else {
            this.animateWalk(HumanoidAnimations.IDLE, ageInTicks, 1.0F, 1.0F, 1.0F);
        }
        if (entity.isBaby()) {
            this.applyStatic(HoggartAnimations.BABY);
        }

        this.piggy_backpack.visible = false;
    }

    @Override
    public ModelPart root() {
        return Entity;
    }

    @Override
    public void translateToHand(HumanoidArm humanoidArm, PoseStack poseStack) {
        this.Entity.translateAndRotate(poseStack);
        this.hoggart.translateAndRotate(poseStack);
        this.body.translateAndRotate(poseStack);
        this.getArm(humanoidArm).translateAndRotate(poseStack);
    }

    protected ModelPart getArm(HumanoidArm humanoidArm) {
        return humanoidArm == HumanoidArm.LEFT ? this.left_arm : this.right_arm;
    }
}