package com.github.teamfusion.greedandbleed.platform.client.forge;

import com.github.teamfusion.greedandbleed.GreedAndBleed;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = GreedAndBleed.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RenderRegistryImpl {
    private static final Set<Consumer<EntityRenderersEvent.RegisterRenderers>> RENDERERS = ConcurrentHashMap.newKeySet();
    private static final Set<Consumer<EntityRenderersEvent.RegisterLayerDefinitions>> DEFINITIONS = ConcurrentHashMap.newKeySet();

    public static void block(RenderType type, Block... blocks) {
        Arrays.stream(blocks).forEach(block -> ItemBlockRenderTypes.setRenderLayer(block, type));
    }

    @SubscribeEvent
    public static void onRendererRegistry(EntityRenderersEvent.RegisterRenderers event) {
        RENDERERS.forEach(registry -> registry.accept(event));
    }

    @SubscribeEvent
    public static void onLayerDefinitionRegistry(EntityRenderersEvent.RegisterLayerDefinitions event) {
        DEFINITIONS.forEach(registry -> registry.accept(event));
    }

    public static <T extends Entity> void entityModel(Supplier<? extends EntityType<? extends T>> type, EntityRendererProvider<T> provider) {
        RENDERERS.add(event -> event.registerEntityRenderer(type.get(), provider));
    }

    public static void layerDefinition(ModelLayerLocation location, Supplier<LayerDefinition> definition) {
        DEFINITIONS.add(event -> event.registerLayerDefinition(location, definition));
    }
}