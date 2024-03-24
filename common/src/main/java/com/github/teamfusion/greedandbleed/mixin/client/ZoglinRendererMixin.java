package com.github.teamfusion.greedandbleed.mixin.client;

import com.github.teamfusion.greedandbleed.client.layers.HoglinArmorLayer;
import net.minecraft.client.model.HoglinModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.ZoglinRenderer;
import net.minecraft.world.entity.monster.Zoglin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ZoglinRenderer.class)
public abstract class ZoglinRendererMixin extends MobRenderer<Zoglin, HoglinModel<Zoglin>> {


    public ZoglinRendererMixin(EntityRendererProvider.Context context, HoglinModel<Zoglin> entityModel, float f) {
        super(context, entityModel, f);
    }

    @Inject(method = ("<init>"), at = @At("TAIL"))
    public void init(EntityRendererProvider.Context context, CallbackInfo callbackInfo) {
        ZoglinRenderer hoglinRenderer = (ZoglinRenderer) ((Object) this);
        this.addLayer(new HoglinArmorLayer<>(hoglinRenderer, context.getModelSet()));
    }
}