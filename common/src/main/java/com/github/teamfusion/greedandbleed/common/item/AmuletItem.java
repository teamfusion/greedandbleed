package com.github.teamfusion.greedandbleed.common.item;

import com.github.teamfusion.greedandbleed.common.entity.IConvertToNormal;
import com.github.teamfusion.greedandbleed.common.entity.TraceAndSetOwner;
import com.github.teamfusion.greedandbleed.common.entity.piglin.pygmy.GBPygmy;
import com.github.teamfusion.greedandbleed.common.registry.EntityTypeRegistry;
import com.github.teamfusion.greedandbleed.common.registry.ItemRegistry;
import com.github.teamfusion.greedandbleed.common.registry.PotionRegistry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import static net.minecraft.world.item.BowItem.getPowerForTime;

public class AmuletItem extends Item {
    public AmuletItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack itemStack = player.getItemInHand(interactionHand);
        int soulsand = ContainerHelper.clearOrCountMatchingItems(player.getInventory(), predicate -> {
            return predicate.is(Items.SOUL_SAND) || predicate.is(Items.SOUL_SOIL);
        }, 1, true);
        boolean bl = player.experienceLevel > 0 || soulsand > 0;
        if (!player.getAbilities().instabuild && !bl) {
            return InteractionResultHolder.fail(itemStack);
        } else if (level.clip(new ClipContext(player.getEyePosition(), player.getEyePosition().add(player.getLookAngle().scale(8.0F)), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player)).getType() == HitResult.Type.BLOCK) {
            player.startUsingItem(interactionHand);
            return InteractionResultHolder.consume(itemStack);
        }
        return InteractionResultHolder.fail(itemStack);
    }

    public int getUseDuration(ItemStack itemStack) {
        return 72000 * 2;
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
            HitResult hitResult = level.clip(new ClipContext(player.getEyePosition(), player.getEyePosition().add(player.getLookAngle().scale(8.0F)), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player));
            int experienceLevel = player.experienceLevel;
            int soulsand = ContainerHelper.clearOrCountMatchingItems(player.getInventory(), predicate -> {
                return predicate.is(Items.SOUL_SAND) || predicate.is(Items.SOUL_SOIL);
            }, 0, true);
            if ((experienceLevel > 0 || soulsand > 0) && hitResult.getType() == HitResult.Type.BLOCK) {
                Vec3 vec3 = hitResult.getLocation();
                EntityType<?> entityType = getMobAndConsume(level, player, experienceLevel, soulsand, f);
                if (entityType != null) {
                    if (level.isClientSide) {
                        for (int i2 = 0; i2 < 8; i2++) {
                            level.addParticle(ParticleTypes.SOUL_FIRE_FLAME, vec3.x + Mth.nextFloat(level.random, -0.5F, 0.5F), vec3.y + Mth.nextFloat(level.random, 0F, 3.0F), vec3.z + Mth.nextFloat(level.random, -0.5F, 0.5F), 0, 0, 0);
                        }
                    } else {
                        Entity entity = entityType.create(level);
                        if (entity instanceof Mob mob && level instanceof ServerLevel serverLevel) {
                            mob.setPos(vec3.x, vec3.y, vec3.z);
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
            int experienceLevel = player.experienceLevel;
            if (experienceLevel >= 3 || player.getAbilities().mayfly) {
                if (livingEntity.hasEffect(PotionRegistry.IMMUNITY.get()) && livingEntity.getEffect(PotionRegistry.IMMUNITY.get()).getAmplifier() > 0) {
                    if (!convertToNormal.gb$hasCorrectConvert()) {
                        convertToNormal.gb$setCanConvertToNormal(true);

                        if (!player.getAbilities().mayfly) {
                            player.giveExperienceLevels(-3);
                        }
                        livingEntity.playSound(SoundEvents.ZOMBIE_VILLAGER_CURE);
                        player.getCooldowns().addCooldown(this, 80);
                        return InteractionResult.sidedSuccess(player.level().isClientSide);
                    }
                }
            }
        }
        int experienceLevel = player.experienceLevel;
        if (experienceLevel >= 3 || player.getAbilities().mayfly) {

            if (livingEntity instanceof AbstractPiglin gbPygmy) {
                if (!(gbPygmy).isImmuneToZombification() && itemStack.is(ItemRegistry.AMULET.get()) && gbPygmy.hasEffect(PotionRegistry.IMMUNITY.get()) && gbPygmy.getEffect(PotionRegistry.IMMUNITY.get()).getAmplifier() > 0) {
                    gbPygmy.setImmuneToZombification(true);
                    player.getCooldowns().addCooldown(this, 80);
                    if (!player.getAbilities().mayfly) {
                        player.giveExperienceLevels(-3);
                    }
                    livingEntity.playSound(SoundEvents.ZOMBIE_VILLAGER_CONVERTED);
                    return InteractionResult.sidedSuccess(player.level().isClientSide);
                }
            }


            if (livingEntity instanceof GBPygmy gbPygmy) {
                if (!gbPygmy.isImmuneToZombification() && itemStack.is(ItemRegistry.AMULET.get()) && gbPygmy.hasEffect(PotionRegistry.IMMUNITY.get()) && gbPygmy.getEffect(PotionRegistry.IMMUNITY.get()).getAmplifier() > 0) {
                    gbPygmy.setImmuneToZombification(true);
                    player.getCooldowns().addCooldown(this, 80);
                    if (!player.getAbilities().mayfly) {
                        player.giveExperienceLevels(-3);
                    }
                    livingEntity.playSound(SoundEvents.ZOMBIE_VILLAGER_CONVERTED);
                    return InteractionResult.sidedSuccess(player.level().isClientSide);
                }
            }

            if (livingEntity instanceof Hoglin gbPygmy) {
                if (!(gbPygmy).isImmuneToZombification() && itemStack.is(ItemRegistry.AMULET.get()) && gbPygmy.hasEffect(PotionRegistry.IMMUNITY.get()) && gbPygmy.getEffect(PotionRegistry.IMMUNITY.get()).getAmplifier() > 0) {
                    gbPygmy.setImmuneToZombification(true);
                    player.getCooldowns().addCooldown(this, 80);
                    if (!player.getAbilities().mayfly) {
                        player.giveExperienceLevels(-3);
                    }
                    livingEntity.playSound(SoundEvents.ZOMBIE_VILLAGER_CONVERTED);
                    return InteractionResult.sidedSuccess(player.level().isClientSide);
                }
            }
        }
        return super.interactLivingEntity(itemStack, player, livingEntity, interactionHand);

    }

    //Check the mob anc consume
    public EntityType<?> getMobAndConsume(Level level, Player player, int xp, int soul, float i) {
        int equipedSoul = soul + xp;


        if ((equipedSoul >= 3 || player.getAbilities().instabuild) && i >= 1.0F) {
            if (!player.getAbilities().instabuild) {

                int soulsand = ContainerHelper.clearOrCountMatchingItems(player.getInventory(), predicate -> {
                    return predicate.is(Items.SOUL_SAND) || predicate.is(Items.SOUL_SOIL);
                }, 3, false);
                player.giveExperienceLevels(soulsand - 3);
                player.getCooldowns().addCooldown(this, 80);
            }
            return level.dimension() == Level.NETHER ? EntityType.ZOGLIN : EntityType.STRAY;
        } else if ((equipedSoul >= 2 || player.getAbilities().instabuild) && i >= 0.5F) {
            if (!player.getAbilities().instabuild) {
                int soulsand = ContainerHelper.clearOrCountMatchingItems(player.getInventory(), predicate -> {
                    return predicate.is(Items.SOUL_SAND) || predicate.is(Items.SOUL_SOIL);
                }, 2, false);
                player.giveExperienceLevels(soulsand - 2);
                player.getCooldowns().addCooldown(this, 80);
            }
            return level.dimension() == Level.NETHER ? EntityType.ZOMBIFIED_PIGLIN : EntityType.HUSK;

        } else if ((equipedSoul >= 1 || player.getAbilities().instabuild) && i >= 0.25F) {
            if (!player.getAbilities().instabuild) {
                int soulsand = ContainerHelper.clearOrCountMatchingItems(player.getInventory(), predicate -> {
                    return predicate.is(Items.SOUL_SAND) || predicate.is(Items.SOUL_SOIL);
                }, 1, false);
                player.giveExperienceLevels(soulsand - 1);
                player.getCooldowns().addCooldown(this, 80);
            }
            return level.dimension() == Level.NETHER ? EntityTypeRegistry.ZOMBIFIED_HOGLET.get() : EntityType.SKELETON;

        } else {
            if (!player.getAbilities().instabuild) {
                player.getCooldowns().addCooldown(this, 80);
            }
            return level.dimension() != Level.NETHER ? EntityType.ZOMBIE : level.getRandom().nextBoolean() ? EntityTypeRegistry.SKELETAL_PIGLIN.get() : EntityTypeRegistry.ZOMBIFIED_PYGMIES.get();

        }
    }
}
