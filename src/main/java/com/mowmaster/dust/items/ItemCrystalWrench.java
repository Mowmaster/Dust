package com.mowmaster.dust.items;

import com.mowmaster.dust.blocks.BlockPedestal;
import com.mowmaster.dust.references.Reference;
import com.mowmaster.dust.tiles.TilePedestal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

import static com.mowmaster.dust.misc.DustyTab.DUSTTABS;


public class ItemCrystalWrench extends Item
{
    private static final BlockPos defaultPos = new BlockPos(0,-2000,0);
    public BlockPos storedPosition = defaultPos;
    public ItemCrystalWrench(String unlocName, String registryName)
    {
        this.setUnlocalizedName(unlocName);
        this.setRegistryName(new ResourceLocation(Reference.MODID, registryName));
        this.maxStackSize = 1;
        this.setCreativeTab(DUSTTABS);
    }

    int ticker=0;
    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (stack.hasTagCompound()) {
            this.getPosFromNBT(stack);
            BlockPos pos = this.getStoredPosition(stack);
            Random rand = new Random();

            int zmin = -8;
            int zmax = 8;
            int xmin = -8;
            int xmax = 8;
            int ymin = -8;
            int ymax = 8;


            if(storedPosition!=defaultPos)
            {
                if(isSelected)
                {
                    if(worldIn.isRemote)
                    {
                        ticker++;
                        if(ticker>30)
                        {

                            //Test to see what location is stored in the wrench System.out.println(this.getStoredPosition(stack));
                            for (int c = zmin; c <= zmax; c++) {
                                for (int a = xmin; a <= xmax; a++) {
                                    for (int b = ymin; b <= ymax; b++) {

                                        double d0 = (double) pos.getX() + 0.55D - (double) (rand.nextFloat() * 0.1F);
                                        double d1 = (double) pos.getY() + 0.55D - (double) (rand.nextFloat() * 0.1F);
                                        double d2 = (double) pos.getZ() + 0.55D - (double) (rand.nextFloat() * 0.1F);
                                        double d3 = (double) (0.4F - (rand.nextFloat() + rand.nextFloat()) * 0.4F);
                                        worldIn.spawnParticle(EnumParticleTypes.ENCHANTMENT_TABLE, pos.add(a,b,c).getX(),pos.add(a,b,c).getY(),pos.add(a,b,c).getZ(), rand.nextGaussian() * 0.005D, rand.nextGaussian() * 0.005D, rand.nextGaussian() * 0.005D, new int[0]);
                                    }
                                }
                            }

                            ticker=0;
                        }
                    }
                }
            }

        }
    }

    public BlockPos getStoredPosition(ItemStack getWrenchItem)
    {
        getPosFromNBT(getWrenchItem);
        return storedPosition;
    }


    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(!worldIn.isRemote)
        {
            IBlockState getBlockState = worldIn.getBlockState(pos);
            if(player.isSneaking())
            {
                if(getBlockState.getBlock() instanceof BlockPedestal)
                {
                    if(player.getHeldItem(hand).isItemEnchanted()==false)
                    {
                        //Gets Pedestal Clicked on Pos
                        this.storedPosition = pos;
                        //Writes to NBT
                        writePosToNBT(player.getHeldItem(hand));
                        //Applies effect to wrench in hand
                        if(player.getHeldItem(hand).getItem() instanceof ItemCrystalWrench)
                        {
                            player.getHeldItem(hand).addEnchantment(Enchantments.UNBREAKING,-1);
                        }
                    }
                    //If wrench has the compound stacks and has a position stored(is enchanted)
                    else if(player.getHeldItem(hand).hasTagCompound() && player.getHeldItem(hand).isItemEnchanted())
                    {
                        //Checks if clicked blocks is a Pedestal
                        if(worldIn.getBlockState(pos).getBlock() instanceof BlockPedestal)
                        {
                            //Checks Tile at location to make sure its a TilePedestal
                            TileEntity tileEntity = worldIn.getTileEntity(pos);
                            if (tileEntity instanceof TilePedestal) {
                                TilePedestal tilePedestal = (TilePedestal) tileEntity;

                                //checks if connecting pedestal is out of range of the senderPedestal
                                if(tilePedestal.isPedestalInRange(getStoredPosition(player.getHeldItem(hand))))
                                {
                                    //Checks if pedestals to be linked are on same networks or if one is neutral
                                    if(tilePedestal.canLinkToPedestalNetwork(getStoredPosition(player.getHeldItem(hand))))
                                    {
                                        //If stored location isnt the same as the connecting pedestal
                                        if(!tilePedestal.isSamePedestal(getStoredPosition(player.getHeldItem(hand))))
                                        {
                                            //Checks if the conenction hasnt been made once already yet
                                            if(!tilePedestal.hasConnectionAlready(getStoredPosition(player.getHeldItem(hand))))
                                            {
                                                //Checks if senderPedestal has locationSlots available
                                                if(tilePedestal.addOutputLocation(getStoredPosition(player.getHeldItem(hand))))
                                                {
                                                    //If slots are available then set wrench properties back to a default value
                                                    this.storedPosition = defaultPos;
                                                    writePosToNBT(player.getHeldItem(hand));
                                                    worldIn.notifyBlockUpdate(pos,worldIn.getBlockState(pos),worldIn.getBlockState(pos),2);
                                                    if(player.getHeldItem(hand).getItem() instanceof ItemCrystalWrench)
                                                    {
                                                        if(player.getHeldItem(hand).isItemEnchanted())
                                                        {
                                                            player.getHeldItem(hand).removeSubCompound("ench");
                                                        }
                                                    }
                                                    player.sendMessage(new TextComponentString(TextFormatting.WHITE + "Link Successful"));
                                                }
                                                else player.sendMessage(new TextComponentString(TextFormatting.WHITE + "Link Unsuccessful"));
                                            }
                                            else player.sendMessage(new TextComponentString(TextFormatting.WHITE + "Connection Already Exists"));
                                        }
                                        else player.sendMessage(new TextComponentString(TextFormatting.WHITE + "Cannot be Linked to Itsself"));
                                    }
                                    else player.sendMessage(new TextComponentString(TextFormatting.WHITE + "Cannot Connect to this Network"));
                                }
                                else player.sendMessage(new TextComponentString(TextFormatting.WHITE + "Too Far to Connect"));

                            }
                        }
                    }
                }
                else
                {
                    this.storedPosition = defaultPos;
                    writePosToNBT(player.getHeldItem(hand));
                    worldIn.notifyBlockUpdate(pos,worldIn.getBlockState(pos),worldIn.getBlockState(pos),2);
                    if(player.getHeldItem(hand).getItem() instanceof ItemCrystalWrench)
                    {
                        if(player.getHeldItem(hand).isItemEnchanted())
                        {
                            player.getHeldItem(hand).removeSubCompound("ench");
                        }
                    }
                }

            }
        }
        return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }

    public void writePosToNBT(ItemStack stack)
    {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setInteger("stored_x",this.storedPosition.getX());
        compound.setInteger("stored_y",this.storedPosition.getY());
        compound.setInteger("stored_z",this.storedPosition.getZ());
        stack.setTagCompound(compound);
    }

    public void getPosFromNBT(ItemStack stack)
    {
        if(stack.hasTagCompound())
        {
            NBTTagCompound getCompound = stack.getTagCompound();
            int x = getCompound.getInteger("stored_x");
            int y = getCompound.getInteger("stored_y");
            int z = getCompound.getInteger("stored_z");
            this.storedPosition = new BlockPos(x,y,z);
        }

    }

    /*
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);
        if(!worldIn.isRemote) {
            if (!playerIn.isSneaking()) {

                getPosFromNBT(stack);
            }
        }
        return ActionResult.newResult(EnumActionResult.PASS,playerIn.getHeldItem(handIn));
    }
     */



    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {

        if(stack.getItem() instanceof ItemCrystalWrench) {
            if (stack.hasTagCompound()) {
                if (stack.isItemEnchanted()) {
                    tooltip.add("Selected Block = X:" + this.getStoredPosition(stack).getX() + " Y:" + this.getStoredPosition(stack).getY() + " Z:" + this.getStoredPosition(stack).getZ());
                } else tooltip.add("No Block Location Stored");
            } else tooltip.add("No Block Location Stored");
        }
    }

}