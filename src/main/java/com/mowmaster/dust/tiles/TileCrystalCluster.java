package com.mowmaster.dust.tiles;

import com.google.common.collect.Lists;
import com.mowmaster.dust.blocks.BlockCrystal;
import com.mowmaster.dust.blocks.BlockCrystalBase;
import com.mowmaster.dust.enums.CrystalItems;
import com.mowmaster.dust.items.ItemCrystal;
import com.mowmaster.dust.items.ItemRegistry;
import com.sun.jna.StringArray;
import com.sun.org.apache.xalan.internal.xsltc.util.IntegerArray;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.BlockSandStone;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

import java.util.ArrayList;

import static com.mowmaster.dust.blocks.BlockRegistry.crystalCluster;
import static com.mowmaster.dust.items.ItemRegistry.crystal;
import static net.minecraft.block.BlockDirectional.FACING;

/**
 * Created by KingMowmaster on 3/26/2017.
 */
public class TileCrystalCluster extends TileEntity implements ITickable
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

    @Override
    public void update() {

    }

    public int getCrystalCount()
    {
        return crystalCount;
    }

    ArrayList<Integer> CrystalList = new ArrayList<>();

    public boolean addCrystal(int type)
    {

        switch (type)
        {
            case 0:
                crystalRed++;
            case 1:
                crystalBlue++;
            case 2:
                crystalYellow++;
            case 3:
                crystalPurple++;
            case 4:
                crystalGreen++;
            case 5:
                crystalOrange++;
            case 6:
                crystalWhite++;
            case 7 :
                crystalBlack++;
        }
            crystalCount++;
            CrystalList.add(type);
        return true;
    }



    public void removeCrystal(TileEntity tile) {
        World worldIn = tile.getWorld();
            if (crystalCount > 0) {
                switch (CrystalList.get(CrystalList.size()-1))
                {
                    case 0:
                        crystalRed--;
                    case 1:
                        crystalBlue--;
                    case 2:
                        crystalYellow--;
                    case 3:
                        crystalPurple--;
                    case 4:
                        crystalGreen--;
                    case 5:
                        crystalOrange--;
                    case 6:
                        crystalWhite--;
                    case 7 :
                        crystalBlack--;
                }
                worldIn.spawnEntity(new EntityItem(worldIn, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, new ItemStack(ItemRegistry.crystal, 1,CrystalList.get(CrystalList.size()-1))));
                CrystalList.remove(CrystalList.size()-1);
                crystalCount--;
            }
            if(crystalCount==0)
            {
                worldIn.setBlockToAir(pos);
            }
    }
//Make CrystalList use nbtTagList instead of to string and back
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);

        compound.setInteger("CrystalCount",crystalCount);
        compound.setString("CrystalList",CrystalList.toString());

        return compound;
    }


    private String crystals;
    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        this.crystalCount = compound.getInteger("CrystalCount");
        this.crystals = compound.getString("CrystalList");
        String[] searilizedList = crystals.replace("[","").replace("]","").split(",");
        for(int i=0;i<searilizedList.length;i++)
        {
            CrystalList.add(Integer.parseInt(searilizedList[i].replace(" ","")));
        }


    }
}
