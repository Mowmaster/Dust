package com.mowmaster.dust.handlers;

import com.mowmaster.dust.references.Reference;
import com.mowmaster.dust.tiles.TileCrystalFurnace;
import com.mowmaster.dust.tiles.containers.ContainerCrystalFurnace;
import com.mowmaster.dust.tiles.guis.GuiCrystalFurnace;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import javax.annotation.Nullable;

/**
 * Created by KingMowmaster on 6/13/2018.
 */
public class GuiHandler implements IGuiHandler
{
    @Nullable
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if(ID == Reference.GUI_FURNACE) return new GuiCrystalFurnace(player.inventory,(TileCrystalFurnace)world.getTileEntity(new BlockPos(x,y,z)));
        return null;
    }

    @Nullable
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if(ID == Reference.GUI_FURNACE) return new ContainerCrystalFurnace(player.inventory,(TileCrystalFurnace)world.getTileEntity(new BlockPos(x,y,z)));
        return null;
    }
}
