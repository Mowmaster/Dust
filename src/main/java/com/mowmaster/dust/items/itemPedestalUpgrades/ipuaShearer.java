package com.mowmaster.dust.items.itemPedestalUpgrades;

import com.mowmaster.dust.tiles.TilePedestal;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;

import java.util.List;
import java.util.Random;

public class ipuaShearer extends ipuBasic {

    public ipuaShearer()
    {

    }

    public void upgradeAction(World world, BlockPos posOfPedestal, int summonRange, int summonCount)
    {
        BlockPos posOfInvBelow = getPosOfBlockBelow(world,posOfPedestal,1);

        if(!world.isRemote)
        {
            if(!world.isBlockPowered(posOfPedestal))
            {
                ItemStack itemFromInv = ItemStack.EMPTY;
                if (world.getTileEntity(posOfInvBelow) != null) {
                    if (world.getTileEntity(posOfInvBelow).hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN)) {

                        TileEntity invToPullFrom = world.getTileEntity(posOfInvBelow);
                        if (invToPullFrom instanceof TilePedestal) {
                            itemFromInv = ItemStack.EMPTY;
                        }
                        else {
                            //itemFromInv = getNextSlotInChest(posOfInvBelow);
                        }

                        if(itemFromInv.getItem() instanceof ItemShears)
                        {

                            List<EntitySheep> baa = world.getEntitiesWithinAABB(EntitySheep.class,new AxisAlignedBB(getPosOfBlockBelow(world,posOfPedestal,-1)));
                            for(EntitySheep baaaaaa : baa)
                            {
                                if (!baaaaaa.getSheared() && !baaaaaa.isChild())
                                {
                                    if(getStackInPedestal(world,posOfPedestal).equals(ItemStack.EMPTY))
                                    {

                                            baaaaaa.setSheared(true);
                                            Random rando = new Random();
                                            int i = 1 + rando.nextInt(3);

                                            for (int j = 0; j < i; ++j)
                                            {
                                                addToPedestal(world,posOfPedestal,new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, baaaaaa.getFleeceColor().getMetadata()));
                                            }

                                        itemFromInv.setItemDamage(itemFromInv.getItemDamage()+1);
                                        world.playSound((EntityPlayer)null, posOfPedestal.getX(), posOfPedestal.getY(), posOfPedestal.getZ(), SoundEvents.ENTITY_SHEEP_SHEAR, SoundCategory.BLOCKS, 0.5F, 1.0F);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

    }

}
