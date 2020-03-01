package com.mowmaster.dust.blocks.buildingblocks;

import com.mowmaster.dust.blocks.blockbasics.BlockBasic;
import com.mowmaster.dust.blocks.machines.BlockVoidPot;
import com.mowmaster.dust.references.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Random;
import static com.mowmaster.dust.misc.DustyTab.DUSTBLOCKSTABS;

public class BlockPot extends BlockBasic
{
    public static Block pot1;
    private static AxisAlignedBB potOne = new AxisAlignedBB(0.125D, 0.0D, 0.125D, 0.875D, 1.0D, 0.875D);

    public BlockPot(String unloc, String registryName)
    {
        super(Material.ROCK);
        this.setUnlocalizedName(unloc);
        this.setRegistryName(new ResourceLocation(Reference.MODID, registryName));
        this.setCreativeTab(DUSTBLOCKSTABS);
        this.setSoundType(SoundType.STONE);
    }

    @Override
    public Item getItemDropped(IBlockState state,Random random,int fortune)
    {
        return null;
    }

    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }

    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return potOne;
    }

    @Override
    public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {
        Random rn = new Random();
        if(!worldIn.isRemote)
        {
            if(this.equals(pot1))
            {
                int rand = rn.nextInt(6);

                if(rand == 0)
                {
                    worldIn.setBlockState(pos, Blocks.DIAMOND_ORE.getDefaultState());
                }
                else if (rand == 1)
                {
                    worldIn.setBlockState(pos, Blocks.GOLD_ORE.getDefaultState());
                }
                else if (rand == 2)
                {
                    worldIn.setBlockState(pos, Blocks.COAL_BLOCK.getDefaultState());
                }
                else if (rand == 3)
                {
                    worldIn.setBlockState(pos, Blocks.LAPIS_ORE.getDefaultState());
                }
                else if (rand == 4)
                {
                    worldIn.setBlockState(pos, Blocks.EMERALD_ORE.getDefaultState());
                }
                else if (rand == 5)
                {
                    worldIn.setBlockState(pos, BlockVoidPot.voidpot.getDefaultState());
                }
            }
        }
    }
/*
    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
        if (!player.hasAchievement(AchievementHandler.achievementLoot))
        {
            player.addStat(AchievementHandler.achievementLoot);
        }
    }
*/
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
    {
        tooltip.add("[WIP] Will Eventually Drop Loot based on players current level");
    }


    public static void Init()
    {
        pot1 = new BlockPot("pot1","pot1");
    }

    public static void Register()
    {
        registerBlock(pot1);
    }

    public static void RegisterRender()
    {
        registerRender(pot1);
    }
}
