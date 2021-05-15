package com.infernalstudios.greedandbleed.client.renderer;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.MobEntity;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class PigmyRenderFactory<T extends MobEntity> implements IRenderFactory<T> {
    private final boolean zombified;

    public PigmyRenderFactory(boolean zombified) {
        this.zombified = zombified;
    }

    @Override
    public EntityRenderer<? super T> createRenderFor(EntityRendererManager manager) {
        return new PigmyRenderer(manager, this.zombified);
    }
}
