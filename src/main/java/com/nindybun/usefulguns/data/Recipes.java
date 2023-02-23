package com.nindybun.usefulguns.data;

import com.google.gson.JsonObject;
import com.nindybun.usefulguns.crafting.TargetNBTIngredient;
import com.nindybun.usefulguns.crafting.WrappedRecipe;
import com.nindybun.usefulguns.modRegistries.ModItems;
import com.nindybun.usefulguns.modRegistries.ModRecipes;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.data.*;
import net.minecraft.item.Items;
import net.minecraftforge.common.Tags;

import javax.annotation.Nonnull;
import java.nio.file.Path;
import java.util.function.Consumer;

public class Recipes extends RecipeProvider {
    public Recipes(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void saveAdvancement(DirectoryCache p_208310_0_, JsonObject p_208310_1_, Path p_208310_2_) {
    }

    @Override
    protected void buildShapelessRecipes(@Nonnull Consumer<IFinishedRecipe> consumer) {
        InventoryChangeTrigger.Instance nul = has(Items.AIR);
        ShapedRecipeBuilder.shaped(ModItems.LEATHER_POUCH.get())
                .pattern("TLT")
                .pattern("LCL")
                .pattern("SLS")
                .define('L', Tags.Items.LEATHER)
                .define('T', Items.TRIPWIRE_HOOK)
                .define('C', Tags.Items.CHESTS)
                .define('S', Tags.Items.STRING)
                .unlockedBy("", nul)
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModItems.IRON_POUCH.get())
                .pattern("III")
                .pattern("IPI")
                .pattern("CBC")
                .define('C', Tags.Items.CHESTS)
                .define('P', TargetNBTIngredient.of(ModItems.LEATHER_POUCH.get()))
                .define('I', Tags.Items.INGOTS_IRON)
                .define('B', Tags.Items.STORAGE_BLOCKS_IRON)
                .unlockedBy("", nul)
                .save(WrappedRecipe.Inject(consumer, ModRecipes.COPY_RECIPE.get()));

        ShapedRecipeBuilder.shaped(ModItems.GOLD_POUCH.get())
                .pattern("III")
                .pattern("IPI")
                .pattern("CBC")
                .define('C', Tags.Items.CHESTS)
                .define('P', TargetNBTIngredient.of(ModItems.IRON_POUCH.get()))
                .define('I', Tags.Items.INGOTS_GOLD)
                .define('B', Tags.Items.STORAGE_BLOCKS_GOLD)
                .unlockedBy("", nul)
                .save(WrappedRecipe.Inject(consumer, ModRecipes.COPY_RECIPE.get()));

        ShapedRecipeBuilder.shaped(ModItems.DIAMOND_POUCH.get())
                .pattern("III")
                .pattern("IPI")
                .pattern("CBC")
                .define('C', Tags.Items.CHESTS)
                .define('P', TargetNBTIngredient.of(ModItems.GOLD_POUCH.get()))
                .define('I', Tags.Items.GEMS_DIAMOND)
                .define('B', Tags.Items.STORAGE_BLOCKS_DIAMOND)
                .unlockedBy("", nul)
                .save(WrappedRecipe.Inject(consumer, ModRecipes.COPY_RECIPE.get()));

        ShapedRecipeBuilder.shaped(ModItems.OBSIDIAN_POUCH.get())
                .pattern("III")
                .pattern("OPO")
                .pattern("COC")
                .define('C', Tags.Items.CHESTS)
                .define('P', TargetNBTIngredient.of(ModItems.DIAMOND_POUCH.get()))
                .define('O', Tags.Items.OBSIDIAN)
                .define('I', Items.IRON_BARS)
                .unlockedBy("", nul)
                .save(WrappedRecipe.Inject(consumer, ModRecipes.COPY_RECIPE.get()));

        ShapedRecipeBuilder.shaped(ModItems.NETHERITE_POUCH.get())
                .pattern("III")
                .pattern("IPI")
                .pattern("CBC")
                .define('C', Tags.Items.CHESTS)
                .define('P', TargetNBTIngredient.of(ModItems.OBSIDIAN_POUCH.get()))
                .define('I', Tags.Items.INGOTS_NETHERITE)
                .define('B', Tags.Items.STORAGE_BLOCKS_NETHERITE)
                .unlockedBy("", nul)
                .save(WrappedRecipe.Inject(consumer, ModRecipes.COPY_RECIPE.get()));

        ShapedRecipeBuilder.shaped(ModItems.NETHERSTAR_POUCH.get())
                .pattern("III")
                .pattern("IPI")
                .pattern("CBC")
                .define('C', Tags.Items.CHESTS)
                .define('P', TargetNBTIngredient.of(ModItems.NETHERITE_POUCH.get()))
                .define('I', Tags.Items.NETHER_STARS)
                .define('B', Items.END_ROD)
                .unlockedBy("", nul)
                .save(WrappedRecipe.Inject(consumer, ModRecipes.COPY_RECIPE.get()));
    }


}
