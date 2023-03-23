package com.nindybun.usefulguns.modRegistries;

import com.nindybun.usefulguns.UsefulGuns;
import com.nindybun.usefulguns.crafting.CopyPouchData;
import com.nindybun.usefulguns.data.*;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModRecipes {
    public static final DeferredRegister<IRecipeSerializer<?>> RECIPES = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, UsefulGuns.MOD_ID);

    public static final RegistryObject<IRecipeSerializer<?>> COPY_RECIPE = RECIPES.register("pouch_upgrade", CopyPouchData.Serializer::new);

    public static final RegistryObject<SpecialRecipeSerializer<TippedBulletRecipe>> TIPPED_BULLET_RECIPE = RECIPES.register("tipped_bullet", () -> new SpecialRecipeSerializer<>(TippedBulletRecipe::new));
    public static final RegistryObject<SpecialRecipeSerializer<SplashBulletRecipe>> SPLASH_BULLET_RECIPE = RECIPES.register("splash_bullet", () -> new SpecialRecipeSerializer<>(SplashBulletRecipe::new));
    public static final RegistryObject<SpecialRecipeSerializer<LingeringBulletRecipe>> LINGERING_BULLET_RECIPE = RECIPES.register("lingering_bullet", () -> new SpecialRecipeSerializer<>(LingeringBulletRecipe::new));
    public static final RegistryObject<SpecialRecipeSerializer<BoreBulletRecipe>> BORE_BULLET_RECIPE = RECIPES.register("bore_bullet", () -> new SpecialRecipeSerializer<>(BoreBulletRecipe::new));
    public static final RegistryObject<SpecialRecipeSerializer<BoreKitRepairRecipe>> BOREKIT_REPAIR_RECIPE = RECIPES.register("borekit_repair", () -> new SpecialRecipeSerializer<>(BoreKitRepairRecipe::new));
    public static final RegistryObject<SpecialRecipeSerializer<BoreKitRecipe>> BOREKIT_RECIPE = RECIPES.register("borekit", () -> new SpecialRecipeSerializer<>(BoreKitRecipe::new));

}
