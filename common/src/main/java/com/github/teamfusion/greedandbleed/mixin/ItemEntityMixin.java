package com.github.teamfusion.greedandbleed.mixin;

import com.github.teamfusion.greedandbleed.common.entity.piglin.Hoglet;
import com.github.teamfusion.greedandbleed.util.HogletUtils;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends Entity implements TraceableEntity {
    @Shadow
    private int pickupDelay;

    public ItemEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "playerTouch", at = @At("HEAD"))
    public void playerTouch(Player player, CallbackInfo callbackInfo) {
        if (!this.level().isClientSide) {
            if (this.pickupDelay == 0 && this.getOwner() instanceof Hoglet hoglet) {
                HogletUtils.angerAndSteal(hoglet, player);
            }
        }

    }

    @Shadow
    public Entity getOwner() {
        return null;
    }

    @Shadow
    public ItemStack getItem() {
        return null;
    }
}
