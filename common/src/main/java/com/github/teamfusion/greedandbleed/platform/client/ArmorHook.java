package com.github.teamfusion.greedandbleed.platform.client;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class ArmorHook {
    @ExpectPlatform
    public static String getArmorTexture(Entity entity, ItemStack stack, String s1, EquipmentSlot slot, String type) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static Model getArmorModel(LivingEntity entity, ItemStack itemStack, EquipmentSlot slot, HumanoidModel model) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static boolean getArmorRender(PoseStack matrices, MultiBufferSource vertexConsumers, ItemStack stack, LivingEntity entity, EquipmentSlot slot, int light, HumanoidModel<LivingEntity> contextModel) {
        throw new AssertionError();
    }
}
