package com.mowmaster.dust.tiles;

import com.mowmaster.dust.blocks.BlockCrystalFurnace;
import com.mowmaster.dust.items.ItemCrystal;
import com.mowmaster.dust.items.ItemRegistry;
import com.mowmaster.dust.tiles.containers.ContainerCrystalFurnace;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

import java.util.Random;


public class TileCrystalFurnace extends TileEntityLockable implements ITickable,ISidedInventory, ICapabilityProvider
{


    private ItemStackHandler handler;
    private static final int[] SLOTS_TOP = new int[] {0};
    private static final int[] SLOTS_BOTTOM = new int[] {3, 4};
    private static final int[] SLOTS_SIDES = new int[] {1, 2};
    public static final PropertyDirection FACING = BlockHorizontal.FACING;


    //private NonNullList<ItemStack> inventory = NonNullList.<ItemStack>withSize(5,ItemStack.EMPTY);//Input, Input Crystal, Input Fuel, Output, Output Spent Crystals
    private String customName;

    private int burnTime;
    private int currentBurnTime;
    private int cookTime;
    private int totalCookTime;
    private int crystalEnergyLeft;
    private int customCookTime;
    private int crystalEffectActive=-1;
    private int randomPotencyChance = 10;
    private int modifier = 1;//Biggest mod is 4

    public TileCrystalFurnace()
    {
        this.handler = new ItemStackHandler(5);
    }

    public ItemStack getFromInv(int num)
    {
        return this.handler.getStackInSlot(num);
    }

    @Override
    public String getName() {
        return this.hasCustomName() ? this.customName : "container.crystalfurnace";//If it has a custom name name it this.customName otherwise set it to container.crystalfurnace
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }



    @Nullable
    @Override
    public ITextComponent getDisplayName() {
        return this.hasCustomName() ? new TextComponentString(this.getName()) : new TextComponentString(this.getName());
    }

    @Override
    public boolean isEmpty() {
        for(int i=0;i<=this.handler.getSlots();i++)
        {
            if(!this.handler.getStackInSlot(i).isEmpty())
                return false;
        }
        return true;
    }

    @Override
    public int getSizeInventory() {
        return this.handler.getSlots();//from the non null inventory above == 5
    }



    @Override
    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
        return new ContainerCrystalFurnace(playerInventory,this);
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return this.handler.getStackInSlot(index);//gets one of our 5 slots like input=0 input crystal=1 input fuel=2 output=3 for example
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        return this.handler.extractItem(index,count,false);//no idea what true and false do here
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        return null;
    }

    public void setInventorySlotContents(int index, ItemStack stack)
    {
        ItemStack itemstack = this.handler.getStackInSlot(index);
        boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack) && ItemStack.areItemStackTagsEqual(stack, itemstack);
        this.handler.setStackInSlot(index,stack);

        if (stack.getCount() > this.getInventoryStackLimit())
        {
            stack.setCount(this.getInventoryStackLimit());
        }

        if (index == 0 && !flag)
        {
            this.totalCookTime = this.getCookTime(stack);
            this.cookTime = 0;
            this.markDirty();
        }
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    public boolean isBurning()
    {
        return this.burnTime > 0;
    }

    @SideOnly(Side.CLIENT)
    public static boolean isBurning(IInventory iInventory)
    {
        return iInventory.getField(0) > 0;
    }

    public int getCookTime(ItemStack input)
    {
        if(crystalEffectActive==2)
        {
            customCookTime =  200-(40*modifier);
        }
        else customCookTime = 200;

        return customCookTime;
    }

    private boolean canSmelt()
    {

        if(((ItemStack)this.handler.getStackInSlot(0)).isEmpty()) return false;//check if input slot is empty, if empty == false
        else//if input isnt empty
        {
            ItemStack result = FurnaceRecipes.instance().getSmeltingResult((ItemStack)this.handler.getStackInSlot(0));//if item cant be smelted then false and do nothing
            if(result.isEmpty())return false;
            else//if item can be smelted
            {
                ItemStack output = (ItemStack)this.handler.getStackInSlot(3);//output slot
                if(output.isEmpty())return true;//If output has nothing in it we're good to go
                if(!output.isItemEqual(result)) return false;//if output has items in it and they dont match what were smelting
                int resulting = output.getCount() + result.getCount();//resulting stack count equals current plus whats being smelted
                return resulting <= getInventoryStackLimit() && resulting <= output.getMaxStackSize();//if restuling is less then or == to the limit in a stack for items/max stack size then good to go
            }
        }
    }

    public void smeltItem()
    {
        if(this.canSmelt())
        {
            ItemStack input = this.handler.getStackInSlot(0);
            ItemStack result = FurnaceRecipes.instance().getSmeltingResult(input);
            ItemStack output = this.handler.getStackInSlot(3);

            if(crystalEffectActive==4 && result.getItem() instanceof ItemFood)
            {
                randomPotencyChance = 30*modifier;
                if(chanceTo())
                {
                    if(output.isEmpty())
                    {
                        //this.inventory.set(3,result.copy()).grow(1);//if output is empty then just copy in the resulting smelted item stack
                        this.handler.setStackInSlot(3,result.copy());
                        output.grow(result.copy().getCount());//just incase the output isnt 1 (double output)
                        consumeCrystalEnergy();
                    }
                    else if(output.getItem() == result.getItem())
                    {
                        output.grow(result.getCount()*2);
                        consumeCrystalEnergy();
                    } //if the output has items increese the stack count of that item(check for same is in canSmelt()
                }
                else
                {
                    if(output.isEmpty()) {
                        //this.inventory.set(3,result.copy());//if output is empty then just copy in the resulting smelted item stack
                        this.handler.setStackInSlot(3,result.copy());
                        consumeCrystalEnergy();
                    }
                    else if(output.getItem() == result.getItem()) {
                        output.grow(result.getCount());//if the output has items increese the stack count of that item(check for same is in canSmelt()
                        consumeCrystalEnergy();
                    }
                }
            }
            else if(crystalEffectActive==1)
            {
                randomPotencyChance = 15*modifier;
                if(chanceTo())
                {
                    if(output.isEmpty())
                    {
                        //this.inventory.set(3,result.copy()).grow(1);//if output is empty then just copy in the resulting smelted item stack
                        this.handler.setStackInSlot(3,result.copy());
                        output.grow(result.copy().getCount());//just incase the output isnt 1 (double output)
                        consumeCrystalEnergy();
                    }
                    else if(output.getItem() == result.getItem()) {
                        output.grow(result.getCount() + 1);//if the output has items increese the stack count of that item(check for same is in canSmelt()
                        consumeCrystalEnergy();
                    }
                }
                else
                {
                    if(output.isEmpty()) {
                        //this.inventory.set(3,result.copy());//if output is empty then just copy in the resulting smelted item stack
                        this.handler.setStackInSlot(3,result.copy());
                        consumeCrystalEnergy();
                    }
                    else if(output.getItem() == result.getItem()) {
                        output.grow(result.getCount());//if the output has items increese the stack count of that item(check for same is in canSmelt()
                        consumeCrystalEnergy();
                    }
                }
            }
            else if(crystalEffectActive==2)
            {
                if(output.isEmpty()) {
                    //this.inventory.set(3,result.copy()).grow(1);//if output is empty then just copy in the resulting smelted item stack
                    this.handler.setStackInSlot(3,result.copy());
                    output.grow(result.copy().getCount());//just incase the output isnt 1 (double output)
                    consumeCrystalEnergy();
                }
                else if(output.getItem() == result.getItem()) {
                    output.grow(result.getCount());//if the output has items increese the stack count of that item(check for same is in canSmelt()
                    consumeCrystalEnergy();
                }
            }
            else
            {
                if(output.isEmpty()) this.handler.setStackInSlot(3,result.copy());//if output is empty then just copy in the resulting smelted item stack
                else if(output.getItem() == result.getItem()) output.grow(result.getCount());//if the output has items increese the stack count of that item(check for same is in canSmelt()
            }
            input.shrink(1);
        }
    }

    public static int getItemBurnTime(ItemStack fuel)
    {
        if (fuel.isEmpty()) return 0;
        else
        {
            int burnTime = net.minecraftforge.event.ForgeEventFactory.getItemBurnTime(fuel);
            if (burnTime >= 0) return burnTime;
            Item item = fuel.getItem();

            if (item == Item.getItemFromBlock(Blocks.WOODEN_SLAB)) return 150;
            else if (item == Item.getItemFromBlock(Blocks.WOOL)) return 100;
            else if (item == Item.getItemFromBlock(Blocks.CARPET)) return 67;
            else if (item == Item.getItemFromBlock(Blocks.LADDER)) return 300;
            else if (item == Item.getItemFromBlock(Blocks.WOODEN_BUTTON)) return 100;
            else if (Block.getBlockFromItem(item).getDefaultState().getMaterial() == Material.WOOD) return 300;
            else if (item == Item.getItemFromBlock(Blocks.COAL_BLOCK)) return 16000;
            else if (item instanceof ItemTool && "WOOD".equals(((ItemTool)item).getToolMaterialName())) return 200;
            else if (item instanceof ItemSword && "WOOD".equals(((ItemSword)item).getToolMaterialName())) return 200;
            else if (item instanceof ItemHoe && "WOOD".equals(((ItemHoe)item).getMaterialName())) return 200;
            else if (item == Items.STICK) return 100;
            else if (item != Items.BOW && item != Items.FISHING_ROD)
            {
                if (item == Items.SIGN) return 200;
                else if (item == Items.COAL) return 1600;
                else if (item == Items.LAVA_BUCKET) return 20000;
                else if (item != Item.getItemFromBlock(Blocks.SAPLING) && item != Items.BOWL)
                {
                    if (item == Items.BLAZE_ROD) return 2400;
                    else if (item instanceof ItemDoor && item != Items.IRON_DOOR) return 200;
                    else return item instanceof ItemBoat ? 400 : 0;
                }
                else return 100;
            }
            else return 300;
        }
    }

    public static boolean isItemCrystal(ItemStack crystal)//finds out if item is instance of crystal
    {
        if(crystal.getItem() instanceof ItemCrystal)
        {
            return true;
        }
        else return false;
    }

    public ItemStack getCrystalIn()//finds out which crystal is being used in the slot
    {
        ItemStack stack = ItemStack.EMPTY;
        if(isItemCrystal(this.handler.getStackInSlot(1)))
        {
            stack = this.handler.getStackInSlot(1);
        }
        return stack;
    }

    public int getCrystalType()//gets meta of crystal
    {
        return getCrystalIn().getMetadata();
    }

    public void setCrystalEnergyLeft()
    {
        if(getCrystalIn().getItem() instanceof ItemCrystal)
        {
            crystalEffectActive = getCrystalType();
            crystalEnergyLeft = 32;
            this.handler.getStackInSlot(1).shrink(1);
            if(this.handler.getStackInSlot(4).isEmpty())
            {
                this.handler.setStackInSlot(4,new ItemStack(ItemRegistry.crystal,1,8));//returns depleated crystal
                //this.inventory.set(4,new ItemStack(ItemRegistry.crystal,1,8));
            }
            else
            {
                //this.inventory.get(4).grow(1);
                this.handler.getStackInSlot(4).grow(1);
            }

        }
    }

    public void consumeCrystal()
    {
        if(crystalEnergyLeft<=0)
        {
            if(!getCrystalIn().isEmpty())
            {
                setCrystalEnergyLeft();
            }
            else
            {
                customCookTime=200;
                crystalEffectActive=-1;
                crystalEnergyLeft=-1;
            }

        }

    }

    public void consumeCrystalEnergy()
    {
        crystalEnergyLeft--;
    }

    private boolean chanceTo()
    {
        Random rn = new Random();
        int chance = rn.nextInt(100);

        if(chance<=randomPotencyChance)
        {
            return true;
        }
        else return false;
    }

    public static boolean isItemFuel(ItemStack fuel)
    {
        return getItemBurnTime(fuel) > 0;
    }

    public boolean isUsableByPlayer(EntityPlayer player)
    {
        return this.world.getTileEntity(this.pos) != this ? false : player.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
    }

    public void openInventory(EntityPlayer player)
    {
    }

    public void closeInventory(EntityPlayer player)
    {
    }

    private boolean isItemSmeltable(ItemStack stack)
    {
        if(FurnaceRecipes.instance().getSmeltingResult(stack).isEmpty())
        {
            return false;
        }
        else return true;
    }



    public boolean isItemValidForSlot(int index, ItemStack stack)
    {
        switch (index)
        {
            case 0:
                return isItemSmeltable(stack);
            case 1:
                return isItemCrystal(stack);
            case 2:
                return isItemFuel(stack);
            case 3:
                return false;
            case 4:
                return false;
            default:
                return false;
        }
    }


    public int[] getSlotsForFace(EnumFacing side)
    {
        if (side == EnumFacing.DOWN)
        {
            return SLOTS_BOTTOM;
        }
        else
        {
            return side == EnumFacing.UP ? SLOTS_TOP : SLOTS_SIDES;
        }
    }

    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction)
    {
        return this.isItemValidForSlot(index, itemStackIn);
    }

    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction)
    {
        if (direction == EnumFacing.DOWN && index == 1)
        {
            Item item = stack.getItem();

            if (item != Items.WATER_BUCKET && item != Items.BUCKET)
            {
                return false;
            }
        }

        return true;
    }


    public String getGuiID()
    {
        return "dust.crystalfurnace";
    }

    public int getField(int id)
    {
        switch (id)
        {
            case  0:
                return this.burnTime;
            case 1:
                return this.currentBurnTime;
            case 2:
                return this.cookTime;
            case 3:
                return this.totalCookTime;
            case 4:
                return this.crystalEnergyLeft;
            case 5:
                return this.crystalEffectActive;
            default:
                return 0;
        }
    }


    @Override
    public void setField(int id, int value)
    {
        switch (id)
        {
            case  0:
                this.burnTime = value;
                break;
            case 1:
                this.currentBurnTime = value;
                break;
            case 2:
                this.cookTime = value;
                break;
            case 3:
                this.totalCookTime = value;
                break;
            case 4:
                this.crystalEnergyLeft = value;
                break;
            case 5:
                this.crystalEffectActive = value;
        }
    }

    @Override
    public int getFieldCount() {
        return 6;
    }

    @Override
    public void clear() {
        for(int i=0;i<=this.handler.getSlots();i++)
        {
            this.handler.setStackInSlot(i,ItemStack.EMPTY);
        }
    }

    private void updateBlock()
    {
        markDirty();
        IBlockState state = world.getBlockState(pos);
        world.notifyBlockUpdate(pos, state, state, 3);
        world.setBlockState(pos,state,3);
    }

    @Override
    public void update()
    {
        boolean flag = this.isBurning();//check for active
        boolean flag1 = false;

        if(this.isBurning()) --this.burnTime;//if active then decreese burntime

        consumeCrystal();


        if(!this.world.isRemote)
        {
            ItemStack stack = (ItemStack)this.handler.getStackInSlot(2);//fuel itemstack

            if(this.isBurning() || !stack.isEmpty() && !((ItemStack)this.handler.getStackInSlot(0)).isEmpty())//if active and fuel and input arnt empty
            {
                if(!this.isBurning() && this.canSmelt())//function checks and consumes fuel
                {
                    this.burnTime = getItemBurnTime(stack);
                    this.currentBurnTime = this.burnTime;

                    if(this.isBurning())
                    {
                        flag1 = true;

                        if(!stack.isEmpty())//if fuel stack isnt empty
                        {
                            Item item = stack.getItem();//set item == to the fuel in the stack
                            randomPotencyChance = 10*modifier;
                            if(crystalEffectActive==5 && chanceTo())
                            {
                                consumeCrystalEnergy();
                            }
                            else if(crystalEffectActive==0)
                            {
                                stack.shrink(1);
                                consumeCrystalEnergy();
                                int oldburntime= burnTime;
                                burnTime = oldburntime + (20*modifier);
                            }
                            else if(crystalEffectActive==5 )
                            {
                                stack.shrink(1);//consume a fuel
                                consumeCrystalEnergy();
                            }
                            else stack.shrink(1);

                            if(stack.isEmpty())
                            {
                                ItemStack item1 = item.getContainerItem(stack);
                                this.handler.setStackInSlot(2,item1);
                            }
                        }
                    }
                }

                if(this.isBurning() && this.canSmelt())
                {
                    ++this.cookTime;

                    if(this.cookTime >= this.totalCookTime)
                    {
                        this.cookTime = 0;
                        this.totalCookTime = this.getCookTime((ItemStack)this.handler.getStackInSlot(0));
                        this.smeltItem();
                        flag1 = true;
                    }
                }
                else this.cookTime = 0;



            }
            else if(!this.isBurning() && this.cookTime > 0)
            {
                this.cookTime = MathHelper.clamp(this.cookTime - 2, 0, this.totalCookTime);
            }

            if(flag != this.isBurning())
            {
                flag1 = true;
                BlockCrystalFurnace.setStateActive(this.isBurning(), this.world, this.pos);
            }
        }
        if(flag1) {this.markDirty();}
    }




    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("burntime",(short)this.burnTime);
        compound.setInteger("cooktime",(short)this.cookTime);
        compound.setInteger("totalcooktime",(short)this.totalCookTime);
        compound.setInteger("crystalenergy",(short)this.crystalEnergyLeft);
        compound.setInteger("crystaleffect",(short)this.crystalEffectActive);
        compound.setInteger("randomchance",(short)this.randomPotencyChance);
        compound.setTag("ItemStackInventoryHandler", this.handler.serializeNBT());
        //ItemStackHelper.saveAllItems(compound,this.inventory);//saves all item stack in the inventory to the nbt

        //if(this.hasCustomName()) compound.setString("customname",this.customName);
        return compound;

    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        //this.inventory = NonNullList.<ItemStack>withSize(this.getInventoryStackLimit(),ItemStack.EMPTY);//makes the inventory again
        //ItemStackHelper.loadAllItems(compound,this.inventory);//loads all the items back into the inventory
        this.handler.deserializeNBT(compound.getCompoundTag("ItemStackInventoryHandler"));
        this.burnTime = compound.getInteger("burntime");
        this.cookTime = compound.getInteger("cooktime");
        this.totalCookTime = compound.getInteger("totalcooktime");
        this.crystalEnergyLeft = compound.getInteger("crystalenergy");
        this.crystalEffectActive = compound.getInteger("crystaleffect");
        this.randomPotencyChance = compound.getInteger("randomchance");
        this.currentBurnTime = getItemBurnTime(this.handler.getStackInSlot(2));//sets burntime for slot 2

        //if(compound.hasKey("customname",8)) this.setCustomName(compound.getString("customname"));
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

    net.minecraftforge.items.IItemHandler handlerTop = new net.minecraftforge.items.wrapper.SidedInvWrapper(this, EnumFacing.UP);
    net.minecraftforge.items.IItemHandler handlerBottom = new net.minecraftforge.items.wrapper.SidedInvWrapper(this, EnumFacing.DOWN);
    net.minecraftforge.items.IItemHandler handlerSide = new net.minecraftforge.items.wrapper.SidedInvWrapper(this, EnumFacing.WEST);

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {

        if (facing != null && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            if (facing == EnumFacing.DOWN)
                return (T) handlerBottom;
            else if (facing == EnumFacing.UP)
                return (T) handlerTop;
            else
                return (T) handlerSide;


        if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return (T) this.handler;
        return super.getCapability(capability, facing);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return true;
        return super.hasCapability(capability, facing);
    }
}
