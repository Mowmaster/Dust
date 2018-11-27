package com.mowmaster.dust.items;

import com.mowmaster.dust.research.GuiIndex;
import com.mowmaster.dust.references.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static com.mowmaster.dust.misc.DustyTab.DUSTTABS;


public class ItemGuideBook extends Item
{
    public ItemGuideBook(String unlocName, String registryName, int stackSize )
    {
        this.setUnlocalizedName(unlocName);
        this.setRegistryName(new ResourceLocation(Reference.MODID, registryName));
        this.maxStackSize = stackSize;
        this.setCreativeTab(DUSTTABS);
    }

    public ItemGuideBook(String unlocName,String registryName )
    {
        this( unlocName ,registryName ,1);
        // Calls the previous constructer and passes the needed stacksize paramater to the above method

    }


    @Override
    @SideOnly(Side.CLIENT)
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand){
        if(world.isRemote)
        {
            Minecraft.getMinecraft().displayGuiScreen(new GuiIndex());
        }

        return super.onItemRightClick(world,player,hand);
    }
}
