package com.nindybun.usefulguns.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.nindybun.usefulguns.UsefulGuns;
import com.nindybun.usefulguns.events.ClientEvents;
import com.nindybun.usefulguns.inventory.PouchData;
import com.nindybun.usefulguns.inventory.PouchHandler;
import com.nindybun.usefulguns.items.AbstractPouch;
import com.nindybun.usefulguns.items.PouchTypes;
import com.nindybun.usefulguns.items.bullets.MiningBullet;
import com.nindybun.usefulguns.items.bullets.ShotgunBullet;
import com.nindybun.usefulguns.items.guns.AbstractGun;
import com.nindybun.usefulguns.items.guns.AbstractShotgun;
import com.nindybun.usefulguns.network.PacketHandler;
import com.nindybun.usefulguns.network.packets.PacketSaveSelection;
import com.nindybun.usefulguns.util.Util;
import javafx.scene.shape.VertexFormat;
import net.minecraft.advancements.criterion.ItemDurabilityTrigger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.CallbackI;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BulletRadialMenu extends Screen {
    private KeyBinding key;
    private int selected;
    private ItemStack selectedItem;
    private PlayerEntity player;
    private ItemStack gun;
    private List<ItemStack> containedItems = new ArrayList<>();
    private Map<ItemStack, Integer> containedItemsCount = new HashMap<>();
    int[] ringSize = {9, 12, 16, 21, 23};

    public BulletRadialMenu(KeyBinding key, PlayerEntity player) {
        super(new StringTextComponent("Title"));
        this.key = key;
        this.player = player;
        this.gun = !(player.getMainHandItem().getItem() instanceof AbstractGun) ? player.getOffhandItem(): player.getMainHandItem();
        this.selected = -1;
        this.selectedItem = ItemStack.of(gun.getOrCreateTag().getCompound("Bullet_Info"));
    }

    @Override
    protected void init(){
        ItemStack pouch = Util.locateAndGetPouch(player);
        if (pouch == null)
            return;
        containedItems = getBulletsInPouch(gun, pouch);
        containedItemsCount = getBulletsCountInPouch(gun, pouch);
    }

    public static boolean isValidForGun(ItemStack gun, ItemStack ammo){
        if (ammo.getItem() instanceof MiningBullet)
            return true;
        if (gun.getItem() instanceof AbstractShotgun && ammo.getItem() instanceof ShotgunBullet)
            return true;
        if (!(gun.getItem() instanceof AbstractShotgun) && !(ammo.getItem() instanceof ShotgunBullet))
            return true;
        return false;
    }

    public static List<ItemStack> getBulletsInPouch(ItemStack gun, ItemStack pouch){
        LazyOptional<IItemHandler> optional = AbstractPouch.getData(pouch).getOptional();
        List<ItemStack> itemStacks = new ArrayList<>();
        if (optional.isPresent()){
            IItemHandler handler = optional.resolve().get();
            for (int i = 0; i < handler.getSlots(); i++){
                ItemStack ammo = handler.getStackInSlot(i);
                if (ammo != ItemStack.EMPTY){
                    if (!doesContainInList(itemStacks, ammo) && isValidForGun(gun, ammo))
                        itemStacks.add(ammo.copy().split(1));
                }
            }
        }

        return itemStacks;
    }

    public Map<ItemStack, Integer> getBulletsCountInPouch(ItemStack gun, ItemStack pouch){
        LazyOptional<IItemHandler> optional = AbstractPouch.getData(pouch).getOptional();
        Map<ItemStack, Integer> map = new HashMap<>();
        if (optional.isPresent()) {
            IItemHandler handler = optional.resolve().get();
            for (int i = 0; i < handler.getSlots(); i++) {
                ItemStack ammo = handler.getStackInSlot(i);
                if (ammo != ItemStack.EMPTY && isValidForGun(gun, ammo)) {
                    ItemStack copy = ammo.copy().split(1);
                    if (!this.putIfPresent(map, ammo))
                        map.put(copy, ammo.getCount());
                }
            }
        }
        return map;
    }

    public boolean putIfPresent(Map<ItemStack, Integer> map, ItemStack bullet){
        for (Map.Entry<ItemStack, Integer> entry : map.entrySet()) {
            ItemStack copy = bullet.copy().split(1);
            ItemStack key = entry.getKey();
            int value = entry.getValue();
            if (key.equals(copy, false)) {
                map.put(key, value + bullet.getCount());
                return true;
            }
        }
        return false;
    }

    public Map.Entry<ItemStack, Integer> getEntry(int current){
        int element = 0;
        for (Map.Entry<ItemStack, Integer> entry : containedItemsCount.entrySet()){
            if (element == current)
                return entry;
            element += 1;
        }
        return null;
    }

    public static boolean doesContainInList(List<ItemStack> list, ItemStack itemStack){
        for (ItemStack stack : list){
            if (stack.copy().split(1).equals(itemStack.copy().split(1), false))
                return true;
        }
        return false;
    }

    @Override
    public boolean mouseReleased(double x, double y, int mouseButton) {
        processClick(true);
        return super.mouseReleased(x, y, mouseButton);
    }

    private void processClick(boolean mousePressed){
        processSlot();
    }

    private void processSlot(){
        if (selected != -1) PacketHandler.sendToServer(new PacketSaveSelection(containedItems.get(selected)));
        onClose();
    }

    public int getCount(int i){
        int count = 0;
        for (int j = 0; j < i ; j++){
            count += ringSize[j];
        }
        return count;
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float ticks_) {
        super.render(matrixStack, mouseX, mouseY, ticks_);
        int numberOfSlices = containedItems.size();
        if (numberOfSlices == 0)
            return;
        List<Integer> allocateSizes = new ArrayList<>();
        //int numberOfSlices = 81;
        for (int size : ringSize) {
            if ((numberOfSlices - size) >= 0){
                numberOfSlices -= size;
                allocateSizes.add(size);
            }else{
                allocateSizes.add(numberOfSlices);
                break;
            }
        }

        float radiusIn = 20;
        float radiusOut = radiusIn * 2;
        int x = width / 2;
        int y = height / 2;

        matrixStack.pushPose();
        matrixStack.translate(0, 0, 0);
        drawBackground(allocateSizes, mouseX, mouseY, x, y, radiusIn, radiusOut);
        matrixStack.popPose();

        matrixStack.pushPose();
        drawItem(allocateSizes, x, y, radiusIn, radiusOut);
        matrixStack.popPose();

        matrixStack.pushPose();
        //drawCount(matrixStack, allocateSizes, x, y, radiusIn, radiusOut);
        matrixStack.popPose();

        matrixStack.pushPose();
        drawToolTip(matrixStack, allocateSizes, x, y, radiusIn, radiusOut);
        matrixStack.popPose();
    }

    public void drawToolTip(MatrixStack matrixStack, List<Integer> allocateSizes, int x, int y, float radiusIn, float radiusOut){
        int numberOfRings = allocateSizes.size();
        for (int i = 0; i < numberOfRings; i++) {
            //int slices = i < numberOfRings-1 ? 9 : numberOfSlices%9 == 0 ? 9 : numberOfSlices%9;
            int slices = allocateSizes.get(i);
            for (int j = 0; j < slices; j++) {
                float start = (((j - 0.5f) / (float) slices) + 0.25f) * 360;
                float end = (((j + 0.5f) / (float) slices) + 0.25f) * 360;
                float addRadius = (radiusIn+5)*i;
                float itemRadius = (radiusIn+radiusOut+(addRadius*2))/2;
                float middle = (float) Math.toRadians(start+end)/2;
                float midX = x + itemRadius * (float) Math.cos(middle);
                float midY = y + itemRadius * (float) Math.sin(middle);
                int current = j+(( i == 0 ? 0 : getCount(i) ));
                if (selected == current)
                    this.renderTooltip(matrixStack, containedItems.get(current), (int)midX, (int)midY);
            }
        }
    }

    public void drawItem(List<Integer> allocateSizes, int x, int y, float radiusIn, float radiusOut){
        RenderHelper.turnBackOn();
        RenderSystem.enableDepthTest();
        RenderSystem.pushMatrix();
        RenderSystem.translatef(-8, -8, 0);
        MatrixStack matrixStack = new MatrixStack();
        matrixStack.translate(0, 0, this.itemRenderer.blitOffset+200);
        int numberOfRings = allocateSizes.size();
        for (int i = 0; i < numberOfRings; i++) {
            //int slices = i < numberOfRings-1 ? 9 : numberOfSlices%9 == 0 ? 9 : numberOfSlices%9;
            int slices = allocateSizes.get(i);
            for (int j = 0; j < slices; j++) {
                float start = (((j - 0.5f) / (float) slices) + 0.25f) * 360;
                float end = (((j + 0.5f) / (float) slices) + 0.25f) * 360;
                float addRadius = (radiusIn+5)*i;
                float itemRadius = (radiusIn+radiusOut+(addRadius*2))/2;
                float middle = (float) Math.toRadians(start+end)/2;
                float midX = x + itemRadius * (float) Math.cos(middle);
                float midY = y + itemRadius * (float) Math.sin(middle);
                int current = j+(( i == 0 ? 0 : getCount(i) ));
                this.itemRenderer.renderAndDecorateItem(containedItems.get(current), (int)midX, (int)midY);
                this.itemRenderer.renderGuiItemDecorations(this.font, containedItems.get(current), (int)midX, (int)midY, "");
                this.font.draw(matrixStack, containedItems.get(current).getCount()+"", midX+5, midY+5, Color.WHITE.getRGB());
            }
        }
        RenderSystem.popMatrix();
        RenderHelper.turnOff();
    }

    public void drawCount(MatrixStack matrixStack, List<Integer> allocateSizes, int x, int y, float radiusIn, float radiusOut){
        int numberOfRings = allocateSizes.size();
        for (int i = 0; i < numberOfRings; i++) {
            //int slices = i < numberOfRings-1 ? 9 : numberOfSlices%9 == 0 ? 9 : numberOfSlices%9;
            int slices = allocateSizes.get(i);
            for (int j = 0; j < slices; j++) {
                float start = (((j - 0.5f) / (float) slices) + 0.25f) * 360;
                float end = (((j + 0.5f) / (float) slices) + 0.25f) * 360;
                float addRadius = (radiusIn+5)*i;
                float itemRadius = (radiusIn+radiusOut+(addRadius*2))/2;
                float middle = (float) Math.toRadians(start+end)/2;
                float midX = x + itemRadius * (float) Math.cos(middle);
                float midY = y + itemRadius * (float) Math.sin(middle);
                int current = j+(( i == 0 ? 0 : getCount(i) ));
                this.font.drawShadow(matrixStack, containedItems.get(current).getCount()+"", (int)midX, (int)midY, Color.WHITE.getRGB());

            }
        }
    }

    public void drawBackground(List<Integer> allocateSizes, int mouseX, int mouseY, int x, int y, float radiusIn, float radiusOut){
        RenderSystem.disableAlphaTest();
        RenderSystem.enableBlend();
        RenderSystem.disableTexture();
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);

        Tessellator tesselator = Tessellator.getInstance();
        BufferBuilder buffer = tesselator.getBuilder();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);

        //int numberOfRings = (int)Math.ceil(numberOfSlices/9.0D);
        int numberOfRings = allocateSizes.size();
        for (int i = 0; i < numberOfRings; i++) {
            //int slices = i < numberOfRings-1 ? 9 : numberOfSlices%9 == 0 ? 9 : numberOfSlices%9;
            int slices = allocateSizes.get(i);
            for (int j = 0; j < slices; j++){
                float s0 = (((0 - 0.5f) / (float) slices) + 0.25f) * 360;
                double angle = Math.toDegrees(Math.atan2(mouseY - y, mouseX - x)); //Angle the mouse makes with the screen's equator
                double distance = Math.sqrt(Math.pow(mouseX - x, 2) + Math.pow(mouseY - y, 2)); //Distance of the mouse from the center of the screen
                if (angle < s0) {
                    angle += 360;
                }
                float start = (((j - 0.5f) / (float) slices) + 0.25f) * 360;
                float end = (((j + 0.5f) / (float) slices) + 0.25f) * 360;
                float addRadius = (radiusIn+5)*i;
                if ((distance >= (radiusOut+addRadius) && distance < (radiusIn+((radiusIn+5)*(i+1)))) || distance < radiusIn || distance >= radiusOut+((radiusIn+5)*(numberOfRings-1)))
                    selected = -1;
                if (angle >= start && angle < end && distance >= (radiusIn+addRadius) && distance < (radiusOut+addRadius)) {
                    selected = j+(( i == 0 ? 0 : getCount(i) ));
                    break;
                }
            }
        }

        for (int i = 0; i < numberOfRings; i++) {
            //int slices = i < numberOfRings-1 ? 9 : numberOfSlices%9 == 0 ? 9 : numberOfSlices%9;
            int slices = allocateSizes.get(i);
            for (int j = 0; j < slices; j++){
                float start = (((j - 0.5f) / (float) slices) + 0.25f) * 360;
                float end = (((j + 0.5f) / (float) slices) + 0.25f) * 360;
                float addRadius = (radiusIn+5)*i;
                int current = j+(( i == 0 ? 0 : getCount(i) ));

                if (selected == current)
                    drawPieArc(buffer, x, y, 0, radiusIn+addRadius, radiusOut+addRadius, start, end, 255, 255, 255, 64);
                else
                    drawPieArc(buffer, x, y, 0, radiusIn + addRadius, radiusOut + addRadius, start, end, 0, 0, 0, 64);

                if (selectedItem.equals(containedItems.get(current), false))
                    drawPieArc(buffer, x, y, 0, radiusIn+addRadius, radiusOut+addRadius, start, end, 0, 255, 0, 64);

            }
        }

        tesselator.end();
        RenderSystem.disableAlphaTest();
        RenderSystem.disableBlend();
        RenderSystem.enableTexture();
    }

    public void drawPieArc(BufferBuilder buffer, float x, float y, float z, float radiusIn, float radiusOut, float startAngle, float endAngle, int r, int g, int b, int a){
        float angle = endAngle - startAngle;
        int sections = (int)Math.max(1, Math.nextUp(angle / 5.0F));

        startAngle = (float) Math.toRadians(startAngle);
        endAngle = (float) Math.toRadians(endAngle);
        angle = endAngle - startAngle;

        for (int i = 0; i < sections; i++)
        {
            float angle1 = startAngle + (i / (float) sections) * angle;
            float angle2 = startAngle + ((i + 1) / (float) sections) * angle;

            float pos1InX = x + radiusIn * (float) Math.cos(angle1);
            float pos1InY = y + radiusIn * (float) Math.sin(angle1);
            float pos1OutX = x + radiusOut * (float) Math.cos(angle1);
            float pos1OutY = y + radiusOut * (float) Math.sin(angle1);
            float pos2OutX = x + radiusOut * (float) Math.cos(angle2);
            float pos2OutY = y + radiusOut * (float) Math.sin(angle2);
            float pos2InX = x + radiusIn * (float) Math.cos(angle2);
            float pos2InY = y + radiusIn * (float) Math.sin(angle2);

            buffer.vertex(pos1OutX, pos1OutY, z).color(r, g, b, a).endVertex();
            buffer.vertex(pos1InX, pos1InY, z).color(r, g, b, a).endVertex();
            buffer.vertex(pos2InX, pos2InY, z).color(r, g, b, a).endVertex();
            buffer.vertex(pos2OutX, pos2OutY, z).color(r, g, b, a).endVertex();
        }
    }

    @SubscribeEvent
    public static void overlayEvent(RenderGameOverlayEvent.Pre event){
        if (Minecraft.getInstance().screen instanceof BulletRadialMenu) {
            if (event.getType() == RenderGameOverlayEvent.ElementType.CROSSHAIRS){
                event.setCanceled(true);
            }
        }
    }

    @Override
    public void removed() {
        super.removed();
        ClientEvents.wipeOpen();
    }

    @Override
    public void tick() {
        super.tick();
        if (!ClientEvents.isKeyDown(key)){
            Minecraft.getInstance().setScreen(null);
            ClientEvents.wipeOpen();
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
