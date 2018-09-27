package com.mowmaster.dust.guideBook;

import com.mowmaster.dust.references.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiButtons extends GuiButton
{
    int posX=175;
    int posY=35;

    private final ResourceLocation tomeBackground = new ResourceLocation(Reference.MODID, "textures/gui/gui_tome.png");
    public GuiButtons(int buttonId, int x, int y) {
        super(buttonId, x,y, 26,37, ""); //Image, no text needed
    }


    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if(visible){

            GlStateManager.pushMatrix();
            {
                GlStateManager.enableAlpha();
                GlStateManager.enableBlend();
                GlStateManager.color(1,1,1,0);
                mc.renderEngine.bindTexture(tomeBackground);
                drawTexturedModalRect(x, y, posX, posY, width, height);
            }
            GlStateManager.popMatrix();

        }
        //super.drawButton(mc, mouseX, mouseY, partialTicks);
    }
}
