package com.github.teamfusion.greedandbleed.platform.client.forge;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.ForgeHooksClient;

public class ArmorHookImpl {
    public static String getArmorTexture(Entity entity, ItemStack stack, String s1, EquipmentSlot slot, String type) {
        return ForgeHooksClient.getArmorTexture(entity, stack, s1, slot, type);
    }

    public static Model getArmorModel(LivingEntity entity, ItemStack itemStack, EquipmentSlot slot, HumanoidModel model) {
        return ForgeHooksClient.getArmorModel(entity, itemStack, slot, model);
    }

    public static boolean getArmorRender(PoseStack matrices, MultiBufferSource vertexConsumers, ItemStack stack, LivingEntity entity, EquipmentSlot slot, int light, HumanoidModel<LivingEntity> contextModel) {
        return false;
    }
}
