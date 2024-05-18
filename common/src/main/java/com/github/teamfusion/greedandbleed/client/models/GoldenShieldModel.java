package com.github.teamfusion.greedandbleed.client.models;

import com.github.teamfusion.greedandbleed.GreedAndBleed;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class GoldenShieldModel extends Model {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(GreedAndBleed.MOD_ID, "golden_shield"), "main");
    public static final ResourceLocation LOCATION = new ResourceLocation(GreedAndBleed.MOD_ID, "textures/entity/shield/golden_shield.png");
    private final ModelPart plate;
    private final ModelPart handle;

    public GoldenShieldModel(ModelPart root) {
        super(RenderType::entityCutoutNoCull);
        this.plate = root.getChild("plate");
        this.handle = root.getChild("handle");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition plate = partdefinition.addOrReplaceChild("plate", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -6.0F, -2.0F, 12.0F, 12.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 13.0F, -1.0F));

        PartDefinition handle = partdefinition.addOrReplaceChild("handle", CubeListBuilder.create().texOffs(26, 0).addBox(-1.0F, -3.0F, -1.0F, 2.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 13.0F, -1.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        plate.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        handle.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    public ModelPart handle() {
        return handle;
    }

    public ModelPart plate() {
        return plate;
    }
}