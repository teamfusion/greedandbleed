package com.infernalstudios.greedandbleed.common;

import com.infernalstudios.greedandbleed.GreedAndBleed;
import com.infernalstudios.greedandbleed.common.entity.piglin.PigmyEntity;
import com.infernalstudios.greedandbleed.common.entity.piglin.SkeletalPiglinEntity;
import com.infernalstudios.greedandbleed.common.network.NetworkHandler;
import com.infernalstudios.greedandbleed.common.registry.EntityTypeRegistry;
import com.infernalstudios.greedandbleed.common.registry.SpawnPlacementRegistry;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = GreedAndBleed.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModCommonEvents {

    @SubscribeEvent
    public static void onAttributeCreation(EntityAttributeCreationEvent event){
        event.put(EntityTypeRegistry.PIGMY.get(), PigmyEntity.createAttributes().build());
        event.put(EntityTypeRegistry.SKELETAL_PIGLIN.get(), SkeletalPiglinEntity.setCustomAttributes().build());
    }

    @SubscribeEvent
    public static void setup(final FMLCommonSetupEvent event)
    {
        GreedAndBleed.LOGGER.debug("Registering network");
        event.enqueueWork(NetworkHandler::register);

        event.enqueueWork(SpawnPlacementRegistry::register);
    }
}
