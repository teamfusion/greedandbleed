package com.infernalstudios.greedandbleed.mixin;

import com.infernalstudios.greedandbleed.common.entity.GBCreatureAttribute;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.piglin.AbstractPiglinEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@SuppressWarnings("NullableProblems")
@Mixin(AbstractPiglinEntity.class)
public abstract class AbstractPiglinEntityMixin extends MonsterEntity {
    private AbstractPiglinEntityMixin(EntityType<? extends MonsterEntity> p_i48553_1_, World p_i48553_2_) {
        super(p_i48553_1_, p_i48553_2_);
    }

    @Override
    public CreatureAttribute getMobType() {
        return GBCreatureAttribute.PIGLIN;
    }
}
