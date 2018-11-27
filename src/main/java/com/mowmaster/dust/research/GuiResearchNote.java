package com.mowmaster.dust.research;

import com.mowmaster.dust.references.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

public class GuiResearchNote extends GuiScreen
{
    private final ResourceLocation noteBackground = new ResourceLocation(Reference.MODID, "textures/guis/research_note_background.png");
    private int guiWidth = 180;
    private int guiHeight = 240;
    private String contents;
    private String[] scrolls;

    public GuiResearchNote(String getText, String[] getScrolls)
    {
        this.contents=getText;
        this.scrolls=getScrolls;
    }

    @Override
    public void initGui() {
        //called when gui initializes and when window size changes
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        //Anything drawn will go here like buttons, labels, tooltips and such
        drawDefaultBackground();
        int centerX = (width/2)-(guiWidth/2);
        int centerY = (height/2)-(guiHeight/2);
        int btnX= ((width/2)+(guiWidth/2));
        //int btnHeight= ((width/2)+(guiWidth/2));

        GlStateManager.pushMatrix();
        {
            GlStateManager.enableAlpha();
            GlStateManager.enableBlend();
            GlStateManager.color(1,1,1,1);
            Minecraft.getMinecraft().renderEngine.bindTexture(noteBackground);
            drawTexturedModalRect(centerX, centerY, 0, 0, guiWidth, guiHeight);
            fontRenderer.drawString(contents, (width/2)-contents.length(), (height/2)-contents.length(), 0x000000);

            /*
            for(int i=0;i<scrolls.length;i++)
            {
                fontRenderer.drawString(scrolls[i], (width/2)-scrolls[i].length()+(i*5), (height/2)-scrolls[i].length()-10, 0x000000);
            }
             */

        }
        GlStateManager.popMatrix();

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        updateButtons();
        super.actionPerformed(button);
    }

    private void updateButtons(){

    }


    private void drawIcon(ResourceLocation loc, int x, int y){
        drawScreen(0,0,0);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        //detect keypresses
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public boolean doesGuiPauseGame() {
        //will game be paused in single player?
        return false;
    }

    @Override
    public void onGuiClosed() {
        //when you close GUI
        super.onGuiClosed();
    }
}
