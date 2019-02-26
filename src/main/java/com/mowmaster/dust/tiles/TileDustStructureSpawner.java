package com.mowmaster.dust.tiles;

import com.mowmaster.dust.blocks.buildingblocks.BlockCrate;
import com.mowmaster.dust.blocks.buildingblocks.BlockLootBlock;
import com.mowmaster.dust.blocks.buildingblocks.BlockPot;
import com.mowmaster.dust.blocks.crystal.BlockCrystal;
import com.mowmaster.dust.blocks.utility.BlockStructureSpawner;
import com.mowmaster.dust.enums.CrystalBlocks;
import com.mowmaster.dust.world.structures.DustStructureGenerator;
import com.mowmaster.dust.world.structures.structurebits.SpawnerTypesHostile;
import com.mowmaster.dust.world.structures.structurebits.SpawnerTypesPassive;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Random;

import static com.mowmaster.dust.blocks.buildingblocks.BlockLootBlock.TYPE;
import static com.mowmaster.dust.misc.DustConfigurationFile.devBlocks;


public class TileDustStructureSpawner extends TileEntity implements ITickable
{
    public static Boolean inDev = devBlocks;
    private String structureName = "";

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
                if(world.getBlockState(pos).getBlock().equals(BlockStructureSpawner.structure2))
                {
                    DustStructureGenerator genme = new DustStructureGenerator(getStructureName());
                    if(!genme.generate(world,rand,pos))
                    {
                        world.setBlockToAir(pos);
                    }
                    else
                    {
                        genme.generate(world,rand,pos);
                    }
                }
            }
        }
    }


    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setString("structurename",this.structureName);

        return compound;

    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.structureName = compound.getString("structurename");
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
