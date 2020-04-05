package com.mowmaster.dust.items;

import com.mowmaster.dust.blocks.machines.BlockTrap;
import com.mowmaster.dust.references.Reference;
import com.mowmaster.dust.tiles.TileTrapBlock;
import net.minecraft.block.BlockBasePressurePlate;
import net.minecraft.block.BlockPressurePlate;
import net.minecraft.block.BlockPressurePlateWeighted;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

import java.util.List;

import static com.mowmaster.dust.misc.DustyTab.DUSTTABS;


public class ItemSpellScroll extends Item
{
    PotionEffect potionEffect = new PotionEffect(MobEffects.LUCK);
    public ItemSpellScroll(String unlocName, String registryName)
    {
        this.setUnlocalizedName(unlocName);
        this.setRegistryName(new ResourceLocation(Reference.MODID, registryName));
        this.maxStackSize = 64;
        //this.setCreativeTab(DUSTTABS);
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (stack.hasTagCompound()) {
            potionEffect = getPotionEffectFromStack(stack);
            String s1 = I18n.translateToLocal(potionEffect.getEffectName()).trim();
            stack.setStackDisplayName("Scroll of " + s1);
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);
        if (stack.hasTagCompound()) {
            playerIn.addPotionEffect(getPotionEffectFromStack(stack));
            playerIn.getHeldItemMainhand().shrink(1);
            worldIn.playSound(playerIn, playerIn.getPosition(), SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, itemRand.nextFloat() * 0.4F + 0.8F);
            worldIn.playSound(playerIn, playerIn.getPosition(), SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 1.0F, itemRand.nextFloat() * 0.4F + 0.8F);
        }
        return ActionResult.newResult(EnumActionResult.PASS,playerIn.getHeldItem(handIn));
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.getHeldItem(hand);
        if(stack.getItem() instanceof ItemSpellScroll)
        {
            IBlockState state = worldIn.getBlockState(pos);
            if(state.getBlock() instanceof BlockBasePressurePlate)
            {
                if(state.getBlock() instanceof BlockPressurePlateWeighted)
                {
                    player.getHeldItem(hand).shrink(1);
                    worldIn.setBlockToAir(pos);
                    worldIn.setBlockState(pos, BlockTrap.blockTrap.getDefaultState());
                    TileEntity tileentity = worldIn.getTileEntity(pos);
                    if (tileentity instanceof TileTrapBlock) {
                        ((TileTrapBlock) tileentity).setTrapEffect(getPotionEffectFromStack(stack));
                    }
                    worldIn.playSound(player, player.getPosition(), SoundEvents.BLOCK_GLASS_PLACE, SoundCategory.BLOCKS, 1.0F, itemRand.nextFloat() * 0.4F + 0.8F);
                }
                else if(state.getBlock() instanceof BlockPressurePlate)
                {
                    player.getHeldItem(hand).shrink(1);
                    worldIn.setBlockToAir(pos);
                    worldIn.setBlockState(pos, BlockTrap.blockTrapMob.getDefaultState());
                    TileEntity tileentity = worldIn.getTileEntity(pos);
                    if (tileentity instanceof TileTrapBlock) {
                        ((TileTrapBlock) tileentity).setTrapEffect(getPotionEffectFromStack(stack));
                    }
                    worldIn.playSound(player, player.getPosition(), SoundEvents.BLOCK_GLASS_PLACE, SoundCategory.BLOCKS, 1.0F, itemRand.nextFloat() * 0.4F + 0.8F);
                }
                else
                {
                    return EnumActionResult.FAIL;
                }
            }
            else
            {
                onItemRightClick(worldIn,player,hand);
            }
        }

        return EnumActionResult.SUCCESS;
    }

    public PotionEffect getPotionEffectFromStack(ItemStack stack)
    {
        if(stack.hasTagCompound())
        {
            NBTTagCompound getCompound = stack.getTagCompound();
            potionEffect = PotionEffect.readCustomPotionEffectFromNBT(stack.getTagCompound().getCompoundTag("scrolleffect"));
        }
        return potionEffect;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {

        if (stack.hasTagCompound())
        {
            String s1 = I18n.translateToLocal(getPotionEffectFromStack(stack).getEffectName()).trim();
            int s2 = getPotionEffectFromStack(stack).getAmplifier();
            int s3 = getPotionEffectFromStack(stack).getDuration();
            String count = "";
            switch (getPotionEffectFromStack(stack).getAmplifier())
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
            tooltip.add("Potency: " + count);
            tooltip.add("Duration: " + s3 / 20 + " seconds");
        }
        else tooltip.add("Doesnt function if grabbed from cheat mode");
    }





}


