package com.nindybun.usefulguns.jeiPlugin;

import com.nindybun.usefulguns.UsefulGuns;
import com.nindybun.usefulguns.items.BoreKit;
import com.nindybun.usefulguns.util.UtilMethods;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraftforge.common.Tags;

import java.util.Arrays;
import java.util.stream.Stream;

public class BoreKitRecipeMaker {
    public static Stream<CraftingRecipe> createRecipes() {
        String group = "jei.borekit";
        return Arrays.stream(BoreKit.Kit.values())
                .map(kit -> {
                    Ingredient materialIngredient = Ingredient.of(kit == BoreKit.Kit.WOOD ? ItemTags.PLANKS : UtilMethods.createTag(kit));
                    Ingredient flintIngredient = Ingredient.of(Items.FLINT);
                    Ingredient stoneIngredient = Ingredient.of(Tags.Items.STONE);
                    NonNullList<Ingredient> inputs = NonNullList.of(Ingredient.EMPTY,
                            materialIngredient, flintIngredient, materialIngredient,
                            stoneIngredient, stoneIngredient, stoneIngredient
                    );
                    ItemStack output = new ItemStack(UtilMethods.createKit(kit));
                    output.getOrCreateTag().putInt(BoreKit.USES, kit.getUses());
                    ResourceLocation id = new ResourceLocation(UsefulGuns.MOD_ID, "jei.borekit." + output.getDescriptionId());
                    return new ShapedRecipe(id, group, 3, 2, inputs, output);
                });
    }

    private BoreKitRecipeMaker() {

    }
}
