package com.infernalstudios.greedandbleed.common;

import com.infernalstudios.greedandbleed.common.entity.IToleratingMount;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(
        bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeCommonEvents {

    private static final int TICKS_PER_SECOND = 20;

    @SubscribeEvent
    public static void onHoglinAttack(LivingAttackEvent event){
        if(event.getSource().getDirectEntity() instanceof IToleratingMount
            && !event.getEntityLiving().level.isClientSide){
            IToleratingMount toleratingMount = (IToleratingMount) event.getSource().getDirectEntity();
            int attackToleranceCost = 1 * TICKS_PER_SECOND;
            if(toleratingMount.getTolerance() >= attackToleranceCost){
                toleratingMount.addTolerance(-1 * attackToleranceCost);
            }
        }
    }

    @SubscribeEvent
    public static void onHoglinUpdate(LivingEvent.LivingUpdateEvent event){
        if(event.getEntityLiving() instanceof IToleratingMount
                && !event.getEntityLiving().level.isClientSide){
            IToleratingMount toleratingMount = (IToleratingMount) event.getEntityLiving();
            if(toleratingMount.getTolerance() > 0){
                toleratingMount.addTolerance(-1);
            }
            else if(toleratingMount.getTolerance() < 0){
                toleratingMount.setTolerance(0);
            }
        }
    }
}