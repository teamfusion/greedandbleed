package com.infernalstudios.greedandbleed.mixin;

import com.infernalstudios.greedandbleed.common.entity.GBCreatureAttribute;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IEquipable;
import net.minecraft.entity.IRideable;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PigEntity.class)
public abstract class PigEntityMixin extends AnimalEntity implements IRideable, IEquipable {

    protected PigEntityMixin(EntityType<? extends AnimalEntity> p_i48568_1_, World p_i48568_2_) {
        super(p_i48568_1_, p_i48568_2_);
    }

    @Override
    public CreatureAttribute getMobType() {
        return GBCreatureAttribute.SWINE;
    }
}
