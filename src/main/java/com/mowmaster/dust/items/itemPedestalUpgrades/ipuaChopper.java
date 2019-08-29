package com.mowmaster.dust.items.itemPedestalUpgrades;

import com.mowmaster.dust.tiles.TilePedestal;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.items.CapabilityItemHandler;

public class ipuaChopper extends ipuBasic{


    public ipuaChopper(){}

    public void upgradeAction(World world, BlockPos posOfPedestal, BlockPos posOfInventory, ItemStack coinInPedestal, int rangeWidth, int rangeHeight)
    {

            WorldServer worldServer = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(0);
            FakePlayer fakePlayer = FakePlayerFactory.getMinecraft(worldServer);

            if(!world.isRemote)
            {
                if(!world.isBlockPowered(posOfPedestal))
                {
                    for(int x=-(rangeWidth);x<=(rangeWidth);x++)
                    {
                        for(int z=-(rangeWidth);z<=(rangeWidth);z++)
                        {
                            for(int y=-(rangeHeight);y<=(rangeHeight);y++) {
                                block = world.getBlockState(posThis.add(x, y, z));
                                if(tickChopper>84)
                                {
                                    if(hasItem())
                                    {
                                        if(Item.getItemFromBlock(block.getBlock()).getHasSubtypes())
                                        {
                                            if(doItemsMatch(new ItemStack(block.getBlock().getItemDropped(block,null,0),1,block.getBlock().getMetaFromState(block))))
                                            {

                                                if (world.getTileEntity(posThis.add(x,y,z))==null && block.getMaterial().equals(Material.WOOD) && block.getBlock().getLocalizedName().toLowerCase().contains("log") ||
                                                        block.getMaterial().equals(Material.LEAVES) && block.getBlock().getLocalizedName().toLowerCase().contains("leaves")) {
                                                    block.getBlock().harvestBlock(world,fakePlayer,posThis.add(x,y,z),block,null,getItemInPedestal());
                                                    world.setBlockToAir(posThis.add(x,y,z));
                                                    tickChopper=0;
                                                }
                                            }
                                        }
                                        else
                                        {
                                            if(doItemsMatch(new ItemStack(block.getBlock().getItemDropped(block,null,0))))
                                            {
                                                if (world.getTileEntity(posThis.add(x,y,z))==null &&block.getMaterial().equals(Material.WOOD) || block.getMaterial().equals(Material.LEAVES)) {
                                                    block.getBlock().harvestBlock(world,fakePlayer,posThis.add(x,y,z),block,null,getItemInPedestal());
                                                    world.setBlockToAir(posThis.add(x,y,z));
                                                    tickChopper=0;
                                                }
                                            }
                                        }

                                    }
                                    else
                                    {
                                        if (world.getTileEntity(posThis.add(x,y,z))==null &&block.getMaterial().equals(Material.WOOD) || block.getMaterial().equals(Material.LEAVES)) {
                                            block.getBlock().harvestBlock(world,fakePlayer,posThis.add(x,y,z),block,null,getItemInPedestal());
                                            world.setBlockToAir(posThis.add(x,y,z));
                                            tickChopper=0;
                                        }
                                    }

                                }
                                else tickChopper++;
                            }
                        }
                    }

                    getItemEntitiesNearby(1+rangeIncrease);
                }
            }
    }
}
