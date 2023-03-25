package com.nindybun.usefulguns.jeiPlugin;

import com.nindybun.usefulguns.UsefulGuns;
import com.nindybun.usefulguns.data.*;
import com.nindybun.usefulguns.items.BoreKit;
import com.nindybun.usefulguns.items.bullets.MiningBullet;
import com.nindybun.usefulguns.modRegistries.ModItems;
import com.nindybun.usefulguns.util.UtilMethods;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.ingredients.subtypes.IIngredientSubtypeInterpreter;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;

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
            List<MobEffectInstance> effects = PotionUtils.getMobEffects(itemStack);
            for (MobEffectInstance effect : effects) {
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

        registration.registerSubtypeInterpreter(VanillaTypes.ITEM_STACK, ModItems.TIPPED_BULLET.get(), potionProvider);
        registration.registerSubtypeInterpreter(VanillaTypes.ITEM_STACK, ModItems.SPLASH_BULLET.get(), potionProvider);
        registration.registerSubtypeInterpreter(VanillaTypes.ITEM_STACK, ModItems.LINGERING_BULLET.get(), potionProvider);
        for (BoreKit.Kit kit : BoreKit.Kit.values()) {
            registration.registerSubtypeInterpreter(VanillaTypes.ITEM_STACK, UtilMethods.createKit(kit), borekitProvider);
            registration.registerSubtypeInterpreter(VanillaTypes.ITEM_STACK, UtilMethods.createBore(kit), boreEnchantmentProvider);
        }
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        Map<Class<? extends CraftingRecipe>, Supplier<Stream<CraftingRecipe>>> replacers = new IdentityHashMap<>();
        Collection<CraftingRecipe> recipes = Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(RecipeType.CRAFTING);
        replacers.put(TippedBulletRecipe.class, TippedBulletRecipeMaker::createRecipes);
        replacers.put(SplashBulletRecipe.class, SplashBulletRecipeMaker::createRecipes);
        replacers.put(LingeringBulletRecipe.class, LingeringBulletRecipeMaker::createRecipes);
        replacers.put(BoreBulletRecipe.class, BoreBulletRecipeMaker::createRecipes);
        replacers.put(BoreKitRepairRecipe.class, BoreKitRepairRecipeMaker::createRecipes);
        replacers.put(BoreKitRecipe.class, BoreKitRecipeMaker::createRecipes);

        List<CraftingRecipe> recipeList = recipes.stream()
                                                .map(CraftingRecipe::getClass)
                                                .distinct()
                                                .filter(replacers::containsKey)
                                                .limit(replacers.size())
                                                .map(replacers::get)
                                                .flatMap(Supplier::get)
                                                .collect(Collectors.toList());
        registration.addRecipes(RecipeTypes.CRAFTING, recipeList);
    }

}
