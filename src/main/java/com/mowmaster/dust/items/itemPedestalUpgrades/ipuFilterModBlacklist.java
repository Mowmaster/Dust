package com.mowmaster.dust.items.itemPedestalUpgrades;

import com.mowmaster.dust.references.Reference;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.IntStream;

import static com.mowmaster.dust.misc.DustyTab.DUSTTABS;

public class ipuFilterModBlacklist extends ipuBasicFilter
{

    public ipuFilterModBlacklist(String unlocName, String registryName)
    {
        this.setUnlocalizedName(unlocName);
        this.setRegistryName(new ResourceLocation(Reference.MODID, registryName));
        this.maxStackSize = 64;
        this.setCreativeTab(DUSTTABS);
        this.isFilter=true;
    }

    public void updateAction(int tick, World world, ItemStack itemInPedestal, ItemStack coinInPedestal, BlockPos pedestalPos)
    {

    }

    @Override
    public boolean canAcceptItem(World world, BlockPos posPedestal, ItemStack itemStackIn)
    {
        boolean returner = true;
        BlockPos posInventory = getPosOfBlockBelow(world, posPedestal, 1);

        if(world.getTileEntity(posInventory) !=null)
        {
            if(world.getTileEntity(posInventory).hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, getPedestalFacing(world, posPedestal)))
            {
                IItemHandler handler = (IItemHandler) world.getTileEntity(posInventory).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, getPedestalFacing(world, posPedestal));


                int range = handler.getSlots();
                for(int i=0;i<range;i++)
                {
                    ItemStack stackInSlot = handler.getStackInSlot(i);
                    //find a slot with items
                    ItemStack itemFromInv = ItemStack.EMPTY;

                    itemFromInv = IntStream.range(0,range)//Int Range
                            .mapToObj((handler)::getStackInSlot)//Function being applied to each interval
                            .filter(itemStack -> itemStack.getItem().getRegistryName().getResourceDomain()==itemStackIn.getItem().getRegistryName().getResourceDomain())
                            .findFirst().orElse(ItemStack.EMPTY);
                    System.out.println(itemFromInv.getDisplayName());

                    if(!itemFromInv.isEmpty())
                    {
                        returner = false;
                    }
                }
            }
        }

        return returner;
    }

    public void upgradeAction(World world, BlockPos posOfPedestal, ItemStack coinInPedestal)
    {

    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {

        tooltip.add(TextFormatting.GOLD + "Filter: Mod Blacklist");
    }

}
