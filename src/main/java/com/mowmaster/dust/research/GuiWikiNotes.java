package com.mowmaster.dust.research;

import com.mowmaster.dust.enums.WikiNoteTypes;
import com.mowmaster.dust.references.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.io.IOException;

public class GuiWikiNotes extends GuiScreen
{
    private final ResourceLocation noteBackground = new ResourceLocation(Reference.MODID, "textures/guis/research_note_background.png");
    private int guiWidth = 180;
    private int guiHeight = 240;
    String TITLE = "Dust Spells";
    String contentss = "By throwing dust on the ground and lighting it with flint and steel one can activate the dormate power in the dust to create effects, " +
            "start off by throwing 5 or more of one type of dust, youll see that it results in a specific effect. " +
            "When you find more dust try mixing colors together and see what sort of combinations you can get! " +
            "Also do note, White Dust improves the potency of positive effects like Strength, Black Dust improves negative effects like Drowning";

    public GuiWikiNotes(WikiNoteTypes types)
    {

    }

    @Override
    public void initGui() {
        //called when gui initializes and when window size changes
    }



    //Replaced by fontrender.getStringWidth()
    /*

    // Capital I = 3 pix
    //lowercase i = 1pix, k=4pix, l=2pix, and t=3pix
    // a space = 3pix, !=1pix, @=6pix, *=4pix, ( or ) & < or > = 4pix, , or . or ; or : =1pix, ~=6pix, ` = 2pix
    // including numbers everything else is 5 pix
    public int getTextPixCount(String text)
    {
        int count = text.length();
        int pixCount = 0;
        for(int i=0;i<text.length();i++)
        {
            Character character = text.charAt(i);

            switch (character)
            {
                case 'I':
                    pixCount +=4;
                    break;
                case 'i':
                    pixCount +=2;
                    break;
                case 'k':
                    pixCount +=5;
                    break;
                case 'l':
                    pixCount +=3;
                    break;
                case 't':
                    pixCount +=4;
                    break;
                case ' ':
                    pixCount +=4;
                    break;
                case '!':
                    pixCount +=2;
                    break;
                case '@':
                    pixCount +=7;
                    break;
                case '*':
                    pixCount +=5;
                    break;
                case '(':
                    pixCount +=5;
                    break;
                case ')':
                    pixCount +=5;
                    break;
                case '<':
                    pixCount +=5;
                    break;
                case '>':
                    pixCount +=5;
                    break;
                case ',':
                    pixCount +=2;
                    break;
                case '.':
                    pixCount +=2;
                    break;
                case ':':
                    pixCount +=2;
                    break;
                case ';':
                    pixCount +=2;
                    break;
                case '~':
                    pixCount +=7;
                    break;
                case '`':
                    pixCount +=3;
                    break;
                default:
                    pixCount+=6;

            }
        }

        return pixCount-1;
    }
     */

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
            fontRenderer.drawString(TITLE, centerX+((guiWidth/2)-(fontRenderer.getStringWidth(TITLE)/2)), centerY+20, 0x000000);
            fontRenderer.drawString(String.valueOf(fontRenderer.getStringWidth(TITLE)), centerX+((guiWidth/2)-(fontRenderer.getStringWidth(String.valueOf(fontRenderer.getStringWidth(TITLE)))/2)), centerY+guiHeight-20, 0x000000);
            //fontRenderer.drawString(contentss, (width/2)-contentss.length(), (height/2)-contentss.length(), 0x000000);

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
        return true;
    }

    @Override
    public void onGuiClosed() {
        //when you close GUI
        super.onGuiClosed();
    }
}
