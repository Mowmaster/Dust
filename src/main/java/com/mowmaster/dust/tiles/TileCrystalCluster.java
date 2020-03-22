package com.mowmaster.dust.tiles;

import com.mowmaster.dust.blocks.BlockCrystalClusterTE;
import com.mowmaster.dust.blocks.BlockPedestalTE;
import com.mowmaster.dust.item.ItemColorDust;
import com.mowmaster.dust.item.ItemSpellScroll;
import com.mowmaster.dust.item.pedestalUpgrades.ItemUpgradeBase;
import com.mowmaster.dust.item.pedestalUpgrades.ItemUpgradeBaseFilter;
import com.mowmaster.dust.references.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
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
import net.minecraft.world.World;
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
import java.util.Random;

import static com.mowmaster.dust.references.Reference.MODID;


public class TileCrystalCluster extends TileEntity implements ITickableTileEntity {

    private LazyOptional<IItemHandler> handler = LazyOptional.of(this::createHandler);

    private static final int[] SLOTS_ALLSIDES = new int[] {0};

    private int storedValueForUpgrades = 0;
    private boolean boolLight = false;

    public TileCrystalCluster()
    {
        super(CLUSTERTYPE);
    }

    private void update()
    {
        markDirty();
        world.notifyBlockUpdate(pos,getBlockState(),getBlockState(),1);
        world.notifyBlockUpdate(pos,getBlockState(),getBlockState(),2);
    }

    private IItemHandler createHandler() {
        return new ItemStackHandler(2) {
            @Override
            protected void onContentsChanged(int slot) {
                update();
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                if (slot == 0) return true;
                if (slot == 1 && stack.getItem() instanceof ItemUpgradeBase) return true;
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

    public boolean hasItem()
    {
        IItemHandler h = handler.orElse(null);
        if(h.getStackInSlot(0).isEmpty())
        {
            return false;
        }
        else  return true;
    }

    public boolean hasLight()
    {
        return boolLight;
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

    public ItemStack removeItem(int numToRemove) {
        IItemHandler h = handler.orElse(null);
        ItemStack stack = h.extractItem(0,numToRemove,false);
        update();

        return stack;
    }

    public ItemStack removeItem() {
        IItemHandler h = handler.orElse(null);
        ItemStack stack = h.extractItem(0,h.getStackInSlot(0).getCount(),false);
        update();

        return stack;
    }

    public int getMaxStackSize(){return 1;}

    public boolean addItem(ItemStack itemFromBlock)
    {
        IItemHandler h = handler.orElse(null);
        if(hasItem())
        {

        }
        else {h.insertItem(0, itemFromBlock.copy(), false);}
        update();

        return true;
    }

    public boolean addLight()
    {
        if(hasLight())
        {
            return false;
        }
        else
        {
            boolLight = true;
            BlockState state = world.getBlockState(pos);
            boolean watered = state.get(BlockPedestalTE.WATERLOGGED);
            Direction dir = state.get(BlockPedestalTE.FACING);
            BlockState newstate = state.with(BlockPedestalTE.FACING,dir).with(BlockPedestalTE.WATERLOGGED,watered).with(BlockPedestalTE.LIT,true);
            world.notifyBlockUpdate(pos,state,newstate,3);
            world.setBlockState(pos,newstate,3);
            //world.markBlockRangeForRenderUpdate(pos,state,newstate);
            update();
            return true;
        }
    }

    //Returns items available to be insert, 0 if false
    public int canAcceptItems(ItemStack itemsIncoming)
    {
        int canAccept = 0;



        return canAccept;
    }

    public boolean hasFilter(TileCrystalCluster pedestalSendingTo)
    {
        boolean returner = false;

        return returner;
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

    int impTicker = 0;
    int pedTicker = 0;
    @Override
    public void tick() {

        if(!world.isRemote)
        {
            if(world.isAreaLoaded(pos,1))
            {

            }
        }
    }

    @Override
    public void read(CompoundNBT tag) {
        CompoundNBT invTag = tag.getCompound("inv");
        handler.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(invTag));

        this.storedValueForUpgrades = tag.getInt("storedUpgradeValue");

        super.read(tag);
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        handler.ifPresent(h -> {
            CompoundNBT compound = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
            tag.put("inv", compound);
        });

        tag.putInt("storedUpgradeValue",storedValueForUpgrades);

        return super.write(tag);
    }

    @Override
    @Nullable
    public SUpdateTileEntityPacket getUpdatePacket()
    {
        CompoundNBT nbtTagCompound = new CompoundNBT();
        write(nbtTagCompound);
        int tileEntityType = 42;  // arbitrary number; only used for vanilla TileEntities.  You can use it, or not, as you want.
        return new SUpdateTileEntityPacket(this.pos, tileEntityType, nbtTagCompound);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        read(pkt.getNbtCompound());
    }

    /* Creates a tag containing the TileEntity information, used by vanilla to transmit from server to client
 */
    @Override
    public CompoundNBT getUpdateTag()
    {
        CompoundNBT nbtTagCompound = new CompoundNBT();
        write(nbtTagCompound);
        return nbtTagCompound;
    }

    /* Populates this TileEntity with information from the tag, used by vanilla to transmit from server to client
 */
    @Override
    public void handleUpdateTag(CompoundNBT tag)
    {
        this.read(tag);
    }

    private static Block[] cluArray = new Block[]{BlockCrystalClusterTE.B_CLUSTER};

    private static final ResourceLocation R_CLUSTER = new ResourceLocation(MODID, "tile/crystalcluster");

    public static TileEntityType<TileCrystalCluster> CLUSTERTYPE = TileEntityType.Builder.create(TileCrystalCluster::new, cluArray).build(null);

    @SubscribeEvent
    public static void onTileEntityRegistry(final RegistryEvent.Register<TileEntityType<?>> event) {
        IForgeRegistry<TileEntityType<?>> r = event.getRegistry();
        r.register(CLUSTERTYPE.setRegistryName(R_CLUSTER));
    }
}