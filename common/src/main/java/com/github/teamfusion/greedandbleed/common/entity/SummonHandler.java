package com.github.teamfusion.greedandbleed.common.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import org.apache.commons.compress.utils.Lists;

import java.util.List;
import java.util.UUID;

public class SummonHandler {
    private final List<SummonData> list = Lists.newArrayList();

    public void tick(Level level, Mob mob) {
        if (!level.isClientSide()) {
            list.removeIf(entity -> {
                LivingEntity living = entity.getOwner(level);
                return living == null || !living.isAlive();
            });
        }
    }

    public void addAdditionalSaveData(CompoundTag tag) {
        ListTag listTag = new ListTag();
        for (SummonData mobEffectInstance : list) {
            listTag.add(mobEffectInstance.save(new CompoundTag()));
        }
        tag.put("SummonEntities", listTag);
    }

    public void readAdditionalSaveData(CompoundTag tag) {
        list.removeAll(list.stream().toList());
        if (tag.contains("SummonEntities", 9)) {
            ListTag listTag = tag.getList("SummonEntities", 10);
            for (int i = 0; i < listTag.size(); ++i) {
                CompoundTag compoundTag2 = listTag.getCompound(i);
                SummonData data = new SummonData();
                data.load(compoundTag2);
                list.add(data);
            }
        }
    }

    public void addSummonData(LivingEntity living) {
        if (list.stream().anyMatch(summonData -> {
            return summonData.getOwnerUUID() != living.getUUID();
        })) ;
        SummonData data = new SummonData();
        data.setOwnerUUID(living);
        list.add(data);
    }

    public void removeSummonData(LivingEntity living) {
        list.removeIf(summonData -> {
            return summonData.getOwnerUUID() == living.getUUID();
        });
    }

    public void removeSummonData(UUID uuid) {
        list.removeIf(summonData -> {
            return summonData.getOwnerUUID() == uuid;
        });
    }

    public List<SummonData> getList() {
        return list;
    }
}
