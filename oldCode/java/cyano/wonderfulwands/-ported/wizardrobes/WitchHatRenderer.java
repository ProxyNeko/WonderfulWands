package cyano.wonderfulwands.wizardrobes;

import net.minecraft.util.ResourceLocation;
import cyano.wonderfulwands.WonderfulWandsOld;

public class WitchHatRenderer extends WizardHatRenderer {
	public WitchHatRenderer(){
		super();
		super.hatTexture = new ResourceLocation(WonderfulWandsOld.MODID+":textures/witchblack.png");
	}
}
