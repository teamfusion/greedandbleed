package com.infernalstudios.greedandbleed.client.renderer;

import com.google.common.collect.ImmutableMap;
import com.infernalstudios.greedandbleed.GreedAndBleed;
import com.infernalstudios.greedandbleed.client.models.PigmyArmorModel;
import com.infernalstudios.greedandbleed.client.models.PigmyModel;
import com.infernalstudios.greedandbleed.common.registry.EntityTypeRegistry;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.monster.piglin.AbstractPiglinEntity;
import net.minecraft.util.ResourceLocation;

import java.util.Map;

public class PigmyRenderer extends BipedRenderer<MobEntity, PigmyModel<MobEntity>> {
    private static final Map<EntityType<?>, ResourceLocation> resourceLocations = ImmutableMap.of(
            EntityTypeRegistry.PYGMY.get(), new ResourceLocation(GreedAndBleed.MODID, "textures/entity/piglin/pigmy.png"));

    public PigmyRenderer(EntityRendererManager entityRendererManager, boolean zombified) {
        super(entityRendererManager, createModel(zombified), 0.5F, 1.0019531F, 1.0F, 1.0019531F);
        this.addLayer(new BipedArmorLayer<>(this,
                new PigmyArmorModel<>(0.5F),
                new PigmyArmorModel<>(1.02F)));
    }

    private static PigmyModel<MobEntity> createModel(boolean zombified) {
        PigmyModel<MobEntity> pigmyModel = new PigmyModel<>(0.0F);
        if (zombified) {
            pigmyModel.earLeft.visible = false;
        }

        return pigmyModel;
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
