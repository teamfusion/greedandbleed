package com.github.teamfusion.greedandbleed.client;

import com.github.teamfusion.greedandbleed.common.entity.ToleratingMount;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.NamedGuiOverlay;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(
        bus = Mod.EventBusSubscriber.Bus.FORGE,
        value = Dist.CLIENT)
public class ForgeClientEvents {

    @SubscribeEvent
    public static void onClientInput(InputEvent event) {
        //TODO: Make mounted hoglins attack on input
    }

    @SubscribeEvent
    public static void onRenderingXPBar(RenderGuiOverlayEvent.Pre event) {
        NamedGuiOverlay type = event.getOverlay();
        if (type == VanillaGuiOverlay.EXPERIENCE_BAR.type()) {
            Minecraft minecraft = Minecraft.getInstance();
            LocalPlayer player = minecraft.player;
            if (player != null && player.getVehicle() instanceof ToleratingMount) {
                event.setCanceled(true);
                ToleratingMount toleratingMount = (ToleratingMount) player.getVehicle();
                RenderHelper.renderToleranceMeter(toleratingMount, event.getGuiGraphics());
            }
        }
    }
}