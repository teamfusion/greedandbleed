package com.github.teamfusion.greedandbleed.common.item;

import com.github.teamfusion.greedandbleed.GreedAndBleed;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class HoglinArmorItem extends Item {
    private final int protection;
    private final ResourceLocation texture;

    public HoglinArmorItem(int protection, String armorName, Properties properties) {
        this(protection, new ResourceLocation(GreedAndBleed.MOD_ID, "textures/entity/hoglin/armor/hoglin_armor_" + armorName + ".png"), properties);
    }

    public HoglinArmorItem(int protection, ResourceLocation texture, Properties properties) {
        super(properties);
        this.protection = protection;
        this.texture = texture;
    }

    @Environment(EnvType.CLIENT)
    public ResourceLocation getTexture() {
        return this.texture;
    }

    public int getProtection() {
        return this.protection;
    }
}