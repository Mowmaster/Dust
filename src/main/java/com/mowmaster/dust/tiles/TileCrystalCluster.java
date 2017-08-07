package com.mowmaster.dust.tiles;

import com.mowmaster.dust.items.ItemRegistry;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.world.World;


import java.util.ArrayList;


public class TileCrystalCluster extends TileEntity implements ITickable
{
    public int crystalCount = 0;
    private int crystalRed = 0;
    private int crystalBlue = 0;
    private int crystalYellow = 0;
    private int crystalPurple = 0;
    private int crystalOrange = 0;
    private int crystalGreen = 0;
    private int crystalWhite = 0;
    private int crystalBlack = 0;

    @Override
    public void update()
    {
        getCrystalCount();
    }

    public int getCrystalCount()
    {
        return crystalCount;
    }
    public int getCrystalInList(int x)
    {
        return CrystalList.get(x);
    }
    public int getCrystalInSize()
    {
        return CrystalList.size();
    }
// Look into using Stack<> or Deque programming methods for queued arrays
    public ArrayList<Integer> CrystalList = new ArrayList<>();

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
        markDirty();
        IBlockState state = world.getBlockState(pos);
        world.notifyBlockUpdate(pos,state,state,3);
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
                markDirty();
                IBlockState state = world.getBlockState(pos);
                world.notifyBlockUpdate(pos,state,state,3);
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



        return compound;
    }



    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);





    }



    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound tagCompound = new NBTTagCompound();
        this.writeUpdateTag(tagCompound);
        return new SPacketUpdateTileEntity(pos, getBlockMetadata(), tagCompound);

    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        NBTTagCompound tagCompound = pkt.getNbtCompound();
        this.readUpdateTag(tagCompound);
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        NBTTagCompound tagCompound = super.getUpdateTag();
        writeUpdateTag(tagCompound);
        return tagCompound;
    }

    public void writeUpdateTag(NBTTagCompound tagCompound)
    {
        //tagCompound.setInteger("carboncount",carboncount);
        tagCompound.setInteger("CrystalCount",crystalCount);
        tagCompound.setString("CrystalList",CrystalList.toString());

    }

    private String crystals;
    public void readUpdateTag(NBTTagCompound tagCompound)
    {
        //this.carboncount = tagCompound.getInteger("carboncount");
        this.crystalCount = tagCompound.getInteger("CrystalCount");
        this.crystals = tagCompound.getString("CrystalList");
        String[] searilizedList = crystals.replace("[","").replace("]","").split(",");
        for(int i=0;i<searilizedList.length;i++)
        {
            CrystalList.add(Integer.parseInt(searilizedList[i].replace(" ","")));
        }

    }

}
