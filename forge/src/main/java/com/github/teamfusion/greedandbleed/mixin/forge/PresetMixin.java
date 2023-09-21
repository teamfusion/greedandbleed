package com.github.teamfusion.greedandbleed.mixin.forge;

import com.github.teamfusion.greedandbleed.common.registry.BiomeRegistry;
import com.mojang.datafixers.util.Pair;
import dev.architectury.patchedmixin.staticmixin.spongepowered.asm.mixin.injection.At;
import dev.architectury.patchedmixin.staticmixin.spongepowered.asm.mixin.injection.Inject;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.MultiNoiseBiomeSourceParameterList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Mixin(targets = "net/minecraft/world/level/biome/MultiNoiseBiomeSourceParameterList$Preset$1")
public class PresetMixin {
    @Inject(method = "apply", at = @At("RETURN"), cancellable = true)
    public <T extends MultiNoiseBiomeSourceParameterList.Preset> void apply(Function<ResourceKey<Biome>, T> function, CallbackInfoReturnable<Climate.ParameterList<T>> cir) {
        List<Pair<Climate.ParameterPoint, T>> entryList = new ArrayList<>();

        entryList.add(Pair.of(Climate.parameters(0.475F, 0.6f, 0.0f, 0.0f, 0.0f, 0.0f, 0.125F), function.apply(BiomeRegistry.HOGDEW_HOLLOW)));
        entryList.addAll(cir.getReturnValue().values());
        cir.setReturnValue(new Climate.ParameterList<>(entryList));
    }
}