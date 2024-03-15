package com.github.teamfusion.greedandbleed.common.item;

import com.github.teamfusion.greedandbleed.common.entity.projectile.ThrownDamageableEntity;
import com.github.teamfusion.greedandbleed.common.registry.ItemRegistry;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

import java.util.function.Predicate;

public class SlingshotItem extends ProjectileWeaponItem implements Vanishable {
    public static final Predicate<ItemStack> SLINGSHOT_ITEMS = (p_220002_0_) -> {
        return p_220002_0_.getItem() == ItemRegistry.PEBBLE.get() || p_220002_0_.getItem() == Items.EGG || p_220002_0_.getItem() == Items.SNOWBALL || p_220002_0_.getItem() instanceof ThrowablePotionItem;
    };

    public SlingshotItem(Item.Properties properties) {
        super(properties);
    }


    public void releaseUsing(ItemStack stack, Level level, LivingEntity living, int usingTime) {
        if (living instanceof Player) {
            Player player = (Player) living;
            boolean flag = player.getAbilities().instabuild;
            ItemStack itemstack = player.getProjectile(stack);
            ItemStack itemstack2 = player.getItemInHand(InteractionHand.OFF_HAND);
            RandomSource random = living.getRandom();
            int i = this.getUseDuration(stack) - usingTime;
            if (i < 0) return;
            //TODO add Catapult Enchant mechanic etc...
            if (itemstack2.getItem() instanceof BlockItem) {
                float f = getPowerForTime(i);
                if (!((double) f < 0.1D)) {
                    boolean flag1 = player.getAbilities().instabuild;
                    if (!level.isClientSide) {

                        ThrownDamageableEntity itemEntity = new ThrownDamageableEntity(level, living);
                        itemEntity.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, f, 1.0F);
                        itemEntity.setItem(itemstack2);

                        itemEntity.setBaseDamage(3.0F);

                        level.addFreshEntity(itemEntity);
                        stack.hurtAndBreak(1, player, (p_220009_1_) -> {
                            p_220009_1_.broadcastBreakEvent(player.getUsedItemHand());
                        });
                    }

                    level.playSound((Player) null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F / (random.nextFloat() * 0.4F + 1.2F) + f * 0.5F);
                    if (!flag1 && !player.getAbilities().instabuild) {
                        itemstack2.shrink(1);
                        if (itemstack2.isEmpty()) {
                            player.getInventory().removeItem(itemstack2);
                        }
                    }
                }
                player.awardStat(Stats.ITEM_USED.get(this));
            } else if (!itemstack.isEmpty() || flag) {
                //normal slingshot mechanic here
                if (itemstack.isEmpty() || itemstack.getItem() == Items.ARROW) {
                    itemstack = new ItemStack(ItemRegistry.PEBBLE.get());
                }

                float f = getPowerForTime(i);
                if (!((double) f < 0.1D)) {
                    boolean flag1 = player.getAbilities().instabuild;
                    if (!level.isClientSide) {
                        if (itemstack2.getItem() instanceof PotionItem potionItem) {
                            ThrownPotion thrownPotion = new ThrownPotion(level, player);
                            thrownPotion.setItem(itemstack2);
                            thrownPotion.shootFromRotation(player, player.getXRot(), player.getYRot(), -5.0F, f, 1.0F);
                            level.addFreshEntity(thrownPotion);
                        } else {
                            ThrownDamageableEntity itemEntity = new ThrownDamageableEntity(level, living);
                            itemEntity.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, f * 1.15F, 1.0F);
                            itemEntity.setItem(itemstack);

                            if (itemstack.getItem() == Items.EGG || itemstack.getItem() == Items.SNOWBALL) {
                                //set egg and snowball damage
                                itemEntity.setBaseDamage(0.5F);
                            }

                            level.addFreshEntity(itemEntity);
                        }
                        stack.hurtAndBreak(1, player, (p_220009_1_) -> {
                            p_220009_1_.broadcastBreakEvent(player.getUsedItemHand());
                        });
                    }

                    level.playSound((Player) null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F / (random.nextFloat() * 0.4F + 1.2F) + f * 0.5F);
                    if (!flag1 && !player.getAbilities().instabuild) {
                        itemstack.shrink(1);
                        if (itemstack.isEmpty()) {
                            player.getInventory().removeItem(itemstack);
                        }
                    }

                    player.awardStat(Stats.ITEM_USED.get(this));
                }
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
        ItemStack itemstack2 = player.getItemInHand(InteractionHand.OFF_HAND);
        boolean flag = !player.getProjectile(itemstack).isEmpty() || itemstack2.getItem() instanceof BlockItem;

        if (!player.getAbilities().instabuild && !flag) {
            return InteractionResultHolder.fail(itemstack);
        } else {
            player.startUsingItem(hand);
            return InteractionResultHolder.consume(itemstack);
        }
    }

    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return SLINGSHOT_ITEMS;
    }

    @Override
    public int getDefaultProjectileRange() {
        return 16;
    }

}
