package com.github.teamfusion.greedandbleed.mixin.forge;

import com.github.teamfusion.greedandbleed.common.registry.BiomeRegistry;
import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.MultiNoiseBiomeSourceParameterList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MultiNoiseBiomeSourceParameterList.class)
public class MultiNoiseBiomeSourceMixin {

    private HolderGetter<Biome> holderGetter;
    @Shadow
    @Final
    private MultiNoiseBiomeSourceParameterList.Preset preset;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(MultiNoiseBiomeSourceParameterList.Preset arg, HolderGetter<Biome> arg2, CallbackInfo ci) {
        holderGetter = arg2;
    }

    @Inject(method = "parameters", at = @At("RETURN"), cancellable = true)
    private void parameters(CallbackInfoReturnable<Climate.ParameterList<Holder<Biome>>> cir) {
        if (preset == MultiNoiseBiomeSourceParameterList.Preset.NETHER) {
            ImmutableList.Builder<Pair<Climate.ParameterPoint, Holder<Biome>>> builder = ImmutableList.builder();

            builder.addAll(cir.getReturnValue().values());
            builder.add(Pair.of(Climate.parameters(0.475F, 0.6f, 0.0f, 0.0f, 0.0f, 0.0f, 0.125F), holderGetter.getOrThrow(BiomeRegistry.HOGDEW_HOLLOW)));

            cir.setReturnValue(new Climate.ParameterList<>(builder.build()));
        }
    }
}
