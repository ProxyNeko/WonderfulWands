package com.mcmoddev.wonderfulwands.client.hats;

import com.mcmoddev.wonderfulwands.WonderfulWands;
import net.minecraft.util.ResourceLocation;

public class WitchHatRenderer extends WizardHatRenderer {

    public WitchHatRenderer() {
        super();
        super.hatTexture = new ResourceLocation(WonderfulWands.MODID + ":textures/witchblack.png");
    }
}
