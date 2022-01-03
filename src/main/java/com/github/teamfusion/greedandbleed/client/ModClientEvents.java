package com.github.teamfusion.greedandbleed.client;

import com.github.teamfusion.greedandbleed.GreedAndBleed;
import com.github.teamfusion.greedandbleed.client.renderer.GBHoglinRenderer;
import com.github.teamfusion.greedandbleed.client.renderer.HogletRenderer;
import com.github.teamfusion.greedandbleed.client.renderer.PigmyRenderFactory;
import com.github.teamfusion.greedandbleed.client.renderer.SkeletalPiglinRenderer;
import com.github.teamfusion.greedandbleed.common.entity.piglin.PigmyEntity;
import com.github.teamfusion.greedandbleed.common.registry.EntityTypeRegistry;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraft.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(
        modid = GreedAndBleed.MOD_ID,
        bus = Mod.EventBusSubscriber.Bus.MOD,
        value = Dist.CLIENT)
public class ModClientEvents {

    public static final PigmyRenderFactory<PigmyEntity> PIGMY_RENDER_FACTORY = new PigmyRenderFactory<>(false);

    @SubscribeEvent
    public static void onClientSetup(final FMLClientSetupEvent event) {
		ItemRenderer itemRenderer = event.getMinecraftSupplier().get().getItemRenderer();

		RenderingRegistry.registerEntityRenderingHandler(
				EntityTypeRegistry.PIGMY.get(), PIGMY_RENDER_FACTORY
		);
		RenderingRegistry.registerEntityRenderingHandler(
				EntityTypeRegistry.SKELETAL_PIGLIN.get(),
				SkeletalPiglinRenderer::new
		);
		RenderingRegistry.registerEntityRenderingHandler(
				EntityTypeRegistry.HOGLET.get(),
				HogletRenderer::new
		);

		// Ideally I would replace this with a Mixin into the original renderer
		// However, couldn't figure out how to call super in the injection into the constructor
		RenderingRegistry.registerEntityRenderingHandler(
				EntityType.HOGLIN, GBHoglinRenderer::new
		);

		RenderingRegistry.registerEntityRenderingHandler(
				EntityTypeRegistry.THROWN_DAMAGEABLE.get(), m -> new SpriteRenderer<>(m, itemRenderer)
		);
    }
}
