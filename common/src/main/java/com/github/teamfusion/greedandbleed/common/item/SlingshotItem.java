package com.github.teamfusion.greedandbleed.common.item;

import com.github.teamfusion.greedandbleed.common.entity.projectile.ThrownDamageableEntity;
import com.github.teamfusion.greedandbleed.common.registry.GBItemTags;
import com.github.teamfusion.greedandbleed.common.registry.ItemRegistry;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import net.minecraft.world.entity.projectile.ThrownExperienceBottle;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class SlingshotItem extends ProjectileWeaponItem implements Vanishable {
    public static final Predicate<ItemStack> SLINGSHOT_ITEMS = (itemStack) -> {
        return itemStack.getItem() == ItemRegistry.PEBBLE.get() || itemStack.getItem() == Items.DIAMOND || itemStack.getItem() == Items.EMERALD || itemStack.getItem() == Items.BRICK || itemStack.getItem() == Items.NETHER_BRICK || itemStack.getItem() == Items.GOLD_INGOT || itemStack.getItem() == Items.IRON_INGOT || itemStack.getItem() == Items.NETHERITE_INGOT || itemStack.getItem() == Items.COPPER_INGOT || itemStack.getItem() == Items.GOLD_NUGGET || itemStack.getItem() == Items.IRON_NUGGET || itemStack.getItem() == Items.RAW_GOLD || itemStack.getItem() == Items.RAW_COPPER || itemStack.getItem() == Items.RAW_IRON || itemStack.getItem() == Items.EGG || itemStack.getItem() == Items.SNOWBALL || itemStack.getItem() == Items.PUFFERFISH || itemStack.getItem() == Items.SPIDER_EYE || itemStack.getItem() instanceof BlockItem || itemStack.getItem() instanceof ThrowablePotionItem || itemStack.getItem() instanceof ExperienceBottleItem || itemStack.getItem() == Items.ENDER_PEARL || itemStack.is(GBItemTags.BUCKSHOT_ITEM) || itemStack.is(Items.FIRE_CHARGE) || itemStack.is(Items.POISONOUS_POTATO);
    };

    private boolean startSoundPlayed = false;

    public SlingshotItem(Item.Properties properties) {
        super(properties);
    }


    public void releaseUsing(ItemStack stack, Level level, LivingEntity living, int usingTime) {

            boolean flag2 = true;
            boolean flag3 = false;
        boolean flag1 = living instanceof Player player && player.getAbilities().instabuild;
        boolean flag = living instanceof Player player && player.getAbilities().instabuild;
        ItemStack itemstack = living instanceof Player player ? this.getProjectile(player) : ItemStack.EMPTY;
        ItemStack itemstack2 = living.getUsedItemHand() == InteractionHand.MAIN_HAND ? living.getItemInHand(InteractionHand.OFF_HAND) : living.getItemInHand(InteractionHand.MAIN_HAND);

            RandomSource random = living.getRandom();
            int i = this.getUseDuration(stack) - usingTime;
            if (i < 0) return;
            //TODO add Catapult Enchant mechanic etc...
            float f = getPowerForTime(i);
            //using hand item
            if (itemstack2.getItem() == Items.ENDER_PEARL) {
                ThrownEnderpearl thrownPotion = new ThrownEnderpearl(level, living);
                thrownPotion.setItem(itemstack2);
                thrownPotion.setOwner(living);
                thrownPotion.shootFromRotation(living, living.getXRot(), living.getYRot(), 0.0F, f * 2.5f, 1.0F);
                level.addFreshEntity(thrownPotion);
            } else if (itemstack2.getItem() instanceof ExperienceBottleItem bottleItem) {
                ThrownExperienceBottle thrownPotion = new ThrownExperienceBottle(level, living);
                thrownPotion.setItem(itemstack2);
                thrownPotion.setOwner(living);
                thrownPotion.shootFromRotation(living, living.getXRot(), living.getYRot(), -5.0F, f * 1.5f, 1.0F);
                level.addFreshEntity(thrownPotion);
            } else if (itemstack2.getItem() instanceof ThrowablePotionItem potionItem) {
                ThrownPotion thrownPotion = new ThrownPotion(level, living);
                thrownPotion.setItem(itemstack2);
                thrownPotion.setOwner(living);
                thrownPotion.shootFromRotation(living, living.getXRot(), living.getYRot(), -5.0F, f * 1.5f, 1.0F);
                level.addFreshEntity(thrownPotion);
            } else if (itemstack2.is(Items.FIRE_CHARGE)) {
                LargeFireball fireball = new LargeFireball(EntityType.FIREBALL, level);
                fireball.setItem(itemstack2);
                fireball.setOwner(living);
                Vec3 vec3 = living.getLookAngle();
                fireball.xPower = vec3.x * 0.05F * f;
                fireball.yPower = -0.03F;
                fireball.zPower = vec3.z * 0.05F * f;
                fireball.moveTo(living.getX(), living.getY() + living.getEyeHeight(), living.getZ(), living.getYRot(), living.getXRot());
                fireball.shootFromRotation(living, living.getXRot(), living.getYRot(), -5.0F, f * 1.5f, 1.0F);
                level.addFreshEntity(fireball);
            } else if (itemstack2.getItem() instanceof BlockItem && !itemstack2.is(GBItemTags.BUCKSHOT_ITEM)) {
                if (!((double) f < 0.1D)) {
                    if (!level.isClientSide) {
                        ThrownDamageableEntity itemEntity = new ThrownDamageableEntity(level, living);
                        itemEntity.shootFromRotation(living, living.getXRot(), living.getYRot(), 0.0F, f * 2.5f, 1.0F);
                        itemEntity.setItem(itemstack2);
                        itemEntity.setOwner(living);
                        itemEntity.setBaseDamage(3.0F);

                        level.addFreshEntity(itemEntity);

                    }
                }
            } else if (!itemstack2.isEmpty() && SLINGSHOT_ITEMS.test(itemstack2)) {
                //normal slingshot mechanic here
                if (itemstack2.isEmpty()) {
                    itemstack2 = new ItemStack(ItemRegistry.PEBBLE.get());
                }
                if (!((double) f < 0.1D)) {
                    if (!level.isClientSide) {

                        int j = itemstack2.is(GBItemTags.BUCKSHOT_ITEM) ? 3 : 1;
                        for (int k = 0; k < j; ++k) {
                            ThrownDamageableEntity itemEntity = new ThrownDamageableEntity(level, living);
                            itemEntity.shootFromRotation(living, living.getXRot() + (random.nextFloat() - random.nextFloat()) * k * 5F, living.getYRot() + (random.nextFloat() - random.nextFloat()) * k * 5F, 0.0F, f * 2.5F, 1.0F);
                            itemEntity.setItem(itemstack2);
                            itemEntity.setOwner(living);


                            if (itemstack2.getItem() == Items.EGG || itemstack2.getItem() == Items.SNOWBALL) {
                                //set egg and snowball damage
                                itemEntity.setBaseDamage(1F);
                            }
                            if (itemstack2.is(ItemTags.VILLAGER_PLANTABLE_SEEDS)) {
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
                            if (itemstack2.getItem() == Items.BRICK || itemstack2.getItem() == Items.NETHER_BRICK || itemstack2.getItem() == Items.IRON_INGOT || itemstack2.getItem() == Items.COPPER_INGOT) {
                                itemEntity.setBaseDamage(3F);
                            }
                            if (itemstack2.getItem() == Items.NETHERITE_INGOT || itemstack2.getItem() == Items.GOLD_INGOT) {
                                itemEntity.setBaseDamage(4F);
                            }
                            if (itemstack2.getItem() == Items.DIAMOND || itemstack2.getItem() == Items.EMERALD) {
                                itemEntity.setBaseDamage(3F);
                            }

                            level.addFreshEntity(itemEntity);

                        }
                    }


                }
            } else if (!itemstack.isEmpty() || flag) {
                flag3 = true;
                flag2 = false;
                //using pouch item
                if (itemstack.getItem() == Items.ENDER_PEARL) {
                    ThrownEnderpearl thrownPotion = new ThrownEnderpearl(level, living);
                    thrownPotion.setItem(itemstack);
                    thrownPotion.setOwner(living);
                    thrownPotion.shootFromRotation(living, living.getXRot(), living.getYRot(), 0.0F, f * 2.5f, 1.0F);
                    level.addFreshEntity(thrownPotion);
                } else if (itemstack.getItem() instanceof ExperienceBottleItem bottleItem) {
                    ThrownExperienceBottle thrownPotion = new ThrownExperienceBottle(level, living);
                    thrownPotion.setItem(itemstack);
                    thrownPotion.setOwner(living);
                    thrownPotion.shootFromRotation(living, living.getXRot(), living.getYRot(), -5.0F, f * 1.5f, 1.0F);
                    level.addFreshEntity(thrownPotion);
                } else if (itemstack.getItem() instanceof ThrowablePotionItem potionItem) {
                    ThrownPotion thrownPotion = new ThrownPotion(level, living);
                    thrownPotion.setItem(itemstack);
                    thrownPotion.setOwner(living);
                    thrownPotion.shootFromRotation(living, living.getXRot(), living.getYRot(), -5.0F, f * 1.5f, 1.0F);
                    level.addFreshEntity(thrownPotion);
                } else if (itemstack.is(Items.FIRE_CHARGE)) {
                    LargeFireball fireball = new LargeFireball(EntityType.FIREBALL, level);
                    fireball.setItem(itemstack);
                    fireball.setOwner(living);
                    fireball.moveTo(living.getX(), living.getY() + living.getEyeHeight(), living.getZ(), living.getYRot(), living.getXRot());
                    Vec3 vec3 = living.getLookAngle();
                    fireball.xPower = vec3.x * 0.05F * f;
                    fireball.yPower = -0.03F;
                    fireball.zPower = vec3.z * 0.05F * f;
                    fireball.shootFromRotation(living, living.getXRot(), living.getYRot(), -5.0F, f * 1.5f, 1.0F);
                    level.addFreshEntity(fireball);
                } else if (itemstack.getItem() instanceof BlockItem && !itemstack2.is(GBItemTags.BUCKSHOT_ITEM)) {
                    if (!((double) f < 0.1D)) {
                        if (!level.isClientSide) {
                            ThrownDamageableEntity itemEntity = new ThrownDamageableEntity(level, living);
                            itemEntity.shootFromRotation(living, living.getXRot(), living.getYRot(), 0.0F, f * 2.5f, 1.0F);
                            itemEntity.setItem(itemstack);
                            itemEntity.setOwner(living);
                            itemEntity.setBaseDamage(3.0F);

                            level.addFreshEntity(itemEntity);

                        }
                    }
                } else if (!itemstack.isEmpty() && SLINGSHOT_ITEMS.test(itemstack) || flag) {
                    //normal slingshot mechanic here
                    if (itemstack.isEmpty() || !SLINGSHOT_ITEMS.test(itemstack) && !SLINGSHOT_ITEMS.test(itemstack2)) {
                        itemstack = new ItemStack(ItemRegistry.PEBBLE.get());
                    }
                    if (!((double) f < 0.1D)) {
                        if (!level.isClientSide) {

                            int j = itemstack.is(GBItemTags.BUCKSHOT_ITEM) ? 3 : 1;
                            for (int k = 0; k < j; ++k) {
                                ThrownDamageableEntity itemEntity = new ThrownDamageableEntity(level, living);
                                itemEntity.shootFromRotation(living, living.getXRot() + (random.nextFloat() - random.nextFloat()) * k * 5F, living.getYRot() + (random.nextFloat() - random.nextFloat()) * k * 5F, 0.0F, f * 2.5F, 1.0F);
                                itemEntity.setItem(itemstack);
                                itemEntity.setOwner(living);

                                if (itemstack.getItem() == Items.EGG || itemstack.getItem() == Items.SNOWBALL) {
                                    //set egg and snowball damage
                                    itemEntity.setBaseDamage(1F);
                                }
                                if (itemstack.is(ItemTags.VILLAGER_PLANTABLE_SEEDS)) {
                                    itemEntity.setBaseDamage(0F);
                                }
                                if (itemstack.getItem() == Items.GOLD_NUGGET || itemstack.getItem() == Items.IRON_NUGGET) {
                                    itemEntity.setBaseDamage(2F);
                                }
                                if (itemstack.getItem() == Items.PUFFERFISH || itemstack.getItem() == Items.SPIDER_EYE) {
                                    itemEntity.setBaseDamage(2F);
                                }
                                if (itemstack.getItem() == Items.RAW_GOLD || itemstack.getItem() == Items.RAW_IRON || itemstack.getItem() == Items.RAW_COPPER) {
                                    itemEntity.setBaseDamage(2.5F);
                                }
                                if (itemstack.getItem() == Items.BRICK || itemstack.getItem() == Items.NETHER_BRICK || itemstack.getItem() == Items.IRON_INGOT || itemstack.getItem() == Items.COPPER_INGOT) {
                                    itemEntity.setBaseDamage(3F);
                                }
                                if (itemstack.getItem() == Items.NETHERITE_INGOT || itemstack.getItem() == Items.GOLD_INGOT) {
                                    itemEntity.setBaseDamage(4F);
                                }
                                if (itemstack.getItem() == Items.DIAMOND || itemstack.getItem() == Items.EMERALD) {
                                    itemEntity.setBaseDamage(3F);
                                }

                                level.addFreshEntity(itemEntity);
                            }
                        }
                    }
                }
            }
            if (flag2) {
                level.gameEvent(GameEvent.PROJECTILE_SHOOT, living.position(), GameEvent.Context.of(living));


                level.playSound((Player) null, living.getX(), living.getY(), living.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F / (random.nextFloat() * 0.4F + 1.2F) + f * 0.5F);

                stack.hurtAndBreak(1, living, (p_220009_1_) -> {
                    p_220009_1_.broadcastBreakEvent(living.getUsedItemHand());
                });

                if (living instanceof Player player) {
                    player.awardStat(Stats.ITEM_USED.get(this));

                    if (!flag1 && !player.getAbilities().instabuild) {
                        itemstack2.shrink(1);
                        if (itemstack2.isEmpty()) {
                            player.getInventory().removeItem(itemstack2);
                        }
                    }
                }
            }
            if (flag3) {
                level.gameEvent(GameEvent.PROJECTILE_SHOOT, living.position(), GameEvent.Context.of(living));


                level.playSound((Player) null, living.getX(), living.getY(), living.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F / (random.nextFloat() * 0.4F + 1.2F) + f * 0.5F);

                stack.hurtAndBreak(1, living, (p_220009_1_) -> {
                    p_220009_1_.broadcastBreakEvent(living.getUsedItemHand());
                });
                if (living instanceof Player player) {
                    player.awardStat(Stats.ITEM_USED.get(this));


                    if (!flag1 && !player.getAbilities().instabuild) {
                        SlingshotPouchItem.removeOneCountItem(this.getPouch(player));
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
        return UseAnim.BOW;
    }


    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        ItemStack itemstack2 = hand == InteractionHand.MAIN_HAND ? player.getItemInHand(InteractionHand.OFF_HAND) : player.getItemInHand(InteractionHand.MAIN_HAND);
        boolean flag = SLINGSHOT_ITEMS.test(itemstack2) || !this.getProjectile(player).isEmpty();

        if (!flag) {
            return InteractionResultHolder.fail(itemstack);
        } else {
            player.startUsingItem(hand);
            return InteractionResultHolder.consume(itemstack);
        }
    }


    public ItemStack getPouch(Player player) {

        for (int i = 0; i < player.getInventory().getContainerSize(); ++i) {
            ItemStack itemStack2 = player.getInventory().getItem(i);
            if (itemStack2.is(ItemRegistry.SLINGSHOT_POUCH.get()))
                return itemStack2;
        }
        return ItemStack.EMPTY;
    }

    public ItemStack getProjectile(Player player) {

        for (int i = 0; i < player.getInventory().getContainerSize(); ++i) {
            ItemStack itemStack2 = player.getInventory().getItem(i);
            if (itemStack2.is(ItemRegistry.SLINGSHOT_POUCH.get())) {
                Stream<ItemStack> stream = SlingshotPouchItem.getContents(itemStack2);

                List<ItemStack> list = stream.toList();

                if (list.isEmpty() || list.size() <= SlingshotPouchItem.getSelectedItem(itemStack2)) {
                    continue;
                }

                ItemStack itemStack3 = list.get(SlingshotPouchItem.getSelectedItem(itemStack2));
                if (!SLINGSHOT_ITEMS.test(itemStack3)) continue;
                return itemStack3;
            }
        }
        return player.getAbilities().instabuild ? new ItemStack(ItemRegistry.PEBBLE.get()) : ItemStack.EMPTY;
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
