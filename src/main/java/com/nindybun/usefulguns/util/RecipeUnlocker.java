package com.nindybun.usefulguns.util;

import com.nindybun.usefulguns.UsefulGuns;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;

import java.util.ArrayList;
import java.util.List;

public class RecipeUnlocker {
    private static String modTag;
    private static int version;

    public static void register(String modId, IEventBus bus, int versionIn){
        modTag = modId;
        version = versionIn;
        bus.addListener(RecipeUnlocker::onPlayerLoggedIn);
    }

    private static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event){
        CompoundNBT tag = event.getPlayer().getPersistentData();
        if (tag.contains(modTag) && tag.getInt(modTag) >= version)
            return;
        PlayerEntity player = event.getPlayer();
        if (player instanceof ServerPlayerEntity){
            MinecraftServer server = player.getServer();
            if (server != null){
                List<IRecipe<?>> recipes = new ArrayList<>(server.getRecipeManager().getRecipes());
                recipes.removeIf(iRecipe -> !iRecipe.getId().getNamespace().contains(UsefulGuns.MOD_ID));
                player.awardRecipes(recipes);
                tag.putInt(modTag, version);
            }
        }
    }
}
