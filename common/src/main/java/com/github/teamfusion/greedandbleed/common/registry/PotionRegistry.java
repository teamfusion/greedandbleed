package com.github.teamfusion.greedandbleed.common.registry;

import com.github.teamfusion.greedandbleed.GreedAndBleed;
import com.github.teamfusion.greedandbleed.common.effect.ImmunityBodyEffect;
import com.github.teamfusion.greedandbleed.common.effect.StunEffect;
import com.github.teamfusion.greedandbleed.common.effect.WarpLinkEffect;
import com.github.teamfusion.greedandbleed.mixin.PotionBrewingMixin;
import com.github.teamfusion.greedandbleed.platform.CoreRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;

import java.util.Objects;
import java.util.function.Supplier;

public class PotionRegistry {
    public static final CoreRegistry<MobEffect> MOB_EFFECT = CoreRegistry.of(BuiltInRegistries.MOB_EFFECT, GreedAndBleed.MOD_ID);
    public static final CoreRegistry<Potion> POTION = CoreRegistry.of(BuiltInRegistries.POTION, GreedAndBleed.MOD_ID);

    public static final Supplier<MobEffect> IMMUNITY = MOB_EFFECT.create("immunity", () -> new ImmunityBodyEffect(MobEffectCategory.BENEFICIAL, 0x869B82));
    public static final Supplier<Potion> IMMUNITY_POTION = POTION.create("immunity", () -> new Potion(new MobEffectInstance(Objects.requireNonNull(IMMUNITY.get()), 3600)));

    public static final Supplier<Potion> LONG_IMMUNITY_POTION = POTION.create("long_immunity", () -> new Potion(new MobEffectInstance(Objects.requireNonNull(IMMUNITY.get()), 9600)));
    public static final Supplier<Potion> STRONG_IMMUNITY_POTION = POTION.create("strong_immunity", () -> new Potion(new MobEffectInstance(Objects.requireNonNull(IMMUNITY.get()), 1200, 1)));

    public static final Supplier<MobEffect> WARP_LINK = MOB_EFFECT.create("warp_link", () -> new WarpLinkEffect(MobEffectCategory.HARMFUL, 0x869B82));
    public static final Supplier<MobEffect> STUN = MOB_EFFECT.create("stun", () -> new StunEffect(MobEffectCategory.HARMFUL, 0xE45151));

    public static void init() {
        PotionBrewingMixin.invokeAddMix(Potions.AWKWARD, BlockRegistry.HOGDEW_FUNGUS.get().asItem(), IMMUNITY_POTION.get());
        PotionBrewingMixin.invokeAddMix(IMMUNITY_POTION.get(), Items.REDSTONE, LONG_IMMUNITY_POTION.get());
        PotionBrewingMixin.invokeAddMix(IMMUNITY_POTION.get(), Items.GLOWSTONE, STRONG_IMMUNITY_POTION.get());
    }
}
