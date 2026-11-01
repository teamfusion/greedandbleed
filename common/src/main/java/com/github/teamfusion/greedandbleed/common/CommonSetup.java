package com.github.teamfusion.greedandbleed.common;

import com.blackgear.platform.common.integration.MobIntegration;
import com.blackgear.platform.common.worldgen.modifier.BiomeManager;
import com.blackgear.platform.core.ParallelDispatch;
import com.github.teamfusion.greedandbleed.common.entity.ToleratingMount;
import com.github.teamfusion.greedandbleed.common.integrations.MobIntegrations;
import com.github.teamfusion.greedandbleed.common.integrations.WorldIntegration;
import com.github.teamfusion.greedandbleed.common.item.slingshot.BuckshotSlingshotBehavior;
import com.github.teamfusion.greedandbleed.common.item.slingshot.SlingshotBehavior;
import com.github.teamfusion.greedandbleed.common.item.slingshot.SlingshotItem;
import com.github.teamfusion.greedandbleed.common.network.GreedAndBleedNetwork;
import com.github.teamfusion.greedandbleed.common.network.GreedAndBleedServerNetwork;
import com.github.teamfusion.greedandbleed.common.registry.*;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.InteractionEvent;
import dev.architectury.event.events.common.LootEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.loot.LootDataManager;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.EnchantRandomlyFunction;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class CommonSetup {
    public static void onBootstrap() {
        MobIntegration.registerIntegrations(MobIntegrations::bootstrap);
    }
    
    public static void onInitialized(ParallelDispatch dispatch) {
        onAmmoInit();
        
        PotionRegistry.init();
        BiomeManager.add(WorldIntegration::bootstrap); //TODO
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
        
        InteractionEvent.RIGHT_CLICK_BLOCK.register((player, hand, blockPos, direction) -> {
            ItemStack stack = player.getItemInHand(hand);
            Level level = player.level();
            if (stack.getItem() instanceof ShovelItem) {
                BlockState state = Blocks.AIR.defaultBlockState();
                
                if (player.level().getBlockState(blockPos).is(Blocks.CRIMSON_NYLIUM)) {
                    state = BlockRegistry.CRIMSON_NYLIUM_PATH.get().defaultBlockState();
                    
                }
                if (player.level().getBlockState(blockPos).is(Blocks.WARPED_NYLIUM)) {
                    state = BlockRegistry.WARPED_NYLIUM_PATH.get().defaultBlockState();
                    
                }
                if (player.level().getBlockState(blockPos).is(BlockRegistry.HOGDEW_NYLIUM.get())) {
                    state = BlockRegistry.HOGDEW_NYLIUM_PATH.get().defaultBlockState();
                }
                if (!state.isAir()) {
                    level.playSound(player, blockPos, SoundEvents.SHOVEL_FLATTEN, SoundSource.BLOCKS, 1.0F, 1.0F);
                    
                    level.setBlock(blockPos, state, 11);
                    level.gameEvent(GameEvent.BLOCK_CHANGE, blockPos, GameEvent.Context.of(player, state));
                    if (player != null) {
                        stack.hurtAndBreak(1, player, (playerx) -> playerx.broadcastBreakEvent(hand));
                    }
                    return EventResult.interruptTrue();
                }
            }
            return EventResult.pass();
        });
        
        GreedAndBleedNetwork.registerReceivers();
        GreedAndBleedServerNetwork.registerReceivers();
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