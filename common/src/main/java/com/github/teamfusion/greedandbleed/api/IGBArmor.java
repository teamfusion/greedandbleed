package com.github.teamfusion.greedandbleed.api;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelPart;

public interface IGBArmor {
    /*
     * This method translate for correct armor render. basically Armor
     * @author bagu_chan
     */

    void translateToHead(ModelPart part, PoseStack poseStack);

    void translateToChest(ModelPart part, PoseStack poseStack);

    void translateToLeg(ModelPart part, PoseStack poseStack);

    void translateToChestPat(ModelPart part, PoseStack poseStack);

    /*
     * if empty part. dosen't render
     */

    default Iterable<ModelPart> rightHandArmors() {
        return ImmutableList.of();
    }

    default Iterable<ModelPart> leftHandArmors() {
        return ImmutableList.of();
    }

    default Iterable<ModelPart> rightLegPartArmors() {
        return ImmutableList.of();
    }

    default Iterable<ModelPart> leftLegPartArmors() {
        return ImmutableList.of();
    }

    default Iterable<ModelPart> bodyPartArmors() {
        return ImmutableList.of();
    }

    default Iterable<ModelPart> headPartArmors() {
        return ImmutableList.of();
    }
}