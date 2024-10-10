package com.github.teamfusion.greedandbleed.common.item;

import com.github.teamfusion.greedandbleed.common.entity.IConvertToNormal;
import com.github.teamfusion.greedandbleed.common.entity.TraceAndSetOwner;
import com.github.teamfusion.greedandbleed.common.registry.EntityTypeRegistry;
import com.github.teamfusion.greedandbleed.common.registry.PotionRegistry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

import static net.minecraft.world.item.BowItem.getPowerForTime;

public class AmuletItem extends Item {
    public AmuletItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack itemStack = player.getItemInHand(interactionHand);
        boolean bl = !player.getProjectile(itemStack).isEmpty();
        if (!player.getAbilities().instabuild && !bl) {
            return InteractionResultHolder.fail(itemStack);
        } else {
            player.startUsingItem(interactionHand);
            return InteractionResultHolder.consume(itemStack);
        }
    }

    public int getUseDuration(ItemStack itemStack) {
        return 72000;
    }

    public UseAnim getUseAnimation(ItemStack itemStack) {
        return UseAnim.BOW;
    }

    @Override
    public void releaseUsing(ItemStack itemStack, Level level, LivingEntity livingEntity, int i) {
        super.releaseUsing(itemStack, level, livingEntity, i);

        int j = this.getUseDuration(itemStack) - i;

        float f = getPowerForTime(j);
        if (livingEntity instanceof Player player) {
            ItemStack itemStack2 = getSoulSand(player);
            if (!itemStack2.isEmpty()) {
                EntityType<?> entityType = getMobAndConsume(level, player, itemStack2, f);
                if (entityType != null) {
                    if (level.isClientSide) {
                        for (int i2 = 0; i2 < 8; i2++) {
                            level.addParticle(ParticleTypes.SOUL_FIRE_FLAME, livingEntity.getX() + Mth.nextFloat(level.random, -0.5F, 0.5F), livingEntity.getY() + Mth.nextFloat(level.random, 0F, 3.0F), livingEntity.getZ() + Mth.nextFloat(level.random, -0.5F, 0.5F), 0, 0, 0);
                        }
                    } else {
                        Entity entity = entityType.create(level);
                        if (entity instanceof Mob mob && level instanceof ServerLevel serverLevel) {
                            mob.setPos(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ());
                            mob.finalizeSpawn(serverLevel, level.getCurrentDifficultyAt(player.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                            if (mob instanceof TraceAndSetOwner traceAndSetOwner) {
                                traceAndSetOwner.setOwner(livingEntity);
                            }
                            mob.setPose(Pose.EMERGING);

                            if (mob instanceof PathfinderMob pathfinderMob) {
                                pathfinderMob.getBrain().setActiveActivityIfPossible(Activity.EMERGE);
                            }

                            serverLevel.addFreshEntity(mob);
                        }
                    }
                    player.getCooldowns().addCooldown(this, 80);
                }

            }

        }
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
    public EntityType<?> getMobAndConsume(Level level, Player player, ItemStack stack, float i) {
        if (stack.getCount() >= 64 && i >= 1.0F) {
            if (!player.getAbilities().instabuild) {
                stack.shrink(64);
                player.getCooldowns().addCooldown(this, 80);
                if (stack.isEmpty()) {
                    player.getInventory().removeItem(stack);
                }
            }
            return level.dimension() == Level.NETHER ? EntityType.ZOGLIN : EntityType.SKELETON;
        } else if (stack.getCount() >= 32 && i >= 0.5F) {
            if (!player.getAbilities().instabuild) {
                stack.shrink(32);
                player.getCooldowns().addCooldown(this, 80);
                if (stack.isEmpty()) {
                    player.getInventory().removeItem(stack);
                }
            }
            return level.dimension() == Level.NETHER ? EntityType.ZOMBIFIED_PIGLIN : EntityType.SKELETON;

        } else if (stack.getCount() >= 16) {
            if (!player.getAbilities().instabuild) {
                stack.shrink(16);
                player.getCooldowns().addCooldown(this, 80);
                if (stack.isEmpty()) {
                    player.getInventory().removeItem(stack);
                }
            }
            return level.dimension() == Level.NETHER ? EntityTypeRegistry.SKELETAL_PIGLIN.get() : EntityType.ZOMBIE;

        }
        return null;
    }
}
