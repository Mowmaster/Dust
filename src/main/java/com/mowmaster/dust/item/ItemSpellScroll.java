package com.mowmaster.dust.item;

import com.mowmaster.dust.blocks.BlockTrap;
import com.mowmaster.dust.dust;
import com.mowmaster.dust.references.Reference;
import com.mowmaster.dust.tiles.TileTrap;
import net.minecraft.block.AbstractPressurePlateBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.PressurePlateBlock;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import javax.annotation.Nullable;
import java.util.List;

import static com.mowmaster.dust.blocks.BlockTrap.WATERLOGGED;
import static com.mowmaster.dust.references.Reference.MODID;


public class ItemSpellScroll extends Item
{
    public ItemSpellScroll()
    {
        super(new Item.Properties().maxStackSize(64).group(dust.ITEM_GROUP));
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        World world = context.getWorld();
        BlockPos pos = context.getPos();
        PlayerEntity player = context.getPlayer();
        Hand hand = context.getHand();
        BlockState state = world.getBlockState(pos);
        ActionResultType returner = ActionResultType.FAIL;

        if(!world.isRemote)
        {
            if(player.getHeldItem(hand).hasTag())
            {
                if(player.getHeldItem(hand).getTag().contains(Reference.MODID + "Potion"))
                {
                    EffectInstance effect = getPotionEffectFromStack(player.getHeldItem(hand));
                    if(effect != null)
                    {
                        player.sendMessage(new StringTextComponent(TextFormatting.LIGHT_PURPLE +""+state.getBlock()));
                        if(state.getBlock() instanceof AbstractPressurePlateBlock)
                        {
                            if(state.getBlock().equals(Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE) || state.getBlock().equals(Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE))
                            {

                                if(player.isInWater())
                                {
                                    world.setBlockState(pos, BlockTrap.BLOCKTRAPPLAYER.getDefaultState().with(WATERLOGGED,true));
                                }
                                else world.setBlockState(pos, BlockTrap.BLOCKTRAPPLAYER.getDefaultState());

                                TileEntity tileentity = world.getTileEntity(pos);
                                if (tileentity instanceof TileTrap) {
                                    ((TileTrap) tileentity).setTrapEffect(effect);
                                    world.playSound((PlayerEntity) null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_SNOW_PLACE, SoundCategory.BLOCKS, 1.0F, 1.0F);
                                    player.getHeldItem(hand).shrink(1);
                                    returner = ActionResultType.PASS;
                                }
                            }
                            else
                            {
                                if(player.isInWater())
                                {
                                    world.setBlockState(pos, BlockTrap.BLOCKTRAPMOB.getDefaultState().with(WATERLOGGED,true));
                                }
                                else world.setBlockState(pos, BlockTrap.BLOCKTRAPMOB.getDefaultState());

                                TileEntity tileentity = world.getTileEntity(pos);
                                if (tileentity instanceof TileTrap) {
                                    ((TileTrap) tileentity).setTrapEffect(effect);
                                    world.playSound((PlayerEntity) null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_SNOW_PLACE, SoundCategory.BLOCKS, 1.0F, 1.0F);
                                    player.getHeldItem(hand).shrink(1);
                                    returner = ActionResultType.PASS;
                                }
                            }
                        }
                        /*else
                        {
                            player.addPotionEffect(effect);
                            world.playSound((PlayerEntity) null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 1.0F, 1.0F);
                            player.getHeldItem(hand).shrink(1);
                        }*/
                    }
                }
            }
        }

        return returner;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World p_77659_1_, PlayerEntity p_77659_2_, Hand p_77659_3_) {
        World world = p_77659_1_;
        PlayerEntity player = p_77659_2_;
        BlockPos pos = player.getPosition();
        Hand hand = p_77659_3_;
        ItemStack itemInHand = player.getHeldItem(hand);

        if(!world.isRemote)
        {
            if(itemInHand.hasTag())
            {
                if(itemInHand.getTag().contains(Reference.MODID + "Potion"))
                {
                    EffectInstance effect = getPotionEffectFromStack(itemInHand);
                    if(effect != null)
                    {
                        player.addPotionEffect(effect);
                        world.playSound((PlayerEntity) null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 1.0F, 1.0F);
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
                String s1 = effect.getPotion().getName();
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
