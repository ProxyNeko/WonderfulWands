package com.mcmoddev.wonderfulwands.init;

import com.mcmoddev.wonderfulwands.WonderfulWands;
import com.mcmoddev.wonderfulwands.api.blocks.WWBlocks;
import com.mcmoddev.wonderfulwands.common.items.WandOfBridging;
import com.mcmoddev.wonderfulwands.common.items.WandOfClimbing;
import com.mcmoddev.wonderfulwands.common.items.WandOfDeath;
import com.mcmoddev.wonderfulwands.common.items.WandOfFire;
import com.mcmoddev.wonderfulwands.common.items.WandOfGreaterLight;
import com.mcmoddev.wonderfulwands.common.items.WandOfGrowth;
import com.mcmoddev.wonderfulwands.common.items.WandOfHarvesting;
import com.mcmoddev.wonderfulwands.common.items.WandOfHealing;
import com.mcmoddev.wonderfulwands.common.items.WandOfIce;
import com.mcmoddev.wonderfulwands.common.items.WandOfIllusions;
import com.mcmoddev.wonderfulwands.common.items.WandOfLevitation;
import com.mcmoddev.wonderfulwands.common.items.WandOfLight;
import com.mcmoddev.wonderfulwands.common.items.WandOfLightning;
import com.mcmoddev.wonderfulwands.common.items.WandOfMagicMissile;
import com.mcmoddev.wonderfulwands.common.items.WandOfMining;
import com.mcmoddev.wonderfulwands.common.items.WandOfRails;
import com.mcmoddev.wonderfulwands.common.items.WandOfStorms;
import com.mcmoddev.wonderfulwands.common.items.WandOfTeleportation;
import com.mcmoddev.wonderfulwands.common.items.WandOfTunneling;
import com.mcmoddev.wonderfulwands.common.items.WandOfWebbing;
import com.mcmoddev.wonderfulwands.common.items.hats.TopHat;
import com.mcmoddev.wonderfulwands.common.items.hats.WitchesHat;
import com.mcmoddev.wonderfulwands.common.items.hats.WizardsHat;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = WonderfulWands.MODID)
public class WWItemRegistry {

    @SubscribeEvent
    public static void onRegisterItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(
                nameWand(new Item(), "wand_generic"),
                nameWand(new WandOfMagicMissile(), "wand_of_magic_missile"),
                nameWand(new WandOfDeath(), "wand_of_death"),
                nameWand(new WandOfFire(), "wand_of_fire"),
                nameWand(new WandOfGrowth(), "wand_of_growth"),
                nameWand(new WandOfHarvesting(), "wand_of_harvesting"),
                nameWand(new WandOfHealing(), "wand_of_healing"),
                nameWand(new WandOfIce(), "wand_of_ice"),
                nameWand(new WandOfMining(), "wand_of_mining"),
                nameWand(new WandOfTeleportation(), "wand_of_teleportation"),
                nameWand(new WandOfLight(), "wand_of_light"),
                nameWand(new WandOfGreaterLight(), "wand_of_greater_light"),
                nameWand(new WandOfStorms(), "wand_of_storms"),
                nameWand(new WandOfLightning(), "wand_of_lightning"),
                nameWand(new WandOfBridging(), "wand_of_bridging"),
                nameWand(new WandOfClimbing(), "wand_of_climbing"),
                nameWand(new WandOfIllusions(), "wand_of_illusions"),
                nameWand(new WandOfRails(), "wand_of_rails"),
                nameWand(new WandOfWebbing(), "wand_of_webbing"),
                nameWand(new WandOfLevitation(), "wand_of_levitation"),
                nameWand(new WandOfTunneling(), "wand_of_tunneling")
        );

        event.getRegistry().registerAll(
                nameItem(new TopHat(), "top_hat"),
                nameItem(new WitchesHat(), "witches_hat"),
                nameItem(new WizardsHat(), "wizards_hat")
        );

        //TODO See if we should keep the items for creative tabs or not as they don't exist and can't be placed without the wands in previous versions
        event.getRegistry().registerAll(
                nameBlockItem(WWBlocks.FEY_RAIL),
                nameBlockItem(WWBlocks.FEY_RAIL_POWERED),
                nameBlockItem(WWBlocks.MAGE_LIGHT)
        );
    }

    private static Item nameItem(Item item, String name) {
        item
                .setRegistryName(name)
                .setTranslationKey(WonderfulWands.MODID + "." + name)
                .setCreativeTab(WonderfulWands.TAB_ROBES);
        return item;
    }

    private static Item nameWand(Item item, String name) {
        item
                .setRegistryName(name)
                .setTranslationKey(WonderfulWands.MODID + "." + name)
                .setCreativeTab(WonderfulWands.TAB_WANDS);
        return item;
    }

    private static ItemBlock nameBlockItem(Block block) {
        ItemBlock item = new ItemBlock(block);
        ResourceLocation name = block.getRegistryName();
        item.setRegistryName(name);
        return item;
    }
}