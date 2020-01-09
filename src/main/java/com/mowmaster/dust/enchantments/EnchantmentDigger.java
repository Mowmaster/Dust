package com.mowmaster.dust.enchantments;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Random;

public class EnchantmentDigger extends Enchantment
{
    // DOES NOT WORK IN PEDESTAL WITH BREAKER UPGRADE
    public EnchantmentDigger() {
        super(Rarity.VERY_RARE, EnumEnchantmentType.DIGGER, new EntityEquipmentSlot[] {EntityEquipmentSlot.MAINHAND});
        setName("enchantDigger");
        setRegistryName(new ResourceLocation("dust", "enchantDigger"));
    }

    /**
     * Returns the minimal value of enchantability needed on the enchantment level passed.
     */
    public int getMinEnchantability(int enchantmentLevel)
    {
        return enchantmentLevel * 15;
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
        return 10;
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void diggerEvent(BlockEvent.BreakEvent event)
    {
        World world = event.getWorld();
        EntityPlayer player = event.getPlayer();
        BlockPos pos = event.getPos();
        Block block = event.getState().getBlock();
        IBlockState state = world.getBlockState(pos);

        if(!world.isRemote)
        {
            if(EnchantmentHelper.getEnchantmentLevel(EnchantmentRegistry.enchantDigger,player.getHeldItem(player.getActiveHand()))!=0)
            {
                ItemStack tool = player.getHeldItem(player.getActiveHand());
                if (player.swingingHand == null) {return;}
                // raytrace stuffs (client)
                //RayTraceResult ray = player.rayTrace(200,1.0f);
                //IBlockState blockLookingAt = player.world.getBlockState(new BlockPos(ray.getBlockPos().getX(),ray.getBlockPos().getY(),ray.getBlockPos().getZ()));
                RayTraceResult result = ForgeHooks.rayTraceEyes(player, player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue() + 1);
                EnumFacing facing = result.sideHit.getOpposite();
                String type = player.getHeldItem(player.getActiveHand()).getItem().getToolClasses(player.getHeldItem(player.getActiveHand())).toString();
                int damage = player.getHeldItem(player.getActiveHand()).getItemDamage();
                int lvl = EnchantmentHelper.getEnchantmentLevel(EnchantmentRegistry.enchantDigger,player.getHeldItem(player.getActiveHand()));
                float blockHardness = world.getBlockState(pos).getBlockHardness(world,pos);
                int zmin=0;
                int zmax=0;
                int xmin=0;
                int xmax=0;
                int ymin=0;
                int ymax=0;

                if(player.capabilities.isFlying)
                {
                    if(facing.equals(EnumFacing.DOWN) || facing.equals(EnumFacing.UP)) {zmin=-lvl;zmax=+lvl;xmin=-lvl;xmax=+lvl;ymin=0;ymax=0;}
                    else if(facing.equals(EnumFacing.WEST) || facing.equals(EnumFacing.EAST)) {zmin=-lvl;zmax=+lvl;xmin=0;xmax=0;ymin=-lvl;ymax=+lvl;}
                    else if(facing.equals(EnumFacing.NORTH) || facing.equals(EnumFacing.SOUTH)) {zmin=0;zmax=0;xmin=-lvl;xmax=+lvl;ymin=-lvl;ymax=+lvl;}
                }
                else
                {
                    if(facing.equals(EnumFacing.DOWN) || facing.equals(EnumFacing.UP)) {zmin=-lvl;zmax=+lvl;xmin=-lvl;xmax=+lvl;ymin=0;ymax=0;}
                    else if(facing.equals(EnumFacing.WEST) || facing.equals(EnumFacing.EAST)) {zmin=-lvl;zmax=+lvl;xmin=0;xmax=0;ymin=-1;ymax=+((2*lvl)-1);}
                    else if(facing.equals(EnumFacing.NORTH) || facing.equals(EnumFacing.SOUTH)) {zmin=0;zmax=0;xmin=-lvl;xmax=+lvl;ymin=-1;ymax=+((2*lvl)-1);}
                }

                if(player.isSneaking()) {}
                else
                {
                    for(int c=zmin;c<=zmax;c++) {
                        for (int a = xmin; a <= xmax; a++)
                            for (int b = ymin; b <= ymax; b++)
                            {
                                Item item = world.getBlockState(pos.add(a,b,c)).getBlock().getItemDropped(world.getBlockState(pos.add(a,b,c)),new Random(),0);
                                ItemStack items = new ItemStack(item,1);
                                BlockPos newpos = pos.add(a,b,c);
                                IBlockState newblockstate = world.getBlockState(pos.add(a,b,c));
                                Block newblock = world.getBlockState(pos.add(a,b,c)).getBlock();
                                float newblockHardness = world.getBlockState(pos.add(a,b,c)).getBlockHardness(world,newpos);
                                int maxdur = tool.getMaxDamage();
                                int damdone = tool.getItemDamage();
                                if (blockHardness>=newblockHardness && !newblock.equals(Blocks.BEDROCK) && tool.getItem() instanceof ItemTool && (Math.subtractExact(maxdur,damdone)>=0) && !newblock.isAir(newblockstate,world,newpos)) {
                                    newblock.harvestBlock(world, player, newpos, world.getBlockState(newpos), null, player.getHeldItem(EnumHand.MAIN_HAND));
                                    player.getHeldItem(player.getActiveHand()).damageItem(1,player);
                                    world.setBlockToAir(newpos);
                                }
                                else continue;
                            }
                    }
                }
            }
        }
    }


}
