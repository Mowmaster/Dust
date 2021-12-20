package com.mowmaster.dust.Capabilities.Dust;

import com.mowmaster.dust.Items.DustMagicItemBase;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

/*
 * Flexible implementation of a Dust Storage object. NOT REQUIRED.
 */
public class DustTank implements IDustHandler, IDustTank{

    protected Predicate<DustMagic> validator;
    protected DustMagic dustMagic;
    protected int capacity;

    public DustTank(int capacity)
    {
        this(capacity, e -> true);
    }

    public DustTank(int capacity, Predicate<DustMagic> validator)
    {
        this.capacity = capacity;
        this.validator = validator;
    }

    public DustTank setCapacity(int capacity)
    {
        this.capacity = capacity;
        return this;
    }

    public DustTank setValidator(Predicate<DustMagic> validator)
    {
        if (validator != null) {
            this.validator = validator;
        }
        return this;
    }

    @Override
    public int getTanks() {
        return 1;
    }

    @NotNull
    @Override
    public DustMagic getDustMagic() {
        return dustMagic;
    }

    @Override
    public int getDustMagicAmount() {
        return dustMagic.getDustAmount();
    }

    @Override
    public int getTankCapacity() {
        return capacity;
    }

    @Override
    public boolean isDustValid(DustMagic dustIn) {
        return validator.test(dustIn);
    }

    @NotNull
    @Override
    public DustMagic getDustMagicInTank(int tank) {
        return getDustMagic();
    }

    @Override
    public int getTankCapacity(int tank) {
        return getTankCapacity();
    }

    @Override
    public boolean isDustValid(int tank, @NotNull DustMagic dustIn) {
        return isDustValid(dustIn);
    }

    public boolean isDustValid(int tank, @NotNull ItemStack stack) {
        if(stack.getItem() instanceof DustMagicItemBase)
        {
            isDustValid(tank, DustMagic.getDustMagicInItemStack(stack));
        }

        return false;
    }

    public DustTank readFromNBT(CompoundTag nbt) {
        this.dustMagic = DustMagic.getDustMagicInTag(nbt);
        return this;
    }

    public CompoundTag writeToNBT(CompoundTag nbt) {


        return DustMagic.setDustMagicInTag(nbt,this.dustMagic);
    }

    @Override
    public int fill(DustMagic dust, DustAction action) {
        if (dust.isEmpty() || !isDustValid(dust))
        {
            return 0;
        }
        if (action.simulate())
        {
            if (dustMagic.isEmpty())
            {
                return Math.min(capacity, dust.getDustAmount());
            }
            if (!dustMagic.isDustEqual(dust))
            {
                return 0;
            }
            return Math.min(capacity - dustMagic.getDustAmount(), dust.getDustAmount());
        }
        if (dustMagic.isEmpty())
        {
            dustMagic = new DustMagic(dust.getDustColor(), Math.min(capacity, dust.getDustAmount()));
            onContentsChanged();
            return dustMagic.getDustAmount();
        }
        if (!dustMagic.isDustEqual(dust))
        {
            return 0;
        }
        int filled = capacity - dustMagic.getDustAmount();

        if (dust.getDustAmount() < filled)
        {
            dustMagic.grow(dust.getDustAmount());
            filled = dust.getDustAmount();
        }
        else
        {
            dustMagic.setDustAmount(capacity);
        }
        if (filled > 0)
            onContentsChanged();
        return filled;
    }

    @NotNull
    @Override
    public DustMagic drain(DustMagic dust, DustAction action) {
        if (dust.isEmpty() || !dust.isDustEqual(dustMagic))
        {
            return new DustMagic(-1, 0);
        }
        return drain(dust.getDustAmount(), action);
    }

    @NotNull
    @Override
    public DustMagic drain(int maxDrain, DustAction action) {
        int drained = maxDrain;
        if (dustMagic.getDustAmount() < drained)
        {
            drained = dustMagic.getDustAmount();
        }
        DustMagic magic = new DustMagic(dustMagic.getDustColor(), drained);
        if (action.execute() && drained > 0)
        {
            dustMagic.shrink(drained);
            onContentsChanged();
        }
        return magic;
    }



    protected void onContentsChanged()
    {

    }
}
