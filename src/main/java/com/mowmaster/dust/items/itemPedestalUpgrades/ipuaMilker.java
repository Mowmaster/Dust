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

public class ipuaMilker extends ipuBasic {

    public ipuaMilker()
    {

    }

    /*public void upgradeAction(World world, BlockPos posOfPedestal, int summonRange, int summonCount)
    {
        //Range comes from enchant
        //ammount comes from...speed?

        if(!world.isRemote)
        {
            if(!world.isBlockPowered(posOfPedestal))
            {
                ItemStack itemFromInv = ItemStack.EMPTY;
                if (world.getTileEntity(getPosOfBlockBelow(1)) != null) {
                    if (world.getTileEntity(getPosOfBlockBelow(1)).hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN)) {

                        TileEntity invToPullFrom = world.getTileEntity(getPosOfBlockBelow(1));
                        if (invToPullFrom instanceof TilePedestal) {
                            itemFromInv = ItemStack.EMPTY;
                        }
                        else {
                            itemFromInv = getNextSlotInChest(getPosOfBlockBelow(1));
                        }

                        if(itemFromInv.getItem() instanceof ItemBucket)
                        {
                            List<EntityCow> moo = world.getEntitiesWithinAABB(EntityCow.class,new AxisAlignedBB(getPosOfBlockBelow(-1)));
                            for(EntityCow moomoo : moo)
                            {

                                if (itemFromInv.getItem() == Items.BUCKET && !moomoo.isChild())
                                {
                                    if (!hasItem())
                                    {
                                        world.playSound((EntityPlayer)null, getPos().getX(), getPos().getY(), getPos().getZ(), SoundEvents.ENTITY_COW_MILK, SoundCategory.BLOCKS, 0.5F, 1.0F);
                                        itemFromInv.shrink(1);
                                        addItem(new ItemStack(Items.MILK_BUCKET,1));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

    }*/

}