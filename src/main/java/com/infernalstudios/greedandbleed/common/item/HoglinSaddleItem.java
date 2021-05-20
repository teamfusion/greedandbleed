package com.infernalstudios.greedandbleed.common.item;

import com.infernalstudios.greedandbleed.common.entity.IToleratingMount;
import net.minecraft.entity.IEquipable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SaddleItem;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;

public class HoglinSaddleItem extends SaddleItem {

    public HoglinSaddleItem(Properties properties) {
        super(properties);
    }

    public ActionResultType interactLivingEntity(ItemStack stack, PlayerEntity player, LivingEntity interactTarget, Hand hand) {
        if (interactTarget instanceof IEquipable
                && interactTarget instanceof IToleratingMount
                && interactTarget.isAlive()) {
            IEquipable iequipable = (IEquipable)interactTarget;
            IToleratingMount toleratingMount = (IToleratingMount)interactTarget;
            if (!iequipable.isSaddled() && toleratingMount.canAcceptSaddle()) {
                if (!interactTarget.level.isClientSide) {
                    iequipable.equipSaddle(SoundCategory.NEUTRAL);
                    stack.shrink(1);
                }
                return ActionResultType.sidedSuccess(interactTarget.level.isClientSide);
            }
        }

        return ActionResultType.PASS;
    }
}
