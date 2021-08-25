package com.infernalstudios.greedandbleed.client;

import com.infernalstudios.greedandbleed.common.entity.IToleratingMount;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
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

    @SubscribeEvent
    public static void onRenderingXPBar(RenderGameOverlayEvent.Pre event){
        RenderGameOverlayEvent.ElementType type = event.getType();
        if (type == RenderGameOverlayEvent.ElementType.EXPERIENCE){
            Minecraft minecraft = Minecraft.getInstance();
            ClientPlayerEntity player = minecraft.player;
            if(player != null && player.getVehicle() instanceof IToleratingMount){
                event.setCanceled(true);
                IToleratingMount toleratingMount = (IToleratingMount) player.getVehicle();
                RenderHelper.renderToleranceMeter(toleratingMount, event.getMatrixStack());
            }
        }
    }
}
