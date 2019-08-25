package com.mowmaster.dust.items.itemPedestalUpgrades;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ipuaDropper extends ipuBasic {

    public ipuaDropper()
    {

    }

    public void upgradeAction(World world, BlockPos posOfPedestal, int summonRange, int summonCount)
    {
        //Range comes from enchant
        //ammount comes from...speed?

        if(!world.isRemote)
        {
            if(!world.isBlockPowered(posOfPedestal))
            {
                if(!getStackInPedestal(world,posOfPedestal).isEmpty())//hasItem
                {
                    ItemStack itemToSummon = getStackInPedestal(world,posOfPedestal).copy();
                    itemToSummon.setCount(summonCount);
                    EntityItem itemEntity = new EntityItem(world,getPosOfBlockBelow(world,posOfPedestal,-summonRange).getX() + 0.5,getPosOfBlockBelow(world,posOfPedestal,-summonRange).getY(),getPosOfBlockBelow(world,posOfPedestal,-summonRange).getZ() + 0.5,itemToSummon);
                    itemEntity.motionX = 0;
                    itemEntity.motionY = 0;
                    itemEntity.motionZ = 0;
                    world.spawnEntity(itemEntity);
                    this.removeFromPedestal(world,posOfPedestal,itemToSummon.getCount());
                }
            }
        }

    }

}
