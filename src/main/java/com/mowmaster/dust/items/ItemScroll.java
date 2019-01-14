package com.mowmaster.dust.items;

import com.mowmaster.dust.enums.WikiNoteTypes;
import com.mowmaster.dust.references.Reference;
import com.mowmaster.dust.research.GuiResearchNote;
import com.mowmaster.dust.research.GuiWikiNotes;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


import java.util.List;

//import static com.mowmaster.dust.misc.AchievementHandler.achievementScrollA;
import static com.mowmaster.dust.misc.DustyTab.DUSTTABS;


public class ItemScroll extends Item
{
    String contents = "";

    public ItemScroll(String unlocName, String registryName)
    {
        this.setUnlocalizedName(unlocName);
        this.setRegistryName(new ResourceLocation(Reference.MODID, registryName));
        this.maxStackSize = 64;
        this.setCreativeTab(DUSTTABS);
    }

    private String[] getScrollsObtained = new String[26];
    //public BlockPos[] storedOutputLocations = {defaultPos,defaultPos,defaultPos,defaultPos,defaultPos,defaultPos,defaultPos,defaultPos};
    private String[] getContents(EntityPlayer player)
    {
        int counter = 0;
        String letter = "";
        for(int i=0;i<player.getInventoryEnderChest().getSizeInventory();i++)
        {
            if(player.getInventoryEnderChest().getStackInSlot(i).getItem() instanceof ItemScroll)
            {
                getScrollsObtained[counter] = player.getInventoryEnderChest().getStackInSlot(i).getItem().getRegistryName().toString().substring(11);
                counter++;
            }
        }


        return getScrollsObtained;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand){

        String[] stringy = getContents(player);
        if(world.isRemote)
        {
            if(player.getHeldItem(hand).getItem().equals(ItemRegistry.scroll))
            {
                contents = "Hello World";
                Minecraft.getMinecraft().displayGuiScreen(new GuiResearchNote(contents.toLowerCase(),stringy));
            }
            else if(player.getHeldItem(hand).getItem().equals(ItemRegistry.scrollA))
            {
                Minecraft.getMinecraft().displayGuiScreen(new GuiWikiNotes(WikiNoteTypes.DUSTSPELLSBASIC));
            }
        }

        return super.onItemRightClick(world,player,hand);
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
    {
        tooltip.add("[WIP] Will randomly give you a letter of the Ancient Alphabet.");
        tooltip.add("Not needed till next beta release(will also be reworked)");
    }

    //Use NBT to define which one and set item resources based on that, maybe also apply to coins...


}


