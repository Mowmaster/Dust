package com.mowmaster.dust.research;

import com.mowmaster.dust.blocks.BlockRegistry;
import com.mowmaster.dust.blocks.treebits.BlockDustLog;
import com.mowmaster.dust.enums.WikiNoteTypes;
import com.mowmaster.dust.items.ItemRegistry;
import com.mowmaster.dust.references.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiScreenBook;
import net.minecraft.client.gui.achievement.GuiStats;
import net.minecraft.client.gui.advancements.GuiAdvancement;
import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemWritableBook;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

import java.awt.*;
import java.io.IOException;

public class GuiWikiNotes extends GuiScreen
{
    private final ResourceLocation tomeBackground = new ResourceLocation(Reference.MODID, "textures/guis/research_note_background.png");
    private final ResourceLocation elements1 = new ResourceLocation(Reference.MODID, "textures/items/charcoal/charcoal_green.png");
    private final ResourceLocation elements2 = new ResourceLocation(Reference.MODID, "textures/icons/icons.png");
    //private final ResourceLocation elements3 = new ResourceLocation(Reference.MODID, "textures/gui/elements3.png");

    //private int guiWidth = 175; //Height and width of book tomeBackground in gui_tome.png
    //private int guiHeight = 228;

    private int boxX=256; //To Future Nero: For your sanity, don't change these :D
    private int boxY=256;




    private final ResourceLocation noteBackground = new ResourceLocation(Reference.MODID, "textures/guis/research_note_background.png");
    private final ResourceLocation icon = new ResourceLocation(Reference.MODID, "textures/items/charcoal/charcoal_green.png");
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


    // Capital I = 3 pix
    //lowercase i = 1pix, k=4pix, l=2pix, and t=3pix
    // a space = 3pix, !=1pix, @=6pix, *=4pix, ( or ) & < or > = 4pix, , or . or ; or : =1pix, ~=6pix, ` = 2pix
    // including numbers everything else is 5 pix
    // region

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


    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        //Anything drawn will go here like buttons, labels, tooltips and such
        drawDefaultBackground();
        int centerX = (width/2)-(guiWidth/2);
        int centerY = (height/2)-(guiHeight/2);
        int btnX= ((width/2)+(guiWidth/2));
        //int btnHeight= ((width/2)+(guiWidth/2));
        //TextureAtlasSprite sprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("dust:particles/rose_petals.png");
        RenderItem item = Minecraft.getMinecraft().getRenderItem();

        GlStateManager.pushMatrix();
        {
            GlStateManager.enableAlpha();
            GlStateManager.enableBlend();
            GlStateManager.color(1,1,1,1);
            Minecraft.getMinecraft().renderEngine.bindTexture(tomeBackground);
            drawTexturedModalRect(centerX, centerY, 0, 0, guiWidth, guiHeight);

            Minecraft.getMinecraft().renderEngine.bindTexture(elements1);
            drawTexturedModalRect((centerX+((guiWidth/2))-8),centerY+50,16,16);


            //drawTexturedModalRect((centerX+((guiWidth/2))-8),centerY+75,16,0,16,16);

            Minecraft.getMinecraft().renderEngine.bindTexture(elements2);
            drawTexturedModalRect((centerX+((guiWidth/2))-8),centerY+100,16,0,16,16);


            fontRenderer.drawString(TextFormatting.BOLD + TITLE, centerX+((guiWidth/2)-(fontRenderer.getStringWidth(TITLE)/2)), centerY+20, 0xff0000);
            fontRenderer.drawString(String.valueOf(fontRenderer.getStringWidth(TITLE)), centerX+((guiWidth/2)-(fontRenderer.getStringWidth(String.valueOf(fontRenderer.getStringWidth(TITLE)))/2)), centerY+guiHeight-20, 0x000000);





            ItemStack stackie = new ItemStack(ItemRegistry.crystalWrench);
            renderItem(stackie,(centerX+((guiWidth/2))-16),centerY+70,2.0f,2.0f, -22.5f,0f,1f,0f);

            ItemStack stacki = new ItemStack(ItemRegistry.enchantUpgrade);
            renderItem(stacki,(centerX+((guiWidth/2))-16),centerY+130,2.0f,2.0f,0f,0f,0f,0f);




        }
        GlStateManager.popMatrix();


        /*
        GlStateManager.pushMatrix();
        {
            GlStateManager.enableAlpha();
            GlStateManager.enableBlend();
            GlStateManager.color(1,1,1,1);
            Minecraft.getMinecraft().renderEngine.bindTexture(noteBackground);
            drawTexturedModalRect(centerX, centerY, 0, 0, guiWidth, guiHeight);
            //this.drawHoveringText(TITLE, centerX+((guiWidth/2)-(fontRenderer.getStringWidth(TITLE)/2)), centerY+40); This is like when you hover over an item
            //Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(Reference.MODID, "textures/items/charcoal/charcoal_green.png"));
            //new ResourceLocation(Reference.MODID, "textures/items/charcoal/charcoal_green.png")
            //drawTexturedModalRect(centerX+((guiWidth/2)),centerY+50,sprite, 16, 16);
            Minecraft.getMinecraft().renderEngine.bindTexture(icon);
            drawTexturedModalRect(centerX+((guiWidth/2)),centerY+50, 0, 0, 16, 16);
        }
        GlStateManager.popMatrix();
         */

        super.drawScreen(mouseX, mouseY, partialTicks);
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
