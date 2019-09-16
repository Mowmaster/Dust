package com.mowmaster.dust.tiles;

import com.mowmaster.dust.blocks.machines.BlockPedestal;
import com.mowmaster.dust.effects.PotionRegistry;
import com.mowmaster.dust.enchantments.EnchantmentRegistry;
import com.mowmaster.dust.enums.FilterTypes;
import com.mowmaster.dust.items.ItemRegistry;
import com.mowmaster.dust.items.itemPedestalUpgrades.*;
import com.mowmaster.dust.particles.ParticlesInALine;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.*;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.*;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.*;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.vecmath.Vector3f;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import static net.minecraft.block.BlockDirectional.FACING;

public class TilePedestal extends TileEntityBase implements ITickable, ICapabilityProvider
{
    private ItemStackHandler item;
    private ItemStackHandler coin;
    private int expInPedestal = 0;
    private static final int[] SLOTS_ALLSIDES = new int[] {0};
    public ItemStack display = ItemStack.EMPTY;
    private int transferSizeCount = 4;
    private static int getPedestalRange = 8;
    private int ticker=0;
    private int ticker2=0;
    private int ticker3=0;
    private int ticker4=0;
    private static final BlockPos defaultPos = new BlockPos(0,-2000,0);
    public BlockPos[] storedOutputLocations = {defaultPos,defaultPos,defaultPos,defaultPos,defaultPos,defaultPos,defaultPos,defaultPos};

    public TilePedestal()
    {
        this.item = new ItemStackHandler(1);
        this.coin = new ItemStackHandler(1);
    }
    public boolean hasItem()
    {
        if(item.getStackInSlot(0).isEmpty())
        {
            return false;
        }
        else  return true;
    }
    public boolean hasCoin()
    {
        if(coin.getStackInSlot(0).isEmpty())
        {
            return false;
        }
        else  return true;
    }
    public ItemStack getItemInPedestal()
    {
        if(hasItem())
        {
            return item.getStackInSlot(0);
        }
        else return ItemStack.EMPTY;
    }
    public ItemStack getCoinOnPedestal()
    {
        if(hasCoin())
        {
            return coin.getStackInSlot(0);
        }
        else return ItemStack.EMPTY;
    }
    public ItemStack getDisplay() {return display;}
    public int getXPInPedestal() {return expInPedestal;}

    public boolean removeExpFromPedestal(int expAmount)
    {
        if(expInPedestal >= expAmount)
        {
            expInPedestal -= expAmount;
            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean addExpToPedestal(int expAmount)
    {
        expInPedestal += expAmount;
        return true;
    }

    public ItemStack removeItem() {
        ItemStack stack = item.extractItem(0,item.getStackInSlot(0).getCount(),false);
        updateBlock();
        return stack;
    }

    public ItemStack removeItem(int numToRemove) {
        ItemStack stack = item.extractItem(0,numToRemove,false);
        updateBlock();
        return stack;
    }

    public ItemStack removeCoin() {
        ItemStack stack = coin.extractItem(0,coin.getStackInSlot(0).getCount(),false);
        updateBlock();
        return stack;
    }

    public int getMaxStackSize(){return 64;}

    public boolean addItem(ItemStack itemFromBlock)
    {
        if(hasItem())
        {
            if(doItemsMatch(itemFromBlock))
            {
                item.insertItem(0, itemFromBlock.copy(), false);
            }
        }
        else {item.insertItem(0, itemFromBlock.copy(), false);}
        updateBlock();
        return true;
    }

    public boolean addCoin(ItemStack coinFromBlock)
    {
        ItemStack itemFromBlock = coinFromBlock.copy();
        itemFromBlock.setCount(1);
        if(hasCoin()){} else coin.insertItem(0,itemFromBlock,false);
        updateBlock();
        return true;
    }

    public boolean doItemsMatch(ItemStack itemStackIn)
    {
        if(hasItem())
        {
            if(itemStackIn.getHasSubtypes())
            {
                if(itemStackIn.getItem().equals(item.getStackInSlot(0).getItem()) && itemStackIn.getMetadata()==item.getStackInSlot(0).getMetadata())
                {
                    return true;
                }
                else return false;
            }
            else if(itemStackIn.hasTagCompound())
            {
                NBTTagCompound itemIn = itemStackIn.getTagCompound();
                NBTTagCompound itemStored = item.getStackInSlot(0).getTagCompound();
                if(itemIn.equals(itemStored) && itemStackIn.getItem().equals(item.getStackInSlot(0).getItem()))
                {
                    return true;
                }
                else return false;
            }
            else
            {
                if(itemStackIn.getItem().equals(item.getStackInSlot(0).getItem()))
                {
                    return true;
                }
            }
        }
        else{return true;}


        return false;
    }

    public boolean hasUpgrade(TilePedestal getReciever, Item coinType)
    {
        if(getReciever.hasCoin())
        {
            if(getReciever.getCoinOnPedestal().getItem().equals(coinType))
            {
                return true;
            }
        }

        return false;
    }

    public boolean hasUpgrade(Item coinType)
    {
        if(hasCoin())
        {
            if(getCoinOnPedestal().getItem().equals(coinType))
            {
                return true;
            }
        }

        return false;
    }

    public boolean hasEffectUpgrade()
    {

        if(hasCoin())
        {
            if(this.coin.getStackInSlot(0).getItem().equals(ItemRegistry.effectUpgrade))
            {
                if(this.coin.getStackInSlot(0).hasTagCompound())
                {
                    return true;
                }
            }
        }
        return false;
    }

    public PotionEffect getEffectFromUpgrade()
    {
        PotionEffect potionEffect = new PotionEffect(MobEffects.LUCK);

        if(hasCoin())
        {
            if(this.coin.getStackInSlot(0).hasTagCompound())
            {
                potionEffect = PotionEffect.readCustomPotionEffectFromNBT(this.coin.getStackInSlot(0).getTagCompound().getCompoundTag("coineffect"));
            }
        }
        return potionEffect;
    }

    public int getUpgradePotency()
    {
        int range = 1;
        if(hasCoin())
        {
            if(getCoinOnPedestal().isItemEnchanted())
            {
                range = EnchantmentHelper.getEnchantmentLevel(EnchantmentRegistry.enchantmentRange,getCoinOnPedestal())+1;
            }
        }
        return range;
    }







    public boolean addOutputLocation(BlockPos getWrench)
    {

        if(canLinkToPedestalNetwork(getWrench))
        {
            for(int i=0;i<storedOutputLocations.length;i++)
            {
                if(getWrench == storedOutputLocations[i])
                {
                    return false;
                }
                else if(defaultPos == storedOutputLocations[i])
                {

                    storedOutputLocations[i] = getWrench;
                    return true;
                }
                //s(storedOutputLocations[i]);
            }
            return false;
        }
        else return false;

    }
    //Checks when linking pedestals if the two being linked are 1.on the same network 2. if one is neutral thus meaning they can link
    public boolean canLinkToPedestalNetwork(BlockPos pedestalToBeLinked)
    {
        //Check to see if pedestal to be linked is a block pedestal
        if(world.getBlockState(pedestalToBeLinked).getBlock() instanceof BlockPedestal)
        {
            //checks to see if pedestal to be linked and this one are the same
            if(world.getBlockState(pedestalToBeLinked).getBlock().equals(world.getBlockState(this.getPos()).getBlock()))
            {
                return true;
            }
            //if they arnt then check if one is neutral
            else if(world.getBlockState(pedestalToBeLinked).getBlock().equals(BlockPedestal.pedestalstone) ||
                    world.getBlockState(this.getPos()).getBlock().equals(BlockPedestal.pedestalstone))
            {
                return true;
            }
            else return false;
        }

        return false;
    }

    public boolean hasConnectionAlready(BlockPos pedestalToBeLinked)
    {
        for(int i=0;i<storedOutputLocations.length;i++)
        {
            if(storedOutputLocations[i].equals(pedestalToBeLinked))
            {
                return true;
            }
        }

        return false;
    }

    public void removeConnection(BlockPos blockToRemove)
    {

        for(int i=0;i<storedOutputLocations.length;i++) {
            if (storedOutputLocations[i].equals(blockToRemove)) {
                storedOutputLocations[i]=defaultPos;

            }
        }

        BlockPos[] copyArray = {defaultPos,defaultPos,defaultPos,defaultPos,defaultPos,defaultPos,defaultPos,defaultPos};
        int j = 0;
        for(int i=0;i<storedOutputLocations.length;i++) {
            if (!(storedOutputLocations[i].equals(defaultPos))) {
                copyArray[j] = storedOutputLocations[i];
                j++;
            }
        }

        storedOutputLocations = copyArray;

    }

    public boolean isSamePedestal(BlockPos pedestalToBeLinked)
    {
        BlockPos thisPedestal = this.getPos();

        if(thisPedestal.equals(pedestalToBeLinked))
        {
            return true;
        }

        return false;
    }

    public boolean isPedestalInRange(BlockPos pedestalToBeLinked)
    {
        int x = pedestalToBeLinked.getX();
        int y = pedestalToBeLinked.getY();
        int z = pedestalToBeLinked.getZ();
        int x1 = this.getPos().getX();
        int y1 = this.getPos().getY();
        int z1 = this.getPos().getZ();
        int xF = Math.abs(Math.subtractExact(x,x1));
        int yF = Math.abs(Math.subtractExact(y,y1));
        int zF = Math.abs(Math.subtractExact(z,z1));

        if(xF>getPedestalRange || yF>getPedestalRange || zF>getPedestalRange)
        {
            return false;
        }
        else return true;
    }

    public int numConnections()
    {
        int connections = 0;
        for(int i=0;i<storedOutputLocations.length;i++)
        {
            if(!storedOutputLocations[i].equals(defaultPos))
            {
                connections++;
            }
        }
        return connections;
    }

    private boolean doesRecieverExistandIsLoaded(BlockPos getRecieverPos)
    {
        if(world.getBlockState(getRecieverPos)!=null)
        {
            if(world.isBlockLoaded(getRecieverPos))
            {
                return true;
            }
            else return false;
        }
        else return false;
    }




    public int roomLeftInStack(ItemStack addToThisStack)
    {
        int maxSize = addToThisStack.getMaxStackSize();
        int currentlyStored = addToThisStack.getCount();
        int countLeftTillFilled = maxSize-currentlyStored;
        return countLeftTillFilled;
    }

    /*
    public BlockPos getPosOfBlockBelow(int numBelow)
    {
        IBlockState state = this.getWorld().getBlockState(this.getPos());
        EnumFacing enumfacing = state.getValue(FACING);
        BlockPos blockBelow = this.getPos();
        switch (enumfacing)
        {
            case UP:
                return blockBelow.add(0,-numBelow,0);
            case DOWN:
                return blockBelow.add(0,numBelow,0);
            case NORTH:
                return blockBelow.add(0,0,numBelow);
            case SOUTH:
                return blockBelow.add(0,0,-numBelow);
            case EAST:
                return blockBelow.add(-numBelow,0,0);
            case WEST:
                return blockBelow.add(numBelow,0,0);
            default:
                return blockBelow;
        }
    }
     */

    public boolean getAndSend(TilePedestal recieverPedestal)
    {
        //getting the stack from this, the sender
        ItemStack stackToAdd = item.getStackInSlot(0).copy();
        ItemStack stackInReciever = recieverPedestal.getItemInPedestal();
        int itemCountInReciever = stackInReciever.getCount();
        int holdItemsInInv = getRedstoneSignalIn();


        //is the reciever has room in inventory
        if(roomLeftInStack(stackInReciever)!=0)
        {
            //checking if sender has at least transferSizeCount in its inventory
            if(this.getItemInPedestal().getCount()-holdItemsInInv>=transferSizeCount)
            {
                //checking if receiver has enough room for transferSizeCount or more
                if(roomLeftInStack(stackInReciever)>=transferSizeCount)
                {
                    //set stack size to transfer to the transfer rate
                    stackToAdd.setCount(transferSizeCount);
                    //add to receiver
                    recieverPedestal.addItem(stackToAdd);
                    //remove from this(sender)
                    this.removeItem(transferSizeCount);
                    return true;
                }
                else
                {
                    int smallStackCount = roomLeftInStack(stackInReciever);
                    stackToAdd.setCount(smallStackCount);
                    recieverPedestal.addItem(stackToAdd);
                    removeItem(smallStackCount);
                    return true;
                }
            }
            //has less the transfer size in sender
            else
            {
                //checks is reciever has enough storage for whats left in this (the sender)
                if(roomLeftInStack(stackInReciever)>=this.getItemInPedestal().getCount()-holdItemsInInv)
                {
                    //set the input itemstack count to be added
                    stackToAdd.setCount(this.getItemInPedestal().getCount()-holdItemsInInv);
                    //actually add it
                    if(stackToAdd.getCount()>0)
                    {
                        recieverPedestal.addItem(stackToAdd);
                        //remove that count from the inventory ??just use regular remove since its all there is??
                        removeItem(this.getItemInPedestal().getCount()-holdItemsInInv);
                        return true;
                    }
                }
                //if the storage cant take in the remaining input item stack we figure out how much it can take.
                else
                {
                    //get how little room there is for storage
                    int smallStackCount = roomLeftInStack(stackInReciever);
                    //set the stack stack size for items being sent
                    stackToAdd.setCount(smallStackCount);
                    //recieve items from sender
                    recieverPedestal.addItem(stackToAdd);
                    //remove those items from sender
                    removeItem(smallStackCount);
                    return true;
                }
            }
        }

        return false;
    }

    public int getColor()
    {
        int color = 0x808080;
        if(world.getBlockState(pos).getBlock().equals(BlockPedestal.pedestalstone)){color = 0x808080;}
        else if(world.getBlockState(pos).getBlock().equals(BlockPedestal.pedestalred)){color = 0xff0008;}
        else if(world.getBlockState(pos).getBlock().equals(BlockPedestal.pedestalblue)){color = 0x000cff;}
        else if(world.getBlockState(pos).getBlock().equals(BlockPedestal.pedestalyellow)){color = 0xfff600;}
        else if(world.getBlockState(pos).getBlock().equals(BlockPedestal.pedestalpurple)){color = 0xb200ff;}
        else if(world.getBlockState(pos).getBlock().equals(BlockPedestal.pedestalgreen)){color = 0x37b700;}
        else if(world.getBlockState(pos).getBlock().equals(BlockPedestal.pedestalorange)){color = 0xff6a00;}
        else if(world.getBlockState(pos).getBlock().equals(BlockPedestal.pedestalwhite)){color = 0xeaeaea;}
        else if(world.getBlockState(pos).getBlock().equals(BlockPedestal.pedestalblack)){color = 0x3f3f3f;}
        return color;
    }

    public void spawnParticles(int reciever){
        if(world.isRemote)
        {
            Vector3f dir = new Vector3f(
                    pos.getX() - storedOutputLocations[0].getX(),
                    pos.getY() - storedOutputLocations[0].getY(),
                    pos.getZ() - storedOutputLocations[0].getZ());

            //dir.normalize();

            Random rand = new Random();

            float i = dir.x;
            float j = dir.z;
            float k = dir.y;

            //world.spawnParticle(EnumParticleTypes.CLOUD, (double)storedOutputLocations[reciever].getX() + 0.5D, (double)storedOutputLocations[reciever].getY() + 1.0D, (double)storedOutputLocations[reciever].getZ() + 0.5D,
            //(double)((float)i ), (double)((float)k -1f), (double)((float)j ), new int[0]);

            new ParticlesInALine(world,pos.getX()+0.5f,pos.getY()+1f,pos.getZ()+0.5f,storedOutputLocations[0].getX()+0.5f,storedOutputLocations[0].getY()+1f,storedOutputLocations[0].getZ()+0.5f,1.0f,getColor(),1.0f);
        }
    }

    /*
    private boolean sendItemsToReciever(int listIndex)
    {
        if(!world.isRemote)
        {
            if(!storedOutputLocations[listIndex].equals(defaultPos))
            {
                if(doesRecieverExistandIsLoaded(storedOutputLocations[listIndex]))
                {
                    TileEntity tileEntity = world.getTileEntity(storedOutputLocations[listIndex]);
                    if (tileEntity instanceof TilePedestal) {
                        TilePedestal tileRecieverPedestal = (TilePedestal) tileEntity;

                        if(tileRecieverPedestal.hasCoin())
                        {
                            if(hasUpgrade(tileRecieverPedestal, ItemRegistry.filterUpgrade))
                            {
                                if(tileRecieverPedestal.hasFilterableInventoryBelow())
                                {
                                    if(areFilteredItemsEqual(tileRecieverPedestal,this.item.getStackInSlot(0),FilterTypes.NORMAL))
                                    {
                                        if(tileRecieverPedestal.doItemsMatch(item.getStackInSlot(0)))
                                        {
                                            getAndSend(tileRecieverPedestal);
                                        }
                                    }
                                }
                            }

                            if(hasUpgrade(tileRecieverPedestal, ItemRegistry.fuzzyFilterUpgrade))
                            {
                                if(tileRecieverPedestal.hasFilterableInventoryBelow())
                                {
                                    if(areFilteredItemsEqual(tileRecieverPedestal,this.item.getStackInSlot(0),FilterTypes.FUZZY))
                                    {
                                        if(tileRecieverPedestal.doItemsMatch(item.getStackInSlot(0)))
                                        {
                                            getAndSend(tileRecieverPedestal);
                                        }

                                    }
                                }
                            }

                            if(hasUpgrade(tileRecieverPedestal, ItemRegistry.filterModUpgrade))
                            {
                                if(tileRecieverPedestal.hasFilterableInventoryBelow())
                                {
                                    if(areFilteredItemsEqual(tileRecieverPedestal,this.item.getStackInSlot(0),FilterTypes.MOD))
                                    {
                                        if(tileRecieverPedestal.doItemsMatch(item.getStackInSlot(0)))
                                        {
                                            getAndSend(tileRecieverPedestal);
                                        }

                                    }
                                }
                            }



                            if(hasUpgrade(tileRecieverPedestal, ItemRegistry.filterBlacklistUpgrade))
                            {
                                if(tileRecieverPedestal.hasFilterableInventoryBelow())
                                {
                                    if(!areFilteredItemsEqual(tileRecieverPedestal,this.item.getStackInSlot(0),FilterTypes.NORMAL))
                                    {
                                        if(tileRecieverPedestal.doItemsMatch(item.getStackInSlot(0)))
                                        {
                                            getAndSend(tileRecieverPedestal);
                                        }
                                    }
                                }
                            }

                            if(hasUpgrade(tileRecieverPedestal, ItemRegistry.fuzzyFilterBlacklistUpgrade))
                            {
                                if(tileRecieverPedestal.hasFilterableInventoryBelow())
                                {
                                    if(!areFilteredItemsEqual(tileRecieverPedestal,this.item.getStackInSlot(0),FilterTypes.FUZZY))
                                    {
                                        if(tileRecieverPedestal.doItemsMatch(item.getStackInSlot(0)))
                                        {
                                            getAndSend(tileRecieverPedestal);
                                        }

                                    }
                                }
                            }

                            if(hasUpgrade(tileRecieverPedestal, ItemRegistry.filterModBlacklistUpgrade))
                            {
                                if(tileRecieverPedestal.hasFilterableInventoryBelow())
                                {
                                    if(!areFilteredItemsEqual(tileRecieverPedestal,this.item.getStackInSlot(0),FilterTypes.MOD))
                                    {
                                        if(tileRecieverPedestal.doItemsMatch(item.getStackInSlot(0)))
                                        {
                                            getAndSend(tileRecieverPedestal);
                                        }

                                    }
                                }
                            }

                            if(hasUpgrade(tileRecieverPedestal, ItemRegistry.effectUpgrade))
                            {
                                if(tileRecieverPedestal.getEffectFromUpgrade().getPotion().equals(PotionRegistry.POTION_GROWER) ||
                                        tileRecieverPedestal.getEffectFromUpgrade().getPotion().equals(PotionRegistry.POTION_PLANTER))
                                {
                                    if(tileRecieverPedestal.doItemsMatch(item.getStackInSlot(0)))
                                    {
                                        getAndSend(tileRecieverPedestal);
                                    }
                                }

                            }

                            //List of upgraded pedestals that can recieve items
                            Item[] sendToList = {ItemRegistry.ancientCoin,ItemRegistry.ancientCoinA,ItemRegistry.ancientCoinB,ItemRegistry.ancientCoinC,ItemRegistry.ancientCoinD,ItemRegistry.ancientCoinE,ItemRegistry.ancientCoinF,
                                    ItemRegistry.ancientCoinG,ItemRegistry.ancientCoinH,ItemRegistry.ancientCoinI,ItemRegistry.ancientCoinJ,ItemRegistry.ancientCoinK,ItemRegistry.ancientCoinL,ItemRegistry.ancientCoinM,
                                    ItemRegistry.ancientCoinN,ItemRegistry.ancientCoinO,ItemRegistry.ancientCoinP,ItemRegistry.ancientCoinQ,ItemRegistry.ancientCoinR,ItemRegistry.ancientCoinS,ItemRegistry.ancientCoinT,
                                    ItemRegistry.ancientCoinU,ItemRegistry.ancientCoinV,ItemRegistry.ancientCoinW,ItemRegistry.ancientCoinX,ItemRegistry.ancientCoinY,ItemRegistry.ancientCoinZ,ItemRegistry.dropperUpgrade,
                                    ItemRegistry.placerUpgrade,ItemRegistry.exportUpgrade,ItemRegistry.singleExportUpgrade};

                            for(int i=0;i<sendToList.length;i++)
                            {
                                if(hasUpgrade(tileRecieverPedestal,sendToList[i]))
                                {
                                    if(tileRecieverPedestal.doItemsMatch(item.getStackInSlot(0)))
                                    {
                                        getAndSend(tileRecieverPedestal);
                                    }
                                }
                            }
                        }
                        else
                        {
                            if(tileRecieverPedestal.doItemsMatch(item.getStackInSlot(0)))
                            {
                                getAndSend(tileRecieverPedestal);
                            }
                        }
                    }
                }
            }
            return false;
        }
        return false;
    }
     */

    private void tryToSendItemToPedestal()
    {
        /*
        if(this.hasItem())
        {
            if(numConnections()>0)
            {
                if(sendItemsToReciever(0)){}
                else if(sendItemsToReciever(1)){}
                else if(sendItemsToReciever(2)){}
                else if(sendItemsToReciever(3)){}
                else if(sendItemsToReciever(4)){}
                else if(sendItemsToReciever(5)){}
                else if(sendItemsToReciever(6)){}
                else if(sendItemsToReciever(7)){}
            }
        }
         */
    }

    public void getStoredBlockPoss()
    {
        for(int i=0;i<storedOutputLocations.length;i++)
        {
            //System.out.println(storedOutputLocations[i]);
        }
    }



    public int getRedstoneSignalIn()
    {
        IBlockState state = this.getWorld().getBlockState(this.getPos());
        EnumFacing enumfacing = state.getValue(FACING);
        int totalPower = 0;
        int pow1 = 0;
        int pow2 = 0;
        int pow3 = 0;
        int pow4 = 0;

        if(world.isBlockPowered(pos))
        {
            if(enumfacing.equals(EnumFacing.UP) || enumfacing.equals(EnumFacing.DOWN))
            {
                BlockPos pos1 = pos.add(1,0,0);
                BlockPos pos2 = pos.add(-1,0,0);
                BlockPos pos3 = pos.add(0,0,1);
                BlockPos pos4 = pos.add(0,0,-1);

                pow1 = getRedstonePowerFromPos(pos1);
                pow2 = getRedstonePowerFromPos(pos2);
                pow3 = getRedstonePowerFromPos(pos3);
                pow4 = getRedstonePowerFromPos(pos4);

                totalPower = pow1 + pow2 + pow3 + pow4;
            }
            else if(enumfacing.equals(EnumFacing.EAST) || enumfacing.equals(EnumFacing.WEST))
            {
                BlockPos pos1 = pos.add(0,1,0);
                BlockPos pos2 = pos.add(0,-1,0);
                BlockPos pos3 = pos.add(0,0,1);
                BlockPos pos4 = pos.add(0,0,-1);
                pow1 = getRedstonePowerFromPos(pos1);
                pow2 = getRedstonePowerFromPos(pos2);
                pow3 = getRedstonePowerFromPos(pos3);
                pow4 = getRedstonePowerFromPos(pos4);

                totalPower = pow1 + pow2 + pow3 + pow4;
            }
            else if(enumfacing.equals(EnumFacing.NORTH) || enumfacing.equals(EnumFacing.SOUTH))
            {
                BlockPos pos1 = pos.add(0,1,0);
                BlockPos pos2 = pos.add(0,-1,0);
                BlockPos pos3 = pos.add(1,0,0);
                BlockPos pos4 = pos.add(-1,0,0);
                pow1 = getRedstonePowerFromPos(pos1);
                pow2 = getRedstonePowerFromPos(pos2);
                pow3 = getRedstonePowerFromPos(pos3);
                pow4 = getRedstonePowerFromPos(pos4);

                totalPower = pow1 + pow2 + pow3 + pow4;
            }

        }

        return totalPower;

    }

    public int getRedstonePowerFromPos(BlockPos pos)
    {
        Boolean isPowered = false;
        int pow=0;
        if(isBlockRedstoneComparator(pos))
        {
            TileEntity tileComp = world.getTileEntity(pos);
            if(tileComp instanceof TileEntityComparator)
            {
                TileEntityComparator tileComparator = (TileEntityComparator)tileComp;
                pow = tileComparator.getOutputSignal();
            }
        }
        if(isBlockRedstoneWire(pos))
        {
            pow = world.getBlockState(pos).getValue(BlockRedstoneWire.POWER);
        }
        else if(isBlockRedstoneLever(pos))
        {
            isPowered = world.getBlockState(pos).getValue(BlockLever.POWERED);
            if(isPowered)
            {
                pow = 16;
            }
        }
        else if(isBlockRedstoneBlock(pos))
        {
            pow = 16;
        }

        return pow;
    }

    public boolean isBlockRedstoneWire(BlockPos pos)
    {
        Block redstoneWire = world.getBlockState(pos).getBlock();
        if(redstoneWire instanceof BlockRedstoneWire)
        {
            return true;
        }
        return false;
    }

    public boolean isBlockRedstoneComparator(BlockPos pos)
    {
        Block redstoneComp = world.getBlockState(pos).getBlock();
        if(redstoneComp instanceof BlockRedstoneComparator)
        {
            TileEntity tileComp = world.getTileEntity(pos);
            if(tileComp instanceof TileEntityComparator)
            {
                return true;
            }
        }

        return false;
    }

    public boolean isBlockRedstoneLever(BlockPos pos)
    {
        Block redstoneLever = world.getBlockState(pos).getBlock();
        if(redstoneLever instanceof BlockLever)
        {
            return true;
        }
        return false;
    }

    public boolean isBlockRedstoneBlock(BlockPos pos)
    {
        Block redstoneBlock = world.getBlockState(pos).getBlock();
        if(redstoneBlock.equals(Blocks.REDSTONE_BLOCK))
        {
            return true;
        }
        return false;
    }

    public boolean isBlockUnder(int x,int y,int z)
    {
        TileEntity tileEntity = world.getTileEntity(pos.add(x,y,z));
        if(tileEntity instanceof ICapabilityProvider)
        {
            return true;
        }
        return false;
    }

    public ItemStack getNextSlotInChest(BlockPos blockOfInv)
    {
        ItemStack stack = ItemStack.EMPTY;
        if(world.getTileEntity(blockOfInv).hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.DOWN))
        {
            TileEntity tileEntity = world.getTileEntity(blockOfInv);
            if(tileEntity instanceof TilePedestal) {
                stack = ItemStack.EMPTY;

            }
            else {
                stack = IntStream.range(0,tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.DOWN).getSlots())//Int Range
                        .mapToObj((tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.DOWN))::getStackInSlot)//Function being applied to each interval
                        .filter(itemStack -> !itemStack.isEmpty())
                        .findFirst().orElse(ItemStack.EMPTY);}
        }
        return stack;
    }

    int impTicker = 0;
    @Override
    public void update() {

        if(!world.isRemote)
        {
            Item coinInPed = getCoinOnPedestal().getItem();
            if(coinInPed instanceof ipuBasic)
            {
                impTicker++;
                ((ipuBasic) coinInPed).updateAction(impTicker,this.world,getItemInPedestal(),getCoinOnPedestal(),this.getPos());
                if(impTicker >=20){impTicker=0;}
            }
            display = getItemInPedestal();
            updateBlock();
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setTag("ItemStackItemInventoryHandler", this.item.serializeNBT());
        compound.setTag("ItemStackCoinInventoryHandler", this.coin.serializeNBT());
        compound.setTag("display",display.writeToNBT(new NBTTagCompound()));
        compound.setInteger("expPedestal",expInPedestal);
        int counter = 0;
        for(int i=0;i<storedOutputLocations.length;i++)
        {
            if(storedOutputLocations[i].equals(defaultPos))
            {
                continue;
            }
            else
            {
                String keyNameX = "storedLocationX" + i;
                String keyNameY = "storedLocationY" + i;
                String keyNameZ = "storedLocationZ" + i;
                compound.setInteger(keyNameX,storedOutputLocations[i].getX());
                compound.setInteger(keyNameY,storedOutputLocations[i].getY());
                compound.setInteger(keyNameZ,storedOutputLocations[i].getZ());
                counter++;
            }
        }
        compound.setInteger("storedBlockPosCounter",counter);

        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.item.deserializeNBT(compound.getCompoundTag("ItemStackItemInventoryHandler"));
        this.coin.deserializeNBT(compound.getCompoundTag("ItemStackCoinInventoryHandler"));
        this.expInPedestal = compound.getInteger("expPedestal");
        NBTTagCompound itemTagD = compound.getCompoundTag("display");
        int counter = compound.getInteger("storedBlockPosCounter");

        for(int i=0;i<counter;i++)
        {
            String getKeyNameX = "storedLocationX" + i;
            String getKeyNameY = "storedLocationY" + i;
            String getKeyNameZ = "storedLocationZ" + i;
            int getX = compound.getInteger(getKeyNameX);
            int getY = compound.getInteger(getKeyNameY);
            int getZ = compound.getInteger(getKeyNameZ);
            BlockPos gotPos = new BlockPos(getX,getY,getZ);
            storedOutputLocations[i] = gotPos;
        }


        this.display = new ItemStack(itemTagD);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        //if(!hasCoin())
        //{
        if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return (T) this.item;

        //}
        return super.getCapability(capability, facing);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        //if(!hasCoin())
        //{
        if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return true;
        //}
        return super.hasCapability(capability, facing);
    }
}