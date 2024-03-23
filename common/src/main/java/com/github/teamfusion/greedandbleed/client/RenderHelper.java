package com.github.teamfusion.greedandbleed.client;

import com.github.teamfusion.greedandbleed.GreedAndBleed;
import com.github.teamfusion.greedandbleed.client.network.GreedAndBleedClientNetwork;
import com.github.teamfusion.greedandbleed.common.entity.ToleratingMount;
import com.github.teamfusion.greedandbleed.common.item.SlingshotPouchItem;
import com.github.teamfusion.greedandbleed.common.registry.ItemRegistry;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static dev.architectury.networking.NetworkManager.clientToServer;

public class RenderHelper {
    public static final ResourceLocation TOLERANCE_METER_LOCATION = new ResourceLocation(GreedAndBleed.MOD_ID, "textures/gui/tolerance_meter.png");
    private static Gui ingameGui;
    private static Minecraft minecraft;

    private static final ResourceLocation TEXTURE = new ResourceLocation(GreedAndBleed.MOD_ID, "hud/pouch");
    private static final ResourceLocation TEXTURE_SELECT = new ResourceLocation(GreedAndBleed.MOD_ID, "hud/pouch_selected");


    public static boolean onMouseScrolled(double scrollDelta) {

        Player player = Minecraft.getInstance().player;
        ItemStack pouch = player.getMainHandItem().is(ItemRegistry.SLINGSHOT_POUCH.get()) ? player.getMainHandItem() : player.getOffhandItem().is(ItemRegistry.SLINGSHOT_POUCH.get()) ? player.getOffhandItem() : ItemStack.EMPTY;
        if (pouch.getItem() == ItemRegistry.SLINGSHOT_POUCH.get()) {
            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
            buf.writeInt(scrollDelta > 0 ? -1 : 1);
            NetworkManager.collectPackets(GreedAndBleedClientNetwork.ofTrackingEntity(() -> player), clientToServer(), GreedAndBleedClientNetwork.SCREEN_OPEN_PACKET, buf);
            return true;
        }
        return false;
    }

    private static void renderSlot(GuiGraphics graphics, int pX, int pY, ItemStack itemStack) {
        if (!itemStack.isEmpty()) {
            graphics.renderItem(itemStack, pX, pY);
        }
    }

    public static void renderItemContent(GuiGraphics graphics, float partialTicks, int screenWidth, int screenHeight) {

        if (Minecraft.getInstance().getCameraEntity() instanceof Player player) {
            ItemStack pouch = player.getMainHandItem().is(ItemRegistry.SLINGSHOT_POUCH.get()) ? player.getMainHandItem() : player.getOffhandItem().is(ItemRegistry.SLINGSHOT_POUCH.get()) ? player.getOffhandItem() : ItemStack.EMPTY;
            if (pouch.getItem() == ItemRegistry.SLINGSHOT_POUCH.get()) {
                ///gui.setupOverlayRenderState(true, false);
                PoseStack poseStack = graphics.pose();
                poseStack.pushPose();

                int selected = SlingshotPouchItem.getSelectedItem(pouch);
                List<ItemStack> list = SlingshotPouchItem.getContents(pouch).toList();
                int slots = list.size();
                int centerX = screenWidth / 2;

                poseStack.pushPose();
                poseStack.translate(0, 0, -90);

                int uWidth = slots * 20 + 2;
                int px = uWidth / 2 - 91 - 26;
                int py = screenHeight - 16 - 3 - 16;


                //TODO Config
                px += 0;
                py += 0;
                poseStack.popPose();

                int i1 = 1;
                for (int i = 0; i < slots; ++i) {
                    int jy = centerX - py + 3 - i * 20;
                    if (!list.isEmpty() && list.size() > i) {
                        renderSlot(graphics, px + 12, jy + 20, list.get(i));
                        if (selected == i) {
                            graphics.blit(TEXTURE_SELECT, px, jy, 0, 0, 22, 22);
                        } else {
                            graphics.blit(TEXTURE, px, jy, 0, 0, 22, 22);
                        }
                    } else {
                        renderSlot(graphics, px + 12, jy + 20, null);
                        if (selected == i) {
                            graphics.blit(TEXTURE_SELECT, px, jy, 0, 0, 22, 22);
                        } else {
                            graphics.blit(TEXTURE, px, jy, 0, 0, 22, 22);
                        }
                    }
                }


                if (!list.isEmpty() && list.size() > selected) {
                    ItemStack selectedArrow = list.get(selected);
                    drawHighlight(graphics, screenWidth, py, selectedArrow);
                }


                poseStack.popPose();
            }
        }
    }


    protected static void drawHighlight(GuiGraphics graphics, int screenWidth, int py, ItemStack selectedEntity) {
        int l;

        MutableComponent mutablecomponent = Component.empty().append(selectedEntity.getDisplayName());
        Component highlightTip = mutablecomponent;
        int fontWidth = Minecraft.getInstance().font.width(highlightTip);
        int nx = (screenWidth - fontWidth) / 2;
        int ny = py - 19;

        l = 255;

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        graphics.fill(nx - 2, ny - 2, nx + fontWidth + 2, ny + 9 + 2, Minecraft.getInstance().options.getBackgroundColor(0));
        graphics.drawString(Minecraft.getInstance().font, highlightTip, nx, ny, 0xFFFFFF + (l << 24));
        RenderSystem.disableBlend();
    }


    public static void renderSlingshotPouchSlot(GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
        if (Minecraft.getInstance().player != null && Minecraft.getInstance().player.isShiftKeyDown()) {
            renderItemContent(guiGraphics, partialTick,
                    Minecraft.getInstance().getWindow().getGuiScaledWidth(),
                    Minecraft.getInstance().getWindow().getGuiScaledHeight());
        }
    }

    public static void renderToleranceMeter(ToleratingMount toleratingMount, GuiGraphics guiGraphics) {
        boolean minecraftChanged = minecraft != Minecraft.getInstance();
        if (minecraftChanged) {
            GreedAndBleed.LOGGER.info("RenderHelper.minecraft was updated to the current Minecraft instance!");
        }
        minecraft = minecraftChanged ? Minecraft.getInstance() : minecraft;
        minecraft.getProfiler().push("toleranceBar");

        float toleranceProgress = toleratingMount.getToleranceProgress();
        int barLength = 182;
        int barHeight = 5;
        int scaledToleranceProgress = (int) (toleranceProgress * 183.0F);

        int screenWidth = minecraft.getWindow().getGuiScaledWidth();
        int barXPos = screenWidth / 2 - 91;
        int screenHeight = minecraft.getWindow().getGuiScaledHeight();
        int barYPos = screenHeight - 32 + 3;

        int emptyBarTexOffsX = 0;
        int emptyBarTexOffsY = 84;
        int fullBarTexOffsX = 0;
        int fullTexOffsY = 89;

        Gui ingameGui = getIngameGui(minecraftChanged);
        if (ingameGui != null) {
            guiGraphics.blit(TOLERANCE_METER_LOCATION, barXPos, barYPos, emptyBarTexOffsX, emptyBarTexOffsY, barLength, barHeight);
            if (scaledToleranceProgress > 0) {
                guiGraphics.blit(TOLERANCE_METER_LOCATION, barXPos, barYPos, fullBarTexOffsX, fullTexOffsY, scaledToleranceProgress, barHeight);
            }
        } else {
            GreedAndBleed.LOGGER.error("Unable to render tolerance meter!");
        }

        minecraft.getProfiler().pop();
    }

    @Nullable
    private static Gui getIngameGui(boolean minecraftChanged) {
        if (ingameGui == null || minecraftChanged) {
            Gui gui = Minecraft.getInstance().gui;
            ingameGui = gui;
        }
        return ingameGui;
    }
}