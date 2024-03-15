package com.github.teamfusion.greedandbleed.common.item;

import com.github.teamfusion.greedandbleed.common.entity.projectile.ThrownDamageableEntity;
import com.github.teamfusion.greedandbleed.common.registry.ItemRegistry;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import net.minecraft.world.entity.projectile.ThrownExperienceBottle;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;

import java.util.function.Predicate;

public class SlingshotItem extends ProjectileWeaponItem implements Vanishable {
    public static final Predicate<ItemStack> SLINGSHOT_ITEMS = (itemStack) -> {
        return itemStack.getItem() == ItemRegistry.PEBBLE.get() || itemStack.getItem() == Items.DIAMOND || itemStack.getItem() == Items.EMERALD || itemStack.getItem() == Items.BRICK || itemStack.getItem() == Items.GOLD_INGOT || itemStack.getItem() == Items.IRON_INGOT || itemStack.getItem() == Items.NETHERITE_INGOT || itemStack.getItem() == Items.COPPER_INGOT || itemStack.getItem() == Items.GOLD_NUGGET || itemStack.getItem() == Items.IRON_NUGGET || itemStack.getItem() == Items.RAW_GOLD || itemStack.getItem() == Items.RAW_COPPER || itemStack.getItem() == Items.RAW_IRON || itemStack.getItem() == Items.EGG || itemStack.getItem() == Items.SNOWBALL || itemStack.getItem() == Items.PUFFERFISH || itemStack.getItem() == Items.SPIDER_EYE || itemStack.getItem() instanceof BlockItem || itemStack.getItem() instanceof ThrowablePotionItem || itemStack.getItem() instanceof ExperienceBottleItem || itemStack.getItem() == Items.ENDER_PEARL;
    };

    private boolean startSoundPlayed = false;

    public SlingshotItem(Item.Properties properties) {
        super(properties);
    }


    public void releaseUsing(ItemStack stack, Level level, LivingEntity living, int usingTime) {
        if (living instanceof Player) {

            boolean flag2 = true;
            Player player = (Player) living;
            boolean flag1 = player.getAbilities().instabuild;
            boolean flag = player.getAbilities().instabuild;
            ItemStack itemstack = player.getProjectile(stack);
            ItemStack itemstack2 = living.getUsedItemHand() == InteractionHand.MAIN_HAND ? player.getItemInHand(InteractionHand.OFF_HAND) : player.getItemInHand(InteractionHand.MAIN_HAND);

            RandomSource random = living.getRandom();
            int i = this.getUseDuration(stack) - usingTime;
            if (i < 0) return;
            //TODO add Catapult Enchant mechanic etc...
            float f = getPowerForTime(i);
            if (itemstack2.getItem() == Items.ENDER_PEARL) {
                ThrownEnderpearl thrownPotion = new ThrownEnderpearl(level, player);
                thrownPotion.setItem(itemstack2);
                thrownPotion.setOwner(player);
                thrownPotion.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, f * 4.0f, 1.0F);
                level.addFreshEntity(thrownPotion);
            } else if (itemstack2.getItem() instanceof ExperienceBottleItem bottleItem) {
                ThrownExperienceBottle thrownPotion = new ThrownExperienceBottle(level, player);
                thrownPotion.setItem(itemstack2);
                thrownPotion.setOwner(player);
                thrownPotion.shootFromRotation(player, player.getXRot(), player.getYRot(), -5.0F, f * 2.0f, 1.0F);
                level.addFreshEntity(thrownPotion);
            } else if (itemstack2.getItem() instanceof ThrowablePotionItem potionItem) {
                ThrownPotion thrownPotion = new ThrownPotion(level, player);
                thrownPotion.setItem(itemstack2);
                thrownPotion.setOwner(player);
                thrownPotion.shootFromRotation(player, player.getXRot(), player.getYRot(), -5.0F, f * 2.0f, 1.0F);
                level.addFreshEntity(thrownPotion);
            } else if (itemstack2.getItem() instanceof BlockItem) {
                if (!((double) f < 0.1D)) {
                    if (!level.isClientSide) {
                        ThrownDamageableEntity itemEntity = new ThrownDamageableEntity(level, living);
                        itemEntity.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, f * 4.0f, 1.0F);
                        itemEntity.setItem(itemstack2);
                        itemEntity.setOwner(player);
                        itemEntity.setBaseDamage(3.0F);

                        level.addFreshEntity(itemEntity);

                    }

                    level.playSound((Player) null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F / (random.nextFloat() * 0.4F + 1.2F) + f * 0.5F);
                    if (!flag1 && !player.getAbilities().instabuild) {
                        itemstack2.shrink(1);
                        if (itemstack2.isEmpty()) {
                            player.getInventory().removeItem(itemstack2);
                        }
                    }
                }
            } else if (!itemstack2.isEmpty() || flag) {
                //normal slingshot mechanic here
                if (itemstack2.isEmpty() || itemstack2.getItem() == Items.ARROW) {
                    itemstack2 = new ItemStack(ItemRegistry.PEBBLE.get());
                }
                if (!((double) f < 0.1D)) {
                    if (!level.isClientSide) {

                        ThrownDamageableEntity itemEntity = new ThrownDamageableEntity(level, living);
                        itemEntity.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, f * 4F, 1.0F);
                        itemEntity.setItem(itemstack2);
                        itemEntity.setOwner(player);

                        if (itemstack2.getItem() == Items.EGG || itemstack2.getItem() == Items.SNOWBALL) {
                            //set egg and snowball damage
                            itemEntity.setBaseDamage(1F);
                        }
                        if (itemstack2.getItem() == Items.FIRE_CHARGE) {
                            itemEntity.setBaseDamage(0F);
                        }
                        if (itemstack2.getItem() == Items.GOLD_NUGGET || itemstack2.getItem() == Items.IRON_NUGGET) {
                            itemEntity.setBaseDamage(2F);
                        }
                        if (itemstack2.getItem() == Items.PUFFERFISH || itemstack2.getItem() == Items.SPIDER_EYE) {
                            itemEntity.setBaseDamage(2F);
                        }
                        if (itemstack2.getItem() == Items.RAW_GOLD || itemstack2.getItem() == Items.RAW_IRON || itemstack2.getItem() == Items.RAW_COPPER) {
                            itemEntity.setBaseDamage(2.5F);
                        }
                        if (itemstack2.getItem() == Items.BRICK || itemstack2.getItem() == Items.IRON_INGOT || itemstack2.getItem() == Items.COPPER_INGOT) {
                            itemEntity.setBaseDamage(3F);
                        }
                        if (itemstack2.getItem() == Items.NETHERITE_INGOT || itemstack2.getItem() == Items.GOLD_INGOT) {
                            itemEntity.setBaseDamage(4F);
                        }
                        if (itemstack2.getItem() == Items.DIAMOND || itemstack2.getItem() == Items.EMERALD) {
                            itemEntity.setBaseDamage(3F);
                        }

                        level.addFreshEntity(itemEntity);

                        stack.hurtAndBreak(1, player, (p_220009_1_) -> {
                            p_220009_1_.broadcastBreakEvent(player.getUsedItemHand());
                        });

                    }



                }
            }
            if (flag2) {
                level.gameEvent(GameEvent.PROJECTILE_SHOOT, player.position(), GameEvent.Context.of(player));

                player.awardStat(Stats.ITEM_USED.get(this));

                level.playSound((Player) null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F / (random.nextFloat() * 0.4F + 1.2F) + f * 0.5F);

                stack.hurtAndBreak(1, player, (p_220009_1_) -> {
                    p_220009_1_.broadcastBreakEvent(player.getUsedItemHand());
                });

                if (!flag1 && !player.getAbilities().instabuild) {
                    itemstack2.shrink(1);
                    if (itemstack2.isEmpty()) {
                        player.getInventory().removeItem(itemstack2);
                    }
                }

            }
        }
    }

    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack itemStack, int i) {
        if (!level.isClientSide) {
            SoundEvent soundEvent = SoundEvents.CROSSBOW_LOADING_START;
            float f = SlingshotItem.getPowerForTime((itemStack.getUseDuration() - i));
            if (f < 0.2f) {
                this.startSoundPlayed = false;
            }
            if (f >= 0.2f && !this.startSoundPlayed) {
                this.startSoundPlayed = true;
                level.playSound(null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), soundEvent, SoundSource.PLAYERS, 0.5f, 1.25f);
            }
        }
    }

    public static float getPowerForTime(int time) {
        float f = (float) time / 20.0F;
        f = (f * f + f * 6.0F) / 3.0F;
        if (f > 1.0F) {
            f = 1.0F;
        }

        return f;
    }

    public int getUseDuration(ItemStack p_77626_1_) {
        return 32000;
    }

    public UseAnim getUseAnimation(ItemStack p_77661_1_) {
        return UseAnim.CROSSBOW;
    }


    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        ItemStack itemstack2 = hand == InteractionHand.MAIN_HAND ? player.getItemInHand(InteractionHand.OFF_HAND) : player.getItemInHand(InteractionHand.MAIN_HAND);
        boolean flag = SLINGSHOT_ITEMS.test(itemstack2);

        if (!player.getAbilities().instabuild && !flag) {
            return InteractionResultHolder.fail(itemstack);
        } else {
            player.startUsingItem(hand);
            return InteractionResultHolder.consume(itemstack);
        }
    }

    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return itemStack -> {
            return false;
        };
    }

    @Override
    public int getDefaultProjectileRange() {
        return 16;
    }

}
