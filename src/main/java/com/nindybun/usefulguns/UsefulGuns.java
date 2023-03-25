package com.nindybun.usefulguns;

import com.nindybun.usefulguns.data.Generator;
import com.nindybun.usefulguns.events.ClientEvents;
import com.nindybun.usefulguns.items.BoreKit;
import com.nindybun.usefulguns.items.bullets.MiningBullet;
import com.nindybun.usefulguns.modRegistries.*;
import com.nindybun.usefulguns.network.PacketHandler;
import com.nindybun.usefulguns.crafting.TargetNBTIngredient;
import com.nindybun.usefulguns.gui.PouchScreen;
import com.nindybun.usefulguns.util.UtilMethods;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.block.model.ItemOverride;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(UsefulGuns.MOD_ID)
public class UsefulGuns
{
    public static final String MOD_ID = "usefulguns";
    public static final Logger LOGGER = LogManager.getLogger();

    public UsefulGuns() {
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModSounds.SOUNDS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);
        ModContainers.CONTAINERS.register(modEventBus);
        ModRecipes.RECIPES.register(modEventBus);
        ModEntities.ENTITY.register(modEventBus);
        modEventBus.addListener(Generator::gatherData);

        //RecipeUnlocker.register(MOD_ID, MinecraftForge.EVENT_BUS, 1);

        modEventBus.addListener(this::setup);
        modEventBus.addListener(this::doClientStuff);
        modEventBus.addListener(this::loadComplete);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> CraftingHelper.register(TargetNBTIngredient.Serializer.NAME, TargetNBTIngredient.SERIALIZER));
        PacketHandler.register();
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        MenuScreens.register(ModContainers.POUCH_CONTAINER.get(), PouchScreen::new);
        event.enqueueWork(()->{
            for (BoreKit.Kit kit : BoreKit.Kit.values())
                ItemProperties.register(UtilMethods.createBore(kit), new ResourceLocation(UsefulGuns.MOD_ID, "enchantment_id"), MiningBullet::getEnchantmentID);
        });
    }

    public void loadComplete(FMLLoadCompleteEvent event){
        event.enqueueWork(() -> {
            if (FMLEnvironment.dist == Dist.CLIENT) ClientEvents.init();
        });
    }

    public static CreativeModeTab itemGroup = new CreativeModeTab(UsefulGuns.MOD_ID) {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.ARMOR_PIERCING_BULLET.get());
        }
    };

}

