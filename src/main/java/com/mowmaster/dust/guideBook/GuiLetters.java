package com.mowmaster.dust.guideBook;


import com.mowmaster.dust.references.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

public class GuiLetters extends GuiScreen
{
    private final ResourceLocation tomeBackground = new ResourceLocation(Reference.MODID, "textures/guis/gui_letters_background.png");
    private final ResourceLocation elements1 = new ResourceLocation(Reference.MODID, "textures/guis/gui_letters_background.png.png");
    private final ResourceLocation elements2 = new ResourceLocation(Reference.MODID, "textures/guis/gui_letters_background.png.png");
    //private final ResourceLocation elements3 = new ResourceLocation(Reference.MODID, "textures/gui/elements3.png");

    private int guiWidth = 175; //Height and width of book tomeBackground in gui_tome.png
    private int guiHeight = 228;

    private int glyphBig=100; //Since they're 100x100 I only need one at the moment
    private int iconMiniW=20; //To Future Nero: For your sanity, don't change these :D
    private int iconMiniH=30;

    private int iconBtnW=26;
    private int iconBtnH=37;

    private int curPage=0;
    private static final int bookTotalPages = 1;
    private static ResourceLocation[] bookPageTextures = new ResourceLocation[bookTotalPages];
    private static String[] stringPageText = new String[bookTotalPages];
    private GuiButton buttonDone;

    private GuiButton button1;

    private final int BTNHOME =0;//Button id
    private final int BTNAIR =1;
    private final int BTNEARTH =2;
    private final int BTNWATER =3;
    private final int BTNFIRE =4;
    private final int BTNLIGHT =5;
    private final int BTNDARK =6;

    private int varSwitch=0;
    private int varU=0;
    private int varV=0;
    private ResourceLocation varLoc;

    @Override
    public void initGui() {
        //called when gui initializes and when window size changes

        int centerY = (height/2)-(guiHeight/2);
        int btnX= ((width/2)+(guiWidth/2));

        buttonList.clear();//Clear just in case it's not empty
        //buttonList.add(button1=new GuiButton(BTNHOME, 0,0,100, 20, "Button"));
        buttonList.add(button1=new GuiButtons(BTNAIR, btnX,centerY));
        buttonList.add(button1=new GuiButtons(BTNEARTH, btnX,centerY+iconBtnH));
        buttonList.add(button1=new GuiButtons(BTNWATER, btnX,centerY+iconBtnH*2));
        buttonList.add(button1=new GuiButtons(BTNFIRE, btnX,centerY+iconBtnH*3));
        buttonList.add(button1=new GuiButtons(BTNDARK, btnX,centerY+iconBtnH*4));
        buttonList.add(button1=new GuiButtons(BTNLIGHT, btnX,centerY+iconBtnH*5));
    }

    /*@Override
    public void updateScreen() {
        //buttonDone.visible = (curPage == bookTotalPages-1);
    }*/

    //@Override
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
            Minecraft.getMinecraft().renderEngine.bindTexture(tomeBackground);
            drawTexturedModalRect(centerX, centerY, 0, 0, guiWidth, guiHeight);
            for(int i=0; i<6; i++){
                drawTexturedModalRect(btnX, centerY+iconBtnH * i, 175, 35, iconBtnW, iconBtnH);
            }
            Minecraft.getMinecraft().renderEngine.bindTexture(elements1); //Mini icons on elements1.png
            for (int i = 0; i < 6; i++) {
                drawTexturedModalRect(btnX + 3, (centerY + 4) + (37 * i), 21 * i, 210, iconMiniW, iconMiniH);
            }
        }
        GlStateManager.popMatrix();

        String greater = "Contents";

        if(varSwitch==0)
        {
            fontRenderer.drawString(greater, (width/2)-greater.length()-25, centerY+20, 0x000000);
        }
        else
        {
            GlStateManager.pushMatrix();
            {
                Minecraft.getMinecraft().renderEngine.bindTexture(varLoc);
                drawModalRectWithCustomSizedTexture(centerX+13, height/2-80,varU, varV, 150, 150,400,400);
            }
            GlStateManager.popMatrix();
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        //check when a button is clicked
        switch (button.id){
            case BTNHOME:
                Minecraft.getMinecraft().displayGuiScreen(new GuiIndex());
                break;
            case BTNAIR:
                Minecraft.getMinecraft().displayGuiScreen(new GuiIndex());
                break;
            case BTNEARTH:
                drawIcon(elements1,160,0);
                break;
            case BTNWATER:
                drawIcon(elements1,0,160);
                break;
            case BTNFIRE:
                drawIcon(elements1,160,160);
                break;
            case BTNDARK:
                drawIcon(elements2,0,0);
                break;
            case BTNLIGHT:
                drawIcon(elements2,160,0);
                break;
            default:
                break;
        }
        updateButtons();
        super.actionPerformed(button);
    }

    private void updateButtons(){

    }


    private void drawIcon(ResourceLocation loc, int x, int y){
        varSwitch=1;
        varU=x;
        varV=y;
        varLoc=loc;

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
