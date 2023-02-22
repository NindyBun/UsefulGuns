package com.nindybun.usefulguns.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.nindybun.usefulguns.UsefulGuns;
import com.nindybun.usefulguns.items.PouchTypes;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import java.awt.*;

public class PouchScreen extends ContainerScreen<PouchContainer> {
    private final ResourceLocation TEXTURE;

    public PouchScreen(PouchContainer container, PlayerInventory playerInventory, ITextComponent name) {
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
    protected void renderBg(MatrixStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.blendColor(1.0f, 1.0f, 1.0f, 1.0f);
        this.getMinecraft().textureManager.bind(TEXTURE);
        int edgeSpacingX = (this.width - imageWidth) / 2;
        int edgeSpacingY = (this.height - imageHeight) / 2;
        this.blit(matrixStack, edgeSpacingX, edgeSpacingY, 0, 0, imageWidth, imageHeight, imageWidth, imageHeight);
    }

    @Override
    protected void renderLabels(MatrixStack matrixStack, int x, int y) {
        this.font.draw(matrixStack, this.title.getString(), 7, 6, Color.DARK_GRAY.getRGB());
    }

    @Override
    public void render(MatrixStack matrixStack, int x, int y, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, x, y, partialTicks);
        this.renderTooltip(matrixStack, x, y);
    }
}
