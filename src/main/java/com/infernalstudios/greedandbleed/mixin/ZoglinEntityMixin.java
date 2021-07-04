package com.infernalstudios.greedandbleed.mixin;

import com.infernalstudios.greedandbleed.common.entity.GBCreatureAttribute;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.IFlinging;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.ZoglinEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ZoglinEntity.class)
public abstract class ZoglinEntityMixin extends MonsterEntity implements IMob, IFlinging {

    protected ZoglinEntityMixin(EntityType<? extends MonsterEntity> p_i48553_1_, World p_i48553_2_) {
        super(p_i48553_1_, p_i48553_2_);
    }

    @Override
    public CreatureAttribute getMobType() {
        return GBCreatureAttribute.SWINE;
    }
}
