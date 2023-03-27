package com.nindybun.usefulguns.modRegistries;

import com.nindybun.usefulguns.UsefulGuns;
import com.nindybun.usefulguns.crafting.CopyPouchData;
import com.nindybun.usefulguns.data.*;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPES = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, UsefulGuns.MOD_ID);

    public static final RegistryObject<RecipeSerializer<?>> COPY_RECIPE = RECIPES.register("pouch_upgrade", CopyPouchData.Serializer::new);

    public static final RegistryObject<SimpleCraftingRecipeSerializer<TippedBulletRecipe>> TIPPED_BULLET_RECIPE = RECIPES.register("tipped_bullet", () -> new SimpleCraftingRecipeSerializer<>(TippedBulletRecipe::new));
    public static final RegistryObject<SimpleCraftingRecipeSerializer<SplashBulletRecipe>> SPLASH_BULLET_RECIPE = RECIPES.register("splash_bullet", () -> new SimpleCraftingRecipeSerializer<>(SplashBulletRecipe::new));
    public static final RegistryObject<SimpleCraftingRecipeSerializer<LingeringBulletRecipe>> LINGERING_BULLET_RECIPE = RECIPES.register("lingering_bullet", () -> new SimpleCraftingRecipeSerializer<>(LingeringBulletRecipe::new));
    public static final RegistryObject<SimpleCraftingRecipeSerializer<BoreBulletRecipe>> BORE_BULLET_RECIPE = RECIPES.register("bore_bullet", () -> new SimpleCraftingRecipeSerializer<>(BoreBulletRecipe::new));
    public static final RegistryObject<SimpleCraftingRecipeSerializer<BoreKitRepairRecipe>> BOREKIT_REPAIR_RECIPE = RECIPES.register("borekit_repair", () -> new SimpleCraftingRecipeSerializer<>(BoreKitRepairRecipe::new));
    public static final RegistryObject<SimpleCraftingRecipeSerializer<BoreKitRecipe>> BOREKIT_RECIPE = RECIPES.register("borekit", () -> new SimpleCraftingRecipeSerializer<>(BoreKitRecipe::new));

}
