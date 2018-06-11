package com.mowmaster.dust.tiles.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class ContainerCrystalCrusher extends Container
{
    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return false;
    }


}
