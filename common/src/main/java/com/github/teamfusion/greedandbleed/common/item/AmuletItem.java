package com.github.teamfusion.greedandbleed.common.item;

import com.github.teamfusion.greedandbleed.common.entity.IConvertToNormal;
import com.github.teamfusion.greedandbleed.common.entity.TraceAndSetOwner;
import com.github.teamfusion.greedandbleed.common.registry.EntityTypeRegistry;
import com.github.teamfusion.greedandbleed.common.registry.PotionRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class AmuletItem extends Item {
    public AmuletItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext useOnContext) {
        Player player = useOnContext.getPlayer();
        Level level = useOnContext.getLevel();
        BlockPos blockPos = useOnContext.getClickedPos();
        BlockPos blockPos2 = blockPos.relative(useOnContext.getClickedFace());
        if (player != null) {
            ItemStack itemStack = getSoulSand(player);
            if (!itemStack.isEmpty()) {
                EntityType<?> entityType = getMobAndConsume(level, player, itemStack);
                if (entityType != null) {
                    if (level.isClientSide) {
                        for (int i = 0; i < 8; i++) {
                            level.addParticle(ParticleTypes.SOUL_FIRE_FLAME, blockPos2.getX() + Mth.nextFloat(level.random, -0.5F, 0.5F), blockPos2.getY() + Mth.nextFloat(level.random, 0F, 3.0F), blockPos2.getZ() + Mth.nextFloat(level.random, -0.5F, 0.5F), 0, 0, 0);
                        }
                    } else {
                        Entity entity = entityType.create(level);
                        if (entity instanceof Mob mob && level instanceof ServerLevel serverLevel) {
                            mob.setPos(blockPos2.getX(), blockPos2.getY(), blockPos2.getZ());
                            mob.finalizeSpawn(serverLevel, level.getCurrentDifficultyAt(blockPos2), MobSpawnType.MOB_SUMMONED, null, null);
                            if (mob instanceof TraceAndSetOwner traceAndSetOwner) {
                                traceAndSetOwner.setOwner(player);
                            }
                            mob.setPose(Pose.EMERGING);

                            if (mob instanceof PathfinderMob pathfinderMob) {
                                pathfinderMob.getBrain().setActiveActivityIfPossible(Activity.EMERGE);
                            }

                            serverLevel.addFreshEntity(mob);
                        }
                    }
                    player.getCooldowns().addCooldown(this, 80);
                    return InteractionResult.sidedSuccess(level.isClientSide);
                }

            }

        }
        return super.useOn(useOnContext);
    }


    @Override
    public InteractionResult interactLivingEntity(ItemStack itemStack, Player player, LivingEntity livingEntity, InteractionHand interactionHand) {
        if (livingEntity instanceof IConvertToNormal convertToNormal) {
            if (livingEntity.hasEffect(PotionRegistry.IMMUNITY.get()) && livingEntity.getEffect(PotionRegistry.IMMUNITY.get()).getAmplifier() > 0) {
                if (!convertToNormal.gb$hasCorrectConvert()) {
                    convertToNormal.gb$setCanConvertToNormal(true);
                }
                player.getCooldowns().addCooldown(this, 80);
                return InteractionResult.sidedSuccess(player.level().isClientSide);
            }
        }
        return super.interactLivingEntity(itemStack, player, livingEntity, interactionHand);
    }

    public ItemStack getSoulSand(Player player) {
        for (int i = 0; i < player.getInventory().getContainerSize(); ++i) {
            ItemStack itemStack3 = player.getInventory().getItem(i);
            if (!itemStack3.is(Items.SOUL_SAND)) continue;
            if (itemStack3.is(Items.SOUL_SAND) && itemStack3.getCount() < 16) continue;
            return itemStack3;
        }
        return ItemStack.EMPTY;
    }

    //Check the mob anc consume
    public EntityType<?> getMobAndConsume(Level level, Player player, ItemStack stack) {
        if (stack.getCount() >= 64) {
            if (!player.getAbilities().instabuild) {
                stack.shrink(64);
                if (stack.isEmpty()) {
                    player.getInventory().removeItem(stack);
                }
            }
            return player.getRandom().nextBoolean() ? EntityType.ZOGLIN : EntityType.SKELETON;
        } else if (stack.getCount() >= 32) {
            if (!player.getAbilities().instabuild) {
                stack.shrink(32);
                if (stack.isEmpty()) {
                    player.getInventory().removeItem(stack);
                }
            }
            return player.getRandom().nextBoolean() ? EntityType.ZOMBIFIED_PIGLIN : EntityType.ZOMBIE;
        } else if (stack.getCount() >= 16) {
            if (!player.getAbilities().instabuild) {
                stack.shrink(16);
                if (stack.isEmpty()) {
                    player.getInventory().removeItem(stack);
                }
            }
            return EntityTypeRegistry.SKELETAL_PIGLIN.get();
        }
        return null;
    }
}
