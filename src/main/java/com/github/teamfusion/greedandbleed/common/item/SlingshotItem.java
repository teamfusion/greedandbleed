package com.github.teamfusion.greedandbleed.common.item;

import com.github.teamfusion.greedandbleed.common.entity.projectile.ThrownDamageableEntity;
import com.github.teamfusion.greedandbleed.common.registry.ItemRegistry;
import net.minecraft.enchantment.IVanishable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

import java.util.function.Predicate;

public class SlingshotItem extends ShootableItem implements IVanishable {
	public static final Predicate<ItemStack> SLINGSHOT_ITEMS = (p_220002_0_) -> {
		return p_220002_0_.getItem() == ItemRegistry.PEBBLE.get() || p_220002_0_.getItem() == Items.EGG || p_220002_0_.getItem() == Items.SNOWBALL;
	};

	public SlingshotItem(Item.Properties properties) {
		super(properties);
	}

	public void releaseUsing(ItemStack stack, World world, LivingEntity living, int usingTime) {
		if (living instanceof PlayerEntity) {
			PlayerEntity playerentity = (PlayerEntity) living;
			boolean flag = playerentity.abilities.instabuild;
			ItemStack itemstack = playerentity.getProjectile(stack);
			ItemStack itemstack2 = playerentity.getItemInHand(Hand.OFF_HAND);

			int i = this.getUseDuration(stack) - usingTime;
			i = net.minecraftforge.event.ForgeEventFactory.onArrowLoose(stack, world, playerentity, i, !itemstack.isEmpty() || flag);
			if (i < 0) return;
			//TODO add Catapult Enchant mechanic etc...
			if (itemstack2.getItem() instanceof BlockItem) {
				float f = getPowerForTime(i);
				if (!((double) f < 0.1D)) {
					boolean flag1 = playerentity.abilities.instabuild;
					if (!world.isClientSide) {
						ThrownDamageableEntity itemEntity = new ThrownDamageableEntity(world, living);
						itemEntity.shootFromRotation(playerentity, playerentity.xRot, playerentity.yRot, 0.0F, f, 1.0F);
						itemEntity.setItem(itemstack2);

						itemEntity.setBaseDamage(3.0F);


						stack.hurtAndBreak(1, playerentity, (p_220009_1_) -> {
							p_220009_1_.broadcastBreakEvent(playerentity.getUsedItemHand());
						});
						world.addFreshEntity(itemEntity);
					}

					world.playSound((PlayerEntity) null, playerentity.getX(), playerentity.getY(), playerentity.getZ(), SoundEvents.ARROW_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F / (random.nextFloat() * 0.4F + 1.2F) + f * 0.5F);
					if (!flag1 && !playerentity.abilities.instabuild) {
						itemstack2.shrink(1);
						if (itemstack2.isEmpty()) {
							playerentity.inventory.removeItem(itemstack2);
						}
					}
				}
				playerentity.awardStat(Stats.ITEM_USED.get(this));
			} else if (!itemstack.isEmpty() || flag) {
				//normal slingshot mechanic here
				if (itemstack.isEmpty() || itemstack.getItem() == Items.ARROW) {
					itemstack = new ItemStack(ItemRegistry.PEBBLE.get());
				}

				float f = getPowerForTime(i);
				if (!((double) f < 0.1D)) {
					boolean flag1 = playerentity.abilities.instabuild;
					if (!world.isClientSide) {
						ThrownDamageableEntity itemEntity = new ThrownDamageableEntity(world, living);
						itemEntity.shootFromRotation(playerentity, playerentity.xRot, playerentity.yRot, 0.0F, f * 1.15F, 1.0F);
						itemEntity.setItem(itemstack);

						if (itemstack.getItem() == Items.EGG || itemstack.getItem() == Items.SNOWBALL) {
							//set egg and snowball damage
							itemEntity.setBaseDamage(1.0F);
						}

						stack.hurtAndBreak(1, playerentity, (p_220009_1_) -> {
							p_220009_1_.broadcastBreakEvent(playerentity.getUsedItemHand());
						});
						world.addFreshEntity(itemEntity);
					}

					world.playSound((PlayerEntity) null, playerentity.getX(), playerentity.getY(), playerentity.getZ(), SoundEvents.ARROW_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F / (random.nextFloat() * 0.4F + 1.2F) + f * 0.5F);
					if (!flag1 && !playerentity.abilities.instabuild) {
						itemstack.shrink(1);
						if (itemstack.isEmpty()) {
							playerentity.inventory.removeItem(itemstack);
						}
					}

					playerentity.awardStat(Stats.ITEM_USED.get(this));
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

	public UseAction getUseAnimation(ItemStack p_77661_1_) {
		return UseAction.BOW;
	}

	public ActionResult<ItemStack> use(World level, PlayerEntity player, Hand hand) {
		ItemStack itemstack = player.getItemInHand(hand);
		ItemStack itemstack2 = player.getItemInHand(Hand.OFF_HAND);
		boolean flag = !player.getProjectile(itemstack).isEmpty() || itemstack2.getItem() instanceof BlockItem;


		ActionResult<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onArrowNock(itemstack, level, player, hand, flag);
		if (ret != null) return ret;

		if (!player.abilities.instabuild && !flag) {
			return ActionResult.fail(itemstack);
		} else {
			player.startUsingItem(hand);
			return ActionResult.consume(itemstack);
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
