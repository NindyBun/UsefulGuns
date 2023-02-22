package com.nindybun.usefulguns.crafting;

import com.google.gson.JsonObject;
import com.nindybun.usefulguns.UsefulGuns;
import com.nindybun.usefulguns.modRegistries.ModRecipes;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CopyPouchData extends ShapedRecipe {
    public CopyPouchData(ResourceLocation id, String group, int recipeWidth, int recipeHeight, NonNullList<Ingredient> ingredients, ItemStack recipeOutput){
        super(id, group, recipeWidth, recipeHeight, ingredients, recipeOutput);
    }

    public CopyPouchData(ShapedRecipe shapedRecipe){
        super(shapedRecipe.getId(), shapedRecipe.getGroup(), shapedRecipe.getRecipeWidth(), shapedRecipe.getRecipeHeight(), shapedRecipe.getIngredients(), shapedRecipe.getResultItem());
    }

    @Override
    @Nonnull
    public ItemStack assemble(@Nonnull CraftingInventory inv) {
        final ItemStack craftingResult = super.assemble(inv);
        TargetNBTIngredient donorIngredient = null;
        ItemStack dataSource = ItemStack.EMPTY;
        NonNullList<Ingredient> ingredients = getIngredients();
        for (Ingredient ingredient : ingredients) {
            if (ingredient instanceof TargetNBTIngredient) {
                donorIngredient = (TargetNBTIngredient) ingredient;
                break;
            }
        }
        if (!craftingResult.isEmpty()) {
            for (int i = 0; i < inv.getContainerSize(); i++) {
                final ItemStack item = inv.getItem(i);
                if (!item.isEmpty() && donorIngredient.test(item)) {
                    dataSource = item;
                    break;
                }
            }

            if (!dataSource.isEmpty() && dataSource.hasTag()) {
                craftingResult.setTag(dataSource.getTag().copy());
            }
        }

        return craftingResult;
    }

    @Override
    @Nonnull
    public IRecipeSerializer<?> getSerializer() {
        return ModRecipes.COPY_RECIPE.get();
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<CopyPouchData> {
        @Nullable
        @Override
        public CopyPouchData fromNetwork(@Nonnull ResourceLocation recipeId, @Nonnull PacketBuffer buffer) {
            return new CopyPouchData(IRecipeSerializer.SHAPED_RECIPE.fromNetwork(recipeId, buffer));
        }

        @Override
        @Nonnull
        public CopyPouchData fromJson(@Nonnull ResourceLocation recipeId, @Nonnull JsonObject json) {
            try {
                return new CopyPouchData(IRecipeSerializer.SHAPED_RECIPE.fromJson(recipeId, json));
            }
            catch (Exception exception) {
                UsefulGuns.LOGGER.info("Error reading CopyPouch Recipe from packet: ", exception);
                throw exception;
            }
        }

        @Override
        public void toNetwork(@Nonnull PacketBuffer buffer, @Nonnull CopyPouchData recipe) {
            try {
                IRecipeSerializer.SHAPED_RECIPE.toNetwork(buffer, recipe);
            }
            catch (Exception exception) {
                UsefulGuns.LOGGER.info("Error writing CopyPouch Recipe to packet: ", exception);
                throw exception;
            }
        }
    }
}
