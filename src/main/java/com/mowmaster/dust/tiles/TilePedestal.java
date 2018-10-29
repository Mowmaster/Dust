package com.mowmaster.dust.tiles;

import com.mowmaster.dust.blocks.BlockPedestal;
import com.mowmaster.dust.blocks.BlockRegistry;
import com.mowmaster.dust.effects.PotionMagnetism;
import com.mowmaster.dust.effects.PotionRegistry;
import com.mowmaster.dust.enums.FilterTypes;
import com.mowmaster.dust.enums.ItemTransferTypes;
import com.mowmaster.dust.items.ItemRegistry;
import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.*;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerEnchantment;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.*;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.*;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.ILockableContainer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
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
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.IntStream;

import static net.minecraft.block.BlockDirectional.FACING;

public class TilePedestal extends TileEntity implements ITickable, ICapabilityProvider
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
    public ItemStack getItemInPedestal() {return item.getStackInSlot(0);}
    public ItemStack getCoinOnPedestal() {return coin.getStackInSlot(0);}
    public ItemStack getDisplay() {return display;}
    public int getXPInPedestal() {return expInPedestal;}

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

    public boolean doItemsMatch(ItemStack itemStackIn, ItemStack toCompareTo)
    {
        if(hasItem())
        {
            if(itemStackIn.getHasSubtypes())
            {
                if(itemStackIn.getItem().equals(toCompareTo.getItem()) && itemStackIn.getMetadata()==toCompareTo.getMetadata())
                {
                    return true;
                }
                else return false;
            }
            else if(itemStackIn.hasTagCompound())
            {
                NBTTagCompound itemIn = itemStackIn.getTagCompound();
                NBTTagCompound itemStored = toCompareTo.getTagCompound();
                if(itemIn.equals(itemStored) && itemStackIn.getItem().equals(toCompareTo.getItem()))
                {
                    return true;
                }
                else return false;
            }
            else
            {
                if(itemStackIn.getItem().equals(toCompareTo.getItem()))
                {
                    return true;
                }
            }
        }
        else{return true;}


        return false;
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

    private void updateBlock()
    {
        markDirty();
        IBlockState state = world.getBlockState(pos);
        world.getRedstonePower(pos,EnumFacing.UP);
        world.notifyBlockUpdate(pos, state, state, 3);
        world.setBlockState(pos,state,3);
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
        return getEffectFromUpgrade().getAmplifier()+1;
    }

    public float getEnchantmentPowerFromSorroundings()
    {
        float enchantPower = 0;

        for (int i = -2; i <= 2; ++i) {
            for (int j = -2; j <= 2; ++j) {
                if (i > -2 && i < 2 && j == -1) {
                    j = 2;
                }
                for (int k = 0; k <= 2; ++k) {
                    BlockPos blockpos = pos.add(i, k, j);
                    Block blockNearBy = world.getBlockState(blockpos).getBlock();

                    if (blockNearBy.getEnchantPowerBonus(world, blockpos)>0)
                    {
                        enchantPower +=blockNearBy.getEnchantPowerBonus(world, blockpos);
                    }
                }
            }
        }
                return enchantPower;
    }

    public void useExpToBottleEnchantRepair()
    {
        if(!world.isBlockPowered(pos))
        {
            float getEnchantPower = getEnchantmentPowerFromSorroundings();
            ItemStack itemFromInv = ItemStack.EMPTY;
            if(world.getTileEntity(getPosOfBlockBelow(1)) !=null)
            {
                if(world.getTileEntity(getPosOfBlockBelow(1)).hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.DOWN))
                {

                    TileEntity invToPullFrom = world.getTileEntity(getPosOfBlockBelow(1));
                    if(invToPullFrom instanceof TilePedestal) {
                        itemFromInv = ItemStack.EMPTY;

                    }
                    else {
                        itemFromInv = IntStream.range(0,invToPullFrom.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.DOWN).getSlots())//Int Range
                                .mapToObj((invToPullFrom.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.DOWN))::getStackInSlot)//Function being applied to each interval
                                .filter(itemStack -> !itemStack.isEmpty())
                                .findFirst().orElse(ItemStack.EMPTY);

                        if(!itemFromInv.equals(ItemStack.EMPTY))
                        {
                            if(!hasItem())
                            {
                                if(itemFromInv.getItem().equals(Items.GLASS_BOTTLE))
                                {
                                    if(expInPedestal>=10)
                                    {
                                        itemFromInv.shrink(1);
                                        item.insertItem(0,new ItemStack(Items.EXPERIENCE_BOTTLE,1),false);
                                        expInPedestal -=10;
                                    }
                                }
                                else if(itemFromInv.getItem().equals(Items.BOOK))
                                {
                                    Random rn = new Random();
                                    ItemStack enchantedBook = itemFromInv.copy();
                                    enchantedBook.setCount(1);

                                    if(getEnchantPower<16.0f)
                                    {
                                        getEnchantPower = getEnchantmentPowerFromSorroundings();
                                    }
                                    else getEnchantPower = 15.0f;

                                    if(expInPedestal>=((int)((getEnchantPower*2.5)*(getEnchantPower*2.5)))+7)
                                    {
                                        int level = (int)getEnchantPower*2;

                                        ItemStack bookEnchanted = EnchantmentHelper.addRandomEnchantment(rn,enchantedBook,level,true);
                                        item.insertItem(0,bookEnchanted,false);
                                        itemFromInv.shrink(1);

                                        expInPedestal -=((int)((getEnchantPower*2.5)*(getEnchantPower*2.5)))+7;
                                    }
                                }
                                else if(itemFromInv.getItem().isDamageable() && itemFromInv.getItem().getDamage(itemFromInv)>0)
                                {

                                    if(getCoinOnPedestal().isItemEnchanted())
                                    {
                                        if(EnchantmentHelper.getEnchantments(coin.getStackInSlot(0)).containsKey(Enchantments.MENDING))
                                        {
                                            if(expInPedestal>=1)
                                            {
                                                int getDamage = itemFromInv.getItemDamage();
                                                if(getDamage>1)
                                                {
                                                    itemFromInv.setItemDamage(getDamage-1);
                                                    expInPedestal -=1;
                                                }
                                                else
                                                {
                                                    ItemStack itemFromInvCopy = itemFromInv.copy();
                                                    itemFromInvCopy.setItemDamage(0);
                                                    item.insertItem(0,itemFromInvCopy,false);
                                                    itemFromInv.shrink(1);
                                                }
                                            }
                                        }
                                    }
                                }
                                else if(!itemFromInv.isItemEnchanted())
                                {
                                    if(itemFromInv.isItemEnchantable())
                                    {
                                        Random rn = new Random();
                                        ItemStack itemFromInvCopy = itemFromInv.copy();
                                        itemFromInvCopy.setCount(1);

                                        if(getEnchantPower<16.0f)
                                        {
                                            getEnchantPower = getEnchantmentPowerFromSorroundings();
                                        }
                                        else getEnchantPower = 15.0f;

                                        if(expInPedestal>=((int)((getEnchantPower*2.5)*(getEnchantPower*2.5)))+7)
                                        {
                                            int level = (int)getEnchantPower*2;

                                            EnchantmentHelper.addRandomEnchantment(rn,itemFromInvCopy,level,true);
                                            item.insertItem(0,itemFromInvCopy,false);
                                            itemFromInv.shrink(1);

                                            expInPedestal -=((int)((getEnchantPower*2.5)*(getEnchantPower*2.5)))+7;
                                        }
                                    }
                                }

                            }
                            else if(getItemInPedestal().getItem().equals(Items.EXPERIENCE_BOTTLE))
                            {
                                if(expInPedestal>=10)
                                {
                                    if(getItemInPedestal().getCount() <= getMaxStackSize()-1)
                                    {
                                        itemFromInv.shrink(1);
                                        item.insertItem(0,new ItemStack(Items.EXPERIENCE_BOTTLE,1),false);
                                        expInPedestal -=10;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    protected int counter = 0;
    public void crafter(int gridSize)//1x1, 2x2, 3x3
    {

        ItemStack nextStackToAdd = ItemStack.EMPTY;
        InventoryCrafting craft = new InventoryCrafting(new Container()
            {
            @Override
            public boolean canInteractWith(@Nonnull EntityPlayer player) {
                return false;
            }
            }, gridSize, gridSize);

        int gridIterateSize = gridSize*gridSize;


        if(!world.isBlockPowered(pos)) {

            if(nextStackToAdd.isEmpty())
            {
                if (world.getTileEntity(getPosOfBlockBelow(1)) != null) {
                    if (world.getTileEntity(getPosOfBlockBelow(1)).hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN)) {
                        TileEntity invToPushTo = world.getTileEntity(getPosOfBlockBelow(1));
                        if (invToPushTo instanceof TilePedestal) {
                            return;
                        }

                        if(invToPushTo.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.DOWN).getSlots()==gridIterateSize)
                        {
                            int itemInStackCount = 0;
                            for(int i = 0; i < gridIterateSize; i++) {
                                ItemStack stack = invToPushTo.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.DOWN).getStackInSlot(i);
                                if(stack.getCount()>=2 || stack.isEmpty())
                                {
                                    itemInStackCount++;
                                }
                            }

                            if(itemInStackCount>=gridIterateSize)
                            {
                                for(int i = 0; i < gridIterateSize; i++) {
                                    ItemStack stack = invToPushTo.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.DOWN).getStackInSlot(i);

                                    if(stack.isEmpty())
                                        continue;

                                    craft.setInventorySlotContents(i, stack);
                                }

                                for(IRecipe recipe : ForgeRegistries.RECIPES)
                                {
                                    if(recipe.matches(craft, world)) {
                                        if(!hasItem())
                                        {
                                            this.addItem(recipe.getCraftingResult(craft));

                                            for(int i = 0; i < gridIterateSize; i++) {
                                                ItemStack stack = invToPushTo.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.DOWN).getStackInSlot(i);
                                                if(stack.isEmpty())
                                                    continue;

                                                if(stack.getItem().hasContainerItem(stack))
                                                {
                                                    ItemStack container = stack.getItem().getContainerItem(stack);
                                                    nextStackToAdd = container.copy();
                                                }
                                                else
                                                {
                                                    stack.shrink(1);
                                                }

                                            }
                                        }
                                        else
                                        {
                                            if(doItemsMatch(recipe.getCraftingResult(craft)))
                                            {
                                                if(getItemInPedestal().getCount()<getMaxStackSize())
                                                {
                                                    this.addItem(recipe.getCraftingResult(craft));
                                                }


                                                for(int i = 0; i < gridIterateSize; i++) {
                                                    ItemStack stack = invToPushTo.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.DOWN).getStackInSlot(i);
                                                    if(stack.isEmpty())
                                                        continue;

                                                    if(stack.getItem().hasContainerItem(stack))
                                                    {
                                                        ItemStack container = stack.getItem().getContainerItem(stack);
                                                        nextStackToAdd = container.copy();
                                                    }
                                                    else
                                                    {
                                                        stack.shrink(1);
                                                    }
                                                }
                                            }
                                        }

                                    }
                                }
                            }
                        }
                        else
                        {
                            int counted = invToPushTo.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.DOWN).getSlots()/gridIterateSize;
                            if(counter<counted)
                            {
                                if(!hasItem())
                                {
                                    // 0 1 2 3 4 5 6 7 8
                                    // 9 10 11 12 13 14 15 16 17
                                    // 18 19 20 21 22 23 24 25 26
                                    int fin = ((counter+1)*gridIterateSize)-1;
                                    int itemInStackCount = 0;
                                    for(int j = (fin-(gridIterateSize-1)); j <=fin; j++) {
                                        ItemStack stack = invToPushTo.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.DOWN).getStackInSlot(j);
                                        if(stack.getCount()>=2 || stack.isEmpty())
                                        {
                                            itemInStackCount++;
                                        }
                                    }

                                    if(itemInStackCount>=gridIterateSize)
                                    {
                                        for(int i = (fin-(gridIterateSize-1)); i <=fin; i++) {
                                            ItemStack stack = invToPushTo.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.DOWN).getStackInSlot(i);

                                            if(stack.isEmpty())
                                                continue;

                                            int getCraftingSlot = i-(counter*gridIterateSize);
                                            craft.setInventorySlotContents(getCraftingSlot, stack);
                                        }

                                        for(IRecipe recipe : ForgeRegistries.RECIPES)
                                        {
                                            if(recipe.matches(craft, world)) {
                                                if(!hasItem())
                                                {
                                                    this.addItem(recipe.getCraftingResult(craft));

                                                    for(int i = (fin-(gridIterateSize-1)); i <=fin; i++) {
                                                        ItemStack stack = invToPushTo.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.DOWN).getStackInSlot(i);
                                                        if(stack.isEmpty())
                                                            continue;

                                                        if(stack.getItem().hasContainerItem(stack))
                                                        {
                                                            ItemStack container = stack.getItem().getContainerItem(stack);
                                                            nextStackToAdd = container.copy();
                                                        }
                                                        else
                                                        {
                                                            stack.shrink(1);
                                                        }

                                                    }
                                                }
                                                else
                                                {
                                                    if(doItemsMatch(recipe.getCraftingResult(craft)))
                                                    {
                                                        if(getItemInPedestal().getCount()<getMaxStackSize())
                                                        {
                                                            this.addItem(recipe.getCraftingResult(craft));
                                                        }


                                                        for(int i = (fin-(gridIterateSize-1)); i <=fin; i++) {
                                                            ItemStack stack = invToPushTo.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.DOWN).getStackInSlot(i);
                                                            if(stack.isEmpty())
                                                                continue;

                                                            if(stack.getItem().hasContainerItem(stack))
                                                            {
                                                                ItemStack container = stack.getItem().getContainerItem(stack);
                                                                nextStackToAdd = container.copy();
                                                            }
                                                            else
                                                            {
                                                                stack.shrink(1);
                                                            }
                                                        }
                                                    }
                                                }

                                            }
                                        }
                                    }

                                    counter++;
                                }
                            }

                            if(counter>=counted)
                            {
                                counter=0;
                            }
                        }
                        //sets the items from the inventory as a crafting pattern WE WILL CHECK the below inv and set the first 9 slots as this
                    }
                }
            }
            else if(hasItem()){}
            else{
                this.addItem(nextStackToAdd);
            }
        }

    }

    //Ehh doesnt work for much :/
    public void useItemFromPedestal()
    {
        WorldServer worldServer = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(0);
        FakePlayer fakePlayer = FakePlayerFactory.getMinecraft(worldServer);
        if(hasItem())
        {
            Item itemInPedestal = getItemInPedestal().getItem();
            itemInPedestal.onItemUse(fakePlayer,worldServer,getPosOfBlockBelow(2),EnumHand.MAIN_HAND,EnumFacing.UP,0.5f,0.5f,0.5f);
        }
    }

    public void placeBlockFromInventory()
    {
        if(hasItem())
        {
            Block block = Block.getBlockFromItem(item.getStackInSlot(0).getItem());
            if(block!=Blocks.AIR)
            {
                if(world.getBlockState(getPosOfBlockBelow(1)).getBlock().equals(Blocks.AIR))
                {
                    if(block instanceof IGrowable)
                    {
                        if(world.getBlockState(getPosOfBlockBelow(2)).getBlock().equals(Blocks.DIRT) || world.getBlockState(getPosOfBlockBelow(2)).getBlock().equals(Blocks.GRASS))
                        {
                            world.setBlockState(getPosOfBlockBelow(1),block.getDefaultState());
                            getItemInPedestal().shrink(1);
                        }
                    }
                    else {
                        world.setBlockState(getPosOfBlockBelow(1),block.getDefaultState());
                        getItemInPedestal().shrink(1);
                    }

                }
            }
        }
    }

    public void summonItemFromInventory()
    {

        if(!world.isRemote)
        {
            if(!world.isBlockPowered(pos))
            {
                if(hasItem())
                {
                    ItemStack itemToSummon = getItemInPedestal().copy();
                    itemToSummon.setCount(1);
                    EntityItem itemEntity = new EntityItem(world,getPosOfBlockBelow(-1).getX() + 0.5,getPosOfBlockBelow(-1).getY(),getPosOfBlockBelow(-1).getZ() + 0.5,itemToSummon);
                    itemEntity.motionX = 0;
                    itemEntity.motionY = 0;
                    itemEntity.motionZ = 0;
                    world.spawnEntity(itemEntity);
                    this.removeItem(itemToSummon.getCount());
                }
            }
        }

    }

    public ItemStack getDropsFromBlock()
    {
        int fortune = 0;
        Random rn = new Random();
        IBlockState blocky = world.getBlockState(getPosOfBlockBelow(1));
        ItemStack getDrops = ItemStack.EMPTY;

        if(!world.getBlockState(getPosOfBlockBelow(1)).getBlock().equals(Blocks.AIR))
        {
            if(hasCoin())
            {
                if(coin.getStackInSlot(0).getItem().equals(ItemRegistry.breakerUpgrade))
                {
                    if(coin.getStackInSlot(0).isItemEnchanted())
                    {
                        if(EnchantmentHelper.getEnchantments(coin.getStackInSlot(0)).containsKey(Enchantments.SILK_TOUCH))
                        {
                            getDrops = new ItemStack(blocky.getBlock());
                        }
                        else if(EnchantmentHelper.getEnchantments(coin.getStackInSlot(0)).containsKey(Enchantments.FORTUNE))
                        {
                            fortune = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE,coin.getStackInSlot(0)) + 1;
                            Item dropItem = blocky.getBlock().getItemDropped(blocky,rn,fortune);
                            int metaDropped = blocky.getBlock().damageDropped(blocky);
                            int countDropped = blocky.getBlock().quantityDropped(blocky,fortune,rn);
                            if(blocky.getBlock().getItemDropped(blocky,rn,fortune)!=null)
                            {
                                if(dropItem.getHasSubtypes())
                                {
                                    getDrops = new ItemStack(dropItem,countDropped,metaDropped);
                                }
                                else getDrops = new ItemStack(dropItem,countDropped);
                            }
                        }
                    }
                    else
                    {
                        Item dropItem = blocky.getBlock().getItemDropped(blocky,rn,0);
                        int metaDropped = blocky.getBlock().damageDropped(blocky);
                        int countDropped = blocky.getBlock().quantityDropped(blocky,0,rn);
                        if(blocky.getBlock().getItemDropped(blocky,rn,0)!=null)
                        {

                            if(dropItem.getHasSubtypes())
                            {
                                getDrops = new ItemStack(dropItem,countDropped,metaDropped);
                            }
                            else getDrops = new ItemStack(dropItem,countDropped);
                        }
                    }
                }
            }
        }
        return getDrops;
    }

    public void getXPOrbEntitiesNearby(int rangeIncrease)
    {
        World world = this.world;
        ItemStack stack = ItemStack.EMPTY;
        int increase = rangeIncrease;
        if(!world.isRemote)
        {
            List<EntityXPOrb> orbs = world.getEntitiesWithinAABB(EntityXPOrb.class,new AxisAlignedBB(this.pos.getX() - (1 + increase), this.pos.getY() - (1 + increase),
                    this.pos.getZ() - (1 + increase), this.pos.getX() + (2 + increase), this.pos.getY() + (1 + increase), this.pos.getZ() + (2 + increase)));

            for (EntityXPOrb orbo : orbs) {
                onEntitiesCollidWithBlock(orbo);
            }

        }
    }

    public void getItemEntitiesNearby(int rangeIncrease)
    {
        World world = this.world;
        ItemStack stack = ItemStack.EMPTY;
        int increase = rangeIncrease;
        if(!world.isRemote)
        {
            List<EntityItem> items = world.getEntitiesWithinAABB(EntityItem.class,new AxisAlignedBB(this.pos.getX() - (1 + increase), this.pos.getY() - (1 + increase),
                    this.pos.getZ() - (1 + increase), this.pos.getX() + (2 + increase), this.pos.getY() + (1 + increase), this.pos.getZ() + (2 + increase)));

            for (EntityItem item : items) {
                onEntitiesCollidWithBlock(item);
            }

        }
    }

    public void onEntitiesCollidWithBlock(Entity entityIn)
    {
        int returner = 0;
        if(entityIn instanceof EntityItem)
        {
            ItemStack incomingItem = ((EntityItem) entityIn).getItem();
            if(!hasItem())
            {
                item.insertItem(0,incomingItem,false);
                returner = incomingItem.getCount();
                if(incomingItem.getCount()-returner>0)
                {
                    incomingItem.setCount(incomingItem.getCount()-returner);
                }
                else entityIn.setDead();
            }
            else if(doItemsMatch(incomingItem))
            {
                int leftTillFilled = roomLeftInStack(this.item.getStackInSlot(0));
                if(leftTillFilled>incomingItem.getCount())
                {

                    item.insertItem(0,incomingItem,false);
                    returner = incomingItem.getCount();
                }
                else
                {
                    ItemStack copyIncoming = incomingItem.copy();
                    copyIncoming.setCount(leftTillFilled);
                    item.insertItem(0,copyIncoming,false);
                    returner = incomingItem.getCount()-leftTillFilled;
                }

                if(incomingItem.getCount()-returner>0)
                {
                    incomingItem.setCount(incomingItem.getCount()-returner);
                }
                else entityIn.setDead();
            }
        }
        else if(entityIn instanceof EntityXPOrb)
        {
            EntityXPOrb getOrb = (EntityXPOrb)entityIn;
            int valueXP = getOrb.getXpValue();
            expInPedestal += valueXP;
            getOrb.setDead();
        }
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
                System.out.println(storedOutputLocations[i]);
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
            else if(world.getBlockState(pedestalToBeLinked).getBlock().equals(BlockRegistry.pedestalstone) ||
                    world.getBlockState(this.getPos()).getBlock().equals(BlockRegistry.pedestalstone))
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

    public boolean isSamePedestal(BlockPos pedestalToBeLinked)
    {
        for(int i=0;i<storedOutputLocations.length;i++)
        {
            if(storedOutputLocations[i].equals(this.getPos()))
            {
                return true;
            }
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

    public void addStackFromBelowInvToPedestal()
    {
        if(!world.isBlockPowered(pos))
        {
            ItemStack itemFromInv = ItemStack.EMPTY;
            if(world.getTileEntity(getPosOfBlockBelow(1)) !=null)
            {
                if(world.getTileEntity(getPosOfBlockBelow(1)).hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.DOWN))
                {

                    TileEntity invToPullFrom = world.getTileEntity(getPosOfBlockBelow(1));
                    if(invToPullFrom instanceof TilePedestal) {
                        itemFromInv = ItemStack.EMPTY;

                    }
                    else {
                        for(int i =0;i<invToPullFrom.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.DOWN).getSlots();i++)
                        {
                            if(!invToPullFrom.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.DOWN).getStackInSlot(i).equals(ItemStack.EMPTY))
                            {
                                itemFromInv = invToPullFrom.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.DOWN).getStackInSlot(i);

                                if(!hasItem())
                                {
                                    ItemStack copyIncoming = itemFromInv.copy();
                                    item.insertItem(0,copyIncoming,false);
                                    itemFromInv.setCount(0);
                                }
                                else if(doItemsMatch(itemFromInv))
                                {
                                    int leftTillFilled = roomLeftInStack(this.item.getStackInSlot(0));
                                    if(leftTillFilled>itemFromInv.getCount())
                                    {
                                        item.insertItem(0,itemFromInv,false);
                                        itemFromInv.setCount(0);
                                    }
                                    else
                                    {
                                        ItemStack copyIncoming = itemFromInv.copy();
                                        copyIncoming.setCount(leftTillFilled);
                                        item.insertItem(0,copyIncoming,false);
                                        itemFromInv.shrink(leftTillFilled);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void addStackToBelowInvFromPedestal()
    {
        if(!world.isBlockPowered(pos))
        {
            ItemStack itemToInv = ItemStack.EMPTY;
            if(world.getTileEntity(getPosOfBlockBelow(1)) !=null)
            {
                if(world.getTileEntity(getPosOfBlockBelow(1)).hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.DOWN))
                {
                    TileEntity invToPushTo = world.getTileEntity(getPosOfBlockBelow(1));
                    if(invToPushTo instanceof TilePedestal) {
                        itemToInv = ItemStack.EMPTY;
                    }
                    else {
                        if(hasItem())
                        {
                            for(int i =0;i<invToPushTo.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.DOWN).getSlots();i++)
                            {
                                itemToInv = invToPushTo.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.DOWN).getStackInSlot(i);
                                if(invToPushTo.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.DOWN).getStackInSlot(i).equals(ItemStack.EMPTY))
                                {
                                    ItemStack copyPedestal = getItemInPedestal().copy();
                                    invToPushTo.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.DOWN).insertItem(i,copyPedestal,false);
                                    removeItem();
                                }
                                else
                                {
                                    if(doItemsMatch(itemToInv))
                                    {
                                        int leftTillFilled = roomLeftInStack(itemToInv);
                                        if(leftTillFilled>getItemInPedestal().getCount())
                                        {
                                            ItemStack getcountFromPedestal = getItemInPedestal().copy();
                                            invToPushTo.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.DOWN).insertItem(i,getcountFromPedestal,false);
                                            removeItem();
                                        }
                                        else
                                        {
                                            ItemStack copyIncoming = getItemInPedestal().copy();
                                            copyIncoming.setCount(leftTillFilled);
                                            invToPushTo.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.DOWN).insertItem(i,copyIncoming,false);
                                            item.getStackInSlot(0).shrink(leftTillFilled);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private int currentslot = 0;
    public void addItemToBelowInvFromPedestal()
    {
        if(!world.isBlockPowered(pos))
        {
            ItemStack itemToInv = ItemStack.EMPTY;
            if(world.getTileEntity(getPosOfBlockBelow(1)) !=null)
            {
                if(world.getTileEntity(getPosOfBlockBelow(1)).hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.DOWN))
                {
                    TileEntity invToPushTo = world.getTileEntity(getPosOfBlockBelow(1));
                    if(invToPushTo instanceof TilePedestal) {
                        itemToInv = ItemStack.EMPTY;
                    }
                    else {
                        if(hasItem())
                        {

                            //iterates through all slots in chest
                            if(currentslot<=invToPushTo.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.DOWN).getSlots())
                            {
                                itemToInv = invToPushTo.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.DOWN).getStackInSlot(currentslot);
                                //if items in slot has less then max size
                                if(invToPushTo.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.DOWN).getStackInSlot(currentslot).getCount() <
                                        invToPushTo.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.DOWN).getStackInSlot(currentslot).getMaxStackSize())
                                {
                                    //if stack is not empty (Avoid empty slots)
                                    if(!invToPushTo.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.DOWN).getStackInSlot(currentslot).equals(ItemStack.EMPTY))
                                    {
                                        //if stacks are equal
                                        if(doItemsMatch(getItemInPedestal(),invToPushTo.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.DOWN).getStackInSlot(currentslot)))
                                        {
                                            ItemStack copyPedestal = getItemInPedestal().copy();
                                            copyPedestal.setCount(1);
                                            invToPushTo.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.DOWN).insertItem(currentslot,copyPedestal,false);
                                            this.removeItem(1);
                                        }
                                    }
                                }

                                currentslot++;
                            }
                            if(currentslot>invToPushTo.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.DOWN).getSlots()-1)
                            {
                                currentslot=0;
                            }
                        }
                    }
                }
            }
        }
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

    public Boolean areFilteredItemsEqual(TilePedestal recievingPedestal, ItemStack itemStackIn, FilterTypes enumType)
    {
        BlockPos filterInv = recievingPedestal.getPosOfBlockBelow(1);
        if(world.getTileEntity(filterInv).hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.DOWN))
        {
            TileEntity tile = world.getTileEntity(filterInv);
            ItemStack stackInFilter = ItemStack.EMPTY;

            for(int i=0;i<tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.DOWN).getSlots();i++)
            {
                if(!tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.DOWN).getStackInSlot(i).equals(ItemStack.EMPTY))
                {
                    stackInFilter = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.DOWN).getStackInSlot(i);

                    if(enumType == FilterTypes.FUZZY)
                    {
                        if(itemStackIn.getItem().equals(stackInFilter.getItem()))
                        {
                            return true;
                        }
                    }
                    if(enumType == FilterTypes.MOD)
                    {
                        if(itemStackIn.getItem().getRegistryName().getResourceDomain()==stackInFilter.getItem().getRegistryName().getResourceDomain())
                        {
                            return true;
                        }
                    }
                    if(enumType == FilterTypes.NORMAL)
                    {
                        if(itemStackIn.getHasSubtypes())
                        {
                            if(itemStackIn.getItem().equals(stackInFilter.getItem()) && itemStackIn.getMetadata()==stackInFilter.getMetadata())
                            {
                                return true;
                            }
                        }
                        else if(itemStackIn.hasTagCompound())
                        {
                            NBTTagCompound itemIn = itemStackIn.getTagCompound();
                            NBTTagCompound itemStored = stackInFilter.getTagCompound();
                            if(itemIn.equals(itemStored) && itemStackIn.getItem().equals(stackInFilter.getItem()))
                            {
                                return true;
                            }
                        }
                        else
                        {
                            if(itemStackIn.getItem().equals(stackInFilter.getItem()))
                            {
                                return true;
                            }
                        }
                    }

                    else return false;
                }
            }
        }

        return false;
    }


    public int roomLeftInStack(ItemStack addToThisStack)
    {
        int maxSize = addToThisStack.getMaxStackSize();
        int currentlyStored = addToThisStack.getCount();
        int countLeftTillFilled = maxSize-currentlyStored;
        return countLeftTillFilled;
    }

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

    public boolean hasFilterableInventoryBelow()
    {
        IBlockState state = this.getWorld().getBlockState(this.getPos());
        EnumFacing enumfacing = state.getValue(FACING);

        switch (enumfacing)
        {
            case UP:
                if(isBlockUnder(0,-1,0)) {return true;}
                return false;
            case DOWN:
                if(isBlockUnder(0,1,0)) {return true;}
                return false;
            case NORTH:
                if(isBlockUnder(0,0,1)) {return true;}
                return false;
            case SOUTH:
                if(isBlockUnder(0,0,-1)) {return true;}
                return false;
            case EAST:
                if(isBlockUnder(-1,0,0)) {return true;}
                return false;
            case WEST:
                if(isBlockUnder(1,0,0)) {return true;}
                return false;
        }
        return false;
    }

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

    private void tryToSendItemToPedestal()
    {
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
    }

    public void getStoredBlockPoss()
    {
        for(int i=0;i<storedOutputLocations.length;i++)
        {
            System.out.println(storedOutputLocations[i]);
        }
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


    @Override
    public void update() {

        getRedstoneSignalIn();
        if(!world.isRemote)
        {
            if(hasEffectUpgrade())
            {
                if(getEffectFromUpgrade().getPotion().equals(PotionRegistry.POTION_MAGNETISM))
                {
                    getItemEntitiesNearby(getUpgradePotency());
                }

            }
            if(hasCoin())
            {
                if(getCoinOnPedestal().getItem().equals(ItemRegistry.enchantUpgrade))
                {
                    if(getEffectFromUpgrade().getPotion().equals(PotionRegistry.POTION_MAGNETISM))
                    {
                        getXPOrbEntitiesNearby(getUpgradePotency());
                    }
                    else getXPOrbEntitiesNearby(0);
                }
            }

            if(this.hasUpgrade(ItemRegistry.singleExportUpgrade))
            {
                addItemToBelowInvFromPedestal();
            }

            ticker4++;
            if(ticker4>10)
            {
                if(this.hasCoin())
                {
                    if(this.hasUpgrade(ItemRegistry.dropperUpgrade))
                    {
                        summonItemFromInventory();
                    }

                    if(this.hasUpgrade(ItemRegistry.ancientCoin))
                    {
                        crafter(3);
                    }
                }
                ticker4=0;
            }

            ticker3++;
            if(ticker3>20)
            {


                if(this.hasCoin())
                {
                    tryToSendItemToPedestal();

                    if(this.hasUpgrade(ItemRegistry.breakerUpgrade))
                    {
                        if(hasItem())
                        {
                            if(getDropsFromBlock().getItem().equals(getItemInPedestal().getItem()))
                            {
                                if(getItemInPedestal().getCount() + getDropsFromBlock().getCount() <= getMaxStackSize())
                                {
                                    addItem(getDropsFromBlock());
                                    world.setBlockToAir(getPosOfBlockBelow(1));
                                }
                            }

                        }
                        else
                        {
                            addItem(getDropsFromBlock());
                            world.setBlockToAir(getPosOfBlockBelow(1));
                        }
                    }

                    if(this.hasUpgrade(ItemRegistry.placerUpgrade))
                    {
                        placeBlockFromInventory();
                    }

                    if(this.hasUpgrade(ItemRegistry.importUpgrade))
                    {
                        addStackFromBelowInvToPedestal();
                    }

                    if(this.hasUpgrade(ItemRegistry.exportUpgrade))
                    {
                        addStackToBelowInvFromPedestal();
                    }

                    if(this.hasUpgrade(ItemRegistry.enchantUpgrade))
                    {
                        useExpToBottleEnchantRepair();
                    }
                }
                else
                {
                    if(!world.isBlockPowered(pos))
                    {
                        tryToSendItemToPedestal();
                    }
                }


                ticker3=0;
            }
        }



        IBlockState state = this.getWorld().getBlockState(this.getPos());
        EnumFacing enumfacing = state.getValue(FACING);
        if(!world.isRemote)
        {
            if(!getItemInPedestal().isEmpty())
            {
                if(getItemInPedestal().getCount()!=getMaxStackSize())
                {
                    ticker2++;
                    if(ticker2>=5)
                    {
                        display = item.getStackInSlot(0);
                        updateBlock();
                        ticker2=0;
                    }
                }
            }
            else
            {
                ticker2++;
                if(ticker2>=20)
                {
                    display = ItemStack.EMPTY;
                    updateBlock();
                    ticker2=0;
                }
            }
        }
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

    public ItemStack getNextSlotInChest(int x,int y,int z)
    {
        ItemStack stack = ItemStack.EMPTY;
        if(world.getTileEntity(pos.add(x,y,z)).hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.DOWN))
        {
            TileEntity tileEntity = world.getTileEntity(pos.add(x,y,z));
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