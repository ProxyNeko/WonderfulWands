package com.mcmoddev.wonderfulwands.client.hats;

import com.mcmoddev.wonderfulwands.WonderfulWands;
import com.mcmoddev.wonderfulwands.client.hats.models.TopHatBlackModel;
import com.mcmoddev.wonderfulwands.client.hats.models.TopHatWhiteModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class TopHatRenderer extends ModelBiped {

    TopHatBlackModel hatBlack;
    TopHatWhiteModel hatBand;
    private ResourceLocation blackTexture = new ResourceLocation(WonderfulWands.MODID + ":textures/witchblack.png");
    private ResourceLocation whiteTexture = new ResourceLocation(WonderfulWands.MODID + ":textures/fancywhite.png");

    public TopHatRenderer() {
        hatBlack = new TopHatBlackModel();
        hatBand = new TopHatWhiteModel();
    }

    /** render the hat on head */
    @Override
    public void render(Entity entity, float posX, float posY, float posZ, float hitX, float hitY, float hitZ) {
        GlStateManager.pushMatrix();
        float yRotation = hitY / (180F / (float) Math.PI);
        float xRotation = hitZ / (180F / (float) Math.PI);
        if (entity.isSneaking()) {
            GlStateManager.translate(0, 0.25F, 0);
        }

        Minecraft.getMinecraft().getRenderManager().renderEngine.bindTexture(blackTexture);
        hatBlack.render(yRotation, xRotation);
        Minecraft.getMinecraft().getRenderManager().renderEngine.bindTexture(whiteTexture);
        hatBand.render(yRotation, xRotation);
        GlStateManager.popMatrix();
    }
}