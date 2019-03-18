package com.mcmoddev.wonderfulwands.client.hats;

import com.mcmoddev.wonderfulwands.WonderfulWands;
import com.mcmoddev.wonderfulwands.client.hats.models.WizardHatModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.FMLClientHandler;
import org.lwjgl.opengl.GL11;

public class WizardHatRenderer extends ModelBiped {

    private WizardHatModel hatModel;
    public ResourceLocation hatTexture = new ResourceLocation(WonderfulWands.MODID + ":textures/bluenstars.png");

    public WizardHatRenderer() {
        hatModel = new WizardHatModel();
    }

    @Override
    public void render(Entity entity, float posX, float posY, float posZ, float hitX, float hitY, float hitZ) {
        float yRotation = hitY / (180F / (float) Math.PI);
        float xRotation = hitZ / (180F / (float) Math.PI);
        if (entity.isSneaking()) {
            GlStateManager.translate(0, -0.125F, 0);
        }

        Minecraft.getMinecraft().getRenderManager().renderEngine.bindTexture(hatTexture);
        hatModel.render(yRotation, xRotation);
    }

    //TODO Commented out in a previous version. See if it does anything.
    /*
    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return true;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {

        switch (type) {
            case ENTITY: {
                renderItem3D(0f, 0f, 0f, 0.5f);
                return;
            }

            case EQUIPPED: {
                renderItem3D(0f, 1f, 1f, 0.5f);
                return;
            }

            case INVENTORY: {
                renderItem3D(0f, 0f, 0f, 0.5f);
                return;
            }

            default:
                return;
        }
    }

    private void renderItem3D(float x, float y, float z, float scale) {
        GL11.glPushMatrix();

        // disable lighting in inventory render
        GL11.glDisable(GL11.GL_LIGHTING);

        GL11.glTranslatef((float) x + 0.5F, (float) y + 0.75F * 1.0F, (float) z + 0.5F);

        GL11.glScalef(1.0F, 1.0F, 1.0F);

        FMLClientHandler.instance().getClient().renderEngine.func_110577_a(hatTexture);
        hatModel.render();

        // re-enable lighting
        GL11.glEnable(GL11.GL_LIGHTING);

        GL11.glPopMatrix();
    }
    */
}
