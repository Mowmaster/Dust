package com.mowmaster.dust.items;

import com.mowmaster.dust.blocks.machines.BlockDustCloud;
import com.mowmaster.dust.blocks.treebits.BlockDustLog;
import com.mowmaster.dust.enums.CrystalItems;
import com.mowmaster.dust.references.Reference;
import com.mowmaster.dust.research.GuiResearchNote;
import com.mowmaster.dust.research.GuiWikiNotes;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import static com.mowmaster.dust.misc.DustyTab.DUSTTABS;


public class ItemWikiScroll extends Item
{
    private final ResourceLocation guiUtils = new ResourceLocation(Reference.MODID, "textures/guis/guiutils.png");
    String title = "";
    String contents = "";
    String author = "";
    ItemStack itemDisplay = ItemStack.EMPTY;

    public ItemWikiScroll(String unlocName)
    {
        this.setUnlocalizedName(unlocName);
        this.setRegistryName(new ResourceLocation(Reference.MODID, unlocName));
        this.setHasSubtypes(true);
        this.setCreativeTab(DUSTTABS);
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        for(int i = 0; i < CrystalItems.CountTypes.values().length; i++)
        {
            items.add(new ItemStack(this,1,i));
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        for(int i = 0; i <CrystalItems.CountTypes.values().length; i++)
        {
            if(stack.getItemDamage() == i)
            {
                return this.getUnlocalizedName() + "." + CrystalItems.CountTypes.values()[i].getName();
            }
            else {
                continue;
            }
        }
        return this.getUnlocalizedName() + "." + CrystalItems.CountTypes.ONE.getName();
    }


    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand){

        if(world.isRemote)
        {
            if(player.getHeldItem(hand).getItem().equals(ItemRegistry.wikiscroll))
            {
                switch (player.getHeldItem(hand).getMetadata())
                {
                    case 0:
                        title = "Dust Spells";
                        contents = "Dust can be dropped on the ground and ignited with flint and steel to make effects. " +
                                "Mixing differnt colors of dust can make differnt effects. " +
                                "White Dust augments good effects, Black dust corrupts effects and makes bad ones stronger.";
                        author = "- Player776";
                        itemDisplay = new ItemStack(ItemRegistry.dust);
                        Minecraft.getMinecraft().displayGuiScreen(new GuiWikiNotes(title,0xff00ff,guiUtils,35,1,1,126,20,contents,0x000000,60, author,0x0000ff,itemDisplay,75,200,2.0f,2.0f));
                        break;
                    default:
                        title = "Hello World";
                        contents = "Hello World???";
                        author = "- Mowmaster";
                        itemDisplay = new ItemStack(BlockDustLog.logwhite);
                        Minecraft.getMinecraft().displayGuiScreen(new GuiWikiNotes(title,0xff00ff,guiUtils,35,1,1,126,20,contents,0x000000,60, author,0x0000ff,itemDisplay,75,200,2.0f,2.0f));
                }
            }

        }

        return super.onItemRightClick(world,player,hand);
    }
}
