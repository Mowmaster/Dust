package com.mowmaster.dust.item;

import com.mowmaster.dust.blocks.BlockTrap;
import com.mowmaster.dust.dust;
import com.mowmaster.dust.references.Reference;
import com.mowmaster.dust.tiles.TileTrap;
import net.minecraft.block.Blocks;
import net.minecraft.block.PressurePlateBlock;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
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
    public ActionResult<ItemStack> onItemRightClick(World p_77659_1_, PlayerEntity p_77659_2_, Hand p_77659_3_) {
        World world = p_77659_1_;
        PlayerEntity player = p_77659_2_;
        Hand hand = p_77659_3_;
        ItemStack itemInHand = player.getHeldItem(hand);

        BlockPos pos = new BlockPos(player.getLookVec().getX(),player.getLookVec().getY(),player.getLookVec().getZ());

        if(itemInHand.hasTag())
        {
            if(itemInHand.getTag().contains(Reference.MODID + "Potion"))
            {
                EffectInstance effect = getPotionEffectFromStack(itemInHand);
                if(effect != null)
                {
                    if(player.getBlockState().getBlock() instanceof PressurePlateBlock)
                    {
                        world.setBlockState(pos, Blocks.AIR.getDefaultState());
                        if(player.getBlockState().getBlock().equals(Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE) || player.getBlockState().getBlock().equals(Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE))
                        {
                            world.setBlockState(pos, BlockTrap.BLOCKTRAPPLAYER.getDefaultState());
                        }
                        else
                        {
                            world.setBlockState(pos, BlockTrap.BLOCKTRAPMOB.getDefaultState());
                        }

                        TileEntity tileentity = world.getTileEntity(pos);
                        if (tileentity instanceof TileTrap) {
                            ((TileTrap) tileentity).setTrapEffect(effect);
                            player.getHeldItem(hand).shrink(1);
                        }
                    }
                    else
                    {
                        player.addPotionEffect(effect);
                        player.getHeldItem(hand).shrink(1);
                    }
                }
            }
        }



        return ActionResult.resultPass(itemInHand);
    }

    public EffectInstance getPotionEffectFromStack(ItemStack stack)
    {
        EffectInstance instance = null;
        if(stack.hasTag())
        {
            if(stack.getTag().contains(Reference.MODID + "Potion"))
            {
                CompoundNBT nbt = stack.getTag();

                instance = new EffectInstance(Effect.get(nbt.getInt(Reference.MODID + "Potion")),nbt.getInt(Reference.MODID + "Duration"),nbt.getInt(Reference.MODID + "Amplifier"));
            }
        }

        return instance;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);

        if(stack.hasTag())
        {
            if(stack.getTag().contains(Reference.MODID + "Potion"))
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
        }
        else
        {
            tooltip.add(new TranslationTextComponent("Doesnt function if grabbed from cheat mode"));
        }
    }

    public static final Item SPELLSCROLL = new ItemSpellScroll().setRegistryName(new ResourceLocation(MODID, "scroll/effect"));

    @SubscribeEvent
    public static void onItemRegistryReady(RegistryEvent.Register<Item> event)
    {
        event.getRegistry().register(SPELLSCROLL);
    }


}
