package com.github.teamfusion.greedandbleed.client.models;// Made with Blockbench 4.8.3
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.github.teamfusion.greedandbleed.client.animation.HumanoidAnimations;
import com.github.teamfusion.greedandbleed.client.animation.ShamanAnimations;
import com.github.teamfusion.greedandbleed.common.entity.piglin.ShamanPiglin;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class ShamanPiglinModel<T extends ShamanPiglin> extends HierarchicalModel<T> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    private final ModelPart Entity;
    private final ModelPart body;
    private final ModelPart head;

    public ShamanPiglinModel(ModelPart root) {
        this.Entity = root.getChild("Entity");
        this.body = this.Entity.getChild("body");
        this.head = this.body.getChild("head");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition Entity = partdefinition.addOrReplaceChild("Entity", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition body = Entity.addOrReplaceChild("body", CubeListBuilder.create().texOffs(24, 25).addBox(-4.0F, -11.5F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 25).addBox(-4.0F, -11.5F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.24F)), PartPose.offset(0.0F, -12.5F, 0.0F));

        PartDefinition right_arm = body.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(32, 41).addBox(-4.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, -11.5F, 0.0F));

        PartDefinition left_arm = body.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(16, 41).addBox(0.0F, -0.1667F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(16, 57).addBox(0.0F, -0.1667F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.25F))
                .texOffs(16, 65).addBox(0.0F, 5.8333F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(4.0F, -11.3333F, 0.0F));

        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(64, 0).addBox(-5.0F, -8.0F, -4.0F, 10.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(26, 73).addBox(-2.0F, -4.0F, -5.0F, 4.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(8, 83).addBox(2.0F, -2.0F, -5.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(4, 83).addBox(-3.0F, -2.0F, -5.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -11.5F, 0.0F));

        PartDefinition shaman_mask = head.addOrReplaceChild("shaman_mask", CubeListBuilder.create().texOffs(0, 0).addBox(-6.75F, -8.3341F, -8.3484F, 13.0F, 6.0F, 19.0F, new CubeDeformation(-1.3F))
                .texOffs(8, 73).addBox(4.75F, -10.5841F, -2.3484F, 2.0F, 8.0F, 2.0F, new CubeDeformation(-0.25F))
                .texOffs(0, 73).addBox(-7.0F, -10.5841F, -2.3484F, 2.0F, 8.0F, 2.0F, new CubeDeformation(-0.25F)), PartPose.offsetAndRotation(0.25F, -3.5F, 1.3333F, 1.2217F, 0.0F, 0.0F));

        PartDefinition left_ear = head.addOrReplaceChild("left_ear", CubeListBuilder.create().texOffs(16, 78).addBox(-1.5F, 0.5F, -2.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(16, 73).addBox(-1.5F, 4.5F, -2.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 83).addBox(-1.5F, 1.5F, 1.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 83).addBox(-1.5F, 1.5F, -2.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(26, 78).addBox(-2.5F, 3.5F, -1.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(-0.25F)), PartPose.offsetAndRotation(-3.5F, -6.5F, 0.0F, 0.0F, 0.0F, 0.2618F));

        PartDefinition right_ear = head.addOrReplaceChild("right_ear", CubeListBuilder.create().texOffs(32, 57).addBox(0.5F, 0.5F, -2.0F, 1.0F, 5.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(26, 78).addBox(-0.5F, 3.5F, -1.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(-0.25F)), PartPose.offsetAndRotation(3.5F, -6.5F, 0.0F, 0.0F, 0.0F, -0.2618F));

        PartDefinition skirt = Entity.addOrReplaceChild("skirt", CubeListBuilder.create().texOffs(48, 25).addBox(-4.0F, -12.5F, -2.0F, 8.0F, 10.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition left_leg = Entity.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 41).addBox(-6.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, -12.0F, 0.0F));

        PartDefinition right_leg = Entity.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 57).addBox(2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, -12.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 112, 112);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.head.yRot = netHeadYaw * ((float) Math.PI / 180.0F);
        this.head.xRot = headPitch * ((float) Math.PI / 180.0F);

        this.animateWalk(HumanoidAnimations.WALK, limbSwing, limbSwingAmount, 2.0F, 2.5F);
        if (entity.walkAnimation.isMoving()) {
            this.animateWalk(HumanoidAnimations.WALK_SWING, limbSwing, limbSwingAmount, 2.0F, 2.5F);
        } else {
            this.animateWalk(ShamanAnimations.IDLE, ageInTicks, 1.0F, 1.0F, 1.0F);
        }
    }

    @Override
    public ModelPart root() {
        return this.Entity;
    }
}