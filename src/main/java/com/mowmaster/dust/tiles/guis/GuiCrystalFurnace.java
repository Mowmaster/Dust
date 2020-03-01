package com.mowmaster.dust.tiles.guis;

import com.mowmaster.dust.references.Reference;
import com.mowmaster.dust.tiles.TileCrystalFurnace;
import com.mowmaster.dust.tiles.containers.ContainerCrystalFurnace;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiCrystalFurnace extends GuiContainer
{
    private static final ResourceLocation TEXTURES = new ResourceLocation(Reference.MODID + ":textures/guis/crystalfurnace.png");
    private final InventoryPlayer player;
    private final TileCrystalFurnace tileCrystalFurnace;

    public GuiCrystalFurnace(InventoryPlayer player, TileCrystalFurnace tileCrystalFurnace)
    {
        super(new ContainerCrystalFurnace(player,tileCrystalFurnace));
        this.player = player;
        this.tileCrystalFurnace = tileCrystalFurnace;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX,mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)//contains the text
    {
        //String tileName = this.tileCrystalFurnace.getDisplayName().getUnformattedComponentText();
        String tileName = "Crystal";
        String tileName2 = "Furnace";
        this.fontRenderer.drawString(tileName,(this.xSize -47 ),6,16777215);//default binary color
        this.fontRenderer.drawString(tileName2,(this.xSize -50),15,16777215);//default binary color
        this.fontRenderer.drawString(this.player.getDisplayName().getUnformattedText(),4,this.ySize - 93 +2,16777215);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)//contains the textures and animations
    {
        GlStateManager.color(1.0f,1.0f,1.0f);//white
        this.mc.getTextureManager().bindTexture(TEXTURES);
        this.drawTexturedModalRect(this.guiLeft,this.guiTop,0,0,this.xSize,this.ySize);

        if(TileCrystalFurnace.isBurning(tileCrystalFurnace))
        {
            int k = this.getBurnLeftScaled(3);//height of fire texture
            this.drawTexturedModalRect(this.guiLeft + 48/*xcord from bottom*/,this.guiTop + 54/*ycord from top*/ + 3-k/*height in pixels to render*/,175/*xcord from bottom of texture side of gui box*/,3-k,17/*width*/,k+0/*height*/);
        }

        int l = this.getCookProgressScaled(86);//lenght of texture from left to right
        this.drawTexturedModalRect(this.guiLeft + 48,this.guiTop + 46,1,166,l+1,16);

        if(this.tileCrystalFurnace.getField(5)>=0)
        {
            int color2 = this.tileCrystalFurnace.getField(5);
            int colored2 = (color2*19) + 38;
            int n = this.getCookProgressScaled(79);//lenght of texture from left to right
            this.drawTexturedModalRect(this.guiLeft + 41,this.guiTop + 39,175,colored2,n+1,16);

            int m = this.getCrystalEnergyLeftScaled(18);//lenght of texture from left to right
            int color = this.tileCrystalFurnace.getField(5);
            int colored = (color*4) + 5;
            this.drawTexturedModalRect(this.guiLeft + 32,this.guiTop + 34,175,colored,m+1,4);
        }


    }

    private int getBurnLeftScaled(int pixels)//how much of the burning image should be left
    {
        int i = this.tileCrystalFurnace.getField(1);//current burn time
        if(i==0) i=200;
        return this.tileCrystalFurnace.getField(0) * pixels /i;//burn time
    }

    private int getCookProgressScaled(int pixels)//how much of the arrow progress image should be left
    {
        int i = this.tileCrystalFurnace.getField(2);//cook time
        int j = this.tileCrystalFurnace.getField(3);//total cook time
        return j !=0 && i != 0 ? i * pixels /j : 0;
    }

    private int getCrystalEnergyLeftScaled(int pixels)//how much of the crystal progress image should be left
    {
        int i = this.tileCrystalFurnace.getField(4);//crystal energy left
        int j = 32;//crystal energy max
        return j !=0 && i != 0 ? i * pixels /j : 0;
    }


}
