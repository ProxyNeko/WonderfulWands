package com.mcmoddev.wonderfulwands;

import com.mcmoddev.wonderfulwands.api.blocks.WWBlocks;
import com.mcmoddev.wonderfulwands.api.items.WWItems;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLFingerprintViolationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(
        modid = WonderfulWands.MODID,
        name = WonderfulWands.NAME,
        version = WonderfulWands.VERSION,
        acceptedMinecraftVersions = WonderfulWands.MC_VERSION,
        certificateFingerprint = WonderfulWands.FINGERPRINT,
        updateJSON = WonderfulWands.UPDATE_JSON
)
public class WonderfulWands {
    //TODO Langs all need fixing.
    //TODO Robes need adding somehow.
    //TODO Illusiory blocks need adding.

    public static final String MODID = "wonderfulwands";
    public static final String NAME = "Wonderful Wands";
    public static final String VERSION = "3.0.0";
    public static final String MC_VERSION = "[1.12,)";
    public static final String FINGERPRINT = "@FINGERPRINT@";
    public static final String UPDATE_JSON = "https://raw.githubusercontent.com/MinecraftModDevelopmentMods/WonderfulWands/master/update.json";
    public static final Logger LOGGER = LogManager.getLogger(NAME);

    public static ItemArmor.ArmorMaterial NONARMOR = EnumHelper.addArmorMaterial("NONARMOR", "empty_armor", 10, new int[]{0, 0, 0, 0}, 0, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0);
    public static ItemArmor.ArmorMaterial WIZARDROBES = EnumHelper.addArmorMaterial("WIZARDCLOTH", "wizard_robes", 15, new int[]{1, 1, 1, 1}, 40, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0);

    @Mod.EventHandler
    public void onFingerprintViolation(FMLFingerprintViolationEvent event) {
        FMLLog.bigWarning("Invalid fingerprint detected! The file " + event.getSource().getName() + " may have been tampered with. This version will NOT be supported by Proxy!");
    }

    public static final CreativeTabs TAB_WANDS = new CreativeTabs("tab." + MODID + ".wands") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(WWItems.WAND_GENERIC);
        }
    };

    public static final CreativeTabs TAB_ROBES = new CreativeTabs("tab." + MODID + ".armor") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(WWItems.WIZARDS_HAT);
        }
    };

    public static final CreativeTabs TAB_BLOCKS = new CreativeTabs("tab." + MODID + ".blocks") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(WWBlocks.MAGE_LIGHT);
        }
    };

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        LOGGER.info("Is this magical robe for me??");
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        LOGGER.info("Ooo a wand too?!");
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        LOGGER.info("I'm a real Wizard!");
    }

}
