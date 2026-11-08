package com.github.teamfusion.greedandbleed.client;

import com.blackgear.platform.client.GameRendering;
import com.blackgear.platform.core.ParallelDispatch;
import com.blackgear.platform.core.network.base.NetworkDirection;
import com.github.teamfusion.greedandbleed.GreedAndBleed;
import com.github.teamfusion.greedandbleed.client.models.*;
import com.github.teamfusion.greedandbleed.client.network.ClientboundHurtPacket;
import com.github.teamfusion.greedandbleed.client.network.ClientboundScreenOpenPacket;
import com.github.teamfusion.greedandbleed.client.renderer.*;
import com.github.teamfusion.greedandbleed.client.screen.PygmyArmorStandScreen;
import com.github.teamfusion.greedandbleed.client.screen.PygmyStationScreen;
import com.github.teamfusion.greedandbleed.common.item.slingshot.SlingshotItem;
import com.github.teamfusion.greedandbleed.common.item.slingshot.SlingshotPouchItem;
import com.github.teamfusion.greedandbleed.common.registry.BlockRegistry;
import com.github.teamfusion.greedandbleed.common.registry.EntityTypeRegistry;
import com.github.teamfusion.greedandbleed.common.registry.ItemRegistry;
import com.github.teamfusion.greedandbleed.common.registry.MenuTypeRegistry;
import dev.architectury.registry.menu.MenuRegistry;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public class ClientSetup {
    private static final Supplier<LayerDefinition> OUTER_ARMOR_DEFINITION = () -> LayerDefinition.create(HumanoidModel.createMesh(new CubeDeformation(1.0F), 0.0F), 64, 32);
    private static final Supplier<LayerDefinition> INNER_ARMOR_DEFINITION = () -> LayerDefinition.create(HumanoidModel.createMesh(new CubeDeformation(0.5F), 0.0F), 64, 32);

    public static final ModelLayerLocation HOGLIN = new ModelLayerLocation(new ResourceLocation(GreedAndBleed.MOD_ID, "gb_hoglin"), "main");
    public static final ModelLayerLocation HOGLIN_ARMOR = new ModelLayerLocation(new ResourceLocation(GreedAndBleed.MOD_ID, "hoglin_armor"), "main");


    public static void onBootstrap() {
        GreedAndBleed.NETWORKING.registerPacket(NetworkDirection.CLIENTBOUND, ClientboundScreenOpenPacket.ID, ClientboundScreenOpenPacket.HANDLER, ClientboundScreenOpenPacket.class);
        GreedAndBleed.NETWORKING.registerPacket(NetworkDirection.CLIENTBOUND, ClientboundHurtPacket.ID, ClientboundHurtPacket.HANDLER, ClientboundHurtPacket.class);
        
        // HOGLET RENDERER
        GameRendering.registerEntityRenderers(event -> {
            event.register(EntityTypeRegistry.HOGLET.get(), HogletRenderer::new);
            event.register(EntityTypeRegistry.ZOMBIFIED_HOGLET.get(), ZombifiedHogletRenderer::new);
            event.register(EntityTypeRegistry.SKELETON_HOGLET.get(), SkeletonHogletRenderer::new);
            event.register(EntityTypeRegistry.SHAMAN_PIGLIN.get(), ShamanPiglinRenderer::new);
            event.register(EntityTypeRegistry.PIGMY.get(), PigmyRenderer::new);
            event.register(EntityTypeRegistry.SHRYGMY.get(), ShrygmyRenderer::new);
            event.register(EntityTypeRegistry.HOGGART.get(), HoggartRenderer::new);
            event.register(EntityTypeRegistry.ZOMBIFIED_PIGMIES.get(), ZombifiedPigmyRenderer::new);
            event.register(EntityTypeRegistry.WARPED_PIGLIN.get(), WarpedPiglinRenderer::new);
            event.register(EntityTypeRegistry.THROWN_DAMAGEABLE.get(), ThrownItemRenderer::new);
            event.register(EntityTypeRegistry.WARPED_SPIT.get(), WarpedSpitRenderer::new);
            event.register(EntityTypeRegistry.SKELETAL_PIGLIN.get(), SkeletalPiglinRenderer::new);
        });
        
        GameRendering.registerModelLayers(event -> {
            event.register(HogletRenderer.MAIN, AbstractHogletModel::createBodyLayer);
            event.register(HogletRenderer.MONSTER, MonsterHogletModel::createBodyLayer);
            event.register(SkeletalPiglinRenderer.MAIN, SkeletalPiglinModel::createBodyLayer);
            event.register(SkeletalPiglinRenderer.OUTER_ARMOR, OUTER_ARMOR_DEFINITION);
            event.register(SkeletalPiglinRenderer.INNER_ARMOR, INNER_ARMOR_DEFINITION);
            event.register(HOGLIN, () -> GBHoglinModelComplete.createBodyLayer(0.0F));
            event.register(HOGLIN_ARMOR, () -> GBHoglinModelComplete.createBodyLayer(0.05F));
            event.register(ShamanPiglinRenderer.MAIN, ShamanPiglinModel::createBodyLayer);
            event.register(PigmyRenderer.MAIN, PigmyModel::createBodyLayer);
            event.register(ShrygmyRenderer.MAIN, ShrygmyModel::createBodyLayer);
            event.register(HoggartRenderer.MAIN, HoggartModel::createBodyLayer);
            event.register(WarpedPiglinRenderer.MAIN, WarpedPiglinModel::createBodyLayer);
            event.register(WarpedSpitRenderer.LAYER_LOCATION, WarpedSpitModel::createBodyLayer);
            event.register(GoldenShieldModel.LAYER_LOCATION, GoldenShieldModel::createBodyLayer);
        });
    }

    public static void onInitialized(ParallelDispatch dispatch) {
        MenuRegistry.registerScreenFactory(MenuTypeRegistry.PYGMY_STATION.get(), PygmyStationScreen::new);
        MenuRegistry.registerScreenFactory(MenuTypeRegistry.PYGMY_ARMOR_STAND.get(), PygmyArmorStandScreen::new);
        GameRendering.registerBlockRenderers(event -> {
            event.register(RenderType.cutout(),
                BlockRegistry.HOGDEW_FUNGUS.get(),
                BlockRegistry.HOGDEW_LUMPS.get(),
                BlockRegistry.HOGDEW_DOOR.get(),
                BlockRegistry.HOGDEW_TRAPDOOR.get()
            );
        });
        
        ItemProperties.register(ItemRegistry.SLINGSHOT.get(), new ResourceLocation("pull"), (stack, level, entity, seed) -> {
            if (entity == null) {
                return 0.0f;
            }
            if (entity.getUseItem() != stack) {
                return 0.0f;
            }
            return SlingshotItem.getPowerForTime(stack.getUseDuration() - entity.getUseItemRemainingTicks());
        });
        ItemProperties.register(ItemRegistry.SLINGSHOT.get(), new ResourceLocation("pulling"), (stack, level, entity, seed) -> {
            return entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F;
        });
        ItemProperties.register(ItemRegistry.SLINGSHOT_POUCH.get(), new ResourceLocation("full"), (stack, level, entity, seed) -> {
            return SlingshotPouchItem.getFullnessDisplay(stack) > 0.0F ? 1.0F : 0.0F;
        });
        ItemProperties.register(ItemRegistry.GOLDEN_SHIELD.get(), new ResourceLocation("blocking"), (itemStack, clientLevel, livingEntity, i) -> livingEntity != null && livingEntity.isUsingItem() && livingEntity.getUseItem() == itemStack ? 1.0f : 0.0f);
    }
}