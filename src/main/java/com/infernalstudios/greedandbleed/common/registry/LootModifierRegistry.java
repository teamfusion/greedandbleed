package com.infernalstudios.greedandbleed.common.registry;

import com.google.gson.JsonObject;
import com.infernalstudios.greedandbleed.GreedAndBleed;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.List;

// COPIED FROM INFERNAL EXPANSION
public class LootModifierRegistry {

    public static final DeferredRegister<GlobalLootModifierSerializer<?>> LOOT_MODIFIERS = DeferredRegister.create(ForgeRegistries.LOOT_MODIFIER_SERIALIZERS, GreedAndBleed.MODID);

    public static final RegistryObject<GlobalLootModifierSerializer<HoglinLootModifier>> HOGLIN_LOOT_MODIFIER = LOOT_MODIFIERS.register("hoglin_loot_modifier", HoglinLootSerializer::new);

    private static class HoglinLootModifier extends LootModifier {

        /**
         * Constructs a LootModifier.
         *
         * @param conditionsIn the ILootConditions that need to be matched before the loot is modified.
         */
        protected HoglinLootModifier(ILootCondition[] conditionsIn) {
            super(conditionsIn);
        }

        @Nonnull
        @Override
        protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {

            int numChops = 0;
            int numCookedChops = 0;

            for (ItemStack item : generatedLoot) {
                if (item.sameItem(Items.PORKCHOP.getDefaultInstance())) {
                    numChops += item.getCount();
                } else if (item.sameItem(Items.COOKED_PORKCHOP.getDefaultInstance())) {
                    numCookedChops += item.getCount();
                }
            }

            generatedLoot.removeIf(x -> x.sameItem(Items.PORKCHOP.getDefaultInstance()));
            generatedLoot.removeIf(x -> x.sameItem(Items.COOKED_PORKCHOP.getDefaultInstance()));
            generatedLoot.add(new ItemStack(ItemRegistry.COOKED_HOGCHOP.get(), numCookedChops));
            generatedLoot.add(new ItemStack(ItemRegistry.RAW_HOGCHOP.get(), numChops));

            return generatedLoot;
        }
    }

    private static class HoglinLootSerializer extends GlobalLootModifierSerializer<HoglinLootModifier> {

        @Override
        public HoglinLootModifier read(ResourceLocation location, JsonObject object, ILootCondition[] conditionsIn) {
            return new HoglinLootModifier(conditionsIn);
        }

        @Override
        public JsonObject write(HoglinLootModifier instance) {
            return null;
        }
    }
}