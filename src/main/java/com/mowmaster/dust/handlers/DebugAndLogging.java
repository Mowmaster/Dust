package com.mowmaster.dust.handlers;

import com.mowmaster.dust.blocks.BlockCrystalClusterBasic;
import com.mowmaster.dust.blocks.BlockRegistry;
import com.mowmaster.dust.items.ItemCrystal;
import com.mowmaster.dust.tiles.TileCrystalCluster;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static com.mowmaster.dust.items.ItemRegistry.crystal;
import static com.mowmaster.dust.items.ItemRegistry.debug;


public class DebugAndLogging
{


    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onItemRightClick(PlayerInteractEvent.RightClickItem event)
    {
        World worldIn = event.getWorld();
        EntityPlayer playerIn = event.getEntityPlayer();
        EnumHand hand = event.getHand();
        IBlockState state = worldIn.getBlockState(event.getPos());

        if((playerIn.getHeldItemOffhand().getItem().equals(debug)))
        {
            if((playerIn.getHeldItemMainhand().isItemEnchanted() || playerIn.getHeldItemMainhand().getItem().equals(Items.ENCHANTED_BOOK)))
            {
                NBTTagList list = playerIn.getHeldItemMainhand().getEnchantmentTagList();
                if (list == null) {
                    return;
                }

                for (int i = 0; i < list.tagCount(); i++) {
                    NBTTagCompound compound = list.getCompoundTagAt(i);
                    int id = compound.getShort("id");
                    int lvl = compound.getShort("lvl");
                    Enchantment e = Enchantment.getEnchantmentByID(id);

                    playerIn.sendMessage(new TextComponentString(TextFormatting.WHITE + e.getName() + ": " + lvl));
                }
            }
        }

    }
}
