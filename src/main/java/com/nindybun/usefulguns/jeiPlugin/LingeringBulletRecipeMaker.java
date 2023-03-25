package com.nindybun.usefulguns.jeiPlugin;

import com.nindybun.usefulguns.UsefulGuns;
import com.nindybun.usefulguns.modRegistries.ModItems;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.stream.Stream;

public class LingeringBulletRecipeMaker {
    public static Stream<CraftingRecipe> createRecipes() {
        String group = "jei.lingering.bullet";
        return ForgeRegistries.POTIONS.getValues().stream()
                .map(potion -> {
                    ItemStack bulletStack = new ItemStack(ModItems.GLASS_BULLET.get());
                    ItemStack lingeringPotion = PotionUtils.setPotion(new ItemStack(Items.LINGERING_POTION), potion);
                    Ingredient bulletIngredient = Ingredient.of(bulletStack);
                    Ingredient potionIngredient = Ingredient.of(lingeringPotion);
                    NonNullList<Ingredient> inputs = NonNullList.of(Ingredient.EMPTY,
                            Ingredient.EMPTY, bulletIngredient, Ingredient.EMPTY,
                            bulletIngredient, potionIngredient, bulletIngredient,
                            Ingredient.EMPTY, bulletIngredient, Ingredient.EMPTY
                    );
                    ItemStack output = new ItemStack(ModItems.LINGERING_BULLET.get(), 4);
                    PotionUtils.setPotion(output, potion);
                    ResourceLocation id = new ResourceLocation(UsefulGuns.MOD_ID, "jei.lingering.bullet." + output.getDescriptionId());
                    return new ShapedRecipe(id, group, 3, 3, inputs, output);
                });
    }

    private LingeringBulletRecipeMaker() {

    }
}
