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
        float f0=1.0f;
        float f1= 0.5f;
        float f2= 0.45f;
        float f3=0.65f;
        float f4=11.25f;
        float f5=0.35f;
        float f6= 0.55f;
        float f7=348.75f;
        float f8=90.0f;
        float f9=101.25f;
        float f10=78.75f;

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
            if(te.crystalCount==1) {renderItem(itemRenderer, crystal1, f1, f6, f1, 0f, 0f, 0f, 0f);}
            if(te.crystalCount==2) {renderItem(itemRenderer, crystal1, f1, f6, f3,  f7 , f0, 0f, 0f);renderItem(itemRenderer, crystal2, f1, f6, f5, f4, f0, 0f, 0f);}
            if(te.crystalCount==3) {renderItem(itemRenderer, crystal1, f1, f6, f3,  f7 , f0, 0f, 0f);renderItem(itemRenderer, crystal2, f1, f6, f5, f4, f0, 0f, 0f);renderItem(itemRenderer, crystal3, f1, f6, f1, 0f, 0f, 0f, 0f);}
        }
        if(enumfacing==EnumFacing.NORTH)
        {
            if(te.crystalCount==1) {renderItem(itemRenderer, crystal1, f1, f1, f6, f8, f0, 0f, 0f);}
            if(te.crystalCount==2) {renderItem(itemRenderer, crystal1, f1, f5, f6, f10, f0, 0f, 0f);renderItem(itemRenderer, crystal2, f1, f3, f6, f9, f0, 0f, 0f);}
            if(te.crystalCount==3) {renderItem(itemRenderer, crystal1, f1, f5, f6, f10, f0, 0f, 0f);renderItem(itemRenderer, crystal2, f1, f3, f6, f9, f0, 0f, 0f);renderItem(itemRenderer, crystal3, f1, f1, f6, f8, f0, 0f, 0f);}
        }
        if(enumfacing==EnumFacing.EAST)
        {
            if(te.crystalCount==1) {renderItem(itemRenderer, crystal1, f2, f1, f1, f8, 0f, 0f, f0);}
            if(te.crystalCount==2) {renderItem(itemRenderer, crystal1, f2, f5, f1,  f10 , 0f, 0f, f0);renderItem(itemRenderer, crystal2, f2, f3, f1, f9, 0f, 0f, f0);}
            if(te.crystalCount==3) {renderItem(itemRenderer, crystal1, f2, f5, f1,  f10 , 0f, 0f, f0);renderItem(itemRenderer, crystal2, f2, f3, f1, f9, 0f, 0f, f0);renderItem(itemRenderer, crystal3, f2, f1, f1, f8, 0f, 0f, f0);}
        }
        if(enumfacing==EnumFacing.SOUTH)
        {
            if(te.crystalCount==1) {renderItem(itemRenderer, crystal1, f1, f1, f2, f8, f0, 0f, 0f);}
            if(te.crystalCount==2) {renderItem(itemRenderer, crystal1, f1, f5, f2, f9, f0, 0f, 0f);renderItem(itemRenderer, crystal2, f1, f3, f2, f10, f0, 0f, 0f);}
            if(te.crystalCount==3) {renderItem(itemRenderer, crystal1, f1, f5, f2, f9, f0, 0f, 0f);renderItem(itemRenderer, crystal2, f1, f3, f2, f10, f0, 0f, 0f);renderItem(itemRenderer, crystal3, f1, f1, f2, f8, f0, 0f, 0f);}
        }
        if(enumfacing==EnumFacing.WEST)
        {
            if(te.crystalCount==1) {renderItem(itemRenderer, crystal1, f3, f1, f1, f8, 0f, 0f, f0);}
            if(te.crystalCount==2) {renderItem(itemRenderer, crystal1, f3, f5, f1,  f9 , 0f, 0f, f0);renderItem(itemRenderer, crystal2, f3, f3, f1, f10, 0f, 0f, f0);}
            if(te.crystalCount==3) {renderItem(itemRenderer, crystal1, f3, f5, f1,  f9 , 0f, 0f, f0);renderItem(itemRenderer, crystal2, f3, f3, f1, f10, 0f, 0f, f0);renderItem(itemRenderer, crystal3, f3, f1, f1, f8, 0f, 0f, f0);}
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
