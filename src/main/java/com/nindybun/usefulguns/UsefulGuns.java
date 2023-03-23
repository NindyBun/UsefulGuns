package com.nindybun.usefulguns;

import com.nindybun.usefulguns.data.Generator;
import com.nindybun.usefulguns.events.ClientEvents;
import com.nindybun.usefulguns.events.ClientStuff;
import com.nindybun.usefulguns.gui.BulletRadialMenu;
import com.nindybun.usefulguns.items.BoreKit;
import com.nindybun.usefulguns.items.bullets.AbstractBullet;
import com.nindybun.usefulguns.items.bullets.MiningBullet;
import com.nindybun.usefulguns.items.guns.AbstractGun;
import com.nindybun.usefulguns.modRegistries.*;
import com.nindybun.usefulguns.network.PacketHandler;
import com.nindybun.usefulguns.util.RecipeUnlocker;
import com.nindybun.usefulguns.crafting.TargetNBTIngredient;
import com.nindybun.usefulguns.gui.PouchScreen;
import com.nindybun.usefulguns.util.Util;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.settings.KeyBindingMap;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

import javax.swing.text.JTextComponent;

import java.awt.im.InputContext;

import static net.minecraft.client.util.InputMappings.Type.KEYSYM;
import static net.minecraft.client.util.InputMappings.Type.MOUSE;

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
        ScreenManager.register(ModContainers.POUCH_CONTAINER.get(), PouchScreen::new);
        event.enqueueWork(()->{
            for (BoreKit.Kit kit : BoreKit.Kit.values())
                ItemModelsProperties.register(Util.createBore(kit), new ResourceLocation(UsefulGuns.MOD_ID, "enchantment_id"), MiningBullet::getEnchantmentID);
        });
    }

    public void loadComplete(FMLLoadCompleteEvent event){
        event.enqueueWork(() -> {
            if (FMLEnvironment.dist == Dist.CLIENT) ClientEvents.init();
        });
    }

    public static ItemGroup itemGroup = new ItemGroup(UsefulGuns.MOD_ID) {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.IRON_POUCH.get());
        }
    };

}

