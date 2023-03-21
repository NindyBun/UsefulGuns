package com.nindybun.usefulguns.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.nindybun.usefulguns.UsefulGuns;
import com.nindybun.usefulguns.inventory.PouchData;
import com.nindybun.usefulguns.items.AbstractPouch;
import com.nindybun.usefulguns.items.bullets.AbstractBullet;
import com.nindybun.usefulguns.items.guns.AbstractGun;
import com.nindybun.usefulguns.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import java.awt.*;

@Mod.EventBusSubscriber(modid = UsefulGuns.MOD_ID, value = Dist.CLIENT)
public class BulletCountRender {

    public static ItemStack getGun(PlayerEntity player){
        ItemStack main = player.getMainHandItem();
        ItemStack off = player.getOffhandItem();

        if (main.getItem() instanceof AbstractGun)
            return main;
        else if (off.getItem() instanceof AbstractGun)
            return off;

        return ItemStack.EMPTY;
    }

    @SubscribeEvent
    public static void renderOverlay(@Nonnull RenderGameOverlayEvent.Post event){
        if (event.getType() == RenderGameOverlayEvent.ElementType.ALL){
            Minecraft mc = Minecraft.getInstance();
            PlayerEntity player = mc.player;
            ItemStack gun = getGun(player);
            if (gun.isEmpty())
                    return;
            else if (!gun.isEmpty()){
                ItemStack pouch = Util.locateAndGetPouch(player);
                if (pouch.isEmpty())
                    return;
                renderBulletCount(event, player, gun, pouch);
            }
        }
    }

    public static void renderBulletCount(RenderGameOverlayEvent.Post event, PlayerEntity player, ItemStack gun, ItemStack pouch){
        FontRenderer font = Minecraft.getInstance().font;
        ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();
        ItemStack bulletInfo = ItemStack.of(gun.getOrCreateTag().getCompound("Bullet_Info"));
        if (bulletInfo.getItem() == Items.AIR)
            return;
        int selectedBulletCount = 0;
        PouchData data = AbstractPouch.getData(pouch);
        if (data.getOptional().isPresent()) {
            IItemHandler handler = data.getOptional().resolve().get();
            //CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.getStorage().readNBT(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, handler, null, pouch.getTag().get("ClientInventory"));
            for (int i = 0; i < handler.getSlots(); i++){
                ItemStack stack = handler.getStackInSlot(i).copy().split(1);
                if (bulletInfo.equals(stack, false)) {
                    selectedBulletCount += handler.getStackInSlot(i).getCount();
                }
            }
        }
        double winW = event.getWindow().getGuiScaledWidth();
        double winH = event.getWindow().getGuiScaledHeight();
        RenderHelper.turnBackOn();
        RenderSystem.pushMatrix();
        RenderSystem.translatef(-8, -8, 0);
        double xoffset = 16-4;
        double yoffset = 8+3;
        renderer.renderAndDecorateItem(bulletInfo, (int)xoffset, (int) (winH-yoffset));
        renderer.renderGuiItemDecorations(font, bulletInfo, (int)xoffset, (int) (winH-yoffset), "");
        RenderSystem.popMatrix();
        RenderHelper.turnOff();
        font.draw(event.getMatrixStack(), ">> " + selectedBulletCount, (float) xoffset+12, (float) (winH-yoffset-3), Color.WHITE.getRGB());
    }
}
