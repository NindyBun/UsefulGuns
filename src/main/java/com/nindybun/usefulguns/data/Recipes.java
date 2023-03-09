package com.nindybun.usefulguns.data;

import com.google.gson.JsonObject;
import com.nindybun.usefulguns.UsefulGuns;
import com.nindybun.usefulguns.crafting.TargetNBTIngredient;
import com.nindybun.usefulguns.crafting.WrappedRecipe;
import com.nindybun.usefulguns.modRegistries.ModItems;
import com.nindybun.usefulguns.modRegistries.ModRecipes;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.data.*;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
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

        CustomRecipe.special(ModRecipes.TIPPED_BULLET_RECIPE.get()).save(consumer, "tipped_bullet");
        CustomRecipe.special(ModRecipes.SPLASH_BULLET_RECIPE.get()).save(consumer, "splash_bullet");
        CustomRecipe.special(ModRecipes.LINGERING_BULLET_RECIPE.get()).save(consumer, "lingering_bullet");

        ShapedRecipeBuilder.shaped(ModItems.FLINT_BULLET.get())
                .pattern(" A ")
                .pattern(" A ")
                .pattern(" B ")
                .define('A', Items.FLINT)
                .define('B', ModItems.BULLET_CASING.get())
                .unlockedBy("", nul)
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModItems.IRON_BULLET.get())
                .pattern(" A ")
                .pattern(" A ")
                .pattern(" B ")
                .define('A', Tags.Items.NUGGETS_IRON)
                .define('B', ModItems.BULLET_CASING.get())
                .unlockedBy("", nul)
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModItems.DRAGONS_BREATH_BULLET.get())
                .pattern(" A ")
                .pattern(" A ")
                .pattern(" B ")
                .define('A', Items.FIRE_CHARGE)
                .define('B', ModItems.BUCKSHOT_BULLET.get())
                .unlockedBy("", nul)
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModItems.DRAGONS_FIREBALL_BULLET.get())
                .pattern(" G ")
                .pattern(" A ")
                .pattern(" B ")
                .define('G', Items.DRAGON_BREATH)
                .define('A', Items.FIRE_CHARGE)
                .define('B', ModItems.BULLET_CASING.get())
                .unlockedBy("", nul)
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModItems.BUCKSHOT_BULLET.get())
                .pattern(" A ")
                .pattern(" A ")
                .pattern(" B ")
                .define('A', Tags.Items.GRAVEL)
                .define('B', ModItems.BULLET_CASING.get())
                .unlockedBy("", nul)
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModItems.BIRDSHOT_BULLET.get())
                .pattern(" A ")
                .pattern(" A ")
                .pattern(" B ")
                .define('A', Tags.Items.SAND)
                .define('B', ModItems.BULLET_CASING.get())
                .unlockedBy("", nul)
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModItems.SLUG_BULLET.get(), 2)
                .pattern(" B ")
                .pattern(" A ")
                .pattern(" B ")
                .define('A', Items.ANVIL)
                .define('B', ModItems.BULLET_CASING.get())
                .unlockedBy("", nul)
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModItems.GLASS_BULLET.get(), 4)
                .pattern(" B ")
                .pattern("BGB")
                .pattern(" B ")
                .define('G', Tags.Items.GLASS)
                .define('B', ModItems.BULLET_CASING.get())
                .unlockedBy("", nul)
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModItems.IRON_GUN.get())
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

        ShapedRecipeBuilder.shaped(ModItems.GOLD_GUN.get())
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

        ShapedRecipeBuilder.shaped(ModItems.DIAMOND_SHOTGUN.get())
                .pattern("DC ")
                .pattern("BDF")
                .pattern(" SW")
                .define('D', Tags.Items.GEMS_DIAMOND)
                .define('B', Items.STONE_BUTTON)
                .define('C', ModItems.BULLET_CASING.get())
                .define('F', Items.FLINT_AND_STEEL)
                .define('S', Items.STICK)
                .define('W', Items.OAK_LOG)
                .unlockedBy("", nul)
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModItems.DIAMOND_SNIPER.get())
                .pattern("DCG")
                .pattern("BDF")
                .pattern(" SW")
                .define('D', Tags.Items.GEMS_DIAMOND)
                .define('B', Items.STONE_BUTTON)
                .define('C', ModItems.BULLET_CASING.get())
                .define('F', Items.FLINT_AND_STEEL)
                .define('S', Items.STICK)
                .define('W', Items.OAK_LOG)
                .define('G', Tags.Items.GLASS)
                .unlockedBy("", nul)
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModItems.DIAMOND_MINIGUN.get())
                .pattern("DCR")
                .pattern("BDS")
                .pattern("RFD")
                .define('D', Tags.Items.GEMS_DIAMOND)
                .define('B', Items.STONE_BUTTON)
                .define('C', ModItems.BULLET_CASING.get())
                .define('F', Items.FLINT_AND_STEEL)
                .define('S', Items.STICK)
                .define('R', Tags.Items.DUSTS_REDSTONE)
                .unlockedBy("", nul)
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModItems.BULLET_CASING.get(), 8)
                .pattern("C C")
                .pattern("CGC")
                .pattern("CFC")
                .define('C', Tags.Items.INGOTS_IRON)
                .define('G', Tags.Items.GUNPOWDER)
                .define('F', Items.FLINT)
                .unlockedBy("", nul)
                .save(consumer);

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

class CustomRecipe {
    private final SpecialRecipeSerializer<?> serializer;

    public CustomRecipe(SpecialRecipeSerializer<?> p_i50786_1_) {
        this.serializer = p_i50786_1_;
    }

    public static CustomRecipe special(SpecialRecipeSerializer<?> p_218656_0_) {
        return new CustomRecipe(p_218656_0_);
    }

    public void save(Consumer<IFinishedRecipe> p_200499_1_, final String p_200499_2_) {
        p_200499_1_.accept(new IFinishedRecipe() {
            public void serializeRecipeData(JsonObject p_218610_1_) {
            }

            public IRecipeSerializer<?> getType() {
                return CustomRecipe.this.serializer;
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