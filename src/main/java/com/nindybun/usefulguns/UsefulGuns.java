package com.nindybun.usefulguns;

import com.nindybun.usefulguns.data.Generator;
import com.nindybun.usefulguns.util.RecipeUnlocker;
import com.nindybun.usefulguns.crafting.TargetNBTIngredient;
import com.nindybun.usefulguns.gui.PouchScreen;
import com.nindybun.usefulguns.modRegistries.ModContainers;
import com.nindybun.usefulguns.modRegistries.ModItems;
import com.nindybun.usefulguns.modRegistries.ModRecipes;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(UsefulGuns.MOD_ID)
public class UsefulGuns
{
    public static final String MOD_ID = "usefulguns";

    public static final Logger LOGGER = LogManager.getLogger();

    public UsefulGuns() {
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.ITEMS.register(modEventBus);
        ModContainers.CONTAINERS.register(modEventBus);
        ModRecipes.RECIPES.register(modEventBus);
        modEventBus.addListener(Generator::gatherData);

        RecipeUnlocker.register(MOD_ID, MinecraftForge.EVENT_BUS, 1);

        modEventBus.addListener(this::setup);
        modEventBus.addListener(this::doClientStuff);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> CraftingHelper.register(TargetNBTIngredient.Serializer.NAME, TargetNBTIngredient.SERIALIZER));
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        ScreenManager.register(ModContainers.POUCH_CONTAINER.get(), PouchScreen::new);
    }

    public static ItemGroup itemGroup = new ItemGroup(UsefulGuns.MOD_ID) {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.IRON_POUCH.get());
        }
    };

}
