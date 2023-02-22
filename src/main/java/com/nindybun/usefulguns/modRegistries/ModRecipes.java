package com.nindybun.usefulguns.modRegistries;

import com.nindybun.usefulguns.UsefulGuns;
import com.nindybun.usefulguns.crafting.CopyPouchData;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModRecipes {
    public static final DeferredRegister<IRecipeSerializer<?>> RECIPES = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, UsefulGuns.MOD_ID);

    public static final RegistryObject<IRecipeSerializer<?>> COPY_RECIPE = RECIPES.register("pouch_upgrade", CopyPouchData.Serializer::new);
}
