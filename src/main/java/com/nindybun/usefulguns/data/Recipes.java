package com.nindybun.usefulguns.data;

import com.google.gson.JsonObject;
import com.nindybun.usefulguns.UsefulGuns;
import com.nindybun.usefulguns.crafting.TargetNBTIngredient;
import com.nindybun.usefulguns.crafting.WrappedRecipe;
import com.nindybun.usefulguns.modRegistries.ModItems;
import com.nindybun.usefulguns.modRegistries.ModRecipes;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.*;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.common.Tags;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;


public class Recipes extends RecipeProvider {
    public Recipes(DataGenerator generator) {
        super(generator.getPackOutput());
    }

    @Override
    protected @org.jetbrains.annotations.Nullable CompletableFuture<?> saveAdvancement(CachedOutput output, FinishedRecipe finishedRecipe, JsonObject advancementJson) {
        return super.saveAdvancement(output, finishedRecipe, advancementJson);
    }

    @Override
    protected void buildRecipes(@Nonnull Consumer<FinishedRecipe> consumer) {
        InventoryChangeTrigger.TriggerInstance nul = has(Items.AIR);

        CustomRecipeHandler.special(ModRecipes.TIPPED_BULLET_RECIPE.get()).save(consumer, "tipped_bullet");
        CustomRecipeHandler.special(ModRecipes.SPLASH_BULLET_RECIPE.get()).save(consumer, "splash_bullet");
        CustomRecipeHandler.special(ModRecipes.LINGERING_BULLET_RECIPE.get()).save(consumer, "lingering_bullet");
        CustomRecipeHandler.special(ModRecipes.BORE_BULLET_RECIPE.get()).save(consumer, "bore_bullet");
        CustomRecipeHandler.special(ModRecipes.BOREKIT_REPAIR_RECIPE.get()).save(consumer, "borekit_repair");
        CustomRecipeHandler.special(ModRecipes.BOREKIT_RECIPE.get()).save(consumer, "borekit");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.BREWING, Items.GUNPOWDER, 8)
                .requires(Items.CHARCOAL)
                .requires(Items.BLAZE_POWDER)
                .requires(Items.FLINT)
                .unlockedBy("", nul)
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.CLEANER.get())
                .pattern("  W")
                .pattern(" M ")
                .pattern("M  ")
                .define('W', ItemTags.WOOL)
                .define('M', Items.STICK)
                .unlockedBy("", nul)
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.BETTER_CLEANER.get())
                .pattern("  W")
                .pattern(" M ")
                .pattern("M  ")
                .define('W', ItemTags.WOOL)
                .define('M', Tags.Items.INGOTS_IRON)
                .unlockedBy("", nul)
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.BEST_CLEANER.get())
                .pattern("  W")
                .pattern(" M ")
                .pattern("M  ")
                .define('W', ItemTags.WOOL)
                .define('M', Tags.Items.GEMS_DIAMOND)
                .unlockedBy("", nul)
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.ULTIMATE_CLEANER.get())
                .pattern("  W")
                .pattern(" S ")
                .pattern("M  ")
                .define('W', ItemTags.WOOL)
                .define('S', Tags.Items.NETHER_STARS)
                .define('M', Tags.Items.INGOTS_NETHERITE)
                .unlockedBy("", nul)
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.FLINT_BULLET.get())
                .pattern(" A ")
                .pattern(" A ")
                .pattern(" B ")
                .define('A', Items.FLINT)
                .define('B', ModItems.BULLET_CASING.get())
                .unlockedBy("", nul)
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.IRON_BULLET.get())
                .pattern(" A ")
                .pattern(" A ")
                .pattern(" B ")
                .define('A', Tags.Items.NUGGETS_IRON)
                .define('B', ModItems.BULLET_CASING.get())
                .unlockedBy("", nul)
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.DIAMOND_BULLET.get(), 8)
                .pattern("BBB")
                .pattern("BAB")
                .pattern("BBB")
                .define('A', Tags.Items.GEMS_DIAMOND)
                .define('B', ModItems.BULLET_CASING.get())
                .unlockedBy("", nul)
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.ARMOR_PIERCING_BULLET.get(), 8)
                .pattern("BBB")
                .pattern("BAB")
                .pattern("BBB")
                .define('A', Tags.Items.INGOTS_NETHERITE)
                .define('B', ModItems.DIAMOND_BULLET.get())
                .unlockedBy("", nul)
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.HOLLOW_POINT_BULLET.get())
                .pattern(" I ")
                .pattern("CGC")
                .pattern(" B ")
                .define('I', Tags.Items.INGOTS_IRON)
                .define('G', Tags.Items.INGOTS_GOLD)
                .define('C', Tags.Items.INGOTS_COPPER)
                .define('B', ModItems.BULLET_CASING.get())
                .unlockedBy("", nul)
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.DRAGONS_BREATH_BULLET.get())
                .pattern(" A ")
                .pattern(" A ")
                .pattern(" B ")
                .define('A', Items.FIRE_CHARGE)
                .define('B', ModItems.BUCKSHOT_BULLET.get())
                .unlockedBy("", nul)
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.DRAGONS_FIREBALL_BULLET.get())
                .pattern(" G ")
                .pattern(" A ")
                .pattern(" B ")
                .define('G', Items.DRAGON_BREATH)
                .define('A', Items.FIRE_CHARGE)
                .define('B', ModItems.BULLET_CASING.get())
                .unlockedBy("", nul)
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.BUCKSHOT_BULLET.get())
                .pattern(" A ")
                .pattern(" A ")
                .pattern(" B ")
                .define('A', Tags.Items.GRAVEL)
                .define('B', ModItems.BULLET_CASING.get())
                .unlockedBy("", nul)
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.BIRDSHOT_BULLET.get())
                .pattern(" A ")
                .pattern(" A ")
                .pattern(" B ")
                .define('A', Tags.Items.SAND)
                .define('B', ModItems.BULLET_CASING.get())
                .unlockedBy("", nul)
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.SLUG_BULLET.get(), 2)
                .pattern(" B ")
                .pattern(" A ")
                .pattern(" B ")
                .define('A', Items.ANVIL)
                .define('B', ModItems.BULLET_CASING.get())
                .unlockedBy("", nul)
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.GLASS_BULLET.get(), 4)
                .pattern(" B ")
                .pattern("BGB")
                .pattern(" B ")
                .define('G', Tags.Items.GLASS)
                .define('B', ModItems.BULLET_CASING.get())
                .unlockedBy("", nul)
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.EXPLOSIVE_BULLET.get(), 2)
                .pattern(" B ")
                .pattern(" A ")
                .pattern(" B ")
                .define('A', Items.TNT)
                .define('B', ModItems.BULLET_CASING.get())
                .unlockedBy("", nul)
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, ModItems.ENDER_BULLET.get())
                .requires(ModItems.BULLET_CASING.get())
                .requires(Items.ENDER_PEARL)
                .unlockedBy("", nul)
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, ModItems.TORCH_BULLET.get())
                .requires(ModItems.BULLET_CASING.get())
                .requires(Items.TORCH)
                .unlockedBy("", nul)
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.IRON_GUN.get())
                .pattern("IC ")
                .pattern("BIF")
                .pattern(" SI")
                .define('I', Tags.Items.INGOTS_IRON)
                .define('B', Items.STONE_BUTTON)
                .define('C', ModItems.BULLET_CASING.get())
                .define('F', Items.FLINT_AND_STEEL)
                .define('S', Items.STICK)
                .unlockedBy("", nul)
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.GOLD_GUN.get())
                .pattern("IC ")
                .pattern("BIF")
                .pattern(" SI")
                .define('I', Tags.Items.INGOTS_GOLD)
                .define('B', Items.STONE_BUTTON)
                .define('C', ModItems.BULLET_CASING.get())
                .define('F', Items.FLINT_AND_STEEL)
                .define('S', Items.STICK)
                .unlockedBy("", nul)
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.DIAMOND_SHOTGUN.get())
                .pattern("DC ")
                .pattern("BDF")
                .pattern(" SW")
                .define('D', Tags.Items.GEMS_DIAMOND)
                .define('B', Items.STONE_BUTTON)
                .define('C', ModItems.BULLET_CASING.get())
                .define('F', Items.FLINT_AND_STEEL)
                .define('S', Items.STICK)
                .define('W', ItemTags.LOGS)
                .unlockedBy("", nul)
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.DIAMOND_SNIPER.get())
                .pattern("DCG")
                .pattern("BDF")
                .pattern(" SW")
                .define('D', Tags.Items.GEMS_DIAMOND)
                .define('B', Items.STONE_BUTTON)
                .define('C', ModItems.BULLET_CASING.get())
                .define('F', Items.FLINT_AND_STEEL)
                .define('S', Items.STICK)
                .define('W', ItemTags.LOGS)
                .define('G', Tags.Items.GLASS)
                .unlockedBy("", nul)
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.DIAMOND_MINIGUN.get())
                .pattern("DCR")
                .pattern("BDP")
                .pattern("RFD")
                .define('D', Tags.Items.GEMS_DIAMOND)
                .define('B', Items.STONE_BUTTON)
                .define('C', ModItems.BULLET_CASING.get())
                .define('F', Items.FLINT_AND_STEEL)
                .define('P', ItemTags.PLANKS)
                .define('R', Tags.Items.DUSTS_REDSTONE)
                .unlockedBy("", nul)
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.BULLET_CASING.get(), 16)
                .pattern("C C")
                .pattern("CGC")
                .pattern("IFI")
                .define('C', Tags.Items.INGOTS_COPPER)
                .define('I', Tags.Items.INGOTS_IRON)
                .define('G', Tags.Items.GUNPOWDER)
                .define('F', Items.FLINT)
                .unlockedBy("", nul)
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.LEATHER_POUCH.get())
                .pattern("TLT")
                .pattern("LCL")
                .pattern("SLS")
                .define('L', Tags.Items.LEATHER)
                .define('T', Items.TRIPWIRE_HOOK)
                .define('C', Tags.Items.CHESTS)
                .define('S', Tags.Items.STRING)
                .unlockedBy("", nul)
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.IRON_POUCH.get())
                .pattern("III")
                .pattern("IPI")
                .pattern("CBC")
                .define('C', Tags.Items.CHESTS)
                .define('P', TargetNBTIngredient.of(ModItems.LEATHER_POUCH.get()))
                .define('I', Tags.Items.INGOTS_IRON)
                .define('B', Tags.Items.STORAGE_BLOCKS_IRON)
                .unlockedBy("", nul)
                .save(WrappedRecipe.Inject(consumer, ModRecipes.COPY_RECIPE.get()));

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.GOLD_POUCH.get())
                .pattern("III")
                .pattern("IPI")
                .pattern("CBC")
                .define('C', Tags.Items.CHESTS)
                .define('P', TargetNBTIngredient.of(ModItems.IRON_POUCH.get()))
                .define('I', Tags.Items.INGOTS_GOLD)
                .define('B', Tags.Items.STORAGE_BLOCKS_GOLD)
                .unlockedBy("", nul)
                .save(WrappedRecipe.Inject(consumer, ModRecipes.COPY_RECIPE.get()));

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.DIAMOND_POUCH.get())
                .pattern("III")
                .pattern("IPI")
                .pattern("CBC")
                .define('C', Tags.Items.CHESTS)
                .define('P', TargetNBTIngredient.of(ModItems.GOLD_POUCH.get()))
                .define('I', Tags.Items.GEMS_DIAMOND)
                .define('B', Tags.Items.STORAGE_BLOCKS_DIAMOND)
                .unlockedBy("", nul)
                .save(WrappedRecipe.Inject(consumer, ModRecipes.COPY_RECIPE.get()));

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.OBSIDIAN_POUCH.get())
                .pattern("III")
                .pattern("OPO")
                .pattern("COC")
                .define('C', Tags.Items.CHESTS)
                .define('P', TargetNBTIngredient.of(ModItems.DIAMOND_POUCH.get()))
                .define('O', Tags.Items.OBSIDIAN)
                .define('I', Items.IRON_BARS)
                .unlockedBy("", nul)
                .save(WrappedRecipe.Inject(consumer, ModRecipes.COPY_RECIPE.get()));

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.NETHERITE_POUCH.get())
                .pattern("III")
                .pattern("IPI")
                .pattern("CBC")
                .define('C', Tags.Items.CHESTS)
                .define('P', TargetNBTIngredient.of(ModItems.OBSIDIAN_POUCH.get()))
                .define('I', Tags.Items.INGOTS_NETHERITE)
                .define('B', Tags.Items.STORAGE_BLOCKS_NETHERITE)
                .unlockedBy("", nul)
                .save(WrappedRecipe.Inject(consumer, ModRecipes.COPY_RECIPE.get()));

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.NETHERSTAR_POUCH.get())
                .pattern("III")
                .pattern("IPI")
                .pattern("CBC")
                .define('C', Tags.Items.CHESTS)
                .define('P', TargetNBTIngredient.of(ModItems.NETHERITE_POUCH.get()))
                .define('I', Tags.Items.NETHER_STARS)
                .define('B', Items.END_ROD)
                .unlockedBy("", nul)
                .save(WrappedRecipe.Inject(consumer, ModRecipes.COPY_RECIPE.get()));

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.OMEGA_POUCH.get())
                .pattern("IRI")
                .pattern("RPR")
                .pattern("CEC")
                .define('C', Tags.Items.CHESTS)
                .define('P', TargetNBTIngredient.of(ModItems.NETHERSTAR_POUCH.get()))
                .define('I', Items.DRAGON_HEAD)
                .define('E', Items.ENDER_EYE)
                .define('R', Items.WITHER_SKELETON_SKULL)
                .unlockedBy("", nul)
                .save(WrappedRecipe.Inject(consumer, ModRecipes.COPY_RECIPE.get()));
    }

}

class CustomRecipeHandler {
    private final RecipeSerializer<?> serializer;

    public CustomRecipeHandler(RecipeSerializer<?> p_i50786_1_) {
        this.serializer = p_i50786_1_;
    }

    public static CustomRecipeHandler special(RecipeSerializer<?> p_218656_0_) {
        return new CustomRecipeHandler(p_218656_0_);
    }

    public void save(Consumer<FinishedRecipe> p_200499_1_, final String p_200499_2_) {
        p_200499_1_.accept(new FinishedRecipe() {
            public void serializeRecipeData(JsonObject p_218610_1_) {
            }

            public RecipeSerializer<?> getType() {
                return CustomRecipeHandler.this.serializer;
            }

            public ResourceLocation getId() {
                return new ResourceLocation(UsefulGuns.MOD_ID, p_200499_2_);
            }

            @Nullable
            public JsonObject serializeAdvancement() {
                return null;
            }

            public ResourceLocation getAdvancementId() {
                return new ResourceLocation("");
            }
        });
    }
}