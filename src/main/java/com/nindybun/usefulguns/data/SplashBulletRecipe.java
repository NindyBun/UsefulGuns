package com.nindybun.usefulguns.data;

import com.nindybun.usefulguns.modRegistries.ModItems;
import com.nindybun.usefulguns.modRegistries.ModRecipes;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class SplashBulletRecipe extends SpecialRecipe {
    public SplashBulletRecipe(ResourceLocation p_i48169_1_) {
        super(p_i48169_1_);
    }

    public boolean matches(CraftingInventory p_77569_1_, World p_77569_2_) {
        if (p_77569_1_.getWidth() == 3 && p_77569_1_.getHeight() == 3) {
            for(int i = 0; i < p_77569_1_.getWidth(); ++i) {
                for(int j = 0; j < p_77569_1_.getHeight(); ++j) {
                    ItemStack itemstack = p_77569_1_.getItem(i + j * p_77569_1_.getWidth());
                    /*if (itemstack.isEmpty()) {
                        return false;
                    }*/

                    Item item = itemstack.getItem();
                    if (i == 1 && j == 1 && item != Items.SPLASH_POTION) {
                        return false;
                    } else if (i == 0 && j == 1 && item != ModItems.GLASS_BULLET.get()) {
                        return false;
                    } else if (i == 1 && j == 0 && item != ModItems.GLASS_BULLET.get()) {
                        return false;
                    } else if (i == 1 && j == 2 && item != ModItems.GLASS_BULLET.get()) {
                        return false;
                    } else if (i == 2 && j == 1 && item != ModItems.GLASS_BULLET.get()) {
                        return false;
                    } else if (i == 0 && j == 0 && item != ItemStack.EMPTY.getItem()) {
                        return false;
                    } else if (i == 0 && j == 2 && item != ItemStack.EMPTY.getItem()) {
                        return false;
                    } else if (i == 2 && j == 0 && item != ItemStack.EMPTY.getItem()) {
                        return false;
                    } else if (i == 2 && j == 2 && item != ItemStack.EMPTY.getItem()) {
                        return false;
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }

    public ItemStack assemble(CraftingInventory p_77572_1_) {
        ItemStack itemstack = p_77572_1_.getItem(1 + p_77572_1_.getWidth());
        if (itemstack.getItem() != Items.SPLASH_POTION) {
            return ItemStack.EMPTY;
        } else {
            ItemStack itemstack1 = new ItemStack(ModItems.SPLASH_BULLET.get(), 4);
            PotionUtils.setPotion(itemstack1, PotionUtils.getPotion(itemstack));
            PotionUtils.setCustomEffects(itemstack1, PotionUtils.getCustomEffects(itemstack));
            return itemstack1;
        }
    }

    public boolean canCraftInDimensions(int p_194133_1_, int p_194133_2_) {
        return p_194133_1_ >= 2 && p_194133_2_ >= 2;
    }

    public IRecipeSerializer<?> getSerializer() {
        return ModRecipes.SPLASH_BULLET_RECIPE.get();
    }
}
