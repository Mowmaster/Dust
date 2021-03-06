package com.mowmaster.dust.effects;

import com.mowmaster.dust.references.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


public class PotionEnvigoration extends Potion
{
    public PotionEnvigoration(String potionName, String registryName)
    {
        super(false,15667711);
        this.setRegistryName(new ResourceLocation(Reference.MODID, registryName));
        setPotionName(potionName);
    }

    private ResourceLocation iconTexture = new ResourceLocation(Reference.MODID, "textures/icons/icons.png");


    /**
     * Called to draw the this Potion onto the player's inventory when it's active.
     * This can be used to e.g. render Potion icons from your own texture.
     *
     * @param x      the x coordinate
     * @param y      the y coordinate
     * @param effect the active PotionEffect
     * @param mc     the Minecraft instance, for convenience
     */

    private int xIcon=2;
    private int yIcon=0;
    @SideOnly(Side.CLIENT)
    @Override
    public void renderInventoryEffect(int x, int y, PotionEffect effect, Minecraft mc) {
        if (mc.currentScreen != null) {
            mc.getTextureManager().bindTexture(iconTexture);
            Gui.drawModalRectWithCustomSizedTexture(x + 6, y + 7, xIcon*18, yIcon*18, 18, 18, 288, 288);
        }
    }

    /**
     * Called to draw the this Potion onto the player's ingame HUD when it's active.
     * This can be used to e.g. render Potion icons from your own texture.
     *
     * @param x      the x coordinate
     * @param y      the y coordinate
     * @param effect the active PotionEffect
     * @param mc     the Minecraft instance, for convenience
     * @param alpha  the alpha value, blinks when the potion is about to run out
     */
    @SideOnly(Side.CLIENT)
    @Override
    public void renderHUDEffect(int x, int y, PotionEffect effect, Minecraft mc, float alpha) {
        mc.getTextureManager().bindTexture(iconTexture);
        Gui.drawModalRectWithCustomSizedTexture(x+3, y+3, xIcon*18, yIcon*18, 18, 18, 288, 288);
        //public static void drawModalRectWithCustomSizedTexture(int x, int y, float u, float v, int width, int height, float textureWidth, float textureHeight)
    }

}
