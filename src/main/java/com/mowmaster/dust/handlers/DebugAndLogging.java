package com.mowmaster.dust.handlers;

import com.mowmaster.dust.blocks.machines.BlockPedestal;
import com.mowmaster.dust.items.ItemRegistry;
import com.mowmaster.dust.items.ItemWikiScroll;
import com.mowmaster.dust.items.itemPedestalUpgrades.ipuBasic;
import com.mowmaster.dust.tiles.TilePedestal;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTippedArrow;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static com.mowmaster.dust.items.ItemRegistry.akashic;
import static com.mowmaster.dust.items.ItemRegistry.wikiscroll;


public class DebugAndLogging
{
    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onItemRightClick(PlayerInteractEvent.RightClickItem event) {
        World worldIn = event.getWorld();
        EntityPlayer playerIn = event.getEntityPlayer();
        BlockPos blockAtFeetLocation = event.getPos();
        //BlockPos pos = playerIn.getPosition();
        //EnumHand hand = event.getHand();
        //IBlockState state = worldIn.getBlockState(event.getPos());
        //ItemStack item = playerIn.getHeldItem(EnumHand.MAIN_HAND);

        if(!worldIn.isRemote)
        {
            if(playerIn.getHeldItemOffhand().getItem().equals(akashic))
            {
                if(playerIn.isSneaking())
                {
                    //System.out.println("Block At Event Location: " + worldIn.getBlockState(blockAtFeetLocation).getBlock().getUnlocalizedName());
                    //System.out.println("POS At Event Location: " + blockAtFeetLocation);
                }
                else
                {
                    if(playerIn.getHeldItemMainhand() != ItemStack.EMPTY)
                    {
                        ItemStack stackMain = playerIn.getHeldItemMainhand();
                        if(stackMain.hasTagCompound())
                        {
                            System.out.println(stackMain.getTagCompound().toString());
                        }
                    }
                }
            }
            else if (playerIn.getHeldItemMainhand().getItem().equals(akashic))
            {
                if(playerIn.isSneaking())
                {

                }
                else if (!(FurnaceRecipes.instance().getSmeltingResult(playerIn.getHeldItemOffhand()).equals(ItemStack.EMPTY)))
                {
                    System.out.println(FurnaceRecipes.instance().getSmeltingResult(playerIn.getHeldItemOffhand()).getCount());
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onItemRightClick(PlayerInteractEvent.RightClickBlock event) {
        World worldIn = event.getWorld();
        EntityPlayer playerIn = event.getEntityPlayer();
        BlockPos blockClickedOn = event.getPos();

        if(!worldIn.isRemote)
        {
            if(playerIn.getHeldItemOffhand().getItem().equals(akashic))
            {
                if(playerIn.isSneaking())
                {
                    //System.out.println("Block At Event Location: " + worldIn.getBlockState(blockClickedOn).getBlock().getUnlocalizedName());
                    //System.out.println("POS At Event Location: " + blockClickedOn);
                    if(worldIn.getBlockState(blockClickedOn) instanceof BlockPedestal)
                    {
                        TileEntity tileEntity = worldIn.getTileEntity(blockClickedOn);

                        if(tileEntity instanceof TilePedestal)
                        {
                            TilePedestal getTilePedestal = (TilePedestal)tileEntity;
                            //System.out.println("Transfer Speed: " + getTilePedestal.getOperationSpeed()+" ticks per operation");
                            //System.out.println("Transfer Amount: " + getTilePedestal.getItemTransferRate());
                            System.out.println("Transfer Speed: " + getTilePedestal.getPedestalTransferSpeed());
                            System.out.println("Transfer Amount: " + getTilePedestal.getPedestalTransferAmount());
                        }
                    }
                }
            }
            else if (playerIn.getHeldItemMainhand().getItem().equals(akashic))
            {
                //System.out.println("MainHand");
                if(playerIn.isSneaking())
                {
                    //System.out.println("Main Sneaking");
                    if(worldIn.getBlockState(blockClickedOn).getBlock() instanceof BlockPedestal)
                    {
                        //System.out.println("Main Snek BlockPed");
                        TileEntity tileEntity = worldIn.getTileEntity(blockClickedOn);

                        if(tileEntity instanceof TilePedestal)
                        {
                            //System.out.println("Main Snek TilePed");
                            TilePedestal getTilePedestal = (TilePedestal)tileEntity;

                            if(playerIn.getHeldItemOffhand().getItem().equals(ItemRegistry.charcoalRed))
                            {
                                System.out.println("Fuel Inside: " + getTilePedestal.getStoredValueForUpgrades());
                            }
                            else if(playerIn.getHeldItemOffhand().getItem().equals(Items.GLASS_BOTTLE))
                            {
                                System.out.println("Exp Inside: " + getTilePedestal.getStoredValueForUpgrades());
                            }

                            if(playerIn.getHeldItemOffhand().getItem().equals(ItemRegistry.crystal))
                            {
                                System.out.println("Number of Positions: " + getTilePedestal.getNumberOfStoredLocations());
                                System.out.println("List Of Positions: " + getTilePedestal.debugLocationList());
                                System.out.println("Transfer Speed: " + getTilePedestal.getOperationSpeed());
                                System.out.println("Transfer Amount: " + getTilePedestal.getItemTransferRate());
                            }
                        }
                    }
                }
            }
        }
    }
}
