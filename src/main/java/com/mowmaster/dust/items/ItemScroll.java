package com.mowmaster.dust.items;

import com.mowmaster.dust.misc.AchievementHandler;
import com.mowmaster.dust.references.Reference;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemFlintAndSteel;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.*;
import net.minecraft.world.World;

import java.util.Random;

import static com.mowmaster.dust.misc.DustyTab.DUSTTABS;


public class ItemScroll extends Item
{
    public ItemScroll(String unlocName, String registryName)
    {
        this.setUnlocalizedName(unlocName);
        this.setRegistryName(new ResourceLocation(Reference.MODID, registryName));
        this.setCreativeTab(DUSTTABS);
    }


    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        ItemStack itemstack = playerIn.getHeldItem(handIn);

        if (!playerIn.capabilities.isCreativeMode){itemstack.shrink(1);}

        worldIn.playSound((EntityPlayer)null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.BLOCK_CLOTH_HIT, SoundCategory.NEUTRAL, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
        playerIn.getCooldownTracker().setCooldown(this, 5);//Cool down before using next item/attack


        Random rn = new Random();
        int scrollnum = rn.nextInt(26);






            if(!playerIn.hasAchievement(AchievementHandler.achievementScrollFirst))
            {
                playerIn.addStat(AchievementHandler.achievementScrollFirst);
            }
            if(scrollnum == 0)
            {
                if(!playerIn.hasAchievement(AchievementHandler.achievementScrollA))
                {
                    if((playerIn.hasAchievement(AchievementHandler.achievementScrollE) && playerIn.hasAchievement(AchievementHandler.achievementScrollI) && playerIn.hasAchievement(AchievementHandler.achievementScrollO) && playerIn.hasAchievement(AchievementHandler.achievementScrollU)))
                    {
                        playerIn.addStat(AchievementHandler.achievementScrollA);
                        playerIn.addStat(AchievementHandler.achievementScrollVowels);
                    }
                    else playerIn.addStat(AchievementHandler.achievementScrollA);
                }
            }
            if(scrollnum == 1)
            {
                if(!playerIn.hasAchievement(AchievementHandler.achievementScrollB))
                {
                    playerIn.addStat(AchievementHandler.achievementScrollB);
                }
            }
            if(scrollnum == 2)
            {
                if(!playerIn.hasAchievement(AchievementHandler.achievementScrollC))
                {
                    playerIn.addStat(AchievementHandler.achievementScrollC);
                }
            }
            if(scrollnum == 3)
            {
                if(!playerIn.hasAchievement(AchievementHandler.achievementScrollD))
                {
                    playerIn.addStat(AchievementHandler.achievementScrollD);
                }
                
            }
            if(scrollnum == 4)
            {
                if(!playerIn.hasAchievement(AchievementHandler.achievementScrollE))
                {
                    if((playerIn.hasAchievement(AchievementHandler.achievementScrollA) && playerIn.hasAchievement(AchievementHandler.achievementScrollI) && playerIn.hasAchievement(AchievementHandler.achievementScrollO) && playerIn.hasAchievement(AchievementHandler.achievementScrollU)))
                    {
                        playerIn.addStat(AchievementHandler.achievementScrollE);
                        playerIn.addStat(AchievementHandler.achievementScrollVowels);
                    }
                    else playerIn.addStat(AchievementHandler.achievementScrollE);
                }
                
            }
            if(scrollnum == 5)
            {
                if(!playerIn.hasAchievement(AchievementHandler.achievementScrollF))
                {
                    playerIn.addStat(AchievementHandler.achievementScrollF);
                }
                
            }
            if(scrollnum == 6)
            {
                if(!playerIn.hasAchievement(AchievementHandler.achievementScrollG))
                {
                    playerIn.addStat(AchievementHandler.achievementScrollG);
                }
                
            }
            if(scrollnum == 7)
            {
                if(!playerIn.hasAchievement(AchievementHandler.achievementScrollH))
                {
                    playerIn.addStat(AchievementHandler.achievementScrollH);
                }
                
            }
            if(scrollnum == 8)
            {
                if(!playerIn.hasAchievement(AchievementHandler.achievementScrollI))
                {
                    if((playerIn.hasAchievement(AchievementHandler.achievementScrollA) && playerIn.hasAchievement(AchievementHandler.achievementScrollE) && playerIn.hasAchievement(AchievementHandler.achievementScrollO) && playerIn.hasAchievement(AchievementHandler.achievementScrollU)))
                    {
                        playerIn.addStat(AchievementHandler.achievementScrollI);
                        playerIn.addStat(AchievementHandler.achievementScrollVowels);
                    }
                    else playerIn.addStat(AchievementHandler.achievementScrollI);
                }
                
            }
            if(scrollnum == 9)
            {
                if(!playerIn.hasAchievement(AchievementHandler.achievementScrollJ))
                {
                    playerIn.addStat(AchievementHandler.achievementScrollJ);
                }
                
            }
            if(scrollnum == 10)
            {
                if(!playerIn.hasAchievement(AchievementHandler.achievementScrollK))
                {
                    playerIn.addStat(AchievementHandler.achievementScrollK);
                }
                
            }
            if(scrollnum == 11)
            {
                if(!playerIn.hasAchievement(AchievementHandler.achievementScrollL))
                {
                    playerIn.addStat(AchievementHandler.achievementScrollL);
                }
                
            }
            if(scrollnum == 12)
            {
                if(!playerIn.hasAchievement(AchievementHandler.achievementScrollM))
                {
                    playerIn.addStat(AchievementHandler.achievementScrollM);
                }
                
            }
            if(scrollnum == 13)
            {
                if(!playerIn.hasAchievement(AchievementHandler.achievementScrollN))
                {
                    playerIn.addStat(AchievementHandler.achievementScrollN);
                }
                
            }
            if(scrollnum == 14)
            {
                if(!playerIn.hasAchievement(AchievementHandler.achievementScrollO))
                {
                    if((playerIn.hasAchievement(AchievementHandler.achievementScrollA) && playerIn.hasAchievement(AchievementHandler.achievementScrollE) && playerIn.hasAchievement(AchievementHandler.achievementScrollI) && playerIn.hasAchievement(AchievementHandler.achievementScrollU)))
                    {
                        playerIn.addStat(AchievementHandler.achievementScrollO);
                        playerIn.addStat(AchievementHandler.achievementScrollVowels);
                    }
                    else playerIn.addStat(AchievementHandler.achievementScrollO);
                }
                
            }
            if(scrollnum == 15)
            {
                if(!playerIn.hasAchievement(AchievementHandler.achievementScrollP))
                {
                    playerIn.addStat(AchievementHandler.achievementScrollP);
                }
                
            }
            if(scrollnum == 16)
            {
                if(!playerIn.hasAchievement(AchievementHandler.achievementScrollQ))
                {
                    playerIn.addStat(AchievementHandler.achievementScrollQ);
                }
                
            }
            if(scrollnum == 17)
            {
                if(!playerIn.hasAchievement(AchievementHandler.achievementScrollR))
                {
                    playerIn.addStat(AchievementHandler.achievementScrollR);
                }
                
            }
            if(scrollnum == 18)
            {
                if(!playerIn.hasAchievement(AchievementHandler.achievementScrollS))
                {
                    playerIn.addStat(AchievementHandler.achievementScrollS);
                }
                
            }
            if(scrollnum == 19)
            {
                if(!playerIn.hasAchievement(AchievementHandler.achievementScrollT))
                {
                    playerIn.addStat(AchievementHandler.achievementScrollT);
                }
                
            }
            if(scrollnum == 20)
            {
                if(!playerIn.hasAchievement(AchievementHandler.achievementScrollU))
                {
                    if((playerIn.hasAchievement(AchievementHandler.achievementScrollA) && playerIn.hasAchievement(AchievementHandler.achievementScrollE) && playerIn.hasAchievement(AchievementHandler.achievementScrollI) && playerIn.hasAchievement(AchievementHandler.achievementScrollO)))
                    {
                        playerIn.addStat(AchievementHandler.achievementScrollU);
                        playerIn.addStat(AchievementHandler.achievementScrollVowels);
                    }
                    else playerIn.addStat(AchievementHandler.achievementScrollU);
                }
                
            }
            if(scrollnum == 21)
            {
                if(!playerIn.hasAchievement(AchievementHandler.achievementScrollV))
                {
                    playerIn.addStat(AchievementHandler.achievementScrollV);
                }

            }
            if(scrollnum == 22)
            {
                if(!playerIn.hasAchievement(AchievementHandler.achievementScrollW))
                {
                    playerIn.addStat(AchievementHandler.achievementScrollW);
                }
                
            }
            if(scrollnum == 23)
            {
                if(!playerIn.hasAchievement(AchievementHandler.achievementScrollX))
                {
                    playerIn.addStat(AchievementHandler.achievementScrollX);
                }
                
            }
            if(scrollnum == 24)
            {
                if(!playerIn.hasAchievement(AchievementHandler.achievementScrollY))
                {
                    playerIn.addStat(AchievementHandler.achievementScrollY);
                }
                
            }
            if(scrollnum == 25)
            {
                if(!playerIn.hasAchievement(AchievementHandler.achievementScrollZ))
                {
                    playerIn.addStat(AchievementHandler.achievementScrollZ);
                }
                
            }


        if (
                playerIn.hasAchievement(AchievementHandler.achievementScrollA) &&
                        playerIn.hasAchievement(AchievementHandler.achievementScrollB) &&
                        playerIn.hasAchievement(AchievementHandler.achievementScrollC) &&
                        playerIn.hasAchievement(AchievementHandler.achievementScrollD) &&
                        playerIn.hasAchievement(AchievementHandler.achievementScrollE) &&
                        playerIn.hasAchievement(AchievementHandler.achievementScrollF) &&
                        playerIn.hasAchievement(AchievementHandler.achievementScrollG) &&
                        playerIn.hasAchievement(AchievementHandler.achievementScrollH) &&
                        playerIn.hasAchievement(AchievementHandler.achievementScrollI) &&
                        playerIn.hasAchievement(AchievementHandler.achievementScrollJ) &&
                        playerIn.hasAchievement(AchievementHandler.achievementScrollK) &&
                        playerIn.hasAchievement(AchievementHandler.achievementScrollL) &&
                        playerIn.hasAchievement(AchievementHandler.achievementScrollM) &&
                        playerIn.hasAchievement(AchievementHandler.achievementScrollN) &&
                        playerIn.hasAchievement(AchievementHandler.achievementScrollO) &&
                        playerIn.hasAchievement(AchievementHandler.achievementScrollP) &&
                        playerIn.hasAchievement(AchievementHandler.achievementScrollQ) &&
                        playerIn.hasAchievement(AchievementHandler.achievementScrollR) &&
                        playerIn.hasAchievement(AchievementHandler.achievementScrollS) &&
                        playerIn.hasAchievement(AchievementHandler.achievementScrollT) &&
                        playerIn.hasAchievement(AchievementHandler.achievementScrollU) &&
                        playerIn.hasAchievement(AchievementHandler.achievementScrollV) &&
                        playerIn.hasAchievement(AchievementHandler.achievementScrollW) &&
                        playerIn.hasAchievement(AchievementHandler.achievementScrollX) &&
                        playerIn.hasAchievement(AchievementHandler.achievementScrollY) &&
                        playerIn.hasAchievement(AchievementHandler.achievementScrollZ)
        )
        {
            playerIn.addStat(AchievementHandler.achievementScrollAll);
        }

        if(playerIn.hasAchievement(AchievementHandler.achievementScrollAll))
        {
            playerIn.setHeldItem(handIn, new ItemStack(Items.PAPER,1));
        }

        return new ActionResult(EnumActionResult.SUCCESS, itemstack);
    }

}


// When used Scrolls they check to see what letters you know and then give you a random one from what you have left

