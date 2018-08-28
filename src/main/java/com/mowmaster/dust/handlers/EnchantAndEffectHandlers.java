package com.mowmaster.dust.handlers;

import com.mowmaster.dust.blocks.BlockLeaf;
import com.mowmaster.dust.effects.PotionMagnetism;
import com.mowmaster.dust.effects.PotionRegistry;
import com.mowmaster.dust.enchantments.EnchantmentQuickPace;
import com.mowmaster.dust.enchantments.EnchantmentRegistry;
import com.mowmaster.dust.items.ItemRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentMending;
import net.minecraft.enchantment.EnchantmentUntouching;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.init.*;
import net.minecraft.item.*;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionAttackDamage;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.StatBasic;
import net.minecraft.util.*;
import net.minecraft.util.datafix.fixes.PotionWater;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.xml.stream.events.Attribute;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class EnchantAndEffectHandlers
{
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void blockBreakEventLowest(BlockEvent.BreakEvent event)
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

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void blockHarvestDropsEventLowest(BlockEvent.HarvestDropsEvent event)
    {
        World world = event.getWorld();
        EntityPlayer player = event.getHarvester();
        BlockPos pos = event.getPos();
        List<ItemStack> stackie = event.getDrops();
        if(!world.isRemote)
        {
            if (player.swingingHand == null) {return;}
            if(EnchantmentHelper.getEnchantmentLevel(EnchantmentRegistry.enchantmentSmelter,player.getHeldItem(player.getActiveHand()))!=0)
            {
                ItemStack tool = player.getHeldItem(player.getActiveHand());

                int lvl = EnchantmentHelper.getEnchantmentLevel(EnchantmentRegistry.enchantmentSmelter,player.getHeldItem(player.getActiveHand()));
                if(tool.getItem() instanceof ItemTool)
                {
                    if(player.isSneaking() || FurnaceRecipes.instance().getSmeltingResult(stackie.get(0)).isEmpty()) {}
                    else
                    {
                        world.spawnEntity(new EntityItem(world, pos.getX() + 0.5,pos.getY() + 1.0,pos.getZ() + 0.5,FurnaceRecipes.instance().getSmeltingResult(stackie.get(0))));
                        event.getDrops().set(0,ItemStack.EMPTY);
                        player.getHeldItem(player.getActiveHand()).damageItem(1,player);
                        world.setBlockToAir(pos);
                    }
                }
            }
        }
    }


    private boolean flight = false;
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onFlight(TickEvent.PlayerTickEvent event)
    {
        int level = 0;
        int expLoss = 0;
        if(event.player.inventory.armorInventory.get(2) !=null && event.player.inventory.armorInventory.get(2).isItemEnchanted())
        {
            if(EnchantmentHelper.getEnchantmentLevel(EnchantmentRegistry.enchantmentFlight,event.player.inventory.armorInventory.get(2))!=0 && event.player.experienceTotal>0)
            {
                flight = true;

                level = EnchantmentHelper.getEnchantmentLevel(EnchantmentRegistry.enchantmentFlight,event.player.inventory.armorInventory.get(2));
                expLoss = (int)(4 * level);

                if(!event.player.world.isRemote && !event.player.isPotionActive(PotionRegistry.POTION_FLIGHT) && !event.player.onGround && event.player.capabilities.isFlying && (event.player.ticksExisted % expLoss == 0)) {
                    removeXp(event.player, 1);
                }
            }
            else flight=false;
        }
        else flight=false;


        if(event.player.isPotionActive(PotionRegistry.POTION_FLIGHT))
        {
            flight=true;
        }
        if(flight || event.player.isCreative() || event.player.isSpectator())
        {
            event.player.capabilities.allowFlying = true;
            event.player.fallDistance=0.0f;
        }
        else
        {
            flight=false;
            event.player.capabilities.isFlying = false;
            event.player.capabilities.allowFlying = false;
        }


    }

    private boolean stepup = false;
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onStepAssist(TickEvent.PlayerTickEvent event)
    {
        int level=0;
        if(event.player.inventory.armorInventory.get(1) !=null && event.player.inventory.armorInventory.get(1).isItemEnchanted())
        {
            if(EnchantmentHelper.getEnchantmentLevel(EnchantmentRegistry.enchantmentStepAssist,event.player.inventory.armorInventory.get(1))!=0)
            {
                stepup = true;
                level = EnchantmentHelper.getEnchantmentLevel(EnchantmentRegistry.enchantmentStepAssist,event.player.inventory.armorInventory.get(1));
            }
            else stepup=false;
        }
        else stepup=false;

        Float stepheightvalue = (float)(0.42 * level) + (float)1.0048174;

        if(stepup==true && !event.player.isSneaking())
        {
            event.player.stepHeight = stepheightvalue;
        }
        else if(stepup==true && event.player.isSneaking())
        {
            event.player.stepHeight=0.6f;
        }
        else
        {
            if(event.player.stepHeight >= 1.3448174)
            {
                event.player.stepHeight=0.6f;
            }
        }
    }

    //Use Aura once Aura is implimented, till then this will have to work.
    //Code snip from betweenlands for learning from and modifying to work here
    //seems like all values but be updated for it to display properly
    public static int removeXp(EntityPlayer player, int amount) {
        int startAmount = amount;
        while(amount > 0) {
            int barCap = player.xpBarCap();
            int barXp = (int) (barCap * player.experience);
            int removeXp = Math.min(barXp, amount);
            int newBarXp = barXp - removeXp;
            amount -= removeXp;//amount = amount-removeXp
/*
        System.out.println(event.player.xpBarCap());//7         9       11      11          xp to next level
        System.out.println(event.player.experience);//0.14285   0.0     0.0     0.0909      1/expierence = xp to next level
        System.out.println(event.player.experienceLevel);//0    1       2       2           #of levels
        System.out.println(event.player.experienceTotal);//1    7       16      17          total xp
 */
            player.experienceTotal -= removeXp;
            if(player.experienceTotal < 0) {
                player.experienceTotal = 0;
            }
            if(newBarXp == 0 && amount > 0) {
                player.experienceLevel--;
                if(player.experienceLevel < 0) {
                    player.experienceLevel = 0;
                    player.experienceTotal = 0;
                    player.experience = 0;
                    break;
                } else {
                    player.experience = 1.0F;
                }
            } else {
                player.experience = newBarXp / (float) barCap;
            }
        }
        return startAmount - amount;
    }


    private boolean runner = false;
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onMovingFast(TickEvent.PlayerTickEvent event)
    {
        int level=0;
        if(event.player.inventory.armorInventory.get(0) !=null && event.player.inventory.armorInventory.get(0).isItemEnchanted() || event.player.inventory.armorInventory.get(1) !=null && event.player.inventory.armorInventory.get(1).isItemEnchanted())
        {
            if(EnchantmentHelper.getEnchantmentLevel(EnchantmentRegistry.enchantmentQuickPace,event.player.inventory.armorInventory.get(0))!=0 || EnchantmentHelper.getEnchantmentLevel(EnchantmentRegistry.enchantmentQuickPace,event.player.inventory.armorInventory.get(1))!=0)
            {
                runner = true;
                level = EnchantmentHelper.getEnchantmentLevel(EnchantmentRegistry.enchantmentQuickPace,event.player.inventory.armorInventory.get(0));//on boots
            }
            else runner=false;
        }
        else runner=false;

        Float fastwalk = (float)(0.1 * level) + 0.1f;

        if(event.player.isPotionActive(PotionRegistry.POTION_QUICKNESS))
        {
            runner=true;
            int amp = event.player.getActivePotionEffect(PotionRegistry.POTION_QUICKNESS).getAmplifier() +1;
            fastwalk = (float)(0.15 * amp)  + fastwalk;
        }
        else if(EnchantmentHelper.getEnchantmentLevel(EnchantmentRegistry.enchantmentQuickPace,event.player.inventory.armorInventory.get(0))!=0 || EnchantmentHelper.getEnchantmentLevel(EnchantmentRegistry.enchantmentQuickPace,event.player.inventory.armorInventory.get(1))!=0){}
        else{runner=false;}

        if (runner == true) {event.player.capabilities.setPlayerWalkSpeed(fastwalk);}
        else {event.player.capabilities.setPlayerWalkSpeed(0.1f);}
    }


    private boolean drowning = false;
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onDrowning(LivingEvent.LivingUpdateEvent event)
    {
        EntityLivingBase entity =  event.getEntityLiving();

        if(entity.isPotionActive(PotionRegistry.POTION_DROWNING))
        {
            drowning=true;
        }
        else{drowning=false;}

        if (drowning == true && !entity.isPotionActive(MobEffects.WATER_BREATHING))
        {
            DamageSource damage = DamageSource.DROWN;
            int amp = entity.getActivePotionEffect(PotionRegistry.POTION_DROWNING).getAmplifier() +1;
            Float amount = (float) 1.0 * amp;
            if(!entity.world.isRemote && entity.isWet() && (entity.ticksExisted % 20 == 0)) {
                entity.attackEntityFrom(damage,amount);
            }
        }
        else
        {

        }

    }

    private boolean buffed = false;
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onBuffed(LivingEvent.LivingUpdateEvent event)
    {
        EntityLivingBase entity =  event.getEntityLiving();

        if(entity.isPotionActive(PotionRegistry.POTION_ENVIGORATION))
        {
            buffed=true;
        }
        else{buffed=false;}

        if (buffed == true)
        {


            int amp = entity.getActivePotionEffect(PotionRegistry.POTION_ENVIGORATION).getAmplifier() +1;
            Float amount = (float) 1.0 * amp;
            applyStatModifiersBasedOnBuffs(entity);




            if(!entity.world.isRemote && (entity.ticksExisted % 20 == 0)) {

                if (entity.getHealth() < entity.getMaxHealth())
                {
                    entity.heal(1.0F + (float)(amp * 0.5));
                }
            }


            //entity.addPotionEffect(new PotionEffect(MobEffects.STRENGTH));
            //entity.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE));
            //entity.addPotionEffect(new PotionEffect(MobEffects.REGENERATION));
            //entity.addPotionEffect(new PotionEffect(MobEffects.SPEED));
            //entity.addPotionEffect(new PotionEffect(MobEffects.HEALTH_BOOST));
        }
        else
        {

        }

    }

    public void applyStatModifiersBasedOnBuffs(EntityLivingBase entity)
    {
        AttributeModifier modifier = new AttributeModifier("mod",1.0D,1);
        //fury
        HashMap<IAttribute, AttributeModifier> fury = new HashMap<IAttribute, AttributeModifier>();
        fury.put(SharedMonsterAttributes.MOVEMENT_SPEED,modifier);
        fury.put(SharedMonsterAttributes.ATTACK_DAMAGE, modifier);
    }


    Boolean magnet;
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onMagnet(TickEvent.PlayerTickEvent event)
    {

        EntityPlayer player = event.player;
        World world = event.player.getEntityWorld();
        int amp=0;
        if(player.isPotionActive(PotionRegistry.POTION_MAGNETISM) && !player.isSneaking())
        {
            magnet=true;
            amp = 1+player.getActivePotionEffect(PotionRegistry.POTION_MAGNETISM).getAmplifier();
        }
        else{magnet=false;}

        if(magnet==true)
        {
            float range = Math.multiplyExact(5,amp);
            float verticalRange = Math.multiplyExact(3,amp);
            float posX = Math.round(player.posX);
            float posY = (float) (player.posY - player.getEyeHeight());
            float posZ = Math.round(player.posZ);

            List<EntityItem> entities = player.getEntityWorld().getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(posX - range, posY - verticalRange, posZ - range, posX + range, posY + verticalRange, posZ + range));
            List<EntityXPOrb> xpOrbs = player.getEntityWorld().getEntitiesWithinAABB(EntityXPOrb.class, new AxisAlignedBB(posX - range, posY - verticalRange, posZ - range, posX + range, posY + verticalRange, posZ + range));

            for (EntityItem entity : entities) {
                if (entity != null && !world.isRemote && !entity.isDead) {
                    entity.onCollideWithPlayer(player);
                }
            }

            for (EntityXPOrb xpOrb : xpOrbs) {
                if (xpOrb != null && !world.isRemote) {
                    xpOrb.onCollideWithPlayer(player);
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onItemRightClick(PlayerInteractEvent.RightClickBlock event) {
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
        int count=0;

        int effectCap=9;
        int minimumDustRequired=5;

        if(!worldIn.isRemote)
        {
            //List<EntityItem> items = player.getEntityWorld().getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(posX-1, posY-1, posZ-1, posX+1, posY+1, posZ+1));
            List<EntityItem> items = player.getEntityWorld().getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(posX-3, posY-3, posZ-3, posX+3, posY+3,posZ+3));

            if((player.getHeldItem(hand) != null)) {
                if (player.getHeldItem(hand).getItem() instanceof ItemFlintAndSteel) {

                    for (EntityItem item : items) {
                        ItemStack stack = item.getItem();
                        if(stack.getItemDamage()==0) {red=red+2*stack.getCount();count=count+stack.getCount();}
                        else if(stack.getItemDamage()==1){blue=blue+2*stack.getCount();count=count+stack.getCount();}
                        else if(stack.getItemDamage()==2){yellow=yellow+2*stack.getCount();count=count+stack.getCount();}
                        else if(stack.getItemDamage()==3){red=red+stack.getCount();blue=blue+stack.getCount();count=count+stack.getCount();}
                        else if(stack.getItemDamage()==4){yellow=yellow+stack.getCount();blue=blue+stack.getCount();count=count+stack.getCount();}
                        else if(stack.getItemDamage()==5){yellow=yellow+stack.getCount();red=red+stack.getCount();count=count+stack.getCount();}
                        else if(stack.getItemDamage()==6){white=white+stack.getCount();count=count+stack.getCount();}
                        else if(stack.getItemDamage()==7){black=black+stack.getCount();count=count+stack.getCount();}
                        item.setDead();
                    }
                }

                if(count>=minimumDustRequired)
                {
                    int amp = 0;
                    if(white>black || white==black)//positive effects
                    {
                        amp=Math.abs(white-black);
                        if(amp>effectCap)
                        {
                            amp=effectCap;
                        }

                        if(red>=1 && blue==0 && yellow==0){player.addPotionEffect(new PotionEffect(MobEffects.STRENGTH ,20*count, amp, false, true));}
                        else if(blue>=1 && red==0 && yellow==0){player.addPotionEffect(new PotionEffect(MobEffects.WATER_BREATHING ,20*count, amp, false, true));}
                        else if(yellow>=1 && blue==0 && red==0){player.addPotionEffect(new PotionEffect(MobEffects.SATURATION ,20*count, amp, false, true));}

                        else if(red>yellow && yellow !=0 && blue==0){player.addPotionEffect(new PotionEffect(MobEffects.JUMP_BOOST ,20*count, amp, false, true));}
                        else if(red<yellow && red !=0 && blue==0){player.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION ,20*count, amp, false, true));}
                        else if(red>blue && blue !=0 && yellow==0){player.addPotionEffect(new PotionEffect(PotionRegistry.POTION_MAGNETISM ,20*count, amp, false, true));}//should be envigoration
                        else if(red<blue && red !=0 && yellow==0){player.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE ,20*count, amp, false, true));}
                        else if(yellow>blue && blue !=0 && red==0){player.addPotionEffect(new PotionEffect(MobEffects.INVISIBILITY,20*count, amp, false, true));}
                        else if(yellow<blue && yellow !=0 && red==0){player.addPotionEffect(new PotionEffect(PotionRegistry.POTION_QUICKNESS,20*count, amp, false, true));}
                        else if((yellow==blue && blue==red)){player.addPotionEffect(new PotionEffect(PotionRegistry.POTION_FLIGHT,20*count, amp, false, true));}

                        else if((blue==red && yellow==0)){player.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE,20*count, amp, false, true));}//purple
                        else if((yellow==blue && red==0)){player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION,20*count, amp, false, true));}//green
                        else if((yellow==red && blue==0)){player.addPotionEffect(new PotionEffect(MobEffects.HASTE,20*count, amp, false, true));}//orange

                    }
                    else//negative effects
                    {
                        amp=Math.abs((black-white)-1);
                        if(amp>effectCap)
                        {
                            amp=effectCap;
                        }

                        //player.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS ,20*count, amp, false, true));

                        if(red>=1 && blue==0 && yellow==0){player.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS ,20*count, amp, false, true));}
                        else if(blue>=1 && red==0 && yellow==0){player.addPotionEffect(new PotionEffect(PotionRegistry.POTION_DROWNING ,20*count, amp, false, true));}
                        else if(yellow>=1 && blue==0 && red==0){player.addPotionEffect(new PotionEffect(MobEffects.HUNGER ,20*count, amp, false, true));}
                        else if(red>yellow && yellow !=0 && blue==0){player.addPotionEffect(new PotionEffect(MobEffects.JUMP_BOOST ,20*count, 250, false, true));}
                        else if(red<yellow && red !=0 && blue==0){player.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS ,20*count, amp, false, true));}
                        else if(red>blue && blue !=0 && yellow==0){player.addPotionEffect(new PotionEffect(MobEffects.NAUSEA ,20*count, amp, false, true));}
                        else if(red<blue && red !=0 && yellow==0){player.addPotionEffect(new PotionEffect(PotionRegistry.POTION_ENVIGORATION ,20*count, amp, false, true));}//should be flamibility
                        else if(yellow>blue && blue !=0 && red==0){player.addPotionEffect(new PotionEffect(MobEffects.GLOWING,20*count, amp, false, true));}
                        else if(yellow<blue && yellow !=0 && red==0){player.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS,20*count, amp, false, true));}
                        else if((yellow==blue && blue==red)){player.addPotionEffect(new PotionEffect(MobEffects.LEVITATION,20*count, amp, false, true));}//Gravity
                        else if((blue==red && yellow==0)){player.addPotionEffect(new PotionEffect(MobEffects.POISON,20*count, amp, false, true));}//purple
                        else if((yellow==blue && red==0)){player.addPotionEffect(new PotionEffect(MobEffects.WITHER,20*count, amp, false, true));}//green
                        else if((yellow==red && blue==0)){player.addPotionEffect(new PotionEffect(MobEffects.MINING_FATIGUE,20*count, amp, false, true));}//orange


                    }
                }
            }
        }




    }

}
