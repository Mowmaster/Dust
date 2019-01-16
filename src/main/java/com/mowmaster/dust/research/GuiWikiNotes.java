package com.mowmaster.dust.research;

import com.mowmaster.dust.items.ItemRegistry;
import com.mowmaster.dust.references.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;
import java.io.IOException;

public class GuiWikiNotes extends GuiScreen
{
    private final ResourceLocation tomeBackground = new ResourceLocation(Reference.MODID, "textures/guis/research_note_background.png");
    private final ResourceLocation elements1 = new ResourceLocation(Reference.MODID, "textures/items/charcoal/charcoal_green.png");
    private final ResourceLocation elements2 = new ResourceLocation(Reference.MODID, "textures/icons/icons.png");

    private int guiWidth = 180;
    private int guiHeight = 240;

    private String getTitle;
    private int getTitleColor;
    private ResourceLocation image;
    private int backgroundY;
    private int imageX;
    private int imageY;
    private int imageSizeX;
    private int imageSizeY;
    private String getContents;
    private int getContentsColor;
    private int cy;
    private String getAuthor;
    private int getAuthorColor;
    private ItemStack getItemStack;
    private int itemStackX;
    private int itemStackY;
    private float sizeX;
    private float sizeY;

    public GuiWikiNotes(String title, int titleColor, ResourceLocation imageLocation, int backgroundYPos, int imageXStart, int imageYStart, int imageSizeXVal, int imageSizeYVal ,
                        String contents, int contentsColor, int contentsy,String author, int authorColor, ItemStack itemDisplay, int itemPosX, int itemPosY, float itemSizeX, float itemSizeY)
    {
        this.getTitle=title;
        this.getTitleColor=titleColor;
        this.image=imageLocation;
        this.backgroundY=backgroundYPos;
        this.imageX=imageXStart;
        this.imageY=imageYStart;
        this.imageSizeX=imageSizeXVal;
        this.imageSizeY=imageSizeYVal;
        this.getContents=contents;
        this.getContentsColor=contentsColor;
        this.cy=contentsy;
        this.getAuthor=author;
        this.getAuthorColor=authorColor;
        this.getItemStack=itemDisplay;
        this.itemStackX=itemPosX;
        this.itemStackY=itemPosY;
        this.sizeX=itemSizeX;
        this.sizeY=itemSizeY;
    }

    @Override
    public void initGui() {
        //called when gui initializes and when window size changes
    }
    // region

    //Replaced by fontrender.getStringWidth()
    // Capital I = 3 pix
    //lowercase i = 1pix, k=4pix, l=2pix, and t=3pix
    // a space = 3pix, !=1pix, @=6pix, *=4pix, ( or ) & < or > = 4pix, , or . or ; or : =1pix, ~=6pix, ` = 2pix
    // including numbers everything else is 5 pix

    /*
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

     //endregion

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        int centerX = (width/2)-(guiWidth/2);
        int centerY = (height/2)-(guiHeight/2);

        GlStateManager.pushMatrix();
        {
            GlStateManager.enableAlpha();
            GlStateManager.enableBlend();
            GlStateManager.color(1,1,1,1);
            Minecraft.getMinecraft().renderEngine.bindTexture(tomeBackground);
            drawTexturedModalRect(centerX, centerY, 0, 0, guiWidth, guiHeight);


            Minecraft.getMinecraft().renderEngine.bindTexture(image);
            drawTexturedModalRect((centerX+((guiWidth/2))-(imageSizeX/2)),centerY+backgroundY,imageX,imageY,imageSizeX,imageSizeY);

            renderTitle(TextFormatting.BOLD,getTitle,centerX+((guiWidth/2)-(fontRenderer.getStringWidth(getTitle)/2)), centerY+20, getTitleColor,1.5f,1.5f);
            fontRenderer.drawSplitString(getContents,centerX + 20,centerY + cy,(guiWidth-40),getContentsColor);
            fontRenderer.drawSplitString(getAuthor,centerX + 20,centerY + (cy+fontRenderer.getWordWrappedHeight(getContents,(guiWidth-40)))+5,(guiWidth-40),getAuthorColor);

            renderItem(getItemStack,(centerX+itemStackX),centerY+itemStackY,sizeX,sizeY,0f,0f,1f,0f);
            //region

            /*
            drawTexturedModalRect((centerX+((guiWidth/2))-63),centerY+63,1,1,126,20);
             */

            //renderItem(new ItemStack(BlockDustLog.logred),centerX+20,centerY+(guiHeight-20),1.0f,1.0f, 0f,0f,0f,0f);

            /*

            Minecraft.getMinecraft().renderEngine.bindTexture(elements1);
            drawTexturedModalRect((centerX+((guiWidth/2))-8),centerY+50,16,16);

            drawTexturedModalRect((centerX+((guiWidth/2))-8),centerY+75,16,0,16,16);

            Minecraft.getMinecraft().renderEngine.bindTexture(elements2);
            drawTexturedModalRect((centerX+((guiWidth/2))-8),centerY+100,16,0,16,16);


            ItemStack stacki = new ItemStack(ItemRegistry.enchantUpgrade);
            renderItem(stacki,(centerX+((guiWidth/2))-16),centerY+130,2.0f,2.0f,0f,0f,0f,0f);



            fontRenderer.drawSplitString(contentss,centerX + 20,centerY +(guiHeight-(fontRenderer.getWordWrappedHeight(contentss,(guiWidth-40))+20)),(guiWidth-40),0x000000);
            fontRenderer.drawString(String.valueOf(fontRenderer.getStringWidth(TITLE)), centerX+((guiWidth/2)-(fontRenderer.getStringWidth(String.valueOf(fontRenderer.getStringWidth(TITLE)))/2)), centerY+guiHeight-20, 0x000000);

             */
            //endregion
        }
        GlStateManager.popMatrix();

        super.drawScreen(mouseX, mouseY, partialTicks);
    }


    public void renderTitle(@Nullable TextFormatting format, String titleText, int x, int y, int color, float xscale, float yscale) {

        int scaleDiffX = (Math.round(((fontRenderer.getStringWidth(titleText)*xscale) - fontRenderer.getStringWidth(titleText))/2))+(Math.round(xscale*5));
        int scaleDiffY = (Math.round(((fontRenderer.getWordWrappedHeight(titleText,(guiWidth-40))*yscale) - fontRenderer.getWordWrappedHeight(titleText,(guiWidth-40)))/2));

        GlStateManager.pushMatrix();
        GlStateManager.translate(x-scaleDiffX, y-scaleDiffY, 0);
        GlStateManager.scale(xscale,yscale,0.0f);

        fontRenderer.drawString(format + titleText, 0, 0, color);

        GlStateManager.popMatrix();
    }

    public static void renderItem(ItemStack itemStack, int x, int y, float xscale, float yscale, float a, float xr, float yr, float zr) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, 0);
        GlStateManager.rotate(a,xr,yr,zr);
        GlStateManager.scale(xscale,yscale,0.0f);
        GlStateManager.pushAttrib();
        RenderHelper.enableStandardItemLighting();
        //Minecraft.getMinecraft().getRenderItem().shouldRenderItemIn3D(itemStack);
        Minecraft.getMinecraft().getRenderItem().renderItemIntoGUI(itemStack,0,0);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.popAttrib();
        GlStateManager.popMatrix();
    }

    public void drawTexturedModalRect(int x, int y, int width, int height)
    {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos((double)(x + 0), (double)(y + height), (double)this.zLevel).tex(0.0625, 1).endVertex();
        bufferbuilder.pos((double)(x + width), (double)(y + height), (double)this.zLevel).tex(1.0625, 1).endVertex();
        bufferbuilder.pos((double)(x + width), (double)(y + 0), (double)this.zLevel).tex(1.0625, 0).endVertex();
        bufferbuilder.pos((double)(x + 0), (double)(y + 0), (double)this.zLevel).tex(0.0625, 0).endVertex();
        tessellator.draw();
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
