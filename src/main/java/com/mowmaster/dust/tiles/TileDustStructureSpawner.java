package com.mowmaster.dust.tiles;

import com.mowmaster.dust.blocks.utility.BlockStructureSpawner;
import com.mowmaster.dust.world.structures.DustStructureGenerator;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

import static com.mowmaster.dust.misc.DustConfigurationFile.devBlocks;


public class TileDustStructureSpawner extends TileEntity implements ITickable
{
    public static Boolean inDev = devBlocks;
    public int x=0;
    public int y=0;
    public int z=0;
    public int rot = 0;
    public String structureName = "";

    private void updateBlock()
    {
        markDirty();
        IBlockState state = world.getBlockState(pos);
        world.notifyBlockUpdate(pos, state, state, 3);
        world.setBlockState(pos,state,3);
    }

    public void setName(String s)
    {
        structureName = s;
        updateBlock();
    }

    public void modifyX(int numAddOrSub)
    {
        x=Math.addExact(x,numAddOrSub);
        updateBlock();
        //System.out.println("X: " + x);
    }

    public void modifyY(int numAddOrSub)
    {
        y=Math.addExact(y,numAddOrSub);
        updateBlock();
        //System.out.println("Y: " + y);
    }

    public void modifyZ(int numAddOrSub)
    {
        z=Math.addExact(z,numAddOrSub);
        updateBlock();
        //System.out.println("Z: " + z);
    }

    public void modifyRotation(int numAddOrSub)
    {
        if(Math.addExact(rot,numAddOrSub)>3)
        {
            rot=0;
        }
        else if(Math.addExact(rot,numAddOrSub)<0)
        {
            rot=3;
        }
        else
        {
            rot=Math.addExact(rot,numAddOrSub);
        }
        updateBlock();
        //System.out.println("R: " + rot);
    }

    public void generate()
    {
        World world = this.getWorld();
        BlockPos pos = this.getPos();
        Random rand = new Random();

        if(!world.isRemote)
        {
            if(world.getBlockState(pos).getBlock() instanceof BlockStructureSpawner)
            {

                DustStructureGenerator genme = new DustStructureGenerator(getStructureName());
                if(!genme.generate(world,rand,pos.add(x,y,z),rot))
                {
                    world.setBlockToAir(pos);
                }
                else
                {
                    world.setBlockToAir(pos);
                    //genme.rotate(rot);
                    genme.generate(world,rand,pos.add(x,y,z),rot);
                }
            }
        }
    }

    private String getStructureName()
    {
        return structureName;
    }


    @Override
    public void update() {
        World world = this.getWorld();
        BlockPos pos = this.getPos();
        Random rand = new Random();


        if(!world.isRemote)
        {
            if(inDev==false)
            {
                if(world.getBlockState(pos).getBlock().equals(BlockStructureSpawner.structure1))
                {
                    DustStructureGenerator genme = new DustStructureGenerator(getStructureName());
                    if(!genme.generate(world,rand,pos.add(x,y,z),rot))
                    {
                        world.setBlockToAir(pos);
                    }
                    else
                    {
                        world.setBlockToAir(pos);
                        //genme.rotate(rot);
                        genme.generate(world,rand,pos.add(x,y,z),rot);
                    }
                }
            }
        }
    }


    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setString("structurename",this.structureName);
        compound.setInteger("posx",this.x);
        compound.setInteger("posy",this.y);
        compound.setInteger("posz",this.z);
        compound.setInteger("rotate",this.rot);


        return compound;

    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.structureName = compound.getString("structurename");
        this.x = compound.getInteger("posx");
        this.y = compound.getInteger("posy");
        this.z = compound.getInteger("posz");
        this.rot = compound.getInteger("rotate");
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        readFromNBT(pkt.getNbtCompound());
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return writeToNBT(new NBTTagCompound());
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(pos, 0, getUpdateTag());
    }
}
