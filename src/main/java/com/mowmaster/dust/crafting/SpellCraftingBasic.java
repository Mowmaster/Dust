package com.mowmaster.dust.crafting;

import com.mowmaster.dust.item.ItemDust;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FlintAndSteelItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

import static com.mowmaster.dust.blocks.BlockDustStone.STONE_RED;

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

        int red=0;
        int blue=0;
        int green=0;
        int white=0;
        int black=0;
        int stone=0;

        if(!worldIn.isRemote) {
            //List<EntityItem> item = player.getEntityWorld().getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(posX-1, posY-1, posZ-1, posX+1, posY+1, posZ+1));
            List<ItemEntity> items = player.getEntityWorld().getEntitiesWithinAABB(ItemEntity.class, new AxisAlignedBB(posX - 3, posY - 3, posZ - 3, posX + 3, posY + 3, posZ + 3));
            ItemStack coined = ItemStack.EMPTY;
            if ((player.getHeldItem(hand) != null)) {
                if (player.getHeldItem(hand).getItem() instanceof FlintAndSteelItem) {

                    for (ItemEntity item : items) {
                        ItemStack stack = item.getItem();
                        if(stack.getItem().equals(Items.STONE))
                        {
                            stone+=stack.getCount();
                            item.remove();
                        }


                            if(stack.getItem().equals(ItemDust.DUST_RED))
                            {
                                red+=stack.getCount();
                                item.remove();
                            }
                            if(stack.getItem().equals(ItemDust.DUST_GREEN))
                            {
                                green+=stack.getCount();
                                item.remove();
                            }
                            if(stack.getItem().equals(ItemDust.DUST_BLUE))
                            {
                                blue+=stack.getCount();
                                item.remove();
                            }
                             if(stack.getItem().equals(ItemDust.DUST_WHITE))
                            {
                                white+=stack.getCount();
                                item.remove();
                            }
                            if(stack.getItem().equals(ItemDust.DUST_BLACK))
                            {
                                black+=stack.getCount();
                                item.remove();
                            }

                    }
                    System.out.println("Stone: " + stone);
                    System.out.println("Red: " + red);
                    System.out.println("Green: " + green);
                    System.out.println("Blue: " + blue);
                    System.out.println("White: " + white);
                    System.out.println("Black: " + black);
                    worldIn.removeBlock(new BlockPos(posX, posY + 1, posZ), false);
                    worldIn.createExplosion(new ItemEntity(worldIn, posX, posY, posZ), posX + 0.5, posY + 2.0, posZ + 0.5, 1.0F, Explosion.Mode.NONE);
                    if (stone > 0) {
                        //if(red>0)
                        //{
                            for(int i=stone;i>0;i--)
                            {


                                if(red>=2)
                                {
                                    stone--;
                                    red-=2;
                                    ItemEntity itemEn = new ItemEntity(worldIn,posX,posY+1,posZ,SpellCraftingStone.instance().getResult(new ItemStack(ItemDust.DUST_RED,2)));
                                    itemEn.setInvulnerable(true);
                                    worldIn.addEntity(itemEn);

                                }

                                if (green >= 2) {
                                    stone--;
                                    green -= 2;
                                    ItemEntity itemEn = new ItemEntity(worldIn,posX,posY+1,posZ,SpellCraftingStone.instance().getResult(new ItemStack(ItemDust.DUST_GREEN,2)));
                                    itemEn.setInvulnerable(true);
                                    worldIn.addEntity(itemEn);
                                }

                                if(blue>=2)
                                {
                                    stone--;
                                    blue-=2;
                                    ItemEntity itemEn = new ItemEntity(worldIn,posX,posY+1,posZ,SpellCraftingStone.instance().getResult(new ItemStack(ItemDust.DUST_BLUE,2)));
                                    itemEn.setInvulnerable(true);
                                    worldIn.addEntity(itemEn);                                }

                                if(white>=2)
                                {
                                    stone--;
                                    white-=2;
                                    ItemEntity itemEn = new ItemEntity(worldIn,posX,posY+1,posZ,SpellCraftingStone.instance().getResult(new ItemStack(ItemDust.DUST_WHITE,2)));
                                    itemEn.setInvulnerable(true);
                                    worldIn.addEntity(itemEn);                                }

                                if(black>=2)
                                {
                                    stone--;
                                    black-=2;
                                    ItemEntity itemEn = new ItemEntity(worldIn,posX,posY+1,posZ,SpellCraftingStone.instance().getResult(new ItemStack(ItemDust.DUST_BLACK,2)));
                                    itemEn.setInvulnerable(true);
                                    worldIn.addEntity(itemEn);                                }

                                if(red<2 && green <2 && blue<2 && white <2 && black<2 && stone>0)
                                {
                                    ItemEntity itemEn = new ItemEntity(worldIn,posX,posY+1,posZ,new ItemStack(Items.STONE,stone));
                                    stone=0;
                                    itemEn.setInvulnerable(true);
                                    worldIn.addEntity(itemEn);
                                }
                            }
                        //}
                        /*if(green>0)
                        {
                            for(int i=stone;i>0;i--) {
                                if (green >= 2) {
                                    green -= 2;
                                    ItemEntity itemEn = new ItemEntity(worldIn,posX,posY+1,posZ,SpellCraftingStone.instance().getResult(new ItemStack(ItemDust.DUST_GREEN,2)));
                                    itemEn.setInvulnerable(true);
                                    worldIn.addEntity(itemEn);
                                }
                            }
                        }*/
                        /*if(blue>0)
                        {
                            for(int i=stone;i>0;i--)
                            {
                                if(blue>=2)
                                {
                                    blue-=2;
                                    ItemEntity itemEn = new ItemEntity(worldIn,posX,posY+1,posZ,SpellCraftingStone.instance().getResult(new ItemStack(ItemDust.DUST_BLUE,2)));
                                    itemEn.setInvulnerable(true);
                                    worldIn.addEntity(itemEn);                                }
                            }
                        }*/
                        /*if(white>0)
                        {
                            for(int i=stone;i>0;i--)
                            {
                                if(white>=2)
                                {
                                    white-=2;
                                    ItemEntity itemEn = new ItemEntity(worldIn,posX,posY+1,posZ,SpellCraftingStone.instance().getResult(new ItemStack(ItemDust.DUST_WHITE,2)));
                                    itemEn.setInvulnerable(true);
                                    worldIn.addEntity(itemEn);                                }
                            }
                        }*/
                        /*if(black>0)
                        {
                            for(int i=stone;i>0;i--)
                            {
                                if(black>=2)
                                {
                                    black-=2;
                                    ItemEntity itemEn = new ItemEntity(worldIn,posX,posY+1,posZ,SpellCraftingStone.instance().getResult(new ItemStack(ItemDust.DUST_BLACK,2)));
                                    itemEn.setInvulnerable(true);
                                    worldIn.addEntity(itemEn);                                }
                            }
                        }*/



                    }
                }
            }
        }
    }
}