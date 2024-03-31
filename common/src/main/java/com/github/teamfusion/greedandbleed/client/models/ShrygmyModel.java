package com.github.teamfusion.greedandbleed.client.models;

import com.github.teamfusion.greedandbleed.client.animation.HumanoidAnimations;
import com.github.teamfusion.greedandbleed.common.entity.piglin.pygmy.Shrygmy;
import com.github.teamfusion.greedandbleed.common.registry.ItemRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.HumanoidArm;

public class ShrygmyModel<T extends Shrygmy> extends HierarchicalModel<T> implements ArmedModel {
    private final ModelPart Entity;

    private final ModelPart body;

    private final ModelPart right_arm;
    private final ModelPart left_arm;
    private final ModelPart head;

    public ShrygmyModel(ModelPart root) {
        this.Entity = root.getChild("Entity");

        this.body = this.Entity.getChild("body");

        this.right_arm = this.body.getChild("right_arm");
        this.left_arm = this.body.getChild("left_arm");
        this.head = this.body.getChild("head");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition Entity = partdefinition.addOrReplaceChild("Entity", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition body = Entity.addOrReplaceChild("body", CubeListBuilder.create().texOffs(28, 20).addBox(-4.0F, 0.0F, -3.0F, 8.0F, 10.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(0, 20).addBox(-4.0F, 0.0F, -3.0F, 8.0F, 10.0F, 6.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, -16.0F, 0.0F));

        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(56, 0).addBox(-5.0F, -8.0F, -4.0F, 10.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(28, 61).addBox(-3.0F, -4.0F, -5.0F, 6.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(28, 69).addBox(3.0F, -2.0F, -5.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(28, 66).addBox(-4.0F, -2.0F, -5.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition mushroom_cap = head.addOrReplaceChild("mushroom_cap", CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, -3.0F, -7.0F, 14.0F, 6.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.0F, 0.0F, -0.2618F, 0.0F, 0.0F));

        PartDefinition left_ear = head.addOrReplaceChild("left_ear", CubeListBuilder.create().texOffs(14, 61).addBox(-0.25F, -1.0F, -3.0F, 1.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.25F, -6.0F, 0.0F, 0.0F, 0.0F, -0.4363F));

        PartDefinition right_ear = head.addOrReplaceChild("right_ear", CubeListBuilder.create().texOffs(0, 61).addBox(-0.75F, -1.0F, -3.0F, 1.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.25F, -6.0F, 0.0F, 0.0F, 0.0F, 0.4363F));

        PartDefinition left_arm = body.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(32, 36).addBox(-1.0F, -2.0F, -2.0F, 3.0F, 10.0F, 5.0F, new CubeDeformation(0.25F))
                .texOffs(16, 36).addBox(-1.0F, -2.0F, -2.0F, 3.0F, 10.0F, 5.0F, new CubeDeformation(0.5F)), PartPose.offset(5.0F, 2.0F, 0.0F));

        PartDefinition right_arm2 = body.addOrReplaceChild("right_arm2", CubeListBuilder.create().texOffs(0, 36).addBox(-2.0F, -2.0F, -2.0F, 3.0F, 10.0F, 5.0F, new CubeDeformation(0.25F))
                .texOffs(56, 20).addBox(-2.0F, -2.0F, -2.0F, 3.0F, 10.0F, 5.0F, new CubeDeformation(0.5F)), PartPose.offset(-5.0F, 2.0F, 0.0F));

        PartDefinition right_leg = body.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(48, 51).addBox(2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.25F))
                .texOffs(32, 51).addBox(2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.5F)), PartPose.offset(-2.0F, 10.0F, 0.0F));

        PartDefinition left_leg = body.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(16, 51).addBox(-6.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.25F))
                .texOffs(0, 51).addBox(-6.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.5F)), PartPose.offset(2.0F, 10.0F, 0.0F));

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
            if (entity.isHolding(ItemRegistry.SLINGSHOT.get())) {
                this.applyStatic(HumanoidAnimations.RANGE_ATTACK);
            } else {
                if (entity.isLeftHanded()) {
                    this.applyStatic(HumanoidAnimations.ATTACK_LEFT);
                } else {
                    this.applyStatic(HumanoidAnimations.ATTACK_RIGHT);
                }
            }
        } else if (entity.walkAnimation.isMoving()) {
            this.animateWalk(HumanoidAnimations.WALK_SWING, limbSwing, limbSwingAmount, 2.0F, 2.5F);
        } else {
            this.animateWalk(HumanoidAnimations.IDLE, ageInTicks, 1.0F, 1.0F, 1.0F);
        }

        if (entity.isBaby()) {
            this.applyStatic(HumanoidAnimations.BABY);
        }
        this.animateWalk(HumanoidAnimations.EAR_MOVING, limbSwing, limbSwingAmount, 1.0F, 1.5F);

        this.animateWalk(HumanoidAnimations.EAR_IDLE, ageInTicks, 1.0F, 1.0F, 1.0F);
    }

    @Override
    public void translateToHand(HumanoidArm humanoidArm, PoseStack poseStack) {
        this.Entity.translateAndRotate(poseStack);
        this.body.translateAndRotate(poseStack);
        this.getArm(humanoidArm).translateAndRotate(poseStack);
    }

    protected ModelPart getArm(HumanoidArm humanoidArm) {
        return humanoidArm == HumanoidArm.LEFT ? this.left_arm : this.right_arm;
    }

    @Override
    public ModelPart root() {
        return Entity;
    }
}