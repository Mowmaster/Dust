package com.mowmaster.dust.items;

import com.mowmaster.dust.misc.AchievementHandler;
import com.mowmaster.dust.references.Reference;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//import static com.mowmaster.dust.misc.AchievementHandler.achievementScrollA;
import static com.mowmaster.dust.misc.DustyTab.DUSTTABS;


public class ItemScroll extends Item
{
    public ItemScroll(String unlocName, String registryName)
    {
        this.setUnlocalizedName(unlocName);
        this.setRegistryName(new ResourceLocation(Reference.MODID, registryName));
        this.maxStackSize = 1;
        this.setCreativeTab(DUSTTABS);
    }
    /*
    //Make way to increese stacksize or on item pickup to paper if player has all notes, or alt to give research notes if player has more
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        if (!playerIn.capabilities.isCreativeMode){itemstack.shrink(1);}

        worldIn.playSound((EntityPlayer)null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.BLOCK_CLOTH_HIT, SoundCategory.NEUTRAL, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
        playerIn.getCooldownTracker().setCooldown(this, 10);//Cool down before using next item/attack


        if(playerIn.hasAchievement(AchievementHandler.achievementScrollAll))
        {
            playerIn.setHeldItem(handIn, new ItemStack(Items.PAPER,1));
            return new ActionResult(EnumActionResult.SUCCESS, itemstack);
        }
        else {

            if (!playerIn.hasAchievement(AchievementHandler.achievementScrollFirst)) {
                playerIn.addStat(AchievementHandler.achievementScrollFirst);
            }

            ArrayList<String> ScrollsDiscovered = new ArrayList<>();
            ScrollsDiscovered.add("achievementScrollA");
            ScrollsDiscovered.add("achievementScrollB");
            ScrollsDiscovered.add("achievementScrollC");
            ScrollsDiscovered.add("achievementScrollD");
            ScrollsDiscovered.add("achievementScrollE");
            ScrollsDiscovered.add("achievementScrollF");
            ScrollsDiscovered.add("achievementScrollG");
            ScrollsDiscovered.add("achievementScrollH");
            ScrollsDiscovered.add("achievementScrollI");
            ScrollsDiscovered.add("achievementScrollJ");
            ScrollsDiscovered.add("achievementScrollK");
            ScrollsDiscovered.add("achievementScrollL");
            ScrollsDiscovered.add("achievementScrollM");
            ScrollsDiscovered.add("achievementScrollN");
            ScrollsDiscovered.add("achievementScrollO");
            ScrollsDiscovered.add("achievementScrollP");
            ScrollsDiscovered.add("achievementScrollQ");
            ScrollsDiscovered.add("achievementScrollR");
            ScrollsDiscovered.add("achievementScrollS");
            ScrollsDiscovered.add("achievementScrollT");
            ScrollsDiscovered.add("achievementScrollU");
            ScrollsDiscovered.add("achievementScrollV");
            ScrollsDiscovered.add("achievementScrollW");
            ScrollsDiscovered.add("achievementScrollX");
            ScrollsDiscovered.add("achievementScrollY");
            ScrollsDiscovered.add("achievementScrollZ");
            if (playerIn.hasAchievement(AchievementHandler.achievementScrollA)) {ScrollsDiscovered.remove("achievementScrollA");}
            if (playerIn.hasAchievement(AchievementHandler.achievementScrollB)) {ScrollsDiscovered.remove("achievementScrollB");}
            if (playerIn.hasAchievement(AchievementHandler.achievementScrollC)) {ScrollsDiscovered.remove("achievementScrollC");}
            if (playerIn.hasAchievement(AchievementHandler.achievementScrollD)) {ScrollsDiscovered.remove("achievementScrollD");}
            if (playerIn.hasAchievement(AchievementHandler.achievementScrollE)) {ScrollsDiscovered.remove("achievementScrollE");}
            if (playerIn.hasAchievement(AchievementHandler.achievementScrollF)) {ScrollsDiscovered.remove("achievementScrollF");}
            if (playerIn.hasAchievement(AchievementHandler.achievementScrollG)) {ScrollsDiscovered.remove("achievementScrollG");}
            if (playerIn.hasAchievement(AchievementHandler.achievementScrollH)) {ScrollsDiscovered.remove("achievementScrollH");}
            if (playerIn.hasAchievement(AchievementHandler.achievementScrollI)) {ScrollsDiscovered.remove("achievementScrollI");}
            if (playerIn.hasAchievement(AchievementHandler.achievementScrollJ)) {ScrollsDiscovered.remove("achievementScrollJ");}
            if (playerIn.hasAchievement(AchievementHandler.achievementScrollK)) {ScrollsDiscovered.remove("achievementScrollK");}
            if (playerIn.hasAchievement(AchievementHandler.achievementScrollL)) {ScrollsDiscovered.remove("achievementScrollL");}
            if (playerIn.hasAchievement(AchievementHandler.achievementScrollM)) {ScrollsDiscovered.remove("achievementScrollM");}
            if (playerIn.hasAchievement(AchievementHandler.achievementScrollN)) {ScrollsDiscovered.remove("achievementScrollN");}
            if (playerIn.hasAchievement(AchievementHandler.achievementScrollO)) {ScrollsDiscovered.remove("achievementScrollO");}
            if (playerIn.hasAchievement(AchievementHandler.achievementScrollP)) {ScrollsDiscovered.remove("achievementScrollP");}
            if (playerIn.hasAchievement(AchievementHandler.achievementScrollQ)) {ScrollsDiscovered.remove("achievementScrollQ");}
            if (playerIn.hasAchievement(AchievementHandler.achievementScrollR)) {ScrollsDiscovered.remove("achievementScrollR");}
            if (playerIn.hasAchievement(AchievementHandler.achievementScrollS)) {ScrollsDiscovered.remove("achievementScrollS");}
            if (playerIn.hasAchievement(AchievementHandler.achievementScrollT)) {ScrollsDiscovered.remove("achievementScrollT");}
            if (playerIn.hasAchievement(AchievementHandler.achievementScrollU)) {ScrollsDiscovered.remove("achievementScrollU");}
            if (playerIn.hasAchievement(AchievementHandler.achievementScrollV)) {ScrollsDiscovered.remove("achievementScrollV");}
            if (playerIn.hasAchievement(AchievementHandler.achievementScrollW)) {ScrollsDiscovered.remove("achievementScrollW");}
            if (playerIn.hasAchievement(AchievementHandler.achievementScrollX)) {ScrollsDiscovered.remove("achievementScrollX");}
            if (playerIn.hasAchievement(AchievementHandler.achievementScrollY)) {ScrollsDiscovered.remove("achievementScrollY");}
            if (playerIn.hasAchievement(AchievementHandler.achievementScrollZ)) {ScrollsDiscovered.remove("achievementScrollZ");}

            if (ScrollsDiscovered.size()-1 ==0)
            {
                int scrollnum = 0;
                if (!playerIn.hasAchievement(AchievementHandler.achievementScrollAll)) {playerIn.addStat(AchievementHandler.achievementScrollAll);}
                //playerIn.sendStatusMessage(new TextComponentString(TextFormatting.GOLD + ScrollsDiscovered.get(scrollnum) + TextFormatting.BLUE + (ScrollsDiscovered.size() - 1)), true);
                if (ScrollsDiscovered.get(scrollnum).equals("achievementScrollA")) {
                        if ((playerIn.hasAchievement(AchievementHandler.achievementScrollE) && playerIn.hasAchievement(AchievementHandler.achievementScrollI) && playerIn.hasAchievement(AchievementHandler.achievementScrollO) && playerIn.hasAchievement(AchievementHandler.achievementScrollU))) {
                            playerIn.addStat(achievementScrollA);playerIn.addStat(AchievementHandler.achievementScrollVowels);} else playerIn.addStat(achievementScrollA);}
                if (ScrollsDiscovered.get(scrollnum).equals("achievementScrollB")) {playerIn.addStat(AchievementHandler.achievementScrollB);}
                if (ScrollsDiscovered.get(scrollnum).equals("achievementScrollC")) {playerIn.addStat(AchievementHandler.achievementScrollC);}
                if (ScrollsDiscovered.get(scrollnum).equals("achievementScrollD")) {playerIn.addStat(AchievementHandler.achievementScrollD);}
                if (ScrollsDiscovered.get(scrollnum).equals("achievementScrollE")) {
                        if ((playerIn.hasAchievement(achievementScrollA) && playerIn.hasAchievement(AchievementHandler.achievementScrollI) && playerIn.hasAchievement(AchievementHandler.achievementScrollO) && playerIn.hasAchievement(AchievementHandler.achievementScrollU))) {
                            playerIn.addStat(AchievementHandler.achievementScrollE);playerIn.addStat(AchievementHandler.achievementScrollVowels);} else playerIn.addStat(AchievementHandler.achievementScrollE);}
                if (ScrollsDiscovered.get(scrollnum).equals("achievementScrollF")) {playerIn.addStat(AchievementHandler.achievementScrollF);}
                if (ScrollsDiscovered.get(scrollnum).equals("achievementScrollG")) {playerIn.addStat(AchievementHandler.achievementScrollG);}
                if (ScrollsDiscovered.get(scrollnum).equals("achievementScrollH")) {playerIn.addStat(AchievementHandler.achievementScrollH);}
                if (ScrollsDiscovered.get(scrollnum).equals("achievementScrollI")) {
                        if ((playerIn.hasAchievement(achievementScrollA) && playerIn.hasAchievement(AchievementHandler.achievementScrollE) && playerIn.hasAchievement(AchievementHandler.achievementScrollO) && playerIn.hasAchievement(AchievementHandler.achievementScrollU))) {
                            playerIn.addStat(AchievementHandler.achievementScrollI);playerIn.addStat(AchievementHandler.achievementScrollVowels);} else playerIn.addStat(AchievementHandler.achievementScrollI);}
                if (ScrollsDiscovered.get(scrollnum).equals("achievementScrollJ")) {playerIn.addStat(AchievementHandler.achievementScrollJ);}
                if (ScrollsDiscovered.get(scrollnum).equals("achievementScrollK")) {playerIn.addStat(AchievementHandler.achievementScrollK);}
                if (ScrollsDiscovered.get(scrollnum).equals("achievementScrollL")) {playerIn.addStat(AchievementHandler.achievementScrollL);}
                if (ScrollsDiscovered.get(scrollnum).equals("achievementScrollM")) {playerIn.addStat(AchievementHandler.achievementScrollM);}
                if (ScrollsDiscovered.get(scrollnum).equals("achievementScrollN")) {playerIn.addStat(AchievementHandler.achievementScrollN);}
                if (ScrollsDiscovered.get(scrollnum).equals("achievementScrollO")) {
                        if ((playerIn.hasAchievement(achievementScrollA) && playerIn.hasAchievement(AchievementHandler.achievementScrollE) && playerIn.hasAchievement(AchievementHandler.achievementScrollI) && playerIn.hasAchievement(AchievementHandler.achievementScrollU))) {
                            playerIn.addStat(AchievementHandler.achievementScrollO);playerIn.addStat(AchievementHandler.achievementScrollVowels);} else playerIn.addStat(AchievementHandler.achievementScrollO);}
                if (ScrollsDiscovered.get(scrollnum).equals("achievementScrollP")) {playerIn.addStat(AchievementHandler.achievementScrollP);}
                if (ScrollsDiscovered.get(scrollnum).equals("achievementScrollQ")) {playerIn.addStat(AchievementHandler.achievementScrollQ);}
                if (ScrollsDiscovered.get(scrollnum).equals("achievementScrollR")) {playerIn.addStat(AchievementHandler.achievementScrollR);}
                if (ScrollsDiscovered.get(scrollnum).equals("achievementScrollS")) {playerIn.addStat(AchievementHandler.achievementScrollS);}
                if (ScrollsDiscovered.get(scrollnum).equals("achievementScrollT")) {playerIn.addStat(AchievementHandler.achievementScrollT);}
                if (ScrollsDiscovered.get(scrollnum).equals("achievementScrollU")) {
                        if ((playerIn.hasAchievement(achievementScrollA) && playerIn.hasAchievement(AchievementHandler.achievementScrollE) && playerIn.hasAchievement(AchievementHandler.achievementScrollI) && playerIn.hasAchievement(AchievementHandler.achievementScrollO))) {
                            playerIn.addStat(AchievementHandler.achievementScrollU);playerIn.addStat(AchievementHandler.achievementScrollVowels);} else playerIn.addStat(AchievementHandler.achievementScrollU);}
                if (ScrollsDiscovered.get(scrollnum).equals("achievementScrollV")) {playerIn.addStat(AchievementHandler.achievementScrollV);}
                if (ScrollsDiscovered.get(scrollnum).equals("achievementScrollW")) {playerIn.addStat(AchievementHandler.achievementScrollW);}
                if (ScrollsDiscovered.get(scrollnum).equals("achievementScrollX")) {playerIn.addStat(AchievementHandler.achievementScrollX);}
                if (ScrollsDiscovered.get(scrollnum).equals("achievementScrollY")) {playerIn.addStat(AchievementHandler.achievementScrollY);}
                if (ScrollsDiscovered.get(scrollnum).equals("achievementScrollZ")) {playerIn.addStat(AchievementHandler.achievementScrollZ);}
            }
            else {
                Random rn = new Random();
                int scrollnum = Math.abs(rn.nextInt(ScrollsDiscovered.size() - 1));
                //playerIn.sendStatusMessage(new TextComponentString(TextFormatting.GOLD + ScrollsDiscovered.get(scrollnum) + TextFormatting.BLUE + (ScrollsDiscovered.size() - 1)), true);
                if (ScrollsDiscovered.get(scrollnum).equals("achievementScrollA")) {
                    if ((playerIn.hasAchievement(AchievementHandler.achievementScrollE) && playerIn.hasAchievement(AchievementHandler.achievementScrollI) && playerIn.hasAchievement(AchievementHandler.achievementScrollO) && playerIn.hasAchievement(AchievementHandler.achievementScrollU))) {
                        playerIn.addStat(achievementScrollA);playerIn.addStat(AchievementHandler.achievementScrollVowels);} else playerIn.addStat(achievementScrollA);}
                if (ScrollsDiscovered.get(scrollnum).equals("achievementScrollB")) {playerIn.addStat(AchievementHandler.achievementScrollB);}
                if (ScrollsDiscovered.get(scrollnum).equals("achievementScrollC")) {playerIn.addStat(AchievementHandler.achievementScrollC);}
                if (ScrollsDiscovered.get(scrollnum).equals("achievementScrollD")) {playerIn.addStat(AchievementHandler.achievementScrollD);}
                if (ScrollsDiscovered.get(scrollnum).equals("achievementScrollE")) {
                    if ((playerIn.hasAchievement(achievementScrollA) && playerIn.hasAchievement(AchievementHandler.achievementScrollI) && playerIn.hasAchievement(AchievementHandler.achievementScrollO) && playerIn.hasAchievement(AchievementHandler.achievementScrollU))) {
                        playerIn.addStat(AchievementHandler.achievementScrollE);playerIn.addStat(AchievementHandler.achievementScrollVowels);} else playerIn.addStat(AchievementHandler.achievementScrollE);}
                if (ScrollsDiscovered.get(scrollnum).equals("achievementScrollF")) {playerIn.addStat(AchievementHandler.achievementScrollF);}
                if (ScrollsDiscovered.get(scrollnum).equals("achievementScrollG")) {playerIn.addStat(AchievementHandler.achievementScrollG);}
                if (ScrollsDiscovered.get(scrollnum).equals("achievementScrollH")) {playerIn.addStat(AchievementHandler.achievementScrollH);}
                if (ScrollsDiscovered.get(scrollnum).equals("achievementScrollI")) {
                    if ((playerIn.hasAchievement(achievementScrollA) && playerIn.hasAchievement(AchievementHandler.achievementScrollE) && playerIn.hasAchievement(AchievementHandler.achievementScrollO) && playerIn.hasAchievement(AchievementHandler.achievementScrollU))) {
                        playerIn.addStat(AchievementHandler.achievementScrollI);playerIn.addStat(AchievementHandler.achievementScrollVowels);} else playerIn.addStat(AchievementHandler.achievementScrollI);}
                if (ScrollsDiscovered.get(scrollnum).equals("achievementScrollJ")) {playerIn.addStat(AchievementHandler.achievementScrollJ);}
                if (ScrollsDiscovered.get(scrollnum).equals("achievementScrollK")) {playerIn.addStat(AchievementHandler.achievementScrollK);}
                if (ScrollsDiscovered.get(scrollnum).equals("achievementScrollL")) {playerIn.addStat(AchievementHandler.achievementScrollL);}
                if (ScrollsDiscovered.get(scrollnum).equals("achievementScrollM")) {playerIn.addStat(AchievementHandler.achievementScrollM);}
                if (ScrollsDiscovered.get(scrollnum).equals("achievementScrollN")) {playerIn.addStat(AchievementHandler.achievementScrollN);}
                if (ScrollsDiscovered.get(scrollnum).equals("achievementScrollO")) {
                    if ((playerIn.hasAchievement(achievementScrollA) && playerIn.hasAchievement(AchievementHandler.achievementScrollE) && playerIn.hasAchievement(AchievementHandler.achievementScrollI) && playerIn.hasAchievement(AchievementHandler.achievementScrollU))) {
                        playerIn.addStat(AchievementHandler.achievementScrollO);playerIn.addStat(AchievementHandler.achievementScrollVowels);} else playerIn.addStat(AchievementHandler.achievementScrollO);}
                if (ScrollsDiscovered.get(scrollnum).equals("achievementScrollP")) {playerIn.addStat(AchievementHandler.achievementScrollP);}
                if (ScrollsDiscovered.get(scrollnum).equals("achievementScrollQ")) {playerIn.addStat(AchievementHandler.achievementScrollQ);}
                if (ScrollsDiscovered.get(scrollnum).equals("achievementScrollR")) {playerIn.addStat(AchievementHandler.achievementScrollR);}
                if (ScrollsDiscovered.get(scrollnum).equals("achievementScrollS")) {playerIn.addStat(AchievementHandler.achievementScrollS);}
                if (ScrollsDiscovered.get(scrollnum).equals("achievementScrollT")) {playerIn.addStat(AchievementHandler.achievementScrollT);}
                if (ScrollsDiscovered.get(scrollnum).equals("achievementScrollU")) {
                    if ((playerIn.hasAchievement(achievementScrollA) && playerIn.hasAchievement(AchievementHandler.achievementScrollE) && playerIn.hasAchievement(AchievementHandler.achievementScrollI) && playerIn.hasAchievement(AchievementHandler.achievementScrollO))) {
                        playerIn.addStat(AchievementHandler.achievementScrollU);playerIn.addStat(AchievementHandler.achievementScrollVowels);} else playerIn.addStat(AchievementHandler.achievementScrollU);}
                if (ScrollsDiscovered.get(scrollnum).equals("achievementScrollV")) {playerIn.addStat(AchievementHandler.achievementScrollV);}
                if (ScrollsDiscovered.get(scrollnum).equals("achievementScrollW")) {playerIn.addStat(AchievementHandler.achievementScrollW);}
                if (ScrollsDiscovered.get(scrollnum).equals("achievementScrollX")) {playerIn.addStat(AchievementHandler.achievementScrollX);}
                if (ScrollsDiscovered.get(scrollnum).equals("achievementScrollY")) {playerIn.addStat(AchievementHandler.achievementScrollY);}
                if (ScrollsDiscovered.get(scrollnum).equals("achievementScrollZ")) {playerIn.addStat(AchievementHandler.achievementScrollZ);}
            }
        }

        return new ActionResult(EnumActionResult.SUCCESS, itemstack);
    }
 */
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
    {
        tooltip.add("[WIP] Will randomly give you a letter of the Ancient Alphabet.");
        tooltip.add("Not needed till next beta release(will also be reworked)");
    }

}


