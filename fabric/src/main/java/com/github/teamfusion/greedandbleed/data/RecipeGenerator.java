package com.github.teamfusion.greedandbleed.data;

import com.github.teamfusion.greedandbleed.common.registry.BlockRegistry;
import com.github.teamfusion.greedandbleed.common.registry.GBItemTags;
import com.github.teamfusion.greedandbleed.common.registry.ItemRegistry;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;

import java.util.function.Consumer;

public class RecipeGenerator extends FabricRecipeProvider {
    public RecipeGenerator(FabricDataOutput dataGenerator) {
        super(dataGenerator);
    }

    @Override
    public void buildRecipes(Consumer<FinishedRecipe> consumer) {
        planksFromLog(consumer, BlockRegistry.HOGDEW_PLANKS.get(), GBItemTags.HOGDEW_LOG, 4);
        woodFromLogs(consumer, Blocks.ACACIA_WOOD, BlockRegistry.HOGDEW_STEM.get());
        slabBuilder(RecipeCategory.BUILDING_BLOCKS, BlockRegistry.HOGDEW_PLANKS_SLAB.get(), Ingredient.of(BlockRegistry.HOGDEW_PLANKS.get().asItem()));
        stairBuilder(BlockRegistry.HOGDEW_PLANKS_STAIRS.get(), Ingredient.of(BlockRegistry.HOGDEW_PLANKS.get().asItem()));

        doorBuilder(BlockRegistry.HOGDEW_DOOR.get(), Ingredient.of(BlockRegistry.HOGDEW_PLANKS.get().asItem()));
        trapdoorBuilder(BlockRegistry.HOGDEW_TRAPDOOR.get(), Ingredient.of(BlockRegistry.HOGDEW_PLANKS.get().asItem()));
        fenceBuilder(BlockRegistry.HOGDEW_FENCE.get(), Ingredient.of(BlockRegistry.HOGDEW_PLANKS.get().asItem()));
        fenceGateBuilder(BlockRegistry.HOGDEW_FENCE_GATE.get(), Ingredient.of(BlockRegistry.HOGDEW_PLANKS.get().asItem()));

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ItemRegistry.CRIMSON_FUNGUS_ON_A_STICK.get())
                .define('S', Items.FISHING_ROD)
                .define('C', Items.CRIMSON_FUNGUS)
                .pattern("S ")
                .pattern(" C")
                .unlockedBy("has_item", has(Items.CRIMSON_FUNGUS)).save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ItemRegistry.GOLDEN_SHIELD.get())
                .define('G', Items.GOLD_NUGGET)
                .define('P', ItemTags.PLANKS)
                .pattern("GPG")
                .pattern("PPP")
                .pattern("GPG")
                .unlockedBy("has_item", has(Items.GOLD_NUGGET)).save(consumer);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, ItemRegistry.PEBBLE.get())
                .requires(Blocks.COBBLESTONE.asItem())
                .unlockedBy("has_item", has(Items.COBBLESTONE)).save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ItemRegistry.SLINGSHOT.get())
                .define('L', Items.LEATHER)
                .define('S', Items.STICK)
                .pattern("SLS")
                .pattern("SSS")
                .pattern(" S ")
                .unlockedBy("has_item", has(Items.LEATHER)).save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ItemRegistry.SLINGSHOT_POUCH.get())
                .define('L', Items.LEATHER)
                .pattern(" L ")
                .pattern("L L")
                .pattern("LLL")
                .unlockedBy("has_item", has(Items.LEATHER)).save(consumer);
    }
}