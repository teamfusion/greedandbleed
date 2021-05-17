package com.infernalstudios.greedandbleed.common;

import com.infernalstudios.greedandbleed.common.entity.IToleratingMount;
import net.minecraft.entity.IEquipable;
import net.minecraft.entity.monster.HoglinEntity;
import net.minecraft.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.entity.living.LivingConversionEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(
        bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeCommonEvents {

    @SubscribeEvent
    public static void onHoglinConvert(LivingConversionEvent event){
        if(event.getEntityLiving() instanceof HoglinEntity
                && event.getEntityLiving() instanceof IEquipable
                && ((IEquipable) event.getEntityLiving()).isSaddled()){
            event.getEntityLiving().spawnAtLocation(Items.SADDLE);
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