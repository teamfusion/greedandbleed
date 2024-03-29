package com.github.teamfusion.greedandbleed.common;

import com.github.teamfusion.greedandbleed.common.entity.ToleratingMount;
import com.github.teamfusion.greedandbleed.common.entity.piglin.Hoglet;
import com.github.teamfusion.greedandbleed.common.entity.piglin.ShamanPiglin;
import com.github.teamfusion.greedandbleed.common.entity.piglin.SkeletalPiglin;
import com.github.teamfusion.greedandbleed.common.entity.piglin.pygmy.Hoggart;
import com.github.teamfusion.greedandbleed.common.entity.piglin.pygmy.Pygmy;
import com.github.teamfusion.greedandbleed.common.item.slingshot.BuckshotSlingshotBehavior;
import com.github.teamfusion.greedandbleed.common.item.slingshot.SlingshotBehavior;
import com.github.teamfusion.greedandbleed.common.item.slingshot.SlingshotItem;
import com.github.teamfusion.greedandbleed.common.network.GreedAndBleedNetwork;
import com.github.teamfusion.greedandbleed.common.network.GreedAndBleedServerNetwork;
import com.github.teamfusion.greedandbleed.common.registry.EnchantmentRegistry;
import com.github.teamfusion.greedandbleed.common.registry.EntityTypeRegistry;
import com.github.teamfusion.greedandbleed.common.registry.ItemRegistry;
import com.github.teamfusion.greedandbleed.common.registry.PotionRegistry;
import com.github.teamfusion.greedandbleed.platform.common.MobRegistry;
import com.github.teamfusion.greedandbleed.platform.common.worldgen.BiomeManager;
import dev.architectury.event.events.common.LootEvent;
import dev.architectury.registry.level.biome.BiomeModifications;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.projectile.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.storage.loot.LootDataManager;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.EnchantRandomlyFunction;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class CommonSetup {
    public static void onBootstrap() {
        MobRegistry.attributes(EntityTypeRegistry.SKELETAL_PIGLIN, SkeletalPiglin::setCustomAttributes);
        MobRegistry.attributes(EntityTypeRegistry.HOGLET, Hoglet::setCustomAttributes);
        MobRegistry.attributes(EntityTypeRegistry.SKOGLET, Hoglet::setCustomAttributes);
        MobRegistry.attributes(EntityTypeRegistry.ZOGLET, Hoglet::setCustomAttributes);
        MobRegistry.attributes(EntityTypeRegistry.SHAMAN_PIGLIN, ShamanPiglin::setCustomAttributes);
        MobRegistry.attributes(EntityTypeRegistry.PYGMY, Pygmy::setCustomAttributes);
        MobRegistry.attributes(EntityTypeRegistry.HOGGART, Hoggart::setCustomAttributes);
    }

    public static void onAmmoInit() {
        SlingshotItem.registerAmmo(Items.EGG, new SlingshotBehavior());
        SlingshotItem.registerAmmo(Items.SNOWBALL, new SlingshotBehavior());
        SlingshotItem.registerAmmo(ItemRegistry.CRIMSON_FUNGUS.get(), new SlingshotBehavior());
        SlingshotItem.registerAmmo(ItemRegistry.PEBBLE.get(), new SlingshotBehavior());
        SlingshotItem.registerAmmo(Items.POISONOUS_POTATO, new SlingshotBehavior());
        SlingshotItem.registerAmmo(Items.PUFFERFISH, new SlingshotBehavior());
        SlingshotItem.registerAmmo(Items.SPIDER_EYE, new SlingshotBehavior());
        SlingshotItem.registerAmmo(Items.IRON_NUGGET, new SlingshotBehavior());
        SlingshotItem.registerAmmo(Items.GOLD_NUGGET, new SlingshotBehavior());
        SlingshotItem.registerAmmo(Items.RAW_IRON, new SlingshotBehavior());
        SlingshotItem.registerAmmo(Items.RAW_GOLD, new SlingshotBehavior());
        SlingshotItem.registerAmmo(Items.RAW_COPPER, new SlingshotBehavior());
        SlingshotItem.registerAmmo(Items.IRON_INGOT, new SlingshotBehavior());
        SlingshotItem.registerAmmo(Items.GOLD_INGOT, new SlingshotBehavior());
        SlingshotItem.registerAmmo(Items.COPPER_INGOT, new SlingshotBehavior());
        SlingshotItem.registerAmmo(Items.NETHERITE_INGOT, new SlingshotBehavior());
        SlingshotItem.registerAmmo(Items.DIAMOND, new SlingshotBehavior());
        SlingshotItem.registerAmmo(Items.EMERALD, new SlingshotBehavior());
        SlingshotItem.registerAmmo(Items.BRICK, new SlingshotBehavior());
        SlingshotItem.registerAmmo(Items.NETHER_BRICK, new SlingshotBehavior());
        SlingshotItem.registerAmmo(Items.BEETROOT_SEEDS, new BuckshotSlingshotBehavior());
        SlingshotItem.registerAmmo(Items.MELON_SEEDS, new BuckshotSlingshotBehavior());
        SlingshotItem.registerAmmo(Items.PUMPKIN_SEEDS, new BuckshotSlingshotBehavior());
        SlingshotItem.registerAmmo(Items.WHEAT_SEEDS, new BuckshotSlingshotBehavior());
        SlingshotItem.registerAmmo(Items.SPLASH_POTION, new SlingshotBehavior() {
            @Override
            public float getXRot() {
                return -10F;
            }

            @Override
            public float getMaxPower() {
                return 1.5F;
            }

            @Override
            public Projectile getProjectile(Level level, BlockPos pos, LivingEntity shooter, ItemStack stack, float power) {
                return new ThrownPotion(level, shooter);
            }
        });
        SlingshotItem.registerAmmo(Items.EXPERIENCE_BOTTLE, new SlingshotBehavior() {
            @Override
            public float getXRot() {
                return -10F;
            }

            @Override
            public float getMaxPower() {
                return 1.5F;
            }

            @Override
            public Projectile getProjectile(Level level, BlockPos pos, LivingEntity shooter, ItemStack stack, float power) {
                return new ThrownExperienceBottle(level, shooter);
            }
        });
        SlingshotItem.registerAmmo(Items.FIRE_CHARGE, new SlingshotBehavior() {
            @Override
            public Projectile getProjectile(Level level, BlockPos pos, LivingEntity shooter, ItemStack stack, float power) {
                LargeFireball largeFireball = new LargeFireball(EntityType.FIREBALL, level);
                Vec3 vec3 = shooter.getViewVector(1.0F);
                largeFireball.xPower = vec3.x * 0.05F * power;
                largeFireball.yPower = -0.03F;
                largeFireball.zPower = vec3.z * 0.05F * power;
                largeFireball.setPos(shooter.getX(), shooter.getEyeY(), shooter.getZ());
                return largeFireball;
            }
        });
        SlingshotItem.registerAmmo(Items.LINGERING_POTION, new SlingshotBehavior() {
            @Override
            public float getXRot() {
                return -10F;
            }

            @Override
            public float getMaxPower() {
                return 1.5F;
            }

            @Override
            public Projectile getProjectile(Level level, BlockPos pos, LivingEntity shooter, ItemStack stack, float power) {
                return new ThrownPotion(level, shooter);
            }
        });

        SlingshotItem.registerAmmo(Items.ENDER_PEARL, new SlingshotBehavior() {

            @Override
            public Projectile getProjectile(Level level, BlockPos pos, LivingEntity shooter, ItemStack stack, float power) {
                return new ThrownEnderpearl(level, shooter);
            }
        });
    }

    public static void onInitialized() {
        onAmmoInit();

        PotionRegistry.init();
        BiomeManager.registrySpawnPlacement(EntityTypeRegistry.HOGLET.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Hoglet::checkHogletSpawnRules);
        BiomeManager.registrySpawnPlacement(EntityTypeRegistry.ZOGLET.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
        BiomeManager.registrySpawnPlacement(EntityTypeRegistry.SKOGLET.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
        BiomeManager.registrySpawnPlacement(EntityTypeRegistry.SKELETAL_PIGLIN.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
        BiomeManager.registrySpawnPlacement(EntityTypeRegistry.SHAMAN_PIGLIN.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
        BiomeManager.registrySpawnPlacement(EntityTypeRegistry.HOGGART.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
        BiomeManager.registrySpawnPlacement(EntityTypeRegistry.PYGMY.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);

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
        LootEvent.MODIFY_LOOT_TABLE.register(new LootEvent.ModifyLootTable() {
            LootPool.Builder pool = LootPool.lootPool().add(
                    LootItem.lootTableItem(ItemRegistry.PIGLIN_BELT.get()));

            @Override
            public void modifyLootTable(@Nullable LootDataManager lootDataManager, ResourceLocation id, LootEvent.LootTableModificationContext context, boolean builtin) {
                if (id.equals(new ResourceLocation("entities/piglin"))) {
                    context.addPool(pool);
                }
            }
        });

        BiomeManager.setup();
        GreedAndBleedNetwork.registerReceivers();
        GreedAndBleedServerNetwork.registerReceivers();
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