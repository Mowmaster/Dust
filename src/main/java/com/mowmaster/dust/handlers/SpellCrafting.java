package com.mowmaster.dust.handlers;

import com.google.common.collect.Lists;
import com.mowmaster.dust.blocks.machines.BlockMachineBase;
import com.mowmaster.dust.blocks.machines.BlockPedestal;
import com.mowmaster.dust.blocks.machines.BlockTrap;
import com.mowmaster.dust.effects.EffectPicker;
import com.mowmaster.dust.enums.CrystalTypes;
import com.mowmaster.dust.items.ItemCoin;
import com.mowmaster.dust.items.ItemDust;
import com.mowmaster.dust.items.ItemRegistry;
import com.mowmaster.dust.items.itemPedestalUpgrades.ipuBasic;
import com.mowmaster.dust.tiles.TileTrapBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBasePressurePlate;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.*;
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
        int purple=0;
        int green=0;
        int orange=0;
        int white=0;
        int black=0;
        int pressurePlate = 0;
        int paper = 0;
        int coin = 0;
        int pedestal = 0;
        //int spellpaper = 0;
        int arrow = 0;
        int stone = 0;
        int count=0;
        ItemStack keepStack = ItemStack.EMPTY;

        int minimumDustRequired=dustToActivate;

        if(!worldIn.isRemote) {
            //List<EntityItem> items = player.getEntityWorld().getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(posX-1, posY-1, posZ-1, posX+1, posY+1, posZ+1));
            List<EntityItem> items = player.getEntityWorld().getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(posX - 3, posY - 3, posZ - 3, posX + 3, posY + 3, posZ + 3));
            ItemStack coined = ItemStack.EMPTY;
            ItemStack peded = ItemStack.EMPTY;
            ItemStack trapped = ItemStack.EMPTY;
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
                                purple++;
                            } else if (stack.getItemDamage() == 4) {
                                yellow += stack.getCount();
                                blue += stack.getCount();
                                count += stack.getCount();
                                green++;
                            } else if (stack.getItemDamage() == 5) {
                                yellow += stack.getCount();
                                red += stack.getCount();
                                count += stack.getCount();
                                orange++;
                            } else if (stack.getItemDamage() == 6) {
                                white += stack.getCount();
                                count += stack.getCount();
                            } else if (stack.getItemDamage() == 7) {
                                black += stack.getCount();
                                count += stack.getCount();
                            }
                            item.setDead();
                        }
                        if(containsPressurePlate(stack))//&& !(arrow>0) || !(paper>0)
                        {
                            trapped = stack.copy();
                            pressurePlate++;
                            item.setDead();
                        }
                        if(containsPaper(stack))//&& !(arrow>0) || !(pressurePlate>0)
                        {
                            paper += stack.getCount();
                            item.setDead();
                        }

                        if(containsCoin(stack))
                        {
                            coined = stack.copy();
                            coin += stack.getCount();
                            item.setDead();
                        }

                        if(containsPedestal(stack))
                        {
                            peded = stack.copy();
                            pedestal += stack.getCount();
                            item.setDead();
                            //System.out.println(peded.getItem().getUnlocalizedName() + peded.getCount());
                        }

                        if(containsArrow(stack))//&& !(paper>0) || !(pressurePlate>0)
                        {
                            arrow += stack.getCount();
                            item.setDead();
                        }

                    }
                    if(red>0 && blue>0 && yellow>0 && purple>0 && green>0 && orange >0 && white>0 && black>0)
                    {
                        worldIn.setBlockToAir(new BlockPos(posX,posY+1,posZ));
                        worldIn.setBlockState(new BlockPos(posX,posY+1,posZ), BlockMachineBase.machineBase.getDefaultState());
                        worldIn.createExplosion(new EntityItem(worldIn), posX + 0.5,posY + 2.0,posZ + 0.5,1.0F, false);
                    }
                    else if (count >= minimumDustRequired && pressurePlate==0 && paper==0 && arrow==0 && coin==0 && pedestal==0) {
                        player.addPotionEffect(EffectPicker.getEffectFromInputs(red, blue, yellow, white, black, 20 * count,potencyLimiter, false, true, CrystalTypes.EffectTypes.DUST));
                    }
                    else if(count >= minimumDustRequired && pressurePlate>=1 && paper==0 && arrow==0 && coin==0 && pedestal==0)
                    {
                        worldIn.setBlockToAir(new BlockPos(posX,posY+1,posZ));
                        if(trapped.getItem().equals(Item.getItemFromBlock(Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE)) || trapped.getItem().equals(Item.getItemFromBlock(Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE)))
                        {
                            worldIn.setBlockState(new BlockPos(posX,posY+1,posZ), BlockTrap.blockTrap.getDefaultState());
                        }
                        else
                        {
                            worldIn.setBlockState(new BlockPos(posX,posY+1,posZ), BlockTrap.blockTrapMob.getDefaultState());
                        }

                        TileEntity tileentity = worldIn.getTileEntity(new BlockPos(posX,posY+1,posZ));
                        if (tileentity instanceof TileTrapBlock) {
                            ((TileTrapBlock) tileentity).setTrapEffect(EffectPicker.getEffectFromInputs(red, blue, yellow, white, black, 20 * count,potencyLimiter, false, true, CrystalTypes.EffectTypes.DUST));
                        }
                    }
                    else if(pressurePlate==0 && arrow==0 && coin==0 && pedestal==0&& paper>=1)
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
                    else if(pressurePlate==0 && arrow==0 && coin==0 &&  paper==0 && pedestal>=1)
                    {
                        if(count/pedestal >= minimumDustRequired)
                        {
                            if(!worldIn.isRemote)
                            {
                                PotionEffect effect = EffectPicker.getEffectFromInputs(red/pedestal, blue/pedestal, yellow/pedestal, white/pedestal, black/pedestal, 1,potencyLimiter, false, true, CrystalTypes.EffectTypes.DUST);
                                NBTTagCompound cmpd = new NBTTagCompound();
                                if(peded.hasTagCompound())
                                {
                                    cmpd =peded.getTagCompound();
                                }

                                cmpd.setTag("coineffect",effect.writeCustomPotionEffectToNBT(new NBTTagCompound()));
                                peded.setTagCompound(cmpd);

                                for(int i=0; i<pedestal; i++)
                                {
                                    EntityItem items1 = new EntityItem(worldIn, posX + 0.5, posY + 1.0, posZ + 0.5, peded.copy());
                                    items1.onCollideWithPlayer(player);
                                    //worldIn.spawnEntity(items1);
                                }
                            }
                        }
                        else player.sendStatusMessage(new TextComponentString(TextFormatting.WHITE +"Not Enough Dust To Make Effect Upgrade"),true);
                    }
                    else if(pressurePlate==0 && arrow==0 &&  paper==0 && pedestal==0&& coin>=1)
                    {
                        if(count/coin >= minimumDustRequired)
                        {
                            if(!worldIn.isRemote)
                            {
                                /*
                                ItemStack stack = ItemStack.EMPTY;
                                stack = new ItemStack(ItemRegistry.effectUpgrade);
                                */
                                PotionEffect effect = EffectPicker.getEffectFromInputs(red/coin, blue/coin, yellow/coin, white/coin, black/coin, 1,potencyLimiter, false, true, CrystalTypes.EffectTypes.DUST);
                                NBTTagCompound cmpd = new NBTTagCompound();
                                if(coined.hasTagCompound())
                                {
                                    cmpd =coined.getTagCompound();
                                }

                                cmpd.setTag("coineffect",effect.writeCustomPotionEffectToNBT(new NBTTagCompound()));
                                coined.setTagCompound(cmpd);

                                for(int i=0; i<coin; i++)
                                {
                                    EntityItem items1 = new EntityItem(worldIn, posX + 0.5, posY + 1.0, posZ + 0.5, coined.copy());
                                    items1.onCollideWithPlayer(player);
                                    //worldIn.spawnEntity(items1);
                                }
                            }
                        }
                        else player.sendStatusMessage(new TextComponentString(TextFormatting.WHITE +"Not Enough Dust To Make Effect Upgrade"),true);
                    }

                    else if(pressurePlate==0 && paper>=0 && coin==0&& pedestal==0&& arrow>=1)
                    {
                        if(count/arrow >= minimumDustRequired)
                        {
                            if(!worldIn.isRemote)
                            {
                                List<PotionEffect> effects = Lists.<PotionEffect>newArrayList();
                                PotionEffect effect = EffectPicker.getEffectFromInputs(red/arrow, blue/arrow, yellow/arrow, white/arrow, black/arrow, 20 * count/arrow,potencyLimiter, false, true, CrystalTypes.EffectTypes.DUST);
                                effects.add(effect);
                                //NBTTagCompound cmpd = new NBTTagCompound();
                                //cmpd.setTag("CustomPotionEffects",effect.writeCustomPotionEffectToNBT(new NBTTagCompound()));
                                //cmpd.setTag("Potion",new NBTTagString(effect.getPotion().getRegistryName().toString()));
                                //stack.setTagCompound(cmpd);
                                ItemStack itemstack1 = new ItemStack(ItemRegistry.dustTippedArrow);
                                //PotionUtils.addPotionToItemStack(itemstack1, new PotionType(new PotionEffect[]{effect}));
                                NBTTagCompound cmpd = new NBTTagCompound();
                                cmpd.setInteger("CustomPotionColor",effect.getPotion().getLiquidColor());
                                itemstack1.setTagCompound(cmpd);
                                PotionUtils.appendEffects(itemstack1, effects);
                                String s1 = I18n.translateToLocal(effect.getEffectName()).trim();
                                itemstack1.setStackDisplayName("Arrow of " + s1);



                                //From Custom Arrow Crafting Manager
                                /*ItemStack itemstack2 = new ItemStack(Items.TIPPED_ARROW, count);
                                PotionUtils.addPotionToItemStack(itemstack2, PotionType.getPotionTypeForName(effect.getEffectName()));
                                PotionUtils.appendEffects(itemstack2, PotionUtils.getFullEffectsFromItem(itemstack));
                                */

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

    public boolean containsPaper(ItemStack stack)
    {
        if(stack.getItem().equals(Items.PAPER))
        {
            return true;
        }
        else return false;
    }

    public boolean containsArrow(ItemStack stack)
    {
        if(stack.getItem() instanceof ItemArrow)
        {
            return true;
        }
        else return false;
    }

    public boolean containsPressurePlate(ItemStack stack)
    {
        if(Block.getBlockFromItem(stack.getItem()) instanceof BlockBasePressurePlate)
        {
            return true;
        }
        else return false;
    }

    public boolean containsCoin(ItemStack stack)
    {
        if(stack.getItem() instanceof ipuBasic || stack.getItem() instanceof ItemCoin)
        {
            return true;
        }
        else return false;
    }

    public boolean containsPedestal(ItemStack stack)
    {
        if(Block.getBlockFromItem(stack.getItem()) instanceof BlockPedestal)
        {
            return true;
        }
        else return false;
    }

    public boolean containsStone(ItemStack stack)
    {
        if(stack.getItem().equals(Item.getItemFromBlock(Blocks.STONE)))
        {
            return true;
        }
        else return false;
    }
}
