package com.nindybun.usefulguns.jeiPlugin;

import com.nindybun.usefulguns.UsefulGuns;
import com.nindybun.usefulguns.modRegistries.ModItems;
import mezz.jei.api.constants.ModIds;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.stream.Stream;

public class TippedBulletRecipeMaker {
    public static Stream<ICraftingRecipe> createRecipes() {
        String group = "jei.tipped.bullet";
        return ForgeRegistries.POTION_TYPES.getValues().stream()
                .map(potion -> {
                    ItemStack bulletStack = new ItemStack(ModItems.FLINT_BULLET.get());
                    ItemStack lingeringPotion = PotionUtils.setPotion(new ItemStack(Items.POTION), potion);
                    Ingredient bulletIngredient = Ingredient.of(bulletStack);
                    Ingredient potionIngredient = Ingredient.of(lingeringPotion);
                    NonNullList<Ingredient> inputs = NonNullList.of(Ingredient.EMPTY,
                            bulletIngredient, bulletIngredient, bulletIngredient,
                            bulletIngredient, potionIngredient, bulletIngredient,
                            bulletIngredient, bulletIngredient, bulletIngredient
                    );
                    ItemStack output = new ItemStack(ModItems.TIPPED_BULLET.get(), 8);
                    PotionUtils.setPotion(output, potion);
                    ResourceLocation id = new ResourceLocation(UsefulGuns.MOD_ID, "jei.tipped.bullet." + output.getDescriptionId());
                    return new ShapedRecipe(id, group, 3, 3, inputs, output);
                });
    }

    private TippedBulletRecipeMaker() {

    }
}
