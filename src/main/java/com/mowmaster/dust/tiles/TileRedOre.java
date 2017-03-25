package com.mowmaster.dust.tiles;

import net.minecraft.block.BlockFurnace;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.property.Properties;

public class TileRedOre extends TileEntity
{
    public static final PropertyDirection TEFACING = PropertyDirection.create("tefacing");
    EnumFacing tefacing = EnumFacing.UP;
    int growth;


    public TileRedOre()
    {
        growth = 0;
    }



    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        return super.writeToNBT(compound);
    }



    /*but you have an EnumFacing facing = EnumFacing.UP; at the start of your TileEntity
and then you use getActualState() in the block to grab the tile (world.getTileEntity(pos)) and then state.withProperty(YOURFACINGPROPERTY, tile.facing);*/
    public IBlockState writeBlockState(IBlockState blockState)
    {
        blockState = blockState.withProperty(TEFACING,EnumFacing.UP);

        return blockState;
    }


    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound nbt = new NBTTagCompound();
        this.writeToNBT(nbt);
        int metadata = getBlockMetadata();
        return new SPacketUpdateTileEntity(this.pos, metadata, nbt);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        this.readFromNBT(pkt.getNbtCompound());
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        NBTTagCompound nbt = new NBTTagCompound();
        this.writeToNBT(nbt);
        return nbt;
    }

    @Override
    public void handleUpdateTag(NBTTagCompound tag) {
        this.readFromNBT(tag);
    }

    @Override
    public NBTTagCompound getTileData() {
        NBTTagCompound nbt = new NBTTagCompound();
        this.writeToNBT(nbt);
        return nbt;
    }
}
