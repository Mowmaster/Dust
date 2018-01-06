package com.mowmaster.dust.tiles;

import com.google.common.collect.Sets;
import com.mowmaster.dust.items.ItemRegistry;
import com.sun.org.apache.xalan.internal.xsltc.util.IntegerArray;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


import javax.annotation.Nullable;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.*;


public class TileCrystalCluster extends TileEntity implements ITickable
{

    public int crystalCount = 0;
    public int crystalMax = 3;
    public int redCrystals =0;
    public int blueCrystals = 0;
    public int yellowCrystals = 0;
    public float whiteCrystals = 0.0F;
    public float blackCrystals = 0.0F;

    public int slot1;
    public int slot2;
    public int slot3;
    public int slot4;
    public int slot5;
    public int slot6;
    public int slot7;
    public int slot8;
    public int slot9;



    public int[] getCrystalsIn(){return new int[]{redCrystals,blueCrystals,yellowCrystals};}
    public int getCrystalCount() { return crystalCount; }
    public int getCrystalFromList(int whichSlot)
    {
        switch (whichSlot)
        {
            default:
            case 1:
                return slot1;
            case 2:
                return slot2;
            case 3:
                return slot3;
            case 4:
                return slot4;
            case 5:
                return slot5;
            case 6:
                return slot6;
            case 7:
                return slot7;
            case 8:
                return slot8;
            case 9:
                return slot9;
        }
    }



    public boolean addCrystal(int type)
    {
        if(type==0){redCrystals++;}
        if(type==1){blueCrystals++;}
        if(type==2){yellowCrystals++;}
        if(crystalCount<crystalMax)
        {
            crystalCount++;
            if(crystalCount==1){slot1=type;}
            if(crystalCount==2){slot2=type;}
            if(crystalCount==3){slot3=type;}
            if(crystalCount==4){slot4=type;}
            if(crystalCount==5){slot5=type;}
            if(crystalCount==6){slot6=type;}
            if(crystalCount==7){slot7=type;}
            if(crystalCount==8){slot8=type;}
            if(crystalCount==9){slot9=type;}

            markDirty();
            IBlockState state = world.getBlockState(pos);
            world.notifyBlockUpdate(pos,state,state,3);
            return true;
        }
        return false;
    }
    private int slot;
    public void removeCrystal(TileEntity tile) {
        World worldIn = tile.getWorld();


        if (crystalCount > 0) {
            if(crystalCount==1){slot=slot1;}
            if(crystalCount==2){slot=slot2;}
            if(crystalCount==3){slot=slot3;}
            if(crystalCount==4){slot=slot4;}
            if(crystalCount==5){slot=slot5;}
            if(crystalCount==6){slot=slot6;}
            if(crystalCount==7){slot=slot7;}
            if(crystalCount==8){slot=slot8;}
            if(crystalCount==9){slot=slot9;}
            if(slot==0){redCrystals--;}
            if(slot==1){blueCrystals--;}
            if(slot==2){yellowCrystals--;}
            worldIn.spawnEntity(new EntityItem(worldIn, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, new ItemStack(ItemRegistry.crystal, 1,slot)));
            crystalCount--;
            markDirty();
            IBlockState state = world.getBlockState(pos);
            world.notifyBlockUpdate(pos,state,state,3);
            if(crystalCount==0) {worldIn.setBlockToAir(pos);}
        }

    }

    public static final Potion[][] LISTOFEFFECTS = new Potion[][] {{MobEffects.SPEED, MobEffects.HASTE}, {MobEffects.RESISTANCE, MobEffects.JUMP_BOOST}, {MobEffects.STRENGTH}, {MobEffects.REGENERATION}};
    private static final Set<Potion> POSSIBLEEFFECTS = Sets.<Potion>newHashSet();
    private void addEffectsToArea()
    {

        int x = this.pos.getX();
        int y = this.pos.getY();
        int z = this.pos.getZ();
        AxisAlignedBB effectBox = (new AxisAlignedBB(0D, 0D, 0D, 0D, 0D, 0D));
        if(crystalCount>=3 && crystalCount<=4)
        {
            effectBox = (new AxisAlignedBB((double)x-1, (double)y, (double)z-1, (double)x +2, (double)y+1, (double)z+2));
        }
        if(crystalCount>=5 && crystalCount<=6)
        {
            effectBox = (new AxisAlignedBB((double)x-2, (double)y-1, (double)z-2, (double)x +3, (double)y+2, (double)z+3));
        }
        if(crystalCount>=7 && crystalCount<=8)
        {
            effectBox = (new AxisAlignedBB((double)x-3, (double)y-2, (double)z-3, (double)x +4, (double)y+3, (double)z+4));
        }
        if(crystalCount>=9)
        {
            effectBox = (new AxisAlignedBB((double)x-4, (double)y-3, (double)z-3, (double)x +5, (double)y+4, (double)z+5));
        }

        PotionEffect crystalEffect;
        if(redCrystals==3){crystalEffect = new PotionEffect(MobEffects.FIRE_RESISTANCE ,100, 0, false, true);}
        else if(blueCrystals==3){crystalEffect = new PotionEffect(MobEffects.WATER_BREATHING ,100, 0, false, true);}
        else if(yellowCrystals==3){crystalEffect = new PotionEffect(MobEffects.SATURATION ,100, 0, false, true);}
        else{crystalEffect = new PotionEffect(MobEffects.SPEED ,100, 0, false, true);}

        if (!this.world.isRemote)
        {
            List<EntityPlayer> list = this.world.<EntityPlayer>getEntitiesWithinAABB(EntityPlayer.class, effectBox);

            for (EntityPlayer entityplayer : list)
            {
                entityplayer.addPotionEffect(crystalEffect);
                //this(potionIn, durationIn, amplifierIn, is splash? false, is ambient? true);
            }
        }
    }

    @Override
    public void update()
    {

        if (this.world.getTotalWorldTime() % 80L == 0L)
        {
            if (this.world != null)
            {
                this.addEffectsToArea();
            }
        }

    }

    @Nullable
    private static Potion isBeaconEffect(int validPotion)
    {
        Potion potion = Potion.getPotionById(validPotion);
        return POSSIBLEEFFECTS.contains(potion) ? potion : null;
    }

    static
    {
        for (Potion[] potionarray : LISTOFEFFECTS)
        {
            Collections.addAll(POSSIBLEEFFECTS, potionarray);
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setInteger("crystalcount",crystalCount);
        compound.setInteger("slot1",slot1);
        compound.setInteger("slot2",slot2);
        compound.setInteger("slot3",slot3);
        compound.setInteger("slot4",slot4);
        compound.setInteger("slot5",slot5);
        compound.setInteger("slot6",slot6);
        compound.setInteger("slot7",slot7);
        compound.setInteger("slot8",slot8);
        compound.setInteger("slot9",slot9);
        return compound;
    }



    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.crystalCount = compound.getInteger("crystalcount");
        this.slot1 = compound.getInteger("slot1");
        this.slot2 = compound.getInteger("slot2");
        this.slot3 = compound.getInteger("slot3");
        this.slot4 = compound.getInteger("slot4");
        this.slot5 = compound.getInteger("slot5");
        this.slot6 = compound.getInteger("slot6");
        this.slot7 = compound.getInteger("slot7");
        this.slot8 = compound.getInteger("slot8");
        this.slot9 = compound.getInteger("slot9");
    }


    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        readFromNBT(pkt.getNbtCompound());
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return writeToNBT(new NBTTagCompound());
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(pos, 0, getUpdateTag());
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
        return oldState.getBlock() != newSate.getBlock();
    }

    /*
            SPEED = getRegisteredMobEffect("speed");
            SLOWNESS = getRegisteredMobEffect("slowness");
            HASTE = getRegisteredMobEffect("haste");
            MINING_FATIGUE = getRegisteredMobEffect("mining_fatigue");
            STRENGTH = getRegisteredMobEffect("strength");
            INSTANT_HEALTH = getRegisteredMobEffect("instant_health");
            INSTANT_DAMAGE = getRegisteredMobEffect("instant_damage");
            JUMP_BOOST = getRegisteredMobEffect("jump_boost");
            NAUSEA = getRegisteredMobEffect("nausea");
            REGENERATION = getRegisteredMobEffect("regeneration");
            RESISTANCE = getRegisteredMobEffect("resistance");
            FIRE_RESISTANCE = getRegisteredMobEffect("fire_resistance");
            WATER_BREATHING = getRegisteredMobEffect("water_breathing");
            INVISIBILITY = getRegisteredMobEffect("invisibility");
            BLINDNESS = getRegisteredMobEffect("blindness");
            NIGHT_VISION = getRegisteredMobEffect("night_vision");
            HUNGER = getRegisteredMobEffect("hunger");
            WEAKNESS = getRegisteredMobEffect("weakness");
            POISON = getRegisteredMobEffect("poison");
            WITHER = getRegisteredMobEffect("wither");
            HEALTH_BOOST = getRegisteredMobEffect("health_boost");
            ABSORPTION = getRegisteredMobEffect("absorption");
            SATURATION = getRegisteredMobEffect("saturation");
            GLOWING = getRegisteredMobEffect("glowing");
            LEVITATION = getRegisteredMobEffect("levitation");
            LUCK = getRegisteredMobEffect("luck");
            UNLUCK = getRegisteredMobEffect("unluck");
     */

}