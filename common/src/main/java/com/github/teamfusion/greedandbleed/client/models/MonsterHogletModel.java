package com.github.teamfusion.greedandbleed.client.models;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.LivingEntity;

public class MonsterHogletModel<T extends LivingEntity> extends AbstractHogletModel<T> {
    private final ModelPart root;

    public MonsterHogletModel(ModelPart root) {
        super(root);
        this.root = root;
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition Body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -5.0F, -4.7143F, 8.0F, 7.0F, 12.0F, new CubeDeformation(0.0F))
                .texOffs(20, 9).addBox(0.0F, -8.0F, -7.7143F, 0.0F, 5.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 17.0F, -1.2857F));

        PartDefinition LeftArm = Body.addOrReplaceChild("left_front_leg", CubeListBuilder.create().texOffs(10, 34).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 3.0F, -2.7143F));

        PartDefinition RightArm = Body.addOrReplaceChild("right_front_leg", CubeListBuilder.create().texOffs(18, 34).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 2.0F, -2.7143F));

        PartDefinition Tail = Body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(0, 21).addBox(0.0F, -2.0F, 0.0F, 0.0F, 3.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -3.0F, 7.2857F));

        PartDefinition head = Body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 19).addBox(-3.5F, -4.8571F, -6.3286F, 7.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(21, 26).addBox(-2.0F, -2.3571F, -11.3286F, 4.0F, 3.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(28, 6).addBox(-2.0F, -3.8571F, -11.2286F, 4.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(36, 34).addBox(2.0F, -3.8571F, -11.2286F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 19).addBox(-3.0F, -3.8571F, -11.2286F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -0.6429F, -4.3857F));

        PartDefinition LeftEar = head.addOrReplaceChild("left_ear", CubeListBuilder.create().texOffs(34, 24).addBox(-0.5F, -2.0F, -1.5F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.5F, -4.3571F, -0.8286F));

        PartDefinition RightEar = head.addOrReplaceChild("right_ear", CubeListBuilder.create().texOffs(26, 34).addBox(3.0F, -14.0F, -7.0F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 7.6429F, 4.6714F));

        PartDefinition Bandana = Body.addOrReplaceChild("bandana", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 1.0F, -4.8143F, -0.2182F, 0.0F, 0.0F));

        PartDefinition Pelvis = partdefinition.addOrReplaceChild("pelvis", CubeListBuilder.create(), PartPose.offset(0.0F, 19.0F, 2.0F));

        PartDefinition LeftLeg = Pelvis.addOrReplaceChild("left_hind_leg", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -1.0F, -1.5F, 3.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 0.0F, 0.5F));

        PartDefinition RightLeg = Pelvis.addOrReplaceChild("right_hind_leg", CubeListBuilder.create().texOffs(28, 0).mirror().addBox(-1.5F, -1.0F, -1.5F, 3.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(2.0F, 0.0F, 0.5F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }
}