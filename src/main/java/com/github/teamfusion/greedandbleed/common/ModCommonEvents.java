package com.github.teamfusion.greedandbleed.common;

import com.github.teamfusion.greedandbleed.GreedAndBleed;
import com.github.teamfusion.greedandbleed.common.entity.piglin.SkeletalPiglinEntity;
import com.github.teamfusion.greedandbleed.common.entity.piglin.PigmyEntity;
import com.github.teamfusion.greedandbleed.common.network.NetworkHandler;
import com.github.teamfusion.greedandbleed.common.registry.EntityTypeRegistry;
import com.github.teamfusion.greedandbleed.common.registry.SpawnPlacementRegistry;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = GreedAndBleed.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
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
