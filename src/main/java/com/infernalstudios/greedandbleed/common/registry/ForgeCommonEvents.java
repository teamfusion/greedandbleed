package com.infernalstudios.greedandbleed.common.registry;

import net.minecraft.entity.IEquipable;
import net.minecraft.entity.monster.HoglinEntity;
import net.minecraft.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.entity.living.LivingConversionEvent;
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
}