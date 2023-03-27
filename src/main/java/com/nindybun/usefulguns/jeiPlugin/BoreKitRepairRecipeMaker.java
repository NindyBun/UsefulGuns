package com.nindybun.usefulguns.jeiPlugin;

import com.nindybun.usefulguns.UsefulGuns;
import com.nindybun.usefulguns.items.BoreKit;
import com.nindybun.usefulguns.util.UtilMethods;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapelessRecipe;

import java.util.Arrays;
import java.util.stream.Stream;

public class BoreKitRepairRecipeMaker {
    public static Stream<CraftingRecipe> createRecipes() {
        String group = "jei.borekit.repair";
        return Arrays.stream(BoreKit.Kit.values()).map(kit -> {
            ItemStack borekit = new ItemStack(UtilMethods.createKit(kit));
            borekit.getOrCreateTag().putInt(BoreKit.USES, 0);
            Ingredient borekitIngredient = Ingredient.of(borekit);
            Ingredient materialIngredient = Ingredient.of(kit == BoreKit.Kit.WOOD ? ItemTags.PLANKS : UtilMethods.createTag(kit));
            NonNullList<Ingredient> inputs = NonNullList.of(Ingredient.EMPTY,
                    borekitIngredient, materialIngredient, materialIngredient
            );
            ItemStack output = new ItemStack(UtilMethods.createKit(kit));
            output.getOrCreateTag().putInt(BoreKit.USES, kit.getUses());
            ResourceLocation id = new ResourceLocation(UsefulGuns.MOD_ID, "jei.borekit.repair." + output.getDescriptionId());
            return new ShapelessRecipe(id, group, CraftingBookCategory.MISC, output, inputs);
        });
    }

    private BoreKitRepairRecipeMaker() {

    }
}
