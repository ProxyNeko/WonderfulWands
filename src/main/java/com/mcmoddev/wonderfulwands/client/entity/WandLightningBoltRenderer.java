package com.mcmoddev.wonderfulwands.client.entity;

import com.mcmoddev.wonderfulwands.WonderfulWands;
import com.mcmoddev.wonderfulwands.common.projectiles.EntityBoltLightning;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class WandLightningBoltRenderer extends Render<EntityBoltLightning> {

    private static final ResourceLocation texture = new ResourceLocation(WonderfulWands.MODID + ":textures/entity/lightning_bolt.png");

    public WandLightningBoltRenderer(RenderManager renderManager) {
        super(renderManager);
    }

    /**
     * Actually renders the given argument.
     */
    public void doRender(EntityBoltLightning entity, double posX, double posY, double posZ, float yaw, float partialTick) {
        bindEntityTexture(entity);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.pushMatrix();
        GlStateManager.disableLighting();
        GlStateManager.enableBlend();
        GlStateManager.translate(posX, posY, posZ);
        GlStateManager.rotate(270F - entity.rotationYaw, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-1 * entity.rotationPitch, 0.0F, 0.0F, 1.0F);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder vertexbuffer = tessellator.getBuffer();
        GlStateManager.enableRescaleNormal();
        double size = 16D * 1;
        float fc = 0.05625F;
        GL11.glRotatef(45.0F, 1.0F, 0.0F, 0.0F); // rotate plane each iteration of loop
        for (int i = 0; i < 4; ++i) {
            GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F); // rotate plane each iteration of loop
            GL11.glNormal3f(0.0F, 0.0F, fc);
            vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
            vertexbuffer.pos(0.0D, -2.0D, 0.0D).tex((double) 0, (double) 0).endVertex();
            vertexbuffer.pos(size, -2.0D, 0.0D).tex(1, (double) 0).endVertex();
            vertexbuffer.pos(size, 2.0D, 0.0D).tex(1, (double) 1).endVertex();
            vertexbuffer.pos(0.0D, 2.0D, 0.0D).tex((double) 0, (double) 1).endVertex();
            tessellator.draw();
        }

        GlStateManager.disableRescaleNormal();
        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        GlStateManager.popMatrix();
        super.doRender(entity, posX, posY, posZ, yaw, partialTick);
    }

    /**
     * Returns the location of an entity's texture.
     */
    protected ResourceLocation getEntityTexture(EntityBoltLightning entity) {
        return texture;
    }
}