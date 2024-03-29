package com.nindybun.usefulguns.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.nindybun.usefulguns.UsefulGuns;
import com.nindybun.usefulguns.events.ClientEvents;
import com.nindybun.usefulguns.items.AbstractPouch;
import com.nindybun.usefulguns.items.bullets.AbstractBullet;
import com.nindybun.usefulguns.items.bullets.MiningBullet;
import com.nindybun.usefulguns.items.bullets.ShotgunBullet;
import com.nindybun.usefulguns.items.guns.AbstractGun;
import com.nindybun.usefulguns.items.guns.AbstractShotgun;
import com.nindybun.usefulguns.network.PacketHandler;
import com.nindybun.usefulguns.network.packets.PacketSaveSelection;
import com.nindybun.usefulguns.util.UtilMethods;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.IItemHandler;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = UsefulGuns.MOD_ID, value = Dist.CLIENT)
public class BulletRadialMenu extends Screen {
    private int selected;
    private ItemStack selectedItem;
    int[] ringSize = {4, 6, 8};
    private List<ItemStack> containedItemsAnAmount;

/*    private Player player;
    private ItemStack gun;
    private List<ItemStack> containedItems = new ArrayList<>();
    private List<Integer> containedAmount = new ArrayList<>();*/

    /*public BulletRadialMenu(Player player){
        super(new TextComponent("Title"));
        this.player = player;
        this.gun = !(player.getMainHandItem().getItem() instanceof AbstractGun) ? player.getOffhandItem(): player.getMainHandItem();
        this.selected = -1;
        this.selectedItem = ItemStack.of(gun.getOrCreateTag().getCompound("Bullet_Info"));
        ItemStack pouch = UtilMethods.locateAndGetPouch(player);
        if (pouch.isEmpty())
            return;
        collectBullets(gun, pouch);
    }*/

    public BulletRadialMenu(ItemStack pouch, ItemStack gun){
        super(Component.literal("Title"));
        this.selected = -1;
        this.selectedItem = ItemStack.of(gun.getOrCreateTag().getCompound(UtilMethods.BULLET_INFO_TAG));
        this.containedItemsAnAmount = UtilMethods.deserializeItemTagList(pouch.getOrCreateTag().getList(UtilMethods.INVENTORY_TAG, Tag.TAG_COMPOUND))
                .stream().filter((itemStack) -> UtilMethods.isValidForGun(gun, itemStack)).collect(Collectors.toList());
    }

    /*public static boolean isValidForGun(ItemStack gun, ItemStack ammo){
        if (ammo.getItem() instanceof AbstractBullet){
            if (ammo.getItem() instanceof MiningBullet)
                return true;
            if (gun.getItem() instanceof AbstractShotgun && ammo.getItem() instanceof ShotgunBullet)
                return true;
            if (!(gun.getItem() instanceof AbstractShotgun) && !(ammo.getItem() instanceof ShotgunBullet))
                return true;
        }
        return false;
    }

    private void collectBullets(ItemStack gun, ItemStack pouch){
        LazyOptional<IItemHandler> optional = AbstractPouch.getData(pouch).getOptional();
        if (optional.isPresent()) {
            IItemHandler handler = optional.resolve().get();
            for (int i = 0; i < handler.getSlots(); i++) {
                ItemStack ammo = handler.getStackInSlot(i);
                if (ammo != ItemStack.EMPTY && isValidForGun(gun, ammo)){
                    ItemStack copy = ammo.copy().split(1);
                    int amount = ammo.getCount();
                    if (!doesContainInList(containedItems, ammo)){
                        containedItems.add(copy);
                        containedAmount.add(amount);
                    }else{
                        for (int j = 0; j < i; j++){
                            if (containedItems.get(j).equals(copy, false)){
                                containedAmount.set(j, containedAmount.get(j)+amount);
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    public static boolean doesContainInList(List<ItemStack> list, ItemStack itemStack){
        for (ItemStack stack : list){
            if (stack.copy().split(1).equals(itemStack.copy().split(1), false))
                return true;
        }
        return false;
    }*/

    @Override
    public boolean mouseReleased(double x, double y, int mouseButton) {
        processClick(true);
        return super.mouseReleased(x, y, mouseButton);
    }

    private void processClick(boolean mousePressed){
        processSlot();
    }

    private void processSlot(){
        if (selected != -1) PacketHandler.sendToServer(new PacketSaveSelection(containedItemsAnAmount.get(selected).copy().split(1)));
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
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float ticks_) {
        super.render(matrixStack, mouseX, mouseY, ticks_);
        int numberOfSlices = containedItemsAnAmount.size();
        if (numberOfSlices == 0)
            return;
        List<Integer> allocateSizes = new ArrayList<>();
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
        drawItem(matrixStack, allocateSizes, x, y, radiusIn, radiusOut);
        matrixStack.popPose();

        matrixStack.pushPose();
        drawToolTip(matrixStack, allocateSizes, x, y, radiusIn, radiusOut);
        matrixStack.popPose();
    }

    public void drawToolTip(PoseStack matrixStack, List<Integer> allocateSizes, int x, int y, float radiusIn, float radiusOut){
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
                float midX = x - itemRadius * (float) Math.cos(middle);
                float midY = y - itemRadius * (float) Math.sin(middle);
                int current = j+(( i == 0 ? 0 : getCount(i) ));
                if (selected == current)
                    this.renderTooltip(matrixStack, containedItemsAnAmount.get(current), (int)midX, (int)midY);
            }
        }
    }

    public void drawItem(PoseStack matrix, List<Integer> allocateSizes, int x, int y, float radiusIn, float radiusOut){
        PoseStack poseStack = RenderSystem.getModelViewStack();
        poseStack.pushPose();
        poseStack.mulPoseMatrix(matrix.last().pose());
        poseStack.translate(-8, -8, 0);
        RenderSystem.applyModelViewMatrix();
        matrix.translate(0, 0, this.itemRenderer.blitOffset+200);
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
                float midX = x - itemRadius * (float) Math.cos(middle);
                float midY = y - itemRadius * (float) Math.sin(middle);
                int current = j+(( i == 0 ? 0 : getCount(i) ));
                ItemStack stack = containedItemsAnAmount.get(current);
                int value = stack.getCount();
                String string = String.valueOf(value);
                this.itemRenderer.renderAndDecorateItem(stack, (int)midX, (int)midY);
                this.itemRenderer.renderGuiItemDecorations(this.font, stack, (int)midX, (int)midY, "");
                this.font.draw(matrix, value > 1 ? string : "", midX+17-font.width(string), midY+9, Color.WHITE.getRGB());
            }
        }
        poseStack.popPose();
        RenderSystem.applyModelViewMatrix();
    }

    public void drawBackground(List<Integer> allocateSizes, int mouseX, int mouseY, int x, int y, float radiusIn, float radiusOut){
        RenderSystem.enableBlend();
        RenderSystem.disableTexture();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder buffer = tesselator.getBuilder();
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

        int numberOfRings = allocateSizes.size();
        for (int i = 0; i < numberOfRings; i++) {
            int slices = allocateSizes.get(i);
            for (int j = 0; j < slices; j++){
                float s0 = (((0 - 0.5f) / (float) slices) + 0.25f) * 360;
                double angle = Math.toDegrees(Math.atan2(y-mouseY, x-mouseX)); //Angle the mouse makes with the screen's equator
                double distance = Math.sqrt(Math.pow(x-mouseX, 2) + Math.pow(y-mouseY, 2)); //Distance of the mouse from the center of the screen
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

                if (selectedItem.equals(containedItemsAnAmount.get(current).copy().split(1), false))
                    drawPieArc(buffer, x, y, 0, radiusIn+addRadius, radiusOut+addRadius, start, end, 0, 255, 0, 64);

            }
        }

        tesselator.end();
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

            //subtracting goes top clockwise
            //addition goes bottom clockwise
            float pos1InX = x - radiusIn * (float) Math.cos(angle1);
            float pos1InY = y - radiusIn * (float) Math.sin(angle1);
            float pos1OutX = x - radiusOut * (float) Math.cos(angle1);
            float pos1OutY = y - radiusOut * (float) Math.sin(angle1);
            float pos2OutX = x - radiusOut * (float) Math.cos(angle2);
            float pos2OutY = y - radiusOut * (float) Math.sin(angle2);
            float pos2InX = x - radiusIn * (float) Math.cos(angle2);
            float pos2InY = y - radiusIn * (float) Math.sin(angle2);

            buffer.vertex(pos1OutX, pos1OutY, z).color(r, g, b, a).endVertex();
            buffer.vertex(pos1InX, pos1InY, z).color(r, g, b, a).endVertex();
            buffer.vertex(pos2InX, pos2InY, z).color(r, g, b, a).endVertex();
            buffer.vertex(pos2OutX, pos2OutY, z).color(r, g, b, a).endVertex();
        }
    }

    @SubscribeEvent
    public static void overlayEvent(RenderGuiOverlayEvent.Pre event){
        if (Minecraft.getInstance().screen instanceof BulletRadialMenu) {
            if (event.getOverlay() == VanillaGuiOverlay.CROSSHAIR.type()){
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
        if (!ClientEvents.isKeyDown(ClientEvents.radialMenu_key)){
            Minecraft.getInstance().setScreen(null);
            ClientEvents.wipeOpen();
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
