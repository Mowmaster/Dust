package com.mowmaster.dust.Features.Pedestals;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import org.jspecify.annotations.Nullable;

public class EntityBlockPedestal extends BlockEntity {

    public EntityBlockPedestal(BlockPos worldPosition, BlockState blockState) {
        super(RegistryPedestalEntity.PEDESTAL_BASE.get(), worldPosition, blockState);
    }

    public final ItemStacksResourceHandler inventory = new ItemStacksResourceHandler(1)
    {
        @Override
        protected void onContentsChanged(int index, ItemStack previousContents) {
            super.onContentsChanged(index, previousContents);
            EntityBlockPedestal.this.setChanged();
            if(!level.isClientSide())
            {
                level.sendBlockUpdated(getBlockPos(),getBlockState(),getBlockState(),3);
            }
        }

        @Override
        protected int getCapacity(int index, ItemResource resource) {
            return Item.DEFAULT_MAX_STACK_SIZE;
        }

    };

    public void dropInventoryItems() {
        SimpleContainer inv = new SimpleContainer(inventory.size());
        for(int i = 0; i < inventory.size(); i++)
        {
            ItemAccess itemAccess = ItemAccess.forHandlerIndex(inventory, 0);
            inv.setItem(i, new ItemStack(itemAccess.getResource().getItem(), itemAccess.getAmount()));
        }

        Containers.dropContents(this.level, this.worldPosition, inv);
    }

    public boolean isEmpty()
    {
        return inventory.getResource(0).isEmpty();
    }

    public ItemStack getStackInPedestal()
    {
        return inventory.getResource(0).toStack();
    }

    public ItemStack removeSingleFromPedestal()
    {
        inventory.set(0,inventory.getResource(0),getStackInPedestal().count()-1);
        return getStackInPedestal().copyWithCount(1);
    }

    public ItemStack removeStackFromPedestal()
    {
        inventory.set(0,inventory.getResource(0),0);
        return getStackInPedestal();
    }

    private boolean canInsertStack(ItemStack inputStack)
    {
        if(isEmpty())return true;
        else if(inventory.getResource(0).is(inputStack.getItem()))
        {
            return true;
        }

        return false;
    }

    public int spaceInPedestal()
    {
        return inventory.getResource(0).getMaxStackSize() - inventory.getResource(0).toStack().count();
    }

    public int insertStackToPedestal(ItemStack inputStack)
    {
        if(canInsertStack(inputStack) && spaceInPedestal() > 0)
        {
            int currentStackSize = (isEmpty())?(0):(getStackInPedestal().count());
            int insertCount = (spaceInPedestal()>inputStack.count())?(inputStack.count()):(spaceInPedestal());
            inventory.set(0,ItemResource.of(inputStack),currentStackSize + insertCount);
            return insertCount;
        }
        return 0;
    }


    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putChild("inventory", inventory);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        input.child("inventory").ifPresent(inventory::deserialize);
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }
}
