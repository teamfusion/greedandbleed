package com.github.teamfusion.greedandbleed.data.worldgen.structure;

import com.github.teamfusion.greedandbleed.data.worldgen.structure.piece.PigmyEncampmentPieces;
import com.github.teamfusion.greedandbleed.data.worldgen.structure.piece.ShamanBasePieces;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

public class ModStructureTemplatePools {
    public static void bootstrap(BootstapContext<StructureTemplatePool> bootstapContext) {
        ShamanBasePieces.bootstrap(bootstapContext);
        PigmyEncampmentPieces.bootstrap(bootstapContext);
    }
}
