package com.nindybun.usefulguns.modRegistries;

import com.nindybun.usefulguns.UsefulGuns;
import com.nindybun.usefulguns.crafting.CopyPouchData;
import com.nindybun.usefulguns.data.*;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPES = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, UsefulGuns.MOD_ID);

    public static final RegistryObject<RecipeSerializer<?>> COPY_RECIPE = RECIPES.register("pouch_upgrade", CopyPouchData.Serializer::new);

    public static final RegistryObject<SimpleRecipeSerializer<TippedBulletRecipe>> TIPPED_BULLET_RECIPE = RECIPES.register("tipped_bullet", () -> new SimpleRecipeSerializer<>(TippedBulletRecipe::new));
    public static final RegistryObject<SimpleRecipeSerializer<SplashBulletRecipe>> SPLASH_BULLET_RECIPE = RECIPES.register("splash_bullet", () -> new SimpleRecipeSerializer<>(SplashBulletRecipe::new));
    public static final RegistryObject<SimpleRecipeSerializer<LingeringBulletRecipe>> LINGERING_BULLET_RECIPE = RECIPES.register("lingering_bullet", () -> new SimpleRecipeSerializer<>(LingeringBulletRecipe::new));
    public static final RegistryObject<SimpleRecipeSerializer<BoreBulletRecipe>> BORE_BULLET_RECIPE = RECIPES.register("bore_bullet", () -> new SimpleRecipeSerializer<>(BoreBulletRecipe::new));
    public static final RegistryObject<SimpleRecipeSerializer<BoreKitRepairRecipe>> BOREKIT_REPAIR_RECIPE = RECIPES.register("borekit_repair", () -> new SimpleRecipeSerializer<>(BoreKitRepairRecipe::new));
    public static final RegistryObject<SimpleRecipeSerializer<BoreKitRecipe>> BOREKIT_RECIPE = RECIPES.register("borekit", () -> new SimpleRecipeSerializer<>(BoreKitRecipe::new));

}
