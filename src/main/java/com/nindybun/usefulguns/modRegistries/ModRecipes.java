package com.nindybun.usefulguns.modRegistries;

import com.nindybun.usefulguns.UsefulGuns;
import com.nindybun.usefulguns.crafting.CopyPouchData;
import com.nindybun.usefulguns.data.LingeringBulletRecipe;
import com.nindybun.usefulguns.data.SplashBulletRecipe;
import com.nindybun.usefulguns.data.TippedBulletRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModRecipes {
    public static final DeferredRegister<IRecipeSerializer<?>> RECIPES = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, UsefulGuns.MOD_ID);

    public static final RegistryObject<IRecipeSerializer<?>> COPY_RECIPE = RECIPES.register("pouch_upgrade", CopyPouchData.Serializer::new);

    public static final RegistryObject<SpecialRecipeSerializer<TippedBulletRecipe>> TIPPED_BULLET_RECIPE = RECIPES.register("crafting_special_tippedbullet", () -> new SpecialRecipeSerializer<>(TippedBulletRecipe::new));
    public static final RegistryObject<SpecialRecipeSerializer<SplashBulletRecipe>> SPLASH_BULLET_RECIPE = RECIPES.register("crafting_special_splashbullet", () -> new SpecialRecipeSerializer<>(SplashBulletRecipe::new));
    public static final RegistryObject<SpecialRecipeSerializer<LingeringBulletRecipe>> LINGERING_BULLET_RECIPE = RECIPES.register("crafting_special_lingeringbullet", () -> new SpecialRecipeSerializer<>(LingeringBulletRecipe::new));


}
