package com.mowmaster.dust.tiles.guis;

import com.mowmaster.dust.references.Reference;
import com.mowmaster.dust.tiles.TileCrystalCluster;
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
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)//contains the text
    {
        String tileName = this.tileCrystalFurnace.getDisplayName().getUnformattedComponentText();
        this.fontRenderer.drawString(tileName,(this.xSize/2 - this.fontRenderer.getStringWidth(tileName) /2),8,4210752);//default binary color
        this.fontRenderer.drawString(this.player.getDisplayName().getUnformattedText(),122,this.ySize - 96 +2,4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)//contains the textures and animations
    {
        GlStateManager.color(1.0f,1.0f,1.0f);//white
        this.mc.getTextureManager().bindTexture(TEXTURES);
        this.drawTexturedModalRect(this.guiLeft,this.guiTop,0,0,this.xSize,this.ySize);

        if(TileCrystalFurnace.isBurning(tileCrystalFurnace))
        {
            int k = this.getBurnLeftScaled(13);//height of fire texture
            this.drawTexturedModalRect(this.guiLeft + 58/*xcord from bottom*/,this.guiTop + 37/*ycord from top*/ + 12-k/*height in pixels to render*/,177/*xcord from bottom of texture side of gui box*/,12-k,14/*width*/,k+1/*height*/);
        }

        int l = this.getCookProgressScaled(24);//lenght of texture from left to right
        this.drawTexturedModalRect(this.guiLeft + 79,this.guiTop + 35,176,14,l+1,16);

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

    private int getCrystalEnergyLeftScaled(int pixels)//how much of the arrow progress image should be left
    {
        int i = this.tileCrystalFurnace.getField(4);//crystal energy left
        int j = 32;//crystal energy max
        return j !=0 && i != 0 ? i * pixels /j : 0;
    }


}
