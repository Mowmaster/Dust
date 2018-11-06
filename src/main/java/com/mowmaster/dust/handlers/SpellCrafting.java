package com.mowmaster.dust.handlers;

import com.google.common.collect.Lists;
import com.mowmaster.dust.blocks.BlockRegistry;
import com.mowmaster.dust.effects.EffectPicker;
import com.mowmaster.dust.enums.CrystalTypes;
import com.mowmaster.dust.items.ItemCoin;
import com.mowmaster.dust.items.ItemDust;
import com.mowmaster.dust.items.ItemRegistry;
import com.mowmaster.dust.tiles.TileTrapBlock;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemFlintAndSteel;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

import static com.mowmaster.dust.misc.DustConfigurationFile.dustToActivate;
import static com.mowmaster.dust.misc.DustConfigurationFile.effectMaximum;

public class SpellCrafting
{
    private static int potencyLimiter =  effectMaximum;
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onFlintAndStealUsed(PlayerInteractEvent.RightClickBlock event) {
        World worldIn = event.getWorld();
        EnumHand hand = event.getHand();
        IBlockState state = worldIn.getBlockState(event.getPos());
        EntityPlayer player = event.getEntityPlayer();

        int posX = event.getPos().getX();
        int posY = event.getPos().getY();
        int posZ = event.getPos().getZ();

        int red=0;
        int blue=0;
        int yellow=0;
        int white=0;
        int black=0;
        int pressurePlate = 0;
        int paper = 0;
        int coin = 0;
        //int spellpaper = 0;
        int arrow = 0;
        int count=0;
        ItemStack keepStack = ItemStack.EMPTY;

        int minimumDustRequired=dustToActivate;

        if(!worldIn.isRemote) {
            //List<EntityItem> items = player.getEntityWorld().getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(posX-1, posY-1, posZ-1, posX+1, posY+1, posZ+1));
            List<EntityItem> items = player.getEntityWorld().getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(posX - 3, posY - 3, posZ - 3, posX + 3, posY + 3, posZ + 3));
            ItemStack coined = ItemStack.EMPTY;
            if ((player.getHeldItem(hand) != null)) {
                if (player.getHeldItem(hand).getItem() instanceof ItemFlintAndSteel) {

                    for (EntityItem item : items) {
                        ItemStack stack = item.getItem();
                        //System.out.println(stack.getItem() instanceof ItemDust);
                        if(stack.getItem() instanceof ItemDust)
                        {
                            if (stack.getItemDamage() == 0) {
                                red += 2 * stack.getCount();
                                count += stack.getCount();
                            } else if (stack.getItemDamage() == 1) {
                                blue += 2 * stack.getCount();
                                count += stack.getCount();
                            } else if (stack.getItemDamage() == 2) {
                                yellow += 2 * stack.getCount();
                                count += stack.getCount();
                            } else if (stack.getItemDamage() == 3) {
                                red += stack.getCount();
                                blue += stack.getCount();
                                count += stack.getCount();
                            } else if (stack.getItemDamage() == 4) {
                                yellow += stack.getCount();
                                blue += stack.getCount();
                                count += stack.getCount();
                            } else if (stack.getItemDamage() == 5) {
                                yellow += stack.getCount();
                                red += stack.getCount();
                                count += stack.getCount();
                            } else if (stack.getItemDamage() == 6) {
                                white += stack.getCount();
                                count += stack.getCount();
                            } else if (stack.getItemDamage() == 7) {
                                black += stack.getCount();
                                count += stack.getCount();
                            }
                            item.setDead();
                        }
                        if(stack.getItem().equals(new ItemStack(Blocks.WOODEN_PRESSURE_PLATE).getItem())
                                || stack.getItem().equals(new ItemStack(Blocks.STONE_PRESSURE_PLATE).getItem())
                                || stack.getItem().equals(new ItemStack(Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE).getItem())
                                || stack.getItem().equals(new ItemStack(Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE).getItem())
                        )//&& !(arrow>0) || !(paper>0)
                        {
                            pressurePlate++;
                            item.setDead();
                        }
                        if(stack.getItem().equals(Items.PAPER) )//&& !(arrow>0) || !(pressurePlate>0)
                        {
                            paper += stack.getCount();
                            item.setDead();
                        }

                        if(stack.getItem()instanceof ItemCoin)//&& !(arrow>0) || !(pressurePlate>0)
                        {
                            coined = stack.copy();
                            coin += stack.getCount();
                            item.setDead();
                        }

                        if(stack.getItem().equals(Items.ARROW) )//&& !(paper>0) || !(pressurePlate>0)
                        {
                            arrow += stack.getCount();
                            item.setDead();
                        }

                    }

                    if (count >= minimumDustRequired && pressurePlate==0 && paper==0 && arrow==0 && coin==0) {
                        player.addPotionEffect(EffectPicker.getEffectFromInputs(red, blue, yellow, white, black, 20 * count,potencyLimiter, false, true, CrystalTypes.EffectTypes.DUST));
                    }
                    else if(count >= minimumDustRequired && pressurePlate>=1 && paper==0 && arrow==0 && coin==0)
                    {

                        /*
                        if(worldIn.getBlockState(new BlockPos(posX,posY+1,posZ)).getBlock() instanceof BlockTrap)
                        {
                            System.out.println("TRAP BLOCK ALREADY HERE!!!");
                        }
                        //Could use this to impliment more plates that could hold multiple effects???
                         */
                        worldIn.setBlockToAir(new BlockPos(posX,posY+1,posZ));
                        worldIn.setBlockState(new BlockPos(posX,posY+1,posZ), BlockRegistry.blockTrap.getDefaultState());
                        TileEntity tileentity = worldIn.getTileEntity(new BlockPos(posX,posY+1,posZ));
                        if (tileentity instanceof TileTrapBlock) {
                            ((TileTrapBlock) tileentity).setTrapEffect(EffectPicker.getEffectFromInputs(red, blue, yellow, white, black, 20 * count,potencyLimiter, false, true, CrystalTypes.EffectTypes.DUST));
                        }
                    }
                    else if(pressurePlate==0 && arrow==0 && coin==0 && paper>=1)
                    {
                        if(count/paper >= minimumDustRequired)
                        {
                            if(!worldIn.isRemote)
                            {
                                PotionEffect effect = EffectPicker.getEffectFromInputs(red/paper, blue/paper, yellow/paper, white/paper, black/paper, 20 * count/paper,potencyLimiter, false, true, CrystalTypes.EffectTypes.DUST);
                                ItemStack stack = new ItemStack(ItemRegistry.spellPaper);
                                NBTTagCompound cmpd = new NBTTagCompound();
                                cmpd.setTag("scrolleffect",effect.writeCustomPotionEffectToNBT(new NBTTagCompound()));
                                stack.setTagCompound(cmpd);

                                for(int i=0; i<paper; i++)
                                {
                                    EntityItem items1 = new EntityItem(worldIn, posX + 0.5, posY + 1.0, posZ + 0.5, stack.copy());
                                    items1.onCollideWithPlayer(player);
                                    //worldIn.spawnEntity(items1);
                                }
                            }
                        }
                        else player.sendStatusMessage(new TextComponentString(TextFormatting.WHITE +"Not Enough Dust To Make Scroll"),true);
                    }
                    else if(pressurePlate==0 && arrow==0 &&  paper==0 && coin>=1)
                    {
                        if(count/coin >= minimumDustRequired)
                        {
                            if(!worldIn.isRemote)
                            {
                                ItemStack stack = ItemStack.EMPTY;
                                if(coined.getItem().equals(ItemRegistry.enchantUpgrade) || coined.getItem().equals(ItemRegistry.chopperUpgrade))
                                {
                                    stack = coined;
                                }
                                else stack = new ItemStack(ItemRegistry.effectUpgrade);


                                PotionEffect effect = EffectPicker.getEffectFromInputs(red/coin, blue/coin, yellow/coin, white/coin, black/coin, 1,potencyLimiter, false, true, CrystalTypes.EffectTypes.DUST);
                                NBTTagCompound cmpd = new NBTTagCompound();
                                cmpd.setTag("coineffect",effect.writeCustomPotionEffectToNBT(new NBTTagCompound()));
                                stack.setTagCompound(cmpd);

                                for(int i=0; i<coin; i++)
                                {
                                    EntityItem items1 = new EntityItem(worldIn, posX + 0.5, posY + 1.0, posZ + 0.5, stack.copy());
                                    items1.onCollideWithPlayer(player);
                                    //worldIn.spawnEntity(items1);
                                }
                            }
                        }
                        else player.sendStatusMessage(new TextComponentString(TextFormatting.WHITE +"Not Enough Dust To Make Effect Upgrade"),true);
                    }

                    else if(pressurePlate==0 && paper>=0 && coin==0&& arrow>=1)
                    {
                        if(count/arrow >= minimumDustRequired)
                        {
                            if(!worldIn.isRemote)
                            {
                                List<PotionEffect> effects = Lists.<PotionEffect>newArrayList();
                                PotionEffect effect = EffectPicker.getEffectFromInputs(red/arrow, blue/arrow, yellow/arrow, white/arrow, black/arrow, 20 * count/arrow * 8,potencyLimiter, false, true, CrystalTypes.EffectTypes.DUST);
                                effects.add(effect);
                                //NBTTagCompound cmpd = new NBTTagCompound();
                                //cmpd.setTag("CustomPotionEffects",effect.writeCustomPotionEffectToNBT(new NBTTagCompound()));
                                //cmpd.setTag("Potion",new NBTTagString(effect.getPotion().getRegistryName().toString()));
                                //stack.setTagCompound(cmpd);

                                ItemStack itemstack1 = new ItemStack(Items.TIPPED_ARROW);
                                //PotionUtils.addPotionToItemStack(itemstack1, new PotionType(new PotionEffect[]{effect}));
                                NBTTagCompound cmpd = new NBTTagCompound();
                                cmpd.setInteger("CustomPotionColor",effect.getPotion().getLiquidColor());
                                itemstack1.setTagCompound(cmpd);
                                PotionUtils.appendEffects(itemstack1, effects);
                                String s1 = I18n.translateToLocal(effect.getEffectName()).trim();
                                itemstack1.setStackDisplayName("Arrow of " + s1);

                                for(int i=0; i<arrow; i++)
                                {
                                    EntityItem items1 = new EntityItem(worldIn, posX + 0.5, posY + 1.0, posZ + 0.5, itemstack1.copy());
                                    items1.onCollideWithPlayer(player);
                                    //worldIn.spawnEntity(items1);
                                }
                            }
                        }
                        else player.sendStatusMessage(new TextComponentString(TextFormatting.WHITE +"Not Enough Dust To Make Scroll"),true);
                    }

                    else if(count<minimumDustRequired && count>0)
                    {
                        player.sendStatusMessage(new TextComponentString(TextFormatting.WHITE +"Not Enough Dust To Activate Effect"),true);
                    }
                }
            }
        }
    }
}
