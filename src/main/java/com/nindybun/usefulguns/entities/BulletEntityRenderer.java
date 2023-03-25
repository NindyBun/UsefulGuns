package com.nindybun.usefulguns.entities;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import com.nindybun.usefulguns.UsefulGuns;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class BulletEntityRenderer extends EntityRenderer<BulletEntity> {
    private final ResourceLocation TEXTURE = new ResourceLocation(UsefulGuns.MOD_ID, "textures/blocks/light.png");

    public BulletEntityRenderer(EntityRendererProvider.Context p_i46179_1_) {
        super(p_i46179_1_);
    }

    @Override
    public void render(BulletEntity entity, float yaw, float ticks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight) {
        matrixStack.pushPose();

        matrixStack.scale(1f, 1f, 1f);
        matrixStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(180f));
        PoseStack.Pose entry = matrixStack.last();

        Matrix4f matrix4f = entry.pose();
        Matrix3f matrix3f = entry.normal();

        VertexConsumer builder = buffer.getBuffer(RenderType.entityCutout(this.getTextureLocation(entity)));
        this.vertex(builder, matrix4f, matrix3f, packedLight, 0.0F, 0, 0, 1);
        this.vertex(builder, matrix4f, matrix3f, packedLight, 1.0F, 0, 1, 1);
        this.vertex(builder, matrix4f, matrix3f, packedLight, 1.0F, 1, 1, 0);
        this.vertex(builder, matrix4f, matrix3f, packedLight, 0.0F, 1, 0, 0);
        matrixStack.popPose();
        super.render(entity, yaw, ticks, matrixStack, buffer, packedLight);
    }

    private static void vertex(VertexConsumer p_229045_0_, Matrix4f p_229045_1_, Matrix3f p_229045_2_, int p_229045_3_, float p_229045_4_, int p_229045_5_, int p_229045_6_, int p_229045_7_) {
        p_229045_0_.vertex(p_229045_1_, p_229045_4_ - 0.5F, (float)p_229045_5_ - 0.375F, 0.0F).color(255, 255, 255, 255).uv((float)p_229045_6_, (float)p_229045_7_).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(p_229045_3_).normal(p_229045_2_, 0.0F, 1.0F, 0.0F).endVertex();
    }

    @Override
    public ResourceLocation getTextureLocation(BulletEntity p_110775_1_) {
        return TEXTURE;
    }
}
