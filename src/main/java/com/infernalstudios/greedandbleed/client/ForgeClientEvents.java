package com.infernalstudios.greedandbleed.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(
        bus = Mod.EventBusSubscriber.Bus.FORGE,
        value = Dist.CLIENT)
public class ForgeClientEvents {

    @SubscribeEvent
    public static void onClientInput(InputEvent.KeyInputEvent event){
        //TODO: Make mounted hoglins attack on input
    }
}