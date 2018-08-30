package com.mowmaster.dust.tiles;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;


public class TileTrapBlock extends TileEntity
{
    PotionEffect potionEffect = new PotionEffect(MobEffects.LUCK);

    private void updateBlock()
    {
        markDirty();
        IBlockState state = world.getBlockState(pos);
        world.notifyBlockUpdate(pos, state, state, 3);
        world.setBlockState(pos,state,3);
    }

    public void setTrapEffect(PotionEffect trapEffect)
    {
        potionEffect=trapEffect;
        updateBlock();
    }

    public PotionEffect getTrapEffect()
    {
        return potionEffect;
    }


    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setTag("effect",potionEffect.writeCustomPotionEffectToNBT(new NBTTagCompound()));
        return compound;
    }



    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        NBTTagCompound savedEffect = compound.getCompoundTag("effect");
        this.potionEffect = PotionEffect.readCustomPotionEffectFromNBT(savedEffect);
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
