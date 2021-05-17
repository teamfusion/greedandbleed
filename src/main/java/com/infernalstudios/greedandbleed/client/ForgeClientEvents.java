package com.infernalstudios.greedandbleed.client;

import com.infernalstudios.greedandbleed.common.entity.IToleratingMount;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.AbstractGui;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.opengl.GL11;

import static net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType.EXPERIENCE;
import static net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType.JUMPBAR;

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
        if(type == RenderGameOverlayEvent.ElementType.EXPERIENCE){
            Minecraft minecraft = Minecraft.getInstance();
            ClientPlayerEntity clientPlayer = minecraft.player;
            if(clientPlayer.getVehicle() instanceof IToleratingMount){
                event.setCanceled(true);
                IToleratingMount toleratingMount = (IToleratingMount) clientPlayer.getVehicle();
                RenderHelper.renderToleranceMeter(toleratingMount, event.getMatrixStack());
            }
        }
    }
}