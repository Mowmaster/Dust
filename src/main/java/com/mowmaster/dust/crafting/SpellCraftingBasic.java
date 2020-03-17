package com.mowmaster.dust.crafting;

import com.mowmaster.dust.item.ItemColorDust;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FlintAndSteelItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;


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
        int count=0;

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


                        /*System.out.println("Red: " + red + " rgb: " +rgbRed);
                        System.out.println("Green: "  + green + " rgb: " + rgbGreen);
                        System.out.println("Blue: " + blue + " rgb: " + rgbBlue);*/
                        player.sendMessage(new StringTextComponent(TextFormatting.GOLD +""+color));
                        //System.out.println(color);

                        //removes fire block???
                        //worldIn.removeBlock(new BlockPos(posX, posY + 1, posZ), false);
                        worldIn.createExplosion(new ItemEntity(worldIn, posX, posY, posZ), posX + 0.5, posY + 2.0, posZ + 0.5, 1.0F, Explosion.Mode.NONE);

                        if(stone == 0 && bowl == 0)
                        {
                            int amp = getPotency(white,black,5 );
                            if(white >= black)
                            {
                                player.addPotionEffect(new EffectInstance(SpellCraftingEffectGood.instance().getResult(color),count*20,amp));
                            }
                            else
                            {
                                player.addPotionEffect(new EffectInstance(SpellCraftingEffectBad.instance().getResult(color),count*20,amp));
                            }
                        }

                        if (stone > 0) {

                            if(color == 16777215 && worldIn.isNightTime())
                            {
                                color = 0;
                            }

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

                        if(bowl>0)
                        {
                            //Make Black color at night
                            if(color == 16777215 && worldIn.isNightTime())
                            {
                                color = 0;
                            }

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

    //ToDo: addconfig for potency cap
    private static int getPotency(int white, int black, int potencyCap)
    {
        int amp=0;
        int potencycount=Math.abs(white-black);
        if(potencycount>=0 && potencycount<10){amp=0;}
        if(potencycount>=10 && potencycount<30){amp=1;}
        if(potencycount>=30 && potencycount<60){amp=2;}
        if(potencycount>=60 && potencycount<100){amp=3;}
        if(potencycount>=100 && potencycount<150){amp=4;}
        if(potencycount>=150 && potencycount<210){amp=5;}
        if(potencycount>=210 && potencycount<280){amp=6;}
        if(potencycount>=280 && potencycount<360){amp=7;}
        if(potencycount>=360 && potencycount<450){amp=8;}
        if(potencycount>=450 && potencycount<550){amp=9;}
        if(potencycount>=550){amp=10;}

        if(amp > 5)
        {
            amp = 5;
        }

        return amp;
    }
}