package com.nindybun.usefulguns.entities;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.nindybun.usefulguns.UsefulGuns;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.ClippingHelper;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;

public class BulletEntityRenderer extends EntityRenderer<BulletEntity> {
    private final ResourceLocation TEXTURE = new ResourceLocation(UsefulGuns.MOD_ID, "textures/blocks/light.png");

    public BulletEntityRenderer(EntityRendererManager p_i46179_1_) {
        super(p_i46179_1_);
    }

    @Override
    public void render(BulletEntity entity, float yaw, float ticks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight) {
        matrixStack.pushPose();

        matrixStack.scale(1f, 1f, 1f);
        matrixStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(180f));
        MatrixStack.Entry entry = matrixStack.last();

        Matrix4f matrix4f = entry.pose();
        Matrix3f matrix3f = entry.normal();

        IVertexBuilder builder = buffer.getBuffer(RenderType.entityCutout(this.getTextureLocation(entity)));
        this.vertex(builder, matrix4f, matrix3f, packedLight, 0.0F, 0, 0, 1);
        this.vertex(builder, matrix4f, matrix3f, packedLight, 1.0F, 0, 1, 1);
        this.vertex(builder, matrix4f, matrix3f, packedLight, 1.0F, 1, 1, 0);
        this.vertex(builder, matrix4f, matrix3f, packedLight, 0.0F, 1, 0, 0);
        matrixStack.popPose();
        super.render(entity, yaw, ticks, matrixStack, buffer, packedLight);
    }

    private static void vertex(IVertexBuilder p_229045_0_, Matrix4f p_229045_1_, Matrix3f p_229045_2_, int p_229045_3_, float p_229045_4_, int p_229045_5_, int p_229045_6_, int p_229045_7_) {
        p_229045_0_.vertex(p_229045_1_, p_229045_4_ - 0.5F, (float)p_229045_5_ - 0.375F, 0.0F).color(255, 255, 255, 255).uv((float)p_229045_6_, (float)p_229045_7_).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(p_229045_3_).normal(p_229045_2_, 0.0F, 1.0F, 0.0F).endVertex();
    }

    @Override
    public ResourceLocation getTextureLocation(BulletEntity p_110775_1_) {
        return TEXTURE;
    }
}
