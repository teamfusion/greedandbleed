package com.github.teamfusion.greedandbleed.common.integrations;

import com.blackgear.platform.common.integration.BlockIntegration.Event;
import com.github.teamfusion.greedandbleed.common.registry.BlockRegistry;
import com.github.teamfusion.greedandbleed.common.registry.PotionRegistry;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

public class BlockIntegrations {
    public static void bootstrap(Event event) {
        event.registerPotionBrewing(brewing -> {
            brewing.addStartMix(BlockRegistry.HOGDEW_FUNGUS.get().asItem(), PotionRegistry.IMMUNITY_POTION.get());
            brewing.addMix(PotionRegistry.IMMUNITY_POTION.get(), Items.REDSTONE, PotionRegistry.LONG_IMMUNITY_POTION.get());
            brewing.addMix(PotionRegistry.IMMUNITY_POTION.get(), Items.GLOWSTONE, PotionRegistry.STRONG_IMMUNITY_POTION.get());
        });
        
        event.registerFlattenableBlock(Blocks.CRIMSON_NYLIUM, BlockRegistry.CRIMSON_NYLIUM_PATH.get());
        event.registerFlattenableBlock(Blocks.WARPED_NYLIUM, BlockRegistry.WARPED_NYLIUM_PATH.get());
        event.registerFlattenableBlock(BlockRegistry.HOGDEW_NYLIUM.get(), BlockRegistry.HOGDEW_NYLIUM_PATH.get());
    }
}