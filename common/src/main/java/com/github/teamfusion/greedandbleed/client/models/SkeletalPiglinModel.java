package com.github.teamfusion.greedandbleed.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.PiglinModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.Mob;

public class SkeletalPiglinModel<T extends Mob> extends PiglinModel<T> {
    public final ModelPart rightEar = this.head.getChild("right_ear");
    private final ModelPart leftEar = this.head.getChild("left_ear");

    public SkeletalPiglinModel(ModelPart root) {
        super(root);
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
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        float f1 = ageInTicks * 0.1F + limbSwing * 0.5F;
        float f2 = 0.08F + limbSwingAmount * 0.4F;
        this.leftEar.zRot = (-(float)Math.PI / 6F) - Mth.cos(f1 * 1.2F) * f2;
        this.rightEar.zRot = ((float)Math.PI / 6F) + Mth.cos(f1) * f2;

        this.leftPants.copyFrom(this.leftLeg);
        this.rightPants.copyFrom(this.rightLeg);
        this.leftSleeve.copyFrom(this.leftArm);
        this.rightSleeve.copyFrom(this.rightArm);
        this.jacket.copyFrom(this.body);
        this.hat.copyFrom(this.head);

        this.hat.visible = false;
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