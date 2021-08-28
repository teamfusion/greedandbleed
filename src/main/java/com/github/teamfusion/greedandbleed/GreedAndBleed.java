package com.github.teamfusion.greedandbleed;

import com.github.teamfusion.greedandbleed.server.registry.MemoryModuleTypeRegistry;
import com.github.teamfusion.greedandbleed.server.registry.SensorTypeRegistry;
import com.github.teamfusion.greedandbleed.common.registry.EnchantmentRegistry;
import com.github.teamfusion.greedandbleed.common.registry.EntityTypeRegistry;
import com.github.teamfusion.greedandbleed.common.registry.ItemRegistry;
import com.github.teamfusion.greedandbleed.common.registry.LootModifierRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(GreedAndBleed.MOD_ID)
public class GreedAndBleed
{
    public static final String MOD_ID = "greedandbleed";
    public static final String MOD_NAME = "Greed and Bleed";

    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public GreedAndBleed() {
        log("Initializing");

        MinecraftForge.EVENT_BUS.register(this);
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        // Deferred Registers
        log(Level.DEBUG, "Registering entity types");
        EntityTypeRegistry.ENTITY_TYPES.register(bus);

        log(Level.DEBUG, "Registering enchantments");
        EnchantmentRegistry.ENCHANTMENTS.register(bus);

        log(Level.DEBUG, "Registering items");
        ItemRegistry.ITEMS.register(bus);

        log(Level.DEBUG, "Registering loot modifiers");
        LootModifierRegistry.LOOT_MODIFIERS.register(bus);

        log(Level.DEBUG, "Registering sensor types");
        SensorTypeRegistry.SENSOR_TYPES.register(bus);

        log(Level.DEBUG, "Registering memory module types");
        MemoryModuleTypeRegistry.MEMORY_MODULE_TYPES.register(bus);

        log("Initialized");
    }

    public static void log(Level level, String message) {
        LOGGER.log(level, "[" + MOD_NAME + "] " + message);
    }
    public static void log(String message) {
        log(Level.INFO, message);
    }
}
