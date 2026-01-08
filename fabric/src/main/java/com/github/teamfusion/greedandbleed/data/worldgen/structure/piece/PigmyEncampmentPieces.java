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

public class PigmyEncampmentPieces {
    public static final ResourceKey<StructureTemplatePool> FARM = registerKey("pigmy_encampment/farm");
    public static final ResourceKey<StructureTemplatePool> HUT = registerKey("pigmy_encampment/hut");
    public static final ResourceKey<StructureTemplatePool> TENT = registerKey("pigmy_encampment/tent");
    public static final ResourceKey<StructureTemplatePool> MISC = registerKey("pigmy_encampment/misc");
    public static final ResourceKey<StructureTemplatePool> STATUE = registerKey("pigmy_encampment/statue");
    public static final ResourceKey<StructureTemplatePool> ROAD = registerKey("pigmy_encampment/road");
    public static final ResourceKey<StructureTemplatePool> TENT_ROAD = registerKey("pigmy_encampment/tent_road");
    public static final ResourceKey<StructureTemplatePool> MAIN = registerKey("pigmy_encampment/main");
    public static final ResourceKey<StructureTemplatePool> PILLAR = registerKey("pigmy_encampment/pillar");
    public static final ResourceKey<StructureTemplatePool> WALL = registerKey("pigmy_encampment/wall");
    public static final ResourceKey<StructureTemplatePool> MOB_PIGMY = registerKey("pigmy_encampment/mob/pigmy");
    public static final ResourceKey<StructureTemplatePool> MOB_HOGLET = registerKey("pigmy_encampment/mob/hoglet");
    public static final ResourceKey<StructureTemplatePool> MOB_HOGGART = registerKey("pigmy_encampment/mob/hoggart");
    public static final ResourceKey<StructureTemplatePool> MOB_HOGLIN = registerKey("pigmy_encampment/mob/hoglin");

    public static ResourceKey<StructureTemplatePool> registerKey(String name) {
        return ResourceKey.create(Registries.TEMPLATE_POOL, new ResourceLocation(GreedAndBleed.MOD_ID, name));
    }

    public static void bootstrap(BootstapContext<StructureTemplatePool> bootstapContext) {
        HolderGetter<StructureProcessorList> holderGetter = bootstapContext.lookup(Registries.PROCESSOR_LIST);
        Holder<StructureProcessorList> holder = holderGetter.getOrThrow(ProcessorLists.BASTION_GENERIC_DEGRADATION);
        HolderGetter<StructureTemplatePool> holderGetter2 = bootstapContext.lookup(Registries.TEMPLATE_POOL);
        Holder<StructureTemplatePool> holder2 = holderGetter2.getOrThrow(Pools.EMPTY);
        bootstapContext.register(FARM, new StructureTemplatePool(holder2, ImmutableList.of(
                Pair.of(StructurePoolElement.single(prefix("pigmy_encampment/farm/hogdew_farm1"), holder), 1),
                Pair.of(StructurePoolElement.single(prefix("pigmy_encampment/farm/hogdew_farm2"), holder), 1),
                Pair.of(StructurePoolElement.single(prefix("pigmy_encampment/farm/hogdew_farm3"), holder), 1),
                Pair.of(StructurePoolElement.single(prefix("pigmy_encampment/farm/hogdew_farm4"), holder), 1),
                Pair.of(StructurePoolElement.single(prefix("pigmy_encampment/farm/hogdew_farm5"), holder), 1),
                Pair.of(StructurePoolElement.single(prefix("pigmy_encampment/farm/hogdew_farm6"), holder), 1),
                Pair.of(StructurePoolElement.single(prefix("pigmy_encampment/farm/hogdew_farm7"), holder), 1),
                Pair.of(StructurePoolElement.single(prefix("pigmy_encampment/farm/netherwart_farm1"), holder), 1),
                Pair.of(StructurePoolElement.single(prefix("pigmy_encampment/farm/netherwart_farm2"), holder), 1),
                Pair.of(StructurePoolElement.single(prefix("pigmy_encampment/farm/netherwart_farm3"), holder), 1),
                Pair.of(StructurePoolElement.single(prefix("pigmy_encampment/farm/netherwart_farm4"), holder), 1),
                Pair.of(StructurePoolElement.single(prefix("pigmy_encampment/farm/netherwart_farm5"), holder), 1),
                Pair.of(StructurePoolElement.single(prefix("pigmy_encampment/farm/netherwart_farm6"), holder), 1),
                Pair.of(StructurePoolElement.single(prefix("pigmy_encampment/farm/netherwart_farm7"), holder), 1),
                Pair.of(StructurePoolElement.single(prefix("pigmy_encampment/farm/hoglin_pens1"), holder), 1),
                Pair.of(StructurePoolElement.single(prefix("pigmy_encampment/farm/hoglin_pens2"), holder), 1),
                Pair.of(StructurePoolElement.single(prefix("pigmy_encampment/farm/pigmy_pen1"), holder), 1),
                Pair.of(StructurePoolElement.single(prefix("pigmy_encampment/farm/pigmy_pen2"), holder), 1),
                Pair.of(StructurePoolElement.single(prefix("pigmy_encampment/farm/pigmy_pen3"), holder), 1)
        ), StructureTemplatePool.Projection.RIGID));
        bootstapContext.register(HUT, new StructureTemplatePool(holder2, ImmutableList.of(
                Pair.of(StructurePoolElement.single(prefix("pigmy_encampment/hut/hoggart_den1"), holder), 1),
                Pair.of(StructurePoolElement.single(prefix("pigmy_encampment/hut/hoggart_den2"), holder), 1),
                Pair.of(StructurePoolElement.single(prefix("pigmy_encampment/hut/pigmy_diningroom"), holder), 1),
                Pair.of(StructurePoolElement.single(prefix("pigmy_encampment/hut/pigmy_room1"), holder), 1),
                Pair.of(StructurePoolElement.single(prefix("pigmy_encampment/hut/pigmy_room2"), holder), 1)),
                StructureTemplatePool.Projection.RIGID));
        bootstapContext.register(TENT, new StructureTemplatePool(holder2, ImmutableList.of(
                Pair.of(StructurePoolElement.single(prefix("pigmy_encampment/camp/pigmy_tent2"), holder), 1),
                Pair.of(StructurePoolElement.single(prefix("pigmy_encampment/camp/pigmy_tent3"), holder), 1)),
                StructureTemplatePool.Projection.RIGID));
        bootstapContext.register(MAIN, new StructureTemplatePool(holder2, ImmutableList.of(
                Pair.of(StructurePoolElement.single(prefix("pigmy_encampment/main/campfire_1"), holder), 1),
                Pair.of(StructurePoolElement.single(prefix("pigmy_encampment/main/campfire_2"), holder), 1)),
                StructureTemplatePool.Projection.RIGID));
        bootstapContext.register(ROAD, new StructureTemplatePool(holder2, ImmutableList.of(
                Pair.of(StructurePoolElement.single(prefix("pigmy_encampment/road/corner_1"), holder), 1),
                Pair.of(StructurePoolElement.single(prefix("pigmy_encampment/road/corner_2"), holder), 1),
                Pair.of(StructurePoolElement.single(prefix("pigmy_encampment/road/crossroad_1"), holder), 1),
                Pair.of(StructurePoolElement.single(prefix("pigmy_encampment/road/crossroad_2"), holder), 1),
                Pair.of(StructurePoolElement.single(prefix("pigmy_encampment/road/straight_01"), holder), 1),
                Pair.of(StructurePoolElement.single(prefix("pigmy_encampment/road/straight_02"), holder), 1)),
                StructureTemplatePool.Projection.RIGID));
        bootstapContext.register(TENT_ROAD, new StructureTemplatePool(holder2, ImmutableList.of(
                Pair.of(StructurePoolElement.single(prefix("pigmy_encampment/tent_road/straight_1"), holder), 1)),
                StructureTemplatePool.Projection.RIGID));

        bootstapContext.register(STATUE, new StructureTemplatePool(holder2, ImmutableList.of(
                Pair.of(StructurePoolElement.single(prefix("pigmy_encampment/statue/ee_babyghast"), holder), 1),
                Pair.of(StructurePoolElement.single(prefix("pigmy_encampment/statue/ee_bagu"), holder), 1),
                Pair.of(StructurePoolElement.single(prefix("pigmy_encampment/statue/ee_blackgear"), holder), 1),
                Pair.of(StructurePoolElement.single(prefix("pigmy_encampment/statue/ee_clover"), holder), 1),
                Pair.of(StructurePoolElement.single(prefix("pigmy_encampment/statue/ee_clover2"), holder), 1),
                Pair.of(StructurePoolElement.single(prefix("pigmy_encampment/statue/ee_rotten"), holder), 1),
                Pair.of(StructurePoolElement.single(prefix("pigmy_encampment/statue/ee_rush"), holder), 1),
                Pair.of(StructurePoolElement.single(prefix("pigmy_encampment/statue/ee_statue"), holder), 1),
                Pair.of(StructurePoolElement.single(prefix("pigmy_encampment/statue/ee_statue2"), holder), 1)),
                StructureTemplatePool.Projection.RIGID));
        bootstapContext.register(MISC, new StructureTemplatePool(holder2, ImmutableList.of(
                Pair.of(StructurePoolElement.single(prefix("pigmy_encampment/hut/pigmy_tower1"), holder), 1),
                Pair.of(StructurePoolElement.single(prefix("pigmy_encampment/hut/pigmy_tower2"), holder), 1),
                Pair.of(StructurePoolElement.single(prefix("pigmy_encampment/hut/cooker_1"), holder), 1),
                Pair.of(StructurePoolElement.single(prefix("pigmy_encampment/hut/cooker_2"), holder), 1)),
                StructureTemplatePool.Projection.RIGID));
        bootstapContext.register(PILLAR, new StructureTemplatePool(holder2, ImmutableList.of(
                Pair.of(StructurePoolElement.single(prefix("pigmy_encampment/pillar/bone_spire1"), holder), 1),
                Pair.of(StructurePoolElement.single(prefix("pigmy_encampment/pillar/bone_spire2"), holder), 1),
                Pair.of(StructurePoolElement.single(prefix("pigmy_encampment/pillar/bone_spire3"), holder), 1),
                Pair.of(StructurePoolElement.single(prefix("pigmy_encampment/pillar/bone_spire4"), holder), 1),
                Pair.of(StructurePoolElement.single(prefix("pigmy_encampment/pillar/bone_spire5"), holder), 1),
                Pair.of(StructurePoolElement.single(prefix("pigmy_encampment/pillar/bone_spire6"), holder), 1),
                Pair.of(StructurePoolElement.single(prefix("pigmy_encampment/pillar/spire1"), holder), 1),
                Pair.of(StructurePoolElement.single(prefix("pigmy_encampment/pillar/spire2"), holder), 1),
                Pair.of(StructurePoolElement.single(prefix("pigmy_encampment/pillar/spire3"), holder), 1),
                Pair.of(StructurePoolElement.single(prefix("pigmy_encampment/pillar/spire4"), holder), 1),
                Pair.of(StructurePoolElement.single(prefix("pigmy_encampment/pillar/spire5"), holder), 1),
                Pair.of(StructurePoolElement.single(prefix("pigmy_encampment/pillar/spire6"), holder), 1),
                Pair.of(StructurePoolElement.single(prefix("pigmy_encampment/pillar/spire7"), holder), 1),
                Pair.of(StructurePoolElement.single(prefix("pigmy_encampment/pillar/spire8"), holder), 1),
                Pair.of(StructurePoolElement.single(prefix("pigmy_encampment/pillar/spire9"), holder), 1),
                Pair.of(StructurePoolElement.single(prefix("pigmy_encampment/pillar/spire10"), holder), 1),
                Pair.of(StructurePoolElement.single(prefix("pigmy_encampment/pillar/spire11"), holder), 1),
                Pair.of(StructurePoolElement.single(prefix("pigmy_encampment/pillar/spire12"), holder), 1),
                Pair.of(StructurePoolElement.single(prefix("pigmy_encampment/pillar/spire13"), holder), 1),
                Pair.of(StructurePoolElement.single(prefix("pigmy_encampment/pillar/spire14"), holder), 1),
                Pair.of(StructurePoolElement.single(prefix("pigmy_encampment/pillar/spire15"), holder), 1),
                Pair.of(StructurePoolElement.single(prefix("pigmy_encampment/pillar/spire16"), holder), 1),
                Pair.of(StructurePoolElement.single(prefix("pigmy_encampment/pillar/spire17"), holder), 1),
                Pair.of(StructurePoolElement.single(prefix("pigmy_encampment/pillar/spire18"), holder), 1),
                Pair.of(StructurePoolElement.single(prefix("pigmy_encampment/pillar/spire19"), holder), 1),
                Pair.of(StructurePoolElement.single(prefix("pigmy_encampment/pillar/spire20"), holder), 1),
                Pair.of(StructurePoolElement.single(prefix("pigmy_encampment/pillar/spire21"), holder), 1),
                Pair.of(StructurePoolElement.single(prefix("pigmy_encampment/pillar/spire22"), holder), 1),
                Pair.of(StructurePoolElement.single(prefix("pigmy_encampment/pillar/spire23"), holder), 1),
                Pair.of(StructurePoolElement.single(prefix("pigmy_encampment/pillar/spire24"), holder), 1)
        ), StructureTemplatePool.Projection.RIGID));
        bootstapContext.register(WALL, new StructureTemplatePool(holder2, ImmutableList.of(
                Pair.of(StructurePoolElement.single(prefix("pigmy_encampment/wall/encampment_wall1"), holder), 1),
                Pair.of(StructurePoolElement.single(prefix("pigmy_encampment/wall/encampment_wall2"), holder), 1),
                Pair.of(StructurePoolElement.single(prefix("pigmy_encampment/wall/encampment_wall3"), holder), 1),
                Pair.of(StructurePoolElement.single(prefix("pigmy_encampment/wall/encampment_wall4"), holder), 1)),
                StructureTemplatePool.Projection.RIGID));


        bootstapContext.register(MOB_HOGGART, new StructureTemplatePool(holder2, ImmutableList.of(
                Pair.of(StructurePoolElement.single(prefix("pigmy_encampment/mob/hoggart"), holder), 1)
        ), StructureTemplatePool.Projection.RIGID));
        bootstapContext.register(MOB_HOGLET, new StructureTemplatePool(holder2, ImmutableList.of(
                Pair.of(StructurePoolElement.single(prefix("pigmy_encampment/mob/hoglet"), holder), 1)
        ), StructureTemplatePool.Projection.RIGID));
        bootstapContext.register(MOB_HOGLIN, new StructureTemplatePool(holder2, ImmutableList.of(
                Pair.of(StructurePoolElement.single(prefix("pigmy_encampment/mob/hoglin"), holder), 4),
                Pair.of(StructurePoolElement.single(prefix("pigmy_encampment/mob/hoglin_armor"), holder), 1)
        ), StructureTemplatePool.Projection.RIGID));
        bootstapContext.register(MOB_PIGMY, new StructureTemplatePool(holder2, ImmutableList.of(
                Pair.of(StructurePoolElement.single(prefix("pigmy_encampment/mob/pigmy"), holder), 1)
        ), StructureTemplatePool.Projection.RIGID));
    }

    public static String prefix(String s) {
        return GreedAndBleed.MOD_ID + ":" + s;
    }
}
