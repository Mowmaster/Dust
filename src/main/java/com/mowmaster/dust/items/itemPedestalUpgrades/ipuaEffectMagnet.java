package com.mowmaster.dust.items.itemPedestalUpgrades;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class ipuaEffectMagnet extends ipuBasic {

    public ipuaEffectMagnet()
    {

    }



    public void upgradeAction(World world, BlockPos posOfPedestal, int summonRange, int summonCount)
    {
        World world = this.world;
        ItemStack stack = ItemStack.EMPTY;
        int increase = rangeIncrease;

        if(!world.isRemote)
        {
            if(!world.isBlockPowered(posOfPedestal))
            {

                List<EntityItem> items = world.getEntitiesWithinAABB(EntityItem.class,new AxisAlignedBB(this.pos.getX() - (1 + increase), this.pos.getY() - (1 + increase),
                        this.pos.getZ() - (1 + increase), this.pos.getX() + (2 + increase), this.pos.getY() + (1 + increase), this.pos.getZ() + (2 + increase)));

                for (EntityItem item : items) {
                    onEntitiesCollidWithBlock(item);
                }
            }
        }

    }

}
