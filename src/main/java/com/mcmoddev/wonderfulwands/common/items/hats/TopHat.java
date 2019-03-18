package com.mcmoddev.wonderfulwands.common.items.hats;

import com.mcmoddev.wonderfulwands.WonderfulWands;
import com.mcmoddev.wonderfulwands.client.hats.TopHatRenderer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TopHat extends ItemArmor {

    public TopHat() {
        super(WonderfulWands.NONARMOR, 0, EntityEquipmentSlot.HEAD);
        setMaxDamage(100);
    }

    @SideOnly(Side.CLIENT)
    public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, ModelBiped biped) {
        return new TopHatRenderer();
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
        return WonderfulWands.MODID + ":textures/models/armor/empty_armor_layer_1.png";
    }
}