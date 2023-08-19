package com.github.teamfusion.greedandbleed.platform.common.worldgen;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class BiomeManager {
    private static final List<BiConsumer<BiomeWriter, BiomeContext>> FEATURES = Lists.newArrayList();
    private static final Map<BiConsumer<BiomeWriter, BiomeContext>, ResourceKey<Biome>[]> BIOME_FEATURES = Maps.newConcurrentMap();

    public static final BiomeManager INSTANCE = new BiomeManager();

    @ExpectPlatform
    public static void setup() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static <T extends Mob> void registrySpawnPlacement(EntityType<T> entityType, SpawnPlacements.Type placementType, Heightmap.Types heightTypes, SpawnPlacements.SpawnPredicate<T> spawnPredicate) {
        throw new AssertionError();
    }

    public void register(BiomeWriter writer) {
        FEATURES.forEach(writer::add);
        BIOME_FEATURES.forEach(writer::add);
    }

    public static void add(BiConsumer<BiomeWriter, BiomeContext> modifier) {
        FEATURES.add(modifier);
    }

    @SafeVarargs
    public static void add(BiConsumer<BiomeWriter, BiomeContext> modifier, ResourceKey<Biome>... biomes) {
        BIOME_FEATURES.put(modifier, biomes);
    }
}