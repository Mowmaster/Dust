package com.mowmaster.dust.handlers;

import com.mowmaster.dust.effects.PotionRegistry;
import com.mowmaster.dust.enchantments.EnchantmentRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.*;
import net.minecraft.inventory.InventoryEnderChest;
import net.minecraft.item.*;
import net.minecraft.potion.*;
import net.minecraft.stats.StatList;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.*;

import static net.minecraft.block.BlockFarmland.MOISTURE;

public class EnchantAndEffectHandlers
{
    /*
    Not needed yet


    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onAdvance(AdvancementEvent event)
    {
        if(event.getEntityPlayer() instanceof EntityPlayerMP) {
            PlayerAdvancements advancements = ((EntityPlayerMP) event.getEntityPlayer()).getAdvancements();
            AdvancementProgress parent = advancements.getProgress(event.getAdvancement().getParent());

            if(event.getAdvancement().getId().getResourceDomain().equals(Reference.MODID))
            {
                if(parent.isDone())
                {

                }
                else
                {
                    System.out.println("PARENT NOT COMPLETED YET!!!");
                }
            }

        }

    }


    public static List<ResourceLocation> syncedAdvancements = null;
    public static Set<String> trackedNamespaces = new HashSet<>();

    @SideOnly(Side.CLIENT)
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onAdvancement(AdvancementEvent event) {
        System.out.println(event.getAdvancement());

        if(event.getEntityPlayer() instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) event.getEntityPlayer();
            buildSyncSet(player);

            System.out.println(event.getAdvancement().getId());
            if(syncedAdvancements.contains(event.getAdvancement().getId()))
            {System.out.println(parentDone(player,event.getAdvancement(), true));}
        }

}

    private static void buildSyncSet(EntityPlayerMP player) {
        if(syncedAdvancements == null) {
            AdvancementManager manager = player.getServer().getAdvancementManager();
            Iterable<Advancement> allAdvancements = manager.getAdvancements();

            syncedAdvancements = new ArrayList<>();
            for(Advancement a : allAdvancements)
                if(trackedNamespaces.contains(a.getId().getResourceDomain()))
                    syncedAdvancements.add(a.getId());
        }
    }

    public static Boolean parentDone(EntityPlayerMP player, Advancement adv,boolean showToast) {
        PlayerAdvancements advancements = player.getAdvancements();
        if(advancements == null)
            return false;

        AdvancementManager manager = player.getServer().getAdvancementManager();

        List<String> completed = new LinkedList<>();
        for(ResourceLocation res : syncedAdvancements) {
            AdvancementProgress p = advancements.getProgress(adv.getParent());
            if(p.isDone())
                return true;
        }

        return false;
    }
     */

    /*
    @SubscribeEvent
    public static void onEntityCapabilitiesAttach (AttachCapabilitiesEvent<Entity> event) {
        Entity entity = event.getObject();
        int level = 0;
        int expLoss = 0;

        if (entity instanceof EntityLivingBase) {
            if (((EntityLivingBase) entity).isPotionActive(PotionRegistry.POTION_FLIGHT)) {
                event.addCapability(CapabilityFlightHandler.FLIGHT, new CapabilityFlightHandler());
            }

            if (entity instanceof EntityPlayer) {
                if (((EntityPlayer) entity).inventory.armorInventory.get(2).isItemEnchanted()) {
                    if (EnchantmentHelper.getEnchantmentLevel(EnchantmentRegistry.enchantmentFlight, ((EntityPlayer) entity).inventory.armorInventory.get(2)) != 0 && ((EntityPlayer) entity).experienceTotal > 0)
                    {
                        event.addCapability(CapabilityFlightHandler.FLIGHT, new CapabilityFlightHandler());
                    }
                }
            }
        }
    }


    private boolean flight = false;
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onFlight(TickEvent.PlayerTickEvent.PlayerTickEvent event)
    {
        EntityPlayer player = event.player;
        World world = event.player.world;

        int level = 0;
        int expLoss = 0;
        if(!world.isRemote)
        {

        }
        if(player.inventory.armorInventory.get(2) !=null && player.inventory.armorInventory.get(2).isItemEnchanted())
        {
            if(EnchantmentHelper.getEnchantmentLevel(EnchantmentRegistry.enchantmentFlight,player.inventory.armorInventory.get(2))!=0 && player.experienceTotal>0)
            {
                flight = true;

                level = EnchantmentHelper.getEnchantmentLevel(EnchantmentRegistry.enchantmentFlight,player.inventory.armorInventory.get(2));
                expLoss = (int)(4 * level);

                if(!player.world.isRemote && !player.isPotionActive(PotionRegistry.POTION_FLIGHT) && !player.onGround && player.capabilities.isFlying && (player.ticksExisted % expLoss == 0)) {
                    removeXp(player, 1);
                }
            }
            else flight=false;
        }
        else flight=false;


        if(player.isPotionActive(PotionRegistry.POTION_FLIGHT))
        {
            flight=true;
        }
        if(flight || player.isCreative() || player.isSpectator())
        {
            player.capabilities.allowFlying = true;
            player.fallDistance=0.0f;
        }
        else
        {
            flight=false;
            player.capabilities.isFlying = false;
            player.capabilities.allowFlying = false;
        }
    }
     */

    private boolean voidStorage = false;
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onVoidStorage(PlayerInteractEvent.RightClickBlock event)
    {
        EntityLivingBase entity = event.getEntityLiving();
        World worldIn = event.getWorld();
        BlockPos blockClickedOn = event.getPos();

        if(entity.isPotionActive(PotionRegistry.POTION_VOIDSTORAGE))
        {
            voidStorage=true;
        }
        else {voidStorage=false;}

        if(voidStorage)
        {
            if(event.getEntityLiving() instanceof EntityPlayer)
            {
                EntityPlayer player = (EntityPlayer)event.getEntityLiving();
                if(player.isSneaking())
                {
                    if(!worldIn.getBlockState(blockClickedOn).getBlock().equals(Blocks.AIR))
                    {
                        InventoryEnderChest inventoryenderchest = player.getInventoryEnderChest();
                        player.displayGUIChest(inventoryenderchest);
                        player.addStat(StatList.ENDERCHEST_OPENED);

                    }
                }
            }
        }

    }

    private boolean stepup = false;
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onStepAssist(LivingEvent.LivingUpdateEvent event)
    {
        EntityLivingBase entity = event.getEntityLiving();
        int amp = 0;

        if(event.getEntityLiving() instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer)event.getEntityLiving();
            if(player.inventory.armorInventory.get(1) !=null && player.inventory.armorInventory.get(1).isItemEnchanted())
            {
                if(EnchantmentHelper.getEnchantmentLevel(EnchantmentRegistry.enchantmentStepAssist,player.inventory.armorInventory.get(1))!=0)
                {
                    int level = EnchantmentHelper.getEnchantmentLevel(EnchantmentRegistry.enchantmentStepAssist,player.inventory.armorInventory.get(1));
                    player.addPotionEffect(new PotionEffect(PotionRegistry.POTION_STEPASSIST,80,level-1,false,false));
                }
            }
        }
        if(entity.isPotionActive(PotionRegistry.POTION_STEPASSIST))
        {
            stepup=true;
            amp = entity.getActivePotionEffect(PotionRegistry.POTION_STEPASSIST).getAmplifier() +1;
        }
        else stepup=false;




        Float stepheightvalue = (float)(0.42 * amp) + (float)1.0048174;

        if(stepup==true && !entity.isSneaking())
        {
            entity.stepHeight = stepheightvalue;
        }
        else if(stepup==true && entity.isSneaking())
        {
            entity.stepHeight=0.6f;
        }
        else
        {
            if(entity.stepHeight >= 1.3448174)
            {
                entity.stepHeight=0.6f;
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

    /*
    private boolean runner = false;
    private boolean runnerE = false;
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onMovingFast(LivingEvent.LivingUpdateEvent event)
    {
        EntityLivingBase entity = event.getEntityLiving();
        int amp=0;
        int level=0;
        if(event.getEntityLiving() instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer)event.getEntityLiving();
            if(!player.inventory.armorInventory.get(0).isEmpty() && player.inventory.armorInventory.get(0).isItemEnchanted() || !player.inventory.armorInventory.get(1).isEmpty() && player.inventory.armorInventory.get(1).isItemEnchanted())
            {
                if(EnchantmentHelper.getEnchantmentLevel(EnchantmentRegistry.enchantmentQuickPace,player.inventory.armorInventory.get(0))!=0 || EnchantmentHelper.getEnchantmentLevel(EnchantmentRegistry.enchantmentQuickPace,player.inventory.armorInventory.get(1))!=0)
                {
                    runnerE = true;
                    if(player.inventory.armorInventory.get(0).isItemEnchanted() && !player.inventory.armorInventory.get(1).isItemEnchanted())
                    {
                        level = EnchantmentHelper.getEnchantmentLevel(EnchantmentRegistry.enchantmentQuickPace,player.inventory.armorInventory.get(0));
                    }
                    else if(player.inventory.armorInventory.get(1).isItemEnchanted() && !player.inventory.armorInventory.get(0).isItemEnchanted())
                    {
                        level = EnchantmentHelper.getEnchantmentLevel(EnchantmentRegistry.enchantmentQuickPace,player.inventory.armorInventory.get(1));
                    }
                    else
                    {
                        level = EnchantmentHelper.getEnchantmentLevel(EnchantmentRegistry.enchantmentQuickPace,player.inventory.armorInventory.get(0)) +
                                EnchantmentHelper.getEnchantmentLevel(EnchantmentRegistry.enchantmentQuickPace,player.inventory.armorInventory.get(1));
                    }
                }
                else runnerE=false;
            }
            else runnerE=false;
        }

        if(entity.isPotionActive(PotionRegistry.POTION_QUICKNESS))
        {
            runner=true;
            amp = entity.getActivePotionEffect(PotionRegistry.POTION_QUICKNESS).getAmplifier() +1;
        }
        else{runner=false;}

        if (runner == true || runnerE == true && entity.onGround && !entity.isInWater() && !entity.isSneaking() && entity.motionY <= 0)
        {
            entity.motionX *= (1.0f + (0.015f * (float)Math.addExact(amp,level)));
            entity.motionZ *= (1.0f + (0.015f * (float)Math.addExact(amp,level)));
        }
    }
     */

    private boolean waterRunner = false;
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onMovingFastInWater(LivingEvent.LivingUpdateEvent event)
    {
        EntityLivingBase entity = event.getEntityLiving();

        int amp=0;

        if(entity.isPotionActive(PotionRegistry.POTION_WATERQUICKNESS))
        {
            waterRunner=true;
            amp = entity.getActivePotionEffect(PotionRegistry.POTION_WATERQUICKNESS).getAmplifier() +1;
        }
        else waterRunner=false;

        if (waterRunner == true && entity.isInWater() || entity.isWet() && !entity.isSneaking())
        {
            entity.motionX *= (1.0f + 0.035 * amp);
            entity.motionZ *= (1.0f + 0.035 * amp);

            if (entity.motionY > 0){
                entity.motionY *= 1.134;
            }
        }
    }

    private boolean petrified = false;
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onStopped(LivingEvent.LivingUpdateEvent event)
    {
        EntityLivingBase entity = event.getEntityLiving();
        int amp=0;

        if(entity.isPotionActive(PotionRegistry.POTION_PETRIFIED))
        {
            petrified=true;
            amp = entity.getActivePotionEffect(PotionRegistry.POTION_PETRIFIED).getAmplifier() +1;
        }
        else petrified=false;

        if (petrified == true)
        {
            if(entity instanceof EntityPlayer)
            {
                if(((EntityPlayer) entity).capabilities.isFlying && !((EntityPlayer) entity).isCreative())
                {
                    //flight=false;
                    ((EntityPlayer) entity).capabilities.isFlying = false;
                    ((EntityPlayer) entity).capabilities.allowFlying = false;
                }
            }

            entity.motionX =0f;
            entity.motionZ =0f;
            entity.motionY =-1f * amp;
        }
    }

    private boolean slowfall = false;
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onSlowFalling(LivingEvent.LivingUpdateEvent event)
    {
        EntityLivingBase entity = event.getEntityLiving();
        int amp=0;

        if(entity.isPotionActive(PotionRegistry.POTION_SLOWFALL))
        {
            slowfall=true;
            amp = entity.getActivePotionEffect(PotionRegistry.POTION_SLOWFALL).getAmplifier() +1;
        }
        else slowfall=false;

        if (slowfall == true && !entity.isSneaking() && !entity.isInWater())
        {
            entity.setPosition(entity.posX, entity.posY + (entity.fallDistance / 1.01), entity.posZ);
            entity.motionX *= (1.0f + 0.0125 * amp);
            entity.motionZ *= (1.0f + 0.0125 * amp);
            entity.fallDistance=0.0f;
        }
        else if(slowfall == true && entity.isSneaking() && !entity.isInWater())
        {
            entity.setPosition(entity.posX, entity.posY + (entity.fallDistance / 1.51), entity.posZ);
            entity.motionX *= (1.0f + 0.025 * amp);
            entity.motionZ *= (1.0f + 0.025 * amp);
            entity.fallDistance=0.0f;
        }
    }

    private boolean harvester = false;
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onHarvester(LivingEvent.LivingUpdateEvent event)
    {
        EntityLivingBase entity = event.getEntityLiving();
        int amp=0;
        int zmin=0;
        int zmax=0;
        int xmin=0;
        int xmax=0;

        if(entity.isPotionActive(PotionRegistry.POTION_HARVESTER))
        {
            harvester=true;
            amp = entity.getActivePotionEffect(PotionRegistry.POTION_HARVESTER).getAmplifier();
            zmin=-amp;zmax=+amp;xmin=-amp;xmax=+amp;
        }
        else harvester=false;

        if (harvester == true)
        {
            BlockPos pos = new BlockPos(entity.posX,entity.posY,entity.posZ);//the block above the block player is standing on
            World world = entity.getEntityWorld();

            if(entity instanceof EntityPlayer)
            {
                for(int c=zmin;c<=zmax;c++) {
                    for (int a = xmin; a <= xmax; a++)
                    {
                        IBlockState state = entity.world.getBlockState(pos.add(a,0,c));
                        if(state.getBlock() instanceof IGrowable && !((IGrowable) state.getBlock()).canGrow(world,pos.add(a,0,c),state,false) && !state.getBlock().equals(Blocks.PUMPKIN_STEM) && !state.getBlock().equals(Blocks.MELON_STEM) )
                        {
                            state.getBlock().harvestBlock(world,(EntityPlayer)entity,pos.add(a,0,c),state,null,entity.getHeldItemMainhand());
                            world.setBlockToAir(pos.add(a,0,c));
                        }
                    }
                }

            }
            else
            {
                for(int c=zmin;c<=zmax;c++) {
                    for (int a = xmin; a <= xmax; a++)
                    {
                        IBlockState state = entity.world.getBlockState(pos.add(a,0,c));
                        if(state.getBlock() instanceof IGrowable && !((IGrowable) state.getBlock()).canGrow(world,pos.add(a,0,c),state,false))
                        {
                            WorldServer worldServer = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(event.getEntityLiving().dimension);
                            FakePlayer fakePlayer = FakePlayerFactory.getMinecraft(worldServer);
                            state.getBlock().harvestBlock(world,fakePlayer,pos.add(a,0,c),state,null,entity.getHeldItemMainhand());
                            world.setBlockToAir(pos.add(a,0,c));
                        }
                    }
                }
            }

        }
    }

    private boolean grower = false;
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onGrower(LivingEvent.LivingUpdateEvent event)
    {
        EntityLivingBase entity = event.getEntityLiving();
        int amp=0;
        int zmin=0;
        int zmax=0;
        int xmin=0;
        int xmax=0;

        if(entity.isPotionActive(PotionRegistry.POTION_GROWER))
        {
            grower=true;
            amp = entity.getActivePotionEffect(PotionRegistry.POTION_GROWER).getAmplifier();
            zmin=-amp;zmax=+amp;xmin=-amp;xmax=+amp;
        }
        else grower=false;

        if (grower == true)
        {
            BlockPos pos = new BlockPos(entity.posX,entity.posY,entity.posZ);//the block above the block player is standing on
            World world = entity.getEntityWorld();
            Random rn = new Random();
            int randompowerful = 1000;
            if(amp<10)
            {
                randompowerful = 1000 - (100*amp);
            }
            else randompowerful=100;

            for(int c=zmin;c<=zmax;c++) {
                for (int a = xmin; a <= xmax; a++)
                {
                    int growchance = rn.nextInt(randompowerful);
                    IBlockState state = entity.world.getBlockState(pos.add(a,0,c));
                    if(growchance<=1)
                    {
                        if(state.getBlock() instanceof IGrowable && ((IGrowable) state.getBlock()).canGrow(world,pos.add(a,0,c),state,false))
                        {
                            ((IGrowable) state.getBlock()).grow(world,rn,pos.add(a,0,c),state);
                        }
                    }

                }
            }
        }
    }

    private boolean tiller = false;
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void ontiller(LivingEvent.LivingUpdateEvent event)
    {
        EntityLivingBase entity = event.getEntityLiving();
        int amp=0;
        int zmin=0;
        int zmax=0;
        int xmin=0;
        int xmax=0;

        if(entity.isPotionActive(PotionRegistry.POTION_TILLER))
        {
            tiller=true;
            amp = entity.getActivePotionEffect(PotionRegistry.POTION_TILLER).getAmplifier();
            zmin=-amp;zmax=+amp;xmin=-amp;xmax=+amp;
        }
        else tiller=false;

        if (tiller == true)
        {
            BlockPos pos = new BlockPos(entity.posX,entity.posY-1,entity.posZ);//the block player is standing on
            World world = entity.getEntityWorld();
            for(int c=zmin;c<=zmax;c++) {
                for (int a = xmin; a <= xmax; a++)
                {
                    IBlockState state = entity.world.getBlockState(pos.add(a,0,c));
                    if(state.getBlock().equals(Blocks.GRASS) || state.getBlock().equals(Blocks.DIRT))
                    {
                        world.setBlockState(pos.add(a,0,c),Blocks.FARMLAND.getDefaultState().withProperty(MOISTURE, 7));//Unless there is some wat to use a fake player or the player effected to use a hoe on the soil for the drops
                    }
                }
            }
        }
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

    /*private boolean buffed = false;
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


        //fun but silly
        entity.setNoGravity(true);
    }*/


    Boolean magnet;
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onMagnet(TickEvent.PlayerTickEvent event)
    {

        EntityPlayer player = event.player;

        World world = player.getEntityWorld();
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
    public void onPlanter(LivingEvent.LivingUpdateEvent event) {
        World worldIn = event.getEntityLiving().getEntityWorld();
        EntityLivingBase entityLiving = event.getEntityLiving();

        int amp=0;
        int zmin=0;
        int zmax=0;
        int xmin=0;
        int xmax=0;


        int posX = entityLiving.getPosition().getX();
        int posY = entityLiving.getPosition().getY();
        int posZ = entityLiving.getPosition().getZ();
        BlockPos pos = new BlockPos(posX,posY,posZ);//the block above the block the entity is standing on
        if(entityLiving.isPotionActive(PotionRegistry.POTION_PLANTER))
        {
            amp = entityLiving.getActivePotionEffect(PotionRegistry.POTION_PLANTER).getAmplifier();
            zmin=-amp;zmax=+amp;xmin=-amp;xmax=+amp;
            if(!worldIn.isRemote) {
                //List<EntityItem> items = player.getEntityWorld().getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(posX-1, posY-1, posZ-1, posX+1, posY+1, posZ+1));
                List<EntityItem> items = entityLiving.getEntityWorld().getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(posX - 3*amp, posY - 2, posZ - 3*amp, posX + 3*amp, posY + 2, posZ + 3*amp));

                for (EntityItem item : items) {
                    ItemStack stack = item.getItem();
                    for(int c=zmin;c<=zmax;c++) {
                        for (int a = xmin; a <= xmax; a++)
                        {
                            if(worldIn.getBlockState(pos.add(a,-1,c)).getBlock().isFertile(worldIn,pos.add(a,-1,c)) || worldIn.getBlockState(pos.add(a,-1,c)).getBlock().equals(Blocks.FARMLAND))
                            {
                                if(worldIn.getBlockState(pos.add(a,0,c)).getBlock().equals(Blocks.AIR))
                                {
                                    if(stack.getCount()>0)
                                    {
                                        int oldCount = stack.getCount();
                                        if(stack.getItem().getRegistryName().getResourceDomain().contains("harvestcraft") && stack.getItem().getUnlocalizedName().contains("sunflowerseedsitem")
                                                || stack.getItem().getUnlocalizedName().contains("roastedpumpkinseedsitem")
                                                || stack.getItem().getUnlocalizedName().contains("toastedsesameseedsitem")
                                                || stack.getItem().getUnlocalizedName().contains("saltedsunflowerseedsitem")) {}
                                        else if(stack.getItem().getRegistryName().getResourceDomain().contains("harvestcraft") && stack.getItem().getUnlocalizedName().contains("teaseeditem"))
                                        {
                                            worldIn.setBlockState(pos.add(a,0,c),Block.getBlockFromName("harvestcraft:pamtealeafcrop").getDefaultState());
                                            stack.setCount(oldCount-1);
                                        }
                                        else if(stack.getItem().getRegistryName().getResourceDomain().contains("harvestcraft") && stack.getItem().getUnlocalizedName().contains("sesameseedsitem") || stack.getItem().getUnlocalizedName().contains("sesameseedsseeditem"))
                                        {
                                            worldIn.setBlockState(pos.add(a,0,c),Block.getBlockFromName("harvestcraft:pamsesameseedscrop").getDefaultState());
                                            stack.setCount(oldCount-1);
                                        }
                                        else if(stack.getItem().getRegistryName().getResourceDomain().contains("harvestcraft") && stack.getItem().getUnlocalizedName().contains("coffeeseeditem"))
                                        {
                                            worldIn.setBlockState(pos.add(a,0,c),Block.getBlockFromName("harvestcraft:pamcoffeebeancrop").getDefaultState());
                                            stack.setCount(oldCount-1);
                                        }
                                        else if(stack.getItem().getRegistryName().getResourceDomain().contains("harvestcraft") && stack.getItem().getUnlocalizedName().contains("mustardseeditem") || stack.getItem().getUnlocalizedName().contains("mustardseedsitem"))
                                        {
                                            worldIn.setBlockState(pos.add(a,0,c),Block.getBlockFromName("harvestcraft:pammustardseedscrop").getDefaultState());
                                            stack.setCount(oldCount-1);
                                        }
                                        else if(stack.getItem().getRegistryName().getResourceDomain().contains("harvestcraft") && stack.getItem().getUnlocalizedName().contains("seeditem"))
                                        {
                                            worldIn.setBlockState(pos.add(a,0,c),Block.getBlockFromName(stack.getItem().getUnlocalizedName().replace("item.","harvestcraft:pam").replace("seeditem","crop")).getDefaultState());
                                            stack.setCount(oldCount-1);

                                        }
                                        else if(stack.getItem().getRegistryName().getResourceDomain().contains("immersiveengineering") && stack.getItem().getUnlocalizedName().contains("seed"))
                                        {
                                            worldIn.setBlockState(pos.add(a,0,c),Block.getBlockFromName("immersiveengineering:hemp").getDefaultState());
                                            stack.setCount(oldCount-1);
                                        }
                                        else if(stack.getItem().equals(Items.WHEAT_SEEDS))
                                        {
                                            worldIn.setBlockState(pos.add(a,0,c),Blocks.WHEAT.getDefaultState());
                                            stack.setCount(oldCount-1);
                                        }
                                        else if(stack.getItem().equals(Items.MELON_SEEDS))
                                        {
                                            worldIn.setBlockState(pos.add(a,0,c),Blocks.MELON_STEM.getDefaultState());
                                            stack.setCount(oldCount-1);
                                        }
                                        else if(stack.getItem().equals(Items.PUMPKIN_SEEDS))
                                        {
                                            worldIn.setBlockState(pos.add(a,0,c),Blocks.PUMPKIN_STEM.getDefaultState());
                                            stack.setCount(oldCount-1);
                                        }
                                        else if(stack.getItem().equals(Items.BEETROOT_SEEDS))
                                        {
                                            worldIn.setBlockState(pos.add(a,0,c),Blocks.BEETROOTS.getDefaultState());
                                            stack.setCount(oldCount-1);
                                        }
                                        else if(stack.getItem().equals(Items.POTATO))
                                        {
                                            worldIn.setBlockState(pos.add(a,0,c),Blocks.POTATOES.getDefaultState());
                                            stack.setCount(oldCount-1);
                                        }
                                        else if(stack.getItem().equals(Items.CARROT))
                                        {
                                            worldIn.setBlockState(pos.add(a,0,c),Blocks.CARROTS.getDefaultState());
                                            stack.setCount(oldCount-1);
                                        }
                                    }
                                    else
                                    {
                                        item.setItem(ItemStack.EMPTY);
                                        item.setDead();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /*Boolean storage;
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onStore(TickEvent.PlayerTickEvent event)
    {

        EntityPlayer player = event.player;

        World world = player.getEntityWorld();
        int amp=0;
        if(player.isPotionActive(PotionRegistry.POTION_VOIDSTORAGE))
        {
            storage=true;
            amp = 1+player.getActivePotionEffect(PotionRegistry.POTION_VOIDSTORAGE).getAmplifier();
        }
        else{storage=false;}

        if(storage==true)
        {
            float range = 5;
            float verticalRange = 3;
            float posX = Math.round(player.posX);
            float posY = (float) (player.posY - player.getEyeHeight());
            float posZ = Math.round(player.posZ);

            *//*List<EntityItem> entities = player.getEntityWorld().getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(posX - range, posY - verticalRange, posZ - range, posX + range, posY + verticalRange, posZ + range));

            for (EntityItem entity : entities) {
                if (entity != null && !world.isRemote && !entity.isDead) {
                    entity.onCollideWithPlayer(player);
                }
            }

            for (EntityXPOrb xpOrb : xpOrbs) {
                if (xpOrb != null && !world.isRemote) {
                    xpOrb.onCollideWithPlayer(player);
                }
            }*//*
        }
    }*/
}
