package com.mowmaster.dust.Items.Upgrades.Pedestal;

import com.mowmaster.dust.CreativeTabs.DustItemTabs;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;

import java.util.List;

public class ItemUpgradeImport extends ItemUpgradeBase
{
    public ItemUpgradeImport(Properties p_41383_) {
        super(new Properties().durability(15).tab(DustItemTabs.TAB_ITEMS));
    }

    public static int getUpgradeMode(ItemStack stack) {

        return stack.getDamageValue();
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level p_41432_, Player p_41433_, InteractionHand p_41434_) {
        Level world = p_41432_;
        Player player = p_41433_;
        InteractionHand hand = p_41434_;
        ItemStack stackInHand = player.getItemInHand(hand);
        //Build Color List from NBT

        HitResult result = player.pick(5,0,false);
        if(player.isCrouching())
        {
            if(result.getType().equals(HitResult.Type.MISS))
            {
                int damage = stackInHand.getDamageValue()+1;
                int setNewDamage = (damage<=14)?(damage):(0);
                stackInHand.setDamageValue(setNewDamage);

                player.setItemInHand(p_41434_,stackInHand);
                System.out.println(setNewDamage);
            }
        }

        return super.use(p_41432_, p_41433_, p_41434_);
    }
}
