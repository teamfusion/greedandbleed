package com.github.teamfusion.greedandbleed.data.worldgen.structure;

import com.github.teamfusion.greedandbleed.GreedAndBleed;
import com.github.teamfusion.greedandbleed.common.registry.GBBiomeTags;
import com.github.teamfusion.greedandbleed.data.worldgen.structure.piece.PigmyEncampmentPieces;
import com.github.teamfusion.greedandbleed.data.worldgen.structure.piece.ShamanBasePieces;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.heightproviders.ConstantHeight;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSpawnOverride;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.structures.JigsawStructure;

import java.util.Map;

public class ModStructures {
    public static final ResourceKey<Structure> SHAMAN_BASE = registerKey("shaman_base");
    public static final ResourceKey<Structure> PIGMY_ENCAMPMENT = registerKey("pigmy_encampment");

    public static ResourceKey<Structure> registerKey(String name) {
        return ResourceKey.create(Registries.STRUCTURE, new ResourceLocation(GreedAndBleed.MOD_ID, name));
    }


    private static Structure.StructureSettings structure(HolderSet<Biome> holderSet, Map<MobCategory, StructureSpawnOverride> map, GenerationStep.Decoration decoration, TerrainAdjustment terrainAdjustment) {
        return new Structure.StructureSettings(holderSet, map, decoration, terrainAdjustment);
    }

    private static Structure.StructureSettings structure(HolderSet<Biome> holderSet, GenerationStep.Decoration decoration, TerrainAdjustment terrainAdjustment) {
        return structure(holderSet, Map.of(), decoration, terrainAdjustment);
    }

    private static Structure.StructureSettings structure(HolderSet<Biome> holderSet, TerrainAdjustment terrainAdjustment) {
        return structure(holderSet, Map.of(), GenerationStep.Decoration.SURFACE_STRUCTURES, terrainAdjustment);
    }

    public static void bootstrap(BootstapContext<Structure> bootstapContext) {
        HolderGetter<Biome> holderGetter = bootstapContext.lookup(Registries.BIOME);
        HolderGetter<StructureTemplatePool> holderGetter2 = bootstapContext.lookup(Registries.TEMPLATE_POOL);
        bootstapContext.register(SHAMAN_BASE, new JigsawStructure(structure(holderGetter.getOrThrow(GBBiomeTags.HAS_SHAMAN_BASE), TerrainAdjustment.BEARD_THIN), holderGetter2.getOrThrow(ShamanBasePieces.SHAMAN_BASE), 6, ConstantHeight.of(VerticalAnchor.absolute(80)), false));
        bootstapContext.register(PIGMY_ENCAMPMENT, new JigsawStructure(structure(holderGetter.getOrThrow(GBBiomeTags.HAS_PIGMY_ENCAMPMENTS), TerrainAdjustment.BEARD_THIN), holderGetter2.getOrThrow(PigmyEncampmentPieces.MAIN), 6, ConstantHeight.of(VerticalAnchor.absolute(80)), false));
    }
}
