package com.mowmaster.dust.Block.BlockEntities.Tier1.Furnaces;

import com.mowmaster.dust.Block.BlockEntities.Tier1.Tier1BaseBlock;
import com.mowmaster.dust.Block.BlockEntities.Tier1.Tier1BaseBlockEntity;
import com.mowmaster.dust.Capabilities.Dust.DustMagic;
import com.mowmaster.dust.Configs.DustConfig;
import com.mowmaster.dust.DeferredRegistery.DeferredBlockEntityTypes;
import com.mowmaster.dust.DeferredRegistery.DeferredRegisterTileBlocks;
import com.mowmaster.dust.Networking.DustPacketHandler;
import com.mowmaster.dust.Networking.DustPacketParticles;
import com.mowmaster.dust.References.ColorReference;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlastFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.FurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class DustFurnacesBaseBlockEntity extends Tier1BaseBlockEntity
{
    private LazyOptional<IItemHandler> allowedInputsHandler = LazyOptional.of(this::createAllowedInputsHandler);
    private List<ItemStack> stacksListAllowedInputsHandler = new ArrayList<>();
    private boolean isLit = false;

    public DustFurnacesBaseBlockEntity(BlockEntityType<?> p_155228_, BlockPos p_155229_, BlockState p_155230_) {
        super(p_155228_, p_155229_, p_155230_);
    }

    /*============================================================================
    ==============================================================================
    =====================   BASE CLASS OVERRIDES - START   =======================
    ==============================================================================
    ============================================================================*/

    @Override
    public void update()
    {
        BlockState state = level.getBlockState(getPos());
        this.level.sendBlockUpdated(getPos(), state, state, 3);
        this.setChanged();
    }

    /*============================================================================
    ==============================================================================
    =====================    BASE CLASS OVERRIDES - END    =======================
    ==============================================================================
    ============================================================================*/

    public RecipeType getRecipeTypeForBlock()
    {
        return RecipeType.SMELTING;
    }


    private IItemHandler createAllowedInputsHandler() {
        /*
         * CONFIG IS USED TO SET THIS, USERS MUST DEFINE RECIPES AND CHANGE CONFIG TO MAKE CHANGES TO MACHINES NOW
         */
        int slots = 2;
        return new ItemStackHandler(slots) {

            @Override
            protected void onLoad() {

                if(getSlots()<slots)
                {
                    for(int i = 0; i < getSlots(); ++i) {
                        stacksListAllowedInputsHandler.add(i,getStackInSlot(i));
                    }
                    setSize(getRepairListStacks().size());
                    for(int j = 0;j<stacksListAllowedInputsHandler.size();j++) {
                        setStackInSlot(j, stacksListAllowedInputsHandler.get(j));
                    }
                }

                super.onLoad();
            }

            @Override
            protected void onContentsChanged(int slot) {
                if(!(stacksListAllowedInputsHandler.size()>0))
                {
                    update();
                }
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                if(slot==0 && !stack.getItem().equals(Items.LAVA_BUCKET) && ForgeHooks.getBurnTime(stack, getRecipeTypeForBlock())>0)return true;
                else if(slot==1 && isAllowedInputItemForMachineSlot(stack))return true;
                else return false;
            }
        };
    }

    public boolean isAllowedInputItemForMachineSlot(ItemStack stack)
    {
        return false;
    }







    public boolean isFurnaceLit()
    {
        return this.isLit;
    }


    @Override
    public void load(CompoundTag p_155245_) {
        super.load(p_155245_);

        this.isLit = p_155245_.getBoolean("isLit");
    }

    @Override
    public CompoundTag save(CompoundTag p_58888_) {
        super.save(p_58888_);
        p_58888_.putBoolean("isLit",isLit);

        return p_58888_;
    }
}
