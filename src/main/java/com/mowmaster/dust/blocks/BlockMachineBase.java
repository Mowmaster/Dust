package com.mowmaster.dust.blocks;

import com.mowmaster.dust.items.ItemRegistry;
import com.mowmaster.dust.references.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.Random;

import static com.mowmaster.dust.misc.DustyBlockTab.DUSTBLOCKSTABS;
import static com.mowmaster.dust.misc.DustyTab.DUSTTABS;


public class BlockMachineBase extends Block
{
    public BlockMachineBase(String unloc, String registryName)
    {
        super(Material.ROCK);
        this.setUnlocalizedName(unloc);
        this.setRegistryName(new ResourceLocation(Reference.MODID, registryName));
        this.setHardness(99);
        this.setResistance(99);
        this.setLightOpacity(10);
        this.setCreativeTab(DUSTBLOCKSTABS);
        this.useNeighborBrightness = true;
    }

    @Override
    public Item getItemDropped(IBlockState state, Random random, int fortune)
    {
        return null;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

        if((playerIn.getHeldItem(hand) != null))
        {
            if(ItemStack.areItemsEqual(playerIn.getHeldItem(hand), new ItemStack(ItemRegistry.crushingcomponents))) {
                    //playerIn.sendMessage(new TextComponentString("You are adding Carbon"));
                    playerIn.getHeldItem(hand).shrink(1);
                    worldIn.setBlockState(pos, Blocks.BEDROCK.getDefaultState());
            }

        }
        return true;
    }
}
