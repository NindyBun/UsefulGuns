package com.nindybun.usefulguns.jeiPlugin;

import com.nindybun.usefulguns.UsefulGuns;
import com.nindybun.usefulguns.data.*;
import com.nindybun.usefulguns.modRegistries.ModItems;
import com.nindybun.usefulguns.modRegistries.ModRecipes;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaRecipeCategoryUid;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.ingredients.subtypes.IIngredientSubtypeInterpreter;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.category.extensions.vanilla.crafting.ICraftingCategoryExtension;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.ResourceLocation;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@JeiPlugin
public class UsefulGunsJEI implements IModPlugin {

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(UsefulGuns.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration registration) {
        IIngredientSubtypeInterpreter<ItemStack> potionProvider = (itemStack, id) -> {
            if (!itemStack.hasTag()) {
                return IIngredientSubtypeInterpreter.NONE;
            }
            Potion potionType = PotionUtils.getPotion(itemStack);
            String potionTypeString = potionType.getName("");
            StringBuilder stringBuilder = new StringBuilder(potionTypeString);
            List<EffectInstance> effects = PotionUtils.getMobEffects(itemStack);
            for (EffectInstance effect : effects) {
                stringBuilder.append(";").append(effect);
            }
            return stringBuilder.toString();
        };

        registration.registerSubtypeInterpreter(ModItems.TIPPED_BULLET.get(), potionProvider);
        registration.registerSubtypeInterpreter(ModItems.SPLASH_BULLET.get(), potionProvider);
        registration.registerSubtypeInterpreter(ModItems.LINGERING_BULLET.get(), potionProvider);
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        Map<Class<? extends ICraftingRecipe>, Supplier<Stream<ICraftingRecipe>>> replacers = new IdentityHashMap<>();
        Collection<ICraftingRecipe> recipes = Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(IRecipeType.CRAFTING);
        replacers.put(TippedBulletRecipe.class, TippedBulletRecipeMaker::createRecipes);
        replacers.put(SplashBulletRecipe.class, SplashBulletRecipeMaker::createRecipes);
        replacers.put(LingeringBulletRecipe.class, LingeringBulletRecipeMaker::createRecipes);

        List<ICraftingRecipe> recipeList = recipes.stream()
                                                .map(ICraftingRecipe::getClass)
                                                .distinct()
                                                .filter(replacers::containsKey)
                                                .limit(replacers.size())
                                                .map(replacers::get)
                                                .flatMap(Supplier::get)
                                                .collect(Collectors.toList());
        registration.addRecipes(recipeList, VanillaRecipeCategoryUid.CRAFTING);
    }

}
