package com.nindybun.usefulguns.jeiPlugin;

import com.nindybun.usefulguns.UsefulGuns;
import com.nindybun.usefulguns.items.BoreKit;
import com.nindybun.usefulguns.modRegistries.ModItems;
import com.nindybun.usefulguns.util.Util;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapelessRecipe;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

import java.util.Arrays;
import java.util.stream.Stream;

public class BoreKitRepairRecipeMaker {
    public static Stream<ICraftingRecipe> createRecipes() {
        String group = "jei.borekit.repair";
        return Arrays.stream(BoreKit.Kit.values()).map(kit -> {
            ItemStack borekit = new ItemStack(Util.createKit(kit));
            borekit.getOrCreateTag().putInt(BoreKit.USES, 0);
            Ingredient borekitIngredient = Ingredient.of(borekit);
            Ingredient materialIngredient = Ingredient.of(kit == BoreKit.Kit.WOOD ? ItemTags.PLANKS : Util.createTag(kit));
            NonNullList<Ingredient> inputs = NonNullList.of(Ingredient.EMPTY,
                    borekitIngredient, materialIngredient, materialIngredient
            );
            ItemStack output = new ItemStack(Util.createKit(kit));
            output.getOrCreateTag().putInt(BoreKit.USES, kit.getUses());
            ResourceLocation id = new ResourceLocation(UsefulGuns.MOD_ID, "jei.borekit.repair." + output.getDescriptionId());
            return new ShapelessRecipe(id, group, output, inputs);
        });
    }

    private BoreKitRepairRecipeMaker() {

    }
}
