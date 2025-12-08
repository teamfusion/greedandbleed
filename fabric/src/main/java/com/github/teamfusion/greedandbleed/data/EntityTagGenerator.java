package com.github.teamfusion.greedandbleed.data;

import com.github.teamfusion.greedandbleed.common.registry.EntityTypeRegistry;
import com.github.teamfusion.greedandbleed.common.registry.GBEntityTypeTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;

import java.util.concurrent.CompletableFuture;

public class EntityTagGenerator extends FabricTagProvider<EntityType<?>> {
    public EntityTagGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, Registries.ENTITY_TYPE, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider arg) {
        this.getOrCreateTagBuilder(GBEntityTypeTags.WOE_OF_SWINES_TARGET).add(EntityTypeRegistry.HOGGART.get()).add(EntityTypeRegistry.HOGLET.get())
                .add(EntityTypeRegistry.SKELETON_HOGLET.get()).add(EntityTypeRegistry.SKELETAL_PIGLIN.get()).add(EntityTypeRegistry.ZOMBIFIED_HOGLET.get()).add(EntityTypeRegistry.ZOMBIFIED_PIGMIES.get())
                .add(EntityTypeRegistry.SHAMAN_PIGLIN.get()).add(EntityTypeRegistry.WARPED_PIGLIN.get())
                .add(EntityTypeRegistry.PIGMY.get()).add(EntityTypeRegistry.SHRYGMY.get())
                .add(EntityTypeRegistry.HOGLET.get()).add(EntityType.PIG).add(EntityType.PIGLIN).add(EntityType.PIGLIN_BRUTE).add(EntityType.ZOMBIFIED_PIGLIN);
    }
}
