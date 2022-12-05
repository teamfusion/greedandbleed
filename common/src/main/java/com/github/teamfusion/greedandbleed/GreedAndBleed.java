package com.github.teamfusion.greedandbleed;

import com.github.teamfusion.greedandbleed.client.ClientSetup;
import com.github.teamfusion.greedandbleed.common.CommonSetup;
import com.github.teamfusion.greedandbleed.common.registry.EntityTypeRegistry;
import com.github.teamfusion.greedandbleed.common.registry.ItemRegistry;
import com.github.teamfusion.greedandbleed.platform.Environment;
import com.github.teamfusion.greedandbleed.platform.ModInstance;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.slf4j.Logger;

public class GreedAndBleed {
    public static final String MOD_ID = "greedandbleed";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final ModInstance INSTANCE = ModInstance.create(MOD_ID).common(CommonSetup::onBootstrap).postCommon(CommonSetup::onInitialized).client(ClientSetup::onBootstrap).postClient(ClientSetup::onInitialized).build();
    public static final CreativeModeTab TAB_GREED_AND_BLEED = Environment.createTab(new ResourceLocation(GreedAndBleed.MOD_ID, "item_group"), new ItemStack(Items.SADDLE));

    public static void bootstrap() {
        INSTANCE.bootstrap();

        EntityTypeRegistry.ENTITIES.register();
        ItemRegistry.ITEMS.register();
    }
}