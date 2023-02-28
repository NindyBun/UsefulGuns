package com.nindybun.usefulguns.entities;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class BulletEntityRenderer extends EntityRenderer<BulletEntity> {
    public BulletEntityRenderer(EntityRendererManager p_i46179_1_) {
        super(p_i46179_1_);
    }

    @Override
    public void render(BulletEntity p_225623_1_, float p_225623_2_, float p_225623_3_, MatrixStack p_225623_4_, IRenderTypeBuffer p_225623_5_, int p_225623_6_) {
        super.render(p_225623_1_, p_225623_2_, p_225623_3_, p_225623_4_, p_225623_5_, p_225623_6_);
    }

    @Override
    public ResourceLocation getTextureLocation(BulletEntity p_110775_1_) {
        return null;
    }
}
