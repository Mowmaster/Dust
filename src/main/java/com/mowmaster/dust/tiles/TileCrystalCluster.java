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
import java.util.*;


public class TileCrystalCluster extends TileEntity implements ITickable
{
 //crystal count is equal to the array size

    @Override
    public void update()
    {
        getCrystalCount();
    }

	
	public int getCrystalColorCountColor(int color)
	{
		return Collections.frequency(CrystalList,color);
	}
    public int getCrystalCount()
    {
        return CrystalList.size();
    }
    public int getCrystalInList(int x)
    {
        return CrystalList.get(x);
    }
    
// Look into using Stack<> or Deque programming methods for queued arrays
    public ArrayList<Integer> CrystalList = new ArrayList<>();

    public boolean addCrystal(int type)
    {
        CrystalList.add(type);
        markDirty();
        IBlockState state = world.getBlockState(pos);
        world.notifyBlockUpdate(pos,state,state,3);
        return true;

    }



    public void removeCrystal(TileEntity tile) {
        World worldIn = tile.getWorld();
            if (CrystalList.size() > 0) {
                
                worldIn.spawnEntity(new EntityItem(worldIn, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, new ItemStack(ItemRegistry.crystal, 1,CrystalList.get(CrystalList.size()-1))));
                CrystalList.remove(CrystalList.size()-1);
                crystalCount--;
                markDirty();
                IBlockState state = world.getBlockState(pos);
                world.notifyBlockUpdate(pos,state,state,3);
            }
            if(CrystalList.size()==0)
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
        tagCompound.setString("CrystalList",CrystalList.toString());


    }

    private String crystals;
    public void readUpdateTag(NBTTagCompound tagCompound)
    {
        //this.carboncount = tagCompound.getInteger("carboncount");
        this.crystals = tagCompound.getString("CrystalList");
        String[] searilizedList = crystals.replace("[","").replace("]","").split(",");
        for(int i=0;i<searilizedList.length;i++)
        {
            CrystalList.add(Integer.parseInt(searilizedList[i].replace(" ","")));
        }


    }

}
