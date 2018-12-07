package com.mowmaster.dust.tiles;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileEntityBase extends TileEntity
{
    public static int getItemBurnTime(ItemStack fuel)
    {
        if (fuel.isEmpty()) return 0;
        else
        {
            int burnTime = net.minecraftforge.event.ForgeEventFactory.getItemBurnTime(fuel);
            if (burnTime >= 0) return burnTime;
            Item item = fuel.getItem();

            if (item == Item.getItemFromBlock(Blocks.WOODEN_SLAB)) return 150;
            else if (item == Item.getItemFromBlock(Blocks.WOOL)) return 100;
            else if (item == Item.getItemFromBlock(Blocks.CARPET)) return 67;
            else if (item == Item.getItemFromBlock(Blocks.LADDER)) return 300;
            else if (item == Item.getItemFromBlock(Blocks.WOODEN_BUTTON)) return 100;
            else if (Block.getBlockFromItem(item).getDefaultState().getMaterial() == Material.WOOD) return 300;
            else if (item == Item.getItemFromBlock(Blocks.COAL_BLOCK)) return 16000;
            else if (item instanceof ItemTool && "WOOD".equals(((ItemTool)item).getToolMaterialName())) return 200;
            else if (item instanceof ItemSword && "WOOD".equals(((ItemSword)item).getToolMaterialName())) return 200;
            else if (item instanceof ItemHoe && "WOOD".equals(((ItemHoe)item).getMaterialName())) return 200;
            else if (item == Items.STICK) return 100;
            else if (item != Items.BOW && item != Items.FISHING_ROD)
            {
                if (item == Items.SIGN) return 200;
                else if (item == Items.COAL) return 1600;
                else if (item == Items.LAVA_BUCKET) return 20000;
                else if (item != Item.getItemFromBlock(Blocks.SAPLING) && item != Items.BOWL)
                {
                    if (item == Items.BLAZE_ROD) return 2400;
                    else if (item instanceof ItemDoor && item != Items.IRON_DOOR) return 200;
                    else return item instanceof ItemBoat ? 400 : 0;
                }
                else return 100;
            }
            else return 300;
        }
    }

    public boolean doItemsMatch(ItemStack itemStackIn, ItemStack toCompareTo)
    {
        if(itemStackIn.getHasSubtypes())
        {
            if(itemStackIn.getItem().equals(toCompareTo.getItem()) && itemStackIn.getMetadata()==toCompareTo.getMetadata())
            {
                return true;
            }
            else return false;
        }
        else if(itemStackIn.hasTagCompound())
        {
            NBTTagCompound itemIn = itemStackIn.getTagCompound();
            NBTTagCompound itemStored = toCompareTo.getTagCompound();
            if(itemIn.equals(itemStored) && itemStackIn.getItem().equals(toCompareTo.getItem()))
            {
                return true;
            }
            else return false;
        }
        else
        {
            if(itemStackIn.getItem().equals(toCompareTo.getItem()))
            {
                return true;
            }
        }

        return false;
    }

    public void updateBlock()
    {
        markDirty();
        IBlockState state = world.getBlockState(pos);
        world.notifyBlockUpdate(pos, state, state, 3);
        world.setBlockState(pos,state,3);
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
