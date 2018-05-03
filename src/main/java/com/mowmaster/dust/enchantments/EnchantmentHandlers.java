package com.mowmaster.dust.enchantments;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentMending;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EnchantmentHandlers
{

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onBreakEvent(BlockEvent.BreakEvent event)
    {
        World world = event.getWorld();
        EntityPlayer player = event.getPlayer();
        if (player.swingingHand == null) {
            return;
        }
        BlockPos pos = event.getPos();
        Block block = event.getState().getBlock();
        IBlockState state = world.getBlockState(pos);
        //is this item stack enchanted with ME?
        ItemStack stackHarvestingWith = player.getHeldItem(player.swingingHand);
        NBTTagList list = player.getHeldItem(player.getActiveHand()).getEnchantmentTagList();
        if (list == null) {
            return;
        }
        int id = 0;
        int lvl = 0;
        int enchantLvl = 0;
        Enchantment e = Enchantment.getEnchantmentByID(id);
        for (int i = 0; i < list.tagCount(); i++) {
            NBTTagCompound compound = list.getCompoundTagAt(i);
            id = compound.getShort("id");
            lvl = compound.getShort("lvl");
            e = Enchantment.getEnchantmentByID(id);

            if (e.getName().contains("enchantVamperic")) {
                lvl = enchantLvl;
            }
        }
        /*
        if (enchantLvl <= 0) {
            return;
        }
         */




        //Wont Mine Dirt/ Wood on pickaxe

        for (String type : stackHarvestingWith.getItem().getToolClasses(stackHarvestingWith)) {
            if (block.isToolEffective(type, world.getBlockState(pos)) == false) {
                return;
            }
        }





        //block.harvestBlock(world, player, targetPos, targetState, null, player.getHeldItem(EnumHand.MAIN_HAND));

        if(!world.isRemote)
        {
            int damage = player.getHeldItem(player.getActiveHand()).getItemDamage();
            Float health = player.getHealth();
            //if(health>3.0f)
            //{
                if(player.getHeldItem(player.getActiveHand()).getMaxDamage()-damage>=5)
                {
                    player.getHeldItem(player.getActiveHand()).setItemDamage(damage - 6);
                    player.setHealth(health-1.0f);
                }

            //}

        }



    }
}
