package com.github.teamfusion.greedandbleed.client.models;

import com.github.teamfusion.greedandbleed.api.HogEquipable;
import com.github.teamfusion.greedandbleed.common.entity.HasMountInventory;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.hoglin.HoglinBase;

public class GBHoglinModelComplete<T extends Mob & HoglinBase> extends AgeableListModel<T> {
    //TODO WIP Hoglin Model
    private static final float DEFAULT_HEAD_X_ROT = 0.87266463F;
    private static final float ATTACK_HEAD_X_ROT_END = -0.34906584F;
    private final ModelPart head;
    private final ModelPart rightEar;
    private final ModelPart leftEar;
    private final ModelPart body;
    private final ModelPart rightFrontLeg;
    private final ModelPart leftFrontLeg;
    private final ModelPart rightHindLeg;
    private final ModelPart leftHindLeg;
    private final ModelPart mane;
    private final ModelPart saddle;
    private final ModelPart left_chest;
    private final ModelPart right_chest;

    private final ModelPart right_tusk_harness;
    private final ModelPart left_tusk_harness;

    private final ModelPart head_harness;

    public GBHoglinModelComplete(ModelPart modelPart) {
        super(true, 8.0F, 6.0F, 1.9F, 2.0F, 24.0F);
        this.body = modelPart.getChild("body");
        this.mane = this.body.getChild("mane");
        this.head = modelPart.getChild("head");
        this.head_harness = this.head.getChild("head_harness");
        this.right_tusk_harness = this.head.getChild("right_tusk_harness");
        this.left_tusk_harness = this.head.getChild("left_tusk_harness");
        this.rightEar = this.head.getChild("right_ear");
        this.leftEar = this.head.getChild("left_ear");
        this.rightFrontLeg = modelPart.getChild("right_front_leg");
        this.leftFrontLeg = modelPart.getChild("left_front_leg");
        this.rightHindLeg = modelPart.getChild("right_hind_leg");
        this.leftHindLeg = modelPart.getChild("left_hind_leg");
        this.left_chest = this.body.getChild("left_chest");
        this.right_chest = this.body.getChild("right_chest");
        this.saddle = this.body.getChild("saddle");
    }

    public static LayerDefinition createBodyLayer(float size) {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition partDefinition = meshDefinition.getRoot();
        PartDefinition partDefinition2 = partDefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(1, 1).addBox(-8.0F, -7.0F, -13.0F, 16.0F, 14.0F, 26.0F, new CubeDeformation(size)), PartPose.offset(0.0F, 7.0F, 0.0F));
        partDefinition2.addOrReplaceChild("mane", CubeListBuilder.create().texOffs(90, 33).addBox(0.0F, 0.0F, -9.0F, 0.0F, 10.0F, 19.0F, new CubeDeformation(0.001F)), PartPose.offset(0.0F, -14.0F, -5.0F));
        partDefinition2.addOrReplaceChild("right_chest", CubeListBuilder.create().texOffs(0, 67).mirror().addBox(-11.0F, -22.0F, 10.0F, 3.0F, 8.0F, 8.0F, new CubeDeformation(0.00F)).mirror(false), PartPose.offset(0.0F, 18.0F, -17.0F));
        partDefinition2.addOrReplaceChild("left_chest", CubeListBuilder.create().texOffs(0, 67).addBox(8.0F, -22.0F, 10.0F, 3.0F, 8.0F, 8.0F, new CubeDeformation(0.00F)), PartPose.offset(0.0F, 18.0F, -17.0F));
        partDefinition2.addOrReplaceChild("saddle", CubeListBuilder.create().texOffs(1, 63).addBox(-8.0F, -24.0F, -8.5F, 16.0F, 16.0F, 26.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, 18.0F, -17.0F));


        PartDefinition partDefinition3 = partDefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(61, 1).addBox(-7.0F, -3.0F, -19.0F, 14.0F, 6.0F, 19.0F, new CubeDeformation(size)), PartPose.offsetAndRotation(0.0F, 2.0F, -12.0F, 0.87266463F, 0.0F, 0.0F));
        partDefinition3.addOrReplaceChild("head_harness", CubeListBuilder.create().texOffs(62, 65).addBox(-7.0F, -25.0F, -28.5F, 14.0F, 6.0F, 19.0F, new CubeDeformation(size + 0.25F)), PartPose.offsetAndRotation(-1.0F, 24.0F, 8.5F, 0.0F, 0.0F, 0));
        partDefinition3.addOrReplaceChild("right_ear", CubeListBuilder.create().texOffs(1, 1).addBox(-6.0F, -1.0F, -2.0F, 6.0F, 1.0F, 4.0F, new CubeDeformation(size)), PartPose.offsetAndRotation(-6.0F, -2.0F, -3.0F, 0.0F, 0.0F, -0.6981317F));
        partDefinition3.addOrReplaceChild("left_ear", CubeListBuilder.create().texOffs(1, 6).addBox(0.0F, -1.0F, -2.0F, 6.0F, 1.0F, 4.0F, new CubeDeformation(size)), PartPose.offsetAndRotation(6.0F, -2.0F, -3.0F, 0.0F, 0.0F, 0.6981317F));
        partDefinition3.addOrReplaceChild("right_horn", CubeListBuilder.create().texOffs(10, 13).addBox(-1.0F, -11.0F, -1.0F, 2.0F, 11.0F, 2.0F), PartPose.offset(-7.0F, 2.0F, -12.0F));
        partDefinition3.addOrReplaceChild("left_horn", CubeListBuilder.create().texOffs(1, 13).addBox(-1.0F, -11.0F, -1.0F, 2.0F, 11.0F, 2.0F), PartPose.offset(7.0F, 2.0F, -12.0F));
        partDefinition3.addOrReplaceChild("right_tusk_harness", CubeListBuilder.create().texOffs(120, 0).addBox(-1.0F, -11.0F, -1.0F, 2.0F, 11.0F, 2.0F, new CubeDeformation(size + 0.25F)), PartPose.offset(-7.0F, 2.0F, -12.0F));
        partDefinition3.addOrReplaceChild("left_tusk_harness", CubeListBuilder.create().texOffs(120, 0).addBox(-1.0F, -11.0F, -1.0F, 2.0F, 11.0F, 2.0F, new CubeDeformation(size + 0.25F)), PartPose.offset(7.0F, 2.0F, -12.0F));
        partDefinition.addOrReplaceChild("right_front_leg", CubeListBuilder.create().texOffs(66, 42).addBox(-3.0F, 0.0F, -3.0F, 6.0F, 14.0F, 6.0F, new CubeDeformation(size)), PartPose.offset(-4.0F, 10.0F, -8.5F));
        partDefinition.addOrReplaceChild("left_front_leg", CubeListBuilder.create().texOffs(41, 42).addBox(-3.0F, 0.0F, -3.0F, 6.0F, 14.0F, 6.0F, new CubeDeformation(size)), PartPose.offset(4.0F, 10.0F, -8.5F));
        partDefinition.addOrReplaceChild("right_hind_leg", CubeListBuilder.create().texOffs(21, 45).addBox(-2.5F, 0.0F, -2.5F, 5.0F, 11.0F, 5.0F, new CubeDeformation(size)), PartPose.offset(-5.0F, 13.0F, 10.0F));
        partDefinition.addOrReplaceChild("left_hind_leg", CubeListBuilder.create().texOffs(0, 45).addBox(-2.5F, 0.0F, -2.5F, 5.0F, 11.0F, 5.0F, new CubeDeformation(size)), PartPose.offset(5.0F, 13.0F, 10.0F));
        return LayerDefinition.create(meshDefinition, 128, 128);
    }

    protected Iterable<ModelPart> headParts() {
        return ImmutableList.of(this.head);
    }

    protected Iterable<ModelPart> bodyParts() {
        return ImmutableList.of(this.body, this.rightFrontLeg, this.leftFrontLeg, this.rightHindLeg, this.leftHindLeg);
    }

    public void setupAnim(T hoglin, float f, float g, float h, float i, float j) {
        this.rightEar.zRot = -0.6981317F - g * Mth.sin(f);
        this.leftEar.zRot = 0.6981317F + g * Mth.sin(f);
        this.head.yRot = i * 0.017453292F;
        int k = ((HoglinBase) hoglin).getAttackAnimationRemainingTicks();
        float l = 1.0F - (float) Mth.abs(10 - 2 * k) / 10.0F;
        this.head.xRot = Mth.lerp(l, 0.87266463F, -0.34906584F);
        if (hoglin.isBaby()) {
            this.head.y = Mth.lerp(l, 2.0F, 5.0F);
            this.mane.z = -3.0F;
        } else {
            this.head.y = 2.0F;
            this.mane.z = -7.0F;
        }

        float m = 1.2F;
        this.rightFrontLeg.xRot = Mth.cos(f) * 1.2F * g;
        this.leftFrontLeg.xRot = Mth.cos(f + 3.1415927F) * 1.2F * g;
        this.rightHindLeg.xRot = this.leftFrontLeg.xRot;
        this.leftHindLeg.xRot = this.rightFrontLeg.xRot;

        if (hoglin instanceof HogEquipable hogEquipable) {
            saddle.visible = hogEquipable.isHogSaddled();
            right_tusk_harness.visible = hogEquipable.isHogSaddled();
            left_tusk_harness.visible = hogEquipable.isHogSaddled();
            head_harness.visible = hogEquipable.isHogSaddled();
        } else {
            saddle.visible = false;
            right_tusk_harness.visible = false;
            left_tusk_harness.visible = false;
            head_harness.visible = false;
        }

        // CHEST
        boolean hasChest = hoglin instanceof HasMountInventory && ((HasMountInventory) hoglin).hasChest();
        if (hasChest) {
            this.left_chest.visible = true;
            this.right_chest.visible = true;
        } else {
            this.left_chest.visible = false;
            this.right_chest.visible = false;
        }
    }
}