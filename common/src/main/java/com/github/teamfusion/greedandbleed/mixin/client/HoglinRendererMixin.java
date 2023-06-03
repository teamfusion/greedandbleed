package com.github.teamfusion.greedandbleed.mixin.client;

import com.github.teamfusion.greedandbleed.GreedAndBleed;
import com.github.teamfusion.greedandbleed.api.HogEquipable;
import com.github.teamfusion.greedandbleed.client.ClientSetup;
import com.github.teamfusion.greedandbleed.client.layers.HoglinArmorLayer;
import com.github.teamfusion.greedandbleed.client.models.GBHoglinModelComplete;
import com.github.teamfusion.greedandbleed.common.entity.HasMountInventory;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HoglinRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HoglinRenderer.class)
public abstract class HoglinRendererMixin extends MobRenderer<Hoglin, GBHoglinModelComplete<Hoglin>> {
    private static final ResourceLocation GB_HOGLIN_TEXTURE =
            new ResourceLocation(GreedAndBleed.MOD_ID,
                    "textures/entity/hoglin/hoglin.png");
    private static final ResourceLocation GB_HOGLIN_SADDLE_TEXTURE =
            new ResourceLocation(GreedAndBleed.MOD_ID,
                    "textures/entity/hoglin/hoglin_with_saddle.png");

    public GBHoglinModelComplete<Hoglin> modelComplete;

    public HoglinRendererMixin(EntityRendererProvider.Context context, GBHoglinModelComplete<Hoglin> entityModel, float f) {
        super(context, entityModel, f);
    }

    @Inject(method = ("<init>"), at = @At("TAIL"))
    public void init(EntityRendererProvider.Context context, CallbackInfo callbackInfo) {
        HoglinRenderer hoglinRenderer = (HoglinRenderer) ((Object) this);
        modelComplete = new GBHoglinModelComplete<>(context.bakeLayer(ClientSetup.HOGLIN));
        this.model = modelComplete;
        this.addLayer(new HoglinArmorLayer(hoglinRenderer, context.getModelSet()));
    }

    @Inject(method = ("getTextureLocation"), at = @At("HEAD"), cancellable = true)
    public void getTextureLocation(Hoglin hoglinEntity, CallbackInfoReturnable<ResourceLocation> callbackInfoReturnable) {
        boolean hasChestOrSaddle = hoglinEntity instanceof HasMountInventory && ((HasMountInventory) hoglinEntity).hasChest()
                || hoglinEntity instanceof HogEquipable && ((HogEquipable) hoglinEntity).isHogSaddled();

        callbackInfoReturnable.setReturnValue(hasChestOrSaddle ? GB_HOGLIN_SADDLE_TEXTURE : GB_HOGLIN_TEXTURE);
    }
}