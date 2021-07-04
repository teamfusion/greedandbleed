package com.infernalstudios.greedandbleed.mixin;

import com.infernalstudios.greedandbleed.common.entity.GBCreatureAttribute;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IAngerable;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.monster.ZombifiedPiglinEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ZombifiedPiglinEntity.class)
public abstract class ZombifiedPiglinEntityMixin extends ZombieEntity implements IAngerable {

    public ZombifiedPiglinEntityMixin(EntityType<? extends ZombieEntity> p_i48549_1_, World p_i48549_2_) {
        super(p_i48549_1_, p_i48549_2_);
    }

    @Override
    public CreatureAttribute getMobType() {
        return GBCreatureAttribute.SWINE;
    }
}
