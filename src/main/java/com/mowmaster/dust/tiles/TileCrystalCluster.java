package com.mowmaster.dust.tiles;

import com.mowmaster.dust.blocks.BlockCrystal;
import com.mowmaster.dust.blocks.BlockCrystalBase;
import com.mowmaster.dust.enums.CrystalItems;
import com.mowmaster.dust.items.ItemCrystal;
import com.mowmaster.dust.items.ItemRegistry;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.BlockSandStone;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

import static com.mowmaster.dust.blocks.BlockRegistry.crystalCluster;
import static com.mowmaster.dust.items.ItemRegistry.crystal;
import static net.minecraft.block.BlockDirectional.FACING;

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

    public int getCrystalCount()
    {
        return crystalCount;
    }

    public boolean addCrystal()
    {
        if(!world.isRemote) {
            crystalCount++;
            return true;
        }
        return false;
    }

    public void getCrystalType(int type)
    {
        type = 0;
    }



    public void removeCrystal() {
        if (!world.isRemote) {
            if (crystalCount > 0) {
                //world.spawnEntity(new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(crystal, 1, 0)));
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
        tagCompound.setInteger("crystalCount",crystalCount);
    }

    public void readUpdateTag(NBTTagCompound tagCompound)
    {
        this.crystalCount = tagCompound.getInteger("crystalCount");
    }

}
