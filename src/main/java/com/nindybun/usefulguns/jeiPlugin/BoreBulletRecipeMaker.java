package com.nindybun.usefulguns.jeiPlugin;

import com.nindybun.usefulguns.UsefulGuns;
import com.nindybun.usefulguns.items.BoreKit;
import com.nindybun.usefulguns.modRegistries.ModItems;
import com.nindybun.usefulguns.util.UtilMethods;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapelessRecipe;

import java.util.Arrays;
import java.util.stream.Stream;

public class BoreBulletRecipeMaker {
    public static Stream<CraftingRecipe> createRecipes() {
        String group = "jei.bore.bullet";
        return Arrays.stream(BoreKit.Kit.values()).map(kit -> {
            ItemStack casing = new ItemStack(ModItems.BULLET_CASING.get());
            Ingredient casingIngredient = Ingredient.of(casing);
            ItemStack borekit = new ItemStack(UtilMethods.createKit(kit));
            borekit.getOrCreateTag().putInt(BoreKit.USES, kit.getUses());
            Ingredient kitIngredient = Ingredient.of(borekit);
            NonNullList<Ingredient> inputs = NonNullList.of(Ingredient.EMPTY,
                    kitIngredient, casingIngredient
            );
            ItemStack output = new ItemStack(UtilMethods.createBore(kit));
            ResourceLocation id = new ResourceLocation(UsefulGuns.MOD_ID, "jei.bore.bullet." + output.getDescriptionId());
            return new ShapelessRecipe(id, group, CraftingBookCategory.MISC, output, inputs);
        });
    }

    private BoreBulletRecipeMaker() {

    }
}
