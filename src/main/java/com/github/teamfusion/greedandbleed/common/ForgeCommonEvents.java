package com.github.teamfusion.greedandbleed.common;

import com.github.teamfusion.greedandbleed.common.entity.IToleratingMount;
import com.github.teamfusion.greedandbleed.common.registry.EntityTypeRegistry;
import net.minecraft.entity.EntityClassification;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(
        bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeCommonEvents {

    @SubscribeEvent
    public static void onHoglinAttack(LivingAttackEvent event){
        if(event.getSource().getDirectEntity() instanceof IToleratingMount
            && !event.getEntityLiving().level.isClientSide){
            IToleratingMount toleratingMount = (IToleratingMount) event.getSource().getDirectEntity();
            int attackToleranceCost = 20;
            if (toleratingMount.getTolerance() >= attackToleranceCost){
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

    @SubscribeEvent
    public static void onBiomeLoading(BiomeLoadingEvent event){
        if(event.getName() != null && event.getName().equals(Biomes.SOUL_SAND_VALLEY.getRegistryName())){
            MobSpawnInfo.Spawners skiglinSpawner = new MobSpawnInfo.Spawners(EntityTypeRegistry.SKELETAL_PIGLIN.get(), 20, 5, 5);
            event.getSpawns().getSpawner(EntityClassification.MONSTER).add(skiglinSpawner);
            event.getSpawns().addMobCharge(EntityTypeRegistry.SKELETAL_PIGLIN.get(), 0.7D, 0.15D);
        }
    }
}
