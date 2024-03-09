package com.github.teamfusion.greedandbleed.common;

import com.github.teamfusion.greedandbleed.common.entity.ToleratingMount;
import com.github.teamfusion.greedandbleed.common.entity.piglin.Hoglet;
import com.github.teamfusion.greedandbleed.common.entity.piglin.ShamanPiglin;
import com.github.teamfusion.greedandbleed.common.entity.piglin.SkeletalPiglin;
import com.github.teamfusion.greedandbleed.common.network.GreedAndBleedNetwork;
import com.github.teamfusion.greedandbleed.common.registry.EnchantmentRegistry;
import com.github.teamfusion.greedandbleed.common.registry.EntityTypeRegistry;
import com.github.teamfusion.greedandbleed.common.registry.PotionRegistry;
import com.github.teamfusion.greedandbleed.platform.common.MobRegistry;
import com.github.teamfusion.greedandbleed.platform.common.worldgen.BiomeManager;
import dev.architectury.event.events.common.LootEvent;
import dev.architectury.registry.level.biome.BiomeModifications;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.storage.loot.LootDataManager;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.EnchantRandomlyFunction;
import org.jetbrains.annotations.Nullable;

public class CommonSetup {
    public static void onBootstrap() {
        MobRegistry.attributes(EntityTypeRegistry.SKELETAL_PIGLIN, SkeletalPiglin::setCustomAttributes);
        MobRegistry.attributes(EntityTypeRegistry.HOGLET, Hoglet::setCustomAttributes);
        MobRegistry.attributes(EntityTypeRegistry.SKOGLET, Hoglet::setCustomAttributes);
        MobRegistry.attributes(EntityTypeRegistry.ZOGLET, Hoglet::setCustomAttributes);
        MobRegistry.attributes(EntityTypeRegistry.SHAMAN_PIGLIN, ShamanPiglin::setCustomAttributes);
    }

    public static void onInitialized() {
        PotionRegistry.init();
        BiomeManager.registrySpawnPlacement(EntityTypeRegistry.HOGLET.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Hoglet::checkHogletSpawnRules);
        BiomeManager.registrySpawnPlacement(EntityTypeRegistry.ZOGLET.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
        BiomeManager.registrySpawnPlacement(EntityTypeRegistry.SKOGLET.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
        BiomeManager.registrySpawnPlacement(EntityTypeRegistry.SKELETAL_PIGLIN.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
        BiomeManager.registrySpawnPlacement(EntityTypeRegistry.SHAMAN_PIGLIN.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);

        BiomeModifications.addProperties((biomeContext, mutable) -> {
            if (Biomes.CRIMSON_FOREST.location() == biomeContext.getKey().get()) {
                mutable.getSpawnProperties().addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityTypeRegistry.HOGLET.get(), 8, 2, 4));
            }

            if (Biomes.SOUL_SAND_VALLEY.location() == biomeContext.getKey().get()) {
                mutable.getSpawnProperties().addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityTypeRegistry.SKELETAL_PIGLIN.get(), 10, 4, 5));
                mutable.getSpawnProperties().setSpawnCost(EntityTypeRegistry.SKELETAL_PIGLIN.get(), 0.7, 0.15);
            }
        });
        LootEvent.MODIFY_LOOT_TABLE.register(new LootEvent.ModifyLootTable() {
            LootPool.Builder pool = LootPool.lootPool().add(
                    LootItem.lootTableItem(Items.ENCHANTED_BOOK)
                            .apply(new EnchantRandomlyFunction.Builder().withEnchantment(EnchantmentRegistry.WOE_OF_SWINES.get()))
            );

            @Override
            public void modifyLootTable(@Nullable LootDataManager lootDataManager, ResourceLocation id, LootEvent.LootTableModificationContext context, boolean builtin) {
                if (id.equals(new ResourceLocation("chests/nether_bridge"))) {
                    context.addPool(pool);
                }
            }
        });

        BiomeManager.setup();
        GreedAndBleedNetwork.registerReceivers();
    }

    public static void onHoglinAttack(LivingEntity entity, DamageSource source) {
        if (source.getDirectEntity() instanceof ToleratingMount mount && !entity.level().isClientSide) {
            if (mount.getTolerance() > 0) {
                mount.addTolerance(-1);
            } else if (mount.getTolerance() < 0) {
                mount.setTolerance(0);
            }
        }
    }

    public static void onHoglinUpdate(LivingEntity entity) {
        if (entity instanceof ToleratingMount mount && !entity.level().isClientSide) {
            if (mount.getTolerance() > 0) {
                mount.addTolerance(-1);
            } else if (mount.getTolerance() < 0) {
                mount.setTolerance(0);
            }
        }
    }
}