package com.mowmaster.dust.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;


public class EnchantmentSmelt extends Enchantment
{

    public EnchantmentSmelt() {
        super(Rarity.VERY_RARE, EnumEnchantmentType.DIGGER, new EntityEquipmentSlot[] {EntityEquipmentSlot.MAINHAND});
        setName("enchantSmelter");
        setRegistryName(new ResourceLocation("dust", "enchantSmelter"));
    }

    /**
     * Returns the minimal value of enchantability needed on the enchantment level passed.
     */
    public int getMinEnchantability(int enchantmentLevel)
    {
        return enchantmentLevel * 25;
    }

    /**
     * Returns the maximum value of enchantability nedded on the enchantment level passed.
     */
    public int getMaxEnchantability(int enchantmentLevel)
    {
        return super.getMinEnchantability(enchantmentLevel) + 50;
    }

    /**
     * Returns the maximum level that the enchantment can have.
     */
    public int getMaxLevel()
    {
        return 1;
    }

    /**
     * Determines if the enchantment passed can be applyied together with this enchantment.
     */
    public boolean canApplyTogether(Enchantment ench)
    {
        return super.canApplyTogether(ench) && ench != Enchantments.SILK_TOUCH;
    }

    /*
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void smeltEvent(BlockEvent.HarvestDropsEvent event)
    {
        World world = event.getWorld();
        EntityPlayer player = event.getHarvester();
        BlockPos pos = event.getPos();
        List<ItemStack> stackie = event.getDrops();
        int count = 1;
        if(!world.isRemote) {//Disabled due to Ticking World Error
            if (player == null) {
                return;
            }

            if (player.swingingHand == null) {
                return;
            }

            //if (EnchantmentHelper.getEnchantmentLevel(EnchantmentRegistry.enchantmentSmelter, player.getHeldItem(player.getActiveHand())) != 0)
            //{
                ItemStack tool = player.getHeldItem(player.getActiveHand());

                //int lvl = EnchantmentHelper.getEnchantmentLevel(EnchantmentRegistry.enchantmentSmelter,player.getHeldItem(player.getActiveHand()));
                if(tool.getItem() instanceof ItemTool)
                {
                    if(!stackie.isEmpty())
                    {
                        if(player.isSneaking() || FurnaceRecipes.instance().getSmeltingResult(stackie.get(0)).isEmpty()) {
                            event.getState().getBlock().harvestBlock(world,player,event.getPos(),event.getState(),null,player.getHeldItemMainhand());
                            world.setBlockToAir(pos);
                        }
                        else
                        {
                            if(FurnaceRecipes.instance().getSmeltingResult(stackie.get(0)).getMetadata()>0)
                            {
                                //world.spawnEntity(new EntityItem(world, pos.getX() + 0.5,pos.getY() + 1.0,pos.getZ() + 0.5,new ItemStack(FurnaceRecipes.instance().getSmeltingResult(stackie.get(0)).getItem(),1,FurnaceRecipes.instance().getSmeltingResult(stackie.get(0)).getMetadata())));
                                event.getDrops().set(0,new ItemStack(FurnaceRecipes.instance().getSmeltingResult(stackie.get(0)).getItem(),1,FurnaceRecipes.instance().getSmeltingResult(stackie.get(0)).getMetadata()));
                                if(EnchantmentHelper.getEnchantmentLevel(EnchantmentRegistry.enchantDigger,player.getHeldItem(player.getActiveHand()))==0) {player.getHeldItem(player.getActiveHand()).damageItem(1,player);}
                                world.setBlockToAir(pos);
                            }
                            else
                            {
                                //world.spawnEntity(new EntityItem(world, pos.getX() + 0.5,pos.getY() + 1.0,pos.getZ() + 0.5,new ItemStack(FurnaceRecipes.instance().getSmeltingResult(stackie.get(0)).getItem(),1)));
                                event.getDrops().set(0,new ItemStack(FurnaceRecipes.instance().getSmeltingResult(stackie.get(0)).getItem(),1));
                                if(EnchantmentHelper.getEnchantmentLevel(EnchantmentRegistry.enchantDigger,player.getHeldItem(player.getActiveHand()))==0) {player.getHeldItem(player.getActiveHand()).damageItem(1,player);}
                                world.setBlockToAir(pos);
                            }

                        }
                    }
                    else
                    {
                        event.getState().getBlock().harvestBlock(world,player,event.getPos(),event.getState(),null,player.getHeldItemMainhand());
                        world.setBlockToAir(pos);
                    }
                }
            //}
        }
    }
     */
}
