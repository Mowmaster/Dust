package com.mowmaster.dust.handlers;

import com.mowmaster.dust.effects.PotionRegistry;
import com.mowmaster.dust.enchantments.EnchantmentRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentMending;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionAttackDamage;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.StatBasic;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.datafix.fixes.PotionWater;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import javax.xml.stream.events.Attribute;

public class EnchantAndEffectHandlers
{

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onBreakEvent(BlockEvent.BreakEvent event)
    {
        World world = event.getWorld();
        EntityPlayer player = event.getPlayer();
        if (player.swingingHand == null) {
            return;
        }
        BlockPos pos = event.getPos();
        Block block = event.getState().getBlock();
        IBlockState state = world.getBlockState(pos);
        //is this item stack enchanted with ME?
        ItemStack stackHarvestingWith = player.getHeldItem(player.swingingHand);
        NBTTagList list = player.getHeldItem(player.getActiveHand()).getEnchantmentTagList();
        if (list == null) {
            return;
        }
        int id = 0;
        int lvl = 0;
        int enchantLvl = 0;
        Enchantment e = Enchantment.getEnchantmentByID(id);
        for (int i = 0; i < list.tagCount(); i++) {
            NBTTagCompound compound = list.getCompoundTagAt(i);
            id = compound.getShort("id");
            lvl = compound.getShort("lvl");
            e = Enchantment.getEnchantmentByID(id);

            if (e.getName().contains("enchantVamperic")) {
                lvl = enchantLvl;
            }
        }
        /*
        if (enchantLvl <= 0) {
            return;
        }
         */




        //Wont Mine Dirt/ Wood on pickaxe

        for (String type : stackHarvestingWith.getItem().getToolClasses(stackHarvestingWith)) {
            if (block.isToolEffective(type, world.getBlockState(pos)) == false) {
                return;
            }
        }





        //block.harvestBlock(world, player, targetPos, targetState, null, player.getHeldItem(EnumHand.MAIN_HAND));

        if(!world.isRemote)
        {
            int damage = player.getHeldItem(player.getActiveHand()).getItemDamage();
            Float health = player.getHealth();
            //if(health>6.0f)
            //{
                if(damage>=5)
                {
                    System.out.println(player.getHeldItem(player.getActiveHand()).getMaxDamage());
                    System.out.println(damage);
                    player.getHeldItem(player.getActiveHand()).setItemDamage(damage - 6);
                    player.setHealth(health-1.0f);
                }

            //}

        }



    }

    private boolean flight = false;
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onFlight(TickEvent.PlayerTickEvent event)
    {
        int level = EnchantmentHelper.getEnchantmentLevel(EnchantmentRegistry.enchantmentFlight,event.player.inventory.armorInventory.get(2));
        int expLoss = (int)(4 * level);
        if(event.player.inventory.armorInventory.get(2) !=null && event.player.inventory.armorInventory.get(2).isItemEnchanted())
        {
            if(EnchantmentHelper.getEnchantmentLevel(EnchantmentRegistry.enchantmentFlight,event.player.inventory.armorInventory.get(2))!=0 && event.player.experienceTotal>0)
            {
                flight = true;

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
        int level = EnchantmentHelper.getEnchantmentLevel(EnchantmentRegistry.enchantmentStepAssist,event.player.inventory.armorInventory.get(1));
        if(event.player.inventory.armorInventory.get(1) !=null && event.player.inventory.armorInventory.get(1).isItemEnchanted())
        {
            if(EnchantmentHelper.getEnchantmentLevel(EnchantmentRegistry.enchantmentStepAssist,event.player.inventory.armorInventory.get(1))!=0)
            {
                stepup = true;
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
        int level = EnchantmentHelper.getEnchantmentLevel(EnchantmentRegistry.enchantmentQuickPace,event.player.inventory.armorInventory.get(0));//on boots
        if(event.player.inventory.armorInventory.get(0) !=null && event.player.inventory.armorInventory.get(0).isItemEnchanted())
        {
            if(EnchantmentHelper.getEnchantmentLevel(EnchantmentRegistry.enchantmentQuickPace,event.player.inventory.armorInventory.get(0))!=0)
            {
                runner = true;
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
        else if(EnchantmentHelper.getEnchantmentLevel(EnchantmentRegistry.enchantmentQuickPace,event.player.inventory.armorInventory.get(0))!=0){}
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
}
