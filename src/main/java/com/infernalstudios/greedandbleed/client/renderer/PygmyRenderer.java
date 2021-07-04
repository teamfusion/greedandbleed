package com.infernalstudios.greedandbleed.client.renderer;

import com.google.common.collect.ImmutableMap;
import com.infernalstudios.greedandbleed.GreedAndBleed;
import com.infernalstudios.greedandbleed.client.models.PygmyArmorModel;
import com.infernalstudios.greedandbleed.client.models.PygmyModel;
import com.infernalstudios.greedandbleed.common.registry.EntityTypeRegistry;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.monster.piglin.AbstractPiglinEntity;
import net.minecraft.util.ResourceLocation;

import java.util.Map;

public class PygmyRenderer extends BipedRenderer<MobEntity, PygmyModel<MobEntity>> {
    private static final Map<EntityType<?>, ResourceLocation> resourceLocations = ImmutableMap.of(
            EntityTypeRegistry.PYGMY.get(), new ResourceLocation(GreedAndBleed.MODID, "textures/entity/piglin/pygmy.png"));

    public PygmyRenderer(EntityRendererManager entityRendererManager, boolean zombified) {
        super(entityRendererManager, createModel(zombified), 0.5F, 1.0019531F, 1.0F, 1.0019531F);
        this.addLayer(new BipedArmorLayer<>(this,
                new PygmyArmorModel<>(0.5F),
                new PygmyArmorModel<>(1.02F)));
    }

    private static PygmyModel<MobEntity> createModel(boolean zombified) {
        PygmyModel<MobEntity> pygmyModel = new PygmyModel<>(0.0F);
        if (zombified) {
            pygmyModel.earLeft.visible = false;
        }

        return pygmyModel;
    }

    @Override
    public ResourceLocation getTextureLocation(MobEntity mobEntity) {
        ResourceLocation resourcelocation = resourceLocations.get(mobEntity.getType());
        if (resourcelocation == null) {
            throw new IllegalArgumentException("I don't know what texture to use for " + mobEntity.getType());
        } else {
            return resourcelocation;
        }
    }

    @Override
    protected boolean isShaking(MobEntity mobEntity) {
        return mobEntity instanceof AbstractPiglinEntity && ((AbstractPiglinEntity)mobEntity).isConverting();
    }
}
