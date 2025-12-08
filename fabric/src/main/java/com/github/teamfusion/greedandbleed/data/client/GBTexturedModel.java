package com.github.teamfusion.greedandbleed.data.client;

import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.data.models.model.TextureSlot;
import net.minecraft.data.models.model.TexturedModel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import static net.minecraft.data.models.model.TextureMapping.getBlockTexture;
import static net.minecraft.data.models.model.TexturedModel.createDefault;

public class GBTexturedModel {
    public static final TexturedModel.Provider NYLIUM_PATH = createDefault(GBTexturedModel::cubeNyliumPath, ModelTemplates.CUBE_BOTTOM_TOP);

    public static TextureMapping cubeNyliumPath(Block block) {
        return (new TextureMapping()).put(TextureSlot.SIDE, getBlockTexture(block, "_side")).put(TextureSlot.TOP, getBlockTexture(block)).put(TextureSlot.BOTTOM, getBlockTexture(Blocks.NETHERRACK));
    }
}
