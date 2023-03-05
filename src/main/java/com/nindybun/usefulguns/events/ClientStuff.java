package com.nindybun.usefulguns.events;

import com.nindybun.usefulguns.UsefulGuns;
import com.nindybun.usefulguns.data.TippedBulletColor;
import com.nindybun.usefulguns.entities.BulletEntityRenderer;
import com.nindybun.usefulguns.modRegistries.ModEntities;
import com.nindybun.usefulguns.modRegistries.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtils;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.awt.*;
import java.awt.image.ColorModel;
import java.nio.file.attribute.UserDefinedFileAttributeView;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = UsefulGuns.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientStuff {

    @SubscribeEvent
    public static void clientStuff(FMLClientSetupEvent event){
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.BULLET.get(), BulletEntityRenderer::new);
    }

    @SubscribeEvent
    public static void registerItemColors(ColorHandlerEvent.Item event){
        event.getItemColors().register(new TippedBulletColor(), ModItems.TIPPED_BULLET.get());
        event.getItemColors().register(new TippedBulletColor(), ModItems.SPLASH_BULLET.get());
        event.getItemColors().register(new TippedBulletColor(), ModItems.LINGERING_BULLET.get());
        event.getItemColors().register((p_getColor_1_, p_getColor_2_) -> Color.GRAY.hashCode(), ModItems.IRON_GUN.get());
    }

}
