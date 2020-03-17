package com.mowmaster.dust.item;

import com.mowmaster.dust.dust;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import javax.annotation.Nullable;
import java.util.List;

import static com.mowmaster.dust.references.Reference.MODID;


public class ItemSpellScroll extends Item
{
    public ItemSpellScroll()
    {
        super(new Item.Properties().maxStackSize(64).group(dust.ITEM_GROUP));
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        PlayerEntity player = context.getPlayer();
        ItemStack stack = context.getItem();

        if(stack.getTag().contains("scrolleffect"))
        {
            EffectInstance effect = getPotionEffectFromStack(stack);
            if(effect != null)
            {
                player.addPotionEffect(effect);
            }
        }
        return super.onItemUse(context);
    }

    public EffectInstance getPotionEffectFromStack(ItemStack stack)
    {
        EffectInstance instance = null;
        if(stack.getTag().contains("scrolleffect"))
        {
            CompoundNBT invTag = stack.getChildTag("scrolleffect");
            instance = EffectInstance.read(invTag);
        }
        return instance;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);

        if(stack.getTag().contains("scrolleffect"))
        {
            EffectInstance effect = getPotionEffectFromStack(stack);
            String s1 = effect.getEffectName();
            int s2 = effect.getAmplifier();
            int s3 = effect.getDuration();
            String count = "";

            switch (s2)
            {
                case 0:
                    count = "I";
                    break;
                case 1:
                    count = "II";
                    break;
                case 2:
                    count = "III";
                    break;
                case 3:
                    count = "IV";
                    break;
                case 4:
                    count = "V";
                    break;
                case 5:
                    count = "VI";
                    break;
                case 6:
                    count = "VII";
                    break;
                case 7:
                    count = "VIII";
                    break;
                case 8:
                    count = "IX";
                    break;
                case 9:
                    count = "X";
                    break;
            }
            stack.setDisplayName(new TranslationTextComponent("Scroll of "+ s1));
            tooltip.add(new TranslationTextComponent("Potency: " + count));
            tooltip.add(new TranslationTextComponent("Duration: " + s3 / 20 + " seconds"));
        }
        else
        {
            tooltip.add(new TranslationTextComponent("Doesnt function if grabbed from cheat mode"));
        }

        //tooltip.add(new TranslationTextComponent(TextFormatting.GOLD + "SpellScroll"));
    }

    public static final Item SPELLSCROLL = new ItemSpellScroll().setRegistryName(new ResourceLocation(MODID, "scroll/spellscroll"));

    @SubscribeEvent
    public static void onItemRegistryReady(RegistryEvent.Register<Item> event)
    {
        event.getRegistry().register(SPELLSCROLL);
    }


}
