package com.mcmoddev.wonderfulwands.common.items.hats;

import com.mcmoddev.wonderfulwands.WonderfulWands;
import com.mcmoddev.wonderfulwands.client.hats.TopHatRenderer;
import com.mcmoddev.wonderfulwands.client.hats.WizardHatRenderer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collection;
import java.util.List;

/**
 * Wizards and Witches hats are expensive head-slot items that are rendered in
 * 3D. A new hat can be combined with a potion to make the potion effect
 * permanent (wearing the hat continuously reapplies the potion effect).
 *
 * @author cybergnome
 */
public class WizardsHat extends ItemArmor {

    private static final int potionApplyInterval = 19;
    private static final int potionDuration = 11 * 20;

    public WizardsHat() {
        super(WonderfulWands.NONARMOR, 0, EntityEquipmentSlot.HEAD);
        setMaxDamage(1);
        setHasSubtypes(true);
    }

    @Override
    public boolean isDamageable() {
        return false;
    }

    public void setPotionEffectID(ItemStack stack, String potionEffectCode) {
        NBTTagCompound tag;
        if (!stack.hasTagCompound()) {
            tag = new NBTTagCompound();
        } else {
            tag = stack.getTagCompound();
        }

        tag.setString("EffectID", potionEffectCode);
        stack.setTagCompound(tag);
    }

    public String getPotionEffectID(ItemStack stack) {
        if (!stack.hasTagCompound()) {
            return null;
        }

        NBTTagCompound tag = stack.getTagCompound();
        if (tag.hasKey("EffectID")) {
            return tag.getString("EffectID");
        } else {
            return null;
        }
    }

    public void setPotionEffectLevel(ItemStack stack, int level) {
        NBTTagCompound tag;
        if (!stack.hasTagCompound()) {
            tag = new NBTTagCompound();
        } else {
            tag = stack.getTagCompound();
        }

        tag.setByte("EffectLvl", (byte) level);
        stack.setTagCompound(tag);
    }

    public int getPotionEffectLevel(ItemStack stack) {
        if (!stack.hasTagCompound()) {
            return 0;
        }

        NBTTagCompound tag = stack.getTagCompound();
        if (tag.hasKey("EffectLvl")) {
            return tag.getByte("EffectLvl");
        } else {
            return 0;
        }
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
        return WonderfulWands.MODID + ":textures/models/armor/empty_armor_layer_1.png";
    }

    @SideOnly(Side.CLIENT)
    public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, ModelBiped biped) {
        return new WizardHatRenderer();
    }

    /**
     * Return whether this item is repairable in an anvil.
     */
    @Override
    public boolean getIsRepairable(ItemStack srcItemStack, ItemStack rawMaterial) {
        // repair with string or wool
        return false;
    }

    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack src) {
        super.onArmorTick(world, player, src);
        if (world.getTotalWorldTime() % (potionApplyInterval) == 0) {
            if (this.getPotionEffectID(src) == null) {
                if (!player.getActivePotionEffects().isEmpty()) {
                    // soak up a potion effect
                    Collection<PotionEffect> c = player.getActivePotionEffects();
                    PotionEffect[] effect = c.toArray(new PotionEffect[c.size()]);
                    int i = world.rand.nextInt(effect.length);
                    String potionCode = effect[i].getPotion().getRegistryName().toString();
                    this.setPotionEffectID(src, potionCode);
                    this.setPotionEffectLevel(src, effect[i].getAmplifier());
                    player.removePotionEffect(effect[i].getPotion());
                }
            } else {
                Potion pot = Potion.getPotionFromResourceLocation(this.getPotionEffectID(src));
                if (pot == null) return;
                int level = this.getPotionEffectLevel(src);
                player.addPotionEffect(new PotionEffect(pot, potionDuration, level, false, false));
            }
        }
    }

    @Override
    public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
        super.addInformation(stack, world, tooltip, flag);
        String potionID = this.getPotionEffectID(stack);
        if (potionID != null && Potion.getPotionFromResourceLocation(potionID) != null) {
            tooltip.add(I18n.translateToLocal(Potion.getPotionFromResourceLocation(potionID).getName()));
        }
    }
}