package com.infernalstudios.greedandbleed;

import com.infernalstudios.greedandbleed.common.registry.EntityTypeRegistry;
import com.infernalstudios.greedandbleed.common.registry.ItemRegistry;
import com.infernalstudios.greedandbleed.server.registry.MemoryModuleTypeRegistry;
import com.infernalstudios.greedandbleed.server.registry.SensorTypeRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(GreedAndBleed.MODID)
public class GreedAndBleed
{
    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MODID = "greedandbleed";

    public GreedAndBleed() {
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Deferred Registers
        LOGGER.debug("Registering entity types");
        EntityTypeRegistry.ENTITY_TYPES.register(modEventBus);
        LOGGER.debug("Registering items");
        ItemRegistry.ITEMS.register(modEventBus);
        LOGGER.debug("Registering sensor types");
        SensorTypeRegistry.SENSOR_TYPES.register(modEventBus);
        LOGGER.debug("Registering memory module types");
        MemoryModuleTypeRegistry.MEMORY_MODULE_TYPES.register(modEventBus);
    }
}
