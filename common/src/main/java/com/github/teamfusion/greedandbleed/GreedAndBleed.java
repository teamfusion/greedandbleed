package com.github.teamfusion.greedandbleed;

import com.github.teamfusion.greedandbleed.client.ClientSetup;
import com.github.teamfusion.greedandbleed.common.CommonSetup;
import com.github.teamfusion.greedandbleed.common.registry.*;
import com.github.teamfusion.greedandbleed.platform.ModInstance;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

public class GreedAndBleed {
    public static final String MOD_ID = "greedandbleed";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final ModInstance INSTANCE = ModInstance.create(MOD_ID).common(CommonSetup::onBootstrap).postCommon(CommonSetup::onInitialized).client(ClientSetup::onBootstrap).postClient(ClientSetup::onInitialized).build();
    public static void bootstrap() {
        INSTANCE.bootstrap();
        PotionRegistry.MOB_EFFECT.register();
        PotionRegistry.POTION.register();
        EnchantmentRegistry.ENCHANTMENT.register();
        MemoryRegistry.MEMORY_MODULE_TYPES.register();
        SensorRegistry.SENSOR_TYPES.register();
        BlockRegistry.BLOCKS.register();
        ItemRegistry.ITEMS.register();
        PoiRegistry.POI_TYPES.register();
        EntityTypeRegistry.ENTITIES.register();
        FeatureRegistry.FEATURES.register();
        CreativeTabRegistry.CREATIVE_TABS.register();
    }
}