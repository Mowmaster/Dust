package com.mowmaster.dust.items;

import com.mowmaster.dust.blocks.machines.BlockCrystalFurnace;
import com.mowmaster.dust.blocks.machines.BlockPedestal;
import com.mowmaster.dust.blocks.machines.BlockVoidPot;
import com.mowmaster.dust.blocks.treebits.SaplingRegister;
import com.mowmaster.dust.blocks.utility.BlockPath;
import com.mowmaster.dust.blocks.utility.BlockSpike;
import com.mowmaster.dust.research.GuiIndex;
import com.mowmaster.dust.references.Reference;
import com.mowmaster.dust.research.GuiWikiNotes;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Arrays;
import java.util.List;

import static com.mowmaster.dust.misc.DustyTab.DUSTTABS;


public class ItemGuideBook extends Item
{
    private final ResourceLocation guiUtils = new ResourceLocation(Reference.MODID, "textures/guis/guiutils.png");
    String title = "";
    String contents = "";
    String author = "";
    ItemStack itemDisplay = ItemStack.EMPTY;

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

    public boolean hasBookList(ItemStack getBook)
    {
        boolean returner = false;

        if(getBook.hasTagCompound())
        {
            NBTTagCompound bookList = getBook.getTagCompound();
            if(bookList.hasKey("bookList"))
            {
                returner = true;
            }
        }

        return  returner;
    }


    @Override
    @SideOnly(Side.CLIENT)
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand){

        if(world.isRemote)
        {
            if(player.getHeldItemMainhand().getItem().equals(ItemRegistry.tomeGuideBook))
            {
                if(hasBookList(player.getHeldItemMainhand()))
                {
                    NBTTagCompound bookList = player.getHeldItemMainhand().getTagCompound();
                    if(bookList.hasKey("bookList"))
                    {
                        String strOfEntries = bookList.getString("bookList");
                        List<String> items = Arrays.asList(strOfEntries.split("\\s*,\\s*"));

                        title = items.get(0)+"Title";
                        contents = items.get(0)+"Entry";
                        author = "";
                        itemDisplay.deserializeNBT(bookList.getCompoundTag(items.get(0)+"Icon"));
                        Minecraft.getMinecraft().displayGuiScreen(new GuiWikiNotes(title,0x000000,guiUtils,35,1,1,126,20,contents,0x000000,60, author,0x0000ff,itemDisplay,75,210,2.0f,2.0f));

                    }
                }
            }
        }

        return super.onItemRightClick(world,player,hand);
    }
}
