package com.nindybun.usefulguns.jeiPlugin;

import com.nindybun.usefulguns.UsefulGuns;
import com.nindybun.usefulguns.items.BoreKit;
import com.nindybun.usefulguns.modRegistries.ModItems;
import com.nindybun.usefulguns.util.Util;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.potion.PotionUtils;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Arrays;
import java.util.stream.Stream;

public class BoreKitRecipeMaker {
    public static Stream<ICraftingRecipe> createRecipes() {
        String group = "jei.borekit";
        return Arrays.stream(BoreKit.Kit.values())
                .map(kit -> {
                    Ingredient materialIngredient = Ingredient.of(kit == BoreKit.Kit.WOOD ? ItemTags.PLANKS : Util.createTag(kit));
                    Ingredient flintIngredient = Ingredient.of(Items.FLINT);
                    Ingredient stoneIngredient = Ingredient.of(Tags.Items.STONE);
                    NonNullList<Ingredient> inputs = NonNullList.of(Ingredient.EMPTY,
                            materialIngredient, flintIngredient, materialIngredient,
                            stoneIngredient, stoneIngredient, stoneIngredient
                    );
                    ItemStack output = new ItemStack(Util.createKit(kit));
                    output.getOrCreateTag().putInt(BoreKit.USES, kit.getUses());
                    ResourceLocation id = new ResourceLocation(UsefulGuns.MOD_ID, "jei.borekit." + output.getDescriptionId());
                    return new ShapedRecipe(id, group, 3, 2, inputs, output);
                });
    }

    private BoreKitRecipeMaker() {

    }
}
