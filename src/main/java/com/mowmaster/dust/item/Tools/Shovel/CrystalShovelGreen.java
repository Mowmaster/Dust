// This is not a normal Shovel...

package com.mowmaster.dust.item.Tools.Shovel;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.mowmaster.dust.init.dustCreativeTabs;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import java.util.Set;

// Need to use the most "complex" tool for the combo, because you have to setup harvestable blocks later
public class CrystalShovelGreen extends ItemSpade {

    public CrystalShovelGreen(ToolMaterial material){

        super(material);


    }

    public CrystalShovelGreen(String crystalshovel_green, ToolMaterial material){
        super(material);
        this.setUnlocalizedName(crystalshovel_green);
        this.setCreativeTab(dustCreativeTabs.dustTool);
    }

    @Override
    public Set<String> getToolClasses(ItemStack stack) {
        return ImmutableSet.of("spade", "hoe");
    }

    private static Set effectiveAgainst = Sets.newHashSet(new Block []
            {
                    Blocks.grass, Blocks.dirt, Blocks.sand, Blocks.gravel,
                    Blocks.snow_layer, Blocks.snow, Blocks.clay, Blocks.farmland,
                    Blocks.soul_sand, Blocks.mycelium
            });

    public boolean canHarvestBlock(Block block)
    {
        return effectiveAgainst.contains(block) ? true : super.canHarvestBlock(block);
    }


    public float getStrBlock(ItemStack stack, Block block)
    {
        return effectiveAgainst.contains(block) ? this.efficiencyOnProperMaterial : super.getStrVsBlock(stack, block);
    }

// Hoe Stuff, this is a bit crazy me thinks, do I understand how this works? Nope! But it does :)

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!player.canPlayerEdit(pos.offset(side), side, stack)) {
            return false;
        } else {
            int hook = net.minecraftforge.event.ForgeEventFactory.onHoeUse(stack, player, world, pos);
            if (hook != 0)
                return hook > 0;

            IBlockState iblockstate = world.getBlockState(pos);
            Block block = iblockstate.getBlock();

            if (side != EnumFacing.DOWN && world.isAirBlock(pos.up())) {
                if (block == Blocks.grass) {
                    return this.useHoe(stack, player, world, pos, Blocks.farmland.getDefaultState());
                }

                if (block == Blocks.dirt) {
                    switch (SwitchDirtType.TYPE_LOOKUP[((BlockDirt.DirtType) iblockstate.getValue(BlockDirt.VARIANT)).ordinal()]) {
                        case 1:
                            return this.useHoe(stack, player, world, pos, Blocks.farmland.getDefaultState());
                        case 2:
                            return this.useHoe(stack, player, world, pos, Blocks.dirt.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.DIRT));
                    }
                }
            }

            return false;
        }
    }

    protected boolean useHoe(ItemStack stack, EntityPlayer player, World worldIn, BlockPos target, IBlockState newState) {
        worldIn.playSoundEffect(target.getX() + 0.5F, target.getY() + 0.5F, target.getZ() + 0.5F, newState.getBlock().stepSound.getStepSound(), (newState.getBlock().stepSound.getVolume() + 1.0F) / 2.0F, newState.getBlock().stepSound.getFrequency() * 0.8F);

        if (worldIn.isRemote) {
            return true;
        } else {
            worldIn.setBlockState(target, newState);
            stack.damageItem(1, player);
            return true;
        }
    }

    static final class SwitchDirtType {

        static final int[] TYPE_LOOKUP = new int[BlockDirt.DirtType.values().length];

        static {
            try {
                TYPE_LOOKUP[BlockDirt.DirtType.DIRT.ordinal()] = 1;
            } catch (NoSuchFieldError var2) {
                ;
            }

            try {
                TYPE_LOOKUP[BlockDirt.DirtType.COARSE_DIRT.ordinal()] = 2;
            } catch (NoSuchFieldError var1) {
                ;
            }
        }
    }

}
