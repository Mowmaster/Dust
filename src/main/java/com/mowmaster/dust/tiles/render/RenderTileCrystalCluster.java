package com.mowmaster.dust.tiles.render;

import com.mowmaster.dust.items.ItemRegistry;
import com.mowmaster.dust.tiles.TileCrystalCluster;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

import static net.minecraft.block.BlockDirectional.FACING;


public class RenderTileCrystalCluster extends TileEntitySpecialRenderer<TileCrystalCluster>
{

    @Override
    public void render(TileCrystalCluster te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {

        IBlockState state = te.getWorld().getBlockState(te.getPos());
        EnumFacing enumfacing = (EnumFacing)state.getValue(FACING);
        RenderItem itemRenderer = Minecraft.getMinecraft().getRenderItem();
        ItemStack crystal1 = new ItemStack(ItemRegistry.crystal,1,te.getCrystalFromList(1));
        ItemStack crystal2 = new ItemStack(ItemRegistry.crystal,1,te.getCrystalFromList(2));
        ItemStack crystal3 = new ItemStack(ItemRegistry.crystal,1,te.getCrystalFromList(3));
        ItemStack crystal4 = new ItemStack(ItemRegistry.crystal,1,te.getCrystalFromList(4));
        ItemStack crystal5 = new ItemStack(ItemRegistry.crystal,1,te.getCrystalFromList(5));
        ItemStack crystal6 = new ItemStack(ItemRegistry.crystal,1,te.getCrystalFromList(6));
        ItemStack crystal7 = new ItemStack(ItemRegistry.crystal,1,te.getCrystalFromList(7));
        ItemStack crystal8 = new ItemStack(ItemRegistry.crystal,1,te.getCrystalFromList(8));
        ItemStack crystal9 = new ItemStack(ItemRegistry.crystal,1,te.getCrystalFromList(9));


        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);

        if(enumfacing==EnumFacing.UP)//when placed on ground
        {
            if(te.crystalCount==1) {renderItem(itemRenderer, crystal1 , 0.5f, 0.45f, 0.5f, 0f, 0f, 0f, 0f);}

            if(te.crystalCount==2) {renderItem(itemRenderer, crystal1, 0.5f, 0.45f, 0.65f,  11.25f , 1.0f, 0f, 0f);
                                    renderItem(itemRenderer, crystal2, 0.5f, 0.45f, 0.35f, 348.75f, 1.0f, 0f, 0f);}

            if(te.crystalCount==3) {renderItem(itemRenderer, crystal1, 0.5f, 0.45f, 0.65f,  11.25f , 1.0f, 0f, 0f);
                                    renderItem(itemRenderer, crystal2, 0.5f, 0.45f, 0.35f, 348.75f, 1.0f, 0f, 0f);
                                    renderItem(itemRenderer, crystal3, 0.5f, 0.45f, 0.5f, 0f, 0f, 0f, 0f);}

            if(te.crystalCount==4) {renderItem(itemRenderer, crystal1, 0.5f, 0.45f, 0.65f,  11.25f , 1.0f, 0f, 0f);
                                    renderItem(itemRenderer, crystal2, 0.5f, 0.45f, 0.35f, 348.75f, 1.0f, 0f, 0f);
                                    renderItem(itemRenderer, crystal3, 0.35f, 0.45f, 0.5f,  11.25f , 0f, 0f, 1.0f);
                                    renderItem(itemRenderer, crystal4, 0.65f, 0.45f, 0.5f, 348.75f, 0f, 0f, 1.0f);}

            if(te.crystalCount==5) {renderItem(itemRenderer, crystal1, 0.5f, 0.45f, 0.65f,  11.25f , 1.0f, 0f, 0f);
                                    renderItem(itemRenderer, crystal2, 0.5f, 0.45f, 0.35f, 348.75f, 1.0f, 0f, 0f);
                                    renderItem(itemRenderer, crystal3, 0.35f, 0.45f, 0.5f,  11.25f , 0f, 0f, 1.0f);
                                    renderItem(itemRenderer, crystal4, 0.65f, 0.45f, 0.5f, 348.75f, 0f, 0f, 1.0f);
                                    renderItem(itemRenderer, crystal5, 0.5f, 0.45f, 0.5f, 0f, 0f, 0f, 0f);}

            if(te.crystalCount==6) {renderItem(itemRenderer, crystal1, 0.5f, 0.45f, 0.65f,  11.25f , 1.0f, 0f, 0f);
                                    renderItem(itemRenderer, crystal2, 0.5f, 0.45f, 0.35f, 348.75f, 1.0f, 0f, 0f);
                                    renderItem(itemRenderer, crystal3, 0.35f, 0.45f, 0.5f,  11.25f , 0f, 0f, 1.0f);
                                    renderItem(itemRenderer, crystal4, 0.65f, 0.45f, 0.5f, 348.75f, 0f, 0f, 1.0f);
                                    renderItem(itemRenderer, crystal5, 0.35f, 0.45f, 0.65f, 11.25f , 1.0f, 0f, 1.0f);
                                    renderItem(itemRenderer, crystal6, 0.65f, 0.45f, 0.35f, 348.75f, 1.0f, 0f, 1.0f);}

            if(te.crystalCount==7) {renderItem(itemRenderer, crystal1, 0.5f, 0.45f, 0.65f,  11.25f , 1.0f, 0f, 0f);
                                    renderItem(itemRenderer, crystal2, 0.5f, 0.45f, 0.35f, 348.75f, 1.0f, 0f, 0f);
                                    renderItem(itemRenderer, crystal3, 0.35f, 0.45f, 0.5f,  11.25f , 0f, 0f, 1.0f);
                                    renderItem(itemRenderer, crystal4, 0.65f, 0.45f, 0.5f, 348.75f, 0f, 0f, 1.0f);
                                    renderItem(itemRenderer, crystal5, 0.35f, 0.45f, 0.65f, 11.25f , 1.0f, 0f, 1.0f);
                                    renderItem(itemRenderer, crystal6, 0.65f, 0.45f, 0.35f, 348.75f, 1.0f, 0f, 1.0f);
                                    renderItem(itemRenderer, crystal7, 0.5f, 0.45f, 0.5f, 0f, 0f, 0f, 0f);}
            if(te.crystalCount==8) {renderItem(itemRenderer, crystal1, 0.5f, 0.45f, 0.65f,  11.25f , 1.0f, 0f, 0f);
                                    renderItem(itemRenderer, crystal2, 0.5f, 0.45f, 0.35f, 348.75f, 1.0f, 0f, 0f);
                                    renderItem(itemRenderer, crystal3, 0.35f, 0.45f, 0.5f,  11.25f , 0f, 0f, 1.0f);
                                    renderItem(itemRenderer, crystal4, 0.65f, 0.45f, 0.5f, 348.75f, 0f, 0f, 1.0f);
                                    renderItem(itemRenderer, crystal5, 0.35f, 0.45f, 0.65f, 11.25f , 1.0f, 0f, 1.0f);
                                    renderItem(itemRenderer, crystal6, 0.65f, 0.45f, 0.35f, 348.75f, 1.0f, 0f, 1.0f);
                                    renderItem(itemRenderer, crystal7, 0.35f, 0.45f, 0.35f, 348.75f , 1.0f, 0f, -1.0f);
                                    renderItem(itemRenderer, crystal8, 0.65f, 0.45f, 0.65f, 11.25f, 1.0f, 0f, -1.0f);}
            if(te.crystalCount==9) {renderItem(itemRenderer, crystal1, 0.5f, 0.45f, 0.65f,  11.25f , 1.0f, 0f, 0f);
                                    renderItem(itemRenderer, crystal2, 0.5f, 0.45f, 0.35f, 348.75f, 1.0f, 0f, 0f);
                                    renderItem(itemRenderer, crystal3, 0.35f, 0.45f, 0.5f,  11.25f , 0f, 0f, 1.0f);
                                    renderItem(itemRenderer, crystal4, 0.65f, 0.45f, 0.5f, 348.75f, 0f, 0f, 1.0f);
                                    renderItem(itemRenderer, crystal5, 0.35f, 0.45f, 0.65f, 11.25f , 1.0f, 0f, 1.0f);
                                    renderItem(itemRenderer, crystal6, 0.65f, 0.45f, 0.35f, 348.75f, 1.0f, 0f, 1.0f);
                                    renderItem(itemRenderer, crystal7, 0.35f, 0.45f, 0.35f, 348.75f , 1.0f, 0f, -1.0f);
                                    renderItem(itemRenderer, crystal8, 0.65f, 0.45f, 0.65f, 11.25f, 1.0f, 0f, -1.0f);
                                    renderItem(itemRenderer, crystal9, 0.5f, 0.45f, 0.5f, 0f, 0f, 0f, 0f);}
        }
        if(enumfacing==EnumFacing.DOWN)
        {
            if(te.crystalCount==1) {renderItem(itemRenderer, crystal1 , 0.5f, 0.55f, 0.5f, 0f, 0f, 0f, 0f);}

            if(te.crystalCount==2) {renderItem(itemRenderer, crystal1, 0.5f, 0.55f, 0.65f,  348.75f , 1.0f, 0f, 0f);
                                    renderItem(itemRenderer, crystal2, 0.5f, 0.55f, 0.35f, 11.25f, 1.0f, 0f, 0f);}

            if(te.crystalCount==3) {renderItem(itemRenderer, crystal1, 0.5f, 0.55f, 0.65f,  348.75f , 1.0f, 0f, 0f);
                                    renderItem(itemRenderer, crystal2, 0.5f, 0.55f, 0.35f, 11.25f, 1.0f, 0f, 0f);
                                    renderItem(itemRenderer, crystal3, 0.5f, 0.55f, 0.5f, 0f, 0f, 0f, 0f);}

            if(te.crystalCount==4) {renderItem(itemRenderer, crystal1, 0.5f, 0.55f, 0.65f,  348.75f , 1.0f, 0f, 0f);
                                    renderItem(itemRenderer, crystal2, 0.5f, 0.55f, 0.35f, 11.25f, 1.0f, 0f, 0f);
                                    renderItem(itemRenderer, crystal3, 0.35f, 0.55f, 0.5f,  348.75f , 0f, 0f, 1.0f);
                                    renderItem(itemRenderer, crystal4, 0.65f, 0.55f, 0.5f, 11.25f, 0f, 0f, 1.0f);}

            if(te.crystalCount==5) {renderItem(itemRenderer, crystal1, 0.5f, 0.55f, 0.65f,  348.75f , 1.0f, 0f, 0f);
                                    renderItem(itemRenderer, crystal2, 0.5f, 0.55f, 0.35f, 11.25f, 1.0f, 0f, 0f);
                                    renderItem(itemRenderer, crystal3, 0.35f, 0.55f, 0.5f,  348.75f , 0f, 0f, 1.0f);
                                    renderItem(itemRenderer, crystal4, 0.65f, 0.55f, 0.5f, 11.25f, 0f, 0f, 1.0f);
                                    renderItem(itemRenderer, crystal5, 0.5f, 0.55f, 0.5f, 0f, 0f, 0f, 0f);}

            if(te.crystalCount==6) {renderItem(itemRenderer, crystal1, 0.5f, 0.55f, 0.65f,  348.75f , 1.0f, 0f, 0f);
                                    renderItem(itemRenderer, crystal2, 0.5f, 0.55f, 0.35f, 11.25f, 1.0f, 0f, 0f);
                                    renderItem(itemRenderer, crystal3, 0.35f, 0.55f, 0.5f,  348.75f , 0f, 0f, 1.0f);
                                    renderItem(itemRenderer, crystal4, 0.65f, 0.55f, 0.5f, 11.25f, 0f, 0f, 1.0f);
                                    renderItem(itemRenderer, crystal5, 0.35f, 0.55f, 0.65f, 348.75f , 1.0f, 0f, 1.0f);
                                    renderItem(itemRenderer, crystal6, 0.65f, 0.55f, 0.35f, 11.25f, 1.0f, 0f, 1.0f);}

            if(te.crystalCount==7) {renderItem(itemRenderer, crystal1, 0.5f, 0.55f, 0.65f,  348.75f , 1.0f, 0f, 0f);
                                    renderItem(itemRenderer, crystal2, 0.5f, 0.55f, 0.35f, 11.25f, 1.0f, 0f, 0f);
                                    renderItem(itemRenderer, crystal3, 0.35f, 0.55f, 0.5f,  348.75f , 0f, 0f, 1.0f);
                                    renderItem(itemRenderer, crystal4, 0.65f, 0.55f, 0.5f, 11.25f, 0f, 0f, 1.0f);
                                    renderItem(itemRenderer, crystal5, 0.35f, 0.55f, 0.65f, 348.75f , 1.0f, 0f, 1.0f);
                                    renderItem(itemRenderer, crystal6, 0.65f, 0.55f, 0.35f, 11.25f, 1.0f, 0f, 1.0f);
                                    renderItem(itemRenderer, crystal7, 0.5f, 0.55f, 0.5f, 0f, 0f, 0f, 0f);}
            if(te.crystalCount==8) {renderItem(itemRenderer, crystal1, 0.5f, 0.55f, 0.65f,  348.75f , 1.0f, 0f, 0f);
                                    renderItem(itemRenderer, crystal2, 0.5f, 0.55f, 0.35f, 11.25f, 1.0f, 0f, 0f);
                                    renderItem(itemRenderer, crystal3, 0.35f, 0.55f, 0.5f,  348.75f , 0f, 0f, 1.0f);
                                    renderItem(itemRenderer, crystal4, 0.65f, 0.55f, 0.5f, 11.25f, 0f, 0f, 1.0f);
                                    renderItem(itemRenderer, crystal5, 0.35f, 0.55f, 0.65f, 348.75f , 1.0f, 0f, 1.0f);
                                    renderItem(itemRenderer, crystal6, 0.65f, 0.55f, 0.35f, 11.25f, 1.0f, 0f, 1.0f);
                                    renderItem(itemRenderer, crystal7, 0.35f, 0.55f, 0.35f, 11.25f , 1.0f, 0f, -1.0f);
                                    renderItem(itemRenderer, crystal8, 0.65f, 0.55f, 0.65f, 348.75f, 1.0f, 0f, -1.0f);}
            if(te.crystalCount==9) {renderItem(itemRenderer, crystal1, 0.5f, 0.55f, 0.65f,  348.75f , 1.0f, 0f, 0f);
                                    renderItem(itemRenderer, crystal2, 0.5f, 0.55f, 0.35f, 11.25f, 1.0f, 0f, 0f);
                                    renderItem(itemRenderer, crystal3, 0.35f, 0.55f, 0.5f,  348.75f , 0f, 0f, 1.0f);
                                    renderItem(itemRenderer, crystal4, 0.65f, 0.55f, 0.5f, 11.25f, 0f, 0f, 1.0f);
                                    renderItem(itemRenderer, crystal5, 0.35f, 0.55f, 0.65f, 348.75f , 1.0f, 0f, 1.0f);
                                    renderItem(itemRenderer, crystal6, 0.65f, 0.55f, 0.35f, 11.25f, 1.0f, 0f, 1.0f);
                                    renderItem(itemRenderer, crystal7, 0.35f, 0.55f, 0.35f, 11.25f , 1.0f, 0f, -1.0f);
                                    renderItem(itemRenderer, crystal8, 0.65f, 0.55f, 0.65f, 348.75f, 1.0f, 0f, -1.0f);
                                    renderItem(itemRenderer, crystal9, 0.5f, 0.55f, 0.5f, 0f, 0f, 0f, 0f);}
        }
        if(enumfacing==EnumFacing.NORTH)
        {
            if(te.crystalCount==1) {renderItem(itemRenderer, crystal1, 0.5f, 0.5f, 0.55f, 90.0f, 1.0f, 0f, 0f);}
            if(te.crystalCount==2) {renderItem(itemRenderer, crystal1, 0.5f, 0.35f, 0.55f, 78.75f, 1.0f, 0f, 0f);
                                    renderItem(itemRenderer, crystal2, 0.5f, 0.65f, 0.55f, 101.25f, 1.0f, 0f, 0f);}
            if(te.crystalCount==3) {renderItem(itemRenderer, crystal1, 0.5f, 0.35f, 0.55f, 78.75f, 1.0f, 0f, 0f);
                                    renderItem(itemRenderer, crystal2, 0.5f, 0.65f, 0.55f, 101.25f, 1.0f, 0f, 0f);
                                    renderItem(itemRenderer, crystal3, 0.5f, 0.5f, 0.55f, 90.0f, 1.0f, 0f, 0f);}

            if(te.crystalCount==1) {renderItem(itemRenderer, crystal1 , 0.5f, 0.55f, 0.5f, 0f, 0f, 0f, 0f);}

            if(te.crystalCount==2) {renderItem(itemRenderer, crystal1, 0.5f, 0.55f, 0.65f,  348.75f , 1.0f, 0f, 0f);
                                    renderItem(itemRenderer, crystal2, 0.5f, 0.55f, 0.35f, 11.25f, 1.0f, 0f, 0f);}

            if(te.crystalCount==3) {renderItem(itemRenderer, crystal1, 0.5f, 0.55f, 0.65f,  348.75f , 1.0f, 0f, 0f);
                                    renderItem(itemRenderer, crystal2, 0.5f, 0.55f, 0.35f, 11.25f, 1.0f, 0f, 0f);
                                    renderItem(itemRenderer, crystal3, 0.5f, 0.55f, 0.5f, 0f, 0f, 0f, 0f);}

            if(te.crystalCount==4) {renderItem(itemRenderer, crystal1, 0.5f, 0.55f, 0.65f,  348.75f , 1.0f, 0f, 0f);
                                    renderItem(itemRenderer, crystal2, 0.5f, 0.55f, 0.35f, 11.25f, 1.0f, 0f, 0f);
                                    renderItem(itemRenderer, crystal3, 0.35f, 0.55f, 0.5f,  348.75f , 0f, 0f, 1.0f);
                                    renderItem(itemRenderer, crystal4, 0.65f, 0.55f, 0.5f, 11.25f, 0f, 0f, 1.0f);}

            if(te.crystalCount==5) {renderItem(itemRenderer, crystal1, 0.5f, 0.55f, 0.65f,  348.75f , 1.0f, 0f, 0f);
                                    renderItem(itemRenderer, crystal2, 0.5f, 0.55f, 0.35f, 11.25f, 1.0f, 0f, 0f);
                                    renderItem(itemRenderer, crystal3, 0.35f, 0.55f, 0.5f,  348.75f , 0f, 0f, 1.0f);
                                    renderItem(itemRenderer, crystal4, 0.65f, 0.55f, 0.5f, 11.25f, 0f, 0f, 1.0f);
                                    renderItem(itemRenderer, crystal5, 0.5f, 0.55f, 0.5f, 0f, 0f, 0f, 0f);}

            if(te.crystalCount==6) {renderItem(itemRenderer, crystal1, 0.5f, 0.55f, 0.65f,  348.75f , 1.0f, 0f, 0f);
                                    renderItem(itemRenderer, crystal2, 0.5f, 0.55f, 0.35f, 11.25f, 1.0f, 0f, 0f);
                                    renderItem(itemRenderer, crystal3, 0.35f, 0.55f, 0.5f,  348.75f , 0f, 0f, 1.0f);
                                    renderItem(itemRenderer, crystal4, 0.65f, 0.55f, 0.5f, 11.25f, 0f, 0f, 1.0f);
                                    renderItem(itemRenderer, crystal5, 0.35f, 0.55f, 0.65f, 348.75f , 1.0f, 0f, 1.0f);
                                    renderItem(itemRenderer, crystal6, 0.65f, 0.55f, 0.35f, 11.25f, 1.0f, 0f, 1.0f);}

            if(te.crystalCount==7) {renderItem(itemRenderer, crystal1, 0.5f, 0.55f, 0.65f,  348.75f , 1.0f, 0f, 0f);
                                    renderItem(itemRenderer, crystal2, 0.5f, 0.55f, 0.35f, 11.25f, 1.0f, 0f, 0f);
                                    renderItem(itemRenderer, crystal3, 0.35f, 0.55f, 0.5f,  348.75f , 0f, 0f, 1.0f);
                                    renderItem(itemRenderer, crystal4, 0.65f, 0.55f, 0.5f, 11.25f, 0f, 0f, 1.0f);
                                    renderItem(itemRenderer, crystal5, 0.35f, 0.55f, 0.65f, 348.75f , 1.0f, 0f, 1.0f);
                                    renderItem(itemRenderer, crystal6, 0.65f, 0.55f, 0.35f, 11.25f, 1.0f, 0f, 1.0f);
                                    renderItem(itemRenderer, crystal7, 0.5f, 0.55f, 0.5f, 0f, 0f, 0f, 0f);}
            if(te.crystalCount==8) {renderItem(itemRenderer, crystal1, 0.5f, 0.55f, 0.65f,  348.75f , 1.0f, 0f, 0f);
                                    renderItem(itemRenderer, crystal2, 0.5f, 0.55f, 0.35f, 11.25f, 1.0f, 0f, 0f);
                                    renderItem(itemRenderer, crystal3, 0.35f, 0.55f, 0.5f,  348.75f , 0f, 0f, 1.0f);
                                    renderItem(itemRenderer, crystal4, 0.65f, 0.55f, 0.5f, 11.25f, 0f, 0f, 1.0f);
                                    renderItem(itemRenderer, crystal5, 0.35f, 0.55f, 0.65f, 348.75f , 1.0f, 0f, 1.0f);
                                    renderItem(itemRenderer, crystal6, 0.65f, 0.55f, 0.35f, 11.25f, 1.0f, 0f, 1.0f);
                                    renderItem(itemRenderer, crystal7, 0.35f, 0.55f, 0.35f, 11.25f , 1.0f, 0f, -1.0f);
                                    renderItem(itemRenderer, crystal8, 0.65f, 0.55f, 0.65f, 348.75f, 1.0f, 0f, -1.0f);}
            if(te.crystalCount==9) {renderItem(itemRenderer, crystal1, 0.5f, 0.55f, 0.65f,  348.75f , 1.0f, 0f, 0f);
                                    renderItem(itemRenderer, crystal2, 0.5f, 0.55f, 0.35f, 11.25f, 1.0f, 0f, 0f);
                                    renderItem(itemRenderer, crystal3, 0.35f, 0.55f, 0.5f,  348.75f , 0f, 0f, 1.0f);
                                    renderItem(itemRenderer, crystal4, 0.65f, 0.55f, 0.5f, 11.25f, 0f, 0f, 1.0f);
                                    renderItem(itemRenderer, crystal5, 0.35f, 0.55f, 0.65f, 348.75f , 1.0f, 0f, 1.0f);
                                    renderItem(itemRenderer, crystal6, 0.65f, 0.55f, 0.35f, 11.25f, 1.0f, 0f, 1.0f);
                                    renderItem(itemRenderer, crystal7, 0.35f, 0.55f, 0.35f, 11.25f , 1.0f, 0f, -1.0f);
                                    renderItem(itemRenderer, crystal8, 0.65f, 0.55f, 0.65f, 348.75f, 1.0f, 0f, -1.0f);
                                    renderItem(itemRenderer, crystal9, 0.5f, 0.55f, 0.5f, 0f, 0f, 0f, 0f);}

        }
        if(enumfacing==EnumFacing.EAST)
        {
            if(te.crystalCount==1) {renderItem(itemRenderer, crystal1, 0.45f, 0.5f, 0.5f, 90.0f, 0f, 0f, 1.0f);}
            if(te.crystalCount==2) {renderItem(itemRenderer, crystal1, 0.45f, 0.35f, 0.5f,  78.75f , 0f, 0f, 1.0f);
                                    renderItem(itemRenderer, crystal2, 0.45f, 0.65f, 0.5f, 101.25f, 0f, 0f, 1.0f);}
            if(te.crystalCount==3) {renderItem(itemRenderer, crystal1, 0.45f, 0.35f, 0.5f,  78.75f , 0f, 0f, 1.0f);
                                    renderItem(itemRenderer, crystal2, 0.45f, 0.65f, 0.5f, 101.25f, 0f, 0f, 1.0f);
                                    renderItem(itemRenderer, crystal3, 0.45f, 0.5f, 0.5f, 90.0f, 0f, 0f, 1.0f);}
        }
        if(enumfacing==EnumFacing.SOUTH)
        {
            if(te.crystalCount==1) {renderItem(itemRenderer, crystal1, 0.5f, 0.5f, 0.45f, 90.0f, 1.0f, 0f, 0f);}
            if(te.crystalCount==2) {renderItem(itemRenderer, crystal1, 0.5f, 0.35f, 0.45f, 101.25f, 1.0f, 0f, 0f);
                                    renderItem(itemRenderer, crystal2, 0.5f, 0.65f, 0.45f, 78.75f, 1.0f, 0f, 0f);}
            if(te.crystalCount==3) {renderItem(itemRenderer, crystal1, 0.5f, 0.35f, 0.45f, 101.25f, 1.0f, 0f, 0f);
                                    renderItem(itemRenderer, crystal2, 0.5f, 0.65f, 0.45f, 78.75f, 1.0f, 0f, 0f);
                                    renderItem(itemRenderer, crystal3, 0.5f, 0.5f, 0.45f, 90.0f, 1.0f, 0f, 0f);}
        }
        if(enumfacing==EnumFacing.WEST)
        {
            if(te.crystalCount==1) {renderItem(itemRenderer, crystal1, 0.65f, 0.5f, 0.5f, 90.0f, 0f, 0f, 1.0f);}
            if(te.crystalCount==2) {renderItem(itemRenderer, crystal1, 0.65f, 0.35f, 0.5f,  101.25f , 0f, 0f, 1.0f);
                                    renderItem(itemRenderer, crystal2, 0.65f, 0.65f, 0.5f, 78.75f, 0f, 0f, 1.0f);}
            if(te.crystalCount==3) {renderItem(itemRenderer, crystal1, 0.65f, 0.35f, 0.5f,  101.25f , 0f, 0f, 1.0f);
                                    renderItem(itemRenderer, crystal2, 0.65f, 0.65f, 0.5f, 78.75f, 0f, 0f, 1.0f);renderItem(itemRenderer, crystal3, 0.65f, 0.5f, 0.5f, 90.0f, 0f, 0f, 1.0f);}
        }
        GlStateManager.popMatrix();
    }


    public static void renderItem(RenderItem itemRenderer, ItemStack itemStack, float x, float y, float z, float angle, float xr, float yr, float zr) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        GlStateManager.rotate(angle, xr, yr, zr);
        if (!itemRenderer.shouldRenderItemIn3D(itemStack)) {GlStateManager.rotate(180f, 0f, 1f, 0f);}
        GlStateManager.pushAttrib();
        RenderHelper.enableStandardItemLighting();
        itemRenderer.renderItem(itemStack, ItemCameraTransforms.TransformType.FIXED);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.popAttrib();
        GlStateManager.popMatrix();
    }
}
