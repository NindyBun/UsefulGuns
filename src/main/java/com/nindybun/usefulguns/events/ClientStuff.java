package com.nindybun.usefulguns.events;

import com.nindybun.usefulguns.UsefulGuns;
import com.nindybun.usefulguns.data.TippedBulletColor;
import com.nindybun.usefulguns.entities.BulletEntityRenderer;
import com.nindybun.usefulguns.gui.BulletRadialMenu;
import com.nindybun.usefulguns.modRegistries.ModEntities;
import com.nindybun.usefulguns.modRegistries.ModItems;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
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
        EntityRenderers.register(ModEntities.BULLET.get(), BulletEntityRenderer::new);
    }

    @SubscribeEvent
    public static void registerItemColors(RegisterColorHandlersEvent.Item event){
        event.register(new TippedBulletColor(), ModItems.TIPPED_BULLET.get());
        event.register(new TippedBulletColor(), ModItems.SPLASH_BULLET.get());
        event.register(new TippedBulletColor(), ModItems.LINGERING_BULLET.get());
    }

}
