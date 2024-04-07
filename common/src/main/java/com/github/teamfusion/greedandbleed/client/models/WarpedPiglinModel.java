package com.github.teamfusion.greedandbleed.client.models;// Made with Blockbench 4.9.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports

import com.github.teamfusion.greedandbleed.client.animation.HumanoidAnimations;
import com.github.teamfusion.greedandbleed.client.animation.WarpedPiglinAnimation;
import com.github.teamfusion.greedandbleed.common.entity.piglin.WarpedPiglin;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.HumanoidArm;

public class WarpedPiglinModel<T extends WarpedPiglin> extends HierarchicalModel<T> implements ArmedModel {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    private final ModelPart Entity;
    private final ModelPart body;

    private final ModelPart right_arm;
    private final ModelPart left_arm;
    private final ModelPart head;

    public WarpedPiglinModel(ModelPart root) {
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

        PartDefinition body = Entity.addOrReplaceChild("body", CubeListBuilder.create().texOffs(60, 22).addBox(-4.0F, -10.0F, -2.0F, 8.0F, 10.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -10.0F, 0.0F));

        PartDefinition warped_wings = body.addOrReplaceChild("warped_wings", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -10.0F, 2.0F, 0.0436F, 0.0F, 0.0F));

        PartDefinition elytras = warped_wings.addOrReplaceChild("elytras", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0873F, 0.0F, 0.0F));

        PartDefinition right_elytra = elytras.addOrReplaceChild("right_elytra", CubeListBuilder.create().texOffs(72, 75).mirror().addBox(-7.5F, -0.5F, -2.0F, 8.0F, 17.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(56, 41).mirror().addBox(-7.5F, -0.5F, 0.0F, 8.0F, 17.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.5F, 0.0F, 0.0F, 0.0F, 0.2618F));

        PartDefinition left_elytra = elytras.addOrReplaceChild("left_elytra", CubeListBuilder.create().texOffs(72, 75).addBox(-0.5F, -0.5F, -2.0F, 8.0F, 17.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(56, 41).addBox(-0.5F, -0.5F, 0.0F, 8.0F, 17.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.5F, 0.0F, 0.0F, 0.0F, -0.2618F));

        PartDefinition right_wing = warped_wings.addOrReplaceChild("right_wing", CubeListBuilder.create().texOffs(72, 41).mirror().addBox(-7.5F, -0.5F, 0.0F, 8.0F, 23.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.5F, 0.0F, 0.0F, 0.0F, 0.0873F));

        PartDefinition left_wing = warped_wings.addOrReplaceChild("left_wing", CubeListBuilder.create().texOffs(72, 41).addBox(-0.5F, -0.5F, 0.0F, 8.0F, 23.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.5F, 0.0F, 0.0F, 0.0F, -0.0873F));

        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 22).addBox(-5.0F, -4.0F, -4.0F, 10.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(16, 75).addBox(-2.0F, 0.0F, -5.0F, 4.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(26, 66).mirror().addBox(2.0F, 2.0F, -5.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(26, 66).addBox(-3.0F, 2.0F, -5.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(44, 0).addBox(-5.0F, -4.0F, -4.0F, 10.0F, 8.0F, 8.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, -14.0F, 0.0F));

        PartDefinition right_ear = head.addOrReplaceChild("right_ear", CubeListBuilder.create().texOffs(16, 66).addBox(-0.5F, -0.5F, -2.0F, 1.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.5F, -1.5F, 0.0F));

        PartDefinition left_ear = head.addOrReplaceChild("left_ear", CubeListBuilder.create().texOffs(16, 66).mirror().addBox(-0.5F, -0.5F, -2.0F, 1.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(5.5F, -1.5F, 0.0F));

        PartDefinition warped_roots = head.addOrReplaceChild("warped_roots", CubeListBuilder.create().texOffs(0, 38).addBox(-6.0F, -4.5F, 0.0F, 12.0F, 9.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.5F, 0.0F, 0.0F, 0.7854F, 0.0F));

        PartDefinition warped_roots2 = warped_roots.addOrReplaceChild("warped_roots2", CubeListBuilder.create().texOffs(0, 38).addBox(-6.0F, -4.5F, 0.0F, 12.0F, 9.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

        PartDefinition left_arm = body.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(0, 66).addBox(0.0F, -1.0F, -2.0F, 4.0F, 10.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, -9.0F, 0.0F));

        PartDefinition right_arm = body.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(32, 52).addBox(-4.0F, -1.0F, -2.0F, 4.0F, 10.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, -9.0F, 0.0F));

        PartDefinition left_leg = Entity.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 52).addBox(2.0F, 2.0F, -2.0F, 4.0F, 10.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, -12.0F, 0.0F, 0.0F, 0.0F, 0.0F));

        PartDefinition right_leg = Entity.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(40, 38).addBox(-6.0F, 2.0F, -2.0F, 4.0F, 10.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, -12.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 96, 96);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.head.yRot = netHeadYaw * ((float) Math.PI / 180.0F);
        this.head.xRot = headPitch * ((float) Math.PI / 180.0F);
        float f = entity.getGroundScale(ageInTicks - entity.tickCount);

        if (entity.isPassenger()) {
            this.applyStatic(HumanoidAnimations.SIT);
        }
        if (entity.walkAnimation.isMoving()) {
            this.animateWalk(HumanoidAnimations.WALK_SWING, limbSwing, limbSwingAmount, 2.0F, 2.5F);
        } else {
            this.animateWalk(HumanoidAnimations.IDLE, ageInTicks, 1.0F, 1.0F, 1.0F);
        }
        this.animateWalk(WarpedPiglinAnimation.flying, ageInTicks, (1 - f), 1.0F, 1.0F);

        this.animateWalk(HumanoidAnimations.WALK, limbSwing, limbSwingAmount * (f), 2.0F, 2.5F);


        if (entity.isBaby()) {
            this.applyStatic(WarpedPiglinAnimation.baby);
        }
        this.animateWalk(HumanoidAnimations.EAR_MOVING, limbSwing, limbSwingAmount, 1.0F, 1.5F);

        this.animateWalk(HumanoidAnimations.EAR_IDLE, ageInTicks, 1.0F, 1.0F, 1.0F);
    }

    @Override
    public ModelPart root() {
        return Entity;
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
}