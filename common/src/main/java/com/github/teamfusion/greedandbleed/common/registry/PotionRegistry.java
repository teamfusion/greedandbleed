package com.github.teamfusion.greedandbleed.common.registry;

import com.blackgear.platform.core.CoreRegistry;
import com.github.teamfusion.greedandbleed.GreedAndBleed;
import com.github.teamfusion.greedandbleed.common.effect.ImmunityBodyEffect;
import com.github.teamfusion.greedandbleed.common.effect.StunEffect;
import com.github.teamfusion.greedandbleed.common.effect.WarpLinkEffect;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;

import java.util.Objects;
import java.util.function.Supplier;

public class PotionRegistry {
    public static final CoreRegistry<MobEffect> MOB_EFFECT = CoreRegistry.create(BuiltInRegistries.MOB_EFFECT, GreedAndBleed.MOD_ID);
    public static final CoreRegistry<Potion> POTION = CoreRegistry.create(BuiltInRegistries.POTION, GreedAndBleed.MOD_ID);

    public static final Supplier<MobEffect> IMMUNITY = MOB_EFFECT.register("immunity", () -> new ImmunityBodyEffect(MobEffectCategory.BENEFICIAL, 0x869B82));
    public static final Supplier<Potion> IMMUNITY_POTION = POTION.register("immunity", () -> new Potion(new MobEffectInstance(Objects.requireNonNull(IMMUNITY.get()), 3600)));

    public static final Supplier<Potion> LONG_IMMUNITY_POTION = POTION.register("long_immunity", () -> new Potion(new MobEffectInstance(Objects.requireNonNull(IMMUNITY.get()), 9600)));
    public static final Supplier<Potion> STRONG_IMMUNITY_POTION = POTION.register("strong_immunity", () -> new Potion(new MobEffectInstance(Objects.requireNonNull(IMMUNITY.get()), 1200, 1)));

    public static final Supplier<MobEffect> WARP_LINK = MOB_EFFECT.register("warp_link", () -> new WarpLinkEffect(MobEffectCategory.HARMFUL, 0x869B82));
    public static final Supplier<MobEffect> STUN = MOB_EFFECT.register("stun", () -> new StunEffect(MobEffectCategory.HARMFUL, 0xE45151));
}
