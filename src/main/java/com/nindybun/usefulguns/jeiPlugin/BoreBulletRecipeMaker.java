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
import net.minecraft.item.crafting.ShapelessRecipe;
import net.minecraft.potion.PotionUtils;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Arrays;
import java.util.stream.Stream;

public class BoreBulletRecipeMaker {
    public static Stream<ICraftingRecipe> createRecipes() {
        String group = "jei.bore.bullet";
        return Arrays.stream(BoreKit.Kit.values()).map(kit -> {
            ItemStack casing = new ItemStack(ModItems.BULLET_CASING.get());
            Ingredient casingIngredient = Ingredient.of(casing);
            ItemStack borekit = new ItemStack(Util.createKit(kit));
            borekit.getOrCreateTag().putInt(BoreKit.USES, kit.getUses());
            Ingredient kitIngredient = Ingredient.of(borekit);
            NonNullList<Ingredient> inputs = NonNullList.of(Ingredient.EMPTY,
                    kitIngredient, casingIngredient
            );
            ItemStack output = new ItemStack(Util.createBore(kit));
            ResourceLocation id = new ResourceLocation(UsefulGuns.MOD_ID, "jei.bore.bullet." + output.getDescriptionId());
            return new ShapelessRecipe(id, group, output, inputs);
        });
    }

    private BoreBulletRecipeMaker() {

    }
}
