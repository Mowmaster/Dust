package com.mowmaster.dust.Items.Augments;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;

public class AugmentRenderDiffuser extends AugmentBase
{

    public AugmentRenderDiffuser(Properties p_41383_) {
        super(p_41383_.durability(6));
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return false;
    }

    public static int getAugmentMode(ItemStack stack) {

        // 0 - No Particles
        // 1 - No Render Item
        // 2 - No Render Upgrade
        // 3 - No Particles/No Render Item
        // 4 - No Particles/No Render Upgrade
        // 5 - No Render Item/No Render Upgrade
        // 6 - No Particles/No Render Item/No Render Upgrade

        return (stack.isDamaged())?(stack.getDamageValue()):(0);
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
                int setNewDamage = (damage<=6)?(damage):(0);
                stackInHand.setDamageValue(setNewDamage);

                player.setItemInHand(p_41434_,stackInHand);
            }
        }

        return super.use(p_41432_, p_41433_, p_41434_);
    }

}
