package com.nindybun.usefulguns.data;

import com.nindybun.usefulguns.modRegistries.ModItems;
import com.nindybun.usefulguns.modRegistries.ModRecipes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class TippedBulletRecipe extends CustomRecipe {
    public TippedBulletRecipe(ResourceLocation p_i48169_1_) {
        super(p_i48169_1_);
    }

    public boolean matches(CraftingContainer p_77569_1_, Level p_77569_2_) {
        if (p_77569_1_.getWidth() == 3 && p_77569_1_.getHeight() == 3) {
            for(int i = 0; i < p_77569_1_.getWidth(); ++i) {
                for(int j = 0; j < p_77569_1_.getHeight(); ++j) {
                    ItemStack itemstack = p_77569_1_.getItem(i + j * p_77569_1_.getWidth());
                    if (itemstack.isEmpty()) {
                        return false;
                    }

                    Item item = itemstack.getItem();
                    if (i == 1 && j == 1) {
                        if (item != Items.POTION) {
                            return false;
                        }
                    } else if (item != ModItems.FLINT_BULLET.get()) {
                        return false;
                    }
                }
            }

            return true;
        } else {
            return false;
        }
    }

    public ItemStack assemble(CraftingContainer p_77572_1_) {
        ItemStack itemstack = p_77572_1_.getItem(1 + p_77572_1_.getWidth());
        if (itemstack.getItem() != Items.POTION) {
            return ItemStack.EMPTY;
        } else {
            ItemStack itemstack1 = new ItemStack(ModItems.TIPPED_BULLET.get(), 8);
            PotionUtils.setPotion(itemstack1, PotionUtils.getPotion(itemstack));
            PotionUtils.setCustomEffects(itemstack1, PotionUtils.getCustomEffects(itemstack));
            return itemstack1;
        }
    }

    public boolean canCraftInDimensions(int p_194133_1_, int p_194133_2_) {
        return p_194133_1_ * p_194133_2_ == 9;
    }

    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.TIPPED_BULLET_RECIPE.get();
    }
}
