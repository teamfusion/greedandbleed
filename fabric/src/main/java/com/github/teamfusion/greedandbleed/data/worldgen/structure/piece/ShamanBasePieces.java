package com.github.teamfusion.greedandbleed.data.worldgen.structure.piece;

import com.github.teamfusion.greedandbleed.GreedAndBleed;
import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.data.worldgen.ProcessorLists;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;

public class ShamanBasePieces {
    public static final ResourceKey<StructureTemplatePool> SHAMAN_BASE = registerKey("shaman_base");

    public static ResourceKey<StructureTemplatePool> registerKey(String name) {
        return ResourceKey.create(Registries.TEMPLATE_POOL, new ResourceLocation(GreedAndBleed.MOD_ID, name));
    }

    public static void bootstrap(BootstapContext<StructureTemplatePool> bootstapContext) {
        HolderGetter<StructureProcessorList> holderGetter = bootstapContext.lookup(Registries.PROCESSOR_LIST);
        Holder<StructureProcessorList> holder = holderGetter.getOrThrow(ProcessorLists.BASTION_GENERIC_DEGRADATION);
        HolderGetter<StructureTemplatePool> holderGetter2 = bootstapContext.lookup(Registries.TEMPLATE_POOL);
        Holder<StructureTemplatePool> holder2 = holderGetter2.getOrThrow(Pools.EMPTY);
        bootstapContext.register(SHAMAN_BASE, new StructureTemplatePool(holder2, ImmutableList.of(Pair.of(StructurePoolElement.single(GreedAndBleed.MOD_ID + ":" + "shaman_base", holder), 1)), StructureTemplatePool.Projection.RIGID));
    }
}
