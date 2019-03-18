package com.mcmoddev.wonderfulwands.client.entity;

import com.mcmoddev.wonderfulwands.WonderfulWands;
import net.minecraft.client.renderer.entity.RenderArrow;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class MagicMissileRenderer extends RenderArrow {

    private static final ResourceLocation arrowTextures = new ResourceLocation(WonderfulWands.MODID + ":textures/entity/magic_missile.png");

    public MagicMissileRenderer(RenderManager renderManager) {
        super(renderManager);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     *
     * @param entity
     */
    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return arrowTextures;
    }
}
