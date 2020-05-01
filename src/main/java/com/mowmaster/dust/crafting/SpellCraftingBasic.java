package com.mowmaster.dust.crafting;

import com.mowmaster.dust.blocks.BlockTrap;
import com.mowmaster.dust.item.ItemColorCrystal;
import com.mowmaster.dust.item.ItemColorDust;
import com.mowmaster.dust.item.ItemSpellScroll;
import com.mowmaster.dust.references.Reference;
import com.mowmaster.dust.tiles.TileTrap;
import net.minecraft.block.*;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

import static com.mowmaster.dust.blocks.BlockTrap.WATERLOGGED;


@Mod.EventBusSubscriber
public class SpellCraftingBasic
{
    @SubscribeEvent()
    public static void SpellCrafting(PlayerInteractEvent.RightClickBlock event)
    {
        World worldIn = event.getWorld();
        Hand hand = event.getHand();
        BlockState state = worldIn.getBlockState(event.getPos());
        PlayerEntity player = event.getPlayer();

        int posX = event.getPos().getX();
        int posY = event.getPos().getY();
        int posZ = event.getPos().getZ();

        double r = 0;
        double g = 0;
        double b = 0;
        double red=0;
        double blue=0;
        double green=0;
        int white=0;
        int black=0;

        double cr = 0;
        double cg = 0;
        double cb = 0;
        double cred=0;
        double cblue=0;
        double cgreen=0;

        int stone=0;
        int bowl=0;
        int paper=0;
        int count=0;
        int plate=0;
        int crystal=0;
        ItemStack trapped = ItemStack.EMPTY;

        if(!worldIn.isRemote) {
            //List<EntityItem> item = player.getEntityWorld().getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(posX-1, posY-1, posZ-1, posX+1, posY+1, posZ+1));
            List<ItemEntity> items = player.getEntityWorld().getEntitiesWithinAABB(ItemEntity.class, new AxisAlignedBB(posX - 3, posY - 3, posZ - 3, posX + 3, posY + 3, posZ + 3));
            ItemStack coined = ItemStack.EMPTY;
            if ((player.getHeldItem(hand) != null)) {
                if (player.getHeldItem(hand).getItem() instanceof FlintAndSteelItem) {

                    for (ItemEntity item : items) {
                        ItemStack stack = item.getItem();

                        if(stack.getItem().equals(Items.BOWL))
                        {
                            bowl+=stack.getCount();
                        }

                        if(stack.getItem().equals(Items.STONE))
                        {
                            stone+=stack.getCount();
                            item.remove();
                        }

                        if(stack.getItem().equals(Items.PAPER))
                        {
                            paper+=stack.getCount();
                            item.remove();
                        }

                        if(stack.getItem().equals(Items.DIAMOND))
                        {
                            crystal+=stack.getCount();
                            item.remove();
                        }

                        if(containsPressurePlate(stack))
                        {
                            trapped = stack.copy();
                            plate++;
                            item.remove();
                        }

                        if(stack.getItem() instanceof ItemColorDust)
                        {
                            if(stack.getTag().getInt("color") == 0)
                            {
                                black+=stack.getCount();
                                item.remove();
                            }
                            else if(stack.getTag().getInt("color") == 16777215)
                            {
                                white+=stack.getCount();
                                item.remove();
                            }
                            else if(stack.getTag().contains("combine"))
                            {
                                if(!stack.getTag().getBoolean("combine"))
                                {
                                    //had '+ black + white' but wasnt sure why
                                    if(cr + cg + cb == 0)
                                    {
                                        {
                                            int[] rgbColors = CalculateColor.getRGBColorFromInt(stack.getTag().getInt("color"));
                                            cr+=rgbColors[0];
                                            cg+=rgbColors[1];
                                            cb+=rgbColors[2];
                                            count+=stack.getCount();
                                            item.remove();
                                        }
                                    }
                                    else
                                    {
                                        count+=stack.getCount();
                                        item.remove();
                                    }
                                }
                            }
                            else
                            {
                                //System.out.println(stack.getTag().getInt("color"));
                                double[] rgbColors = CalculateColor.getRGBColorFromStack(stack);
                                r+=rgbColors[0];
                                g+=rgbColors[1];
                                b+=rgbColors[2];
                                count+=stack.getCount();
                                item.remove();
                            }
                        }
                    }


                    if(count > 0)
                    {
                        red = r%256;
                        green = g%256;
                        blue = b%256;

                        cred = cr%256;
                        cgreen = cg%256;
                        cblue = cb%256;

                        //Combination color, need to not use combi colors for mixing but use for stone
                        //combi colors cant be mixed
                        //combi colors override normal color mix for stone


                        double rgbRed = red;
                        double rgbGreen = green;
                        double rgbBlue = blue;
                        double crgbRed = cred;
                        double crgbGreen = cgreen;
                        double crgbBlue = cblue;

                        int color = 0;


                        if(stone>0 && (black>0 || white>0))
                        {
                            if (black>white)
                            {
                                rgbRed = 0;
                                rgbGreen = 0;
                                rgbBlue = 0;
                                count = black + white;
                            }
                            else
                            {
                                rgbRed = 255;
                                rgbGreen = 255;
                                rgbBlue = 255;
                                count = white + black;
                            }
                        }

                        if(bowl > 0)
                        {
                            color = CalculateColor.getColorFromRGB(rgbRed,rgbGreen,rgbBlue);
                        }
                        else if (r+g+b > 0)
                        {
                            color = CalculateColor.getColorFromRGB(rgbRed,rgbGreen,rgbBlue);
                        }
                        else
                        {
                            color = CalculateColor.getColorFromRGB(crgbRed,crgbGreen,crgbBlue);
                        }

                        if(color == 16777215 && worldIn.isNightTime())
                        {
                            color = 0;
                        }


                        /*System.out.println("Red: " + red + " rgb: " +rgbRed);
                        System.out.println("Green: "  + green + " rgb: " + rgbGreen);
                        System.out.println("Blue: " + blue + " rgb: " + rgbBlue);*/
                        player.sendMessage(new StringTextComponent(TextFormatting.GOLD +""+color));
                        //System.out.println(color);

                        //removes fire block???
                        //worldIn.removeBlock(new BlockPos(posX, posY + 1, posZ), false);
                        worldIn.createExplosion(new ItemEntity(worldIn, posX, posY, posZ), posX + 0.5, posY + 2.0, posZ + 0.5, 1.0F, Explosion.Mode.NONE);

                        if(stone == 0 && bowl == 0 && paper == 0 && plate == 0 && crystal == 0)
                        {
                            int amp = getPotency(white,black,5 );
                            if(white >= black)
                            {
                                player.addPotionEffect(new EffectInstance(SpellCraftingEffectGood.instance().getResult(color),count*getPotionModifier(false,color),amp));
                                count=0;
                            }
                            else
                            {
                                player.addPotionEffect(new EffectInstance(SpellCraftingEffectBad.instance().getResult(color),count*getPotionModifier(true,color),amp));
                                count=0;
                            }
                        }

                        else if (plate > 0) {

                            int amp = getPotency(white,black,5 );
                            EffectInstance effect = null;

                            if(white >= black)
                            {
                                effect = new EffectInstance(SpellCraftingEffectGood.instance().getResult(color),count*getPotionModifier(false,color),amp);
                            }
                            else
                            {
                                effect = new EffectInstance(SpellCraftingEffectBad.instance().getResult(color),count*getPotionModifier(true,color),amp);
                            }

                            if(effect != null)
                            {
                                worldIn.setBlockState(new BlockPos(posX,posY+1,posZ), Blocks.AIR.getDefaultState());
                                if(trapped.getItem().equals(Item.getItemFromBlock(Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE)) || trapped.getItem().equals(Item.getItemFromBlock(Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE)))
                                {
                                    if(player.isInWater())
                                    {
                                        worldIn.setBlockState(new BlockPos(posX,posY+1,posZ), BlockTrap.BLOCKTRAPPLAYER.getDefaultState().with(WATERLOGGED,true));
                                    }
                                    else worldIn.setBlockState(new BlockPos(posX,posY+1,posZ), BlockTrap.BLOCKTRAPPLAYER.getDefaultState());
                                }
                                else
                                {
                                    if(player.isInWater())
                                    {
                                        worldIn.setBlockState(new BlockPos(posX,posY+1,posZ), BlockTrap.BLOCKTRAPMOB.getDefaultState().with(WATERLOGGED,true));
                                    }
                                    worldIn.setBlockState(new BlockPos(posX,posY+1,posZ), BlockTrap.BLOCKTRAPMOB.getDefaultState());
                                }

                                TileEntity tileentity = worldIn.getTileEntity(new BlockPos(posX,posY+1,posZ));
                                if (tileentity instanceof TileTrap) {
                                    ((TileTrap) tileentity).setTrapEffect(effect);
                                    plate = 0;
                                }
                            }
                        }

                        else if (paper > 0) {

                            int amp = getPotency(white,black,5 );
                            EffectInstance effect = null;

                            if(white >= black)
                            {
                                effect = new EffectInstance(SpellCraftingEffectGood.instance().getResult(color),count*getPotionModifier(false,color),amp);
                            }
                            else
                            {
                                effect = new EffectInstance(SpellCraftingEffectBad.instance().getResult(color),count*getPotionModifier(true,color),amp);
                            }

                            if(effect != null)
                            {
                                ItemStack scroll = new ItemStack(ItemSpellScroll.SPELLSCROLL,1);
                                int dur = effect.getDuration()/paper;
                                CompoundNBT nbt = new CompoundNBT();
                                nbt.putInt(Reference.MODID + "Potion",Effect.getId(effect.getPotion()));
                                nbt.putInt(Reference.MODID + "Amplifier",effect.getAmplifier());
                                nbt.putInt(Reference.MODID + "Duration",(dur));
                                nbt.putInt(Reference.MODID + "Color",(color));
                                scroll.setTag(nbt);
                                TranslationTextComponent potionname = new TranslationTextComponent(scroll.getTranslationKey() + ".crafted");
                                potionname.appendSibling(effect.getPotion().getEffect().getDisplayName());
                                scroll.setDisplayName(potionname);

                                for(int i=paper;i>0;i--)
                                {
                                    paper-=1;
                                    ItemEntity itemEn = new ItemEntity(worldIn,posX,posY+1,posZ,scroll);
                                    itemEn.setInvulnerable(true);
                                    itemEn.onCollideWithPlayer(player);
                                    //worldIn.addEntity(itemEn);
                                }
                            }
                        }

                        else if (crystal > 0) {

                            for(int i=crystal;i>0;i--)
                            {
                                if(count>=8)
                                {
                                    count-=8;
                                    crystal-=1;
                                    ItemStack stacked = new ItemStack(ItemColorCrystal.CRYSTAL,1);
                                    CompoundNBT nbt = new CompoundNBT();
                                    nbt.putInt("color",color);
                                    stacked.setTag(nbt);
                                    ItemEntity itemEn = new ItemEntity(worldIn,posX,posY+1,posZ,stacked);
                                    itemEn.setInvulnerable(true);
                                    worldIn.addEntity(itemEn);
                                }
                                else
                                {
                                    ItemEntity itemEn = new ItemEntity(worldIn,posX,posY+1,posZ,new ItemStack(Items.DIAMOND,crystal));
                                    crystal-=crystal;
                                    itemEn.setInvulnerable(true);
                                    worldIn.addEntity(itemEn);
                                }
                            }
                        }

                        else if (stone > 0) {

                            for(int i=stone;i>0;i--)
                            {
                                if(count>=2)
                                {
                                    count-=2;
                                    stone-=1;
                                    ItemEntity itemEn = new ItemEntity(worldIn,posX,posY+1,posZ,new ItemStack(SpellCraftingStone.instance().getResult(color).getItem(),1));
                                    itemEn.setInvulnerable(true);
                                    worldIn.addEntity(itemEn);
                                }
                                else
                                {
                                    ItemEntity itemEn = new ItemEntity(worldIn,posX,posY+1,posZ,new ItemStack(Items.STONE,stone));
                                    stone-=stone;
                                    itemEn.setInvulnerable(true);
                                    worldIn.addEntity(itemEn);
                                }
                            }
                        }

                        else if(bowl>0)
                        {
                            //NEED TO ADD ANOTHER TAG TO ITEM TO MAKE IT NOT USEABLE IN COMBINING AGAIN!!!
                            ItemStack stacked = new ItemStack(ItemColorDust.DUST,1);
                            CompoundNBT nbt = new CompoundNBT();
                            nbt.putInt("color",color);
                            if(color == 255 || color == 16711680 || color == 65280) {} else {nbt.putBoolean("combine",false);}
                            stacked.setTag(nbt);
                            stacked.setCount(count);
                            ItemEntity itemEn = new ItemEntity(worldIn,posX,posY+1,posZ,stacked);
                            itemEn.setInvulnerable(true);
                            worldIn.addEntity(itemEn);
                        }
                    }
                }
            }
        }
    }

    public static int getPotionModifier(boolean isBad, int color)
    {
        int count = 20;
        Effect getEffect = (isBad)?(SpellCraftingEffectBad.instance().getResult(color)):(SpellCraftingEffectGood.instance().getResult(color));
        if(getEffect.isInstant())
        {
            count = 1;
        }

        return count;
    }

    //ToDo: addconfig for potency cap
    private static int getPotency(int white, int black, int potencyCap)
    {
        int amp=0;
        int potencycount=Math.abs(white-black);
        if(potencycount>=0 && potencycount<64){amp=0;}
        if(potencycount>=64 && potencycount<128){amp=1;}
        if(potencycount>=128 && potencycount<192){amp=2;}
        if(potencycount>=192 && potencycount<256){amp=3;}
        if(potencycount>=256 && potencycount<320){amp=4;}
        if(potencycount>=320 && potencycount<384){amp=5;}
        if(potencycount>=384 && potencycount<448){amp=6;}
        if(potencycount>=448 && potencycount<512){amp=7;}
        if(potencycount>=512 && potencycount<576){amp=8;}
        if(potencycount>=576 && potencycount<640){amp=9;}
        if(potencycount>=640){amp=10;}

        if(amp > 4)
        {
            amp = 4;
        }

        return amp;
    }

    public static boolean containsPressurePlate(ItemStack stack)
    {
        if(Block.getBlockFromItem(stack.getItem()) instanceof AbstractPressurePlateBlock)
        {
            return true;
        }
        else return false;
    }
}