package com.mowmaster.dust.tiles;

import com.mowmaster.dust.enums.CrystalItems;
import com.mowmaster.dust.items.ItemCrystal;
import com.mowmaster.dust.items.ItemRegistry;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import static com.mowmaster.dust.items.ItemRegistry.crystal;

/**
 * Created by KingMowmaster on 3/26/2017.
 */
public class TileCrystalCluster extends TileEntity
{
    private int crystalCount = 0;
    private int crystalRed = 0;
    private int crystalBlue = 0;
    private int crystalYellow = 0;
    private int crystalPurple = 0;
    private int crystalOrange = 0;
    private int crystalGreen = 0;
    private int crystalWhite = 0;
    private int crystalBlack = 0;

    public boolean addCrystal()
    {
        if(!world.isRemote)
        {
            if (crystalCount < 5) {
                crystalCount++;
                return true;
            }
        }
        return false;
    }



    public void removeCrystal() {
        if (!world.isRemote) {
            if (crystalCount > 0) {
                world.spawnEntity(new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(crystal, 1, 0)));
                crystalCount--;
            }
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setInteger("CrystalCount",crystalCount);

        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        this.crystalCount = compound.getInteger("CrystalCount");
    }
}
