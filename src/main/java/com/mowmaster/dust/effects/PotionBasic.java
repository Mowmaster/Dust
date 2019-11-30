package com.mowmaster.dust.effects;


import com.mowmaster.dust.references.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


public class PotionBasic extends Potion
{
    public PotionBasic(String potionName, String registryName, Boolean harmfulToUndead, int decimalPotionColor, int iconXCord, int iconYCord)
    {
        super(harmfulToUndead,decimalPotionColor);
        this.setRegistryName(new ResourceLocation(Reference.MODID, registryName));
        setPotionName(potionName);
        this.xIcon=iconXCord;
        this.yIcon=iconYCord;
    }

    private ResourceLocation iconTexture = new ResourceLocation(Reference.MODID, "textures/icons/icons.png");
    private int xIcon;
    private int yIcon;

    @SideOnly(Side.CLIENT)
    @Override
    public void renderInventoryEffect(int x, int y, PotionEffect effect, Minecraft mc) {
        if (mc.currentScreen != null) {
            mc.getTextureManager().bindTexture(iconTexture);
            Gui.drawModalRectWithCustomSizedTexture(x + 6, y + 7, xIcon*18, yIcon*18, 18, 18, 288, 288);
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void renderHUDEffect(int x, int y, PotionEffect effect, Minecraft mc, float alpha) {
        mc.getTextureManager().bindTexture(iconTexture);
        Gui.drawModalRectWithCustomSizedTexture(x+3, y+3, xIcon*18, yIcon*18, 18, 18, 288, 288);
    }

    public void effectOnPlayer()
    {

    }

    public void effectOnPedestal()
    {

    }

}
