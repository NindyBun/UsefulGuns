package com.nindybun.usefulguns.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.nindybun.usefulguns.UsefulGuns;
import com.nindybun.usefulguns.inventory.PouchData;
import com.nindybun.usefulguns.items.AbstractPouch;
import com.nindybun.usefulguns.items.guns.AbstractGun;
import com.nindybun.usefulguns.util.UtilMethods;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import java.awt.*;

@Mod.EventBusSubscriber(modid = UsefulGuns.MOD_ID, value = Dist.CLIENT)
public class BulletCountRender {

    public static ItemStack getGun(Player player){
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
            Player player = mc.player;
            ItemStack gun = getGun(player);
            if (gun.isEmpty())
                    return;
            else if (!gun.isEmpty()){
                ItemStack pouch = UtilMethods.locateAndGetPouch(player);
                renderBulletCount(event, gun, pouch);
            }
        }
    }

    public static void renderBulletCount(RenderGameOverlayEvent.Post event, ItemStack gun, ItemStack pouch){
        Font font = Minecraft.getInstance().font;
        ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();
        ItemStack bulletInfo = ItemStack.of(gun.getOrCreateTag().getCompound("Bullet_Info"));
        if (bulletInfo.getItem() == Items.AIR)
            return;
        int selectedBulletCount = 0;
        if (!pouch.isEmpty()) {
            PouchData data = AbstractPouch.getData(pouch);
            if (data.getOptional().isPresent()) {
                IItemHandler handler = data.getOptional().resolve().get();
                for (int i = 0; i < handler.getSlots(); i++){
                    ItemStack stack = handler.getStackInSlot(i).copy().split(1);
                    if (bulletInfo.equals(stack, false)) {
                        selectedBulletCount += handler.getStackInSlot(i).getCount();
                    }
                }
            }
        }
        double winW = event.getWindow().getGuiScaledWidth();
        double winH = event.getWindow().getGuiScaledHeight();
        PoseStack poseStack = event.getMatrixStack();
        poseStack.pushPose();

        PoseStack poseStack2 = RenderSystem.getModelViewStack();
        poseStack2.pushPose();
        poseStack2.mulPoseMatrix(poseStack.last().pose());
        poseStack2.translate(-8, -8, 0);
        RenderSystem.applyModelViewMatrix();
        double xoffset = 16-4;
        double yoffset = 8+3;
        renderer.renderAndDecorateItem(bulletInfo, (int)xoffset, (int) (winH-yoffset));
        renderer.renderGuiItemDecorations(font, bulletInfo, (int)xoffset, (int) (winH-yoffset), "");
        poseStack2.popPose();
        RenderSystem.applyModelViewMatrix();

        poseStack.popPose();
        font.draw(event.getMatrixStack(), ">> " + selectedBulletCount, (float) xoffset+12, (float) (winH-yoffset-3), Color.WHITE.getRGB());
    }
}
