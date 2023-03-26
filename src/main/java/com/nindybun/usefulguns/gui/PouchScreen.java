package com.nindybun.usefulguns.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.nindybun.usefulguns.UsefulGuns;
import com.nindybun.usefulguns.items.PouchTypes;
import com.nindybun.usefulguns.network.PacketHandler;
import com.nindybun.usefulguns.network.packets.PacketSavePouchInventory;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.awt.*;

public class PouchScreen extends AbstractContainerScreen<PouchContainer> {
    private final ResourceLocation TEXTURE;

    public PouchScreen(PouchContainer container, Inventory playerInventory, Component name) {
        super(container, playerInventory, name);
        PouchTypes type = container.getPouchType();
        TEXTURE = type.texture;
        imageWidth = type.textureW;
        imageHeight = type.textureH;
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    protected void renderBg(PoseStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int edgeSpacingX = (this.width - imageWidth) / 2;
        int edgeSpacingY = (this.height - imageHeight) / 2;
        this.blit(matrixStack, edgeSpacingX, edgeSpacingY, 0, 0, imageWidth, imageHeight, imageWidth, imageHeight);
    }

    @Override
    protected void renderLabels(PoseStack matrixStack, int x, int y) {
        this.font.draw(matrixStack, this.title.getString(), 7, 6, Color.DARK_GRAY.getRGB());
    }

    @Override
    public void render(PoseStack matrixStack, int x, int y, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, x, y, partialTicks);
        this.renderTooltip(matrixStack, x, y);
    }

    @Override
    public void removed() {
        PacketHandler.sendToServer(new PacketSavePouchInventory());
        super.removed();
    }
}
