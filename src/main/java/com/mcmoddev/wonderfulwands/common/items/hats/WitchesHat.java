package com.mcmoddev.wonderfulwands.common.items.hats;

import com.mcmoddev.wonderfulwands.client.hats.WitchHatRenderer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class WitchesHat extends WizardsHat {

    public WitchesHat() {
        super();
    }

    @SideOnly(Side.CLIENT)
    public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, ModelBiped biped) {
        return new WitchHatRenderer();
    }
}