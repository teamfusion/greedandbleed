package com.github.teamfusion.greedandbleed.client;

import com.github.teamfusion.greedandbleed.GreedAndBleed;
import com.github.teamfusion.greedandbleed.common.entity.ToleratingMount;
import com.github.teamfusion.greedandbleed.common.item.SlingshotPouchItem;
import com.github.teamfusion.greedandbleed.common.network.GreedAndBleedServerNetwork;
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

public class RenderHelper {
    public static final ResourceLocation TOLERANCE_METER_LOCATION = new ResourceLocation(GreedAndBleed.MOD_ID, "textures/gui/tolerance_meter.png");
    private static Gui ingameGui;
    private static Minecraft minecraft;

    private static final ResourceLocation TEXTURE = new ResourceLocation(GreedAndBleed.MOD_ID, "textures/gui/hud/pouch.png");
    private static final ResourceLocation TEXTURE_SELECT = new ResourceLocation(GreedAndBleed.MOD_ID, "textures/gui/hud/pouch_selected.png");


    public static boolean onMouseScrolled(Minecraft minecraft, double scrollDelta) {

        Player player = minecraft.player;
        ItemStack pouch = player.getMainHandItem().is(ItemRegistry.SLINGSHOT_POUCH.get()) ? player.getMainHandItem() : player.getOffhandItem().is(ItemRegistry.SLINGSHOT_POUCH.get()) ? player.getOffhandItem() : ItemStack.EMPTY;
        if (player.isShiftKeyDown() && pouch.getItem() == ItemRegistry.SLINGSHOT_POUCH.get()) {
            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
            buf.writeInt(scrollDelta > 0 ? -1 : 1);
            NetworkManager.sendToServer(GreedAndBleedServerNetwork.SELECT_SYNC_PACKET, buf);
            return true;
        }
        return false;
    }

    private static void renderSlot(GuiGraphics graphics, int pX, int pY, ItemStack itemStack) {
        if (!itemStack.isEmpty()) {
            graphics.renderItem(itemStack, pX, pY);
            graphics.renderItemDecorations(Minecraft.getInstance().font, itemStack, pX, pY);
        }
    }

    public static void renderItemContent(GuiGraphics graphics, int screenWidth, int screenHeight) {

        if (Minecraft.getInstance().getCameraEntity() instanceof Player player) {
            ItemStack pouch = player.getMainHandItem().is(ItemRegistry.SLINGSHOT_POUCH.get()) ? player.getMainHandItem() : player.getOffhandItem().is(ItemRegistry.SLINGSHOT_POUCH.get()) ? player.getOffhandItem() : ItemStack.EMPTY;
            if (pouch.getItem() == ItemRegistry.SLINGSHOT_POUCH.get()) {
                ///gui.setupOverlayRenderState(true, false);
                PoseStack poseStack = graphics.pose();
                poseStack.pushPose();

                int selected = SlingshotPouchItem.getSelectedItem(pouch);
                List<ItemStack> list = SlingshotPouchItem.getContents(pouch).toList();
                int slots = list.size();
                int uWidth = slots * 20 + 2;
                int px = screenWidth / 2 - 91 - 29;
                int py = screenHeight - 16 - 3 - 16;


                //TODO Config
                px += 0;
                py += 0;
                int i1 = 1;
                for (int i = 0; i < slots; ++i) {
                    int jy = py - 10 - i * 20;
                    if (!list.isEmpty() && list.size() > i) {
                        renderSlot(graphics, px + 3, jy + 3, list.get(i));
                        graphics.blit(TEXTURE, px, jy, 0, 0, 22, 22, 22, 22);

                    } else {
                        renderSlot(graphics, px + 3, jy + 3, null);
                        graphics.blit(TEXTURE, px, jy, 0, 0, 22, 22, 22, 22);
                    }
                }
                if (selected < slots) {
                    int jy = py - 10 - selected * 20;
                    graphics.blit(TEXTURE_SELECT, px - 1, jy - 1, 0, 0, 24, 24, 24, 24);
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


    public static void renderSlingshotPouchSlot(GuiGraphics guiGraphics, float partialTick) {
        if (Minecraft.getInstance().player != null && Minecraft.getInstance().player.isShiftKeyDown()) {
            renderItemContent(guiGraphics,
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