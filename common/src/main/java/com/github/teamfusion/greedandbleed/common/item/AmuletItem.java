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
        boolean bl = player.experienceLevel > 0;
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
            int experienceLevel = player.experienceLevel;
            if (experienceLevel > 0) {
                EntityType<?> entityType = getMobAndConsume(level, player, experienceLevel, f);
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

    //Check the mob anc consume
    public EntityType<?> getMobAndConsume(Level level, Player player, int xp, float i) {
        if ((xp >= 3 || player.getAbilities().instabuild) && i >= 1.0F) {
            if (!player.getAbilities().instabuild) {
                player.giveExperienceLevels(-3);
                player.getCooldowns().addCooldown(this, 80);
            }
            return level.dimension() == Level.NETHER ? EntityType.ZOGLIN : level.getRandom().nextBoolean() ? EntityType.STRAY : EntityType.HUSK;
        } else if ((xp >= 2 || player.getAbilities().instabuild) && i >= 0.5F) {
            if (!player.getAbilities().instabuild) {
                player.giveExperienceLevels(-2);
                player.getCooldowns().addCooldown(this, 80);
            }
            return level.dimension() == Level.NETHER ? EntityType.ZOMBIFIED_PIGLIN : EntityType.SKELETON;

        } else if (xp >= 1 || player.getAbilities().instabuild) {
            if (!player.getAbilities().instabuild) {
                player.giveExperienceLevels(-1);
                player.getCooldowns().addCooldown(this, 80);
            }
            return level.dimension() == Level.NETHER ? EntityTypeRegistry.SKELETAL_PIGLIN.get() : EntityType.ZOMBIE;

        }
        return null;
    }
}
