package com.mowmaster.dust.tiles;

import com.mowmaster.dust.blocks.BlockPedestal;
import com.mowmaster.dust.blocks.BlockTest;
import com.mowmaster.dust.item.pedestalUpgrades.UpgradeBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

import static com.mowmaster.dust.references.Reference.MODID;


public class TileTest extends TileEntity implements ITickableTileEntity {

    private LazyOptional<IItemHandler> handler = LazyOptional.of(this::createHandler);

    private static final int[] SLOTS_ALLSIDES = new int[] {0};

    private int storedValueForUpgrades = 0;
    private int intTransferAmount = 4;
    private int intTransferSpeed = 20;
    private int intTransferRange = 8;
    private final List<BlockPos> storedLocations = new ArrayList<BlockPos>();

    public TileTest()
    {
        super(TEST);
    }

    private IItemHandler createHandler() {
        return new ItemStackHandler(2) {
            @Override
            protected void onContentsChanged(int slot) {
                markDirty();
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                if (slot == 0) return true;
                if (slot == 1 && stack.getItem() instanceof UpgradeBase) return true;
                return false;
            }
        };
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return handler.cast();
        }
        return super.getCapability(cap, side);
    }

    public int getNumberOfStoredLocations() {return storedLocations.size();}
    public boolean storeNewLocation(BlockPos pos)
    {
        boolean returner = false;
        if(getNumberOfStoredLocations() < 8)
        {
            storedLocations.add(pos);
            returner=true;
        }

        return returner;
    }






    public int getStoredValueForUpgrades()
    {
        return storedValueForUpgrades;
    }
    public void setStoredValueForUpgrades(int value)
    {
        storedValueForUpgrades = value;
    }
    public int getPedestalTransferAmount(){return intTransferAmount;}
    public void setPedestalTransferAmount(int transferAmount){intTransferAmount = transferAmount;}
    public int getPedestalTransferSpeed(){return intTransferSpeed;}
    public void setPedestalTransferSpeed(int transferSpeed){intTransferSpeed = transferSpeed;}
    public int getPedestalTransferRange(){return intTransferRange;}
    public void setPedestalTransferRange(int transferSpeed){intTransferRange = transferSpeed;}
    public int spaceInPedestal()
    {
        int space = 0;

        if(getItemInPedestal().isEmpty() || getItemInPedestal().equals(ItemStack.EMPTY))
        {
            space = 64;
        }
        else
        {
            space = (getMaxStackSize() - getItemInPedestal().getCount());
        }

        return space;
    }

    public boolean hasItem()
    {
        IItemHandler h = handler.orElse(null);
        if(h.getStackInSlot(0).isEmpty())
        {
            return false;
        }
        else  return true;
    }
    public boolean hasCoin()
    {
        IItemHandler h = handler.orElse(null);
        if(h.getStackInSlot(1).isEmpty())
        {
            return false;
        }
        else  return true;
    }

    public ItemStack getItemInPedestal()
    {
        IItemHandler h = handler.orElse(null);
        if(hasItem())
        {
            return h.getStackInSlot(0);
        }
        else return ItemStack.EMPTY;

    }
    public ItemStack getCoinOnPedestal()
    {
        IItemHandler h = handler.orElse(null);
        if(hasCoin())
        {
            return h.getStackInSlot(1);
        }
        else return ItemStack.EMPTY;
    }


    /**
     * world.notifyBlockUpdate(this.pos,this.getBlockState(),this.getBlockState(),1);
     * Sets a block state into this world.Flags are as follows:
     * 1 will cause a block update.
     * 2 will send the change to clients.
     * 4 will prevent the block from being re-rendered.
     * 8 will force any re-renders to run on the main thread instead
     * 16 will prevent neighbor reactions (e.g. fences connecting, observers pulsing).
     * 32 will prevent neighbor reactions from spawning drops.
     * 64 will signify the block is being moved.
     * Flags can be OR-ed
     */
    public ItemStack removeItem(int numToRemove) {
        IItemHandler h = handler.orElse(null);
        ItemStack stack = h.extractItem(0,numToRemove,false);
        world.notifyBlockUpdate(this.pos,this.getBlockState(),this.getBlockState(),1);
        this.markDirty();
        return stack;
    }

    public ItemStack removeItem() {
        IItemHandler h = handler.orElse(null);
        ItemStack stack = h.extractItem(0,h.getStackInSlot(0).getCount(),false);
        world.notifyBlockUpdate(this.pos,this.getBlockState(),this.getBlockState(),1);
        this.markDirty();
        return stack;
    }

    public ItemStack removeCoin() {
        IItemHandler h = handler.orElse(null);
        ItemStack stack = h.extractItem(1,h.getStackInSlot(1).getCount(),false);
        setStoredValueForUpgrades(0);
        world.notifyBlockUpdate(this.pos,this.getBlockState(),this.getBlockState(),1);
        this.markDirty();
        return stack;
    }

    

    public int getMaxStackSize(){return 64;}

    public boolean addItem(ItemStack itemFromBlock)
    {
        IItemHandler h = handler.orElse(null);
        if(hasItem())
        {
            if(doItemsMatch(itemFromBlock))
            {
                h.insertItem(0, itemFromBlock.copy(), false);
            }
        }
        else {h.insertItem(0, itemFromBlock.copy(), false);}
        world.notifyBlockUpdate(this.pos,this.getBlockState(),this.getBlockState(),1);
        this.markDirty();
        return true;
    }

    public boolean addCoin(ItemStack coinFromBlock)
    {
        IItemHandler h = handler.orElse(null);
        ItemStack itemFromBlock = coinFromBlock.copy();
        itemFromBlock.setCount(1);
        if(hasCoin()){} else h.insertItem(1,itemFromBlock,false);
        setStoredValueForUpgrades(0);
        world.notifyBlockUpdate(this.pos,this.getBlockState(),this.getBlockState(),1);
        this.markDirty();
        return true;
    }

    public boolean doItemsMatch(ItemStack itemStackIn)
    {
        IItemHandler h = handler.orElse(null);
        if(hasItem())
        {
            if(itemStackIn.hasTag())
            {
                CompoundNBT itemIn = itemStackIn.getTag();
                CompoundNBT itemStored = h.getStackInSlot(0).getTag();
                if(itemIn.equals(itemStored) && itemStackIn.getItem().equals(h.getStackInSlot(0).getItem()))
                {
                    return true;
                }
                else return false;
            }
            else
            {
                if(itemStackIn.getItem().equals(h.getStackInSlot(0).getItem()))
                {
                    return true;
                }
            }
        }
        else{return true;}

        return false;
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

    //Checks when linking pedestals if the two being linked are 1.on the same network 2. if one is neutral thus meaning they can link
    public boolean canLinkToPedestalNetwork(BlockPos pedestalToBeLinked)
    {
        //Check to see if pedestal to be linked is a block pedestal
        if(world.getBlockState(pedestalToBeLinked).getBlock() instanceof BlockPedestal)
        {
            return true;
            /*//checks to see if pedestal to be linked and this one are the same
            if(world.getBlockState(pedestalToBeLinked).getBlock().equals(world.getBlockState(this.getPos()).getBlock()))
            {
                return true;
            }
            else if(world.getBlockState(this.getPos()).getBlock().equals(BlockPedestal.pedestalred) && world.getBlockState(pedestalToBeLinked).getBlock().equals(BlockPedestal.pedestalredunlit)){return true;}
            else if(world.getBlockState(this.getPos()).getBlock().equals(BlockPedestal.pedestalredunlit) && world.getBlockState(pedestalToBeLinked).getBlock().equals(BlockPedestal.pedestalred)){return true;}
            else if(world.getBlockState(this.getPos()).getBlock().equals(BlockPedestal.pedestalblue) && world.getBlockState(pedestalToBeLinked).getBlock().equals(BlockPedestal.pedestalblueunlit)){return true;}
            else if(world.getBlockState(this.getPos()).getBlock().equals(BlockPedestal.pedestalblueunlit) && world.getBlockState(pedestalToBeLinked).getBlock().equals(BlockPedestal.pedestalblue)){return true;}
            else if(world.getBlockState(this.getPos()).getBlock().equals(BlockPedestal.pedestalyellow) && world.getBlockState(pedestalToBeLinked).getBlock().equals(BlockPedestal.pedestalyellowunlit)){return true;}
            else if(world.getBlockState(this.getPos()).getBlock().equals(BlockPedestal.pedestalyellowunlit) && world.getBlockState(pedestalToBeLinked).getBlock().equals(BlockPedestal.pedestalyellow)){return true;}
            else if(world.getBlockState(this.getPos()).getBlock().equals(BlockPedestal.pedestalpurple) && world.getBlockState(pedestalToBeLinked).getBlock().equals(BlockPedestal.pedestalpurpleunlit)){return true;}
            else if (world.getBlockState(this.getPos()).getBlock().equals(BlockPedestal.pedestalpurpleunlit) && world.getBlockState(pedestalToBeLinked).getBlock().equals(BlockPedestal.pedestalpurple)){return true;}
            else if(world.getBlockState(this.getPos()).getBlock().equals(BlockPedestal.pedestalgreen) && world.getBlockState(pedestalToBeLinked).getBlock().equals(BlockPedestal.pedestalgreenunlit)){return true;}
            else if (world.getBlockState(this.getPos()).getBlock().equals(BlockPedestal.pedestalgreenunlit) && world.getBlockState(pedestalToBeLinked).getBlock().equals(BlockPedestal.pedestalgreen)){return true;}
            else if(world.getBlockState(this.getPos()).getBlock().equals(BlockPedestal.pedestalorange) && world.getBlockState(pedestalToBeLinked).getBlock().equals(BlockPedestal.pedestalorangeunlit)){return true;}
            else if (world.getBlockState(this.getPos()).getBlock().equals(BlockPedestal.pedestalorangeunlit) && world.getBlockState(pedestalToBeLinked).getBlock().equals(BlockPedestal.pedestalorange)){return true;}
            else if(world.getBlockState(this.getPos()).getBlock().equals(BlockPedestal.pedestalwhite) && world.getBlockState(pedestalToBeLinked).getBlock().equals(BlockPedestal.pedestalwhiteunlit)){return true;}
            else if (world.getBlockState(this.getPos()).getBlock().equals(BlockPedestal.pedestalwhiteunlit) && world.getBlockState(pedestalToBeLinked).getBlock().equals(BlockPedestal.pedestalwhite)){return true;}
            else if(world.getBlockState(this.getPos()).getBlock().equals(BlockPedestal.pedestalblack) && world.getBlockState(pedestalToBeLinked).getBlock().equals(BlockPedestal.pedestalblackunlit)){return true;}
            else if (world.getBlockState(this.getPos()).getBlock().equals(BlockPedestal.pedestalblackunlit) && world.getBlockState(pedestalToBeLinked).getBlock().equals(BlockPedestal.pedestalblack)){return true;}
            //if they arnt then check if one is neutral
            else if(world.getBlockState(this.getPos()).getBlock().equals(BlockPedestal.pedestalstone) || world.getBlockState(this.getPos()).getBlock().equals(BlockPedestal.pedestalstoneunlit)
                    || world.getBlockState(pedestalToBeLinked).getBlock().equals(BlockPedestal.pedestalstone) || world.getBlockState(pedestalToBeLinked).getBlock().equals(BlockPedestal.pedestalstoneunlit))
            {
                return true;
            }
            else return false;*/
        }

        return false;
    }

    //Returns items available to be insert, 0 if false
    public int canAcceptItems(ItemStack itemsIncoming)
    {
        int canAccept = 0;

        if(getItemInPedestal().isEmpty() || getItemInPedestal().equals(ItemStack.EMPTY))
        {
            canAccept = 64;
        }
        else
        {
            if(doItemsMatch(itemsIncoming))
            {
                //Two buckets match but cant be stacked since max stack size is 1
                if(itemsIncoming.getMaxStackSize() > 1)
                {
                    if(getItemInPedestal().getCount() < getMaxStackSize())
                    {
                        canAccept = (getMaxStackSize() - getItemInPedestal().getCount());
                    }
                }
            }
        }

        return canAccept;
    }









    @Override
    public void tick() {


    }

    @Override
    public void read(CompoundNBT tag) {
        CompoundNBT invTag = tag.getCompound("inv");
        handler.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(invTag));

        this.storedValueForUpgrades = tag.getInt("storedUpgradeValue");
        this.intTransferAmount = tag.getInt("intTransferAmount");
        this.intTransferSpeed = tag.getInt("intTransferSpeed");
        this.intTransferRange = tag.getInt("intTransferRange");

        int counter = tag.getInt("storedBlockPosCounter");
        for(int i=0;i<counter;i++)
        {
            String getKeyNameX = "storedLocationX" + i;
            String getKeyNameY = "storedLocationY" + i;
            String getKeyNameZ = "storedLocationZ" + i;
            int getX = tag.getInt(getKeyNameX);
            int getY = tag.getInt(getKeyNameY);
            int getZ = tag.getInt(getKeyNameZ);
            BlockPos gotPos = new BlockPos(getX,getY,getZ);
            storeNewLocation(gotPos);
        }
        //CompoundNBT itemTagD = compound.getCompound("display");
        //this.display = new ItemStack(itemTagD);
        super.read(tag);
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        handler.ifPresent(h -> {
            CompoundNBT compound = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
            tag.put("inv", compound);
        });

        tag.putInt("storedUpgradeValue",storedValueForUpgrades);
        tag.putInt("intTransferAmount",intTransferAmount);
        tag.putInt("intTransferSpeed",intTransferSpeed);
        tag.putInt("intTransferRange",intTransferRange);
        int counter = 0;
        for(int i=0;i<getNumberOfStoredLocations();i++)
        {
            String keyNameX = "storedLocationX" + i;
            String keyNameY = "storedLocationY" + i;
            String keyNameZ = "storedLocationZ" + i;
            tag.putInt(keyNameX,storedLocations.get(i).getX());
            tag.putInt(keyNameY,storedLocations.get(i).getY());
            tag.putInt(keyNameZ,storedLocations.get(i).getZ());
            counter++;
        }
        tag.putInt("storedBlockPosCounter",counter);
        //compound.put("display",display.write(new CompoundNBT()));
        return super.write(tag);
    }

    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(pos, -1, getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager manager, SUpdateTileEntityPacket packet) {
        read(packet.getNbtCompound());
    }

    private static final ResourceLocation RESLOC_TILE_TEST = new ResourceLocation(MODID, "tile/test");

    public static TileEntityType<TileTest> TEST = TileEntityType.Builder.create(TileTest::new, BlockTest.BLOCK_TEST).build(null);

    @SubscribeEvent
    public static void onTileEntityRegistry(final RegistryEvent.Register<TileEntityType<?>> event) {
        IForgeRegistry<TileEntityType<?>> r = event.getRegistry();
        r.register(TEST.setRegistryName(RESLOC_TILE_TEST));
    }
}
