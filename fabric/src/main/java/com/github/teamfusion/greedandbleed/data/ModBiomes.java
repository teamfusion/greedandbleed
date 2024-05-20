package com.github.teamfusion.greedandbleed.data;

import com.github.teamfusion.greedandbleed.common.registry.BiomeRegistry;
import com.github.teamfusion.greedandbleed.common.registry.EntityTypeRegistry;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.Carvers;
import net.minecraft.data.worldgen.biome.OverworldBiomes;
import net.minecraft.data.worldgen.placement.MiscOverworldPlacements;
import net.minecraft.data.worldgen.placement.NetherPlacements;
import net.minecraft.data.worldgen.placement.OrePlacements;
import net.minecraft.sounds.Musics;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class ModBiomes {
    public static void bootstrap(BootstapContext<Biome> biomeBootstapContext) {
        biomeBootstapContext.register(BiomeRegistry.HOGDEW_HOLLOW, hogedewHollow(biomeBootstapContext.lookup(Registries.PLACED_FEATURE), biomeBootstapContext.lookup(Registries.CONFIGURED_CARVER)));
    }

    public static Biome hogedewHollow(HolderGetter<PlacedFeature> holderGetter, HolderGetter<ConfiguredWorldCarver<?>> holderGetter2) {
        MobSpawnSettings mobSpawnSettings = (new MobSpawnSettings.Builder()).addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityTypeRegistry.HOGLET.get(), 9, 3, 4)).addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.STRIDER, 60, 1, 2)).build();
        BiomeGenerationSettings.Builder builder = (new BiomeGenerationSettings.Builder(holderGetter, holderGetter2)).addCarver(GenerationStep.Carving.AIR, Carvers.NETHER_CAVE).addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MiscOverworldPlacements.SPRING_LAVA);
        BiomeDefaultFeatures.addDefaultMushrooms(builder);
        builder.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.SPRING_OPEN).addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.PATCH_FIRE).addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.GLOWSTONE_EXTRA).addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.GLOWSTONE).addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, OrePlacements.ORE_MAGMA).addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.SPRING_CLOSED).addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.HOGDEW_PATCH).addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.HOGDEW_BUBBLE).addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.HOGDEW_FUNGUS).addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.HOGDEW_LUMPS).addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.HOGDEW_HOLLOW_VEGITATION);
        BiomeDefaultFeatures.addNetherDefaultOres(builder);
        return (new Biome.BiomeBuilder()).hasPrecipitation(false).temperature(2.0F).downfall(0.0F).specialEffects((new BiomeSpecialEffects.Builder()).waterColor(4159204).waterFogColor(329011).fogColor(3343107).skyColor(OverworldBiomes.calculateSkyColor(2.0F)).ambientParticle(new AmbientParticleSettings(ParticleTypes.CRIMSON_SPORE, 0.025F)).ambientLoopSound(SoundEvents.AMBIENT_CRIMSON_FOREST_LOOP).ambientMoodSound(new AmbientMoodSettings(SoundEvents.AMBIENT_CRIMSON_FOREST_MOOD, 6000, 8, 2.0)).ambientAdditionsSound(new AmbientAdditionsSettings(SoundEvents.AMBIENT_CRIMSON_FOREST_ADDITIONS, 0.0111)).backgroundMusic(Musics.createGameMusic(SoundEvents.MUSIC_BIOME_CRIMSON_FOREST)).build()).mobSpawnSettings(mobSpawnSettings).generationSettings(builder.build()).build();
    }
}
