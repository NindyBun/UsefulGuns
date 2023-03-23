package com.nindybun.usefulguns.jeiPlugin;

import com.nindybun.usefulguns.UsefulGuns;
import com.nindybun.usefulguns.data.*;
import com.nindybun.usefulguns.items.BoreKit;
import com.nindybun.usefulguns.items.bullets.MiningBullet;
import com.nindybun.usefulguns.modRegistries.ModItems;
import com.nindybun.usefulguns.modRegistries.ModRecipes;
import com.nindybun.usefulguns.util.Util;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaRecipeCategoryUid;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.ingredients.subtypes.IIngredientSubtypeInterpreter;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.category.extensions.vanilla.crafting.ICraftingCategoryExtension;
import mezz.jei.api.registration.IModIngredientRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.EnchantmentNameParts;
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

        IIngredientSubtypeInterpreter<ItemStack> borekitProvider = (itemStack, id) -> {
            if (!(itemStack.getItem() instanceof BoreKit))
                return IIngredientSubtypeInterpreter.NONE;
            int uses = itemStack.getOrCreateTag().getInt(BoreKit.USES);
            if (uses == 0)
                return "empty";
            else if (uses == ((BoreKit) itemStack.getItem()).getKit().getUses())
                return "full";
            return IIngredientSubtypeInterpreter.NONE;
        };

        IIngredientSubtypeInterpreter<ItemStack> boreEnchantmentProvider = (itemStack, id) -> {
            if (!(itemStack.getItem() instanceof MiningBullet))
                return IIngredientSubtypeInterpreter.NONE;
            Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(itemStack);
            int level = 0;
            if (enchantments.containsKey(Enchantments.BLOCK_FORTUNE))
                level = enchantments.get(Enchantments.BLOCK_FORTUNE);
            else if (enchantments.containsKey(Enchantments.SILK_TOUCH))
                level = 4;
            return level+"";
        };

        registration.registerSubtypeInterpreter(ModItems.TIPPED_BULLET.get(), potionProvider);
        registration.registerSubtypeInterpreter(ModItems.SPLASH_BULLET.get(), potionProvider);
        registration.registerSubtypeInterpreter(ModItems.LINGERING_BULLET.get(), potionProvider);
        for (BoreKit.Kit kit : BoreKit.Kit.values()) {
            registration.registerSubtypeInterpreter(Util.createKit(kit), borekitProvider);
            registration.registerSubtypeInterpreter(Util.createBore(kit), boreEnchantmentProvider);
        }
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        Map<Class<? extends ICraftingRecipe>, Supplier<Stream<ICraftingRecipe>>> replacers = new IdentityHashMap<>();
        Collection<ICraftingRecipe> recipes = Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(IRecipeType.CRAFTING);
        replacers.put(TippedBulletRecipe.class, TippedBulletRecipeMaker::createRecipes);
        replacers.put(SplashBulletRecipe.class, SplashBulletRecipeMaker::createRecipes);
        replacers.put(LingeringBulletRecipe.class, LingeringBulletRecipeMaker::createRecipes);
        replacers.put(BoreBulletRecipe.class, BoreBulletRecipeMaker::createRecipes);
        replacers.put(BoreKitRepairRecipe.class, BoreKitRepairRecipeMaker::createRecipes);
        replacers.put(BoreKitRecipe.class, BoreKitRecipeMaker::createRecipes);

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
