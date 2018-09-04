package com.mowmaster.dust.tiles;

import com.google.common.collect.Sets;
import com.mowmaster.dust.effects.EffectPicker;
import com.mowmaster.dust.effects.PotionRegistry;
import com.mowmaster.dust.enums.CrystalTypes;
import com.mowmaster.dust.items.ItemRegistry;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;


import javax.annotation.Nullable;
import java.util.*;


public class TileCrystalCluster extends TileEntity implements ITickable
{

    public int crystalCount = 0;
    public int crystalMax = 9;
    public int redCrystals =0;
    public int blueCrystals = 0;
    public int yellowCrystals = 0;
    public int whiteCrystals = 0;
    public int blackCrystals = 0;

    public int slot1;
    public int slot2;
    public int slot3;
    public int slot4;
    public int slot5;
    public int slot6;
    public int slot7;
    public int slot8;
    public int slot9;

    private boolean hasGlowstone = false;



    public boolean getLight() {return hasGlowstone;}
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
        if(type==0){redCrystals++;redCrystals++;}
        if(type==1){blueCrystals++;blueCrystals++;}
        if(type==2){yellowCrystals++;yellowCrystals++;}

        if(type==3){redCrystals++;blueCrystals++;}//purple
        if(type==4){yellowCrystals++;blueCrystals++;}//green
        if(type==5){yellowCrystals++;redCrystals++;}//orange

        if(type==6){whiteCrystals++;}
        if(type==7){blackCrystals++;}
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
    public ItemStack removeCrystal(TileEntity tile) {
        World worldIn = tile.getWorld();
        ItemStack droppedItem = ItemStack.EMPTY;


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
            if(slot==0){redCrystals--;redCrystals--;}
            if(slot==1){blueCrystals--;blueCrystals--;}
            if(slot==2){yellowCrystals--;yellowCrystals--;}
            if(slot==3){redCrystals--;blueCrystals--;}//purple
            if(slot==4){yellowCrystals--;blueCrystals--;}//green
            if(slot==5){yellowCrystals--;redCrystals--;}//orange
            if(slot==6){whiteCrystals--;}
            if(slot==7){blackCrystals--;}


             droppedItem = new ItemStack(ItemRegistry.crystal, 1,slot);


            crystalCount--;
            markDirty();
            IBlockState state = world.getBlockState(pos);
            world.notifyBlockUpdate(pos,state,state,3);
            if(hasGlowstone==true)
            {
                if(crystalCount==0) {worldIn.spawnEntity(new EntityItem(worldIn, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, new ItemStack(Blocks.GLOWSTONE,1)));
                    worldIn.setBlockToAir(pos);}
            }
            else{if(crystalCount==0) {worldIn.setBlockToAir(pos);}}
        }
        return droppedItem;

    }


    public boolean addGlowstone()
    {
        if(hasGlowstone!=true)
        {
            hasGlowstone=true;
            markDirty();
            world.checkLight(pos);
            IBlockState state = world.getBlockState(pos);
            world.notifyBlockUpdate(pos,state,state,3);
            world.setBlockState(pos,state,3);
            return true;
        }
        return false;
    }

    private int amp;
    public static final Potion[][] LISTOFEFFECTS = new Potion[][] {{MobEffects.SPEED, MobEffects.HASTE}, {MobEffects.RESISTANCE, MobEffects.JUMP_BOOST}, {MobEffects.STRENGTH}, {MobEffects.REGENERATION}};
    private static final Set<Potion> POSSIBLEEFFECTS = Sets.<Potion>newHashSet();
    private void addEffectsToArea()
    {
        int totalColors=redCrystals+blueCrystals+yellowCrystals;
        int x = this.pos.getX();
        int y = this.pos.getY();
        int z = this.pos.getZ();
        int a=0;//CrystalEffectRange
        if(crystalCount>=3 && crystalCount<=4) {a=0;}
        if(crystalCount>=5 && crystalCount<=6) {a=1;}
        if(crystalCount>=7 && crystalCount<=8) {a=2;}
        if(crystalCount>=9) {a=Math.round(crystalCount/3);}//??? may need to change to work???



        if (!this.world.isRemote)
        {
            if(!this.world.isBlockPowered(pos))
            {
                if(crystalCount>=3)
                {
                    List<EntityCreature> lista = this.world.<EntityCreature>getEntitiesWithinAABB(EntityCreature.class, (new AxisAlignedBB((double)x-1, (double)y, (double)z-1, (double)x +2, (double)y+1, (double)z+2).grow(a)));
                    List<EntityPlayer> listed = this.world.<EntityPlayer>getEntitiesWithinAABB(EntityPlayer.class, (new AxisAlignedBB((double)x-1, (double)y, (double)z-1, (double)x +2, (double)y+1, (double)z+2).grow(a)));
                    for (EntityCreature entityCreature : lista)
                    {
                        entityCreature.addPotionEffect(EffectPicker.getEffectFromInputs(redCrystals,blueCrystals,yellowCrystals,whiteCrystals,blackCrystals,100,9,false,true, CrystalTypes.EffectTypes.CRYSTAL));
                        //this(potionIn, durationIn, amplifierIn, is splash? false, is ambient? true);
                    }
                    for (EntityPlayer entityPlayer : listed)
                    {
                        entityPlayer.addPotionEffect(EffectPicker.getEffectFromInputs(redCrystals,blueCrystals,yellowCrystals,whiteCrystals,blackCrystals,100,9,false,false, CrystalTypes.EffectTypes.CRYSTAL));
                        //this(potionIn, durationIn, amplifierIn, is splash? false, is ambient? true);
                    }
                }
            }
        }
    }



    private int ticker=0;
    private int ticked=0;
    @Override
    public void update()
    {
        if(ticked<=0)
        {
            ticker++;
        }

        if (this.world.getTotalWorldTime() % 80L == 0L)
        {
            if (this.world != null)
            {
                this.addEffectsToArea();
            }
        }

        if(ticker>=20 && hasGlowstone==true && ticked<=0)
        {
            world.checkLight(pos);
            ticker=0;
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
        compound.setInteger("red",redCrystals);
        compound.setInteger("blue",blueCrystals);
        compound.setInteger("yellow",yellowCrystals);
        compound.setInteger("white",whiteCrystals);
        compound.setInteger("black",blackCrystals);
        compound.setBoolean("light",hasGlowstone);
        compound.setInteger("ticked",ticked);;
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
        this.redCrystals = compound.getInteger("red");
        this.blueCrystals = compound.getInteger("blue");
        this.yellowCrystals = compound.getInteger("yellow");
        this.whiteCrystals = compound.getInteger("white");
        this.blackCrystals = compound.getInteger("black");
        this.hasGlowstone = compound.getBoolean("light");
        this.ticked=compound.getInteger("ticked");
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

}