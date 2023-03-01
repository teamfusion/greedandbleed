package com.github.teamfusion.greedandbleed.common.item;

import com.github.teamfusion.greedandbleed.api.HogEquipable;
import com.github.teamfusion.greedandbleed.common.entity.ToleratingMount;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SaddleItem;

public class HoglinSaddleItem extends SaddleItem {
    public HoglinSaddleItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity entity, InteractionHand hand) {
        if (entity instanceof HogEquipable equipable && entity instanceof ToleratingMount mount && entity.isAlive()) {
            if (!equipable.isHogSaddled() && mount.canAcceptSaddle()) {
                if (!entity.level.isClientSide) {
                    equipable.equipHogSaddle(SoundSource.NEUTRAL);
                    stack.shrink(1);
                }

                return InteractionResult.sidedSuccess(entity.level.isClientSide);
            }
        }

        return InteractionResult.PASS;
    }
}