package com.github.teamfusion.greedandbleed.data.worldgen.structure;

import com.github.teamfusion.greedandbleed.GreedAndBleed;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;

import java.util.List;

public class ModStructureSets {
    public static final ResourceKey<StructureSet> SHAMAN_BASE = registerKey("shaman_base");

    public static ResourceKey<StructureSet> registerKey(String name) {
        return ResourceKey.create(Registries.STRUCTURE_SET, new ResourceLocation(GreedAndBleed.MOD_ID, name));
    }

    public static void bootstrap(BootstapContext<StructureSet> bootstapContext) {
        HolderGetter<Structure> holderGetter = bootstapContext.lookup(Registries.STRUCTURE);
        HolderGetter<Biome> holderGetter2 = bootstapContext.lookup(Registries.BIOME);

        bootstapContext.register(SHAMAN_BASE, new StructureSet(List.of(StructureSet.entry(holderGetter.getOrThrow(ModStructures.SHAMAN_BASE), 1)), new RandomSpreadStructurePlacement(32, 6, RandomSpreadType.LINEAR, 28184232)));
    }
}
