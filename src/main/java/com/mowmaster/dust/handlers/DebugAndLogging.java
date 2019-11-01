package com.mowmaster.dust.handlers;

import com.mowmaster.dust.blocks.machines.BlockPedestal;
import com.mowmaster.dust.items.ItemRegistry;
import com.mowmaster.dust.items.ItemWikiScroll;
import com.mowmaster.dust.items.itemPedestalUpgrades.ipuBasic;
import com.mowmaster.dust.tiles.TilePedestal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTippedArrow;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static com.mowmaster.dust.items.ItemRegistry.akashic;
import static com.mowmaster.dust.items.ItemRegistry.wikiscroll;


public class DebugAndLogging
{
    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onItemRightClick(PlayerInteractEvent.RightClickItem event) {
        World worldIn = event.getWorld();
        EntityPlayer playerIn = event.getEntityPlayer();
        BlockPos pos = playerIn.getPosition();
        EnumHand hand = event.getHand();
        IBlockState state = worldIn.getBlockState(event.getPos());
        ItemStack item = playerIn.getHeldItem(EnumHand.MAIN_HAND);

        if(!worldIn.isRemote)
        {
            if (playerIn.isSneaking()) {
                if ((playerIn.getHeldItemMainhand().getItem() instanceof ItemWikiScroll) || (playerIn.getHeldItemOffhand().getItem() instanceof ItemWikiScroll))
                {
                    if (playerIn.getHeldItem(hand).getMetadata() != 15) {
                        playerIn.getHeldItem(hand).setItemDamage((playerIn.getHeldItem(hand).getMetadata() + 1));
                    } else {
                        playerIn.getHeldItem(hand).setItemDamage(0);
                    }
                }

                if(playerIn.getHeldItemMainhand().getItem().equals(ItemRegistry.akashic))
                {
                    System.out.println(pos);
                    System.out.println(event.getPos());

                    if (worldIn.getBlockState(pos).getBlock() instanceof BlockPedestal) {
                        TileEntity tileEntity = worldIn.getTileEntity(pos);
                        if (tileEntity instanceof TilePedestal) {
                            TilePedestal tilePedestal = (TilePedestal) tileEntity;
                            //tilePedestal.getStoredBlockPoss();
                            System.out.println(tilePedestal.getPedestalTransferAmount());
                            System.out.println(tilePedestal.getPedestalTransferSpeed());
                            playerIn.sendMessage(new TextComponentString(TextFormatting.RED + "" + tilePedestal.getPedestalTransferAmount()));
                            playerIn.sendMessage(new TextComponentString(TextFormatting.DARK_RED + "" + tilePedestal.getPedestalTransferSpeed() ));
                        }
                    }
                }


            }
        }

        if ((playerIn.getHeldItemOffhand().getItem().equals(akashic))) {



            if ((playerIn.getHeldItemMainhand().isItemEnchanted() || playerIn.getHeldItemMainhand().getItem().equals(Items.ENCHANTED_BOOK))) {
                NBTTagList list = playerIn.getHeldItemMainhand().getEnchantmentTagList();
                if (playerIn.getHeldItemMainhand().getItem().equals(Items.ENCHANTED_BOOK)) {
                    list = ItemEnchantedBook.getEnchantments(item);
                }


                if (list == null) {
                    return;
                }


                int id = 0;
                int lvl = 0;
                Enchantment e = Enchantment.getEnchantmentByID(id);
                for (int i = 0; i < list.tagCount(); i++) {
                    NBTTagCompound compound = list.getCompoundTagAt(i);
                    id = compound.getShort("id");
                    lvl = compound.getShort("lvl");
                    e = Enchantment.getEnchantmentByID(id);

                    if (e.getName().contains("enchantment.knockback")) {
                        lvl = lvl + 100;
                        ItemStack stack = new ItemStack(item.getItem(), 1);
                        stack.addEnchantment(Enchantment.getEnchantmentByID(id), lvl);

                        if (!worldIn.isRemote) {
                            worldIn.spawnEntity(new EntityItem(worldIn, pos.getX() + 0.5, pos.getY() + 0.0, pos.getZ() + 0.5, stack));
                        }

                    }

                    if (!worldIn.isRemote) {
                        playerIn.sendMessage(new TextComponentString(TextFormatting.WHITE + e.getName().replace("enchantment.", "") + ": " + TextFormatting.GOLD + lvl));
                        playerIn.sendMessage(new TextComponentString(TextFormatting.WHITE + "ID" + ": " + TextFormatting.GREEN + id));
                    }

                }
            }

            if (worldIn.isRemote) {
                if (!playerIn.isSneaking() && playerIn.getHeldItemMainhand().isEmpty()) {
                    int expL = playerIn.experienceLevel;
                    float exp = playerIn.experience;
                    int expT = playerIn.experienceTotal;
                    playerIn.sendMessage(new TextComponentString(TextFormatting.WHITE + "Level: " + expL));
                    playerIn.sendMessage(new TextComponentString(TextFormatting.WHITE + "Exp: " + exp));
                    playerIn.sendMessage(new TextComponentString(TextFormatting.WHITE + "Total: " + expT));
                }
            }


            if (worldIn.isRemote) {
                if (playerIn.getHeldItemMainhand().getItem() instanceof ItemTippedArrow) {
                    playerIn.sendMessage(new TextComponentString(TextFormatting.WHITE + PotionUtils.getEffectsFromStack(playerIn.getHeldItemMainhand()).toString()));
                }
            }

            if (worldIn.isRemote) {
                if (playerIn.getHeldItemMainhand().getItem() instanceof ipuBasic) {
                    if(playerIn.getHeldItemMainhand().hasTagCompound())
                    {
                        if(playerIn.getHeldItemMainhand().getTagCompound().hasKey("coineffect"))
                        {
                            playerIn.sendMessage(new TextComponentString(TextFormatting.WHITE + PotionEffect.readCustomPotionEffectFromNBT(playerIn.getHeldItemMainhand().getTagCompound().getCompoundTag("coineffect")).toString()));
                        }
                    }
                }
            }



        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onItemRightClick(PlayerInteractEvent.RightClickBlock event)
    {
        World worldIn = event.getWorld();
        EntityPlayer playerIn = event.getEntityPlayer();
        BlockPos pos = event.getPos();
        EnumHand hand = event.getHand();
        IBlockState state = worldIn.getBlockState(event.getPos());
        ItemStack item = playerIn.getHeldItem(EnumHand.MAIN_HAND);



            if(playerIn.getHeldItemOffhand().getItem().equals(akashic))
            {
                TileEntity tileEntity = worldIn.getTileEntity(pos);
                if(tileEntity instanceof TilePedestal)
                {
                    if(((TilePedestal) tileEntity).hasCoin())
                    {
                        playerIn.sendMessage(new TextComponentString(TextFormatting.WHITE + ((TilePedestal) tileEntity).getCoinOnPedestal().getUnlocalizedName()));
                    }
                    if(((TilePedestal) tileEntity).hasItem())
                    {
                        playerIn.sendMessage(new TextComponentString(TextFormatting.WHITE + ((TilePedestal) tileEntity).getItemInPedestal().getDisplayName()));
                    }
                }
            }


    }
}
