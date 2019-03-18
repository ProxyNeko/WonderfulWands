package com.mcmoddev.wonderfulwands.client.entity;

import com.mcmoddev.wonderfulwands.WonderfulWands;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class LightWispRenderer extends Render {

    private static final ResourceLocation texture = new ResourceLocation(WonderfulWands.MODID + ":textures/entity/light_wisp.png");

    public LightWispRenderer(final RenderManager renderManager) {
        super(renderManager);
    }

    @Override
    public void doRender(Entity entity, double posX, double posY, double posZ, float f1, float f2) {
        GlStateManager.pushMatrix();
        bindEntityTexture(entity);
        GlStateManager.translate(posX, posY + 0.5, posZ);
        GlStateManager.enableRescaleNormal();
        GlStateManager.disableLighting();
        //final float scale = this.scale;
        //GlStateManager.scale(scale / 1.0f, scale / 1.0f, scale / 1.0f);
        final Tessellator instance = Tessellator.getInstance();
        final BufferBuilder worldRenderer = instance.getBuffer();
        final float minU = 0;
        final float maxU = 1;
        final float minV = 0;
        final float maxV = 1;
        this.bindTexture(getEntityTexture(entity));
        GlStateManager.rotate(180.0F - renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX_NORMAL);
        worldRenderer.pos(-0.5D, -0.25D, 0.0D).tex(minU, maxV).normal(0.0F, 1.0F, 0.0F).endVertex();
        worldRenderer.pos(0.5D, -0.25D, 0.0D).tex(maxU, maxV).normal(0.0F, 1.0F, 0.0F).endVertex();
        worldRenderer.pos(0.5D, 0.75D, 0.0D).tex(maxU, minV).normal(0.0F, 1.0F, 0.0F).endVertex();
        worldRenderer.pos(-0.5D, 0.75D, 0.0D).tex(minU, minV).normal(0.0F, 1.0F, 0.0F).endVertex();
        instance.draw();
        GlStateManager.enableLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
        super.doRender(entity, posX, posY, posZ, f1, f2);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return texture;
    }
}
