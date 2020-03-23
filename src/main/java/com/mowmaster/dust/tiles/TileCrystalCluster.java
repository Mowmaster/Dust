package com.mowmaster.dust.tiles;

import com.mowmaster.dust.blocks.BlockCrystalClusterTE;
import com.mowmaster.dust.blocks.BlockPedestalTE;
import com.mowmaster.dust.item.ItemColorCrystal;
import com.mowmaster.dust.item.ItemColorDust;
import com.mowmaster.dust.item.ItemSpellScroll;
import com.mowmaster.dust.item.pedestalUpgrades.ItemUpgradeBase;
import com.mowmaster.dust.item.pedestalUpgrades.ItemUpgradeBaseFilter;
import com.mowmaster.dust.references.Reference;
import net.minecraft.block.AirBlock;
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
        return new ItemStackHandler(9) {
            @Override
            protected void onContentsChanged(int slot) {
                update();
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                if(stack.getItem() instanceof ItemColorCrystal)
                {
                    if (slot == 0 && getStackInSlot(0).isEmpty()){
                        return true;
                    }
                    else if (slot == 1 && getStackInSlot(1).isEmpty()){
                        return true;
                    }
                    else if (slot == 2 && getStackInSlot(2).isEmpty()){
                        return true;
                    }
                    else if (slot == 3 && getStackInSlot(3).isEmpty()){
                        return true;
                    }
                    else if (slot == 4 && getStackInSlot(4).isEmpty()){
                        return true;
                    }
                    else if (slot == 5 && getStackInSlot(5).isEmpty()){
                        return true;
                    }
                    else if (slot == 6 && getStackInSlot(6).isEmpty()){
                        return true;
                    }
                    else if (slot == 7 && getStackInSlot(7).isEmpty()){
                        return true;
                    }
                    else if (slot == 8 && getStackInSlot(8).isEmpty()){
                        return true;
                    }
                }

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

    public boolean hasItem(int slot)
    {
        IItemHandler h = handler.orElse(null);
        if(h.getStackInSlot(slot).isEmpty())
        {
            return false;
        }
        else  return true;
    }

    public boolean hasLight()
    {
        return boolLight;
    }


    public ItemStack getItemInPedestal(int slot)
    {
        IItemHandler h = handler.orElse(null);
        if(hasItem(slot))
        {
            return h.getStackInSlot(slot);
        }
        else return ItemStack.EMPTY;

    }

    public ItemStack removeItem(int slot) {
        IItemHandler h = handler.orElse(null);
        ItemStack stack = h.extractItem(slot,h.getStackInSlot(slot).getCount(),false);
        update();

        return stack;
    }

    public int getMaxStackSize(){return 1;}

    public boolean addItem(int slot, ItemStack itemFromBlock)
    {
        IItemHandler h = handler.orElse(null);
        if(hasItem(slot))
        {

        }
        else {h.insertItem(slot, itemFromBlock.copy(), false);}
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
            boolean watered = state.get(BlockCrystalClusterTE.WATERLOGGED);
            Direction dir = state.get(BlockCrystalClusterTE.FACING);
            BlockState newstate = state.with(BlockCrystalClusterTE.FACING,dir).with(BlockCrystalClusterTE.WATERLOGGED,watered).with(BlockCrystalClusterTE.LIT,true);
            world.notifyBlockUpdate(pos,state,newstate,3);
            world.setBlockState(pos,newstate,3);
            update();
            return true;
        }
    }

    public boolean hasEmptySlot()
    {
        IItemHandler h = handler.orElse(null);
        int slots = h.getSlots();
        int i = 0;
        int count = 0;
        for(i=0;i<slots;i++)
        {
            if(hasItem(i)) count++;
        }

        return (count==9)?(false):(true);
    }

    public int getNextEmptySlot()
    {
        IItemHandler h = handler.orElse(null);
        int slots = h.getSlots();
        int i = -1;
        if(hasEmptySlot())
        {
            for(i=0;i<slots;i++)
            {
                if(!hasItem(i)) break;
            }
        }

        return i;
    }

    public boolean isBlockUnder(int x,int y,int z)
    {
        return (world.getBlockState(pos.add(x,y,z)).getBlock() instanceof AirBlock)?(false):(true);
    }

    public BlockState getBlockUnder(int x,int y,int z)
    {
        return (isBlockUnder(x,y,z))?(world.getBlockState(pos.add(x,y,z))):(null);
    }

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

        //this.storedValueForUpgrades = tag.getInt("storedUpgradeValue");

        super.read(tag);
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        handler.ifPresent(h -> {
            CompoundNBT compound = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
            tag.put("inv", compound);
        });

        //tag.putInt("storedUpgradeValue",storedValueForUpgrades);

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