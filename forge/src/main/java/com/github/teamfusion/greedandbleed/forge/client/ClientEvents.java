package com.github.teamfusion.greedandbleed.forge.client;


import com.github.teamfusion.greedandbleed.GreedAndBleed;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = GreedAndBleed.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientEvents {

    @SubscribeEvent
    public static void registerModels(final ModelEvent.RegisterAdditional evt) {
        evt.register(new ResourceLocation(GreedAndBleed.MOD_ID, "item/slingshot_back"));
    }
}
