package com.github.teamfusion.greedandbleed;

import com.github.teamfusion.greedandbleed.client.ClientSetup;
import com.github.teamfusion.greedandbleed.common.CommonSetup;
import com.github.teamfusion.greedandbleed.platform.ModInstance;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

public class GreedAndBleed {
    public static final String MOD_ID = "greedandbleed";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final ModInstance INSTANCE = ModInstance.create(MOD_ID).client(ClientSetup::onBootstrap).postClient(ClientSetup::onInitialize).common(CommonSetup::onInitialize).postCommon(CommonSetup::onBootstrap).build();

    public static void bootstrap() {
        INSTANCE.bootstrap();


    }
}