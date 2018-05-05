package com.mowmaster.dust.blocks;

import com.mowmaster.dust.items.ItemRegistry;
import com.mowmaster.dust.references.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;
import static com.mowmaster.dust.misc.DustyTab.DUSTBLOCKSTABS;


public class BlockMachineBase extends BlockFalling //Temporarily BlockFalling until i get to making machines
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
        this.setSoundType(SoundType.ANVIL);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random random, int fortune)
    {
        return null;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        /*
        if (!playerIn.hasAchievement(AchievementHandler.achievementFindMachine))
        {
            playerIn.addStat(AchievementHandler.achievementFindMachine);
        }
*/

        if((playerIn.getHeldItem(hand) != null))
        {
            if(ItemStack.areItemsEqual(playerIn.getHeldItem(hand), new ItemStack(ItemRegistry.crushingComponents))) {
                    //playerIn.sendMessage(new TextComponentString("You are adding Carbon"));
                    playerIn.getHeldItem(hand).shrink(1);
                    worldIn.setBlockState(pos, Blocks.BEDROCK.getDefaultState());
            }

        }
        return true;
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
    {
        tooltip.add("[WIP] Mark this block on map for later.");
        tooltip.add("It will turn into a machine in next beta release.");
    }
}
