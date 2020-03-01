package com.mowmaster.dust.items;

import com.mowmaster.dust.references.Reference;
import com.mowmaster.dust.research.GuiWikiNotes;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
            if(bookList.hasKey("booklist"))
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
                title = "[WIP]";
                contents = "If you need In Game Help, Use the Advancements to plot your way through the mod (plus earn rewards). Otherwise visit the wiki on the github: https://github.com/Mowmaster/Dust/wiki";
                author = "~Player235";
                itemDisplay = new ItemStack(ItemRegistry.finnisher);
                Minecraft.getMinecraft().displayGuiScreen(new GuiWikiNotes(title,0x000000,guiUtils,35,1,1,126,20,contents,0x000000,60, author,0x0000ff,itemDisplay,75,210,2.0f,2.0f));
                /*if(!player.isSneaking())
                {
                    if(hasBookList(player.getHeldItemMainhand()))
                    {
                        NBTTagCompound bookList = player.getHeldItemMainhand().getTagCompound();
                        if(bookList.hasKey("booklist"))
                        {
                            String strOfEntries = bookList.getString("booklist");
                            List<String> items = Arrays.asList(strOfEntries.split("\\s*,\\s*"));

                            title = bookList.getString(items.get(0)+"Title");
                            contents = bookList.getString(items.get(0)+"Entry");
                            author = "";
                            itemDisplay.deserializeNBT(bookList.getCompoundTag(items.get(0)+"Icon"));
                            Minecraft.getMinecraft().displayGuiScreen(new GuiWikiNotes(title,0x000000,guiUtils,35,1,1,126,20,contents,0x000000,60, author,0x0000ff,itemDisplay,75,210,2.0f,2.0f));

                        }
                    }
                }*/
            }
        }

        return super.onItemRightClick(world,player,hand);
    }

    /*@Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(!worldIn.isRemote)
        {
            IBlockState getBlockState = worldIn.getBlockState(pos);
            if(player.isSneaking())
            {
                if(getBlockState.getBlock().getRegistryName().getResourceDomain().equals(Reference.MODID))
                {

                    if(getBlockState.getBlock() instanceof BlockBasic) {
                        BlockBasic getBlock = (BlockBasic) getBlockState.getBlock();

                        boolean hasMethod = false;
                        Method[] methods = getBlock.getClass().getMethods();

                        for (Method m : methods) {
                            if (m.getName().equals("hasBookEntry")) {
                                hasMethod = true;
                                break;
                            }
                        }

                        if(hasMethod)
                        {
                            switch(getBlock.hasBookEntry(player.getHeldItemMainhand()))
                            {
                                case -1:
                                    player.sendMessage(new TextComponentString(TextFormatting.WHITE + "There is no info on this block"));
                                    break;
                                case 0:
                                    getBlock.addBookEntry(player.getHeldItemMainhand());
                                    player.sendMessage(new TextComponentString(TextFormatting.WHITE + "New Entry Added"));
                                    break;
                                case 1:
                                    player.sendMessage(new TextComponentString(TextFormatting.WHITE + "Entry Already Exists"));
                                    break;
                                default:
                                    player.sendMessage(new TextComponentString(TextFormatting.WHITE + "There is no info on this block"));
                                    break;
                            }
                        }
                    }
                }
            }
        }
        return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }*/
}
