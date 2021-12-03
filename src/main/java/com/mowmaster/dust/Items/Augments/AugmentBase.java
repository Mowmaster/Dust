package com.mowmaster.dust.Items.Augments;

import com.mowmaster.dust.DeferredRegistery.DeferredRegisterItems;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

import static com.mowmaster.dust.References.Constants.MODID;

public class AugmentBase extends Item implements IPedestalAugment
{

    public AugmentBase(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {
        super.appendHoverText(p_41421_, p_41422_, p_41423_, p_41424_);

        TranslatableComponent base = new TranslatableComponent(getDescriptionId() + ".description");
        base.withStyle(ChatFormatting.LIGHT_PURPLE);
        p_41423_.add(base);

        if(p_41421_.getItem().equals(DeferredRegisterItems.AUGMENT_PEDESTAL_RENDERDIFFUSER.get()))
        {
            TranslatableComponent use = new TranslatableComponent(getDescriptionId() + ".description_use");
            use.withStyle(ChatFormatting.AQUA);
            p_41423_.add(use);
        }

        TranslatableComponent crafted = new TranslatableComponent(getDescriptionId() + ".description_crafted");
        crafted.withStyle(ChatFormatting.RED);
        p_41423_.add(crafted);
    }
}
