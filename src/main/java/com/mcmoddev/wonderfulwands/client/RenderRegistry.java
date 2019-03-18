package com.mcmoddev.wonderfulwands.client;

import com.mcmoddev.wonderfulwands.WonderfulWands;
import com.mcmoddev.wonderfulwands.api.blocks.WWBlocks;
import com.mcmoddev.wonderfulwands.api.items.WWItems;
import com.mcmoddev.wonderfulwands.client.entity.LightWispRenderer;
import com.mcmoddev.wonderfulwands.client.entity.MagicMissileRenderer;
import com.mcmoddev.wonderfulwands.client.entity.WandLightningBoltRenderer;
import com.mcmoddev.wonderfulwands.client.hats.TopHatRenderer;
import com.mcmoddev.wonderfulwands.client.hats.WitchHatRenderer;
import com.mcmoddev.wonderfulwands.client.hats.WizardHatRenderer;
import com.mcmoddev.wonderfulwands.common.entities.EntityLightWisp;
import com.mcmoddev.wonderfulwands.common.projectiles.EntityBoltLightning;
import com.mcmoddev.wonderfulwands.common.projectiles.EntityMagicMissile;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(modid = WonderfulWands.MODID, value = Side.CLIENT)
public class RenderRegistry {

    @SubscribeEvent
    public static void onRegisterRenders(ModelRegistryEvent event) {
        initModel(WWItems.WAND_GENERIC, 0);
        initModel(WWItems.WAND_OF_MAGIC_MISSILE, 0);
        initModel(WWItems.WAND_OF_DEATH, 0);
        initModel(WWItems.WAND_OF_FIRE, 0);
        initModel(WWItems.WAND_OF_GROWTH, 0);
        initModel(WWItems.WAND_OF_HARVESTING, 0);
        initModel(WWItems.WAND_OF_HEALING, 0);
        initModel(WWItems.WAND_OF_ICE, 0);
        initModel(WWItems.WAND_OF_MINING, 0);
        initModel(WWItems.WAND_OF_TELEPORTATION, 0);
        initModel(WWItems.WAND_OF_LIGHT, 0);
        initModel(WWItems.WAND_OF_GREATER_LIGHT, 0);
        initModel(WWItems.WAND_OF_STORMS, 0);
        initModel(WWItems.WAND_OF_LIGHTNING, 0);
        initModel(WWItems.WAND_OF_BRIDGING, 0);
        initModel(WWItems.WAND_OF_CLIMBING, 0);
        initModel(WWItems.WAND_OF_ILLUSIONS, 0);
        initModel(WWItems.WAND_OF_RAILS, 0);
        initModel(WWItems.WAND_OF_WEBBING, 0);
        initModel(WWItems.WAND_OF_LEVITATION, 0);
        initModel(WWItems.WAND_OF_TUNNELING, 0);
        initModel(WWItems.TOP_HAT, 0);
        initModel(WWItems.WITCHES_HAT, 0);
        initModel(WWItems.WIZARDS_HAT, 0);

        initModel(Item.getItemFromBlock(WWBlocks.FEY_RAIL), 0);
        initModel(Item.getItemFromBlock(WWBlocks.FEY_RAIL_POWERED), 0);
        initModel(Item.getItemFromBlock(WWBlocks.MAGE_LIGHT), 0);

        RenderingRegistry.registerEntityRenderingHandler(EntityMagicMissile.class, (RenderManager renderManager) -> {
            return new MagicMissileRenderer(renderManager);
        });
        RenderingRegistry.registerEntityRenderingHandler(EntityBoltLightning.class, (RenderManager renderManager) -> {
            return new WandLightningBoltRenderer(renderManager);
        });
        RenderingRegistry.registerEntityRenderingHandler(EntityLightWisp.class, (RenderManager renderManager) -> {
            return new LightWispRenderer(renderManager);
        });

        new TopHatRenderer();
        new WitchHatRenderer();
        new WizardHatRenderer();
    }

    private static void initModel(Item item, int meta) {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName(), "inventory"));
    }
}
