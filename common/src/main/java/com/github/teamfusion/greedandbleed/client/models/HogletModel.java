package com.github.teamfusion.greedandbleed.client.models;

import com.github.teamfusion.greedandbleed.client.aniamtion.HogletAnimation;
import com.github.teamfusion.greedandbleed.common.entity.piglin.Hoglet;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

public class HogletModel<T extends Hoglet> extends HierarchicalModel<T> {
    private final ModelPart root;
    public final ModelPart body;
    private final ModelPart leftFrontLeg;
    private final ModelPart rightFrontLeg;
    public final ModelPart head;
    private final ModelPart pelvis;
    private final ModelPart leftHindLeg;
    private final ModelPart rightHindLeg;
    public HogletModel(ModelPart root) {
        this.root = root;
        this.body = root.getChild("body");
        this.leftFrontLeg = this.body.getChild("left_front_leg");
        this.rightFrontLeg = this.body.getChild("right_front_leg");
        this.head = this.body.getChild("head");
        this.pelvis = root.getChild("pelvis");
        this.leftHindLeg = this.pelvis.getChild("left_hind_leg");
        this.rightHindLeg = this.pelvis.getChild("right_hind_leg");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create()
                .texOffs(0, 0).addBox(-4.0F, -5.0F, -4.7143F, 8.0F, 7.0F, 12.0F)
                .texOffs(20, 9).addBox(0.0F, -8.0F, -7.7143F, 0.0F, 5.0F, 10.0F),
                PartPose.offset(0.0F, 17.0F, -1.2857F));

        PartDefinition leftFrontLeg = body.addOrReplaceChild("left_front_leg", CubeListBuilder.create()
                .texOffs(12, 34).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 5.0F, 2.0F),
                PartPose.offset(-2.0F, 3.0F, -2.7143F));

        PartDefinition rightFrontLeg = body.addOrReplaceChild("right_front_leg", CubeListBuilder.create()
                .texOffs(20, 34).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 5.0F, 2.0F),
                PartPose.offset(2.0F, 2.0F, -2.7143F));

        PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create()
                .texOffs(0, 21).addBox(0.0F, -2.0F, 0.0F, 0.0F, 3.0F, 10.0F),
                PartPose.offset(0.0F, -3.0F, 7.2857F));

        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create()
                .texOffs(0, 19).addBox(-3.5F, -4.8571F, -6.3286F, 7.0F, 6.0F, 6.0F)
                .texOffs(21, 26).addBox(-2.0F, -2.3571F, -11.3286F, 4.0F, 3.0F, 5.0F)
                .texOffs(28, 6).addBox(-2.0F, -3.8571F, -11.2286F, 4.0F, 4.0F, 1.0F)
                .texOffs(36, 34).addBox(2.0F, -3.8571F, -11.2286F, 1.0F, 4.0F, 1.0F)
                .texOffs(0, 19).addBox(-3.0F, -3.8571F, -11.2286F, 1.0F, 4.0F, 1.0F),
                PartPose.offset(0.0F, -0.6429F, -4.3857F));

        PartDefinition leftEar = head.addOrReplaceChild("left_ear", CubeListBuilder.create()
                        .texOffs(34, 24).addBox(-0.5F, -2.0F, -1.5F, 1.0F, 2.0F, 3.0F),
                PartPose.offset(-3.5F, -4.3571F, -0.8286F));

        PartDefinition rightEar = head.addOrReplaceChild("right_ear", CubeListBuilder.create()
                        .texOffs(28, 34).addBox(3.0F, -14.0F, -7.0F, 1.0F, 2.0F, 3.0F),
                PartPose.offset(0.0F, 7.6429F, 4.6714F));

        PartDefinition bandana = body.addOrReplaceChild("bandana", CubeListBuilder.create()
                        .texOffs(28, 0).addBox(-4.0F, -1.0F, 0.0F, 8.0F, 6.0F, 0.0F),
                PartPose.offsetAndRotation(0.0F, 1.0F, -4.8143F, -0.2182F, 0.0F, 0.0F));

        PartDefinition pelvis = root.addOrReplaceChild("pelvis", CubeListBuilder.create(), PartPose.offset(0.0F, 19.0F, 2.0F));

        PartDefinition rightHindLeg = pelvis.addOrReplaceChild("right_hind_leg", CubeListBuilder.create()
                        .texOffs(0, 34).addBox(-1.5F, -1.0F, -1.5F, 3.0F, 6.0F, 3.0F),
                PartPose.offset(-2.0F, 0.0F, 0.5F));

        PartDefinition leftHindLeg = pelvis.addOrReplaceChild("left_hind_leg", CubeListBuilder.create()
                        .texOffs(0, 0).addBox(-1.5F, -1.0F, -1.5F, 3.0F, 6.0F, 3.0F),
                PartPose.offset(2.0F, 0.0F, 0.5F));

        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.head.yRot = netHeadYaw * ((float) Math.PI / 180.0F);
        this.head.xRot = headPitch * ((float) Math.PI / 180.0F);

        this.rightFrontLeg.xRot = Mth.cos(limbSwing) * 1.2F * limbSwingAmount;
        this.leftFrontLeg.xRot = Mth.cos(limbSwing + (float) Math.PI) * 1.2F * limbSwingAmount;
        this.rightHindLeg.xRot = this.leftFrontLeg.xRot;
        this.leftHindLeg.xRot = this.rightFrontLeg.xRot;
        if (this.young) {
            this.applyStatic(HogletAnimation.BABY);
        }
        this.animate(entity.diggingAnimationState, HogletAnimation.DIGGING, ageInTicks);
        this.animate(entity.angryAnimationState, HogletAnimation.ANGRY, ageInTicks);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.body.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        this.pelvis.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }
}