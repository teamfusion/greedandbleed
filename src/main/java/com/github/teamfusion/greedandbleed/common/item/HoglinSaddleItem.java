package com.github.teamfusion.greedandbleed.common.item;

import com.github.teamfusion.greedandbleed.api.IHogEquipable;
import com.github.teamfusion.greedandbleed.common.entity.IToleratingMount;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SaddleItem;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;

@SuppressWarnings("NullableProblems")
public class HoglinSaddleItem extends SaddleItem {

    public HoglinSaddleItem(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResultType interactLivingEntity(ItemStack stack, PlayerEntity player, LivingEntity interactTarget, Hand hand) {
        if (interactTarget instanceof IHogEquipable
                && interactTarget instanceof IToleratingMount
                && interactTarget.isAlive()) {
            IHogEquipable iequipable = (IHogEquipable) interactTarget;
            IToleratingMount toleratingMount = (IToleratingMount) interactTarget;
            if (!iequipable.isHogSaddled() && toleratingMount.canAcceptSaddle()) {
                if (!interactTarget.level.isClientSide) {
                    iequipable.equipHogSaddle(SoundCategory.NEUTRAL);
                    stack.shrink(1);
                }
                return ActionResultType.sidedSuccess(interactTarget.level.isClientSide);
            }
        }

        return ActionResultType.PASS;
    }
}
