package com.mcmoddev.wonderfulwands.common.items;

import com.mcmoddev.wonderfulwands.WonderfulWands;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.Map;

public class WizardingArmor extends ItemArmor {

    public static final int[] robeDamageReduction = new int[]{1, 1, 1, 1}; // head, shoulders, knees, and toes
    public static final int robeEnchantibility = 40;
    public static final int robeMaxDamageFactor = 15;
    private static final String itemName = "robes";
    final private String color;
    public static final Map<EntityEquipmentSlot, String> slotName = new HashMap<>();

    static {
        slotName.put(EntityEquipmentSlot.HEAD, "helmet");
        slotName.put(EntityEquipmentSlot.CHEST, "chestplate");
        slotName.put(EntityEquipmentSlot.LEGS, "leggings");
        slotName.put(EntityEquipmentSlot.FEET, "boots");
    }

    public WizardingArmor(ArmorMaterial material, String color, EntityEquipmentSlot armorSlot) {
        super(material, material.ordinal(), armorSlot);
        //super(ArmorMaterial.CHAIN,1, armorSlot); // does render as chainmail
        //add icons
        //func_111206_d("wizardrobes:robes"+armorSlot);
        String name = itemName + "_" + color + "_" + slotName.get(armorSlot);
        setRegistryName("robes" + armorSlot);
        setTranslationKey(WonderfulWands.MODID + "." + name);
        setCreativeTab(WonderfulWands.TAB_ROBES);
        this.color = color;
    }

    /**
     * Return whether this item is repairable in an anvil.
     */
    @Override
    public boolean getIsRepairable(ItemStack stack, ItemStack rawMaterial) {
        // repair with string or wool
        return rawMaterial.getTranslationKey().equals(Items.STRING.getTranslationKey())
                || rawMaterial.getTranslationKey().equals(Blocks.WOOL.getTranslationKey());
    }

    @SideOnly(Side.CLIENT)
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String layer) {
        // layer will either be "overlay" or null (ignore this variable)
        return WonderfulWands.MODID + ":textures/models/armor/robes_" + color + "_layer_" + (slot == EntityEquipmentSlot.LEGS ? 2 : 1) + ".png";
    }
}