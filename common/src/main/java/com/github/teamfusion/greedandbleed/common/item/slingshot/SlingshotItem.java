package com.github.teamfusion.greedandbleed.common.item.slingshot;

import com.github.teamfusion.greedandbleed.common.registry.ItemRegistry;
import it.unimi.dsi.fastutil.objects.AbstractObject2ObjectFunction;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.Util;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class SlingshotItem extends ProjectileWeaponItem implements Vanishable {

    private static final Map<Item, SlingshotBehavior> AMMO_REGISTRY = Util.make(new Object2ObjectOpenHashMap<>(), AbstractObject2ObjectFunction::defaultReturnValue);

    public static void registerAmmo(ItemLike item, SlingshotBehavior behavior) {
        AMMO_REGISTRY.put(item.asItem(), behavior);
    }

    public static SlingshotBehavior getAmmoBehavior(ItemLike item) {
        return AMMO_REGISTRY.get(item.asItem());
    }


    private boolean startSoundPlayed = false;

    public SlingshotItem(Item.Properties properties) {
        super(properties);
    }


    public void releaseUsing(ItemStack stack, Level level, LivingEntity living, int usingTime) {
        boolean flag2 = true;
        boolean flag3 = false;
        boolean flag1 = living instanceof Player player && player.getAbilities().instabuild;
        boolean flag = living instanceof Player player && player.getAbilities().instabuild;
        ItemStack itemstack2 = living.getUsedItemHand() == InteractionHand.MAIN_HAND ? living.getItemInHand(InteractionHand.OFF_HAND) : living.getItemInHand(InteractionHand.MAIN_HAND);
        ItemStack pouch = living.getMainHandItem().is(ItemRegistry.SLINGSHOT_POUCH.get()) ? living.getMainHandItem() : living.getOffhandItem().is(ItemRegistry.SLINGSHOT_POUCH.get()) ? living.getOffhandItem() : ItemStack.EMPTY;

        RandomSource random = living.getRandom();
        int i = this.getUseDuration(stack) - usingTime;
        if (i < 0) return;
        //TODO add Catapult Enchant mechanic etc...
        float f = getPowerForTime(i);

        if (living instanceof Player player) {
            if (!pouch.isEmpty()) {
                flag2 = false;
                flag3 = true;
                itemstack2 = this.getProjectile(player);
            }
        }

        //using hand item
        if (!itemstack2.isEmpty()) {
            if (!level.isClientSide() && AMMO_REGISTRY.get(itemstack2.getItem()) != null) {
                AMMO_REGISTRY.get(itemstack2.getItem()).shootBehavior(level, living.blockPosition(), living, itemstack2, f);
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
        boolean flag = SlingshotItem.getAmmoBehavior(itemstack2.getItem()) != null || !this.getProjectile(player).isEmpty();

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
        ItemStack pouch = player.getMainHandItem().is(ItemRegistry.SLINGSHOT_POUCH.get()) ? player.getMainHandItem() : player.getOffhandItem().is(ItemRegistry.SLINGSHOT_POUCH.get()) ? player.getOffhandItem() : ItemStack.EMPTY;
        if (pouch.is(ItemRegistry.SLINGSHOT_POUCH.get())) {
            Stream<ItemStack> stream = SlingshotPouchItem.getContents(pouch);

            List<ItemStack> list = stream.toList();

            if (list.isEmpty() || list.size() <= SlingshotPouchItem.getSelectedItem(pouch)) {
                return ItemStack.EMPTY;
            }

            ItemStack itemStack3 = list.get(SlingshotPouchItem.getSelectedItem(pouch));
            if (SlingshotItem.getAmmoBehavior(itemStack3.getItem()) == null) return ItemStack.EMPTY;
            return itemStack3;
        }

        return ItemStack.EMPTY;
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
