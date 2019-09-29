package com.mowmaster.dust.crafting;

import com.mowmaster.dust.item.ItemColorDust;
import com.mowmaster.dust.item.ItemDust;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FlintAndSteelItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

import static com.mowmaster.dust.references.Reference.MODID;


@Mod.EventBusSubscriber
public class SpellCraftingBasic
{
    @SubscribeEvent()
    public static void SpellCrafting(PlayerInteractEvent.RightClickBlock event)
    {
        World worldIn = event.getWorld();
        Hand hand = event.getHand();
        BlockState state = worldIn.getBlockState(event.getPos());
        PlayerEntity player = event.getEntityPlayer();

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

                    red = r%256;
                    green = g%256;
                    blue = b%256;

                    double rgbRed = red;
                    double rgbGreen = green;
                    double rgbBlue = blue;

                    if(black>0 || white>0)
                    {
                        if (black>white)
                        {
                            rgbRed = 0;
                            rgbGreen = 0;
                            rgbBlue = 0;
                            count = black;
                        }
                        else
                        {
                            rgbRed = 255;
                            rgbGreen = 255;
                            rgbBlue = 255;
                            count = white;
                        }
                    }

                    int color = CalculateColor.getColorFromRGB(rgbRed,rgbGreen,rgbBlue);
                    /*System.out.println("Red: " + red + " rgb: " +rgbRed);
                    System.out.println("Green: "  + green + " rgb: " + rgbGreen);
                    System.out.println("Blue: " + blue + " rgb: " + rgbBlue);*/
                    player.sendMessage(new StringTextComponent(TextFormatting.GOLD +"" +color));
                    //System.out.println(color);


                    worldIn.removeBlock(new BlockPos(posX, posY + 1, posZ), false);
                    worldIn.createExplosion(new ItemEntity(worldIn, posX, posY, posZ), posX + 0.5, posY + 2.0, posZ + 0.5, 1.0F, Explosion.Mode.NONE);
                    if (stone > 0) {
                        //if(red>0)
                        //{
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
                        ItemStack stacked = new ItemStack(ItemColorDust.DUST,1);
                        CompoundNBT nbt = new CompoundNBT();
                        nbt.putInt("color",color);
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