package com.nindybun.usefulguns.jeiPlugin;


import com.nindybun.usefulguns.UsefulGuns;
import com.nindybun.usefulguns.data.BulletBoreRecipe;
import com.nindybun.usefulguns.items.BoreKit;
import com.nindybun.usefulguns.modRegistries.ModItems;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapelessRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

import java.util.Arrays;
import java.util.stream.Stream;

public class BulletBoreRecipeMaker {
    private BulletBoreRecipeMaker() {}

    public static Stream<ICraftingRecipe> createRecipes() {
        String group = "jei.mining.bullet";
        return Arrays.stream(BoreKit.Kit.values()).map(kit -> {
            Ingredient casing = Ingredient.of(new ItemStack(ModItems.BULLET_CASING.get()));
            Ingredient bore = Ingredient.of(createKit(kit));
            ItemStack output = createBore(kit);
            NonNullList<Ingredient> inputs = NonNullList.of(Ingredient.EMPTY, casing, bore);
            ResourceLocation id = new ResourceLocation(UsefulGuns.MOD_ID, "jei.mining.bullet." + output.getDescriptionId());
            return new ShapelessRecipe(id, group, output, inputs);
        });
    }

    private static ItemStack createKit(BoreKit.Kit kit){
        ItemStack boreKit = ItemStack.EMPTY;
        switch (kit){
            case WOOD:
                boreKit = new ItemStack(ModItems.WOOD_BORE_KIT.get());
                break;
            case STONE:
                boreKit = new ItemStack(ModItems.STONE_BORE_KIT.get());
                break;
            case IRON:
                boreKit = new ItemStack(ModItems.IRON_BORE_KIT.get());
                break;
            case GOLD:
                boreKit = new ItemStack(ModItems.GOLD_BORE_KIT.get());
                break;
            case DIAMOND:
                boreKit = new ItemStack(ModItems.DIAMOND_BORE_KIT.get());
                break;
            case NETHERITE:
                boreKit = new ItemStack(ModItems.NETHERITE_BORE_KIT.get());
                break;
        }
        return boreKit;
    }

    private static ItemStack createBore(BoreKit.Kit kit){
        ItemStack bore = ItemStack.EMPTY;
        switch (kit){
            case WOOD:
                bore = new ItemStack(ModItems.WOOD_MINING_BULLET.get());
                break;
            case STONE:
                bore = new ItemStack(ModItems.STONE_MINING_BULLET.get());
                break;
            case IRON:
                bore = new ItemStack(ModItems.IRON_MINING_BULLET.get());
                break;
            case GOLD:
                bore = new ItemStack(ModItems.GOLD_MINING_BULLET.get());
                break;
            case DIAMOND:
                bore = new ItemStack(ModItems.DIAMOND_MINING_BULLET.get());
                break;
            case NETHERITE:
                bore = new ItemStack(ModItems.NETHERITE_MINING_BULLET.get());
                break;
        }
        return bore;
    }

}
