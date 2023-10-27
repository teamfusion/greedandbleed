package com.github.teamfusion.greedandbleed.common.item;

import com.github.teamfusion.greedandbleed.common.entity.TraceAndSetOwner;
import com.github.teamfusion.greedandbleed.common.entity.piglin.SkeletalPiglin;
import com.github.teamfusion.greedandbleed.common.registry.EntityTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class AmuletItem extends Item {
    public AmuletItem(Item.Properties properties) {
        super(properties);
    }


    @Override
    public boolean useOnRelease(ItemStack itemStack) {
        return itemStack.is(this);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack itemStack) {
        return UseAnim.BOW;
    }

    @Override
    public int getUseDuration(ItemStack itemStack) {
        return 63;
    }


    @Override
    public void releaseUsing(ItemStack itemStack, Level level, LivingEntity livingEntity, int time) {
        float f = (float) (itemStack.getUseDuration() - time) / 60;
        if (f >= 0.8F) {
            if (livingEntity instanceof Player player) {
                HitResult hitResult = this.calculateHitResult(livingEntity);
                if (hitResult instanceof BlockHitResult blockHitResult) {
                    BlockPos blockPos = blockHitResult.getBlockPos();
                    BlockPos blockPos2 = blockPos.relative(blockHitResult.getDirection());
                    ItemStack itemStack2 = getSoulSand(player);
                    if (!itemStack2.isEmpty()) {
                        EntityType<?> entityType = getMobAndConsume(level, player, itemStack2);
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
                                    if (mob instanceof SkeletalPiglin skeletalPiglin) {
                                        if (level.random.nextBoolean()) {
                                            mob.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.GOLDEN_SWORD));
                                        } else {
                                            mob.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.CROSSBOW));
                                        }
                                    }
                                    if (mob instanceof TraceAndSetOwner traceAndSetOwner) {
                                        traceAndSetOwner.setOwner(player);
                                    }
                                    serverLevel.addFreshEntity(mob);
                                }
                            }
                            player.getCooldowns().addCooldown(this, 80);
                        }

                    }

                }
            }
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack itemStack = player.getItemInHand(interactionHand);

        player.startUsingItem(interactionHand);
        return InteractionResultHolder.consume(itemStack);
    }

    private HitResult calculateHitResult(LivingEntity livingEntity) {
        return ProjectileUtil.getHitResultOnViewVector(livingEntity, entity -> !entity.isSpectator() && entity.isPickable(), 8.0F);
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
