package com.github.teamfusion.greedandbleed.data.client;

import com.github.teamfusion.greedandbleed.GreedAndBleed;
import net.minecraft.data.models.model.ModelTemplate;
import net.minecraft.data.models.model.TextureSlot;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;

public class GBModelTemplates {
    public static final ModelTemplate NYLIUM_PATH = create("nylium_path", TextureSlot.TOP, TextureSlot.BOTTOM, TextureSlot.SIDE);

    private static ModelTemplate create(String string, TextureSlot... textureSlots) {
        return new ModelTemplate(Optional.of(new ResourceLocation(GreedAndBleed.MOD_ID, "block/" + string)), Optional.empty(), textureSlots);
    }

    private static ModelTemplate createItem(String string, TextureSlot... textureSlots) {
        return new ModelTemplate(Optional.of(new ResourceLocation(GreedAndBleed.MOD_ID, "item/" + string)), Optional.empty(), textureSlots);
    }

    private static ModelTemplate create(String string, String string2, TextureSlot... textureSlots) {
        return new ModelTemplate(Optional.of(new ResourceLocation(GreedAndBleed.MOD_ID, "block/" + string)), Optional.of(string2), textureSlots);
    }
}
